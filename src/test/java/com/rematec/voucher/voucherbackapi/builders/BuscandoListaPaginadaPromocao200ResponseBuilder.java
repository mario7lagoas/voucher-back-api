package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;

public class BuscandoListaPaginadaPromocao200ResponseBuilder {

    private BuscandoListaPaginadaPromocao200Response paginadaPromocao200Response;
    private BuscandoListaPaginadaPromocao200ResponseBuilder(){}

    public static BuscandoListaPaginadaPromocao200ResponseBuilder umBuscandoListaPaginadaPromocao200Response(){
        BuscandoListaPaginadaPromocao200ResponseBuilder builder = new BuscandoListaPaginadaPromocao200ResponseBuilder();
        builder.paginadaPromocao200Response = new BuscandoListaPaginadaPromocao200Response();

        return builder;
    }

    public BuscandoListaPaginadaPromocao200Response agora(){
        return paginadaPromocao200Response;
    }

}
