CREATE TABLE IF NOT EXISTS telegram_info
(
    id          BIGSERIAL PRIMARY KEY,
    chat_id     BIGINT      NOT NULL,
    username    VARCHAR(255),
    entity_type VARCHAR(50) NOT NULL,
    entity_id   BIGINT      NOT NULL,
    CONSTRAINT uq_telegram_info UNIQUE (entity_type, entity_id)
);

COMMENT ON COLUMN telegram_info.id IS 'Уникальный идентификатор записи в таблице';
COMMENT ON COLUMN telegram_info.chat_id IS 'Идентификатор чата Телеграм';
COMMENT ON COLUMN telegram_info.username IS 'Никнейм Пользователя Телеграм';
COMMENT ON COLUMN telegram_info.entity_type IS 'Тип сущности (клиент или сотрудник)';
COMMENT ON COLUMN telegram_info.entity_id IS 'Идентификатор сущности, с которой связана информация о Телеграме';