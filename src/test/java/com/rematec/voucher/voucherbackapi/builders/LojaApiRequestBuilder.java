package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.LojaApiRequest;

public class LojaApiRequestBuilder {

    private LojaApiRequest lojaApiRequest;
    
    private LojaApiRequestBuilder(){}

    public static LojaApiRequestBuilder umaLojaApiRequest(){

        LojaApiRequestBuilder builder = new LojaApiRequestBuilder();
        builder.lojaApiRequest = new LojaApiRequest();
        builder.lojaApiRequest.setNome("Loja001");
        builder.lojaApiRequest.setIdentificacao("0001");
        builder.lojaApiRequest.setCnpj("42492701000135");
        builder.lojaApiRequest.setStatus(true);
        return builder;
    }
    public LojaApiRequestBuilder nome(String nome){
        lojaApiRequest.setNome(nome);
        return this;
    }

    public LojaApiRequestBuilder identificacao(String identificacao){
        lojaApiRequest.setIdentificacao(identificacao);
        return this;
    }

    public LojaApiRequestBuilder cnpj(String cnpj){
        lojaApiRequest.setCnpj(cnpj);
        return this;
    }

    public LojaApiRequestBuilder status(Boolean status){
        lojaApiRequest.setStatus(status);
        return this;
    }

    public LojaApiRequest agora(){
        return lojaApiRequest;
    }

}
