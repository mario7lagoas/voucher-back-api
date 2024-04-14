package com.rematec.voucher.voucherbackapi.models.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromocaoRequest {
    @NotBlank(message = "A descrição da promoção não pode ser nulo ou vazio.")
    private String descricao;
    @NotNull(message = "Data inicio promoção não pode ser nulo ou vazio.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inicio;
    @NotNull(message = "Data fim promoção não pode ser nulo ou vazio.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fim;
    @NotBlank(message = "Tipo de desconto da promoção não pode ser nulo ou vazio.")
    private String tipoDesconto;
    @NotNull(message = "Valor minimo para disparo nao pode ser nulo.")
    private Double valorMinimoParaDisparo;
    @NotNull(message = "Validade no Voucher não pode ser nulo")
    private Integer diasValidadeVoucher;
    @NotBlank(message = "Autor da operação não pode ser nulo ou vazio.")
    private String autorAlteracao;
    private Double discontoValor;
    private Integer discontoPercentual;
    private List<Guid> lojas;

}
