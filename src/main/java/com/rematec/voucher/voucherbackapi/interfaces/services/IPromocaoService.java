package com.rematec.voucher.voucherbackapi.interfaces.services;

import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.models.PromocaoApiRequest;
import com.rematec.voucher.models.PromocaoApiResponse;
import com.rematec.voucher.models.PromocaoUpdateApiRequest;
import java.util.List;

public interface IPromocaoService {

    List<PromocaoApiResponse> buscandoListaPromocao();
    BuscandoListaPaginadaPromocao200Response buscandoListaPaginadaPromocao(String descricao, Integer page, Integer size);
    PromocaoApiResponse criandoPromocao(PromocaoApiRequest promocaoApiRequest);
    PromocaoApiResponse buscandoPromocaoPeloGUID(String guid);
    void ativandoPromocao(String guid, String autorAlteracao);
    PromocaoApiResponse alterandoPromocao(String guid, PromocaoUpdateApiRequest promocaoUpdateApiRequest);
    BuscandoListaPaginadaPromocao200Response buscandoListaFiltroPromocao(String descricao, String tipo, String status,
                                                                         String inicio, String fim, Integer page,
                                                                         Integer size);
    void apagandoPromocao(String guid);

}
