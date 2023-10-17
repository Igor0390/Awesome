package com.awesome.park.controller;

import com.awesome.park.api.BookingApi;
import com.awesome.park.dto.BookingDto;
import com.awesome.park.dto.request.BookingRequestDto;
import com.awesome.park.dto.response.BookingResponseDto;
import com.awesome.park.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BookingRestController implements BookingApi {
    private final BookingService bookingService;

    @Override
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        List<BookingResponseDto> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @Override
    public ResponseEntity<BookingDto> getBookingById(Long id) {
        Optional<BookingDto> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<String> createBooking(BookingRequestDto requestDto) {
        bookingService.createOrUpdateBookingAndCustomer(requestDto);
        return ResponseEntity.ok("Вы успешно записались на каталку");
    }

    @Override
    public ResponseEntity<String> updateBooking(Long id, BookingDto bookingDto) {
        bookingDto.setId(id);
        bookingService.createOrUpdateBookingAndCustomer(bookingDto);
        return ResponseEntity.ok("Вы успешно обновили запись");
    }

    @Override
    public ResponseEntity<String> deleteBooking(Long id) {
        bookingService.deleteBookingById(id);
        return ResponseEntity.ok("Запись успешно удалена");
    }
}




/*
    private final CustomerService customerService;


    @Operation(summary = "Создание записи для пользователя",
            description = "Позволяет зарегистрировать пользователя на каталку")
    @PostMapping
    public ResponseEntity<String> createOrUpdateBooking(@RequestBody BookingRequestDto bookingDto) {
        return customerService.createOrUpdateBooking(bookingDto.getPhone(), bookingDto.getName(), bookingDto.getTime());
    }


    @Operation(summary = "Получить запись по id",
            description = "Возвращает информацию о записавшемся пользователе по id")
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable @Parameter(description = "Id Идентификатор пользователя") UUID id) {
        BookingResponseDto responseDto = customerService.getBookingById(id);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Получить все записи пользователей",
            description = "Возвращает информацию о всех записавшихся пользователях")
    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        List<BookingResponseDto> responseDtoList = customerService.getAllBookings();
        return ResponseEntity.ok(responseDtoList);
    }

    @Operation(summary = "Удалить запись по id",
            description = "Удаляет запись о пользователе по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable @Parameter(description = "Id Идентификатор пользователя") UUID id) {
        customerService.deleteBooking(id);
        return ResponseEntity.ok("Запись успешно удалена");
    }*/