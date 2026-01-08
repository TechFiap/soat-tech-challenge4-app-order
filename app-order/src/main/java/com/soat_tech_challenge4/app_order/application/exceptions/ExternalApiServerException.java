package com.soat_tech_challenge4.app_order.application.exceptions;

public class ExternalApiServerException extends RuntimeException {
    public ExternalApiServerException(String message) {
        super(message);
    }
}
