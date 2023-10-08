package com.awesome.park.service;

import com.awesome.park.dto.TelegramInfoDto;
import com.awesome.park.entity.TelegramInfo;
import com.awesome.park.mappers.TelegramInfoMapper;
import com.awesome.park.repository.TelegramInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelegramInfoService {
    private final TelegramInfoRepository telegramInfoRepository;
    private final TelegramInfoMapper telegramInfoMapper;

    public List<TelegramInfoDto> getAllTelegramInfos() {
        List<TelegramInfo> telegramInfos = telegramInfoRepository.findAll();
        return telegramInfos.stream()
                .map(telegramInfoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<TelegramInfoDto> getTelegramInfoById(Long id) {
        return telegramInfoRepository.findById(id)
                .map(telegramInfoMapper::mapToDto);
    }

    public TelegramInfoDto createOrUpdateTelegramInfo(TelegramInfoDto telegramInfoDto) {
        TelegramInfo telegramInfo = telegramInfoMapper.mapToEntity(telegramInfoDto);
        TelegramInfo savedTelegramInfo = telegramInfoRepository.save(telegramInfo);
        return telegramInfoMapper.mapToDto(savedTelegramInfo);
    }

    public void deleteTelegramInfoById(Long id) {
        telegramInfoRepository.deleteById(id);
    }
}

