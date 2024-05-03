package com.rematec.voucher.voucherbackapi.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class VoucherFiltroResponse {
    private String guid;
    private String codigo;
    private String clienteCpf;
    private String filialCnpj;
    private String promocaoGuid;
    private String descricao;
    private String pdv;
    private String cupom;
    private String pdvResgate;
    private String filialCnpjResgate;
    private String cupomResgate;
    private BigDecimal valorPagamento;
    private String tipoDesconto;
    private String voucherStatus;
    private String promocaoStatus;
    private BigDecimal valorDesconto;
    private BigDecimal valorMaximoDesconto;
    private BigDecimal valorPago;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fimResgate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataResgate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inicio;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fim;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataAtualizacao;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCadastro;
}
