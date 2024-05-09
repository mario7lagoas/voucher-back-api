package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.voucherbackapi.models.requests.UsuarioPrintRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UsuarioRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UpdateStatusResquest;
import com.rematec.voucher.voucherbackapi.models.response.UsuarioResponse;
import com.rematec.voucher.voucherbackapi.models.response.UsuariosPaginadaResponse;
import com.rematec.voucher.voucherbackapi.services.UsuarioServiceImpl;
import com.rematec.voucher.voucherbackapi.utils.VoucherUtil;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuario")
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuarioResponse>> getAllUsuarios() {

        return ResponseEntity.ok().body(this.usuarioService.getAllUsuarios());
    }

    @GetMapping(value = "/paginada", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuariosPaginadaResponse> getAllLojasPaginada(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                                                        @RequestParam(name = "nome", defaultValue = "") String nome) {
        return new ResponseEntity<UsuariosPaginadaResponse>(this.usuarioService.obterUsuarioPaginadas(nome, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioResponse> buscarUsuarioByGuid(@PathVariable("guid") String guid) {

        return new ResponseEntity<>(this.usuarioService.buscarUsuarioByGuid(guid), HttpStatus.OK);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioResponse> addUsuario(@RequestBody @Valid UsuarioRequest usuarioRequest) {

        return new ResponseEntity<>(this.usuarioService.addUsuario(usuarioRequest), HttpStatus.CREATED);
    }

    @PutMapping(value = "{guid}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioResponse> editUsuario(@PathVariable("guid") String guid,
                                                       @RequestBody UsuarioRequest usuarioRequest) {
        return new ResponseEntity<>(this.usuarioService.updateUsuario(guid, usuarioRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "{guid}")
    public ResponseEntity deleteUsuario(@PathVariable("guid") String guid) {
        this.usuarioService.apagarUsuario(guid);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/print", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> printUsuarios(@RequestBody List<UsuarioPrintRequest> prints) {
        return new ResponseEntity<String>(this.voucherUtil.print(new JRBeanCollectionDataSource(prints), "usuarios"),
                HttpStatus.OK);
    }

    @PatchMapping(value = "{guid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarStatus(@PathVariable("guid") String guid,
                                              @RequestBody @Valid
                                              UpdateStatusResquest statusResquest) {
        this.usuarioService.updateStatus(guid, statusResquest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
