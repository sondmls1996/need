package com.nf.vi.needfoodshipper.Constructor;

/**
 * Created by Minh Nhat on 5/2/2017.
 */

public class HistoryConstructor {
    public String stt;
    public String bcode;
    public String time;
    public String timeleft;
    public String reason;

    public HistoryConstructor(String stt, String bcode, String time, String timeleft, String reason) {
        this.stt = stt;
        this.bcode = bcode;
        this.time = time;
        this.timeleft = timeleft;
        this.reason = reason;
    }

    public HistoryConstructor() {
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getBcode() {
        return bcode;
    }

    public void setBcode(String bcode) {
        this.bcode = bcode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeleft() {
        return timeleft;
    }

    public void setTimeleft(String timeleft) {
        this.timeleft = timeleft;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
