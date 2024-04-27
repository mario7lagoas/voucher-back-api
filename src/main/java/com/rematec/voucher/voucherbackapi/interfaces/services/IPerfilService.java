package com.rematec.voucher.voucherbackapi.interfaces.services;

import com.rematec.voucher.voucherbackapi.models.requests.PerfilRequest;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResumidoResponse;

import java.util.List;

public interface IPerfilService {

    List<PerfilResponse> getAllPerfil();
    List<PerfilResumidoResponse> getAllPerfilResumido();
    PerfilResponse addPerfil(PerfilRequest request);
    PerfilResponse getPerfilNome(String nome);
    PerfilResponse getPerfilGuid(String guid);
    PerfilResponse alterarPerfil(String guid, PerfilRequest request);
    void apagarPerfil(String guid);

}
