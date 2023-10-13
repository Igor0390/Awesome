package com.awesome.park.service;

import com.awesome.park.dto.BookingDto;
import com.awesome.park.entity.Booking;
import com.awesome.park.mappers.BookingMapper;
import com.awesome.park.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    public List<LocalDateTime> getAvailableBookingTimes(Duration interval, Long activityId) {
        // Создаем список всех временных слотов с указанным интервалом
        List<LocalDateTime> allTimeSlots = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 0));

        while (startTime.isBefore(endTime)) {
            allTimeSlots.add(startTime);
            startTime = startTime.plus(interval);
        }

        // Получаем список всех бронирований
        List<LocalDateTime> bookedTimeSlots = getAllBookingTimesByActivityId(activityId);

        // Если bookedTimeSlots пустой или null, возвращаем полный список временных слотов
        if (bookedTimeSlots == null || bookedTimeSlots.isEmpty()) {
            return allTimeSlots;
        } else {
            // Исключаем занятые временные слоты из списка всех временных слотов
            allTimeSlots.removeAll(bookedTimeSlots);
            return allTimeSlots;
        }
    }


    private List<LocalDateTime> getAllBookingTimesByActivityId(Long activityId) {
        List<Booking> bookings = bookingRepository.findByActivityId(activityId);
        return bookings.stream()
                .map(Booking::getBookingTime)
                .collect(Collectors.toList());
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

    public void createOrUpdateBooking(Booking booking) {
        // Проверяем, существует ли запись с таким же пользователем и временем
        Booking existingBooking = getByCustomerId(booking.getCustomerId());

        if (existingBooking != null) {
            // Если запись существует, обновляем ее данные
            existingBooking.setEmployeeId(booking.getEmployeeId());
            // Другие обновления полей
            bookingRepository.save(existingBooking);
        } else {
            // Если запись не существует, создаем новую
            bookingRepository.save(booking);
        }
    }

    public void createOrUpdateSupBoardBooking(Booking booking) {
        // пишем все сапборды в табличку потому что кол-во сапов соответствуют кол-ву записей в таблице
        bookingRepository.save(booking);
    }


    public Booking getByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    public Booking getByCustomerIdAndActivityId(Long customerId, Long activityId) {
        return bookingRepository.findByCustomerIdAndActivityId(customerId, activityId);
    }


    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }


    public int getSupBoardsCountAtTime(Long activityId, LocalDateTime selectedTime) {
        return bookingRepository.countBookedSupBoardsAtTime(activityId, selectedTime);
    }
}
