CREATE TABLE IF NOT EXISTS farmers (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)    NOT NULL,
    phone       VARCHAR(15)     UNIQUE,
    village     VARCHAR(100),
    state       VARCHAR(50),
    crop_type   VARCHAR(255),
    land_acres  DECIMAL(10,2),
    created_at  DATETIME(6),
    PRIMARY KEY (id)
);