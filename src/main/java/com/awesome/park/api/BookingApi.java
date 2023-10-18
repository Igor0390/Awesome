package com.awesome.park.api;

import com.awesome.park.dto.BookingDto;
import com.awesome.park.dto.request.BookingRequestDto;
import com.awesome.park.dto.response.BookingResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Управление бронированиями")
@RequestMapping("/api/bookings")
public interface BookingApi {

    @Operation(summary = "Получить список бронирований")
    @ApiResponse(responseCode = "200", description = "Список бронирований")
    @GetMapping("/")
    ResponseEntity<List<BookingResponseDto>> getAllBookings();

    @Operation(summary = "Получить бронирование по ID")
    @ApiResponse(responseCode = "200", description = "Бронирование найдено")
    @ApiResponse(responseCode = "404", description = "Бронирование не найдено")
    @GetMapping("/{id}")
    ResponseEntity<BookingDto> getBookingById(@PathVariable Long id);

    @Operation(summary = "Получить список доступных временных слотов")
    @ApiResponse(responseCode = "200", description = "Время найдено")
    @ApiResponse(responseCode = "404", description = "Время не найдено")
    @GetMapping("/times")
     List<LocalDateTime> getAvailableWakeBookingTimes();

    @Operation(summary = "Создать новое бронирование")
    @ApiResponse(responseCode = "201", description = "Бронирование создано")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @PostMapping("/")
    ResponseEntity<String> createBooking(@RequestBody BookingRequestDto requestDto);

    @Operation(summary = "Обновить информацию о бронировании")
    @ApiResponse(responseCode = "200", description = "Информация о бронировании обновлена")
    @ApiResponse(responseCode = "404", description = "Бронирование не найдено")
    @PutMapping("/{id}")
    ResponseEntity<String> updateBooking(@PathVariable Long id, @RequestBody BookingDto bookingDto);

    @Operation(summary = "Удалить бронирование по ID")
    @ApiResponse(responseCode = "204", description = "Бронирование удалено")
    @ApiResponse(responseCode = "404", description = "Бронирование не найдено")
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteBooking(@PathVariable Long id);
}
