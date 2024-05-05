package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.models.PerfilResumidoApiResponse;
import com.rematec.voucher.voucherbackapi.models.requests.PerfilRequest;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResumidoResponse;
import com.rematec.voucher.voucherbackapi.services.PerfilServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/perfil")
public class PerfilController  implements PerfilApi{


    @Autowired
    private PerfilServiceImpl perfilService;


    @Override
    public ResponseEntity<List<PerfilApiResponse>> buscandoListaPerfil() {
        return new ResponseEntity<>(this.perfilService.buscandoListaPerfil(), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<List<PerfilResumidoApiResponse>> buscandoListaResumidoPerfil() {
        return new ResponseEntity<List<PerfilResumidoApiResponse>>(
                this.perfilService.buscandoListaResumidoPerfil(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PerfilApiResponse> buscandoPerfilPeloGUID(String guid) {
        return new ResponseEntity<PerfilApiResponse>(this.perfilService.buscandoPerfilPeloGUID(guid), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PerfilApiResponse> buscandoPerfilPeloNome(String nome) {
        return new ResponseEntity<PerfilApiResponse>(this.perfilService.buscandoPerfilPeloNome(nome), HttpStatus.OK);
    }
/*
    @Override
    public ResponseEntity<PerfilApiResponse> criandoPerfil(@Valid @RequestBody PerfilApiRequest perfilApiRequest) {
        return new ResponseEntity<PerfilApiResponse>(this.perfilService.criandoPerfil(
        this.dto.perfilApiRequestToPerfilRequest(perfilApiRequest)
        ), HttpStatus.CREATED);
    }
    */



    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PerfilResponse>> getAllPerfil() {
        return new ResponseEntity<>(this.perfilService.getAllPerfil(), HttpStatus.OK);
    }


    @GetMapping(value = "/resumido", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PerfilResumidoResponse>> getAllPerfilResumido() {
        return new ResponseEntity<List<PerfilResumidoResponse>>(this.perfilService.getAllPerfilResumido(), HttpStatus.OK);
    }

    @GetMapping(value = "{nome}/nome", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PerfilResponse> getPerfilNome(@PathVariable("nome") String nome) {
        return new ResponseEntity<PerfilResponse>(this.perfilService.getPerfilNome(nome), HttpStatus.OK);
    }

    @GetMapping(value = "{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PerfilResponse> getPerfilGuid(@PathVariable("guid") String guid) {
        return new ResponseEntity<PerfilResponse>(this.perfilService.getPerfilGuid(guid), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PerfilResponse> addPerfil(@Valid @RequestBody PerfilRequest request) {

        return new ResponseEntity<PerfilResponse>(this.perfilService.addPerfil(request), HttpStatus.CREATED);
    }

    @PutMapping(value = "{guid}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PerfilResponse> alterarPefil(@PathVariable("guid") String guid,
                                                       @RequestBody PerfilRequest request) {
        return new ResponseEntity<PerfilResponse>(this.perfilService.alterarPerfil(guid, request), HttpStatus.ACCEPTED);

    }

    @DeleteMapping(value = "{guid}")
    public ResponseEntity apagarPerfil(@PathVariable("guid") String guid) {
        this.perfilService.apagarPerfil(guid);
        return ResponseEntity.noContent().build();
    }


}
