package com.rematec.voucher.voucherbackapi.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rematec.voucher.voucherbackapi.models.enums.TipoDescontoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse {
    private String descricao;
    private String codigo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inicio;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fim;
    private TipoDescontoEnum tipoDesconto;
    private BigDecimal valorDesconto;
    private BigDecimal valorMaximoDesconto;
}
