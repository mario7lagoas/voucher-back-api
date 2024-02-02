package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.voucherbackapi.models.requests.PromocaoRequest;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoUpdateRequest;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
import com.rematec.voucher.voucherbackapi.services.PromocaoService;
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
@RequestMapping("/promocao")
public class PromocaoController {

    @Autowired
    private PromocaoService promocaoService;

    @GetMapping
    public ResponseEntity<List<PromocaoResponse>> getAll(){
        return new ResponseEntity<>(promocaoService.getAllPromocoes(), HttpStatus.OK);
    }

    @GetMapping("/{guid}")
    public ResponseEntity<PromocaoResponse> buscarPromocaiPorGuid(@PathVariable("guid") String guid){
        return new ResponseEntity<>(promocaoService.buscarPromocaoByGuid(guid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PromocaoResponse> addPromocao(@RequestBody @Valid PromocaoRequest promocaoRequest){
        return new ResponseEntity<>(promocaoService.addPromocao(promocaoRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{guid}")
    public ResponseEntity<PromocaoResponse> editPromocao(@PathVariable("guid") String guid,
                                                          @RequestBody @Valid PromocaoUpdateRequest promocaoUpdateRequest){
        return new ResponseEntity<>(promocaoService.alterarPromocao(guid, promocaoUpdateRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{guid}")
    public ResponseEntity deletePromocao(@PathVariable("guid") String guid){
        this.promocaoService.apagarPromocao(guid);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
