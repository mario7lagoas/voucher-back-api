package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.voucherbackapi.models.requests.PerfilRequest;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResumidoResponse;
import com.rematec.voucher.voucherbackapi.services.PerfilService;
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
public class PerfilController {

    @Autowired
    private PerfilService perfilService;


    @GetMapping
    public ResponseEntity<List<PerfilResponse>> getAllPerfil(){
        return new ResponseEntity<>(this.perfilService.getAllPeril(), HttpStatus.OK);
    }

    @GetMapping(value = "/resumido")
    public ResponseEntity<List<PerfilResumidoResponse>> getAllPerfilResumido(){
        return new ResponseEntity<>(this.perfilService.getAllPerilResumido(), HttpStatus.OK);
    }

    @GetMapping(value = "{nome}/nome")
    public ResponseEntity<PerfilResponse> getPerfilNome(@PathVariable("nome") String nome ){
        return new ResponseEntity<>(this.perfilService.getPerfilNome(nome), HttpStatus.OK);
    }

    @GetMapping(value = "{guid}")
    public ResponseEntity<PerfilResponse> getPerfilGuid(@PathVariable("guid") String guid ){
        return new ResponseEntity<>(this.perfilService.getPerfilGuid(guid), HttpStatus.OK);
    }



    @PostMapping
    public ResponseEntity<PerfilResponse> addPerfil(@Valid @RequestBody PerfilRequest request){

        return new ResponseEntity<>(this.perfilService.addPerfil(request), HttpStatus.CREATED);
    }
    @PutMapping(value = "{guid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PerfilResponse> alterarPefil (@PathVariable("guid") String guid ,
                                                        @RequestBody PerfilRequest request){

        return new ResponseEntity<PerfilResponse>(this.perfilService.alterarPerfil(guid, request), HttpStatus.ACCEPTED);

    }

    @DeleteMapping(value = "{guid}")
    public ResponseEntity apagarPerfil(@PathVariable("guid") String guid){

        this.perfilService.apagarPerfil(guid);

        return  ResponseEntity.noContent().build();
    }

}
