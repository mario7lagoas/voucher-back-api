package com.rematec.voucher.voucherbackapi.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PerfilResponse {
    private String guid;
    private String nome;
    private Boolean status;

    private List<RuleResponse> roles;

}
