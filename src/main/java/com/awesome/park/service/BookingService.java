package com.awesome.park.service;

import com.awesome.park.dto.BookingDto;
import com.awesome.park.dto.CustomerDto;
import com.awesome.park.dto.request.BookingRequestDto;
import com.awesome.park.dto.response.BookingResponseDto;
import com.awesome.park.entity.Booking;
import com.awesome.park.entity.Customer;
import com.awesome.park.mappers.BookingMapper;
import com.awesome.park.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CustomerService customerService;

    public List<BookingResponseDto> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(booking -> {
            BookingResponseDto responseDto = new BookingResponseDto();
            responseDto.setId(booking.getId().toString());
            ZoneId kaliningradZone = ZoneId.of("GMT");
            responseDto.setTime(booking.getBookingTime().atZone(kaliningradZone).toInstant());
            responseDto.setActivityId(booking.getActivityId());
            responseDto.setActivityCount(booking.getActivityCount());
            // Получить информацию о пользователе по ID
            CustomerDto customer = customerService.getCustomerById(booking.getCustomerId());
            if (customer != null) {
                responseDto.setName(customer.getFirstName());
                responseDto.setSurname(customer.getLastName());
                responseDto.setPhone(customer.getPhoneNumber());
            }
            return responseDto;
        }).collect(Collectors.toList());
    }

    public List<LocalDateTime> getAllTimeSlots(Duration interval) {
        List<LocalDateTime> allTimeSlots = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 0));

        while (startTime.isBefore(endTime)) {
            allTimeSlots.add(startTime);
            startTime = startTime.plus(interval);
        }
        return allTimeSlots;
    }

    public List<LocalDateTime> getAvailableBookingTimes(Duration interval, Long activityId) {
        List<LocalDateTime> allTimeSlots = getAllTimeSlots(interval);

        List<LocalDateTime> bookedTimeSlots = getAllBookingTimesByActivityId(activityId);

        if (bookedTimeSlots != null && !bookedTimeSlots.isEmpty()) {
            allTimeSlots.removeAll(bookedTimeSlots);
        }
        return allTimeSlots;
    }

    public List<LocalDateTime> getAvailableBookingTimesForSupBoards(Duration interval, Long activityId, Integer maxCapacity, LocalDateTime selectedTime) {
        List<LocalDateTime> allTimeSlots = getAllTimeSlots(interval);
        int bookedSupBoardsCount = getSupBoardsCountAtTime(activityId, selectedTime);

        if (bookedSupBoardsCount >= maxCapacity) {
            Long capacity = Long.valueOf(maxCapacity);
            List<LocalDateTime> bookedTimeSlots = bookingRepository.getBookedTimeSlots(activityId, capacity);
            allTimeSlots.removeAll(bookedTimeSlots);
        }

        return allTimeSlots;
    }

    public Optional<BookingDto> getBookingById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.map(bookingMapper::mapToDto);
    }

    public void createOrUpdateBookingAndCustomer(BookingDto bookingDto) {
        Booking booking = bookingMapper.mapToEntity(bookingDto);
        Booking savedBooking = bookingRepository.save(booking);
        bookingMapper.mapToDto(savedBooking);
    }

    public void createOrUpdateBookingAndCustomer(BookingRequestDto requestDto) {
        // Поиск существующего клиента по номеру телефона
        Customer existingCustomer = customerService.getCustomerByPhoneNumber(requestDto.getPhone());

        if (existingCustomer == null) {
            // Если клиент не найден, создаем нового
            Customer customer = new Customer();
            customer.setFirstName(requestDto.getName());
            customer.setLastName(requestDto.getSurname());
            customer.setPhoneNumber(requestDto.getPhone());
            customerService.createOrUpdateCustomer(customer);

            // Создаем новое бронирование
            createBooking(requestDto, customer);
        } else {
            // Если клиент найден, обновляем его данные
            existingCustomer.setFirstName(requestDto.getName());
            existingCustomer.setLastName(requestDto.getSurname());
            customerService.createOrUpdateCustomer(existingCustomer);

            // Поиск существующего бронирования для клиента
            Booking existingBooking = bookingRepository.findByCustomerIdAndActivityId(existingCustomer.getId(), requestDto.getActivityId());

            if (existingBooking == null) {
                // Если бронирование не найдено, создаем новое
                createBooking(requestDto, existingCustomer);
            } else {
                // Если бронирование найдено, обновляем его данные
                existingBooking.setBookingTime(requestDto.getTime());
                existingBooking.setActivityId(requestDto.getActivityId());
                existingBooking.setActivityCount(requestDto.getActivityCount());
                bookingRepository.save(existingBooking);
            }
        }
    }

    private void createBooking(BookingRequestDto requestDto, Customer existingCustomer) {
        Booking booking = new Booking();
        booking.setBookingTime(requestDto.getTime());
        booking.setActivityId(requestDto.getActivityId());
        booking.setCustomerId(existingCustomer.getId());
        booking.setEmployeeId(null); // todo поправить
        booking.setActivityCount(requestDto.getActivityCount());
        bookingRepository.save(booking);
    }


    public void createOrUpdateBookingAndCustomer(Booking booking) {
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

    private List<LocalDateTime> getAllBookingTimesByActivityId(Long activityId) {
        List<Booking> bookings = bookingRepository.findByActivityId(activityId);
        return bookings.stream()
                .map(Booking::getBookingTime)
                .collect(Collectors.toList());
    }
}
