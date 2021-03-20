package com.andrei.ticket_system.service;

import com.andrei.ticket_system.entity.Role;
import com.andrei.ticket_system.entity.User;
import com.andrei.ticket_system.repository.RoleRepository;
import com.andrei.ticket_system.repository.UserRepository;
import com.andrei.ticket_system.service.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserServiceImpl(@Autowired UserRepository userRepository, @Autowired RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllByRole(String role) {
        return userRepository.findAllByRole(role);
    }


    @Override
    public User getById(int id) {
        Optional<User> user =  userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public boolean saveOrUpdate(User user) {
        if(user != null) {
            if(user.getId() == 0 && !userRepository.existsByLogin(user.getLogin())) {
                Role role = new Role();
                role.setLogin(user.getLogin());
                if(user.getRole() != null && !user.getRole().equals("")) {
                    role.setRole(user.getRole());
                }else {
                    role.setRole("ROLE_CASHIER");
                }
                roleRepository.save(role);
                if(user.getRole() == null || user.getRole().equals("")) {
                    user.setRole("ROLE_CASHIER");
                }
            }
                userRepository.save(user);
                roleRepository.cleanUp();
                if(!roleRepository.existsByLogin(user.getLogin())) {
                    Role role = new Role();
                    role.setLogin(user.getLogin());
                    role.setRole(user.getRole());
                    roleRepository.save(role);
                }
                return true;
            }
        return false;
    }


    @Override
    public boolean delete(int id) {
        User user = getById(id);
        if(user != null && !user.getRole().equals("ROLE_ADMIN")) {
            userRepository.deleteById(id);
            roleRepository.cleanUp();
            return true;
        }
        return false;
}

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
}
