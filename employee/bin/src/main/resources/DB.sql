create database employee;

use employee;

CREATE TABLE Rights (
    right_id INT PRIMARY KEY AUTO_INCREMENT,
    right_code VARCHAR(10) UNIQUE,
    right_name VARCHAR(10) UNIQUE NOT NULL,
	right_status VARCHAR(10),
    created_at DATETIME,
    updated_at DATETIME 
);


DELIMITER $$
CREATE TRIGGER trg_right_code
BEFORE INSERT ON Rights
FOR EACH ROW
BEGIN
    SET NEW.right_code = CONCAT('RIG', LPAD((SELECT IFNULL(MAX(right_id),0)+1 FROM Rights), 4, '0'));
END $$
DELIMITER ;



CREATE TABLE EmployeeRequests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(30) UNIQUE NOT NULL,
    department VARCHAR(20),
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    phone_number VARCHAR(10) UNIQUE NOT NULL,
	gender VARCHAR(7),
    country VARCHAR(3),
    state VARCHAR(3),
    city VARCHAR(20),
    approvedStatus VARCHAR(10) DEFAULT 'Created',
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE Employees (
    emp_id INT PRIMARY KEY AUTO_INCREMENT,
	emp_code VARCHAR(10) UNIQUE,
    emp_password VARCHAR(255) NOT NULL, 
    email VARCHAR(30) UNIQUE NOT NULL,
    first_name VARCHAR(20), 
    last_name VARCHAR(20),  
	phone_number VARCHAR(10) UNIQUE NOT NULL,
	gender VARCHAR(7),
    country VARCHAR(3),
    state VARCHAR(3),
    city VARCHAR(20),
    emp_status VARCHAR(8) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);


DELIMITER $$
CREATE TRIGGER trg_employee_code
BEFORE INSERT ON Employees
FOR EACH ROW
BEGIN
    SET NEW.emp_code = CONCAT('EMP', LPAD((SELECT IFNULL(MAX(emp_id),0)+1 FROM Employees), 4, '0'));
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION generates_strong_password(length INT)
RETURNS VARCHAR(255)
NO SQL
BEGIN
    SET @chars = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@*';
    SET @password = '';
    SET @i = 0;
    WHILE @i < length DO
        SET @password = CONCAT(@password, SUBSTR(@chars, FLOOR(RAND() * LENGTH(@chars)) + 1, 1));
        SET @i = @i + 1;
    END WHILE;
    RETURN @password;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER trg_generate_strong_password
BEFORE INSERT ON Employees
FOR EACH ROW
BEGIN
    SET NEW.emp_password = generates_strong_password(12);
END$$
DELIMITER ;

CREATE TABLE EmployeeRights (
    emp_id INT,
    right_id INT,
    PRIMARY KEY(emp_id, right_id),
    FOREIGN KEY(emp_id) REFERENCES Employees(emp_id),
    FOREIGN KEY(right_id) REFERENCES Rights(right_id)
);

 

