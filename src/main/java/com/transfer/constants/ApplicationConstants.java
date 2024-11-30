package com.transfer.constants;

public enum ApplicationConstants {
    ACCOUNT_NOT_FOUND("Account not found"),
    BANK_ACCOUNT("Bank Account"),
    CUSTOMER_ALREADY_EXISTS("Customer with email already exists"),
    DEPOSIT_SUCCESSFULLY("Deposit done successfully"),
    LOGIN_SUCCESSFULLY("Login successfully"),
    BEARER("Bearer"),
    CUSTOMER_NOT_FOUND("Customer not found"),
    NOT_CORRECT_PASSWORD("Old password is incorrect"),
    FAVORITE_RECIPIENT_ALREADY_EXIST("Favorite recipient already exists"),
    FAVORITE_RECIPIENT_NOT_FOUND("Favorite recipient not found"),
    SENDER_ACCOUNT_NOT_FOUND("Sender account not found"),
    RECEIVER_ACCOUNT_NOT_FOUND("Receiver account not found"),
    ERROR_RECIPIENT_NAME("Recipient name does not match the account"),
    INSUFFICIENT_FUNDS("Insufficient funds in sender's account");


    private final String message;

    ApplicationConstants(String message) {this.message = message;}

    @Override
    public String toString() {return message;}
}
