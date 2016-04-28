package com.bryanching.hotslogsample.Presenter;

import com.bryanching.hotslogsample.HotsLogApplication;
import com.bryanching.hotslogsample.Model.HotsLogDatabase;
import com.bryanching.hotslogsample.Model.HotsLogProfile;
import com.bryanching.hotslogsample.squidb.HotsLogUser;
import com.bryanching.hotslogsample.squidb.MmrLog;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.TaskParams;
import com.yahoo.squidb.data.SquidCursor;
import com.yahoo.squidb.sql.Query;
import com.yahoo.squidb.sql.TableStatement;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by bching on 4/14/16.
 */
public class UpdateService extends GcmTaskService {

    @Inject
    HotsLogDatabase mHotsLogDatabase;

    public UpdateService() {
        super();
    }

    @Override
    public void onInitializeTasks() {
        GcmNetworkManager.getInstance(this)
                .schedule(new PeriodicTask
                        .Builder()
                        .setService(UpdateService.class)
                        .setPeriod(TimeUnit.DAYS.toSeconds(1))
                        .setTag("tag")
                        .build());
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        getHotsLogProfiles(mHotsLogDatabase);
        return GcmNetworkManager.RESULT_SUCCESS;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HotsLogApplication.getHotsLogComponent().inject(this);
    }

    private static HashSet<Long> getHotsLogIdsFromDatabase(HotsLogDatabase hotsLogDatabase) {
        HashSet<Long> hotsLogIds = new HashSet<>();
        SquidCursor<HotsLogUser> hotsLogUserSquidCursor = hotsLogDatabase.query(HotsLogUser
                .class, Query.select
                (HotsLogUser.PLAYER_ID).from(HotsLogUser.TABLE));
        if (hotsLogUserSquidCursor != null) {
            try {
                while (hotsLogUserSquidCursor.moveToNext()) {
                    hotsLogIds.add(hotsLogUserSquidCursor.get(HotsLogUser.PLAYER_ID));
                }
            } finally {
                hotsLogUserSquidCursor.close();
            }
        }
        return hotsLogIds;
    }

    public static Subscription getHotsLogProfiles(HotsLogDatabase hotsLogDatabase) {
        return Observable.merge(
                Observable.merge(
                        Observable.from(getHotsLogIdsFromDatabase(hotsLogDatabase)),
                        Observable.from(HotsLogPresenter.hotsLogIds))
                        .distinct()
                        .flatMap(HotsLogPresenter::getHotsLogRankings),
                Observable.from(HotsLogPresenter.bNetIds)
                        .flatMap(HotsLogPresenter::getHotsLogRankings))
                .distinct()
                .toSortedList((hotsLogProfile, hotsLogProfile2) -> hotsLogProfile2
                        .getLeaderboardRankings().get(1).getCurrentMMR() - hotsLogProfile
                        .getLeaderboardRankings().get(1).getCurrentMMR())
                .subscribeOn(Schedulers.newThread())
                .subscribe(hotsLogProfiles -> {
                    hotsLogDatabase.beginTransaction();
                    try {
                        Long timestamp = System.currentTimeMillis();
                        for (HotsLogProfile hotsLogProfile : hotsLogProfiles) {
                            HotsLogUser hotsLogUser = new HotsLogUser();
                            hotsLogUser.setPlayerId(Long.valueOf(hotsLogProfile.getPlayerID()));
                            hotsLogUser.setPlayerName(hotsLogProfile.getName());
                            if (!hotsLogDatabase.persistWithOnConflict(hotsLogUser,
                                    TableStatement.ConflictAlgorithm.REPLACE)) {
                                return;
                            }

                            MmrLog mmrLog = new MmrLog();
                            mmrLog.setPlayerId(Long.valueOf(hotsLogProfile.getPlayerID()));
                            mmrLog.setTimestamp(timestamp);
                            HotsLogProfile.LeaderboardRanking leaderboardRanking =
                                    hotsLogProfile.getLeaderboardRankings().get(0);
                            mmrLog.setQmLeagueId(leaderboardRanking.getLeagueID());
                            mmrLog.setQmMmr(leaderboardRanking.getCurrentMMR());
                            leaderboardRanking = hotsLogProfile.getLeaderboardRankings().get(1);
                            mmrLog.setHlLeagueId(leaderboardRanking.getLeagueID());
                            mmrLog.setHlMmr(leaderboardRanking.getCurrentMMR());
                            leaderboardRanking = hotsLogProfile.getLeaderboardRankings().get(2);
                            mmrLog.setTlLeagueId(leaderboardRanking.getLeagueID());
                            mmrLog.setTlMmr(leaderboardRanking.getCurrentMMR());
                            if (!hotsLogDatabase.persist(mmrLog)) {
                                return;
                            }
                        }
                        HotsLogPresenter.hotsLogIds.clear();
                        HotsLogPresenter.bNetIds.clear();
                        hotsLogDatabase.setTransactionSuccessful();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        hotsLogDatabase.endTransaction();
                    }
                }, Throwable::printStackTrace);
    }

}
