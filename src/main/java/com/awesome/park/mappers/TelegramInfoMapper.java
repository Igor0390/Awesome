package com.awesome.park.mappers;

import com.awesome.park.dto.EmployeeDto;
import com.awesome.park.dto.TelegramInfoDto;
import com.awesome.park.entity.Employee;
import com.awesome.park.entity.TelegramInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TelegramInfoMapper extends BaseMapper<TelegramInfoDto, TelegramInfo> {
}

