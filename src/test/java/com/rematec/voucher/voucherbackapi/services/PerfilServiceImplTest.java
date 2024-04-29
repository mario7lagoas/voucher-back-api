package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.exceptios.PerfilCadastradoException;
import com.rematec.voucher.voucherbackapi.exceptios.PerfilNaoEncontradoException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPerfilRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.entities.RoleEntity;
import com.rematec.voucher.voucherbackapi.models.enums.PermissaoEnum;
import com.rematec.voucher.voucherbackapi.models.requests.PerfilRequest;
import com.rematec.voucher.voucherbackapi.models.requests.RoleRequest;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResponse;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResumidoResponse;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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

    private PerfilEntity getPerfilEntity() {

        return PerfilEntity.builder()
                .id(1L)
                .nome("My Perfil")
                .dataAtualizacao(LocalDateTime.now())
                .dataCadastro(LocalDateTime.now())
                .guid("123456")
                .roles(Arrays.asList(RoleEntity.builder().id(1L).nome(PermissaoEnum.MENU_LOJA).build(),
                        RoleEntity.builder().id(2L).nome(PermissaoEnum.MENU_USUARIO).build()))
                .build();
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
        PerfilRequest request = PerfilRequest.builder()
                .nome("Any Description")
                .roles(Arrays.asList(RoleRequest.builder().nome(PermissaoEnum.MENU_LOJA).build(),
                        RoleRequest.builder().nome(PermissaoEnum.MENU_USUARIO).build()))
                .build();

        when(this.iPerfilRepository.save(any(PerfilEntity.class))).thenReturn(new PerfilEntity());
        when(mapper.perfilEntityToPerfilResponse(any(PerfilEntity.class)))
                .thenReturn(new PerfilResponse());

        //when

        PerfilResponse perfilResponse = this.perfilService.addPerfil(request);

        //then

        Assertions.assertNotNull(perfilResponse);

        verify(this.iPerfilRepository, times(1)).save(
                argThat(perfilArg -> perfilArg.getNome().equals("Any Description")
                        && perfilArg.getGuid() != null)
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Perfil Exist")
    public void addPerfilCase2() {

        //having

        PerfilRequest request = PerfilRequest.builder()
                .nome("Any Description")
                .roles(Arrays.asList(RoleRequest.builder().nome(PermissaoEnum.MENU_LOJA).build(),
                        RoleRequest.builder().nome(PermissaoEnum.MENU_USUARIO).build()))
                .build();

        when(this.iPerfilRepository.findByNome(anyString()).isPresent())
                .thenThrow(new PerfilCadastradoException("Já existe um Perfil com este nome."));
        //doReturn(true).when(this.iPerfilRepository.findByNome(anyString())).isPresent();

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(PerfilCadastradoException.class,
                () -> this.perfilService.addPerfil(request));

        assertThat(exception.getMessage(), is("Já existe um Perfil com este nome."));

    }

    @Test
    @DisplayName("Should Return A Perfil By Guid Successfully")
    public void getPerfilGuidCase1() {
        //having
        String guid = UUID.randomUUID().toString();

        PerfilEntity entity = getPerfilEntity();

        when(this.iPerfilRepository.findByGuid(guid)).thenReturn(Optional.of(entity));
        when(this.mapper.perfilEntityToPerfilResponse(any(PerfilEntity.class))).thenReturn(new PerfilResponse());

        //when
        PerfilResponse perfilResponse = this.perfilService.getPerfilGuid(guid);

        //then
        Assertions.assertNotNull(perfilResponse);
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Get Perfil by Guid")
    public void getPerfilGuidCase2() {
        //having
        String guid = UUID.randomUUID().toString();

        when(this.iPerfilRepository.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Exception exception = Assertions.assertThrows(PerfilNaoEncontradoException.class,
                () -> this.perfilService.getPerfilGuid(guid));

        assertThat(exception.getMessage(), is("Perfil não encontrado."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Find By Perfil At GUID And Name IsNull Or Empty")
    public void getPerfilGuidCase3() {
        //having


        //when

        //then
        Exception exception = Assertions.assertThrows(PerfilNaoEncontradoException.class,
                () -> this.perfilService.getPerfilGuid(null));

        assertThat(exception.getMessage(), is("Perfil não encontrado."));
    }

    @Test
    @DisplayName("Should Return A Perfil By Nome Successfully")
    public void getPerfilNomeCase1() {
        //having
        String perfil = "My Perfil";

        PerfilEntity entity = getPerfilEntity();

        when(this.iPerfilRepository.findByNome(perfil)).thenReturn(Optional.of(entity));
        when(this.mapper.perfilEntityToPerfilResponse(any(PerfilEntity.class))).thenReturn(new PerfilResponse());

        //when
        PerfilResponse perfilResponse = this.perfilService.getPerfilNome(perfil);

        //then
        Assertions.assertNotNull(perfilResponse);
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Get Perfil by Nome")
    public void getPerfilNomeCase2() {
        //having
        String perfil = "My Perfil";

        when(this.iPerfilRepository.findByNome(perfil)).thenReturn(Optional.empty());

        //when

        //then
        Exception exception = Assertions.assertThrows(PerfilNaoEncontradoException.class,
                () -> this.perfilService.getPerfilNome(perfil));

        assertThat(exception.getMessage(), is("Perfil não encontrado."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Find By Perfil At Nome And Name IsNull Or Empty")
    public void getPerfilNomeCase3() {
        //having


        //when

        //then
        Exception exception = Assertions.assertThrows(PerfilNaoEncontradoException.class,
                () -> this.perfilService.getPerfilNome(null));

        assertThat(exception.getMessage(), is("Perfil não encontrado."));
    }

    @Test
    @DisplayName("Should Update a Perfil status Successfully")
    public void alterarPerfilCase1() {
        //having
        PerfilEntity entity = getPerfilEntity();
        String guid = entity.getGuid();
        String nameAltered = "Other";

        PerfilRequest request = PerfilRequest.builder()
                .nome(nameAltered)
                .roles(null)
                .build();

        when(this.iPerfilRepository.save(any(PerfilEntity.class))).thenReturn(new PerfilEntity());
        when(this.iPerfilRepository.findByGuid(guid)).thenReturn(Optional.of(entity));
        when(this.mapper.perfilEntityToPerfilResponse(any(PerfilEntity.class))).thenReturn(new PerfilResponse());
        when(this.voucherUtil.checkDataNullAndEmpty(request.getNome())).thenReturn(true);

        //when
        PerfilResponse perfilResponse = this.perfilService.alterarPerfil(guid, request);

        //then
        Assertions.assertNotNull(perfilResponse);

        verify(iPerfilRepository, times(1)).save(
                argThat(perfilArg -> perfilArg.getNome().equals("Other")
                        && perfilArg.getGuid().equals(guid)
                )
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Update Perfil And It Does Not Exist")
    public void alterarPerfilCase2() {
        //having
        String guid = UUID.randomUUID().toString();

        PerfilRequest request = PerfilRequest.builder()
                .nome("Any Description")
                .roles(null)
                .build();


        when(this.iPerfilRepository.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(guid);

        Exception exception = Assertions.assertThrows(PerfilNaoEncontradoException.class,
                () -> this.perfilService.alterarPerfil(guid, request));

        assertThat(exception.getMessage(), is("Perfil não encontrado."));

    }

}
