package com.awesome.park.entity.mappers;/*
package com.awesomepark.awesomeparkbackdev.entity.mappers;


import com.awesomepark.awesomeparkbackdev.dto.response.BookingResponseDto;
import com.awesomepark.awesomeparkbackdev.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Mapper(componentModel = "spring")
@Component
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "id", target = "id")
    BookingResponseDto toBookingResponseDto(Booking booking);

    @Mapping(source = "id", target = "id")
    Booking bookingResponseDtoToBooking(BookingResponseDto bookingResponseDto);

}
*/
