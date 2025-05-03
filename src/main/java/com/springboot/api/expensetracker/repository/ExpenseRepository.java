package com.springboot.api.expensetracker.repository;

import com.springboot.api.expensetracker.model.ExpenseModel;
import com.springboot.api.expensetracker.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseModel, Long> {
    List<ExpenseModel> findByUser(UserModel user);

}
