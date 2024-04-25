package com.rematec.voucher.voucherbackapi.models.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class VoucherFiltro {
    private String codigo;
    private String descricao;
    private String clienteCpf;
    private String pdv;
    private String cupomResgate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate inicio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fim;

}
