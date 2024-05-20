package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaFiltroVoucher200Response;
import com.rematec.voucher.models.ConsultaVoucherApiRequest;
import com.rematec.voucher.models.ConsultaVoucherApiResponse;
import com.rematec.voucher.models.VoucherApiRequest;
import com.rematec.voucher.models.VoucherFinalizeApiRequest;
import com.rematec.voucher.models.VoucherPromocaoApiRequest;
import com.rematec.voucher.models.VoucherPromocaoApiResponse;
import java.util.List;

public interface IVoucherService {

    ConsultaVoucherApiResponse consultandoPromocoes(ConsultaVoucherApiRequest consultaVoucherApiRequest);
    void confirmandoVoucher(List<VoucherApiRequest> voucherApiRequest);
    void cancelandoVoucher(List<VoucherApiRequest> voucherApiRequest);
    VoucherPromocaoApiResponse resgatandoVoucher(VoucherPromocaoApiRequest voucherPromocaoApiRequest);
    void consumindoVoucher(VoucherFinalizeApiRequest voucherFinalizeApiRequest);
    void estornandoVoucher(VoucherFinalizeApiRequest voucherFinalizeApiRequest);
    BuscandoListaFiltroVoucher200Response buscandoListaFiltroVoucher(Integer page, Integer size, String codigo,
                                                                     String descricao, String clienteCpf, String pdv,
                                                                     String cupomResgate, String inicio, String fim,
                                                                     String voucherStatus, String filialCnpj,
                                                                     String tipoDesconto);
}
