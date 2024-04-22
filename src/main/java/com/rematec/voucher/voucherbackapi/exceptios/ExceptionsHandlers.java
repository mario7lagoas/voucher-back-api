package com.rematec.voucher.voucherbackapi.exceptios;

import com.rematec.voucher.voucherbackapi.models.enums.ErrosEnum;
import com.rematec.voucher.voucherbackapi.models.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionsHandlers {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> usuarioNaoEncontradaExceptionHandler(UsuarioNaoEncontradoException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.NAO_ENCONTRADO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VoucherNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> voucherNaoEncontradaExceptionHandler(VoucherNaoEncontradoException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.NAO_ENCONTRADO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioInativoException.class)
    public ResponseEntity<ErrorResponse> lusuarioInativoExceptionHandler(UsuarioInativoException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.USUARIO_INATIVADO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(PromocaoNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> promocaoNaoEncontradaExceptionHandler(PromocaoNaoEncontradaException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.NAO_ENCONTRADO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(LojaNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> lojaNaoEncontradaExceptionHandler(LojaNaoEncontradaException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.NAO_ENCONTRADO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(NaoPermitidoExcluirLojaException.class)
    public ResponseEntity<ErrorResponse> lojaPermitidoExcluirLojaExceptionHandler(NaoPermitidoExcluirLojaException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.NAO_PERMITIDO_EXCLUIR.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(NaoPermitidoExcluirPerfilException.class)
    public ResponseEntity<ErrorResponse> lojaPermitidoExcluirPerfilExceptionHandler(NaoPermitidoExcluirPerfilException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.NAO_PERMITIDO_EXCLUIR.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(NaoPermitidoAlterarStatusException.class)
    public ResponseEntity<ErrorResponse> naoPermitidoalterarStatusExceptionHandler(NaoPermitidoAlterarStatusException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.NAO_PERMITIDO_ALTERAR_STATUS.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(LojaCadastradaException.class)
    public ResponseEntity<ErrorResponse> lojaCadastradaExceptionHandler(LojaCadastradaException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.CNPJ_JA_CADASTRADO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(PerfilCadastradoException.class)
    public ResponseEntity<ErrorResponse> perfilExceptionHandler(PerfilCadastradoException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.PERFIL_JA_CADASTRADO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(UsuarioCadastradoException.class)
    public ResponseEntity<ErrorResponse> UsuarioCadastradoExceptionHandler(UsuarioCadastradoException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.USUARIO_JA_CADASTRADO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> oResourceFoundHandle(NoResourceFoundException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("codigo", "ENDPOINT_NAO_ENCONTRADO");
        response.put("mensagem", ex.getBody().toString());

        ErrorResponse errorresponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(response))
                .build();

        return new ResponseEntity<>(errorresponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> methodValidationexceptionHandle(HandlerMethodValidationException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("codigo", "PAYLOAD_INVALIDO");
        response.put("mensagem", ex.getBody().toString());

        ErrorResponse errorresponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .erros(Collections.singletonList(response))
                .build();

        return new ResponseEntity<>(errorresponse, HttpStatus.BAD_REQUEST);
    }

}
