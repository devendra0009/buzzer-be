package com.davendra.buzzer.serviceImpl;

import com.davendra.buzzer.entity.UserModel;
import com.davendra.buzzer.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> userModel = userRepo.findByEmail(username);
        if (userModel.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        return new User(userModel.get().getEmail(), userModel.get().getPassword(), grantedAuthorityList);
    }
}
