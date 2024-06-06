package com.rematec.voucher.voucherbackapi.repositories;

import com.rematec.voucher.voucherbackapi.models.entities.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEmpresaRepository extends JpaRepository<EmpresaEntity, Long> {

    Optional<EmpresaEntity> findByIdentificacao(String identificacao);
    Optional<EmpresaEntity> findByGuid(String guid);
}
