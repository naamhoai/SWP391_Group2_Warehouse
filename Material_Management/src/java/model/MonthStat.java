package model;

public class MonthStat {
    private String month;
    private int count;      // cho số lượng yêu cầu
    private long value;     // cho giá trị mua hàng

    public MonthStat() {
    }

    public MonthStat(String month, int count, long value) {
        this.month = month;
        this.count = count;
        this.value = value;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
} 