package com.awesome.park.controller;

import com.awesome.park.api.ActivityApi;
import com.awesome.park.dto.ActivityDto;
import com.awesome.park.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ActivityRestController implements ActivityApi {
    private final ActivityService activityService;

    @Override
    public ResponseEntity<List<ActivityDto>> getAllActivities() {
        List<ActivityDto> activities = activityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }

    @Override
    public ResponseEntity<ActivityDto> getActivityById(Long id) {
        Optional<ActivityDto> activity = activityService.getActivityById(id);
        return activity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ActivityDto> createActivity(ActivityDto activityDto) {
        ActivityDto createdActivity = activityService.createOrUpdateActivity(activityDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdActivity);
    }

    @Override
    public ResponseEntity<ActivityDto> updateActivity(Long id, ActivityDto activityDto) {
        Optional<ActivityDto> existingActivity = activityService.getActivityById(id);
        if (existingActivity.isPresent()) {
            ActivityDto updatedActivity = activityService.createOrUpdateActivity(activityDto);
            return ResponseEntity.ok(updatedActivity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteActivity(Long id) {
        Optional<ActivityDto> existingActivity = activityService.getActivityById(id);
        if (existingActivity.isPresent()) {
            activityService.deleteActivityById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
