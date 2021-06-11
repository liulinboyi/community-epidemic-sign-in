package com.companysign.service;

import com.companysign.po.Registration;

public interface RegistrationService {
    // 通过id查询
    public Registration findRegistrationById(Integer id);
}
