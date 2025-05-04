package com.springboot.api.expensetracker.repository;

import com.springboot.api.expensetracker.model.UserModel;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    //JPA handles basic database operations like save findById, findAll
    Optional<UserModel> findByEmail(String email);
}

