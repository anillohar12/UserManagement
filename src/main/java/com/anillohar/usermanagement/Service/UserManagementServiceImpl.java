package com.anillohar.usermanagement.Service;

import com.anillohar.usermanagement.Repo.UserMasterRepo;
import com.anillohar.usermanagement.Utils.EmailUtils;
import com.anillohar.usermanagement.bindings.ActivateAccount;
import com.anillohar.usermanagement.bindings.Login;
import com.anillohar.usermanagement.bindings.User;
import com.anillohar.usermanagement.entity.Usermaster;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final UserMasterRepo userMasterRepo;
    private final EmailUtils emailUtils;

    @Autowired
    public UserManagementServiceImpl(UserMasterRepo userMasterRepo,
                                     EmailUtils emailUtils) {
        this.userMasterRepo = userMasterRepo;
        this.emailUtils = emailUtils;
    }

    @Override
    public boolean saveUser(User user) {

        Usermaster entity = new Usermaster();

        BeanUtils.copyProperties(user, entity);

        String tempPwd = generateRandomPwd();

        entity.setPassword(tempPwd);
        entity.setAccStatus("In-Active");

        Usermaster savedUser = userMasterRepo.save(entity);

        if (savedUser.getUserId() != null) {

            String subject = "Your Account Registration - Activation Required";

            String body = "Hi " + user.getFullname() + ",\n\n"
                    + "Your account has been created successfully.\n"
                    + "Temporary Password: " + tempPwd + "\n\n"
                    + "Please use this temporary password to activate your account.";

            return emailUtils.sendEmail(
                    user.getEmail(),
                    subject,
                    body
            );
        }

        return false;
    }

    @Override
    public boolean activateUserAcc(ActivateAccount activateAcc) {

        Usermaster entity = new Usermaster();

        entity.setEmail(activateAcc.getEmail());
        entity.setPassword(activateAcc.getTempPwd());

        Example<Usermaster> example = Example.of(entity);

        List<Usermaster> users = userMasterRepo.findAll(example);

        if (users.isEmpty()) {
            return false;
        }

        Usermaster userMaster = users.get(0);

        userMaster.setPassword(activateAcc.getNewPwd());
        userMaster.setAccStatus("Active");

        userMasterRepo.save(userMaster);

        return true;
    }

    @Override
    public List<User> getAllUsers() {

        List<Usermaster> entities = userMasterRepo.findAll();

        List<User> users = new ArrayList<>();

        for (Usermaster entity : entities) {

            User user = new User();

            BeanUtils.copyProperties(entity, user);

            users.add(user);
        }

        return users;
    }

    @Override
    public User getUserById(Integer userId) {

        Optional<Usermaster> optionalUser =
                userMasterRepo.findById(userId);

        if (optionalUser.isPresent()) {

            User user = new User();

            BeanUtils.copyProperties(
                    optionalUser.get(),
                    user
            );

            return user;
        }

        return null;
    }

    @Override
    public boolean deleteUserById(Integer userId) {

        if (userMasterRepo.existsById(userId)) {

            userMasterRepo.deleteById(userId);

            return true;
        }

        return false;
    }

    @Override
    public boolean changeAccountStatus(Integer userId,
                                       String accStatus) {

        Optional<Usermaster> optionalUser =
                userMasterRepo.findById(userId);

        if (optionalUser.isPresent()) {

            Usermaster userMaster = optionalUser.get();

            userMaster.setAccStatus(accStatus);

            userMasterRepo.save(userMaster);

            return true;
        }

        return false;
    }

    @Override
    public String login(Login login) {

        Usermaster entity = new Usermaster();

        entity.setEmail(login.getEmail());
        entity.setPassword(login.getPassword());

        Example<Usermaster> example = Example.of(entity);

        List<Usermaster> users =
                userMasterRepo.findAll(example);

        if (users.isEmpty()) {
            return "Invalid Credentials";
        }

        Usermaster userMaster = users.get(0);

        if ("Active".equalsIgnoreCase(userMaster.getAccStatus())) {
            return "SUCCESS";
        }

        return "Account not activated";
    }

    @Override
    public String forgotPwd(String email) {

        Usermaster userMaster =
                userMasterRepo.findByEmail(email);

        if (userMaster == null) {
            return "Invalid Email";
        }

        String subject =
                "Recover Password - User Management System";

        String body =
                "Hi " + userMaster.getFullname() + ",\n\n"
                        + "Your account password is: "
                        + userMaster.getPassword()
                        + "\n\n"
                        + "Please keep your credentials secure.";

        boolean isSent =
                emailUtils.sendEmail(
                        email,
                        subject,
                        body
                );

        if (isSent) {
            return "Password sent to your registered email";
        }

        return "Failed to send email. Please try again later.";
    }

    private String generateRandomPwd() {

        String alphaNumeric =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                        + "abcdefghijklmnopqrstuvwxyz"
                        + "0123456789";

        StringBuilder sb = new StringBuilder();

        SecureRandom random = new SecureRandom();

        int length = 6;

        for (int i = 0; i < length; i++) {

            int index =
                    random.nextInt(alphaNumeric.length());

            sb.append(alphaNumeric.charAt(index));
        }

        return sb.toString();
    }
}