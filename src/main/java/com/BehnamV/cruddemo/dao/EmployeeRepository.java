package com.BehnamV.cruddemo.dao;

import com.BehnamV.cruddemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    // No Need for Implementation Class
}
