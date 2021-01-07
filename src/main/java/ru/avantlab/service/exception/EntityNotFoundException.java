package ru.avantlab.service.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String massage) {
        super(massage);
    }
}
