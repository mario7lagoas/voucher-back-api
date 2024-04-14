package com.rematec.voucher.voucherbackapi.interfaces.mapper;

import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import com.rematec.voucher.voucherbackapi.models.response.LojasPaginadaResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResumidoResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfisPaginadaResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocoesPaginadaResponse;
import com.rematec.voucher.voucherbackapi.models.response.UsuarioResponse;
import com.rematec.voucher.voucherbackapi.models.response.UsuariosPaginadaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface VouckBackMapper {
    List<UsuarioResponse> listUsuarioEntityTolistUsuarioResponse(List<UsuarioEntity> usuarios);

    UsuarioResponse usuarioEntityToUsuarioResponse(UsuarioEntity usuario);

    List<LojaResponse> listLojaEntityToListLojaResponse(List<LojaEntity> lojas);
    LojaResponse lojaEntityToLojaResponse(LojaEntity loja);
    List<PromocaoResponse> listPromocaoEntitytoListPromocaoResponse(List<PromocaoEntity> promocoes);
    PromocaoResponse promocaoEntitytopromocaoResponse(PromocaoEntity promocao);

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


}

