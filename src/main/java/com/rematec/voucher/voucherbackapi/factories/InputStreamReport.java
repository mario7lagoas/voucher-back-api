package com.rematec.voucher.voucherbackapi.factories;

import java.io.InputStream;

public class InputStreamReport {
    private static final String PREFIX_RELATORIO = "/relatorios/relatorio-de-";

    public InputStream getInputStream(String relatorio){
        return this.getClass().getResourceAsStream(PREFIX_RELATORIO.concat(relatorio).concat(".jasper"));
    }
}
