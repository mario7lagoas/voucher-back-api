package com.rematec.voucher.voucherbackapi.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
import java.util.List;

@Entity(name = "loja")
@Table(indexes = {@Index(name = "IDX_GUID_LOJA", columnList = "guid"),
        @Index(name = "IDX_CNPJ_LOJA", columnList = "cnpj"),
        @Index(name = "IDX_STATUS_LOJA", columnList = "status")})
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LojaEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String cnpj;

    private String identificacao;

    private Boolean status;


    @ManyToMany(mappedBy = "lojas", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<PromocaoEntity> promocoes;

    @ManyToMany(mappedBy = "lojas", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<UsuarioEntity> usuarios;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaEntity empresa;

}
