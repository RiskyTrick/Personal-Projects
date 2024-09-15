CREATE DATABASE IF NOT EXISTS BankSystem;

USE BankSystem;

CREATE TABLE IF NOT EXISTS accounts (
    account_number INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    type ENUM('Deposit', 'Withdrawal') NOT NULL,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);
