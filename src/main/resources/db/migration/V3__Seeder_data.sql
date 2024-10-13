INSERT INTO 
    tbl_user (userName, password, firstName, lastName, email)
VALUES
    /* 
     * Admin credential
     * Username: admin
     * Password: Admin@123
     */
    ('admin', '$2a$04$TSIDKn009yppIXB//jHmQ.92J3b0lPYjGAt4YjAlJArwDrEG8SUqm', 'admin', 'admin', 'admin@example.com'),
    /* 
     * User credential
     * Username: non_admin
     * Password: Non_admin@123
     */
    ('non_admin', '$2a$04$yXPmiIaolFHbbl4zBZgepeBjrHdx4RDweIAu6bTp8oGSQdyzgjHiy', 'non_admin', 'non_admin', 'non_admin@example.com');

INSERT INTO
    tbl_role (roleName)
VALUES
    ('ADMIN'),
    ('USER');

INSERT INTO
    tbl_user_role (userID, roleID)
VALUES
    (1, 1),
    (2, 2);
