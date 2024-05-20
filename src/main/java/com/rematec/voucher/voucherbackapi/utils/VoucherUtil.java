package com.rematec.voucher.voucherbackapi.utils;

import com.rematec.voucher.models.GuidApiRequest;
import com.rematec.voucher.models.PromocaoApiRequest;
import com.rematec.voucher.models.RoleApiResponse;
import com.rematec.voucher.models.UsuarioPerfilApiRequest;
import com.rematec.voucher.models.VoucherApiRequest;
import com.rematec.voucher.voucherbackapi.exceptios.VoucherEmUsoException;
import com.rematec.voucher.voucherbackapi.exceptios.VoucherNaoEncontradoException;
import com.rematec.voucher.voucherbackapi.exceptios.VoucherUtilizadoException;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.ILojaRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPerfilRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IRoleRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IVoucherRepository;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PromocaoEntity;
import com.rematec.voucher.voucherbackapi.models.entities.RoleEntity;
import com.rematec.voucher.voucherbackapi.models.entities.VoucherEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PermissaoEnum;
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.TipoDescontoEnum;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherPromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class VoucherUtil {

    @Autowired
    private ILojaRepository iLojaReposity;
    @Autowired
    private IPromocaoRepository iPromocaoRepository;
    @Autowired
    private IPerfilRepository iPerfilRepository;
    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private IVoucherRepository iVoucherRepository;

    public List<LojaEntity> getListGuidApiRequestToListLojasEntity(List<GuidApiRequest> guidList) {

        return guidList != null ? guidList.stream()
                .map(loja -> this.iLojaReposity.findByGuid(loja.getGuid()).get())
                .toList() : null;
    }

    public boolean checkDataNullAndEmpty(String data) {

        if (data != null && !data.isEmpty())
            return true;

        return false;
    }

    public Set<PerfilEntity> listUsuarioPerfilApiRequestToListPerfilEntity(List<UsuarioPerfilApiRequest> perfis) {
        Set<PerfilEntity> listPerfils = perfis
                .stream()
                .map(p -> this.iPerfilRepository.findByNome(p.getNome()).get())
                .collect(Collectors.toSet());
        return listPerfils;
    }

    public List<RoleEntity> listRoleApiResponseToListRoleEntity(List<RoleApiResponse> roles) {
        return roles
                .stream()
                .map(roleRequest -> this.iRoleRepository.findByNome(PermissaoEnum.valueOf(roleRequest.getNome())))
                .collect(Collectors.toList());

    }

    @Async("threadPollverificarPromocoesVencidasExecutor")
    public void verificarPromocoesVencidas() {
        log.warn("Verificando Promoções vencidas.");
        List<PromocaoEntity> promocaoEntities = this.iPromocaoRepository
                .findByFimLessThanAndPromocaoStatusNot(LocalDateTime.now(), PromocaoStatusEnum.FINALIZADA);
        if (promocaoEntities != null) {
            promocaoEntities.forEach(p -> {
                log.info("Promoção [{}] vencida dia [{}] . Inativando promoção automaticamente.",
                        p.getDescricao(), p.getFim());
                p.setPromocaoStatus(PromocaoStatusEnum.FINALIZADA);
                this.iPromocaoRepository.save(p);
            });
        }
    }

    public String gerarCodigoVoucher(String pdv) {

        String voucher;
        StringBuffer thebuffer;
        String theAlphaNumericS;
        byte[] chave = new byte[256];
        new Random().nextBytes(chave);
        voucher = new String(chave, Charset.forName("UTF-8"));
        thebuffer = new StringBuffer();
        theAlphaNumericS = voucher.replaceAll("[^0-9]", "");

        int i = 3;
        //random selection
        for (int m = 0; m < theAlphaNumericS.length(); m++) {

            if (Character.isLetter(theAlphaNumericS.charAt(m)) && (i > 0)
                    || Character.isDigit(theAlphaNumericS.charAt(m)) && (i > 0)) {

                thebuffer.append(theAlphaNumericS.charAt(m));
                i--;
            }
        }
        String voucherFinal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS"));
        return voucherFinal.concat(thebuffer.toString()).concat(pdv);

    }
    public void cancelOrConfirmVoucherApi(List<VoucherApiRequest> voucherRequests, VoucherStatusEnum statusEnum) {
        voucherRequests.forEach(voucherRequest -> {
            VoucherEntity voucherEntity =
                    this.iVoucherRepository.findByCodigoEqualsAndClienteCpfEqualsAndFilialCnpjEqualsAndVoucherStatus(
                            voucherRequest.getCodigo(), this.apenasNumerosNaString(voucherRequest.getClienteCpf()),
                            this.apenasNumerosNaString(voucherRequest.getFilialCnpj()),
                            VoucherStatusEnum.DISPONIBILIZADO
                    ).orElseThrow(
                            () -> new VoucherNaoEncontradoException("Voucher codigo [" + voucherRequest.getCodigo()
                                    + "] não encontrado")
                    );
            voucherEntity.setVoucherStatus(statusEnum);
            if (VoucherStatusEnum.CANCELADO.equals(statusEnum)) {
                voucherEntity.setPromocaoStatus(VoucherPromocaoStatusEnum.CANCELADO);
            }
            this.iVoucherRepository.save(voucherEntity);
        });
    }

    public String apenasNumerosNaString(String input) {
        return input.replaceAll("[^0-9]", "");
    }

    public void checkStatusVoucher(VoucherEntity voucherEntity) {

        switch (voucherEntity.getPromocaoStatus().name()) {
            case "EM_USO":
                throw new VoucherEmUsoException("Em uso no PDV [" + voucherEntity.getPdvResgate()
                        + "] - Filial [" + this.getLojaNome(voucherEntity.getFilialCnpjResgate())
                        + "] - Cupom [" + voucherEntity.getCupomResgate() + "]");
            case "UTILIZADO":
                throw new VoucherUtilizadoException("Utilizado [" + voucherEntity.getPdvResgate()
                        + "] - Filial [" + getLojaNome(voucherEntity.getFilialCnpjResgate())
                        + "] - Cupom [" + voucherEntity.getCupomResgate() + "]");
            default:
                log.info("Promoção Valida");
                break;
        }
    }

    public String getLojaNome(String cnpj) {

        Optional<LojaEntity> lojaEntity = this.iLojaReposity.findByCnpj(this.apenasNumerosNaString(cnpj));
        if (lojaEntity.isPresent()) {
            log.debug("Retornado nome da loja {} ", lojaEntity.get().getNome());
            return lojaEntity.get().getNome();
        }
        log.debug("Retornado nome da loja {} ", cnpj);
        return cnpj;
    }

    public BigDecimal getPromocaoApiRequestValorMaximoDesconto(PromocaoApiRequest request) {

        if (request.getValorMaximoDesconto() != null &&
                request.getTipoDesconto().equals(TipoDescontoEnum.PERCENTUAL.name()) &&
                request.getValorMaximoDesconto().compareTo(BigDecimal.ZERO) > 0) {
            log.debug("Retornado valor Maximo desconto {} ", request.getValorMaximoDesconto());
            return request.getValorMaximoDesconto();
        }
        log.debug("Retornado valor Maximo desconto {} ", BigDecimal.ZERO);
        return BigDecimal.ZERO;
    }

    public LocalDateTime stringToLocalDateTime(String dateTime) {

        if (checkDataNullAndEmpty(dateTime)) {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(dateTime, formatter);
        }
        return null;

    }

    public List<Long> getListLojaIdForUsuarioEmail(String email) {

        List<LojaEntity> lojaEntities = this.iLojaReposity.findByUsuariosEmail(email);

        return  lojaEntities != null && !lojaEntities.isEmpty() ?
                lojaEntities.stream().map(LojaEntity::getId).toList() : List.of(0L);
    }
}
