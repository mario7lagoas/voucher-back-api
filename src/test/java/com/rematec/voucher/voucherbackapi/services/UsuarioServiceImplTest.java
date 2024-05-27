package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.UpdateStatusApiRequest;
import com.rematec.voucher.models.UsuarioApiRequest;
import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.models.UsuarioUpdateApiRequest;
import com.rematec.voucher.voucherbackapi.exceptios.BadRequestException;
import com.rematec.voucher.voucherbackapi.exceptios.UsuarioCadastradoException;
import com.rematec.voucher.voucherbackapi.exceptios.UsuarioNaoEncontradoException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.rematec.voucher.voucherbackapi.builders.UpdateStatusApiRequestBuilder.umUpdateStatusApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.UsuarioApiRequestBuilder.umUsuarioApiRequest;
import static com.rematec.voucher.voucherbackapi.builders.UsuarioEntityBuilder.umUsuarioEntity;
import static com.rematec.voucher.voucherbackapi.builders.UsuarioUpdateApiRequestBuilder.umUsuarioUpdateApiRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Mock
    private IUsuarioRepository iUsuarioRepository;

    @Spy
    private VouckBackMapper mapper;

    @Spy
    private VoucherUtil voucherUtil;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should Return A List UsuarioApiResponse Successfully")
    public void buscandoListaUsuarioCase1() {
        //having
        when(this.iUsuarioRepository.findAll()).thenReturn(new CollisionCheckStack<UsuarioEntity>());

        //when
        List<UsuarioApiResponse> responses = this.usuarioService.buscandoListaUsuario();

        //then
        Assertions.assertNotNull(responses);

    }

    @Test
    @DisplayName("Should Return A List UsuarioApiResponse paginator Successfully")
    public void buscandoListaPaginadaUsuarioCase1() {

        //having
        String nome = "anyname";
        Integer page = 0;
        Integer size = 10;

        Pageable pageable = PageRequest.of(page, size);
        List<UsuarioEntity> entities = Arrays.asList(umUsuarioEntity().agora());
        Page<UsuarioEntity> usuarioEntityPage = new PageImpl<>(entities, pageable, 1L);

        when(this.iUsuarioRepository.findByUserNameContaining(nome, PageRequest.of(page, size))).thenReturn(usuarioEntityPage);
        when(this.mapper.pageUsuariosEntityToUsuariosApiPaginadaResponse(usuarioEntityPage))
                .thenReturn(new BuscandoListaPaginadaUsuario200Response());

        //when
        BuscandoListaPaginadaUsuario200Response responses = this.usuarioService.buscandoListaPaginadaUsuario(
                nome, page, size);

        //then
        Assertions.assertNotNull(responses);

    }

    @Test
    @DisplayName("Should Return A UsuarioApiResponse By GUID Successfully")
    public void buscandoUsuarioPeloGUIDCase1() {

        //having
        String guid = UUID.randomUUID().toString();
        UsuarioEntity entity = umUsuarioEntity().agora();

        when(this.iUsuarioRepository.findByGuid(anyString())).thenReturn(Optional.of(entity));
        when(this.mapper.usuarioEntityToUsuarioApiResponse(entity)).thenReturn(new UsuarioApiResponse());

        //when
        UsuarioApiResponse response = this.usuarioService.buscandoUsuarioPeloGUID(guid);

        //then
        Assertions.assertNotNull(guid);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Get LojaApiResponse By GUID")
    public void buscandoUsuarioPeloGUIDCase2() {

        //having
        when(this.iUsuarioRepository.findByGuid(anyString())).thenReturn(Optional.empty());

        //when

        //then
        Exception exception = Assertions.assertThrows(UsuarioNaoEncontradoException.class,
                () -> this.usuarioService.buscandoUsuarioPeloGUID(anyString()));

        assertThat(exception.getMessage(), is("Usuario não encontrado."));

    }

    @Test
    @DisplayName("Should Create a Usuario Successfully")
    public void criandoLojaCase1() {

        //having

        UsuarioApiRequest request = umUsuarioApiRequest().userName("New User").comPerfis().agora();

        when(this.iUsuarioRepository.save(any(UsuarioEntity.class))).thenReturn(new UsuarioEntity());
        when(this.voucherUtil.listUsuarioPerfilApiRequestToListPerfilEntity(anyList())).thenReturn(new HashSet<>());
        when(this.mapper.usuarioEntityToUsuarioApiResponse(any(UsuarioEntity.class))).thenReturn(new UsuarioApiResponse());

        //when
        UsuarioApiResponse response = this.usuarioService.criandoUsuario(request);

        //then
        Assertions.assertNotNull(response);

        verify(this.iUsuarioRepository, times(1)).save(
                argThat(usuarioArg -> usuarioArg.getUserName().equals("New User")
                        && usuarioArg.getGuid() != null)
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Usuario Exist")
    public void criandoLojaCase2() {

        //having
        UsuarioApiRequest request = umUsuarioApiRequest().userName("New User").agora();
        UsuarioEntity entity = umUsuarioEntity().agora();

        when(this.iUsuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(entity));

        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(UsuarioCadastradoException.class,
                () -> this.usuarioService.criandoUsuario(request));

        assertThat(exception.getMessage(), is("E-mail já cadastrado."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Usuario Email Is Null")
    public void criandoLojaCase3() {

        //having
        UsuarioApiRequest request = umUsuarioApiRequest().email(null).agora();
        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.usuarioService.criandoUsuario(request));

        assertThat(exception.getMessage(), is("E-mail do usuário obrigatório."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Usuario Password Is Null")
    public void criandoLojaCase4() {

        //having
        UsuarioApiRequest request = umUsuarioApiRequest().password(null).agora();
        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.usuarioService.criandoUsuario(request));

        assertThat(exception.getMessage(), is("Senha do usuário obrigatório."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Usuario UserName Is Null")
    public void criandoLojaCase5() {

        //having
        UsuarioApiRequest request = umUsuarioApiRequest().userName(null).comPerfis().agora();
        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.usuarioService.criandoUsuario(request));

        assertThat(exception.getMessage(), is("Nome do usuário obrigatório."));

    }


    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Usuario Email Is Empty")
    public void criandoLojaCase6() {

        //having
        UsuarioApiRequest request = umUsuarioApiRequest().email("").agora();
        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.usuarioService.criandoUsuario(request));

        assertThat(exception.getMessage(), is("E-mail do usuário obrigatório."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Usuario Password Is Empty")
    public void criandoLojaCase7() {

        //having
        UsuarioApiRequest request = umUsuarioApiRequest().password("").agora();
        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.usuarioService.criandoUsuario(request));

        assertThat(exception.getMessage(), is("Senha do usuário obrigatório."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Usuario UserName Is Empty")
    public void criandoLojaCase8() {

        //having
        UsuarioApiRequest request = umUsuarioApiRequest().userName("").comPerfis().agora();
        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.usuarioService.criandoUsuario(request));

        assertThat(exception.getMessage(), is("Nome do usuário obrigatório."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Usuario Status Is Null")
    public void criandoLojaCase9() {

        //having
        UsuarioApiRequest request = umUsuarioApiRequest().status(null).agora();
        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.usuarioService.criandoUsuario(request));

        assertThat(exception.getMessage(), is("Status do usuário obrigatório."));

    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Add Usuario Perfis Is Null")
    public void criandoLojaCase10() {

        //having
        UsuarioApiRequest request = umUsuarioApiRequest().agora();
        //when

        //then
        Assertions.assertNotNull(request);
        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> this.usuarioService.criandoUsuario(request));

        assertThat(exception.getMessage(), is("Perfil do usuário obrigatório."));

    }

    @Test
    @DisplayName("Should Update a Loja Successfully")
    public void alterandoUsuarioCase1() {
        //having
        UsuarioEntity usuario = umUsuarioEntity().agora();
        String guid = usuario.getGuid();

        UsuarioUpdateApiRequest request = umUsuarioUpdateApiRequest().userName("Other").comPerfis().agora();

        when(this.iUsuarioRepository.save(any(UsuarioEntity.class))).thenReturn(new UsuarioEntity());
        when(this.iUsuarioRepository.findByGuid(guid)).thenReturn(Optional.of(usuario));
        when(this.voucherUtil.listUsuarioPerfilApiRequestToListPerfilEntity(anyList())).thenReturn(new HashSet<>());
        when(this.mapper.usuarioEntityToUsuarioApiResponse(any(UsuarioEntity.class))).thenReturn(new UsuarioApiResponse());

        //when
        UsuarioApiResponse response = this.usuarioService.alterandoUsuario(guid, request);

        //then
        Assertions.assertNotNull(response);

        verify(this.iUsuarioRepository, times(1)).save(
                argThat(usuarioArg -> usuarioArg.getUserName().equals("Other")
                        && usuarioArg.getGuid().equals(guid)
                )
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Update Usuario And It Does Not Exist")
    public void alterandoUsuarioCase2() {
        //having
        String guid = UUID.randomUUID().toString();
        UsuarioUpdateApiRequest request = umUsuarioUpdateApiRequest().agora();

        when(this.iUsuarioRepository.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(guid);

        Exception exception = Assertions.assertThrows(UsuarioNaoEncontradoException.class,
                () -> this.usuarioService.alterandoUsuario(guid, request));

        assertThat(exception.getMessage(), is("Usuario não encontrado."));
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Update Usuario And It Does Not Exist")
    public void alterandoUsuarioCase3() {
        //having
        String guid = UUID.randomUUID().toString();
        UsuarioEntity usuarioEntity = umUsuarioEntity().agora();
        UsuarioUpdateApiRequest request = umUsuarioUpdateApiRequest().userName("Other Name").agora();

        when(this.iUsuarioRepository.findByGuid(guid)).thenReturn(Optional.of(new UsuarioEntity()));
        when(this.iUsuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(usuarioEntity));

        //when

        //then
        Assertions.assertNotNull(request);
        Assertions.assertNotNull(guid);

        Exception exception = Assertions.assertThrows(UsuarioCadastradoException.class,
                () -> this.usuarioService.alterandoUsuario(guid, request));

        assertThat(exception.getMessage(), is("E-mail já cadastrado."));
    }

    @Test
    @DisplayName("Should Delete A Usuario Successfully")
    public void apagandoUsuarioCase1() {
        //having
        String guid = UUID.randomUUID().toString();
        UsuarioEntity usuarioEntity = umUsuarioEntity().guid(guid).userName("User Delete").agora();

        when(this.iUsuarioRepository.findByGuid(guid)).thenReturn(Optional.of(usuarioEntity));

        //when
        this.usuarioService.apagandoUsuario(guid);

        //then
        Assertions.assertNotNull(guid);

        verify(this.iUsuarioRepository, times(1)).delete(
                argThat(usuarioArg -> usuarioArg.getUserName().equals("User Delete")
                        && usuarioArg.getGuid().equals(guid)
                )
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Delete Usuario And It Does Not Exist")
    public void apagandoUsuarioCase2() {
        //having
        String guid = UUID.randomUUID().toString();

        when(this.iUsuarioRepository.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Assertions.assertNotNull(guid);
        Exception exception = Assertions.assertThrows(UsuarioNaoEncontradoException.class,
                () -> this.usuarioService.apagandoUsuario(guid));

        assertThat(exception.getMessage(), is("Usuario não encontrado."));
    }

    @Test
    @DisplayName("Should Update Status A Usuario Successfully")
    public void alterandoStatusUsuarioCase1() {
        //having
        UsuarioEntity usuarioEntity = umUsuarioEntity().agora();
        String guid = usuarioEntity.getGuid();
        UpdateStatusApiRequest request = umUpdateStatusApiRequest().status(false).agora();

        when(this.iUsuarioRepository.findByGuid(guid)).thenReturn(Optional.of(usuarioEntity));
        when(this.iUsuarioRepository.save(any(UsuarioEntity.class))).thenReturn(new UsuarioEntity());
        //when
        this.usuarioService.alterandoStatusUsuario(guid, request);

        //then
        Assertions.assertNotNull(guid);

        verify(this.iUsuarioRepository, times(1)).save(
                argThat(usuarioArg -> usuarioArg.getStatus().equals(false)
                        && usuarioArg.getGuid().equals(guid)
                )
        );
    }

    @Test
    @DisplayName("Should Thrown An Exception When Try To Update Status a Usuario And It Does Not Exist")
    public void alterandoStatusUsuarioCase2() {
        //having
        String guid = UUID.randomUUID().toString();
        UpdateStatusApiRequest request = umUpdateStatusApiRequest().status(false).agora();

        when(this.iUsuarioRepository.findByGuid(guid)).thenReturn(Optional.empty());

        //when

        //then
        Assertions.assertNotNull(guid);
        Exception exception = Assertions.assertThrows(UsuarioNaoEncontradoException.class,
                () -> this.usuarioService.alterandoStatusUsuario(guid, request));

        assertThat(exception.getMessage(), is("Usuario não encontrado."));
    }

}
