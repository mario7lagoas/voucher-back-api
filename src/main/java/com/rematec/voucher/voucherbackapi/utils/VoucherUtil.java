package com.rematec.voucher.voucherbackapi.utils;

import com.rematec.voucher.voucherbackapi.exceptios.VoucherNaoEncontradoException;
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
import com.rematec.voucher.voucherbackapi.models.enums.PromocaoStatusEnum;
import com.rematec.voucher.voucherbackapi.models.enums.VoucherStatusEnum;
import com.rematec.voucher.voucherbackapi.models.requests.Guid;
import com.rematec.voucher.voucherbackapi.models.requests.PerfilRequest;
import com.rematec.voucher.voucherbackapi.models.requests.RoleRequest;
import com.rematec.voucher.voucherbackapi.models.requests.VoucherRequest;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    private static String DATA_BASE64 = "data:application/pdf;base64,";

    public List<LojaEntity> getListGuidLojasToListLojasEntity(List<Guid> lojas) {

        return lojas != null ? lojas.stream()
                .map(loja -> iLojaReposity.findByGuid(loja.getGuid()).get())
                .toList() : null;
    }

    public String print(JRBeanCollectionDataSource prints, String relatorio) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
            parametros.put("logo", this.getClass().getResourceAsStream("/static/img/".concat(relatorio).concat(".jpg")));
            InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/relatorio-de-".concat(relatorio).concat(".jasper"));

            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, prints);
            return DATA_BASE64.concat(Base64.encodeBase64String(JasperExportManager.exportReportToPdf(jasperPrint)));

        } catch (JRException e) {
            throw new RuntimeException("Erro em gerar o PDF " + e);

        }
    }

    public boolean checkDataNullAndEmpty(String data) {

        if (data != null && !data.isEmpty())
            return true;

        return false;
    }

    public Set<PerfilEntity> listPerfisRequestToListPerfilEntity(Set<PerfilRequest> perfis) {

        Set<PerfilEntity> listPerfils = perfis
                .stream()
                .map(p -> iPerfilRepository.findByNome(p.getNome()).get())
                .collect(Collectors.toSet());

        return listPerfils;
    }

    public List<RoleEntity> listRolesRequestToListRoleEntity(List<RoleRequest> roles) {

        return roles
                .stream()
                .map(roleRequest -> iRoleRepository.findByNome(roleRequest.getNome()))
                .collect(Collectors.toList());
    }

    public void verificarPromocoesVencidias() {
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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");


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

    public void cancelOrConfirmVoucher(List<VoucherRequest> voucherRequests, VoucherStatusEnum statusEnum) {
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
            this.iVoucherRepository.save(voucherEntity);
        });
    }

    public String apenasNumerosNaString(String input){
        return input.replaceAll("[^0-9]", "");
    }

}
