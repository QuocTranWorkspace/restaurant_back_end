CREATE TABLE tbl_user_role (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    role_id INT NOT NULL, 
    CONSTRAINT fk_user_role FOREIGN KEY (user_id) REFERENCES tbl_user(id),
    CONSTRAINT fk_role_user FOREIGN KEY (role_id) REFERENCES tbl_role(id),
    status TINYINT(1) DEFAULT 1,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME
);

