package com.andrei.ticket_system.rest_controller.dto;

import com.andrei.ticket_system.entity.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

public class UserDTOMapper {

    public static User convertUserDTOToUserAddMode(UserDTO userDTO) {
        if(userDTO.getLogin() == null || userDTO.getPassword() == null || userDTO.getLogin().equals("") || userDTO.getPassword().equals("")) {
            return null;
        }
        User user = new User();
        user.setId(0);
        user.setLogin(userDTO.getLogin());
        user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userDTO.getPassword()));
        if(userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }
        return user;
    }

    public static User convertUserDTOToUserEditMode(UserDTO userDTO) {
        if(userDTO.getLogin() == null || userDTO.getPassword() == null || (userDTO.getLogin().equals("") && userDTO.getPassword().equals(""))) {
            return null;
        }
        //User wants to change only his password
        if(userDTO.getLogin().equals("")) {
            User user = new User();
            user.setLogin("");
            user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userDTO.getPassword()));
            return user;
        }//User wants to change only his login
        else if(userDTO.getPassword().equals("")) {
            User user = new User();
            user.setLogin(userDTO.getLogin());
            user.setPassword("");
            return user;
        }//User wants to change his username and password
        else {
            User user = new User();
            user.setLogin(userDTO.getLogin());
            user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userDTO.getPassword()));
            return user;
        }
    }
}
