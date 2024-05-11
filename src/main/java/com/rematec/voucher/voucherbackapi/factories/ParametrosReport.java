package com.rematec.voucher.voucherbackapi.factories;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public  class ParametrosReport {

    private static final String PREFIX_IMG = "/static/img/";

    public Map<String, Object> getParametros(String relatorio){
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
        parametros.put("logo", this.getClass().getResourceAsStream(PREFIX_IMG.concat(relatorio).concat(".jpg")));

        return parametros;
    }
}
