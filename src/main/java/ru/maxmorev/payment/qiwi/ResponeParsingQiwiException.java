package ru.maxmorev.payment.qiwi;

import java.io.IOException;

public class ResponeParsingQiwiException extends IOException {
    public ResponeParsingQiwiException(String message) {
        super(message);
    }
}
