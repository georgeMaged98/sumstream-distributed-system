DROP TABLE IF EXISTS sum;
CREATE TABLE IF NOT EXISTS sum (
    id SERIAL PRIMARY KEY,
    num1 INT,
    num2 INT,
    sum INT
);

DROP TABLE IF EXISTS outbox_message;
CREATE TABLE IF NOT EXISTS outbox_message (
    id SERIAL PRIMARY KEY,
    type TEXT NOT NULL,
    content TEXT NOT NULL,
    occurred_on_utc TIMESTAMPTZ NOT NULL,
    processed_on_utc TIMESTAMPTZ,
    error TEXT
);