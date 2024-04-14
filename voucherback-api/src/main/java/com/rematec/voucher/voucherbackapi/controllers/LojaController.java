package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.voucherbackapi.models.requests.LojaPrintRequest;
import com.rematec.voucher.voucherbackapi.models.requests.LojaRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UpdateStatusResquest;
import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import com.rematec.voucher.voucherbackapi.models.response.LojasPaginadaResponse;
import com.rematec.voucher.voucherbackapi.services.LojaServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loja")
public class LojaController {

    @Autowired
    private LojaServiceImpl lojaService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LojaResponse> addLoja(@RequestBody @Valid LojaRequest lojaRequest){

        return new ResponseEntity<LojaResponse>(this.lojaService.addLoja(lojaRequest), HttpStatus.CREATED);

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LojaResponse>> getAll(){
        return new ResponseEntity<List<LojaResponse>>(lojaService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/paginada", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LojasPaginadaResponse> getAllLojasPaginada(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                     @RequestParam(name = "size", defaultValue = "10") int size,
                                                                     @RequestParam(name = "cnpj", defaultValue = "") String cnpj){
        return new ResponseEntity<LojasPaginadaResponse>(this.lojaService.obterLojasPaginadas(cnpj, page,size), HttpStatus.OK);
    }

    @GetMapping(value = "/ativa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LojaResponse>> getAllLojasAtivas(){
        return new ResponseEntity<List<LojaResponse>>(this.lojaService.getLojasAtivas(), HttpStatus.OK);
    }

    @GetMapping(value = "{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LojaResponse> buscarLojaByGuid(@PathVariable("guid") String guid){
        return new ResponseEntity<LojaResponse>(this.lojaService.buscarLojaByGuid(guid), HttpStatus.OK);
    }

    @PutMapping(value = "{guid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LojaResponse> editLoja(@PathVariable("guid") String guid,
                                                 @RequestBody LojaRequest lojaRequest){
        return new ResponseEntity<LojaResponse>(this.lojaService.updateLoja(guid, lojaRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "{guid}")
    public ResponseEntity deleteLoja(@PathVariable("guid") String guid){
        this.lojaService.apagarLoja(guid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PatchMapping(value = "{guid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarStatus(@PathVariable("guid") String guid,
                                              @RequestBody @Valid
                                              UpdateStatusResquest statusResquest) {
        this.lojaService.updateStatus(guid, statusResquest);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/print" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> printPromocoes(@RequestBody List<LojaPrintRequest> prints){

        return new ResponseEntity<String>(this.lojaService.printLojas(prints), HttpStatus.OK );
    }


}
