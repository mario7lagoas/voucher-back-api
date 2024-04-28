package com.rematec.voucher.voucherbackapi.interfaces.repositories;

import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);
    Optional<UsuarioEntity> findByGuid(String guid);
    Page<UsuarioEntity> findByUserNameContaining(String nome, PageRequest of);
    Optional<UsuarioEntity> findTop1ByPerfisGuid(String guid);
}
