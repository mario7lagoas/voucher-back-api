package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.voucherbackapi.models.requests.LojaRequest;
import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import com.rematec.voucher.voucherbackapi.services.LojaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/loja")
public class LojaController {

    @Autowired
    private LojaService lojaService;

    @PostMapping
    public ResponseEntity<LojaResponse> addLoja(@RequestBody @Valid LojaRequest lojaRequest){

        return new ResponseEntity<>(lojaService.addLoja(lojaRequest), HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<LojaResponse>> gelAll(){
        return new ResponseEntity<>(lojaService.gelAll(), HttpStatus.OK);
    }

    @GetMapping("/{guid}")
    public ResponseEntity<LojaResponse> buscarLojaByGuid(@PathVariable("guid") String guid){
        return new ResponseEntity<>(lojaService.buscarLojaByGuid(guid), HttpStatus.OK);
    }

    @PutMapping("/{guid}")
    public ResponseEntity<LojaResponse> editLoja(@PathVariable("guid") String guid,
                                                 @RequestBody LojaRequest lojaRequest){
        return new ResponseEntity<>(lojaService.updateLoja(guid, lojaRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{guid}")
    public ResponseEntity deleteLoja(@PathVariable("guid") String guid){
        this.lojaService.apagarLoja(guid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
