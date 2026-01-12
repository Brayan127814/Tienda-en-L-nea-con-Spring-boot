package com.TiendaEnLinea.TiendaEnLinea.Exceptions;


import com.TiendaEnLinea.TiendaEnLinea.Entity.UsuarioEntity;
import com.TiendaEnLinea.TiendaEnLinea.dtos.UsuarioResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.TiendaEnLinea.TiendaEnLinea.dtos.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    //MANEJO DE EXCEPCIONES PARA CUANDO UN RECURSO NO ES ECONTRADO
    @ExceptionHandler(NotFoundExceptions.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundExceptions e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());

        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
