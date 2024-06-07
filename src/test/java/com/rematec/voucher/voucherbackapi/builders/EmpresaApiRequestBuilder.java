package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.EmpresaApiRequest;

public class EmpresaApiRequestBuilder {
    private EmpresaApiRequest empresaApiRequest;
    private EmpresaApiRequestBuilder(){}

    public static EmpresaApiRequestBuilder umaEmpresaApiRequest(){
        EmpresaApiRequestBuilder builder = new EmpresaApiRequestBuilder();
        builder.empresaApiRequest = new EmpresaApiRequest();
        builder.empresaApiRequest.setNome("Any Empresa");
        builder.empresaApiRequest.setIdentificacao("C0001");

        return builder;
    }

    public EmpresaApiRequestBuilder nome(String nome){
        this.empresaApiRequest.setNome(nome);
        return this;
    }

    public EmpresaApiRequest agora(){
        return empresaApiRequest;
    }
}
