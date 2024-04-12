package com.rematec.voucher.voucherbackapi.interfaces.services;

import com.rematec.voucher.voucherbackapi.models.requests.PromocaoPrintRequest;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoRequest;
import com.rematec.voucher.voucherbackapi.models.requests.PromocaoUpdateRequest;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocoesPaginadaResponse;

import java.time.LocalDate;
import java.util.List;

public interface IPromocaoService {

    List<PromocaoResponse> getAllPromocoes();
    PromocaoResponse addPromocao(PromocaoRequest promocaoRequest);
    PromocaoResponse alterarPromocao(String guid, PromocaoUpdateRequest promocaoUpdateRequest);
    void apagarPromocao(String guid);
    PromocaoResponse buscarPromocaoByGuid(String guid);
    PromocoesPaginadaResponse obterPromocoesPaginadas(String descricao, int page, int size);
    PromocoesPaginadaResponse promocaoFiltro(String descricao, String promocaoStatus, LocalDate inicio,
                                             LocalDate fim, int page, int size);
    String printPromocoes(List<PromocaoPrintRequest> prints);
    void ativarPromocao(String guid);

}
