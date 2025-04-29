package com.springboot.api.expensetracker.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ExpenseModel {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private Double amount;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    private UserModel user;
}

