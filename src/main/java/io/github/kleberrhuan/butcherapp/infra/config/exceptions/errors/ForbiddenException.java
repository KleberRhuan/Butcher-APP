package io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message) {
        super(message);
    }
}
