package com.rematec.voucher.voucherbackapi.interfaces.services;

import com.rematec.voucher.voucherbackapi.models.requests.LojaRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UpdateStatusResquest;
import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import com.rematec.voucher.voucherbackapi.models.response.LojasPaginadaResponse;

import java.util.List;

public interface ILojaService {

    LojaResponse addLoja(LojaRequest lojaRequest);
    List<LojaResponse> getAll();
    LojaResponse updateLoja(String guid, LojaRequest lojaRequest);
    void apagarLoja(String guid);
    LojaResponse buscarLojaByGuid(String guid);
    LojaResponse updateStatus(String guid, UpdateStatusResquest statusResquest);
    List<LojaResponse> getLojasAtivas();
    LojasPaginadaResponse obterLojasPaginadas(String nome, int page, int size);

}
