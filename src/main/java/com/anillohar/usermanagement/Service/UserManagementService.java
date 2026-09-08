package com.anillohar.usermanagement.Service;

import com.anillohar.usermanagement.bindings.ActivateAccount;
import com.anillohar.usermanagement.bindings.Login;
import com.anillohar.usermanagement.bindings.User;

import java.util.List;

public interface UserManagementService {

    boolean saveUser(User user);

    boolean activateUserAcc(ActivateAccount activateAcc);

    List<User> getAllUsers();

    User getUserById(Integer userId);

    boolean deleteUserById(Integer userId);

    boolean changeAccountStatus(Integer userId, String accStatus);

    String login(Login login);

    String forgotPwd(String email);
}