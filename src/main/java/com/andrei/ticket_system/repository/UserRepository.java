package com.andrei.ticket_system.repository;

import com.andrei.ticket_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByRole(String role);
    boolean existsByLogin(String login);
}
