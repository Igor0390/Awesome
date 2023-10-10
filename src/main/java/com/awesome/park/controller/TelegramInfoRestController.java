package com.awesome.park.controller;

import com.awesome.park.api.TelegramInfoApi;
import com.awesome.park.dto.TelegramInfoDto;
import com.awesome.park.service.TelegramInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TelegramInfoRestController implements TelegramInfoApi {
    private final TelegramInfoService telegramInfoService;

    public ResponseEntity<List<TelegramInfoDto>> getAllTelegramInfos() {
        List<TelegramInfoDto> telegramInfos = telegramInfoService.getAllTelegramInfos();
        return ResponseEntity.ok(telegramInfos);
    }

    public ResponseEntity<TelegramInfoDto> getTelegramInfoById(Long id) {
        Optional<TelegramInfoDto> telegramInfo = telegramInfoService.getTelegramInfoById(id);
        return telegramInfo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<TelegramInfoDto> createTelegramInfo(TelegramInfoDto telegramInfoDto) {
        TelegramInfoDto createdTelegramInfo = telegramInfoService.createOrUpdateTelegramInfo(telegramInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTelegramInfo);
    }

    public ResponseEntity<TelegramInfoDto> updateTelegramInfo(Long id, TelegramInfoDto telegramInfoDto) {
        TelegramInfoDto updatedTelegramInfo = telegramInfoService.createOrUpdateTelegramInfo(telegramInfoDto);
        return ResponseEntity.ok(updatedTelegramInfo);
    }

    public ResponseEntity<Void> deleteTelegramInfo(Long id) {
        telegramInfoService.deleteTelegramInfoById(id);
        return ResponseEntity.noContent().build();
    }
}
