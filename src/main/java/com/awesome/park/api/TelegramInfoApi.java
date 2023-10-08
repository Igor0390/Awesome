package com.awesome.park.api;

import com.awesome.park.dto.TelegramInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Управление информацией о Telegram")
@RequestMapping("/api/telegram-info")
public interface TelegramInfoApi {

    @Operation(summary = "Получить всю информацию о Telegram")
    @ApiResponse(responseCode = "200", description = "Список информации о Telegram")
    @GetMapping("/")
    ResponseEntity<List<TelegramInfoDto>> getAllTelegramInfos();

    @Operation(summary = "Получить информацию о Telegram по ID")
    @ApiResponse(responseCode = "200", description = "Информация о Telegram найдена")
    @ApiResponse(responseCode = "404", description = "Информация о Telegram не найдена")
    @GetMapping("/{id}")
    ResponseEntity<TelegramInfoDto> getTelegramInfoById(@PathVariable Long id);

    @Operation(summary = "Создать новую информацию о Telegram")
    @ApiResponse(responseCode = "201", description = "Информация о Telegram создана")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @PostMapping("/")
    ResponseEntity<TelegramInfoDto> createTelegramInfo(@RequestBody TelegramInfoDto telegramInfoDto);

    @Operation(summary = "Обновить информацию о Telegram")
    @ApiResponse(responseCode = "200", description = "Информация о Telegram обновлена")
    @ApiResponse(responseCode = "404", description = "Информация о Telegram не найдена")
    @PutMapping("/{id}")
    ResponseEntity<TelegramInfoDto> updateTelegramInfo(@PathVariable Long id, @RequestBody TelegramInfoDto telegramInfoDto);

    @Operation(summary = "Удалить информацию о Telegram по ID")
    @ApiResponse(responseCode = "204", description = "Информация о Telegram удалена")
    @ApiResponse(responseCode = "404", description = "Информация о Telegram не найдена")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTelegramInfo(@PathVariable Long id);
}
