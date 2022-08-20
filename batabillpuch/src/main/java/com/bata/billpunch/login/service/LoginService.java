package com.bata.billpunch.login.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bata.billpunch.login.model.UserModel;
import com.bata.billpunch.login.repository.UserRepository;
import com.bata.billpunch.login.service.impl.UserDetailsImpl;

@Transactional
@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UserRepository userInfoRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {

    	System.out.println("In LoginService loadUserByUsername==="+username);
    	
        UserModel user = userInfoRepository.findByUsername(username);
        
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
		/*
		 * return new
		 * org.springframework.security.core.userdetails.User(user.getUsername(),
		 * user.getPassword(), new ArrayList<>());
		 */
        return UserDetailsImpl.build(user);
    }

}
