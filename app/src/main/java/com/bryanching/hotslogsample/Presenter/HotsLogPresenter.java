package com.bryanching.hotslogsample.Presenter;

import android.content.Context;

import com.bryanching.hotslogsample.HotsLogApplication;
import com.bryanching.hotslogsample.HotsLogFragment;
import com.bryanching.hotslogsample.Model.HotsLogDatabase;
import com.bryanching.hotslogsample.Model.HotsLogProfile;
import com.bryanching.hotslogsample.squidb.HotsLogUser;
import com.bryanching.hotslogsample.squidb.MmrLog;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.yahoo.squidb.data.SquidCursor;
import com.yahoo.squidb.sql.Function;
import com.yahoo.squidb.sql.Property;
import com.yahoo.squidb.sql.Property.LongProperty;
import com.yahoo.squidb.sql.Query;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by bching on 4/9/16.
 */
public class HotsLogPresenter {

    public Property.IntegerProperty sortProperty;
    private HotsLogFragment hotsLogFragment;
    @Inject
    HotsLogDatabase hotsLogDatabase;

    private Context mContext;

    @Inject
    public HotsLogPresenter(Context context, HotsLogFragment hl) {
        HotsLogApplication.getHotsLogComponent().inject(this);
        mContext = context;
        hotsLogFragment = hl;
        hotsLogDatabase.observeTable(MmrLog.TABLE, true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(table -> {
                    updateHlf();
                });
        GcmNetworkManager.getInstance(mContext)
                .schedule(new PeriodicTask
                        .Builder()
                        .setService(UpdateService.class)
                        .setPeriod(TimeUnit.DAYS.toSeconds(1))
                        .setTag("tag")
                        .build());
    }

    static HashSet<String> bNetIds = new HashSet<>();
    static HashSet<Long> hotsLogIds = new HashSet<>();

    // @formatter:off
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://www.hotslogs.com/API/")
            .client(new OkHttpClient())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    // @formatter:on

    private void updateHlf() {
        Query query = Query.select(HotsLogUser.PROPERTIES)
                .selectMore(MmrLog.PROPERTIES)
                .from(HotsLogUser.TABLE)
                .leftJoin(MmrLog.TABLE, HotsLogUser.PLAYER_ID.eq(MmrLog.PLAYER_ID))
                .groupBy(HotsLogUser.PLAYER_ID)
                .orderBy(MmrLog.TIMESTAMP.desc());
        if (sortProperty != null) {
            query = Query.fromSubquery(query, "subquery");
            Property.IntegerProperty mmr = query.getTable().qualifyField(sortProperty);
            Property.IntegerProperty hlMmr = query.getTable().qualifyField(MmrLog.HL_MMR);
            query.orderBy(mmr.desc(), hlMmr.desc());
        }

        SquidCursor<HotsLogUser> squidCursor = hotsLogDatabase.query(HotsLogUser.class, query);
        hotsLogFragment.swapCursor(squidCursor);

        LongProperty maxId = LongProperty.fromFunction(Function.max(MmrLog.TIMESTAMP),
                "maxTimestamp");
        SquidCursor<MmrLog> cursor = hotsLogDatabase.query(MmrLog.class, Query.select(maxId)
                .from(MmrLog.TABLE));
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    Long maxTimestamp = cursor.get(maxId);
                    if (maxTimestamp != null) {
                        hotsLogFragment.setLastUpdated("Last Updated: " + new Date(maxTimestamp).toString());
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }

    public void sortByQm() {
        sortProperty = MmrLog.QM_MMR;
        updateHlf();
    }

    public void sortByHl() {
        sortProperty = MmrLog.HL_MMR;
        updateHlf();
    }

    public void sortByTl() {
        sortProperty = MmrLog.TL_MMR;
        updateHlf();
    }

    public void onInsertId(String id) {
        if (id.contains("#")) {
            bNetIds.add(id.replace('#', '_'));
        } else {
            try {
                Long hotsLogId = Long.valueOf(id);
                hotsLogIds.add(Long.valueOf(hotsLogId));
            } catch (NumberFormatException ignored) {

            }
        }
        updateOneOff();
    }

    public static Observable<HotsLogProfile> getHotsLogRankings(String battleNetId) {

        HotsLogService service = retrofit.create(HotsLogService.class);
        return service.getByBNet(battleNetId);
    }

    public static Observable<HotsLogProfile> getHotsLogRankings(Long hotsLogId) {

        HotsLogService service = retrofit.create(HotsLogService.class);
        return service.getByHotsLog(hotsLogId);
    }

    public interface HotsLogService {

        //https://www.hotslogs.com/API/Players/1/Bryan792_1986
        @GET("Players/1/{user}")
        Observable<HotsLogProfile> getByBNet(@Path("user") String user);

        @GET("Players/{id}")
        Observable<HotsLogProfile> getByHotsLog(@Path("id") Long id);

    }

    public void onResume() {
        updateOneOff();
    }

    public void onUpdatePressed() {
        updateOneOff();
    }

    private void updateOneOff() {
        UpdateService.getHotsLogProfiles(hotsLogDatabase);
    }

    public void addKnownUsers() {
        hotsLogIds.add(3944819L);
        hotsLogIds.add(Long.valueOf("6486420"));
        hotsLogIds.add(Long.valueOf("5476145"));
        hotsLogIds.add(Long.valueOf("5462387"));
        hotsLogIds.add(Long.valueOf("2230236"));
        hotsLogIds.add(Long.valueOf("5238907"));
        bNetIds.add("Bryan792_1986");
    }
}
