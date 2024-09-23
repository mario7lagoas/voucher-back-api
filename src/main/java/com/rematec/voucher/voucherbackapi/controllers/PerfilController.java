package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.models.PerfilApiRequest;
import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.models.PerfilResumidoApiResponse;
import com.rematec.voucher.models.PerfilUpdateApiRequest;
import com.rematec.voucher.voucherbackapi.services.VoucherBackFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PerfilController implements PerfilApi {

    private final VoucherBackFacade perfilService;

    public PerfilController(final VoucherBackFacade perfilService) {
        this.perfilService = perfilService;
    }

    @Override
    public ResponseEntity<List<PerfilApiResponse>> buscandoListaPerfil() {
        return new ResponseEntity<>(this.perfilService.buscandoListaPerfil(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<PerfilApiResponse>> buscandoListaPerfilPelaEmpresa(String guid) {
        return new ResponseEntity<List<PerfilApiResponse>>(
                this.perfilService.buscandoListaPerfilPelaEmpresa(guid), HttpStatus.OK);
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

    @Override
    public ResponseEntity<PerfilApiResponse> criandoPerfil(PerfilApiRequest perfilApiRequest) {
        return new ResponseEntity<PerfilApiResponse>(this.perfilService.criandoPerfil(perfilApiRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PerfilApiResponse> alterandoPerfil(String guid, PerfilUpdateApiRequest perfilApiRequest) {
        return new ResponseEntity<PerfilApiResponse>(
                this.perfilService.alterandoPerfil(guid, perfilApiRequest), HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<Void> apagandoPerfil(String guid) {
        this.perfilService.apagandoPerfil(guid);
        return ResponseEntity.noContent().build();
    }

}
