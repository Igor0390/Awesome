package com.awesome.park.mappers;

import com.awesome.park.dto.ActivityDto;
import com.awesome.park.entity.Activity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityMapper extends BaseMapper<ActivityDto, Activity> {
}

