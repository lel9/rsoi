package ru.bmstu.notebook.exception;

public final class NoSuchUserException extends AppException {
    private String id;

    public NoSuchUserException(String id) {
        super();
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Пользователь %s не найден", id);
    }

}
