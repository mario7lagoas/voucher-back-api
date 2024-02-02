package com.rematec.voucher.voucherbackapi.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PromocaoUpdateRequest extends PromocaoRequest{
    private String promocaoStatus;
}
