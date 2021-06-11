package com.companysign.service.impl;

import com.companysign.dao.UserDao;
import com.companysign.po.Sign;
import com.companysign.po.User;
import com.companysign.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User findUserById(Integer id) {
        return this.userDao.findUserById(id);
    }

    @Override
    public User findUserSign(User user) {
        return this.userDao.findUserSign(user);
    }

    @Override
    public User login(User user) {
        return this.userDao.login(user);
    }

    @Override
    public User findUserByName(User user) {
        return this.userDao.findUserByName(user);
    }

    @Override
    public User addUser(User user) {
        return this.userDao.addUser(user);
    }

    @Override
    public void sign(Sign sign) {
        this.userDao.sign(sign);
    }

    @Override
    public Sign getRecentSignById(User user) {
        return this.userDao.getRecentSignById(user);
    }
}
