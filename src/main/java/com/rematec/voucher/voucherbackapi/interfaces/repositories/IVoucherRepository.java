package com.rematec.voucher.voucherbackapi.interfaces.repositories;

import com.rematec.voucher.voucherbackapi.interfaces.repositories.criteria.IVoucherRepositoryQuery;
import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
}
