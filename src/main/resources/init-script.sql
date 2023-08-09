-- Create the database
CREATE DATABASE IF NOT EXISTS human_creator;
USE human_creator;

-- Create the table
CREATE TABLE IF NOT EXISTS human (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name TEXT NOT NULL,
    age INT,
    birthday DATE
);