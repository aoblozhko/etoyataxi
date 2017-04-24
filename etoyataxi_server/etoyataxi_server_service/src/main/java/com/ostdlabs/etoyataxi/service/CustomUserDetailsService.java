package com.ostdlabs.etoyataxi.service;


import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import com.ostdlabs.etoyataxi.domain.CurrentUser;
import com.ostdlabs.etoyataxi.domain.Role;
import com.ostdlabs.etoyataxi.domain.RoleRepository;
import com.ostdlabs.etoyataxi.domain.User;
import com.ostdlabs.etoyataxi.domain.UserRepository;

import javax.inject.Inject;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Inject UserRepository userRepository;

    @Inject RoleRepository roleRepository;

    @Inject ApplicationContext applicationContext;

    @Override
    public CurrentUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findFirstByEmail(email);
        return new CurrentUser(user);
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof UserDetails) {
            user = ((CurrentUser)principal).getUser();
        }
        return user;
    }


    public User create(String username, String password) {
        User user = new User();
        user.setEmail(username);
        user.setUsername(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        user.setPasswordHash(hashedPassword);
        user = userRepository.save(user);
        Role userRole = roleRepository.findFirstByName("ROLE_USER");
        Set<Role> roles = new HashSet<Role>() {{
            add(userRole);
        }};
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }

    public User findOne(long id) {
        return userRepository.findOne(id);
    }

    public User findFirstById(long id) {
        return userRepository.findFirstById(id);
    }

    public User findFirstByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }


    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

}