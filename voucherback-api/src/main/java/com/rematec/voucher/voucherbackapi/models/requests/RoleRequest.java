package com.rematec.voucher.voucherbackapi.models.requests;

import com.rematec.voucher.voucherbackapi.models.enums.PermissaoEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class RoleRequest {
    @Enumerated(EnumType.STRING )
    private PermissaoEnum nome;
}
