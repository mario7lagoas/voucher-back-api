package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.EmpresaApiResponse;
import com.rematec.voucher.voucherbackapi.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.models.entities.EmpresaEntity;
import com.rematec.voucher.voucherbackapi.repositories.IEmpresaRepository;
import org.aspectj.lang.annotation.Before;
import org.glassfish.jaxb.runtime.v2.util.CollisionCheckStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmpresaServiceImplTest {

    @InjectMocks
    private EmpresaServiceImpl empresaService;

    @Mock
    private IEmpresaRepository iEmpresaRepository;

    @Spy
    private VouckBackMapper mapper;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should Return A List EmpresaApiResponse Successfully")
    public void buscandoListaEmpresaCase1() {
        //having
        when(this.iEmpresaRepository.findAll()).thenReturn(new CollisionCheckStack<EmpresaEntity>());

        //when
        List<EmpresaApiResponse> responses = this.empresaService.buscandoListaEmpresa();

        //then
        Assertions.assertNotNull(responses);
    }
}
