package com.rematec.voucher.voucherbackapi.interfaces.repositories.criteria;

import com.rematec.voucher.voucherbackapi.models.filter.PromocaoFiltro;
import com.rematec.voucher.voucherbackapi.models.response.PromocoesPaginadaResponse;
import org.springframework.data.domain.Pageable;

public interface IPromocaoRepositoryQuery {
    PromocoesPaginadaResponse filtrar(PromocaoFiltro promocaoFiltro, Pageable page);
}
