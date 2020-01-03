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
@Table(name = "income_record")
public class IncomeRecordDatabase {
    @Override
    public String toString() {
        return "IncomeRecordDatabase{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", income='" + income + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public IncomeRecordDatabase() {
    }

    @Column(name = "_id",isId = true)
    private  int id;

    @Column(name = "_time")
    private  String time;

    @Column(name = "_income")
    private  String income;

    @Column(name = "_detail")
    private  String detail;

}
