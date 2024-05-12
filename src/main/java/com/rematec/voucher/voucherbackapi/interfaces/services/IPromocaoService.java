package com.rematec.voucher.voucherbackapi.interfaces.services;

import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.models.PromocaoApiResponse;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoRequest;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoUpdateRequest;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocoesPaginadaResponse;

import java.time.LocalDate;
import java.util.List;

public interface IPromocaoService {

    List<PromocaoApiResponse> buscandoListaPromocao();
    BuscandoListaPaginadaPromocao200Response buscandoListaPaginadaPromocao(String descricao, Integer page, Integer size);


    List<PromocaoResponse> getAllPromocoes();
    PromocaoResponse addPromocao(PromocaoRequest promocaoRequest);
    PromocaoResponse alterarPromocao(String guid, PromocaoUpdateRequest promocaoUpdateRequest);
    void apagarPromocao(String guid);
    PromocaoResponse buscarPromocaoByGuid(String guid);
    PromocoesPaginadaResponse obterPromocoesPaginadas(String descricao, int page, int size);
    PromocoesPaginadaResponse promocaoFiltro(String descricao, String tipoDesconto, String promocaoStatus, LocalDate inicio,
                                             LocalDate fim, int page, int size);
    void ativarPromocao(String guid, String nomeAutorizador);


}
