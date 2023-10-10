package com.awesome.park.controller;

import com.awesome.park.api.EmployeeApi;
import com.awesome.park.dto.EmployeeDto;
import com.awesome.park.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class EmployeeRestController implements EmployeeApi {
    private final EmployeeService employeeService;

    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    public ResponseEntity<EmployeeDto> getEmployeeById(Long id) {
        Optional<EmployeeDto> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<EmployeeDto> createEmployee(EmployeeDto employeeDto) {
        EmployeeDto createdEmployee = employeeService.createOrUpdateEmployee(employeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    public ResponseEntity<EmployeeDto> updateEmployee(Long id, EmployeeDto employeeDto) {
        EmployeeDto updatedEmployee = employeeService.createOrUpdateEmployee(employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    public ResponseEntity<Void> deleteEmployee(Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }
}
