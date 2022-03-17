package ru.dyoung.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import ru.dyoung.springsecurity.entity.User;
import ru.dyoung.springsecurity.model.Role;
import ru.dyoung.springsecurity.repository.UserRepository;
import ru.dyoung.springsecurity.service.MailSender;

import java.util.Collections;
import java.util.UUID;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
//        User user = userRepository.findByLogin(login).orElseThrow(() ->
//                new UsernameNotFoundException("User doesn't exists"));
        return SecurityUser.fromUser(user);
    }


}
