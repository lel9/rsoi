package ru.bmstu.notebook.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.bmstu.notebook.exception.NoSuchNoteException;
import ru.bmstu.notebook.exception.NoSuchUserException;
import ru.bmstu.notebook.model.GenericResponse;

@ControllerAdvice
class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public RestResponseEntityExceptionHandler() {
        super();
    }

    // 400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final BindingResult result = ex.getBindingResult();
        final GenericResponse bodyOfResponse = new GenericResponse(result.getAllErrors(), "Invalid" + result.getObjectName());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    public ResponseEntity<Object> handleBindException(final BindException ex,
                                                      final HttpHeaders headers,
                                                      final HttpStatus status,
                                                      final WebRequest request) {
        final BindingResult result = ex.getBindingResult();
        final GenericResponse bodyOfResponse = new GenericResponse(result.getAllErrors(), "Invalid" + result.getObjectName());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({NoSuchUserException.class})
    public ResponseEntity<Object> handleNoSuchUser(final RuntimeException ex, final WebRequest request) {
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), "NoSuchUser");
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({NoSuchNoteException.class})
    public ResponseEntity<Object> handleNoSuchNote(final RuntimeException ex, final WebRequest request) {
        final GenericResponse bodyOfResponse = new GenericResponse(ex.getMessage(), "NoSuchNote");
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        ex.printStackTrace();
        final GenericResponse bodyOfResponse = new GenericResponse("Внутренняя ошибка сервера", "InternalError");
        return new ResponseEntity<>(bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
