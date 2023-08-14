package io.github.kleberrhuan.butcherapp.infra.config.exceptions.errors;

public class AlreadyVerifiedException extends RuntimeException {
    public AlreadyVerifiedException(String message) {
        super(message);
    }
}

