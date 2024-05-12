package com.rematec.voucher.voucherbackapi.interfaces.mapper;

import com.rematec.voucher.models.BuscandoListaPaginadaLoja200Response;
import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.LojaApiResponse;
import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.models.PerfilResumidoApiResponse;

import com.rematec.voucher.models.PromocaoApiResponse;
import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocoesPaginadaResponse;
import com.rematec.voucher.voucherbackapi.models.response.VoucherResponse;
import com.rematec.voucher.voucherbackapi.models.response.VouchersPaginadaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface VouckBackMapper {

    List<UsuarioApiResponse> listUsuarioEntityTolistUsuarioApiResponse(List<UsuarioEntity> all);

    UsuarioApiResponse usuarioEntityToUsuarioApiResponse(UsuarioEntity usuarioEntity);

    List<LojaResponse> listLojaEntityToListLojaResponse(List<LojaEntity> lojas);

    LojaResponse lojaEntityToLojaResponse(LojaEntity loja);

    List<PromocaoResponse> listPromocaoEntityToListPromocaoResponse(List<PromocaoEntity> promocoes);

    PromocaoResponse promocaoEntityTopromocaoResponse(PromocaoEntity promocao);

    @Mapping(source = "promocaoEntityPage.content", target = "promocoes")
    PromocoesPaginadaResponse pagePromocoesEntityToPromocoesPaginadaResponse(Page<PromocaoEntity> promocaoEntityPage);

    VoucherResponse voucherEntityToVoucherResponse(VoucherEntity voucherEntity);

    @Mapping(source = "voucherEntities.content", target = "transacoes")
    VouchersPaginadaResponse pageVouchersEntityToVouchersPaginadaResponse(Page<VoucherEntity> voucherEntities);

    List<PerfilApiResponse> listPerfilEntityToListPerfilApiResponse(List<PerfilEntity> all);

    PerfilApiResponse perfilEntityToPerfilApiResponse(PerfilEntity perfilEntity);

    List<PerfilResumidoApiResponse> listPerfilEntityToListPerfilResumidoApiResponse(List<PerfilEntity> all);

    PerfilResumidoApiResponse perfilEntityToPerfilResumidoApiResponse(PerfilEntity perfilEntity);

    List<LojaApiResponse> listLojaEntityToListLojaApiResponse(List<LojaEntity> all);

    LojaApiResponse lojaEntityToLojaApiResponse(LojaEntity lojaEntity);

    @Mapping(source = "lojaEntityPage.content", target = "lojas")
    BuscandoListaPaginadaLoja200Response pageLojasEntityToLojasPaginadaApiResponse(Page<LojaEntity> lojaEntityPage);

    @Mapping(source = "usuarioEntityPage.content", target = "usuarios")
    BuscandoListaPaginadaUsuario200Response pageUsuariosEntityToUsuariosApiPaginadaResponse(Page<UsuarioEntity> usuarioEntityPage);
/*
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "dataCadastro", ignore = true),
            @Mapping(target = "dataAtualizacao", ignore = true),
            @Mapping(source = "voucherStatus", target = "voucherStatus"),
            @Mapping(source = "promocaoStatus", target = "promocaoStatus"),
            @Mapping(source = "codigo", target = "codigo"),
            @Mapping(source = "guid", target = "guid"),
            @Mapping(source = "clienteCpf", target = "clienteCpf"),
            @Mapping(source = "filialCnpj", target = "filialCnpj"),
            @Mapping(source = "valorDesc", target = "valorDesconto"),
            @Mapping(source = "promocao.guid", target = "promocaoGuid"),
            @Mapping(source = "pdv", target = "pdv"),
            @Mapping(source = "fimResgate", target = "fimResgate"),
            @Mapping(source = "cupom", target = "cupom"),
    })
    VoucherEntity promocaoEntityToVoucherEntity(PromocaoEntity promocao, VoucherStatusEnum voucherStatus,
                                                VoucherPromocaoStatusEnum promocaoStatus, String guid, String codigo,
                                                String clienteCpf, String filialCnpj, BigDecimal valorDesc, String pdv,
                                                LocalDateTime fimResgate, String cupom);

 */

    List<PromocaoApiResponse> listPromocaoEntityToListPromocaoApiResponse(List<PromocaoEntity> all);
    PromocaoApiResponse promocaoEntityToPromocaoApiResponse(PromocaoEntity all);

    @Mapping(source = "promocaoEntityPage.content", target = "promocoes")
    BuscandoListaPaginadaPromocao200Response pagePromocoesEntityToPromocoesApiPaginadaResponse(Page<PromocaoEntity> promocaoEntityPage);
}

