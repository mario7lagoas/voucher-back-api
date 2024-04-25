package com.rematec.voucher.voucherbackapi.interfaces.repositories.criteria;

import com.rematec.voucher.voucherbackapi.models.filter.VoucherFiltro;
import com.rematec.voucher.voucherbackapi.models.response.VouchersPaginadaResponse;
import org.springframework.data.domain.Pageable;

public interface IVoucherRepositoryQuery {
    VouchersPaginadaResponse filtrar(VoucherFiltro voucherFiltro, Pageable page);
}
