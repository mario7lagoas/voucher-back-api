package com.rematec.voucher.voucherbackapi.controllers;

import com.rematec.voucher.voucherbackapi.models.requests.UsuarioRequest;
import com.rematec.voucher.voucherbackapi.models.response.UsuarioCadastroResponse;
import com.rematec.voucher.voucherbackapi.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioCadastroResponse>> getAll() {

        return new ResponseEntity<>(usuarioService.gelAll(), HttpStatus.OK);
    }

    @GetMapping("/{guid}")
    public ResponseEntity<UsuarioCadastroResponse> buscarUsuarioByGuid(@PathVariable("guid") String guid) {

        return new ResponseEntity<>(usuarioService.buscarUsuarioByGuid(guid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UsuarioCadastroResponse> addUsuario(@RequestBody @Valid UsuarioRequest usuarioRequest) {

        return new ResponseEntity<>(usuarioService.addUsuario(usuarioRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{guid}")
    public ResponseEntity<UsuarioCadastroResponse> editUsuario(@PathVariable("guid") String guid,
                                                               @RequestBody UsuarioRequest usuarioRequest){
        return new ResponseEntity<>(usuarioService.updateUsuario(guid, usuarioRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{guid}")
    public ResponseEntity deleteUsuario(@PathVariable("guid") String guid){
        this.usuarioService.apagarUsuario(guid);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
