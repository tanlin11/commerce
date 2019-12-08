package com.tl.commerce.service;

import com.tl.commerce.domain.User;


public interface UserService {

    User getUserByLoginPhone(String login_phone);


    int save(User user);
}
