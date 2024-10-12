CREATE TABLE tbl_category (
    id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL UNIQUE,
    category_description VARCHAR(255),
    status TINYINT(1) DEFAULT 1,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME
);

CREATE TABLE tbl_product (
    id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    avatar VARCHAR(255),
    product_name VARCHAR(255) NOT NULL,
    product_description VARCHAR(255),
    original_price DECIMAL NOT NULL,
    sale_price DECIMAL NOT NULL,
    category_id INT NOT NULL,
    status TINYINT(1) DEFAULT 1,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME
);

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