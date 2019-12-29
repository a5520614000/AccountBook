package com.cgj.accountbook.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * @author onono
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
    @Table(name = "accounts")
public class AccountDatabase {

    @Column(name = "_id",isId = true)
    private int id;

    @Column(name = "_type")
    private String type;

    @Column(name = "_money")
    private String money;

    @Column(name = "_time")
    private String time;

    @Column(name = "_month")
    private String month;

    @Column(name = "_week")
    private String week;

    @Column(name = "_mark")
    private String mark;

    @Column(name = "_other")
    private String other;


    public AccountDatabase() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "AccountDatabase{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", money='" + money + '\'' +
                ", time='" + time + '\'' +
                ", month='" + month + '\'' +
                ", week='" + week + '\'' +
                ", mark='" + mark + '\'' +
                ", other='" + other + '\'' +
                '}';
    }
}
