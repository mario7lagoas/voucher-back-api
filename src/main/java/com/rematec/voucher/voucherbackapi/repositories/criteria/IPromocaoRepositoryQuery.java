package com.rematec.voucher.voucherbackapi.repositories.criteria;

import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.voucherbackapi.models.filter.PromocaoFiltro;
import org.springframework.data.domain.Pageable;

public interface IPromocaoRepositoryQuery {
    BuscandoListaPaginadaPromocao200Response filtrar(PromocaoFiltro promocaoFiltro, Pageable page);
}
