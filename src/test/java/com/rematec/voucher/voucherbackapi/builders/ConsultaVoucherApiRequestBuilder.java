package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.ConsultaVoucherApiRequest;

import java.math.BigDecimal;

public class ConsultaVoucherApiRequestBuilder {
    private ConsultaVoucherApiRequest consultaVoucherApiRequest;
    private ConsultaVoucherApiRequestBuilder(){}

    public static ConsultaVoucherApiRequestBuilder umaConsultaVoucherApiRequest(){
        ConsultaVoucherApiRequestBuilder builder = new ConsultaVoucherApiRequestBuilder();
        builder.consultaVoucherApiRequest = new ConsultaVoucherApiRequest();
        builder.consultaVoucherApiRequest.setClienteCpf("04372430604");
        builder.consultaVoucherApiRequest.setCupom("123456");
        builder.consultaVoucherApiRequest.setFilialCnpj("11111111111111");
        builder.consultaVoucherApiRequest.setPdvFilial("001");
        builder.consultaVoucherApiRequest.setValorCompra(BigDecimal.valueOf(50.0));
        return builder;
    }

    public ConsultaVoucherApiRequest agora(){
        return consultaVoucherApiRequest;
    }
}
