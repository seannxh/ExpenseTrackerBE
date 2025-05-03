package com.springboot.api.expensetracker.service;

import com.springboot.api.expensetracker.model.ExpenseModel;

import java.util.List;

public interface ExpenseService {
    ExpenseModel createExpense(ExpenseModel expense, String email);
    ExpenseModel updateExpense(ExpenseModel expense, String email);
    void deleteExpense(Long id, String email);
    List<ExpenseModel> getUserExpenses(String email);
}

