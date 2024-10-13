CREATE TABLE tbl_order (
    id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    totalPrice DECIMAL NOT NULL,
    customerName VARCHAR(255) NOT NULL,
    custmerPhone VARCHAR(255) NOT NULL,
    customerAddress VARCHAR(255) NOT NULL,
    status TINYINT(1) DEFAULT 1,
    createdDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    updatedDate DATETIME
);

CREATE TABLE tbl_order_product (
    id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    orderID INT NOT NULL,
    productID INT NOT NULL,
    CONSTRAINT fk_order_product FOREIGN KEY (orderID) REFERENCES tbl_order(id),
    CONSTRAINT fk_product_order FOREIGN KEY (productID) REFERENCES tbl_product(id),
    status TINYINT(1) DEFAULT 1,
    createdDate DATETIME CURRENT_TIMESTAMP,
    updatedDate DATETIME
);