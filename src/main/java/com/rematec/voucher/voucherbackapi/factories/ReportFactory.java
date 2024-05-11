package com.rematec.voucher.voucherbackapi.factories;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.InputStream;
import java.util.Map;

public class ReportFactory {

    private static final String DATA_BASE64 = "data:application/pdf;base64,";

    public static String report(JRBeanCollectionDataSource collectionDataSource, String relatorio) {
        try {
            Map<String, Object> parametros = new ParametrosReport().getParametros(relatorio);
            InputStream inputStream = new InputStreamReport().getInputStream(relatorio);
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, collectionDataSource);
            return DATA_BASE64.concat(Base64.encodeBase64String(JasperExportManager.exportReportToPdf(jasperPrint)));

        } catch (JRException e) {
            throw new RuntimeException("Erro em gerar o PDF " + e);

        }
    }
}
