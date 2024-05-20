package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.models.BuscandoListaFiltroVoucher200Response;
import com.rematec.voucher.models.ConsultaVoucherApiRequest;
import com.rematec.voucher.models.ConsultaVoucherApiResponse;
import com.rematec.voucher.models.VoucherApiRequest;
import com.rematec.voucher.models.VoucherFiltroApiResponse;
import com.rematec.voucher.models.VoucherFinalizeApiRequest;
import com.rematec.voucher.models.VoucherPromocaoApiRequest;
import com.rematec.voucher.models.VoucherPromocaoApiResponse;
import com.rematec.voucher.voucherbackapi.services.VoucherBackFacade;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VoucherController implements VoucherApi {

    @Autowired
    private VoucherBackFacade voucherService;

    @Override
    public ResponseEntity<ConsultaVoucherApiResponse> consultandoPromocoes(ConsultaVoucherApiRequest consultaVoucherApiRequest) {
        return new ResponseEntity<ConsultaVoucherApiResponse>(
                this.voucherService.consultandoPromocoes(consultaVoucherApiRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> confirmandoVoucher(List<VoucherApiRequest> voucherApiRequest) {
        this.voucherService.confirmandoVoucher(voucherApiRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> cancelandoVoucher(List<VoucherApiRequest> voucherApiRequest) {
        this.voucherService.cancelandoVoucher(voucherApiRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<VoucherPromocaoApiResponse> resgatandoVoucher(VoucherPromocaoApiRequest voucherPromocaoApiRequest) {
        return new ResponseEntity<VoucherPromocaoApiResponse>(
                this.voucherService.resgatandoVoucher(voucherPromocaoApiRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> consumindoVoucher(VoucherFinalizeApiRequest voucherFinalizeApiRequest) {
        this.voucherService.consumindoVoucher(voucherFinalizeApiRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> estornandoVoucher(VoucherFinalizeApiRequest voucherFinalizeApiRequest) {
        this.voucherService.estornandoVoucher(voucherFinalizeApiRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BuscandoListaFiltroVoucher200Response> buscandoListaFiltroVoucher(
            Integer page, Integer size, String codigo, String descricao, String clienteCpf, String pdv, String cupomResgate,
            String voucherStatus, String filialCnpj, String tipoDesconto, String inicio, String fim) {

        return new ResponseEntity<BuscandoListaFiltroVoucher200Response>(
                this.voucherService.buscandoListaFiltroVoucher(page, size, codigo, descricao, clienteCpf, pdv, cupomResgate,
                        inicio, fim, voucherStatus, filialCnpj, tipoDesconto), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<String> relatorioVoucher(List<VoucherFiltroApiResponse> prints) {
        return new ResponseEntity<String>(
                this.voucherService.report(new JRBeanCollectionDataSource(prints), "vouchers"), HttpStatus.OK);
    }
}
