package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.models.AtivandoPromocaoRequest;
import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.models.PromocaoApiRequest;
import com.rematec.voucher.models.PromocaoApiResponse;
import com.rematec.voucher.models.PromocaoUpdateApiRequest;
import com.rematec.voucher.voucherbackapi.factories.ReportFactory;
import com.rematec.voucher.voucherbackapi.services.VoucherBackFacade;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PromocaoController implements PromocaoApi{

    @Autowired
    private VoucherBackFacade promocaoService;

    @Override
    public ResponseEntity<List<PromocaoApiResponse>> buscandoListaPromocao() {
        return new ResponseEntity<List<PromocaoApiResponse>>(
                this.promocaoService.buscandoListaPromocao(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BuscandoListaPaginadaPromocao200Response> buscandoListaPaginadaPromocao(Integer page,
                                                                                                  Integer size,
                                                                                                  String descricao) {
        return new ResponseEntity<BuscandoListaPaginadaPromocao200Response>(
                this.promocaoService.buscandoListaPaginadaPromocao(descricao, page, size), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PromocaoApiResponse> buscandoPromocaoPeloGUID(String guid) {
        return new ResponseEntity<PromocaoApiResponse>(this.promocaoService.buscandoPromocaoPeloGUID(guid), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PromocaoApiResponse> criandoPromocao(PromocaoApiRequest promocaoApiRequest) {
        return new ResponseEntity<PromocaoApiResponse>(
                this.promocaoService.criandoPromocao(promocaoApiRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> ativandoPromocao(String guid, AtivandoPromocaoRequest ativandoPromocaoRequest) {
        this.promocaoService.ativandoPromocao(guid, ativandoPromocaoRequest.getAutorAlteracao());
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<PromocaoApiResponse> alterandoPromocao(String guid, PromocaoUpdateApiRequest promocaoUpdateApiRequest) {
        return new ResponseEntity<PromocaoApiResponse>(
                this.promocaoService.alterandoPromocao(guid, promocaoUpdateApiRequest), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<BuscandoListaPaginadaPromocao200Response> buscandoListaFiltroPromocao(Integer page,
                                                                                                Integer size,
                                                                                                String descricao,
                                                                                                String status,
                                                                                                String tipo,
                                                                                                String inicio,
                                                                                                String fim,
                                                                                                String email) {
        return new ResponseEntity<BuscandoListaPaginadaPromocao200Response>(
                this.promocaoService.buscandoListaFiltroPromocao(descricao, tipo, status, inicio, fim, page, size, email),
                HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> apagandoPromocao(String guid) {
        this.promocaoService.apagandoPromocao(guid);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<String> relatorioPromocao(List<PromocaoApiResponse> promocaoApiResponse) {
        return new ResponseEntity<String>(
                this.promocaoService.report(new JRBeanCollectionDataSource(promocaoApiResponse), "promocoes"),
                HttpStatus.OK);
    }

}
