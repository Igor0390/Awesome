package com.awesome.park.service;

import com.awesome.park.dto.EmployeeDto;
import com.awesome.park.dto.TelegramInfoUsernameDto;
import com.awesome.park.entity.Employee;
import com.awesome.park.entity.TelegramInfo;
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

    public void saveInDataBase(Employee employee) {
        Employee existEmployee = employeeRepository.findByTelegramInfoUsername(employee.getTelegramInfo().getUsername());
        if (existEmployee != null) {
            employee.setId(existEmployee.getId());

            existEmployee.setFirstName(employee.getFirstName());
            existEmployee.setLastName(employee.getLastName());
            existEmployee.setRole(employee.getRole());
            existEmployee.setTelegramInfo(employee.getTelegramInfo());
        }
        employeeRepository.save(employee);
    }

    public EmployeeDto createOrUpdateEmployee(EmployeeDto employeeDto) {
        // Преобразуйте EmployeeDto в сущность Employee
        Employee employee = employeeMapper.mapToEntity(employeeDto);

        // Создайте новый объект TelegramInfo
        TelegramInfoUsernameDto telegramInfoDto = employeeDto.getTelegramInfo();
        TelegramInfo telegramInfo = new TelegramInfo();
        telegramInfo.setUsername(telegramInfoDto.getUsername());
        telegramInfo.setChatId(getChatIdByTelegramName(telegramInfoDto.getUsername()));

        // Сохраните или обновите сотрудника и связанный с ним TelegramInfo
        employee.setTelegramInfo(telegramInfo);
        Employee savedEmployee = employeeRepository.save(employee);

        // Преобразуйте сохраненного сотрудника обратно в DTO и верните его
        return employeeMapper.mapToDto(savedEmployee);
    }

    private Long getChatIdByTelegramName(String username) {
        return 1L;
    }


    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }
}
