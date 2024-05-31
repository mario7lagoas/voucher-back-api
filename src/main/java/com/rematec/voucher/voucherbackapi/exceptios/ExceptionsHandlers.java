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

        return new ResponseEntity<>(getNotfound(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VoucherNaoEncontradoException.class)
    public ResponseEntity<ErrorApiResponse> voucherNaoEncontradaExceptionHandler(VoucherNaoEncontradoException ex) {

        return new ResponseEntity<>(getNotfound(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VoucherEmUsoException.class)
    public ResponseEntity<ErrorApiResponse> voucherEmUsoExceptionHandler(VoucherEmUsoException ex) {

        return new ResponseEntity<>(getUnProcessable(ex, ErrosEnum.EM_USO.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(VoucherUtilizadoException.class)
    public ResponseEntity<ErrorApiResponse> voucherUtilizadoExceptionHandler(VoucherUtilizadoException ex) {

        return new ResponseEntity<>(getUnProcessable(ex, ErrosEnum.UTILIZADO.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(VoucherNaoPermitidoException.class)
    public ResponseEntity<ErrorApiResponse> voucherNaoPermitidoExceptionHandler(VoucherNaoPermitidoException ex) {

        return new ResponseEntity<>(getUnProcessable(ex, ErrosEnum.NAO_PERMITIDO.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UsuarioInativoException.class)
    public ResponseEntity<ErrorApiResponse> usuarioInativoExceptionHandler(UsuarioInativoException ex) {

        return new ResponseEntity<>(getUnProcessable(ex, ErrosEnum.USUARIO_INATIVADO.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PromocaoNaoEncontradaException.class)
    public ResponseEntity<ErrorApiResponse> promocaoNaoEncontradaExceptionHandler(PromocaoNaoEncontradaException ex) {

        return new ResponseEntity<>(getNotfound(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PerfilNaoEncontradoException.class)
    public ResponseEntity<ErrorApiResponse> perfilNaoEncontradaExceptionHandler(PerfilNaoEncontradoException ex) {

        return new ResponseEntity<>(getNotfound(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LojaNaoEncontradaException.class)
    public ResponseEntity<ErrorApiResponse> lojaNaoEncontradaExceptionHandler(LojaNaoEncontradaException ex) {

        return new ResponseEntity<>(getNotfound(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NaoPermitidoExcluirLojaException.class)
    public ResponseEntity<ErrorApiResponse> naoPermitidoExcluirLojaExceptionHandler(NaoPermitidoExcluirLojaException ex) {

        return new ResponseEntity<>(getUnProcessable(ex, ErrosEnum.NAO_PERMITIDO_EXCLUIR.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NaoPermitidoException.class)
    public ResponseEntity<ErrorApiResponse> naoPermitidoExceptionHandler(NaoPermitidoException ex) {

        return new ResponseEntity<>(getUnProcessable(ex, ErrosEnum.NAO_PERMITIDO.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NaoPermitidoExcluirPerfilException.class)
    public ResponseEntity<ErrorApiResponse> naoPermitidoExcluirPerfilExceptionHandler(NaoPermitidoExcluirPerfilException ex) {

        return new ResponseEntity<>(getUnProcessable(ex, ErrosEnum.NAO_PERMITIDO_EXCLUIR.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NaoPermitidoAlterarStatusException.class)
    public ResponseEntity<ErrorApiResponse> naoPermitidoalterarStatusExceptionHandler(NaoPermitidoAlterarStatusException ex) {

        return new ResponseEntity<>(getUnProcessable(ex, ErrosEnum.NAO_PERMITIDO_ALTERAR_STATUS.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(LojaCadastradaException.class)
    public ResponseEntity<ErrorApiResponse> lojaCadastradaExceptionHandler(LojaCadastradaException ex) {

        return new ResponseEntity<>(getUnProcessable(ex, ErrosEnum.CNPJ_JA_CADASTRADO.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PerfilCadastradoException.class)
    public ResponseEntity<ErrorApiResponse> perfilExceptionHandler(PerfilCadastradoException ex) {

        return new ResponseEntity<>(getUnProcessable(ex, ErrosEnum.PERFIL_JA_CADASTRADO.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UsuarioCadastradoException.class)
    public ResponseEntity<ErrorApiResponse> UsuarioCadastradoExceptionHandler(UsuarioCadastradoException ex) {

        return new ResponseEntity<>(getUnProcessable(ex, ErrosEnum.USUARIO_JA_CADASTRADO.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
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

    private ErrorApiResponse getNotfound(Exception ex) {

        return ErrorApiResponseBuilder.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(ErrosEnum.NAO_ENCONTRADO.toString())
                        .mensagem(ex.getMessage())
                        .build())
                ).build();
    }

    private ErrorApiResponse getUnProcessable(Exception ex, String statusEnum) {
        return ErrorApiResponseBuilder.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(ErroResponseBuilder.builder()
                        .codigo(statusEnum)
                        .mensagem(ex.getMessage())
                        .build())
                ).build();
    }

}
