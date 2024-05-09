package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.LojaUpdateApiRequest;

public class LojaUpdateApiRequestBuilder {

    private LojaUpdateApiRequest lojaUpdateApiRequest;

    private LojaUpdateApiRequestBuilder(){

    }

    public static LojaUpdateApiRequestBuilder umaLojaUpdateApiRequest(){
        LojaUpdateApiRequestBuilder builder = new LojaUpdateApiRequestBuilder();
        builder.lojaUpdateApiRequest = new LojaUpdateApiRequest();
        builder.lojaUpdateApiRequest.setNome("Loja 002");
        builder.lojaUpdateApiRequest.setIdentificacao("0002");
        builder.lojaUpdateApiRequest.setCnpj("22222222222222");
        builder.lojaUpdateApiRequest.setStatus(true);
        return builder;
    }

    public LojaUpdateApiRequestBuilder nome(String nome){
        lojaUpdateApiRequest.setNome(nome);
        return this;
    }

    public LojaUpdateApiRequestBuilder identificacao(String identificacao){
        lojaUpdateApiRequest.setIdentificacao(identificacao);
        return this;
    }

    public LojaUpdateApiRequestBuilder cnpj(String cnpj){
        lojaUpdateApiRequest.setCnpj(cnpj);
        return this;
    }

    public LojaUpdateApiRequestBuilder status(Boolean status){
        lojaUpdateApiRequest.setStatus(status);
        return this;
    }

    public LojaUpdateApiRequest agora(){
        return lojaUpdateApiRequest;
    }

}
