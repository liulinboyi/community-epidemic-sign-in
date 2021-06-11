package com.companysign.dao;

import com.companysign.po.Registration;

public interface RegistrationDao {
    // 通过id查询
    public Registration findRegistrationById(Integer id);
}
