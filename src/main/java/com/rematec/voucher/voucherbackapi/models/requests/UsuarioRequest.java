package com.rematec.voucher.voucherbackapi.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest {
    @NotNull(message = "Nome do  Usuario é Obrigatorio")
    @NotBlank(message = "Nome do  Usuario é Obrigatorio")
    private String userName;
    @NotNull(message = "E-mail do  Usuario é Obrigatorio")
    @NotBlank(message = "E-mail do  Usuario é Obrigatorio")
    @Email(message = "E-mail inválido")
    private String email;
    @NotNull(message = "Senha do  Usuario é Obrigatorio")
    @NotBlank(message = "Senha do  Usuario é Obrigatorio")
    private String password;

    @NotNull(message = "Perfil do  Usuario é Obrigatorio")
    private Set<PerfilRequest> perfis ;

    @NotNull(message = "Status do  Usuario é Obrigatorio")
    private Boolean status;

   // @NotNull(message = "Perfil do  Usuario é Obrigatorio")
    private List<RoleRequest> roles;
}
