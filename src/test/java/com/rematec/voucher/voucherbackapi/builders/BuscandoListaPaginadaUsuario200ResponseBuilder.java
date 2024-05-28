package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;

import java.util.Collections;

import static com.rematec.voucher.voucherbackapi.builders.UsuarioApiResponseBuilder.umUsuarioApiResponse;

public class BuscandoListaPaginadaUsuario200ResponseBuilder {
    private BuscandoListaPaginadaUsuario200Response usuario200Response;
    private BuscandoListaPaginadaUsuario200ResponseBuilder(){}

    public static BuscandoListaPaginadaUsuario200ResponseBuilder umaListaPaganidaUsuarioResponse(){
        BuscandoListaPaginadaUsuario200ResponseBuilder builder = new BuscandoListaPaginadaUsuario200ResponseBuilder();
        builder.usuario200Response = new BuscandoListaPaginadaUsuario200Response();
        return builder;
    }
    public BuscandoListaPaginadaUsuario200ResponseBuilder comUmUsuario(){
        usuario200Response.setTotalPages(1);
        usuario200Response.setTotalElements(1);
        usuario200Response.setNumber(0);
        usuario200Response.setUsuarios(Collections.singletonList(umUsuarioApiResponse()
                .agora()));
        return this;
    }

    public BuscandoListaPaginadaUsuario200Response agora(){
        return usuario200Response;
    }
}
