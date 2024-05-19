package com.rematec.voucher.voucherbackapi.interfaces.services;

import com.rematec.voucher.models.ConsultaVoucherApiRequest;
import com.rematec.voucher.models.ConsultaVoucherApiResponse;
import com.rematec.voucher.models.VoucherApiRequest;
import com.rematec.voucher.models.VoucherFinalizeApiRequest;
import com.rematec.voucher.models.VoucherPromocaoApiRequest;
import com.rematec.voucher.models.VoucherPromocaoApiResponse;
import com.rematec.voucher.voucherbackapi.models.requests.ConsultaVoucherRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherFinalizeRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherPromocaoRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherRequest;
import com.rematec.voucher.voucherbackapi.models.response.ConsultaVoucherResponse;
import com.rematec.voucher.voucherbackapi.models.response.VoucherPromocaoResponse;
import com.rematec.voucher.voucherbackapi.models.response.VouchersPaginadaResponse;

import java.time.LocalDate;
import java.util.List;

public interface IVoucherService {

    ConsultaVoucherApiResponse consultandoPromocoes(ConsultaVoucherApiRequest consultaVoucherApiRequest);
    void confirmandoVoucher(List<VoucherApiRequest> voucherApiRequest);
    void cancelandoVoucher(List<VoucherApiRequest> voucherApiRequest);
    VoucherPromocaoApiResponse resgatandoVoucher(VoucherPromocaoApiRequest voucherPromocaoApiRequest);
    void consumindoVoucher(VoucherFinalizeApiRequest voucherFinalizeApiRequest);
    void estornandoVoucher(VoucherFinalizeApiRequest voucherFinalizeApiRequest);

    ConsultaVoucherResponse consultarPromocoes(ConsultaVoucherRequest consulta);

    void confirmarVoucher(List<VoucherRequest> voucherRequests);
    void cancelarVoucher(List<VoucherRequest> voucherRequests);
    void rollback(VoucherFinalizeRequest voucher);
    void consumer(VoucherFinalizeRequest voucher);
    VoucherPromocaoResponse resgateVoucher(VoucherPromocaoRequest promocaoRequest);
    VouchersPaginadaResponse voucherFiltro(int page, int size, String codigo, String descricao, String clienteCpf
            , String pdv, String cupomResgate, LocalDate inicio, LocalDate fim, String voucherStatus, String filialCnpj,
                                           String tipoDesconto);



}
