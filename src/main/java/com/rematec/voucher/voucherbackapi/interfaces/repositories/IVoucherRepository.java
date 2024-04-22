package com.rematec.voucher.voucherbackapi.interfaces.repositories;

import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherPromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVoucherRepository extends JpaRepository<VoucherEntity, Long> {
    Optional<VoucherEntity> findByCodigoEquals(String codigo);

    Optional<VoucherEntity> findTop1ByClienteCpfEqualsAndPromocaoGuidAndVoucherStatusAndPromocaoStatus(String cpf,
                                                                                                       String promocaoGuid,
                                                                                                       VoucherStatusEnum statusEnum,
                                                                                                       VoucherPromocaoStatusEnum promocaoEnum);

    Optional<VoucherEntity> findByCodigoEqualsAndClienteCpfEqualsAndFilialCnpjEqualsAndVoucherStatus(String codigo,
                                                                                                     String clienteCpf,
                                                                                                     String filialCnpj,
                                                                                                     VoucherStatusEnum statusEnum);
}
