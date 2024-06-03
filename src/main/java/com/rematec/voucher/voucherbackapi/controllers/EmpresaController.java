package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.models.EmpresaApiRequest;
import com.rematec.voucher.models.EmpresaApiResponse;
import com.rematec.voucher.voucherbackapi.services.VoucherBackFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmpresaController implements EmpresaApi{

    @Autowired
    private VoucherBackFacade empresaService;

    @Override
    public ResponseEntity<List<EmpresaApiResponse>> buscandoListaEmpresa() {
        return new ResponseEntity<List<EmpresaApiResponse>>(this.empresaService.buscandoListaEmpresa(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EmpresaApiResponse> criandoEmpresa(EmpresaApiRequest empresaApiRequest) {
        return new ResponseEntity<EmpresaApiResponse>(this.empresaService.criandoEmpresa(empresaApiRequest), HttpStatus.ACCEPTED);
    }
}
