package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.UpdateStatusApiRequest;
import com.rematec.voucher.models.UsuarioApiRequest;
import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.models.UsuarioUpdateApiRequest;

import java.util.List;

public interface IUsuarioService {

    List<UsuarioApiResponse> buscandoListaUsuario();

    BuscandoListaPaginadaUsuario200Response buscandoListaPaginadaUsuario(String nome, Integer page, Integer size);

    UsuarioApiResponse buscandoUsuarioPeloGUID(String guid);

    UsuarioApiResponse criandoUsuario(UsuarioApiRequest usuarioApiRequest);

    UsuarioApiResponse alterandoUsuario(String guid, UsuarioUpdateApiRequest usuarioUpdateApiRequest);

    void apagandoUsuario(String guid);

    void alterandoStatusUsuario(String guid, UpdateStatusApiRequest updateStatusApiRequest);

}
