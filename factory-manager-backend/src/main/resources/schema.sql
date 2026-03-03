DROP TABLE IF EXISTS product_ingredient;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS raw_material;

CREATE TABLE raw_material (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    code           VARCHAR(20)    NOT NULL UNIQUE,
    name           VARCHAR(100)   NOT NULL,
    stock_quantity DOUBLE         NOT NULL,
    unit           VARCHAR(10)    NOT NULL
);

CREATE TABLE product (
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    code  VARCHAR(20)    NOT NULL UNIQUE,
    name  VARCHAR(100)   NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE product_ingredient (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id        BIGINT NOT NULL,
    raw_material_id   BIGINT NOT NULL,
    quantity_required DOUBLE NOT NULL,
    CONSTRAINT fk_ingredient_product      FOREIGN KEY (product_id)      REFERENCES product(id),
    CONSTRAINT fk_ingredient_raw_material FOREIGN KEY (raw_material_id) REFERENCES raw_material(id)
);