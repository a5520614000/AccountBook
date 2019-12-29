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
@Table(name = "groups")
public class GroupsDatabase {
    public GroupsDatabase() {
    }

    @Column(name = "_id" ,isId = true)
    private int id;

    @Column(name = "_month")
    private String month;

    @Column(name = "_other")
    private String other;

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

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "GroupsDatabase{" +
                "id=" + id +
                ", month='" + month + '\'' +
                ", other='" + other + '\'' +
                '}';
    }
}
