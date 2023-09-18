CREATE TABLE IF NOT EXISTS booking (
    id UUID not null DEFAULT gen_random_uuid() PRIMARY KEY,
    phone VARCHAR(20),
    name VARCHAR(255),
    time TIMESTAMPTZ
    );
