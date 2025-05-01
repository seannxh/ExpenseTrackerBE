package com.springboot.api.expensetracker.controller;

import com.springboot.api.expensetracker.model.ExpenseModel;
import com.springboot.api.expensetracker.model.UserModel;
import com.springboot.api.expensetracker.repository.ExpenseRepository;
import com.springboot.api.expensetracker.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    // GET all expenses for logged-in user
    @GetMapping("/myexpense")
    public ResponseEntity<List<ExpenseModel>> getUserExpenses(Authentication authentication) {
        String email = authentication.getName();
        UserModel user = userRepository.findByEmail(email).orElseThrow();
        List<ExpenseModel> expenses = expenseRepository.findByUser(user);
        return ResponseEntity.ok(expenses);
    }


    // POST to create a new expense
    @PostMapping("/create")
    public ResponseEntity<ExpenseModel> createExpense(@RequestBody ExpenseModel expense, Authentication authentication) {
        String email = authentication.getName();
        UserModel user = userRepository.findByEmail(email).orElseThrow();
        expense.setUser(user); // attach the current user to the expense
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseRepository.save(expense));

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody ExpenseModel updatedExpense, Authentication authentication) {
        String email = authentication.getName();
        UserModel user = userRepository.findByEmail(email).orElseThrow();

        return expenseRepository.findById(id)
                .filter(expense -> expense.getUser() != null && expense.getUser().getId().equals(user.getId()))
                .<ResponseEntity<?>>map(expense -> {
                    expense.setTitle(updatedExpense.getTitle());
                    expense.setAmount(updatedExpense.getAmount());
                    expense.setDate(updatedExpense.getDate());
                    expenseRepository.save(expense);
                    return ResponseEntity.ok(expense);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Expense not found or not authorized"));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        UserModel user = userRepository.findByEmail(email).orElseThrow();

        return expenseRepository.findById(id)
                .filter(expense -> expense.getUser() != null && expense.getUser().getId().equals(user.getId()))
                .map(expense -> {
                    expenseRepository.delete(expense);
                    return ResponseEntity.ok("Expense deleted");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found or not authorized"));
    }


}
