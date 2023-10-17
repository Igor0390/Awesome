-- Создание таблицы "Активности"
CREATE TABLE IF NOT EXISTS activities
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255)   NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

COMMENT ON COLUMN activities.id IS 'Уникальный идентификатор записи в таблице';
COMMENT ON COLUMN activities.name IS 'Наименование активности.';
COMMENT ON COLUMN activities.price IS 'Стоимость активности (в рублях).';
