

CREATE TABLE BLOCK_LIST(
    DOCUMENT    VARCHAR(20) PRIMARY KEY,
    STATUS      VARCHAR(20),
    CREATED_AT TIMESTAMP DEFAULT CURRENT_DATE,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_DATE
);