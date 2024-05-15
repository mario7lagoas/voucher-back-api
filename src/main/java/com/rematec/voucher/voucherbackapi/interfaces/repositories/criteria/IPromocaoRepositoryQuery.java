package com.rematec.voucher.voucherbackapi.interfaces.repositories.criteria;

import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.voucherbackapi.models.filter.PromocaoFiltro;
import com.rematec.voucher.voucherbackapi.models.response.PromocoesPaginadaResponse;
import org.springframework.data.domain.Pageable;

public interface IPromocaoRepositoryQuery {
    BuscandoListaPaginadaPromocao200Response filtrar(PromocaoFiltro promocaoFiltro, Pageable page);
}
