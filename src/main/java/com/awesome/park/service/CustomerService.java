package com.awesome.park.service;

import com.awesome.park.dto.CustomerDto;
import com.awesome.park.entity.Customer;
import com.awesome.park.mappers.CustomerMapper;
import com.awesome.park.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customerMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<CustomerDto> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::mapToDto);
    }

    public CustomerDto createOrUpdateCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.mapToEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.mapToDto(savedCustomer);
    }

    public void createOrUpdateCustomer(Customer customer) {
        customerRepository.save(customer);

    }




    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }

    public Customer getCustomerByTelegramInfoId(Long telegramInfoId) {
       return customerRepository.getCustomerByTelegramInfoId(telegramInfoId);
    }
}



/*
    @Transactional
    public ResponseEntity<String> createOrUpdateBooking(Customer customer, Instant time) {
        List<Customer> existingCustomers = customerRepository.findByPhoneNumberAndFirstNameAndLastName(
                customer.getPhoneNumber(), customer.getFirstName(), customer.getLastName());

        // Если клиент не найден, создаем нового
        if (existingCustomers.isEmpty()) {
            createCustomer(customer, time);
            return ResponseEntity.ok("Вы успешно записались на активность");
        } else {
            // Проверяем каждого найденного клиента
            for (Customer existingCustomer : existingCustomers) {
                // Проверяем, если дата и время совпадают
                if (existingCustomer.hasBookingAtTime(time)) {
                    return ResponseEntity.badRequest().body("У вас уже есть бронь на это время");
                }
            }
        }

        // Создаем новую бронь для клиента
        createBooking(customer, time);
        return ResponseEntity.ok("Бронь успешно создана");
    }

    private void createCustomer(Customer customer, Instant time) {
        customer.getBookings().add(time);
        customerRepository.save(customer);
    }

    private void createBooking(Customer customer, Instant time) {
        customer.getBookings().add(time);
        customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public ResponseEntity<String> deleteCustomer(Long customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return ResponseEntity.ok("Клиент удален успешно");
        } else {
            return ResponseEntity.badRequest().body("Клиент не найден");
        }
    }

    public Set<Instant> getCustomerBookings(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Клиент не найден"));

        return customer.getBookings();
    }

    @Transactional
    public ResponseEntity<String> cancelBooking(Long customerId, Instant bookingTime) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Клиент не найден"));

        if (customer.getBookings().remove(bookingTime)) {
            return ResponseEntity.ok("Бронь успешно отменена");
        } else {
            return ResponseEntity.badRequest().body("Бронь не найдена");
        }
    }*/

  /*  private final BookingRepository bookingRepository;
    @Transactional
    public ResponseEntity<String> createOrUpdateBooking(String phone, String name, Instant time) {


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
    }*/
