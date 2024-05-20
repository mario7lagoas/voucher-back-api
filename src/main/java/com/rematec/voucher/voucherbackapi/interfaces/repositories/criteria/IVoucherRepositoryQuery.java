package com.rematec.voucher.voucherbackapi.interfaces.repositories.criteria;

import com.rematec.voucher.models.BuscandoListaFiltroVoucher200Response;
import com.rematec.voucher.voucherbackapi.models.filter.VoucherFiltro;
import org.springframework.data.domain.Pageable;

public interface IVoucherRepositoryQuery {
    BuscandoListaFiltroVoucher200Response filtrar(VoucherFiltro voucherFiltro, Pageable page);
}
