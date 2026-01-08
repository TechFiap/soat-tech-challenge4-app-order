package com.soat_tech_challenge4.app_order.application.exceptions;

public class ExternalApiClientException extends RuntimeException {
    public ExternalApiClientException(String message) {
        super(message);
    }
}
