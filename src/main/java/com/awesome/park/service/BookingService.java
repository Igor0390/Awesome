package com.awesome.park.service;

import com.awesome.park.dto.BookingDto;
import com.awesome.park.entity.Booking;
import com.awesome.park.mappers.BookingMapper;
import com.awesome.park.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;


    public List<BookingDto> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(bookingMapper::mapToDto)
                .collect(Collectors.toList());
    }
    public List<LocalDateTime> getAvailableBookingTimes() {
        // Создаем список всех временных слотов с интервалом в 30 минут
        List<LocalDateTime> allTimeSlots = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 0));
        while (startTime.isBefore(endTime)) {
            allTimeSlots.add(startTime);
            startTime = startTime.plusMinutes(30);
        }

        // Получаем список всех бронирований
        List<LocalDateTime> bookedTimeSlots = getAllBookingTimes();

        // Исключаем занятые временные слоты из списка всех временных слотов
        allTimeSlots.removeAll(bookedTimeSlots);

        return allTimeSlots;
    }


    public Optional<BookingDto> getBookingById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.map(bookingMapper::mapToDto);
    }

    public BookingDto createOrUpdateBooking(BookingDto bookingDto) {
        Booking booking = bookingMapper.mapToEntity(bookingDto);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.mapToDto(savedBooking);
    }

    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }

    private List<LocalDateTime> getAllBookingTimes() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(Booking::getBookingTime)
                .collect(Collectors.toList());
    }
}
