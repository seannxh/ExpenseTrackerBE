package com.springboot.api.expensetracker.repository;

import com.springboot.api.expensetracker.model.ExpenseModel;
import com.springboot.api.expensetracker.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<ExpenseModel, Long> {
    Optional<ExpenseModel> findByUser(UserModel user);
}
