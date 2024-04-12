package com.rematec.voucher.voucherbackapi.interfaces.services;

import com.rematec.voucher.voucherbackapi.models.requests.UsuarioPrintRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UsuarioRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UpdateStatusResquest;
import com.rematec.voucher.voucherbackapi.models.response.UsuarioResponse;
import com.rematec.voucher.voucherbackapi.models.response.UsuariosPaginadaResponse;

import java.util.List;

public interface IUsuarioService {
    List<UsuarioResponse> getAllUsuarios();
    UsuarioResponse addUsuario(UsuarioRequest usuarioRequest);
    UsuarioResponse updateUsuario(String guid, UsuarioRequest usuarioRequest);
    UsuarioResponse buscarUsuarioByGuid(String guid);
    void apagarUsuario(String guid);
    UsuarioResponse updateStatus(String guid, UpdateStatusResquest statusResquest);
    UsuariosPaginadaResponse obterUsuarioPaginadas(String nome, int page, int size);
    String printUsuarios(List<UsuarioPrintRequest> prints);

}
