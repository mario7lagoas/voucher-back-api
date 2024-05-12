package com.rematec.voucher.voucherbackapi.builders;

import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.TipoDescontoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.rematec.voucher.voucherbackapi.builders.LojaEntityBuilder.umaLojaEntity;

public class PromocaoEntityBuilder {

    private PromocaoEntity promocaoEntity;

    private PromocaoEntityBuilder() {
    }

    public static PromocaoEntityBuilder umaPromocaoEntity() {

        PromocaoEntityBuilder builder = new PromocaoEntityBuilder();
        builder.promocaoEntity = new PromocaoEntity();
        builder.promocaoEntity.setId(1L);
        builder.promocaoEntity.setGuid("123456");
        builder.promocaoEntity.setDescricao("Promoção");
        builder.promocaoEntity.setPromocaoStatus(PromocaoStatusEnum.ATIVA);
        builder.promocaoEntity.setTipoDesconto(TipoDescontoEnum.PERCENTUAL);
        builder.promocaoEntity.setValorMaximoDesconto(BigDecimal.valueOf(10.00));
        builder.promocaoEntity.setDiasValidadeVoucher(3);
        builder.promocaoEntity.setDescontoPercentual(BigDecimal.valueOf(5.00));
        builder.promocaoEntity.setDescontoValor(BigDecimal.ZERO);
        builder.promocaoEntity.setValorMinimoParaDisparo(BigDecimal.valueOf(50.00));
        builder.promocaoEntity.setAutorAlteracao("Any User");
        builder.promocaoEntity.setInicio(LocalDateTime.now());
        builder.promocaoEntity.setFim(LocalDateTime.now().plusDays(1));

        return builder;

    }

    public PromocaoEntityBuilder id(Long id) {
        promocaoEntity.setId(id);
        return this;
    }

    public PromocaoEntityBuilder guid(String guid) {
        promocaoEntity.setGuid(guid);
        return this;
    }

    public PromocaoEntityBuilder descricao(String descricao) {
        promocaoEntity.setDescricao(descricao);
        return this;
    }

    public PromocaoEntityBuilder promocaoStatus(PromocaoStatusEnum statusEnum) {
        promocaoEntity.setPromocaoStatus(statusEnum);
        return this;
    }

    public PromocaoEntityBuilder tipoDesconto(TipoDescontoEnum tipoDescontoEnum) {
        promocaoEntity.setTipoDesconto(tipoDescontoEnum);
        return this;
    }

    public PromocaoEntityBuilder valorMinimoParaDisparo(BigDecimal bigDecimal) {
        promocaoEntity.setValorMinimoParaDisparo(bigDecimal);
        return this;
    }

    public PromocaoEntityBuilder valorMaximoDesconto(BigDecimal bigDecimal) {
        promocaoEntity.setValorMaximoDesconto(bigDecimal);
        return this;
    }

    public PromocaoEntityBuilder descontoPercentual(BigDecimal bigDecimal) {
        promocaoEntity.setDescontoPercentual(bigDecimal);
        return this;
    }

    public PromocaoEntityBuilder descontoValor(BigDecimal bigDecimal) {
        promocaoEntity.setDescontoValor(bigDecimal);
        return this;
    }

    public PromocaoEntityBuilder inicio(LocalDateTime dateTime) {
        promocaoEntity.setInicio(dateTime);
        return this;
    }

    public PromocaoEntityBuilder fim(LocalDateTime dateTime) {
        promocaoEntity.setFim(dateTime);
        return this;
    }

    public PromocaoEntityBuilder comLoja() {
        promocaoEntity.setLojas(Arrays.asList(umaLojaEntity().agora()));
        return this;
    }

    public PromocaoEntity agora() {
        return promocaoEntity;
    }
}
