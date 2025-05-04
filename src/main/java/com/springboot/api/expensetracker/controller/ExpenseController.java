package com.springboot.api.expensetracker.controller;

import com.springboot.api.expensetracker.model.ExpenseModel;
import com.springboot.api.expensetracker.model.UserModel;
import com.springboot.api.expensetracker.repository.ExpenseRepository;
import com.springboot.api.expensetracker.repository.UserRepository;
import com.springboot.api.expensetracker.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseService expenseService, ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseService = expenseService;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    //GET
    @GetMapping("/myexpense")
    public ResponseEntity<List<ExpenseModel>> getUserExpenses(Authentication authentication) {
        return ResponseEntity.ok(expenseService.getUserExpenses(authentication.getName()));
    }

    //GET ID for specific expenses.
    @GetMapping("/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        UserModel user = userRepository.findByEmail(email).orElseThrow();

        ExpenseModel expense = expenseRepository.findById(id)
                .filter(e -> e.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Expense not found or unauthorized"));

        return ResponseEntity.ok(expense);
    }


    //POST
    @PostMapping("/create")
    public ResponseEntity<ExpenseModel> createExpense(@RequestBody ExpenseModel expense, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.createExpense(expense, authentication.getName()));
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody ExpenseModel updatedExpense, Authentication authentication) {
        updatedExpense.setId(id); // Ensure the ID is set
        try {
            ExpenseModel updated = expenseService.updateExpense(updatedExpense, authentication.getName());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id, Authentication authentication) {
        try {
            expenseService.deleteExpense(id, authentication.getName());
            return ResponseEntity.ok("Expense deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

