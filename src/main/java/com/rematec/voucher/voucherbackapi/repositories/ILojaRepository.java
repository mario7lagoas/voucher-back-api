package com.rematec.voucher.voucherbackapi.repositories;

import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ILojaRepository extends JpaRepository<LojaEntity, Long> {

    Optional<LojaEntity> findByGuid(String guid);
    List<Optional<LojaEntity>> findByPromocoesLojasGuid(String guid);
    Optional<LojaEntity> findByCnpj(String cnpj);
    List<LojaEntity> findByStatusTrue();
    List<LojaEntity> findByStatusTrueAndUsuariosEmail(String email);
    Page<LojaEntity> findByCnpjContaining(String cnpj, PageRequest of);
    List<Optional<LojaEntity>> findByUsuariosLojasGuid(String guid);
    List<LojaEntity> findByUsuariosEmail(String email);

    @Query("SELECT l FROM loja l WHERE l.guid IN :guids")
    List<LojaEntity> findByGuids(List<String> guids);
}
