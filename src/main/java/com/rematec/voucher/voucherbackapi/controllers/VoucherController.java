package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.voucherbackapi.models.requests.ConsultaVoucherRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherFinalizeRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherPrintRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherPromocaoRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherRequest;
import com.rematec.voucher.voucherbackapi.models.response.ConsultaVoucherResponse;
import com.rematec.voucher.voucherbackapi.models.response.VouchersPaginadaResponse;
import com.rematec.voucher.voucherbackapi.models.response.VoucherPromocaoResponse;
import com.rematec.voucher.voucherbackapi.services.VoucherService;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherUtil voucherUtil;

    @PostMapping(value = "/consulta", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "/consumer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity consumerVoucher(@RequestBody @Valid VoucherFinalizeRequest voucherComsumer) {
        this.voucherService.consumer(voucherComsumer);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping(value = "/rollback", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity cancelVoucher(@RequestBody @Valid VoucherFinalizeRequest voucherRollback) {
        this.voucherService.rollback(voucherRollback);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VouchersPaginadaResponse> getAllTransacoesFilter(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "codigo", defaultValue = "") String codigo,
            @RequestParam(name = "descricao", defaultValue = "") String descricao,
            @RequestParam(name = "clienteCpf", defaultValue = "") String clienteCpf,
            @RequestParam(name = "pdv", defaultValue = "") String pdv,
            @RequestParam(name = "cupomResgate", defaultValue = "") String cupomResgate,
            @RequestParam(name = "voucherStatus", defaultValue = "") String voucherStatus,
            @RequestParam(name = "filialCnpj", defaultValue = "") String filialCnpj,
            @RequestParam(name = "tipoDesconto", defaultValue = "") String tipoDesconto,
            @RequestParam(name = "inicio", defaultValue = "") LocalDate inicio,
            @RequestParam(name = "fim", defaultValue = "") LocalDate fim) {


        return new ResponseEntity<VouchersPaginadaResponse>(
                this.voucherService.voucherFiltro(page, size, codigo, descricao, clienteCpf, pdv, cupomResgate,
                        inicio, fim, voucherStatus, filialCnpj, tipoDesconto), HttpStatus.OK);

    }
    @PostMapping(value =  "/print", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> printPromocoes(@RequestBody List<VoucherPrintRequest> prints){
        return new ResponseEntity<String>(this.voucherUtil.print(
                new JRBeanCollectionDataSource(prints), "vouchers"),
                HttpStatus.OK);

    }

}
