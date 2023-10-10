CREATE TABLE IF NOT EXISTS telegram_info
(
    id       BIGSERIAL PRIMARY KEY,
    chat_id  BIGINT NOT NULL,
    username VARCHAR(255)

);

COMMENT ON COLUMN telegram_info.id IS 'Уникальный идентификатор записи в таблице';
COMMENT ON COLUMN telegram_info.chat_id IS 'Идентификатор чата Телеграм';
COMMENT ON COLUMN telegram_info.username IS 'Никнейм Пользователя Телеграм';
