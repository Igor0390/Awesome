package com.awesome.park.controller;


import com.awesome.park.dto.request.BookingRequestDto;
import com.awesome.park.dto.response.BookingResponseDto;
import com.awesome.park.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/booking")
@RequiredArgsConstructor
@Tag(name = "Контроллер букинга", description = "Этот контроллер управляет записью на каталку")
public class BookingController {

    private final BookingService bookingService;


    @Operation(summary = "Создание записи для пользователя",
            description = "Позволяет зарегистрировать пользователя на каталку")
    @PostMapping
    public ResponseEntity<String> createOrUpdateBooking(@RequestBody BookingRequestDto bookingDto) {
        return bookingService.createOrUpdateBooking(bookingDto.getPhone(), bookingDto.getName(), bookingDto.getTime());
    }


    @Operation(summary = "Получить запись по id",
            description = "Возвращает информацию о записавшемся пользователе по id")
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable @Parameter(description = "Id Идентификатор пользователя") UUID id) {
        BookingResponseDto responseDto = bookingService.getBookingById(id);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Получить все записи пользователей",
            description = "Возвращает информацию о всех записавшихся пользователях")
    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        List<BookingResponseDto> responseDtoList = bookingService.getAllBookings();
        return ResponseEntity.ok(responseDtoList);
    }

    @Operation(summary = "Удалить запись по id",
            description = "Удаляет запись о пользователе по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable @Parameter(description = "Id Идентификатор пользователя") UUID id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Запись успешно удалена");
    }
}