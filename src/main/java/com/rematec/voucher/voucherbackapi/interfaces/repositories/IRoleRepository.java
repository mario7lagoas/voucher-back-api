package com.rematec.voucher.voucherbackapi.interfaces.repositories;

import com.rematec.voucher.voucherbackapi.models.entities.RoleEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PermissaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByNome(PermissaoEnum nome);
}
