package com.rematec.voucher.voucherbackapi.interfaces.services;

import com.rematec.voucher.models.BuscandoListaPaginadaLoja200Response;
import com.rematec.voucher.models.LojaApiRequest;
import com.rematec.voucher.models.LojaApiResponse;
import com.rematec.voucher.models.LojaUpdateApiRequest;
import com.rematec.voucher.models.UpdateStatusApiRequest;

import java.util.List;

public interface ILojaService {

    List<LojaApiResponse> buscandoListaLoja();

    BuscandoListaPaginadaLoja200Response buscandoListaPaginadaLoja(String cnpj, Integer page, Integer size);

    List<LojaApiResponse> buscandoListaLojaAtiva(String email);

    LojaApiResponse buscandoLojaPeloGUID(String guid);

    LojaApiResponse criandoLoja(LojaApiRequest lojaApiRequest);

    LojaApiResponse alterandoLoja(String guid, LojaUpdateApiRequest lojaApiRequest);

    void apagandoLoja(String guid);

    void alterandoStatusLoja(String guid, UpdateStatusApiRequest updateStatusApiRequest);
}
