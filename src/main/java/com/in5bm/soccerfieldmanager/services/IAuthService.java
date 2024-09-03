package com.in5bm.soccerfieldmanager.services;

import com.in5bm.soccerfieldmanager.models.User;

public interface IAuthService {
    public User save(User user);
    public User login(String email);
}
