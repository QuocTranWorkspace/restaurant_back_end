CREATE TABLE tbl_user_role (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    userID INT NOT NULL,
    roleID INT NOT NULL, 
    CONSTRAINT fk_user_role FOREIGN KEY (userID) REFERENCES tbl_user(id),
    CONSTRAINT fk_role_user FOREIGN KEY (roleID) REFERENCES tbl_role(id),
    status TINYINT(1) DEFAULT 1,
    createdDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    updatedDate DATETIME
);

