package com.bryanching.hotslogsample.squidb;

import com.yahoo.squidb.annotations.ColumnSpec;
import com.yahoo.squidb.annotations.TableModelSpec;

/**
 * Created by bching on 4/12/16.
 */
@TableModelSpec(className="HotsLogUser", tableName="user")
public class HotsLogUserSpec {

    @ColumnSpec(constraints = "UNIQUE")
    public Long playerId;

    public String playerName;

}