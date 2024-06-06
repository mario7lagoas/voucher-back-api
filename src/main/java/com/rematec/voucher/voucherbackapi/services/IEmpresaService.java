package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.EmpresaApiRequest;
import com.rematec.voucher.models.EmpresaApiResponse;

import java.util.List;

public interface IEmpresaService {

    List<EmpresaApiResponse> buscandoListaEmpresa();
    EmpresaApiResponse criandoEmpresa(EmpresaApiRequest empresaApiRequest);
    EmpresaApiResponse buscandoEmpresaPeloGUID(String guid);

}
