CREATE TABLE tbl_category (
    id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    categoryName VARCHAR(255) NOT NULL UNIQUE,
    categoryDescription VARCHAR(255),
    status TINYINT(1) DEFAULT 1,
    createdDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    updatedDate DATETIME
);

CREATE TABLE tbl_product (
    id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    avatar VARCHAR(255),
    productName VARCHAR(255) NOT NULL,
    productDescription VARCHAR(255),
    originalPrice DECIMAL NOT NULL,
    salePrice DECIMAL NOT NULL,
    categoryID INT NOT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (categoryID) REFERENCES tbl_category(id),
    status TINYINT(1) DEFAULT 1,
    createdDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    updatedDate DATETIME
);