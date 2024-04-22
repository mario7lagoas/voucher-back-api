package com.rematec.voucher.voucherbackapi.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VoucherRequest {
    @NotNull(message = "Codigo do Voucher não pode ser nulo.")
    @NotBlank(message = "Codigo do Voucher não pode ser vazio.")
    private String codigo;

    @NotNull(message = "CNPJ da filial não pode ser nulo.")
    @NotBlank(message = "CNPJ da filial não pode ser vazio.")
    private String filialCnpj;

    @NotNull(message = "CPF do Cliente não pode ser nulo.")
    @NotBlank(message = "CPF do Cliente não pode ser vazio.")
    private String clienteCpf;
}
