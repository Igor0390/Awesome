package com.awesome.park.mappers;

import com.awesome.park.dto.EmployeeDto;
import com.awesome.park.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends BaseMapper<EmployeeDto, Employee> {
}

