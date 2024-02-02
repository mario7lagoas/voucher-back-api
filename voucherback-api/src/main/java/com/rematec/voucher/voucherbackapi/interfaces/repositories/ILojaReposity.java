package com.rematec.voucher.voucherbackapi.interfaces.repositories;

import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ILojaReposity extends JpaRepository<LojaEntity, Long> {

    Optional<LojaEntity> findByGuid(String guid);
    List<Optional<LojaEntity>> findByPromocoesLojasGuid(String guid);
}
