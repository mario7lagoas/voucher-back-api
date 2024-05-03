package com.rematec.voucher.voucherbackapi.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rematec.voucher.voucherbackapi.models.enums.TipoDescontoEnum;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherPromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "voucher")
@Table(indexes = {@Index(name = "IDX_GUID_VOUCHER", columnList = "guid"),
        @Index(name = "IDX_COD_VOUCHER", columnList = "codigo"),
        @Index(name = "IDX_CNPJ_VOUCHER", columnList = "filialCnpj"),
        @Index(name = "IDX_CPF_VOUCHER", columnList = "clienteCpf"),
        @Index(name = "IDX_PROMO_GUID_VOUCHER", columnList = "promocaoGuid")
})
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String clienteCpf;
    private String filialNome;
    private String filialCnpj;
    private String promocaoGuid;
    private String descricao;
    private String pdv;
    private String cupom;
    private String pdvResgate;
    private String filialCnpjResgate;
    private String filialResgateNome;
    private String cupomResgate;
    private BigDecimal valorPagamento;
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
    @Enumerated(EnumType.STRING)
    private TipoDescontoEnum tipoDesconto;
    @Enumerated(EnumType.STRING)
    private VoucherStatusEnum voucherStatus;
    @Enumerated(EnumType.STRING)
    private VoucherPromocaoStatusEnum promocaoStatus;
    private BigDecimal valorDesconto;
    private Integer diasValidadeVoucher;

}
