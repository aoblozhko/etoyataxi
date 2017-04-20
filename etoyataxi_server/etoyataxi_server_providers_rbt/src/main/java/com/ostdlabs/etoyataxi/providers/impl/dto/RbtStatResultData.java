package com.ostdlabs.etoyataxi.providers.impl.dto;


import org.joda.time.DateTime;

import java.math.BigDecimal;

public class RbtStatResultData {

    private DateTime date;

    private int count;

    private int done;

    private BigDecimal sms;

    private BigDecimal avg;

    private BigDecimal sum;

    private int abuse;

    public RbtStatResultData(String[] data) {
        this.date = DateTime.parse(data[0]);
        this.count = Integer.parseInt(data[1]);
        this.done = Integer.parseInt(data[2]);
        this.sms = new BigDecimal(Double.parseDouble(data[3]));
        this.avg = new BigDecimal(Double.parseDouble(data[4]));
        this.sum = new BigDecimal(Double.parseDouble(data[5]));
        this.abuse = Integer.parseInt(data[6]);
    }

    public RbtStatResultData(DateTime date, int count, int done, BigDecimal sms, BigDecimal sum, int abuse) {
        this.date = date;
        this.count = count;
        this.done = done;
        this.sms = sms;
        this.sum = sum;
        this.abuse = abuse;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public BigDecimal getSms() {
        return sms;
    }

    public void setSms(BigDecimal sms) {
        this.sms = sms;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public int getAbuse() {
        return abuse;
    }

    public void setAbuse(int abuse) {
        this.abuse = abuse;
    }
}
