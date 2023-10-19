package com.awesome.park.repository;

import com.awesome.park.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByTelegramInfoUsername(String username);
}
