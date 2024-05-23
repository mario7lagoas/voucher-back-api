package com.rematec.voucher.voucherbackapi.controlles;

import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.voucherbackapi.controllers.UsuarioController;
import com.rematec.voucher.voucherbackapi.services.VoucherBackFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static com.rematec.voucher.voucherbackapi.builders.UsuarioApiResponseBuilder.umUsuarioApiResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {
    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private VoucherBackFacade usuarioService;

    @Test
    @DisplayName("Should Call Get All UsuarioApiResponse And Return Code 200")
    public void buscandoListaUsuarioCase1(){
        //Having
        when(this.usuarioService.buscandoListaUsuario()).thenReturn(Collections.singletonList(umUsuarioApiResponse()
                .agora()));

        //when
        ResponseEntity<List<UsuarioApiResponse>> response = this.usuarioController.buscandoListaUsuario();

        //then
        Assertions.assertNotNull(response);

        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody().size(), is(1));
        assertThat(response.getBody().get(0).getUserName(), is("Any Name"));
        assertThat(response.getBody().get(0).getEmail(), is("any@email.com"));

    }




}
