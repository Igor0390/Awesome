-- Создание таблицы "Работники"
CREATE TABLE IF NOT EXISTS employees
(
    id                SERIAL PRIMARY KEY,
    first_name        VARCHAR(255) NOT NULL,
    last_name         VARCHAR(255) NOT NULL,
    role              VARCHAR(255) NOT NULL,
    telegram_info_id BIGINT, -- Внешний ключ на таблицу TelegramInfo
    FOREIGN KEY (telegram_info_id) REFERENCES telegram_info(id)
);

COMMENT ON TABLE employees IS 'Таблица с информацией о сотрудниках.';
COMMENT ON COLUMN employees.first_name IS 'Имя сотрудника.';
COMMENT ON COLUMN employees.last_name IS 'Фамилия сотрудника.';
COMMENT ON COLUMN employees.role IS 'Роль сотрудника.';
COMMENT ON COLUMN employees.telegram_info_id IS 'Идентификатор информации о Телеграме сотрудника';