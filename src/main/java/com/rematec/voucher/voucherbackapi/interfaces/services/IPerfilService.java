package com.rematec.voucher.voucherbackapi.interfaces.services;



import com.rematec.voucher.models.PerfilApiRequest;
import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.models.PerfilResumidoApiResponse;

import java.util.List;

public interface IPerfilService {

    List<PerfilApiResponse> buscandoListaPerfil();
    List<PerfilResumidoApiResponse> buscandoListaResumidoPerfil();
    PerfilApiResponse buscandoPerfilPeloGUID(String guid);
    PerfilApiResponse buscandoPerfilPeloNome(String nome);
    PerfilApiResponse criandoPerfil(PerfilApiRequest perfilApiRequest);
    PerfilApiResponse alterandoPerfil(String guid, PerfilApiRequest perfilApiRequest);
    void apagandoPerfil(String guid);

}
