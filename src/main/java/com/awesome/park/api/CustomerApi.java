package com.awesome.park.api;

import com.awesome.park.dto.CustomerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Управление клиентами")
@RequestMapping("/api/customers")
public interface CustomerApi {

    @Operation(summary = "Получить всех клиентов")
    @ApiResponse(responseCode = "200", description = "Список клиентов")
    @GetMapping("/")
    ResponseEntity<List<CustomerDto>> getAllCustomers();

    @Operation(summary = "Получить клиента по ID")
    @ApiResponse(responseCode = "200", description = "Клиент найден")
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @GetMapping("/{id}")
    ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id);

    @Operation(summary = "Создать нового клиента")
    @ApiResponse(responseCode = "201", description = "Клиент создан")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @PostMapping("/")
    ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto);

    @Operation(summary = "Обновить информацию о клиенте")
    @ApiResponse(responseCode = "200", description = "Информация о клиенте обновлена")
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @PutMapping("/{id}")
    ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto);

    @Operation(summary = "Удалить клиента по ID")
    @ApiResponse(responseCode = "204", description = "Клиент удален")
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCustomer(@PathVariable Long id);
}
