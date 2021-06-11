package com.companysign.dao;

import com.companysign.po.Sign;
import com.companysign.po.User;

public interface UserDao {
    public User findUserById(Integer id);

    public User findUserSign(User user);

    public User login(User user);

    public User findUserByName(User user);

    public User addUser(User user);

    public void sign(Sign sign);

    public Sign getRecentSignById(User user);
}
