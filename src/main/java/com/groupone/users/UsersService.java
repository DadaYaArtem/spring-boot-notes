package com.groupone.users;

import com.groupone.mail.MailSender;
import com.groupone.mail.token.VerificationToken;
import com.groupone.mail.token.VerificationTokenRepository;
import com.groupone.notes.Notes;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final VerificationTokenRepository tokenRepository;
    private final MailSender mailSender;

    public void createUser(String email, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = new UserEntity();
        userEntity.setActive(false);
        userEntity.setEmail(email);
        userEntity.setPassword(encodedPassword);

        usersRepository.save(userEntity);
        createVerificationToken(userEntity);
    }

    private void sendMessage(UserEntity userEntity, VerificationToken token) {
        String message = String.format("Hello! \n" +
                        "Welcome to Notes. Please, visit next link: http://localhost:8080/activate/%s",
                token.getToken());
        mailSender.send(userEntity.getEmail(), "Activation code", message);
    }

    public void createVerificationToken(UserEntity user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        tokenRepository.save(verificationToken);
        sendMessage(user, verificationToken);
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

    public UserEntity findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public boolean activateUser(String code) {
        VerificationToken byToken = tokenRepository.findByToken(code);
        if (byToken == null) {
            return false;
        }
        byToken.setToken(null);

        UserEntity userEntity = byToken.getUserEntity();
        userEntity.setActive(true);
        usersRepository.save(userEntity);
        tokenRepository.delete(byToken);

        return true;
    }

}