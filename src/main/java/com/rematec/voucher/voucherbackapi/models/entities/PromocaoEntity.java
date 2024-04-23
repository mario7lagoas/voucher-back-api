package com.rematec.voucher.voucherbackapi.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.TipoDescontoEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "promocao")
@Table(indexes = {@Index(name = "IDX_GUID_PROMO", columnList = "guid"),
        @Index(name = "IDX_INICIO_PROMO", columnList = "inicio"),
        @Index(name = "IDX_FIM_PROMO", columnList = "fim"),
        @Index(name = "IDX_VLR_MIN_PROMO", columnList = "valorMinimoParaDisparo")
})
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromocaoEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inicio;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fim;
    @Enumerated(EnumType.STRING)
    private TipoDescontoEnum tipoDesconto;
    @Enumerated(EnumType.STRING)
    private PromocaoStatusEnum promocaoStatus;
    private BigDecimal valorMinimoParaDisparo;
    private Integer diasValidadeVoucher;
    private BigDecimal discontoValor;
    private BigDecimal discontoPercentual;
    private String autorAlteracao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "promocoes_lojas",
            joinColumns = @JoinColumn(name = "promocao_id"),
            inverseJoinColumns = @JoinColumn(name = "loja_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"promocao_id", "loja_id"})
    )
    private List<LojaEntity> lojas;


}
