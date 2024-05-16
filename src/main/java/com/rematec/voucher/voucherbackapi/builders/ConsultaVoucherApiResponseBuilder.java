package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.ConsultaVoucherApiResponse;
import com.rematec.voucher.models.VoucherApiResponse;

import java.util.List;

public class ConsultaVoucherApiResponseBuilder {

    private ConsultaVoucherApiResponse consultaVoucherApiResponse;

    private ConsultaVoucherApiResponseBuilder(){};

    public static ConsultaVoucherApiResponseBuilder builder() {

        ConsultaVoucherApiResponseBuilder builder = new ConsultaVoucherApiResponseBuilder();
        builder.consultaVoucherApiResponse = new ConsultaVoucherApiResponse();
        return builder;
    }

    public ConsultaVoucherApiResponseBuilder status(String status){
        this.consultaVoucherApiResponse.setStatus(status);
        return this;
    }
    public ConsultaVoucherApiResponseBuilder totalVoucher(Integer total){
        this.consultaVoucherApiResponse.setTotalVoucher(total);
        return this;
    }

    public ConsultaVoucherApiResponseBuilder vouchers(List<VoucherApiResponse> list){
        this.consultaVoucherApiResponse.setVouchers(list);
        return this;
    }


    public ConsultaVoucherApiResponse build(){
        return consultaVoucherApiResponse;
    }

}
