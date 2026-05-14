package com.rematec.voucher.voucherbackapi.repositories;

import com.rematec.voucher.voucherbackapi.repositories.criteria.IPromocaoRepositoryQuery;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.enums.PromocaoStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPromocaoRepository extends JpaRepository<PromocaoEntity, Long>, IPromocaoRepositoryQuery {

    Optional<PromocaoEntity> findByGuid(String guid);

    Page<PromocaoEntity> findByDescricaoContaining(String descricao, Pageable page);

    List<PromocaoEntity> findByFimLessThanAndPromocaoStatusNot(LocalDateTime dateNow, PromocaoStatusEnum notStatus);

    List<PromocaoEntity> findByInicioLessThanEqualAndFimGreaterThanEqualAndValorMinimoParaDisparoLessThanEqualAndPromocaoStatusAndLojasCnpjAndLojasStatusTrue(
            LocalDateTime inicio, LocalDateTime fim, BigDecimal valorCompra, PromocaoStatusEnum status, String cnpj);

    Optional<PromocaoEntity> findByGuidAndLojasCnpjAndLojasStatusTrue(String guid, String cnpjFilial);

    @Query("SELECT DISTINCT p FROM promocao p " +
           "LEFT JOIN FETCH p.lojas l " +
           "WHERE p.inicio <= :inicio " +
           "AND p.fim > :fim " +
           "AND p.valorMinimoParaDisparo <= :valorCompra " +
           "AND p.promocaoStatus = :status " +
           "AND l.cnpj = :cnpj " +
           "AND l.status = true")
    List<PromocaoEntity> findPromocoesAtivasComLojas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("valorCompra") BigDecimal valorCompra,
            @Param("status") PromocaoStatusEnum status,
            @Param("cnpj") String cnpj);
}
