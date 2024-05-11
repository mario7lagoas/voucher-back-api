package com.rematec.voucher.voucherbackapi.interfaces.services;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public interface IReportService {
    String report(JRBeanCollectionDataSource collectionDataSource, String relatorio);
}
