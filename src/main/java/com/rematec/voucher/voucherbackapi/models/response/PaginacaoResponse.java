package com.rematec.voucher.voucherbackapi.models.response;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
public class PaginacaoResponse {
    private int number;
    private Long totalPages;
    private int totalElements;
}
