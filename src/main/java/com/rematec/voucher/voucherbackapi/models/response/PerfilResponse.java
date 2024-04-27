package com.rematec.voucher.voucherbackapi.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilResponse {
    private String guid;
    private String nome;
    private Boolean status;

    private List<RoleResponse> roles;

}
