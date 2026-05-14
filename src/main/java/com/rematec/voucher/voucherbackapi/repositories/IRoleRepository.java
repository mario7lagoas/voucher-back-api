package com.rematec.voucher.voucherbackapi.repositories;

import com.rematec.voucher.voucherbackapi.models.entities.RoleEntity;
import com.rematec.voucher.voucherbackapi.enums.PermissaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByNome(PermissaoEnum nome);

    @Query("SELECT r FROM RoleEntity r WHERE r.nome IN :nomes")
    List<RoleEntity> findByNomes(List<PermissaoEnum> nomes);
}
