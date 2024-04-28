package com.rematec.voucher.voucherbackapi.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@Entity(name= "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = { @Index(name = "IDX_GUID_USU" , columnList = "guid")})
public class UsuarioEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String userName;

    @Column(unique = true, length = 50)
    private String email;
    @NotNull
    @Column(length = 150)
    private String password;

    private Boolean status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_perfil",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "perfil_id"})
    )
    private Set<PerfilEntity> perfis = new HashSet<>();

}
