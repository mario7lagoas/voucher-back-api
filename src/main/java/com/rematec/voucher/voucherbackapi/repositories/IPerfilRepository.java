package com.rematec.voucher.voucherbackapi.repositories;

import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPerfilRepository extends JpaRepository<PerfilEntity,Long> {

    Optional<PerfilEntity> findByNome(String nome);
    Optional<PerfilEntity> findByGuid(String guid);
    List<PerfilEntity> findByEmpresaGuid(String guid);
}
