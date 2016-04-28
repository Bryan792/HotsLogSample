package com.bryanching.hotslogsample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HotsLogProfile {

    @SerializedName("PlayerID")
    @Expose
    private Integer PlayerID;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("LeaderboardRankings")
    @Expose
    private List<LeaderboardRanking> LeaderboardRankings
            = new ArrayList<LeaderboardRanking>();

    /**
     * @return The PlayerID
     */
    public Integer getPlayerID() {
        return PlayerID;
    }

    /**
     * @param PlayerID The PlayerID
     */
    public void setPlayerID(Integer PlayerID) {
        this.PlayerID = PlayerID;
    }

    /**
     * @return The Name
     */
    public String getName() {
        return Name;
    }

    /**
     * @param Name The Name
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * @return The LeaderboardRankings
     */
    public List<LeaderboardRanking> getLeaderboardRankings() {
        return LeaderboardRankings;
    }

    /**
     * @param LeaderboardRankings The LeaderboardRankings
     */
    public void setLeaderboardRankings(List<LeaderboardRanking> LeaderboardRankings) {
        this.LeaderboardRankings = LeaderboardRankings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HotsLogProfile that = (HotsLogProfile) o;

        if (PlayerID != null ? !PlayerID.equals(that.PlayerID) : that.PlayerID != null)
            return false;
        if (Name != null ? !Name.equals(that.Name) : that.Name != null) return false;
        return LeaderboardRankings != null ? LeaderboardRankings.equals(that.LeaderboardRankings) : that.LeaderboardRankings == null;

    }

    @Override
    public int hashCode() {
        int result = PlayerID != null ? PlayerID.hashCode() : 0;
        result = 31 * result + (Name != null ? Name.hashCode() : 0);
        result = 31 * result + (LeaderboardRankings != null ? LeaderboardRankings.get(1).CurrentMMR : 0);
        return result;
    }

    public class LeaderboardRanking {

        @SerializedName("GameMode")
        @Expose
        private String GameMode;
        @SerializedName("LeagueID")
        @Expose
        private Integer LeagueID;
        @SerializedName("LeagueRank")
        @Expose
        private Integer LeagueRank;
        @SerializedName("CurrentMMR")
        @Expose
        private Integer CurrentMMR;

        /**
         * @return The GameMode
         */
        public String getGameMode() {
            return GameMode;
        }

        /**
         * @param GameMode The GameMode
         */
        public void setGameMode(String GameMode) {
            this.GameMode = GameMode;
        }

        /**
         * @return The LeagueID
         */
        public Integer getLeagueID() {
            return LeagueID;
        }

        /**
         * @param LeagueID The LeagueID
         */
        public void setLeagueID(Integer LeagueID) {
            this.LeagueID = LeagueID;
        }

        /**
         * @return The LeagueRank
         */
        public Integer getLeagueRank() {
            return LeagueRank;
        }

        /**
         * @param LeagueRank The LeagueRank
         */
        public void setLeagueRank(Integer LeagueRank) {
            this.LeagueRank = LeagueRank;
        }

        /**
         * @return The CurrentMMR
         */
        public Integer getCurrentMMR() {
            return CurrentMMR;
        }

        /**
         * @param CurrentMMR The CurrentMMR
         */
        public void setCurrentMMR(Integer CurrentMMR) {
            this.CurrentMMR = CurrentMMR;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LeaderboardRanking that = (LeaderboardRanking) o;

            if (GameMode != null ? !GameMode.equals(that.GameMode) : that.GameMode != null)
                return false;
            if (LeagueID != null ? !LeagueID.equals(that.LeagueID) : that.LeagueID != null)
                return false;
            if (LeagueRank != null ? !LeagueRank.equals(that.LeagueRank) : that.LeagueRank != null)
                return false;
            return CurrentMMR != null ? CurrentMMR.equals(that.CurrentMMR) : that.CurrentMMR ==
                    null;
        }

        @Override
        public int hashCode() {
            int result = GameMode != null ? GameMode.hashCode() : 0;
            result = 31 * result + (LeagueID != null ? LeagueID.hashCode() : 0);
            result = 31 * result + (LeagueRank != null ? LeagueRank.hashCode() : 0);
            result = 31 * result + (CurrentMMR != null ? CurrentMMR.hashCode() : 0);
            return result;
        }
    }
}
