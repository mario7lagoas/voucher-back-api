package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.UsuarioApiResponse;
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

import java.util.Arrays;
import java.util.List;

import static com.rematec.voucher.voucherbackapi.builders.UsuarioEntityBuilder.umUsuarioEntity;
import static org.mockito.Mockito.when;

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

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should Return A List UsuarioApiResponse Successfully")
    public void buscandoListaLojaCase1() {
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
}
