package com.rematec.voucher.voucherbackapi.interfaces.mapper;


import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.models.response.LojaResponse;
import com.rematec.voucher.voucherbackapi.models.response.PromocaoResponse;
import com.rematec.voucher.voucherbackapi.models.response.UsuarioCadastroResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface VouckBackMapper {
    List<UsuarioCadastroResponse> listUsuarioEntityTolistUsuarioResponse(List<UsuarioEntity> usuarios);

    UsuarioCadastroResponse usuarioEntityToUsuarioCadastroResponse(UsuarioEntity usuario);

    List<LojaResponse> listLojaEntityToListLojaResponse(List<LojaEntity> lojas);
    LojaResponse lojaEntityToLojaResponse(LojaEntity loja);
    List<PromocaoResponse> listPromocaoEntitytoListPromocaoResponse(List<PromocaoEntity> promocoes);
    PromocaoResponse promocaoEntitytopromocaoResponse(PromocaoEntity promocao);
}
