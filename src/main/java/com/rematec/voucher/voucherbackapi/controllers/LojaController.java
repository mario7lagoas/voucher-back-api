package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.models.BuscandoListaPaginadaLoja200Response;
import com.rematec.voucher.models.LojaApiRequest;
import com.rematec.voucher.models.LojaApiResponse;
import com.rematec.voucher.models.LojaUpdateApiRequest;
import com.rematec.voucher.models.UpdateStatusApiRequest;
import com.rematec.voucher.voucherbackapi.services.LojaServiceImpl;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LojaController  implements LojaApi{

    @Autowired
    private LojaServiceImpl lojaService;

    @Autowired
    private VoucherUtil voucherUtil;

    @Override
    public ResponseEntity<List<LojaApiResponse>> buscandoListaLoja() {
        return new ResponseEntity<List<LojaApiResponse>>(lojaService.buscandoListaLoja(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BuscandoListaPaginadaLoja200Response> buscandoListaPaginadaLoja(Integer page, Integer size,
                                                                                          String cnpj) {
        return new ResponseEntity<BuscandoListaPaginadaLoja200Response>(
                this.lojaService.buscandoListaPaginadaLoja(cnpj, page,size), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<List<LojaApiResponse>> buscandoListaLojaAtiva() {
        return new ResponseEntity<List<LojaApiResponse>>(this.lojaService.buscandoListaLojaAtiva(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LojaApiResponse> buscandoLojaPeloGUID(String guid) {
        return new ResponseEntity<LojaApiResponse>(this.lojaService.buscandoLojaPeloGUID(guid), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LojaApiResponse> criandoLoja(LojaApiRequest lojaApiRequest) {
        return new ResponseEntity<LojaApiResponse>(this.lojaService.criandoLoja(lojaApiRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<LojaApiResponse> alterandoLoja(String guid, LojaUpdateApiRequest lojaApiRequest) {
        return new ResponseEntity<LojaApiResponse>(
                this.lojaService.alterandoLoja(guid, lojaApiRequest), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Void> apagandoLoja(String guid) {
        this.lojaService.pagandoLoja(guid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> alterandoStatusLoja(String guid, UpdateStatusApiRequest updateStatusApiRequest) {
        this.lojaService.alterandoStatusLoja(guid, updateStatusApiRequest);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }


    @Override
    public ResponseEntity<String> relatorioLoja(List<LojaApiResponse> lojaApiResponse) {
        return new ResponseEntity<String>(this.voucherUtil.print(new JRBeanCollectionDataSource(lojaApiResponse), "lojas")
                , HttpStatus.OK );
    }

}
