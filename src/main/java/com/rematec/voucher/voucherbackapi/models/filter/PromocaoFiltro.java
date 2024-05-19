package com.rematec.voucher.voucherbackapi.models.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class PromocaoFiltro {
    private String descricao;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate inicio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fim;
    private String tipoDesconto;
    private String promocaoStatus;
    private List<Long> idLojas;

}
