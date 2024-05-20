package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.PerfilApiRequest;
import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.models.PerfilResumidoApiResponse;
import com.rematec.voucher.models.PerfilUpdateApiRequest;
import com.rematec.voucher.voucherbackapi.exceptios.BadRequestException;
import com.rematec.voucher.voucherbackapi.exceptios.NaoPermitidoExcluirPerfilException;
import com.rematec.voucher.voucherbackapi.exceptios.PerfilCadastradoException;
import com.rematec.voucher.voucherbackapi.exceptios.PerfilNaoEncontradoException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPerfilRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.entities.RoleEntity;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.rematec.voucher.voucherbackapi.builders.PerfilApiRequestBuilder.umPerfilApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.PerfilEntityBuilder.umPerfilEntity;
import static com.rematec.voucher.voucherbackapi.builders.PerfilUpdateApiRequestBuilder.umPerfilUpdateApiRequest;
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
    private IUsuarioRepository iUsuarioRepository;

    @Spy
    private VouckBackMapper mapper;
    @Spy
    private VoucherUtil voucherUtil;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should Return A List PerfilApiResponse Successfully")
    public void buscandoListaPerfilCase1() {
        //having
        when(this.iPerfilRepository.findAll()).thenReturn(new CollisionCheckStack<PerfilEntity>());

        //when
        List<PerfilApiResponse> perfilResponseList = this.perfilService.buscandoListaPerfil();

        //then
        Assertions.assertNotNull(perfilResponseList);
    }

    @Test
    @DisplayName("Should Return A List PerfilResumidoApiResponse Successfully")
    public void buscandoListaResumidoPerfilCase1() {
        //having
        when(this.iPerfilRepository.findAll()).thenReturn(new CollisionCheckStack<PerfilEntity>());

        //when
        List<PerfilResumidoApiResponse> perfilResponseList = this.perfilService.buscandoListaResumidoPerfil();

        //then
        Assertions.assertNotNull(perfilResponseList);
    }

    @Test
    @DisplayName("Should Create a Perfil Successfully")
    public void criandoPerfilCase1() {

        //having
        PerfilApiRequest request = umPerfilApiRequest().comRoles().agora();

        when(this.iPerfilRepository.save(any(PerfilEntity.class))).thenReturn(new PerfilEntity());
        when(this.voucherUtil.listRoleApiResponseToListRoleEntity(anyList()))
                .thenReturn(new CollisionCheckStack<RoleEntity>());
        when(mapper.perfilEntityToPerfilApiResponse(any(PerfilEntity.class))).thenReturn(new PerfilApiResponse());


        //when
        PerfilApiResponse perfilResponse = this.perfilService.criandoPerfil(request);

        //then

        Assertions.assertNotNull(perfilResponse);

        verify(this.iPerfilRepository, times(1)).save(
                argThat(perfilArg -> perfilArg.getNome().equals("Any Description")
                        && perfilArg.getGuid() != null)
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Perfil Exist")
    public void criandoPerfilCase2() {

        //having
        PerfilApiRequest request = umPerfilApiRequest().comRoles().agora();
        PerfilEntity perfilEntity = umPerfilEntity().agora();

        when(this.iPerfilRepository.findByNome(anyString())).thenReturn(Optional.of(perfilEntity));

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(PerfilCadastradoException.class,
                () -> this.perfilService.criandoPerfil(request));

        assertThat(exception.getMessage(), is("Já existe um Perfil com este nome."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Perfil Nome Is Null")
    public void criandoPerfilCase3() {

        //having
        PerfilApiRequest request = umPerfilApiRequest().nomeNull().comRoles().agora();

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.perfilService.criandoPerfil(request));

        assertThat(exception.getMessage(), is("Nome do perfil obrigatório."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Perfil Nome Is Empty")
    public void criandoPerfilCase4() {

        //having
        PerfilApiRequest request = umPerfilApiRequest().nomeEmpty().comRoles().agora();

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.perfilService.criandoPerfil(request));

        assertThat(exception.getMessage(), is("Nome do perfil obrigatório."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Perfil Roles Is Blank")
    public void criandoPerfilCase5() {

        //having
        PerfilApiRequest request = umPerfilApiRequest().agora();

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.perfilService.criandoPerfil(request));

        assertThat(exception.getMessage(), is("Permissão do Perfil é Obrigatório."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Perfil Roles Is Null")
    public void criandoPerfilCase6() {

        //having
        PerfilApiRequest request = umPerfilApiRequest().rolesNull().agora();

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.perfilService.criandoPerfil(request));

        assertThat(exception.getMessage(), is("Permissão do Perfil é Obrigatório."));

    }

    @Test
    @DisplayName("Should Return A Perfil By Guid Successfully")
    public void buscandoPerfilPeloGUIDCase1() {
        //having
        String guid = UUID.randomUUID().toString();

        PerfilEntity entity = umPerfilEntity().comRoles().agora();

        when(this.iPerfilRepository.findByGuid(guid)).thenReturn(Optional.of(entity));
        when(this.mapper.perfilEntityToPerfilApiResponse(any(PerfilEntity.class))).thenReturn(new PerfilApiResponse());

        //when
        PerfilApiResponse perfilResponse = this.perfilService.buscandoPerfilPeloGUID(guid);

        //then
        Assertions.assertNotNull(perfilResponse);
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Get Perfil by Guid")
    public void buscandoPerfilPeloGUIDCase2() {
        //having
        String guid = UUID.randomUUID().toString();

        when(this.iPerfilRepository.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Exception exception = Assertions.assertThrows(PerfilNaoEncontradoException.class,
                () -> this.perfilService.buscandoPerfilPeloGUID(guid));

        assertThat(exception.getMessage(), is("Perfil não encontrado."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Find By Perfil At GUID And Name IsNull Or Empty")
    public void buscandoPerfilPeloGUIDCase3() {
        //having

        //when

        //then
        Exception exception = Assertions.assertThrows(PerfilNaoEncontradoException.class,
                () -> this.perfilService.buscandoPerfilPeloGUID(null));

        assertThat(exception.getMessage(), is("Perfil não encontrado."));
    }

    @Test
    @DisplayName("Should Return A Perfil By Nome Successfully")
    public void buscandoPerfilPeloNomeCase1() {
        //having
        String perfil = "My Perfil";

        PerfilEntity entity = umPerfilEntity().agora();

        when(this.iPerfilRepository.findByNome(anyString())).thenReturn(Optional.of(entity));
        when(this.mapper.perfilEntityToPerfilApiResponse(entity)).thenReturn(new PerfilApiResponse());

        //when
        PerfilApiResponse perfilResponse = this.perfilService.buscandoPerfilPeloNome(perfil);

        //then
        Assertions.assertNotNull(perfilResponse);
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Get Perfil by Nome")
    public void buscandoPerfilPeloNomeCase2() {
        //having
        String perfil = "My Perfil";

        when(this.iPerfilRepository.findByNome(perfil)).thenReturn(Optional.empty());

        //when

        //then
        Exception exception = Assertions.assertThrows(PerfilNaoEncontradoException.class,
                () -> this.perfilService.buscandoPerfilPeloNome(perfil));

        assertThat(exception.getMessage(), is("Perfil não encontrado."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Find By Perfil At Nome And Name IsNull Or Empty")
    public void buscandoPerfilPeloNomeCase3() {
        //having

        //when

        //then
        Exception exception = Assertions.assertThrows(PerfilNaoEncontradoException.class,
                () -> this.perfilService.buscandoPerfilPeloNome(null));

        assertThat(exception.getMessage(), is("Perfil não encontrado."));
    }

    @Test
    @DisplayName("Should Update a Perfil status Successfully")
    public void alterandoPerfilCase1() {
        //having
        PerfilEntity entity = umPerfilEntity().agora();
        String guid = entity.getGuid();

        PerfilUpdateApiRequest request = umPerfilUpdateApiRequest().setNome("Other").agora();

        when(this.iPerfilRepository.save(any(PerfilEntity.class))).thenReturn(new PerfilEntity());
        when(this.iPerfilRepository.findByGuid(guid)).thenReturn(Optional.of(entity));
        when(this.mapper.perfilEntityToPerfilApiResponse(any(PerfilEntity.class))).thenReturn(new PerfilApiResponse());

        //when
        PerfilApiResponse perfilResponse = this.perfilService.alterandoPerfil(guid, request);

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

        PerfilUpdateApiRequest request = umPerfilUpdateApiRequest().agora();

        when(this.iPerfilRepository.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(guid);

        Exception exception = Assertions.assertThrows(PerfilNaoEncontradoException.class,
                () -> this.perfilService.alterandoPerfil(guid, request));

        assertThat(exception.getMessage(), is("Perfil não encontrado."));

    }

    @Test
    @DisplayName("Should Delete A Perfil Successfully")
    public void apagandoPerfilCase1() {

        //having
        String guid = UUID.randomUUID().toString();

        PerfilEntity entity = umPerfilEntity().setGuid(guid).setNome("Perfil Delete").agora();

        when(this.iPerfilRepository.findByGuid(guid)).thenReturn(Optional.of(entity));
        when(this.iUsuarioRepository.findTop1ByPerfisGuid(guid)).thenReturn(Optional.empty());

        //when
        this.perfilService.apagandoPerfil(guid);

        //then
        verify(this.iPerfilRepository, times(1)).delete(
                argThat(perfilEntity -> perfilEntity.getNome().equals("Perfil Delete")
                        && perfilEntity.getGuid().equals(guid))
        );

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Delete Perfil And It Does Not Exist")
    public void apagandoPerfilCase2() {

        //having
        String guid = UUID.randomUUID().toString();

        when(this.iPerfilRepository.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Exception exception = Assertions.assertThrows(PerfilNaoEncontradoException.class,
                () -> this.perfilService.apagandoPerfil(guid));

        assertThat(exception.getMessage(), is("Perfil não encontrado."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Delete Perfil And It Does In use")
    public void apagandoPerfilCase3() {

        //having
        String guid = UUID.randomUUID().toString();
        PerfilEntity entity = umPerfilEntity().setGuid(guid).setNome("Perfil In Use").agora();

        when(this.iPerfilRepository.findByGuid(guid)).thenReturn(Optional.of(entity));
        when(this.iUsuarioRepository.findTop1ByPerfisGuid(guid)).thenReturn(Optional.of(new UsuarioEntity()));

        //when

        //then
        Exception exception = Assertions.assertThrows(NaoPermitidoExcluirPerfilException.class,
                () -> this.perfilService.apagandoPerfil(guid));

        assertThat(exception.getMessage(), is("Não permitido Excluir. Perfil associado a algum Usuario."));

    }

}
