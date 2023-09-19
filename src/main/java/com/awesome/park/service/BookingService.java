package com.awesome.park.service;


import com.awesome.park.dto.response.BookingResponseDto;
import com.awesome.park.entity.Booking;
import com.awesome.park.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.awesome.park.util.TimeSlot.END_AT_WORK;
import static com.awesome.park.util.TimeSlot.START_AT_WORK;


@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

    @Transactional
    public ResponseEntity<String> createOrUpdateBooking(String phone, String name, Instant time) {

        // Retrieve all bookings with the given phone and name
        List<Booking> existingBookings = bookingRepository.findByPhoneAndName(phone, name);

        // Если записи нет, создаем новую
        if (existingBookings.isEmpty()) {
            createBooking(phone, name, time);
            return ResponseEntity.ok("Вы успешно записались на каталку");
        } else {
            // Проверяем каждую найденную запись
            for (Booking existingBooking : existingBookings) {
                Instant existingBookingTime = existingBooking.getTime();

                // Проверяем, если время совпадает
                if (existingBookingTime.equals(time)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Время записи совпадает");
                } else {
                    Instant bookingDate = time.truncatedTo(ChronoUnit.DAYS);
                    Instant existingBookingDate = existingBookingTime.truncatedTo(ChronoUnit.DAYS);

                    // Проверяем, если дата совпадает
                    if (existingBookingDate.equals(bookingDate)) {
                        existingBooking.setTime(time);
                        bookingRepository.save(existingBooking);
                        return ResponseEntity.ok("Время записи изменено");
                    }
                }
            }
        }

        // Создаем новую запись, если нет совпадений
        createBooking(phone, name, time);
        return ResponseEntity.ok("Запись создана");
    }



    private void createBooking(String phone, String name, Instant time) {
        Booking booking = new Booking();
        booking.setPhone(phone);
        booking.setName(name);
        booking.setTime(time);
        bookingRepository.save(booking);
    }

    public BookingResponseDto getBookingById(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Запись не найдена"));

        return mapToResponseDto(booking);
    }

    public List<BookingResponseDto> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return mapToResponseDtoList(bookings);
    }

    private List<BookingResponseDto> mapToResponseDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private BookingResponseDto mapToResponseDto(Booking booking) { //todo переделать на мапер
        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setPhone(booking.getPhone());
        responseDto.setName(booking.getName());
        responseDto.setTime(booking.getTime());
        responseDto.setId(String.valueOf(booking.getId()));

        return responseDto;
    }

    @Transactional
    public void deleteBooking(UUID id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Запись не найдена");
        }
    }

    public List<LocalTime> getAvailableStartTimes() {
        LocalDate currentDate = LocalDate.now();
        Instant startOfDay = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = currentDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Запросите из базы данных бронирования только для текущего дня
        List<Booking> bookings = bookingRepository.findByTimeBetween(startOfDay, endOfDay);

        // Создайте множество времен начала забронированных слотов
        Set<LocalTime> bookedStartTimes = new HashSet<>();
        for (Booking booking : bookings) {
            Instant bookingTime = booking.getTime();
            LocalTime startTime = bookingTime.atZone(ZoneId.systemDefault()).toLocalTime();
            bookedStartTimes.add(startTime);
        }

        // Определите доступные времена начала слотов
        List<LocalTime> availableStartTimes = new ArrayList<>();
        LocalTime currentTime = START_AT_WORK.getTime(); // Начальное время
        LocalTime endTime = END_AT_WORK.getTime(); // Конечное время

        while (currentTime.isBefore(endTime)) {
            if (!bookedStartTimes.contains(currentTime)) {
                availableStartTimes.add(currentTime);
            }
            int interval = 30;
            currentTime = currentTime.plusMinutes(interval); // Увеличьте на 30 минут
        }

        return availableStartTimes;
    }
}
