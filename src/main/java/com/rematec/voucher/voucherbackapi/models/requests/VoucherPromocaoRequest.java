package com.rematec.voucher.voucherbackapi.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class VoucherPromocaoRequest {
    @NotNull(message = "Codigo do Voucher não pode ser nulo.")
    @NotBlank(message = "Codigo do Voucher não pode ser vazio.")
    private String codigo;
    @NotNull(message = "CNPJ da filial não pode ser nulo.")
    @NotBlank(message = "CNPJ da filial não pode ser vazio.")
    private String filialCnpj;
    @NotNull(message = "Cupom não pode ser nulo.")
    @NotBlank(message = "Cupom não pode ser vazio.")
    private String cupom;
    @NotNull(message = "Valor da compra nao pode ser nulo.")
    private BigDecimal valorCompra;
}
