CREATE TABLE tbl_order (
    id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    total_price DECIMAL NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    custmer_phone VARCHAR(255) NOT NULL,
    customer_address VARCHAR(255) NOT NULL,
    status TINYINT(1) DEFAULT 1,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME
);

CREATE TABLE tbl_order_product (
    id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    oerder_id INT NOT NULL,
    product_id INT NOT NULL,
    CONSTRAINT fk_order_product FOREIGN KEY (oerder_id) REFERENCES tbl_order(id),
    CONSTRAINT fk_product_order FOREIGN KEY (product_id) REFERENCES tbl_product(id),
    status TINYINT(1) DEFAULT 1,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME
);