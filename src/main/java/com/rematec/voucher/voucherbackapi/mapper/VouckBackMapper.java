package com.rematec.voucher.voucherbackapi.mapper;

import com.rematec.voucher.models.BuscandoListaFiltroVoucher200Response;
import com.rematec.voucher.models.BuscandoListaPaginadaLoja200Response;
import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.EmpresaApiResponse;
import com.rematec.voucher.models.LojaApiResponse;
import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.models.PerfilResumidoApiResponse;

import com.rematec.voucher.models.PromocaoApiResponse;
import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.models.VoucherApiResponse;
import com.rematec.voucher.voucherbackapi.constants.VoucherConstants;
import com.rematec.voucher.voucherbackapi.models.entities.EmpresaEntity;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring", imports = {
        VoucherConstants.class
})
public interface VouckBackMapper {

    List<UsuarioApiResponse> listUsuarioEntityTolistUsuarioApiResponse(List<UsuarioEntity> all);

    UsuarioApiResponse usuarioEntityToUsuarioApiResponse(UsuarioEntity usuarioEntity);

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

    List<PromocaoApiResponse> listPromocaoEntityToListPromocaoApiResponse(List<PromocaoEntity> all);
    PromocaoApiResponse promocaoEntityToPromocaoApiResponse(PromocaoEntity all);

    @Mapping(source = "promocaoEntityPage.content", target = "promocoes")
    BuscandoListaPaginadaPromocao200Response pagePromocoesEntityToPromocoesApiPaginadaResponse(Page<PromocaoEntity> promocaoEntityPage);

    @Mapping(source = "promocaoEntities.content", target = "promocoes")
    BuscandoListaPaginadaPromocao200Response pagePromocoesEntityToBuscandoListaPaginadaPromocao200Response(
            PageImpl<PromocaoEntity> promocaoEntities);

    VoucherApiResponse voucherEntityToVoucherApiResponse(VoucherEntity save);

    @Mapping(source = "voucherEntities.content", target = "transacoes")
    BuscandoListaFiltroVoucher200Response pageVouchersEntityToBuscandoListaFiltroVoucher200Response(PageImpl<VoucherEntity> voucherEntities);

    List<EmpresaApiResponse> listEmpresaEntityToListEmpresaApiResponse(List<EmpresaEntity> all);

    EmpresaApiResponse empresaEntityToEmpresaApiResponse(EmpresaEntity  empresaEntity);
}

