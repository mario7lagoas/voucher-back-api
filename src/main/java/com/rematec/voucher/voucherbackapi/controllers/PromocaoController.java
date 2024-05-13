package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.models.PromocaoApiRequest;
import com.rematec.voucher.models.PromocaoApiResponse;
import com.rematec.voucher.voucherbackapi.factories.ReportFactory;
import com.rematec.voucher.voucherbackapi.models.requests.AutorRequest;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoPrintRequest;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoRequest;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoUpdateRequest;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocoesPaginadaResponse;
import com.rematec.voucher.voucherbackapi.services.PromocaoService;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/promocao")
public class PromocaoController implements PromocaoApi{

    @Autowired
    private PromocaoService promocaoService;

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
    public ResponseEntity<PromocaoApiResponse> criandoPromocao(PromocaoApiRequest promocaoApiRequest) {
        return new ResponseEntity<PromocaoApiResponse>(
                this.promocaoService.criandoPromocao(promocaoApiRequest), HttpStatus.CREATED);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PromocaoResponse>> getAll() {
        return new ResponseEntity<>(this.promocaoService.getAllPromocoes(), HttpStatus.OK);
    }

    @GetMapping(value = "/paginada", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PromocoesPaginadaResponse> getAllPromocoesPaginada(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "descricao", defaultValue = "") String descricao) {

        return new ResponseEntity<PromocoesPaginadaResponse>(this.promocaoService
                .obterPromocoesPaginadas(descricao, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PromocoesPaginadaResponse> getAllPromocoesFilter(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "descricao", defaultValue = "") String descricao,
            @RequestParam(name = "status", defaultValue = "") String promocaoStatus,
            @RequestParam(name = "tipo", defaultValue = "") String tipoDesconto,
            @RequestParam(name = "inicio", defaultValue = "") LocalDate inicio,
            @RequestParam(name = "fim", defaultValue = "") LocalDate fim) {

        return new ResponseEntity<PromocoesPaginadaResponse>(this.promocaoService
                .promocaoFiltro(descricao, tipoDesconto, promocaoStatus, inicio, fim, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PromocaoResponse> buscarPromocaiPorGuid(@PathVariable("guid") String guid) {
        return new ResponseEntity<>(this.promocaoService.buscarPromocaoByGuid(guid), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PromocaoResponse> addPromocao(@RequestBody @Valid PromocaoRequest promocaoRequest) {
        return new ResponseEntity<>(this.promocaoService.addPromocao(promocaoRequest), HttpStatus.CREATED);
    }

    @PutMapping(value = "{guid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PromocaoResponse> editPromocao(@PathVariable("guid") String guid,
                                                         @RequestBody @Valid PromocaoUpdateRequest promocaoUpdateRequest) {
        return new ResponseEntity<>(this.promocaoService.alterarPromocao(guid, promocaoUpdateRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "{guid}")
    public ResponseEntity deletePromocao(@PathVariable("guid") String guid) {
        this.promocaoService.apagarPromocao(guid);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/print", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> printPromocoes(@RequestBody List<PromocaoPrintRequest> prints) {

        return new ResponseEntity<String>(
                ReportFactory.report(new JRBeanCollectionDataSource(prints), "promocoes"), HttpStatus.OK);
    }

    @PatchMapping(value = "{guid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> ativarPromocao(@PathVariable("guid") String guid,
                                               @RequestBody AutorRequest autorRequest) {

        this.promocaoService.ativarPromocao(guid, autorRequest.getAutorAlteracao());
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }


}
