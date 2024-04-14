package com.rematec.voucher.voucherbackapi.interfaces.repositories;

import com.rematec.voucher.voucherbackapi.interfaces.repositories.criteria.IPromocaoRepositoryQuery;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPromocaoRepository extends JpaRepository<PromocaoEntity, Long>, IPromocaoRepositoryQuery {

    Optional<PromocaoEntity> findByGuid(String guid);
  //  List<Optional<PromocaoEntity>> findByLojasGuid(String guid);

    Page<PromocaoEntity> findByDescricaoContaining(String descricao, Pageable page);

    List<PromocaoEntity> findByFimLessThanAndPromocaoStatusNot(LocalDateTime dateNow, PromocaoStatusEnum notStatus);

}
