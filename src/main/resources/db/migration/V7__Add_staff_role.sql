INSERT INTO
    tbl_user (user_name, password, first_name, last_name, email)
VALUES
    /*
     * Staff credential
     * Username: staff
     * Password: Staff@123
     */
    ('staff', '$2a$04$ISFj0EH0TCVnvlI.2UdQ/.FJnecKOnVfcRPe9gQ5BmPnwYaPXzTKO', 'staff', 'staff', 'staff@example.com');

INSERT INTO
    tbl_role (role_name)
VALUES
    ('STAFF');

INSERT INTO
    tbl_user_role (user_id, role_id)
VALUES
    (3, 3);
