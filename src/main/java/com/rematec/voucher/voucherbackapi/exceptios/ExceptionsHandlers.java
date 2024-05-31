package com.rematec.voucher.voucherbackapi.exceptios;

import com.rematec.voucher.models.ErrorApiResponse;
import com.rematec.voucher.voucherbackapi.builders.ErroResponseBuilder;
import com.rematec.voucher.voucherbackapi.builders.ErrorApiResponseBuilder;
import com.rematec.voucher.voucherbackapi.enums.ErrosEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;

@ControllerAdvice
public class ExceptionsHandlers {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErrorApiResponse> usuarioNaoEncontradaExceptionHandler(UsuarioNaoEncontradoException ex) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.NAO_ENCONTRADO.toString())
                        .mensagem(ex.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VoucherNaoEncontradoException.class)
    public ResponseEntity<ErrorApiResponse> voucherNaoEncontradaExceptionHandler(VoucherNaoEncontradoException ex) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.NAO_ENCONTRADO.toString())
                        .mensagem(ex.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VoucherEmUsoException.class)
    public ResponseEntity<ErrorApiResponse> voucherEmUsoExceptionHandler(VoucherEmUsoException ex) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.EM_USO.toString())
                        .mensagem(ex.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(VoucherUtilizadoException.class)
    public ResponseEntity<ErrorApiResponse> voucherUtilizadoExceptionHandler(VoucherUtilizadoException ex) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.UTILIZADO.toString())
                        .mensagem(ex.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(VoucherNaoPermitidoException.class)
    public ResponseEntity<ErrorApiResponse> voucherNaoPermitidoExceptionHandler(VoucherNaoPermitidoException ex) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.NAO_PERMITIDO.toString())
                        .mensagem(ex.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UsuarioInativoException.class)
    public ResponseEntity<ErrorApiResponse> usuarioInativoExceptionHandler(UsuarioInativoException ex) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.USUARIO_INATIVADO.toString())
                        .mensagem(ex.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @ExceptionHandler(PromocaoNaoEncontradaException.class)
    public ResponseEntity<ErrorApiResponse> promocaoNaoEncontradaExceptionHandler(PromocaoNaoEncontradaException exception) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.NAO_ENCONTRADO.toString())
                        .mensagem(exception.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PerfilNaoEncontradoException.class)
    public ResponseEntity<ErrorApiResponse> perfilNaoEncontradaExceptionHandler(PerfilNaoEncontradoException exception) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.NAO_ENCONTRADO.toString())
                        .mensagem(exception.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LojaNaoEncontradaException.class)
    public ResponseEntity<ErrorApiResponse> lojaNaoEncontradaExceptionHandler(LojaNaoEncontradaException exception) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.NAO_ENCONTRADO.toString())
                        .mensagem(exception.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NaoPermitidoExcluirLojaException.class)
    public ResponseEntity<ErrorApiResponse> naoPermitidoExcluirLojaExceptionHandler(NaoPermitidoExcluirLojaException exception) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.NAO_PERMITIDO_EXCLUIR.toString())
                        .mensagem(exception.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NaoPermitidoException.class)
    public ResponseEntity<ErrorApiResponse> naoPermitidoExceptionHandler(NaoPermitidoException exception) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.NAO_PERMITIDO.toString())
                        .mensagem(exception.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NaoPermitidoExcluirPerfilException.class)
    public ResponseEntity<ErrorApiResponse> naoPermitidoExcluirPerfilExceptionHandler(NaoPermitidoExcluirPerfilException exception) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.NAO_PERMITIDO_EXCLUIR.toString())
                        .mensagem(exception.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NaoPermitidoAlterarStatusException.class)
    public ResponseEntity<ErrorApiResponse> naoPermitidoalterarStatusExceptionHandler(NaoPermitidoAlterarStatusException exception) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.NAO_PERMITIDO_ALTERAR_STATUS.toString())
                        .mensagem(exception.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(LojaCadastradaException.class)
    public ResponseEntity<ErrorApiResponse> lojaCadastradaExceptionHandler(LojaCadastradaException exception) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.CNPJ_JA_CADASTRADO.toString())
                        .mensagem(exception.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PerfilCadastradoException.class)
    public ResponseEntity<ErrorApiResponse> perfilExceptionHandler(PerfilCadastradoException exception) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.PERFIL_JA_CADASTRADO.toString())
                        .mensagem(exception.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UsuarioCadastradoException.class)
    public ResponseEntity<ErrorApiResponse> UsuarioCadastradoExceptionHandler(UsuarioCadastradoException exception) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.USUARIO_JA_CADASTRADO.toString())
                        .mensagem(exception.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorApiResponse> noResourceFoundHandle(NoResourceFoundException ex) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.ENDPOINT_NAO_ENCONTRADO.toString())
                        .mensagem(ex.getBody().toString())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorApiResponse> methodValidationexceptionHandle(HandlerMethodValidationException ex) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.PAYLOAD_INVALIDO.toString())
                        .mensagem(ex.getBody().toString())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorApiResponse> badRequestExceptionHandle(BadRequestException ex) {

        ErrorApiResponse erroResponse = ErrorApiResponseBuilder.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.CAMPO_OBRIGATORIO.toString())
                        .mensagem(ex.getMessage())
                        .build())
                ).build();

        return new ResponseEntity<>(erroResponse, HttpStatus.BAD_REQUEST);
    }
}
