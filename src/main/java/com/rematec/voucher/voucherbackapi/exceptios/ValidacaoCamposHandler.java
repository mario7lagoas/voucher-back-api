package com.rematec.voucher.voucherbackapi.exceptios;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rematec.voucher.models.ErroResponse;
import com.rematec.voucher.models.ErrorApiResponse;
import com.rematec.voucher.voucherbackapi.builders.ErroResponseBuilder;
import com.rematec.voucher.voucherbackapi.builders.ErrorApiResponseBuilder;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Field;
import java.util.List;

@ControllerAdvice
public class ValidacaoCamposHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> tratarValidacoes(MethodArgumentNotValidException ex) {

        List<ErroResponse> erros1 = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> ErroResponseBuilder.builder()
                        .codigo(obterNomePropriedade(erro))
                        .mensagem(erro.getDefaultMessage())
                        .build()).toList();

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .erros(erros1)
                .build();

        return new ResponseEntity<>(erroResponse, HttpStatus.BAD_REQUEST);
    }

    private String obterNomePropriedade(final FieldError error) {

        if (error.contains(ConstraintViolation.class)) {

            try {

                final ConstraintViolation<?> violacao = error.unwrap(ConstraintViolation.class);
                final Field campo;
                campo = violacao.getRootBeanClass().getDeclaredField(error.getField());
                final JsonProperty anotacao = campo.getAnnotation(JsonProperty.class);

                if (anotacao != null && anotacao.value() != null && !anotacao.value().isEmpty()) {
                    return anotacao.value();
                }
            } catch (Exception e) {
            }
        }
        return error.getField();
    }
}
