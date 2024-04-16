package com.rematec.voucher.voucherbackapi.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ConsultaVoucherResponse {

    private String guid;
    private String status;
    private List<VoucherResponse> vouchers;
}
