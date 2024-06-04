package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.EmpresaApiRequest;
import com.rematec.voucher.models.EmpresaApiResponse;
import com.rematec.voucher.voucherbackapi.exceptios.EmpresaCadastradaException;
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
import java.util.Optional;

import static com.rematec.voucher.voucherbackapi.builders.EmpresaApiRequestBuilder.umaEmpresaApiRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

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

    @Test
    @DisplayName("Should Create A Empresa Successfully")
    public void criandoEmpresaCase1() {

        //having
        EmpresaApiRequest request = umaEmpresaApiRequest().agora();

        when(this.iEmpresaRepository.save(any(EmpresaEntity.class))).thenReturn(new EmpresaEntity());
        when(this.mapper.empresaEntityToEmpresaApiResponse(any(EmpresaEntity.class))).thenReturn(new EmpresaApiResponse());

        //when
        EmpresaApiResponse response = this.empresaService.criandoEmpresa(request);

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(response);

        verify(this.iEmpresaRepository, times(1)).save(
                argThat(empresaArg -> empresaArg.getNome().equals("Any Empresa")
                        && empresaArg.getGuid() != null)
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Empresa Exist")
    public void criandoEmpresaCase2() {

        //having
        EmpresaApiRequest request = umaEmpresaApiRequest().agora();
        EmpresaEntity empresaEntity = EmpresaEntity.builder()
                .status(true)
                .nome("Any Empresa")
                .identificacao("C0001")
                .guid("123456")
                .build();

        when(this.iEmpresaRepository.findByIdentificacao(anyString())).thenReturn(Optional.of(empresaEntity));

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(EmpresaCadastradaException.class,
                () -> this.empresaService.criandoEmpresa(request));

        assertThat(exception.getMessage(), is("Empresa já cadastrada."));
    }
}
