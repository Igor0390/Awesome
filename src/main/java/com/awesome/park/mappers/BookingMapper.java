package com.awesome.park.mappers;

import com.awesome.park.dto.BookingDto;
import com.awesome.park.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper extends BaseMapper<BookingDto, Booking> {
}

