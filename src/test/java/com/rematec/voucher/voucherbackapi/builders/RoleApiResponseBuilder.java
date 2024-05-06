package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.RoleApiResponse;

public class RoleApiResponseBuilder {

    private RoleApiResponse roleApiResponse;

    private RoleApiResponseBuilder(){}

    public static RoleApiResponseBuilder umRoleApiResponse(){

        RoleApiResponseBuilder builder = new RoleApiResponseBuilder();
        builder.roleApiResponse = new RoleApiResponse();
        builder.roleApiResponse.setNome("MENU_LOJA");
        return builder;
    }


    public RoleApiResponse agora(){
        return roleApiResponse;
    }

}
