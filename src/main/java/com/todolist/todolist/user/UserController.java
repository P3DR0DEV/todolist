package com.todolist.todolist.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @GetMapping("users")
  public List<UserModel> listUsers() {
    var users = this.userRepository.findAll();
    return users;
  }

  @GetMapping("users/{id}")
  public Optional<UserModel> fetchUserById(@PathVariable UUID id) {
    var user = this.userRepository.findById(id);

    return user;
  }

  @PostMapping("create")
  public UserModel createUser(@RequestBody UserModel userModel) {
    var user = this.userRepository.save(userModel);
    return user;
  }

  @DeleteMapping("users/{id}")
  public void delete(@PathVariable UUID id) {
    this.userRepository.deleteById(id);
  }

  @PutMapping("users/{id}")
  public UserModel update(@PathVariable UUID id, @RequestBody UserModel userModel) {
    var user = this.userRepository.findById(id).get();

    user.setName(userModel.getName());
    user.setPassword(userModel.getPassword());
    user.setUsername(userModel.getPassword());

    return this.userRepository.save(user);
  }
}