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
@Table(name = "limits")
public class LimitsDatabase {
    public LimitsDatabase() {
    }

    @Column(name = "_id",isId = true)
    private int id;

    @Column(name = "_type")
    private String type;

    @Column(name = "_used")
    private String used;

    @Column(name = "_progress")
    private String progress ;

    @Column(name = "_limit")
    private String limit;

    @Column(name = "_color")
    private String color;

    @Override
    public String toString() {
        return "LimitsDatabase{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", used='" + used + '\'' +
                ", progress='" + progress + '\'' +
                ", limit='" + limit + '\'' +
                ", color='" + color + '\'' +
                '}';
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

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
