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
@Table(name = "srzcs")
public class SrzcsDatabase {
    public SrzcsDatabase() {
    }
    @Column(name = "_id" ,isId = true)
    private int id;

    @Column(name = "_month")
    private String month;

    @Column(name = "_sr")
    private String sr;

    @Column(name = "_zc")
    private String zc;

    @Column(name = "_other")
    private String other;

    @Override
    public String toString() {
        return "srzcsDatabase{" +
                "id=" + id +
                ", month='" + month + '\'' +
                ", sr='" + sr + '\'' +
                ", zc='" + zc + '\'' +
                ", other='" + other + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getZc() {
        return zc;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
