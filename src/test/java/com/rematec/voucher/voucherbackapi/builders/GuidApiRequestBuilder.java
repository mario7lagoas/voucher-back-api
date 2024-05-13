package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.models.GuidApiRequest;

public class GuidApiRequestBuilder {

    private GuidApiRequest request;

    private GuidApiRequestBuilder() {
    }

    public static GuidApiRequestBuilder umGuidApiRequest() {

        GuidApiRequestBuilder builder = new GuidApiRequestBuilder();
        builder.request = new GuidApiRequest();
        builder.request.setGuid("aaaa-bbbb-cccc-dddd-eeeee");
        return builder;
    }

    public GuidApiRequestBuilder guid(String guid) {
        request.setGuid(guid);
        return this;
    }

    public GuidApiRequest agora() {
        return request;
    }

}
