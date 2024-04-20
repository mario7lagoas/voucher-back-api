package com.rematec.voucher.voucherbackapi.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private String status;
    private List<Map<String, String>> erros;
}
