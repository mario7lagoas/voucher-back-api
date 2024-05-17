package com.rematec.voucher.voucherbackapi.services.impl;

import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPromocaoRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IVoucherRepository;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceImplTest {

    @InjectMocks
    private VoucherServiceImpl voucherService;

    @Mock
    private IVoucherRepository iVoucherRepository;

    @Mock
    private IPromocaoRepository iPromocaoRepository;

    @Spy
    private VoucherUtil voucherUtil;

    @Spy
    private VouckBackMapper mapper;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }




}
