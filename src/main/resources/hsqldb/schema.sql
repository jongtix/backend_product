DROP TABLE product IF EXISTS ;

CREATE TABLE product(
    seq INTEGER NOT NULL IDENTITY,
    id INTEGER NOT NULL,
    name VARCHAR(255),
    price INTEGER,
    type INTEGER,
    PRIMARY KEY (seq)
);

DROP TABLE stock IF EXISTS;

CREATE TABLE stock(
    seq INTEGER NOT NULL IDENTITY,
    id INTEGER,
    amount INTEGER,
    sold_out VARCHAR(1),
    PRIMARY KEY (seq)
);

ALTER TABLE stock ADD FOREIGN KEY (id) REFERENCES product(id);

DROP TABLE productTypeInfo IF EXISTS;

CREATE TABLE productTypeInfo(
    seq INTEGER NOT NULL IDENTITY,
    id INTEGER,
    sub_id INTEGER,
    discount_ratio DOUBLE
);

ALTER TABLE productTypeInfo ADD FOREIGN KEY (id) REFERENCES product(id);