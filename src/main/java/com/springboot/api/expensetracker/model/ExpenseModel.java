package com.springboot.api.expensetracker.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class ExpenseModel {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private Double amount;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "BIGINT")
    private UserModel user;

    @Override
    public String toString() {
        return "ExpenseModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", user=" + user +
                '}';
    }
}

