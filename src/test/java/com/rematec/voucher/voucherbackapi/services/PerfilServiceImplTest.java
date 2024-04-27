package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPerfilRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PermissaoEnum;
import com.rematec.voucher.voucherbackapi.models.requests.PerfilRequest;
import com.rematec.voucher.voucherbackapi.models.requests.RoleRequest;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResumidoResponse;
import com.rematec.voucher.voucherbackapi.models.response.RoleResponse;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;

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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PerfilServiceImplTest {

    @InjectMocks
    private PerfilServiceImpl perfilService;

    @Mock
    private IPerfilRepository iPerfilRepository;
    @Mock
    private VouckBackMapper mapper;

    @Mock
    private VoucherUtil voucherUtil;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should Return A List PerfilResponse Successfully")
    public void getAllPerfilCase1() {
        //having
        when(this.iPerfilRepository.findAll()).thenReturn(new CollisionCheckStack<PerfilEntity>());
        when(this.mapper.listPerfilEntityToListPerfilResponse(anyList()))
                .thenReturn(new CollisionCheckStack<PerfilResponse>());

        //when
        List<PerfilResponse> perfilResponseList = this.perfilService.getAllPerfil();

        //then
        Assertions.assertNotNull(perfilResponseList);
    }

    @Test
    @DisplayName("Should Return A List PerfilResumidoResponse Successfully")
    public void getAllPerfilResumidoCase1() {
        //having
        when(this.iPerfilRepository.findAll()).thenReturn(new CollisionCheckStack<PerfilEntity>());
        when(this.mapper.listPerfilEntityToListPerfilResumidoResponse(anyList()))
                .thenReturn(new CollisionCheckStack<PerfilResumidoResponse>());

        //when
        List<PerfilResumidoResponse> perfilResponseList = this.perfilService.getAllPerfilResumido();

        //then
        Assertions.assertNotNull(perfilResponseList);
    }

    @Test
    @DisplayName("Should Create a Perfil Successfully")
    public void addPerfilCase1() {

        //having
/*
        PerfilRequest request = PerfilRequest.builder()
                .nome("Any Description")
                .roles(Arrays.asList(RoleRequest.builder().nome(PermissaoEnum.ALTERAR_PERFIL).build(),
                        RoleRequest.builder().nome(PermissaoEnum.APAGAR_PERFIL).build()))
                .build();

        PerfilResponse perfilResponse = PerfilResponse.builder()
                .nome("Any Description")
                .roles(Arrays.asList(RoleResponse.builder().nome("ALTERAR_PERFIL").build(),
                        RoleResponse.builder().nome("APAGAR_PERFIL").build()))
                .build();

        when(iPerfilRepository.save(any(PerfilEntity.class))).thenReturn(new PerfilEntity());

        when(mapper.perfilEntityToPerfilResponse(any(PerfilEntity.class)))
                .thenReturn(perfilResponse);


 */
        //when


        //then
/*
        Assertions.assertNotNull(perfilResponse);

        verify(this.iPerfilRepository, times(1)).save(
                argThat(perfilArg -> perfilArg.getNome().equals("Any Description")
                        && perfilArg.getGuid() != null)
        );
*/

    }

}
