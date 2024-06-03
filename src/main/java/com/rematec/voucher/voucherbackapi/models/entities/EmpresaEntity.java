package com.rematec.voucher.voucherbackapi.models.entities;

import jakarta.persistence.Entity;
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

@Entity(name = "empresa")
@Table(indexes = {@Index(name = "IDX_GUID_EMPRESA", columnList = "guid"),
        @Index(name = "IDX_IDENTIFICACAO_EMPRESA", columnList = "identificacao"),
        @Index(name = "IDX_STATUS_EMPRESA", columnList = "status")})
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaEntity  extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String identificacao;

    private Boolean status;
}
