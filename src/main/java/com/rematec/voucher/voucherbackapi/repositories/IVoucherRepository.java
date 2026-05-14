package com.rematec.voucher.voucherbackapi.repositories;

import com.rematec.voucher.voucherbackapi.repositories.criteria.IVoucherRepositoryQuery;
import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import com.rematec.voucher.voucherbackapi.enums.VoucherStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IVoucherRepository extends JpaRepository<VoucherEntity, Long>, IVoucherRepositoryQuery {
    Optional<VoucherEntity> findByCodigoEqualsAndFimResgateGreaterThanEqualAndVoucherStatus(String codigo,
                                                                                            LocalDateTime fimResgate,
                                                                                            VoucherStatusEnum voucherStatus);

    Optional<VoucherEntity> findTop1ByClienteCpfEqualsAndPromocaoGuidAndVoucherStatusNot(String cpf,
                                                                                         String promocaoGuid,
                                                                                         VoucherStatusEnum statusEnum
    );

    Optional<VoucherEntity> findByCodigoEqualsAndClienteCpfEqualsAndFilialCnpjEqualsAndVoucherStatus(String codigo,
                                                                                                     String clienteCpf,
                                                                                                     String filialCnpj,
                                                                                                     VoucherStatusEnum statusEnum);

    Optional<VoucherEntity> findByGuid(String voucherGuid);

    @Query("SELECT COUNT(v) > 0 FROM voucher v " +
           "WHERE v.clienteCpf = :cpf " +
           "AND v.promocaoGuid = :promocaoGuid " +
           "AND v.voucherStatus != :statusExcluido")
    boolean existsVoucherAtivoParaClienteEPromocao(
            @Param("cpf") String cpf,
            @Param("promocaoGuid") String promocaoGuid,
            @Param("statusExcluido") VoucherStatusEnum statusExcluido);

    @Query("SELECT v FROM voucher v WHERE v.codigo IN :codigos AND v.clienteCpf IN :cpfs AND v.filialCnpj IN :cnpjs AND v.voucherStatus = :status")
    List<VoucherEntity> findVouchersByCodigosCpfsCnpjsAndStatus(
            @Param("codigos") List<String> codigos,
            @Param("cpfs") List<String> cpfs,
            @Param("cnpjs") List<String> cnpjs,
            @Param("status") VoucherStatusEnum status);
}
