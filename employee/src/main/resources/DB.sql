create database employee;

use employee;

show tables;

select * from EmployeeRequests;
select * from EmployeeRequests_log;

select * from Employees_log;
select * from Employees;


select * from Rights;
select * from Rights_log;

select * from EmployeeRights;
select * from EmployeeRights_log;

CREATE TABLE Rights (
    right_id INT PRIMARY KEY AUTO_INCREMENT,
    right_code VARCHAR(10) UNIQUE NOT NULL , 
    right_name VARCHAR(100) UNIQUE NOT NULL,
	right_status CHAR(1) NOT NULL, 
    createdDateTime DATETIME NOT NULL, 
    updatedDateTime DATETIME ,
    createdBy VARCHAR(20) NOT NULL,
    updatedBy VARCHAR(20),
	sysTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE EmployeeRequests (
    emp_RequestId INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(200) UNIQUE NOT NULL,
    department VARCHAR(20) NOT NULL,
    name VARCHAR(20) NOT NULL,
    phone_number VARCHAR(10) UNIQUE NOT NULL,
	gender CHAR(1) NOT NULL,
    country VARCHAR(3),
    state VARCHAR(3),
    city VARCHAR(20),
    emp_RequestStatus CHAR(1) NOT NULL,
    createdDateTime DATETIME NOT NULL,
    updatedDateTime DATETIME,
	createdBy VARCHAR(20) NOT NULL,
    updatedBy VARCHAR(20),
	sysTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE Employees (
    emp_id INT PRIMARY KEY AUTO_INCREMENT,
	emp_code VARCHAR(10) UNIQUE NOT NULL, 
    emp_password VARCHAR(255) NOT NULL, 
    email VARCHAR(200) UNIQUE NOT NULL,
	department VARCHAR(20) NOT NULL, 
    name VARCHAR(20) NOT NULL,
	phone_number VARCHAR(10) UNIQUE NOT NULL,
	gender CHAR(1) NOT NULL,
    country VARCHAR(3), 
    state VARCHAR(3),
    city VARCHAR(20),
    emp_status CHAR(1) NOT NULL, 
    createdDateTime DATETIME NOT NULL, 
    updatedDateTime DATETIME,
	createdBy VARCHAR(20) NOT NULL,
    updatedBy VARCHAR(20),
	sysTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

);

CREATE TABLE EmployeeRights (
    emp_code VARCHAR(10),
    right_code VARCHAR(10),
    PRIMARY KEY(emp_code, right_code),
    FOREIGN KEY(emp_code) REFERENCES Employees(emp_code),
    FOREIGN KEY(right_code) REFERENCES Rights(right_code),
	createdDateTime DATETIME NOT NULL,
	createdBy VARCHAR(20) NOT NULL,
    updatedBy VARCHAR(20),
	sysTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

);

 -- Drop table Rights;
--  Drop table Employees;
--  Drop table EmployeeRequests;
--   Drop table EmployeeRights;

-- Drop table Rights_log;
-- Drop table Employees_log;
-- Drop table EmployeeRequests_log;
 -- Drop table EmployeeRights_log;

-- logs 
CREATE TABLE Rights_log (
    right_id INT ,
    right_code VARCHAR(10)  NOT NULL , 
    right_name VARCHAR(10)  NOT NULL,
	right_status CHAR(1), 
    createdDateTime DATETIME NOT NULL, 
    updatedDateTime DATETIME ,
    createdBy VARCHAR(20) NOT NULL,
    updatedBy VARCHAR(20),
	sysTime DATETIME NOT NULL
);


CREATE TABLE EmployeeRequests_log (
    emp_RequestId INT,
    email VARCHAR(200) NOT NULL,
    department VARCHAR(20) NOT NULL,
    name VARCHAR(20) NOT NULL,
    phone_number VARCHAR(10) NOT NULL,
	gender CHAR(1) NOT NULL,
    country VARCHAR(3),
    state VARCHAR(3),
    city VARCHAR(20),
    emp_RequestStatus CHAR(1) NOT NULL,
    createdDateTime DATETIME NOT NULL,
    updatedDateTime DATETIME,
	createdBy VARCHAR(20) NOT NULL,
    updatedBy VARCHAR(20),
	sysTime DATETIME NOT NULL
);

CREATE TABLE EmployeeRights_log (
    emp_code VARCHAR(10),
    right_code VARCHAR(10),
	createdDateTime DATETIME NOT NULL,
	createdBy VARCHAR(20) NOT NULL,
    updatedBy VARCHAR(20),
	sysTime DATETIME NOT NULL

);
 
CREATE TABLE Employees_log (
    emp_id INT ,
	emp_code VARCHAR(10)  NOT NULL, 
    emp_password VARCHAR(255) NOT NULL, 
    email VARCHAR(200)  NOT NULL,
	department VARCHAR(20) NOT NULL, 
    name VARCHAR(20) NOT NULL,
	phone_number VARCHAR(10)  NOT NULL,
	gender CHAR(1) NOT NULL,
    country VARCHAR(3), 
    state VARCHAR(3),
    city VARCHAR(20),
    emp_status CHAR(1) NOT NULL, 
    createdDateTime DATETIME NOT NULL, 
    updatedDateTime DATETIME,
	createdBy VARCHAR(20) NOT NULL,
    updatedBy VARCHAR(20),
	sysTime DATETIME NOT NULL

);


