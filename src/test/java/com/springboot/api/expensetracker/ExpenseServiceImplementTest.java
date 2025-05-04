package com.springboot.api.expensetracker;

import com.springboot.api.expensetracker.service.ExpenseServiceImplement;
import org.junit.jupiter.api.Test;

import com.springboot.api.expensetracker.model.ExpenseModel;
import com.springboot.api.expensetracker.model.UserModel;
import com.springboot.api.expensetracker.repository.ExpenseRepository;
import com.springboot.api.expensetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplementTest {

	@Mock
	private ExpenseRepository expenseRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ExpenseServiceImplement expenseService;

	private UserModel user;

	@BeforeEach
	void setup() {
		user = new UserModel();
		user.setId(1L);
		user.setEmail("test@example.com");
		user.setName("Test User");
	}

	@Test
	void createExpense_success() {
		ExpenseModel expense = new ExpenseModel();
		expense.setCategory("Food");

		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
		when(expenseRepository.save(any(ExpenseModel.class))).thenAnswer(i -> i.getArgument(0));

		ExpenseModel result = expenseService.createExpense(expense, "test@example.com");

		assertEquals("Food", result.getCategory());
		assertEquals(user, result.getUser());
	}

	@Test
	void updateExpense_success() {
		ExpenseModel existing = new ExpenseModel();
		existing.setId(1L);
		existing.setUser(user);

		ExpenseModel updated = new ExpenseModel();
		updated.setId(1L);
		updated.setTitle("Updated");
		updated.setAmount(100.0);
		updated.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		updated.setCategory("Rent");

		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
		when(expenseRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(expenseRepository.save(any(ExpenseModel.class))).thenAnswer(i -> i.getArgument(0));

		ExpenseModel result = expenseService.updateExpense(updated, "test@example.com");

		assertEquals("Updated", result.getTitle());
		assertEquals(100.0, result.getAmount());
	}

	@Test
	void deleteExpense_success() {
		ExpenseModel existing = new ExpenseModel();
		existing.setId(1L);
		existing.setUser(user);

		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
		when(expenseRepository.findById(1L)).thenReturn(Optional.of(existing));

		expenseService.deleteExpense(1L, "test@example.com");

		verify(expenseRepository).delete(existing);
	}

	@Test
	void getUserExpenses_success() {
		List<ExpenseModel> mockExpenses = Arrays.asList(
				new ExpenseModel(), new ExpenseModel()
		);

		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
		when(expenseRepository.findByUser(user)).thenReturn(mockExpenses);

		List<ExpenseModel> result = expenseService.getUserExpenses("test@example.com");

		assertEquals(2, result.size());
	}
}

