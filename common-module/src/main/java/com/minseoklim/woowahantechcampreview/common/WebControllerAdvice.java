package com.minseoklim.woowahantechcampreview.common;

import static org.springframework.http.HttpStatus.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;
import com.minseoklim.woowahantechcampreview.common.exception.NotFoundException;

@RestControllerAdvice
@Slf4j
public class WebControllerAdvice {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(final BadRequestException exception) {
        return new ResponseEntity<>(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(final NotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException exception
    ) {
        final List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        final String errorMessage = errors.stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(System.lineSeparator()));

        return new ResponseEntity<>(errorMessage, BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(final AuthenticationException exception) {
        return new ResponseEntity<>(exception.getMessage(), UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(final AccessDeniedException exception) {
        return new ResponseEntity<>(exception.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public void handleException(final Exception exception) {
        // TODO: Sentry와 같은 모니터링 시스템에 exception에 대한 알림을 할 필요
        log.error(exception.getMessage());
    }
}
