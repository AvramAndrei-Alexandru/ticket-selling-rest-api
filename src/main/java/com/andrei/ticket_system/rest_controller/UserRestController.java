package com.andrei.ticket_system.rest_controller;

import com.andrei.ticket_system.entity.User;
import com.andrei.ticket_system.rest_controller.dto.UserDTOMapper;
import com.andrei.ticket_system.rest_controller.dto.UserDTO;
import com.andrei.ticket_system.service.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private UserService userService;

    public UserRestController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAll();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "getUsers");
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(users);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<User> getOne(@PathVariable int id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "getOne");
        User user = userService.getById(id);
        if(user != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .body(user);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .headers(httpHeaders)
                .body(null);
    }

    @PostMapping(value = "/createCashier", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> createCashier(@RequestBody UserDTO userDTO) {
        User user = UserDTOMapper.convertUserDTOToUserAddMode(userDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "createCashier");
        boolean added =  userService.saveOrUpdate(user);
        if(!added) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .headers(httpHeaders)
                    .body(null);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(httpHeaders)
                .body(user);
    }

    @DeleteMapping(value = "/deleteUser/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable int id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "deleteUser");
        boolean deleted = userService.delete(id);
        if(!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .headers(httpHeaders)
                    .body(false);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(true);
    }

    @PutMapping(value = "/editUser/{id}")
    public ResponseEntity<User> editUser(@RequestBody UserDTO userDTO, @PathVariable int id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Method", "editUser");
        User existingUser = userService.getById(id);
        User user = UserDTOMapper.convertUserDTOToUserEditMode(userDTO);
        boolean editedSuccessfully = false;
        if(existingUser != null) {
            if(user != null) {
                user.setId(id);
                if(user.getLogin().equals("")) {
                    user.setLogin(existingUser.getLogin());
                }else if(user.getPassword().equals("")) {
                    user.setPassword(existingUser.getPassword());
                }
                //The user wants to change his name
                if(!user.getLogin().equals(existingUser.getLogin()) && !userService.existsByLogin(user.getLogin())) {
                    user.setRole(existingUser.getRole());
                    userService.saveOrUpdate(user);
                    editedSuccessfully = true;
                }else if(user.getLogin().equals(existingUser.getLogin())) {
                    user.setRole(existingUser.getRole());
                    userService.saveOrUpdate(user);
                    editedSuccessfully = true;
                }
            }
        }
        if(!editedSuccessfully) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .headers(httpHeaders)
                    .body(null);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(user);
    }
}
