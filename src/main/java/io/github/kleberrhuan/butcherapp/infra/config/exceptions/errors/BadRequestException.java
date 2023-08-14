package io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
