package ru.bmstu.notebook.exception;

public class NoSuchNoteException extends AppException {
    private String id;

    public NoSuchNoteException(String id) {
        super();
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Заметка %s не найдена", id);
    }

}
