package com.springboot.api.expensetracker.service;

import com.springboot.api.expensetracker.model.ExpenseModel;
import com.springboot.api.expensetracker.model.UserModel;
import com.springboot.api.expensetracker.repository.ExpenseRepository;
import com.springboot.api.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImplement implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseServiceImplement(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ExpenseModel createExpense(ExpenseModel expense, String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        String formattedCategory = formatAndValidateCategory(expense.getCategory());
        expense.setCategory(formattedCategory);
        expense.setUser(user);

        return expenseRepository.save(expense);
    }

    //Created a method to trasnform messy category input from user to clean
    private String formatAndValidateCategory(String rawCategory) {
        if (rawCategory == null || rawCategory.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }

        String formatted = rawCategory.trim().toLowerCase();
        formatted = formatted.substring(0, 1).toUpperCase() + formatted.substring(1);

        List<String> allowed = List.of(
                "Food",
                "Rent",
                "Utilities",
                "Entertainment",
                "Transport",
                "Healthcare",
                "Savings",
                "Other",
                "Groceries",
                "Dining Out",
                "Gas",
                "Public Transit",
                "Internet",
                "Phone",
                "Insurance",
                "Credit Card Payment",
                "Student Loan",
                "Mortgage",
                "Childcare",
                "Clothing",
                "Personal Care",
                "Subscriptions",
                "Travel",
                "Gym",
                "Pets",
                "Gifts",
                "Charity",
                "Taxes",
                "Home Maintenance",
                "Car Maintenance",
                "Education",
                "Investments",
                "Emergency Fund"
        );

        if (!allowed.contains(formatted)) {
            throw new IllegalArgumentException("Invalid category: " + formatted);
        }
        return formatted;
    }

    @Override
    public ExpenseModel updateExpense(ExpenseModel updatedExpense, String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        ExpenseModel existing = expenseRepository.findById(updatedExpense.getId())
                .filter(e -> e.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Unauthorized or expense not found"));

        existing.setTitle(updatedExpense.getTitle());
        existing.setAmount(updatedExpense.getAmount());
        existing.setDate(updatedExpense.getDate());
        existing.setCategory(formatAndValidateCategory(updatedExpense.getCategory()));

        return expenseRepository.save(existing);
    }

    @Override
    public void deleteExpense(Long id, String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        ExpenseModel expense = expenseRepository.findById(id)
                .filter(e -> e.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Unauthorized or expense not found"));

        expenseRepository.delete(expense);
    }

    @Override
    public List<ExpenseModel> getUserExpenses(String email) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return expenseRepository.findByUser(user);
    }


}
