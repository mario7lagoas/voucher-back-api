package com.rematec.voucher.voucherbackapi.models.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusResquest {
    @NotNull(message = "Status é Obrigatorio")
    private Boolean status;
}
