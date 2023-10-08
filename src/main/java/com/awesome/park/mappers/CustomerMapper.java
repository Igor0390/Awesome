package com.awesome.park.mappers;

import com.awesome.park.dto.CustomerDto;
import com.awesome.park.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends BaseMapper<CustomerDto, Customer> {
}

