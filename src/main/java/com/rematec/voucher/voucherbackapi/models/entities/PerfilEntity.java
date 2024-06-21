package com.rematec.voucher.voucherbackapi.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
import java.util.List;

@Entity(name = "perfil")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(indexes = {@Index(name = "IDX_GUID_PERFIL", columnList = "guid")})
public class PerfilEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "perfil_roles",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"perfil_id", "role_id"})
    )
    private List<RoleEntity> roles;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private EmpresaEntity empresa;

}
