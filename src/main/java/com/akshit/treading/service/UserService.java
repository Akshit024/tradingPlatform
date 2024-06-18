package com.akshit.treading.service;

import com.akshit.treading.domain.VerificationType;
import com.akshit.treading.modal.User;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws Exception;

    public User findUserProfileByEmail(String email) throws Exception;

    public User findUserProfileById(Long userId) throws Exception;

    User enableTwoFactorAuthentication(VerificationType verificationType, String sentTo, User user);

    User updatePassword(User user, String newPassword);

}
