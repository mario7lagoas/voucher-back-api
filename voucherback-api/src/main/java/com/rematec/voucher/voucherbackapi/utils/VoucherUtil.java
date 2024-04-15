package com.rematec.voucher.voucherbackapi.utils;

import com.rematec.voucher.voucherbackapi.interfaces.repositories.ILojaRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPerfilRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IRoleRepository;
import com.rematec.voucher.voucherbackapi.models.entities.LojaEntity;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.entities.RoleEntity;
import com.rematec.voucher.voucherbackapi.models.requests.Guid;
import com.rematec.voucher.voucherbackapi.models.requests.PerfilRequest;
import com.rematec.voucher.voucherbackapi.models.requests.RoleRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class VoucherUtil {

    @Autowired
    private ILojaRepository iLojaReposity;

    @Autowired
    private IPerfilRepository iPerfilRepository;
    @Autowired
    private IRoleRepository iRoleRepository;

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

}
