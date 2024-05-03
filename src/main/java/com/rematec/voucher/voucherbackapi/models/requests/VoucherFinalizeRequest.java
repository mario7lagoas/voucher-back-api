package com.rematec.voucher.voucherbackapi.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherFinalizeRequest {
    @NotNull(message = "Transacao Voucher não pode ser nulo.")
    @NotBlank(message = "Transacao não pode ser vazio.")
    private String transacao;
    private BigDecimal valorPago;
}
