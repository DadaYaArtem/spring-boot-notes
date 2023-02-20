package com.groupone.users;

import com.groupone.mail.MailSender;
import com.groupone.notes.Notes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final MailSender mailSender;

    public void createUser(String email, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        Users users = new Users();
        users.setActive(false);
        users.setEmail(email);
        users.setPassword(encodedPassword);
        users.setActivationCode(UUID.randomUUID().toString());

        usersRepository.save(users);

        String message = String.format("Hello! \n" +
                        "Welcome to Notes. Please, visit next link: http://localhost:8080/activate/%s",
                users.getActivationCode());
        mailSender.send(users.getEmail(), "Activation code", message);
    }

    public Users getUserByUuid(UUID userUuid) {
        return usersRepository.getReferenceById(userUuid);
    }

    public void updateUserByUuid(UUID userUuid, String email, String password, List<Notes> notes) {
        Users users = getUserByUuid(userUuid);
        users.setEmail(email);
        users.setPassword(password);
        users.setNotesList(notes);
        usersRepository.save(users);
    }

    public void deleteUserByUuid(UUID userUuid) {
        usersRepository.deleteById(userUuid);
    }

    public Users findByEmail(String email){
        return usersRepository.findByEmail(email);
    }

    public boolean activateUser(String code) {
        Users user = usersRepository.findByActivationCode(code);
        if (user == null){
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);

        usersRepository.save(user);
        System.out.println(user);
        return true;
    }
}