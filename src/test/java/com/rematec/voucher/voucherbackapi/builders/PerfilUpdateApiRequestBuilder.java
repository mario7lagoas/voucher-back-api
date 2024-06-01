package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.PerfilApiRequest;
import com.rematec.voucher.models.PerfilUpdateApiRequest;

import java.util.Arrays;

import static com.rematec.voucher.voucherbackapi.builders.RoleApiResponseBuilder.umRoleApiResponse;


public class PerfilUpdateApiRequestBuilder {

    private PerfilUpdateApiRequest perfilApiRequest;

    private PerfilUpdateApiRequestBuilder(){

    }

    public static PerfilUpdateApiRequestBuilder umPerfilUpdateApiRequest(){

        PerfilUpdateApiRequestBuilder builder = new PerfilUpdateApiRequestBuilder();
        builder.perfilApiRequest = new PerfilUpdateApiRequest();
        builder.perfilApiRequest.setNome("Any Description");
        return builder;
    }
    public PerfilUpdateApiRequestBuilder nome(String nome){
        perfilApiRequest.setNome(nome);
        return this;
    }

    public PerfilUpdateApiRequestBuilder comRoles(){
        perfilApiRequest.setRoles(Arrays.asList(umRoleApiResponse().agora()));
        return this;
    }

    public PerfilUpdateApiRequestBuilder rolesNull(){
        perfilApiRequest.setRoles(null);
        return this;
    }

    public PerfilUpdateApiRequestBuilder nomeNull(){
        perfilApiRequest.setNome(null);
        return this;
    }
    public PerfilUpdateApiRequestBuilder nomeEmpty(){
        perfilApiRequest.setNome("");
        return this;
    }

    public PerfilUpdateApiRequest agora(){
        return perfilApiRequest;
    }

}
