package com.rematec.voucher.voucherbackapi.exceptios;

import com.rematec.voucher.models.CriandoPromocao404Response;
import com.rematec.voucher.models.CriandoPromocao404ResponseErrosInner;
import com.rematec.voucher.voucherbackapi.enums.ErrosEnum;
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

    @ExceptionHandler(VoucherEmUsoException.class)
    public ResponseEntity<ErrorResponse> voucherEmUsoExceptionHandler(VoucherEmUsoException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.EM_USO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(VoucherUtilizadoException.class)
    public ResponseEntity<ErrorResponse> voucherUtilizadoExceptionHandler(VoucherUtilizadoException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.UTILIZADO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(VoucherNaoPermitidoException.class)
    public ResponseEntity<ErrorResponse> voucherNaoPermitidoExceptionHandler(VoucherNaoPermitidoException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.NAO_PERMITIDO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UsuarioInativoException.class)
    public ResponseEntity<ErrorResponse> usuarioInativoExceptionHandler(UsuarioInativoException exception){
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
    public ResponseEntity<CriandoPromocao404Response> promocaoNaoEncontradaExceptionHandler(PromocaoNaoEncontradaException exception){

        CriandoPromocao404ResponseErrosInner errosInner = new CriandoPromocao404ResponseErrosInner();
        errosInner.setCodigo(ErrosEnum.NAO_ENCONTRADO.toString());
        errosInner.setMensagem(exception.getMessage());

        CriandoPromocao404Response erroResponse =  new CriandoPromocao404Response();
        erroResponse.setStatus(HttpStatus.NOT_FOUND.toString());
        erroResponse.setErros(Collections.singletonList(errosInner));

        return new ResponseEntity<>(erroResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PerfilNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> perfilNaoEncontradaExceptionHandler(PerfilNaoEncontradoException exception){
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
    public ResponseEntity<ErrorResponse> naoPermitidoExcluirLojaExceptionHandler(NaoPermitidoExcluirLojaException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.NAO_PERMITIDO_EXCLUIR.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(NaoPermitidoException.class)
    public ResponseEntity<ErrorResponse> naoPermitidoExceptionHandler(NaoPermitidoException exception){
        Map<String, String> response = new HashMap<>();
        response.put("codigo", ErrosEnum.NAO_PERMITIDO.toString());
        response.put("mensagem", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .erros(Collections.singletonList(response))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(NaoPermitidoExcluirPerfilException.class)
    public ResponseEntity<ErrorResponse> naoPermitidoExcluirPerfilExceptionHandler(NaoPermitidoExcluirPerfilException exception){
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
        response.put("codigo", ErrosEnum.ENDPOINT_NAO_ENCONTRADO.toString());
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
        response.put("codigo", ErrosEnum.PAYLOAD_INVALIDO.toString());
        response.put("mensagem", ex.getBody().toString());

        ErrorResponse errorresponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .erros(Collections.singletonList(response))
                .build();

        return new ResponseEntity<>(errorresponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestExceptionHandle(BadRequestException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("codigo", "CAMPO_OBRIGATORIO");
        response.put("mensagem", ex.getMessage());

        ErrorResponse errorresponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .erros(Collections.singletonList(response))
                .build();

        return new ResponseEntity<>(errorresponse, HttpStatus.BAD_REQUEST);
    }

}
