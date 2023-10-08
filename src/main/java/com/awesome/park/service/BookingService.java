package com.awesome.park.service;

import com.awesome.park.dto.BookingDto;
import com.awesome.park.entity.Booking;
import com.awesome.park.mappers.BookingMapper;
import com.awesome.park.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
