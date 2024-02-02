package com.rematec.voucher.voucherbackapi.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rematec.voucher.voucherbackapi.models.enums.PermissaoEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name= "role")
@EqualsAndHashCode
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING )
    private PermissaoEnum nome;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<UsuarioEntity> usuarios ;
}
