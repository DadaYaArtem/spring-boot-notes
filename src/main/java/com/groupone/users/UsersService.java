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

        UserEntity userEntity = new UserEntity();
        userEntity.setActive(false);
        userEntity.setEmail(email);
        userEntity.setPassword(encodedPassword);
        userEntity.setActivationCode(UUID.randomUUID().toString());

        usersRepository.save(userEntity);

        String message = String.format("Hello! \n" +
                        "Welcome to Notes. Please, visit next link: http://localhost:8080/activate/%s",
                userEntity.getActivationCode());
        mailSender.send(userEntity.getEmail(), "Activation code", message);
    }

    public UserEntity getUserByUuid(UUID userUuid) {
        return usersRepository.getReferenceById(userUuid);
    }

    public void updateUserByUuid(UUID userUuid, String email, String password, List<Notes> notes) {
        UserEntity userEntity = getUserByUuid(userUuid);
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setNotesList(notes);
        usersRepository.save(userEntity);
    }

    public void deleteUserByUuid(UUID userUuid) {
        usersRepository.deleteById(userUuid);
    }

    public UserEntity findByEmail(String email){
        return usersRepository.findByEmail(email);
    }

    public boolean activateUser(String code) {
        UserEntity user = usersRepository.findByActivationCode(code);
        user.setActivationCode(null);
        user.setActive(true);
        usersRepository.save(user);
        return true;
    }
}