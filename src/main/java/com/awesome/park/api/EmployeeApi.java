package com.awesome.park.api;

import com.awesome.park.dto.EmployeeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Управление сотрудниками")
@RequestMapping("/api/employees")
public interface EmployeeApi {

    @Operation(summary = "Получить всех сотрудников")
    @ApiResponse(responseCode = "200", description = "Список сотрудников")
    @GetMapping("/")
    ResponseEntity<List<EmployeeDto>> getAllEmployees();

    @Operation(summary = "Получить сотрудника по ID")
    @ApiResponse(responseCode = "200", description = "Сотрудник найден")
    @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    @GetMapping("/{id}")
    ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id);

    @Operation(summary = "Создать нового сотрудника")
    @ApiResponse(responseCode = "201", description = "Сотрудник создан")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @PostMapping("/")
    ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto);

    @Operation(summary = "Обновить информацию о сотруднике")
    @ApiResponse(responseCode = "200", description = "Информация о сотруднике обновлена")
    @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    @PutMapping("/{id}")
    ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto);

    @Operation(summary = "Удалить сотрудника по ID")
    @ApiResponse(responseCode = "204", description = "Сотрудник удален")
    @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEmployee(@PathVariable Long id);
}
