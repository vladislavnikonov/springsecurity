package ru.dyoung.springsecurity.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import ru.dyoung.springsecurity.entity.User;
import ru.dyoung.springsecurity.model.Role;
import ru.dyoung.springsecurity.model.Status;
import ru.dyoung.springsecurity.repository.UserRepository;

import java.util.UUID;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        log.info(String.format("User %s saved", user.getFirstName()));
    }

    public void addUser(User user) {
        User userFromDb = userRepository.findByLogin(user.getLogin());

        if (userFromDb != null) {
            return;
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setStatus(Status.BANNED);
        user.setRole(Role.USER);
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Matcha. Please, visit next link: http://localhost:8080/auth/%s",
                    user.getLogin(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }

    }

    public void activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            log.error("not found user");
            return;
        }

        user.setActivationCode(null);
        user.setStatus(Status.ACTIVE);

        userRepository.save(user);
        log.info("user updated");
    }

    public void sentCode(User userWithEmail) {
        User user = userRepository.findByEmail(userWithEmail.getEmail());

        if (user == null) {
            log.error("user not found");
            return;
        }

        user.setActivationCode(String.format("%6d", (int) (Math.random() * 1000000)));
        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" + "Welcome to Matcha. Please, enter code: %s to change password",
                    user.getLogin(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Update password", message);
        }

    }

    public void updatePassword(User userWithCode) {
        User user = userRepository.findByActivationCode(userWithCode.getActivationCode());

        if (user == null) {
            log.error("not found user");
            return;
        }

        user.setActivationCode(null);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        String hashedPassword = passwordEncoder.encode(userWithCode.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
        log.info("user updated");
    }
}
