package com.awesome.park.service;

import com.awesome.park.dto.EmployeeDto;
import com.awesome.park.entity.Employee;
import com.awesome.park.mappers.EmployeeMapper;
import com.awesome.park.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employeeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeDto> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::mapToDto);
    }

    public EmployeeDto createOrUpdateEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.mapToEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.mapToDto(savedEmployee);
    }

    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }
}
