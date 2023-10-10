-- Создание таблицы "Клиенты"
CREATE TABLE IF NOT EXISTS customers
(
    id                BIGSERIAL PRIMARY KEY,
    first_name        VARCHAR(255) NOT NULL,
    last_name         VARCHAR(255) NOT NULL,
    phone_number      VARCHAR(15)  NOT NULL,
    telegram_info_id BIGINT,
    FOREIGN KEY (telegram_info_id) REFERENCES telegram_info(id)
);

COMMENT ON TABLE customers IS 'Таблица с информацией о клиентах.';
COMMENT ON COLUMN customers.first_name IS 'Имя клиента.';
COMMENT ON COLUMN customers.last_name IS 'Фамилия клиента.';
COMMENT ON COLUMN customers.phone_number IS 'Номер телефона клиента.';
COMMENT ON COLUMN customers.telegram_info_id IS 'Идентификатор информации о Телеграме клиента';


