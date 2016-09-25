package com.jipup.www.drive;

/**
 * Created by HP on 2016-07-05.
 */
/*커스텀내부에 저장되는 값들의 getter, setter*/
public class ListViewItem {

    private String tvDate ;
    private String tvTimer ;
    private String tvAvg ;
    private String tvMax ;

    public void setTvDate(String tvDate) {
        this.tvDate = tvDate;
    }
    public void setTvTimer(String tvTimer) {
        this.tvTimer = tvTimer;
    }
    public void setTvAvg(String tvAvg) {
        this.tvAvg = tvAvg;
    }
    public void setTvMax(String tvMax) {
        this.tvMax = tvMax;
    }

    public String getTvDate() {
        return tvDate;
    }
    public String getTvTimer() {
        return tvTimer;
    }
    public String getTvAvg() {
        return tvAvg;
    }
    public String getTvMax() {
        return tvMax;
    }
}