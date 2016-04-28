package com.bryanching.hotslogsample.squidb;

import com.yahoo.squidb.annotations.TableModelSpec;

/**
 * Created by bching on 4/12/16.
 */
@TableModelSpec(className = "MmrLog", tableName = "mmr_log",
        tableConstraint = "FOREIGN KEY(playerId) references HotsLogUser(playerId) ON DELETE " +
                "CASCADE")
public class MmrLogSpec {

    public Long playerId;

    public Long timestamp;

    public Integer qmLeagueId;
    public Integer qmMmr;

    public Integer hlLeagueId;
    public Integer hlMmr;

    public Integer tlLeagueId;
    public Integer tlMmr;
}