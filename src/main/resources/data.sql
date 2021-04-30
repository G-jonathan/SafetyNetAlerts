DROP TABLE IF EXISTS person CASCADE ;
DROP TABLE IF EXISTS firestation CASCADE;
DROP TABLE IF EXISTS medicalrecord CASCADE;

CREATE TABLE person (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(250),
    last_name VARCHAR(250),
    address VARCHAR(250),
    city VARCHAR(250),
    zip VARCHAR(250),
    phone VARCHAR(250),
    email VARCHAR(250)
);
CREATE TABLE firestation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(250),
    station VARCHAR(250)
);
CREATE TABLE medicalrecord (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(250),
    last_name VARCHAR(250),
    birthdate VARCHAR(250),
    medications VARCHAR(250),
    allergies VARCHAR(250)
);