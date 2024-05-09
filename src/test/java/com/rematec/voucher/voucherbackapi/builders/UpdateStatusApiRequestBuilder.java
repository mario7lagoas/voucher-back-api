package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.UpdateStatusApiRequest;

public class UpdateStatusApiRequestBuilder {

    private UpdateStatusApiRequest updateStatusApiRequest;

    private UpdateStatusApiRequestBuilder(){}

    public static UpdateStatusApiRequestBuilder umUpdateStatusApiRequest(){

        UpdateStatusApiRequestBuilder builder = new UpdateStatusApiRequestBuilder();
        builder.updateStatusApiRequest = new UpdateStatusApiRequest();
        builder.updateStatusApiRequest.setStatus(true);
        return builder;
    }


    public UpdateStatusApiRequestBuilder status(Boolean status){
        updateStatusApiRequest.setStatus(status);
        return this;
    }

    public UpdateStatusApiRequest agora(){
        return updateStatusApiRequest;
    }

}
