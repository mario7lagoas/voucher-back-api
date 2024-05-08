package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.PerfilApiRequest;

import java.util.Arrays;

import static com.rematec.voucher.voucherbackapi.builders.RoleApiResponseBuilder.umRoleApiResponse;


public class PerfilApiRequestBuilder {

    private PerfilApiRequest perfilApiRequest;

    private PerfilApiRequestBuilder(){

    }

    public static PerfilApiRequestBuilder umPerfilApiRequest(){

        PerfilApiRequestBuilder builder = new  PerfilApiRequestBuilder();
        builder.perfilApiRequest = new PerfilApiRequest();
        builder.perfilApiRequest.setNome("Any Description");
        return builder;
    }
    public PerfilApiRequestBuilder setNome(String nome){
        perfilApiRequest.setNome(nome);
        return this;
    }

    public PerfilApiRequestBuilder comRoles(){
        perfilApiRequest.setRoles(Arrays.asList(umRoleApiResponse().agora()));
        return this;
    }

    public PerfilApiRequestBuilder rolesNull(){
        perfilApiRequest.setRoles(null);
        return this;
    }

    public PerfilApiRequestBuilder nomeNull(){
        perfilApiRequest.setNome(null);
        return this;
    }
    public PerfilApiRequestBuilder nomeEmpty(){
        perfilApiRequest.setNome("");
        return this;
    }

    public PerfilApiRequest agora(){
        return perfilApiRequest;
    }

}
