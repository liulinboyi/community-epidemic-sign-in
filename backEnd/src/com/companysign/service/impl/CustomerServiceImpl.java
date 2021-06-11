package com.companysign.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.companysign.dao.CustomerDao;
import com.companysign.po.Customer;
import com.companysign.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    //注解注入CustomerDao
    @Autowired
    private CustomerDao customerDao;

    //查询客户
    public Customer findCustomerById(Integer id) {
        return this.customerDao.findCustomerById(id);
    }
}
