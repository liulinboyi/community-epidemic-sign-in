package com.companysign.service.impl;

import com.companysign.dao.RegistrationDao;
import com.companysign.po.Registration;
import com.companysign.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService {
    //注解注入CustomerDao
    @Autowired
    private RegistrationDao registrationDao;

    @Override
    public Registration findRegistrationById(Integer id) {
        return this.registrationDao.findRegistrationById(id);
    }
}
