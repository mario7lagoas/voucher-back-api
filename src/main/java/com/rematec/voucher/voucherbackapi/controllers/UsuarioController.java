package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.UpdateStatusApiRequest;
import com.rematec.voucher.models.UsuarioApiRequest;
import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.models.UsuarioUpdateApiRequest;
import com.rematec.voucher.voucherbackapi.services.UsuarioServiceImpl;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsuarioController implements UsuarioApi {

    @Autowired
    private UsuarioServiceImpl usuarioService;
    @Autowired
    private VoucherUtil voucherUtil;

    @Override
    public ResponseEntity<List<UsuarioApiResponse>> buscandoListaUsuario() {
        return new ResponseEntity<List<UsuarioApiResponse>>(this.usuarioService.buscandoListaUsuario(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BuscandoListaPaginadaUsuario200Response> buscandoListaPaginadaUsuario(Integer page,
                                                                                                Integer size,
                                                                                                String nome) {

        return new ResponseEntity<BuscandoListaPaginadaUsuario200Response>(
                this.usuarioService.buscandoListaPaginadaUsuario(nome, page, size), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UsuarioApiResponse> buscandoUsuarioPeloGUID(String guid) {
        return new ResponseEntity<UsuarioApiResponse>(this.usuarioService.buscandoUsuarioPeloGUID(guid), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UsuarioApiResponse> criandoUsuario(UsuarioApiRequest usuarioApiRequest) {
        return new ResponseEntity<UsuarioApiResponse>(
                this.usuarioService.criandoUsuario(usuarioApiRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UsuarioApiResponse> alterandoUsuario(String guid, UsuarioUpdateApiRequest usuarioUpdateApiRequest) {
        return new ResponseEntity<UsuarioApiResponse>(
                this.usuarioService.alterandoUsuario(guid, usuarioUpdateApiRequest), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Void> apagandoUsuario(String guid) {
        this.usuarioService.apagandoUsuario(guid);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> alterandoStatusUsuario(String guid, UpdateStatusApiRequest updateStatusApiRequest) {
        this.usuarioService.alterandoStatusUsuario(guid, updateStatusApiRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<String> relatorioUsuario(List<UsuarioApiResponse> usuarioApiResponse) {
        return new ResponseEntity<String>(
                this.voucherUtil.print(new JRBeanCollectionDataSource(usuarioApiResponse), "usuarios"),
                HttpStatus.OK);
    }
}
