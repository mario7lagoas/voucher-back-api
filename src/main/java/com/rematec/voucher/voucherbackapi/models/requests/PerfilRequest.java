package com.rematec.voucher.voucherbackapi.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerfilRequest {

    @NotBlank(message = "Nome do Perfil é Obrigatorio")
    private String nome;
    @NotNull(message = "Permissões do Perfil é Obrigatorio")
    private List<RoleRequest> roles;
}
