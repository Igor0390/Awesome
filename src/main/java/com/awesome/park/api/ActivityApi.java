package com.awesome.park.api;

import com.awesome.park.dto.ActivityDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Управление видами услуг (активностями)")
@RequestMapping("/api/activities")
public interface ActivityApi {

    @Operation(summary = "Получить список услуг (активностей)")
    @ApiResponse(responseCode = "200", description = "Список активностей")
    @GetMapping("/")
    ResponseEntity<List<ActivityDto>> getAllActivities();

    @Operation(summary = "Получить услугу (активность) по ID")
    @ApiResponse(responseCode = "200", description = "Активность найдена")
    @ApiResponse(responseCode = "404", description = "Активность не найдена")
    @GetMapping("/{id}")
    ResponseEntity<ActivityDto> getActivityById(@PathVariable Long id);

    @Operation(summary = "Создать новую  услугу (активность)")
    @ApiResponse(responseCode = "201", description = "Активность создана")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @PostMapping("/")
    ResponseEntity<ActivityDto> createActivity(@RequestBody ActivityDto activityDto);

    @Operation(summary = "Обновить информацию об услуге (активности)")
    @ApiResponse(responseCode = "200", description = "Информация об активности обновлена")
    @ApiResponse(responseCode = "404", description = "Активность не найдена")
    @PutMapping("/{id}")
    ResponseEntity<ActivityDto> updateActivity(@PathVariable Long id, @RequestBody ActivityDto activityDto);

    @Operation(summary = "Удалить услугу (активность) по ID")
    @ApiResponse(responseCode = "204", description = "Активность удалена")
    @ApiResponse(responseCode = "404", description = "Активность не найдена")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteActivity(@PathVariable Long id);
}
