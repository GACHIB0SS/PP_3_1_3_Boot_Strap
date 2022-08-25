package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AdminService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.UserValidator;

import java.security.Principal;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final RoleService roleService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final UserValidator userValidator;


    @Autowired
    public AdminController(AdminService adminService, RoleService roleService,
                           UserService userService, PasswordEncoder passwordEncoder,
                           UserValidator userValidator) {
        this.adminService = adminService;
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
    }

    @GetMapping() //+
    public String index(Model model, Principal principal) {
        model.addAttribute("allUsers", adminService.showUsers());
        model.addAttribute("roleList", roleService.findAll());
        model.addAttribute("currentUser", userService.findByEmail(principal.getName()));
        model.addAttribute("newUser", new User());
        return "admin/index";
    }


    @PostMapping("/save") //+
    public String addUser(@ModelAttribute("user") User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        adminService.addUser(user);
        return "redirect:/admin";
    }


    @PatchMapping("/{id}/update") //+
    public String updateUser(@ModelAttribute("userToUpdate") User userToUpdate,
                             @RequestParam("roles") Set<Role> roles, BindingResult bindingResult) {
        userValidator.validate(userToUpdate, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        userToUpdate.setRoles(roles);
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        adminService.updateUser(userToUpdate);
        return "redirect:/admin";
    }

    @DeleteMapping("{id}/delete") //+
    public String deleteUser(@PathVariable("id") Long id) {
        adminService.deleteUserById(id);
        return "redirect:/admin";
    }


}