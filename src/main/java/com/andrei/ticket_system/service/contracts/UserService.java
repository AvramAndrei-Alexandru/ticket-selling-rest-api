package com.andrei.ticket_system.service.contracts;

import com.andrei.ticket_system.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    List<User> findAllByRole(String role);
    User getById(int id);
    boolean saveOrUpdate(User user);
    boolean delete(int id);
    boolean existsByLogin(String login);
}
