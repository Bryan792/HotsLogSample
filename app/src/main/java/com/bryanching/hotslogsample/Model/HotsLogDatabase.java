package com.bryanching.hotslogsample.Model;

import android.content.Context;

import com.bryanching.hotslogsample.squidb.HotsLogUser;
import com.bryanching.hotslogsample.squidb.MmrLog;
import com.yahoo.squidb.data.adapter.SQLiteDatabaseWrapper;
import com.yahoo.squidb.reactive.ReactiveSquidDatabase;
import com.yahoo.squidb.sql.Table;

import javax.inject.Inject;

/**
 * Created by bching on 4/12/16.
 */
public class HotsLogDatabase extends ReactiveSquidDatabase {

    /**
     * Create a new SquidDatabase
     *
     * @param context the Context, must not be null
     */
    @Inject
    public HotsLogDatabase(Context context) {
        super(context);
    }

    @Override
    public String getName() {
        return "hots_log.db";
    }

    @Override
    protected int getVersion() {
        return 1;
    }

    @Override
    protected Table[] getTables() {
        return new Table[]{HotsLogUser.TABLE, MmrLog.TABLE};
    }

    @Override
    protected boolean onUpgrade(SQLiteDatabaseWrapper db, int oldVersion, int newVersion) {
        return true;
    }
}
