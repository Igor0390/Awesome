package com.awesome.park.repository;

import com.awesome.park.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer getCustomerByTelegramInfoId(Long telegramInfo_id);
    Customer getCustomerByPhoneNumber(String phoneNumber);
}
