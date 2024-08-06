CREATE DATABASE IF NOT EXISTS webPos;

USE webPos;

CREATE TABLE customer (
                          customerId VARCHAR(30) PRIMARY KEY,
                          firstName  VARCHAR(30) NULL,
                          lastName   VARCHAR(30) NULL,
                          dob        DATE        NULL,
                          address    VARCHAR(30) NULL,
                          mobile     VARCHAR(30) NULL
);

CREATE TABLE item (
                      itemId    VARCHAR(30)   PRIMARY KEY,
                      itemName  VARCHAR(30)   NULL,
                      price     DOUBLE(10, 2) NULL,
                      quantity  INT           NULL,
                      category  VARCHAR(30)   NULL,
                      imagePath VARCHAR(100)  NULL
);

CREATE TABLE orders (
                        orderId     VARCHAR(30)                PRIMARY KEY,
                        dateAndTime TIMESTAMP                  NULL,
                        customerId  VARCHAR(30)                NULL,
                        subtotal    DOUBLE(10, 2) DEFAULT 0.00 NOT NULL,
                        discount    DOUBLE(10, 2) DEFAULT 0.00 NOT NULL,
                        amount_paid DOUBLE(10, 2) DEFAULT 0.00 NOT NULL,
                        FOREIGN KEY (customerId) REFERENCES customer (customerId)
                            ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE orderDetail (
                             orderId   VARCHAR(30)   NULL,
                             itemId    VARCHAR(30)   NULL,
                             quantity  VARCHAR(30)   NULL,
                             unitPrice DOUBLE(10, 2) NULL,
                             FOREIGN KEY (orderId) REFERENCES orders (orderId)
                                 ON UPDATE CASCADE ON DELETE CASCADE,
                             FOREIGN KEY (itemId) REFERENCES item (itemId)
                                 ON UPDATE CASCADE ON DELETE CASCADE
);