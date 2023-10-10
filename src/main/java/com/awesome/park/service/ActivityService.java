package com.awesome.park.service;

import com.awesome.park.dto.ActivityDto;
import com.awesome.park.entity.Activity;
import com.awesome.park.mappers.ActivityMapper;
import com.awesome.park.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public List<ActivityDto> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activities.stream()
                .map(activityMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<ActivityDto> getActivityById(Long id) {
        return activityRepository.findById(id).map(activityMapper::mapToDto);
    }

    public ActivityDto createOrUpdateActivity(ActivityDto activityDto) {
        Activity activity = activityMapper.mapToEntity(activityDto);
        activity = activityRepository.save(activity);
        return activityMapper.mapToDto(activity);
    }

    public void deleteActivityById(Long id) {
        activityRepository.deleteById(id);
    }
}

