package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.voucherbackapi.models.requests.ConsultaVoucherRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherPromocaoRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherRequest;
import com.rematec.voucher.voucherbackapi.models.response.ConsultaVoucherResponse;
import com.rematec.voucher.voucherbackapi.models.response.VoucherPromocaoResponse;
import com.rematec.voucher.voucherbackapi.services.VoucherServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherServiceImpl voucherService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsultaVoucherResponse> consultarPromocao(@RequestBody @Valid ConsultaVoucherRequest consulta) {

        return new ResponseEntity<ConsultaVoucherResponse>(this.voucherService.consultarPromocoes(consulta), HttpStatus.OK);

    }

    @PostMapping(value = "/confirm", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity confirmarVoucher(@RequestBody @Valid List<VoucherRequest> voucherRequests) {
        this.voucherService.confirmarVoucher(voucherRequests);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/cancel", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity cancelarVoucher(@RequestBody @Valid List<VoucherRequest> voucherRequests) {
        this.voucherService.cancelarVoucher(voucherRequests);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/resgate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoucherPromocaoResponse> resgateVoucher(@RequestBody @Valid VoucherPromocaoRequest promocaoRequest) {

        return new ResponseEntity<VoucherPromocaoResponse>(this.voucherService.resgateVoucher(promocaoRequest), HttpStatus.OK);

    }

}
