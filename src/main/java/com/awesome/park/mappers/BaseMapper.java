package com.awesome.park.mappers;

/**
 * Базовый маппер для преобразования между сущностьми и их DTO.
 *
 * @param <D> Тип DTO.
 * @param <E> Тип сущности.
 */

public interface BaseMapper<D, E> {

    /**
     * Преобразует сущность в DTO.
     *
     * @param entity Сущность для преобразования.
     * @return DTO, представляющее сущность.
     */
    D mapToDto(E entity);

    /**
     * Преобразует DTO в сущность.
     *
     * @param dto DTO для преобразования.
     * @return entity  Сущность, в которую нужно выполнить преобразование.
     */
    E mapToEntity(D dto);
}

