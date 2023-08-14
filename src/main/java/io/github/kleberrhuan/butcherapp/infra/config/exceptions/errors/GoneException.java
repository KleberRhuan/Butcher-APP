package io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors;

public class GoneException extends RuntimeException{
    public GoneException(String message) {
        super(message);
    }
}
