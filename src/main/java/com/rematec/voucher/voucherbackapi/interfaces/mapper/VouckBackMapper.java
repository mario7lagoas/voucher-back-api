package com.rematec.voucher.voucherbackapi.interfaces.mapper;

import com.rematec.voucher.models.PerfilApiRequest;
import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.models.PerfilResumidoApiResponse;
import com.rematec.voucher.models.RoleApiResponse;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.entities.RoleEntity;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherPromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherStatusEnum;
import com.rematec.voucher.voucherbackapi.models.requests.PerfilRequest;
import com.rematec.voucher.voucherbackapi.models.requests.RoleRequest;
import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import com.rematec.voucher.voucherbackapi.models.response.LojasPaginadaResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResumidoResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfisPaginadaResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocoesPaginadaResponse;
import com.rematec.voucher.voucherbackapi.models.response.UsuarioResponse;
import com.rematec.voucher.voucherbackapi.models.response.UsuariosPaginadaResponse;
import com.rematec.voucher.voucherbackapi.models.response.VoucherResponse;
import com.rematec.voucher.voucherbackapi.models.response.VouchersPaginadaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface VouckBackMapper {
    List<UsuarioResponse> listUsuarioEntityTolistUsuarioResponse(List<UsuarioEntity> usuarios);

    UsuarioResponse usuarioEntityToUsuarioResponse(UsuarioEntity usuario);

    List<LojaResponse> listLojaEntityToListLojaResponse(List<LojaEntity> lojas);

    LojaResponse lojaEntityToLojaResponse(LojaEntity loja);

    List<PromocaoResponse> listPromocaoEntityToListPromocaoResponse(List<PromocaoEntity> promocoes);

    PromocaoResponse promocaoEntityTopromocaoResponse(PromocaoEntity promocao);

    List<PerfilResponse> listPerfilEntityToListPerfilResponse(List<PerfilEntity> perfils);

    PerfilResponse perfilEntityToPerfilResponse(PerfilEntity perfil);

    List<PerfilResumidoResponse> listPerfilEntityToListPerfilResumidoResponse(List<PerfilEntity> perfisEnyityes);

    PerfilResumidoResponse perfilEntityToperfilResumidoResponse(PerfilEntity perfilEntity);

    @Mapping(source = "promocaoEntityPage.content", target = "promocoes")
    PromocoesPaginadaResponse pagePromocoesEntityToPromocoesPaginadaResponse(Page<PromocaoEntity> promocaoEntityPage);

    @Mapping(source = "usuarioEntityPage.content", target = "usuarios")
    UsuariosPaginadaResponse pageUsuariosEntityToUsuariosPaginadaResponse(Page<UsuarioEntity> usuarioEntityPage);

    @Mapping(source = "perfilEntityPage.content", target = "perfis")
    PerfisPaginadaResponse pagePerfisEntityToPerfisPaginadaResponse(Page<PerfilEntity> perfilEntityPage);

    @Mapping(source = "lojaEntityPage.content", target = "lojas")
    LojasPaginadaResponse pageLojasEntityToLojasPaginadaResponse(Page<LojaEntity> lojaEntityPage);

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

    VoucherResponse voucherEntityToVoucherResponse(VoucherEntity voucherEntity);

    @Mapping(source = "voucherEntities.content", target = "transacoes")
    VouchersPaginadaResponse pageVouchersEntityToVouchersPaginadaResponse(Page<VoucherEntity> voucherEntities);

    List<PerfilApiResponse> listPerfilEntityToListPerfilApiResponse(List<PerfilEntity> all);

    PerfilApiResponse perfilEntityToPerfilApiResponse(PerfilEntity perfilEntity);

    List<PerfilResumidoApiResponse> listPerfilEntityToListPerfilResumidoApiResponse(List<PerfilEntity> all);
    PerfilResumidoApiResponse perfilEntityToPerfilResumidoApiResponse(PerfilEntity perfilEntity);

    PerfilRequest perfilApiRequestToPerfilRequest(PerfilApiRequest perfilApiRequest);


    List<RoleEntity> listRoleApiResponseToListRoleEntity(List<RoleApiResponse> roles);

    RoleEntity roleApiResponseToRoleEntity(RoleApiResponse role);
}

