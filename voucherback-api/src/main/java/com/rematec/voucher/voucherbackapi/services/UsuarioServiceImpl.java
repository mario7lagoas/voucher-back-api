package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.voucherbackapi.exceptios.UsuarioCadastradoException;
import com.rematec.voucher.voucherbackapi.exceptios.UsuarioNaoEncontradoException;
import com.rematec.voucher.voucherbackapi.interfaces.mapper.VouckBackMapper;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IPerfilRepository;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IRoleRepository;
import com.rematec.voucher.voucherbackapi.interfaces.services.IUsuarioService;
import com.rematec.voucher.voucherbackapi.models.entities.PerfilEntity;
import com.rematec.voucher.voucherbackapi.models.entities.UsuarioEntity;
import com.rematec.voucher.voucherbackapi.models.requests.PerfilRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UsuarioPrintRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UsuarioRequest;
import com.rematec.voucher.voucherbackapi.models.requests.UpdateStatusResquest;
import com.rematec.voucher.voucherbackapi.models.response.PerfilResumidoResponse;
import com.rematec.voucher.voucherbackapi.models.response.UsuarioPrintResponse;
import com.rematec.voucher.voucherbackapi.models.response.UsuarioResponse;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.models.response.UsuariosPaginadaResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Autowired
    private VouckBackMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private IPerfilRepository iPerfilRepository;

    @Override
    public List<UsuarioResponse> getAllUsuarios() {
        List<UsuarioEntity> entities = iUsuarioRepository.findAll();
        log.info("Buscando Usuários");

        return mapper.listUsuarioEntityTolistUsuarioResponse(entities);
    }

    @Override
    public UsuarioResponse addUsuario(UsuarioRequest usuarioRequest) {
        if (iUsuarioRepository.findByEmail(usuarioRequest.getEmail()).isPresent()) {
            throw new UsuarioCadastradoException("E-mail já cadastrado.");
        }

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .guid(UUID.randomUUID().toString())
                .userName(usuarioRequest.getUserName())
                .email(usuarioRequest.getEmail())
                .perfis(getPerfis(usuarioRequest.getPerfis()))
                .status(usuarioRequest.getStatus())
                .password(passwordEncoder.encode(usuarioRequest.getPassword()))
              //  .roles(getRoles(usuarioRequest.getRoles()))
                .build();

        log.info("Adicionando novo usuário [{}]", usuarioRequest.getUserName());

        return mapper.usuarioEntityToUsuarioResponse(iUsuarioRepository.save(usuarioEntity));

    }

    @Override
    public UsuarioResponse updateUsuario(String guid, UsuarioRequest usuarioRequest) {
        UsuarioEntity usuario = iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario nao Encontrado"));

        if (checkData(usuarioRequest.getUserName()))
            usuario.setUserName(usuarioRequest.getUserName());

        if (checkData(usuarioRequest.getPassword()))
            usuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));

        if (checkData(usuarioRequest.getEmail()))
            usuario.setEmail(usuarioRequest.getEmail());

        if (usuarioRequest.getPerfis() != null && !usuarioRequest.getPerfis().isEmpty()) {
            usuario.setPerfis(getPerfis(usuarioRequest.getPerfis()));
        }
        if (usuarioRequest.getStatus() != null) {
            usuario.setStatus(usuarioRequest.getStatus());
        }
        /*
        if (usuarioRequest.getRoles() != null && !usuarioRequest.getRoles().isEmpty()) {
            usuario.setRoles(getRoles(usuarioRequest.getRoles()));
        }
        */


        log.info("Atualizando usuário [{}]", usuarioRequest.getUserName());

        return mapper.usuarioEntityToUsuarioResponse(iUsuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponse buscarUsuarioByGuid(String guid) {
        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario nao Encontrado"));

        log.info("Buscando usuário pelo GUID [{}]", guid);

        return mapper.usuarioEntityToUsuarioResponse(usuario);
    }

    @Override
    public void apagarUsuario(String guid) {
        UsuarioEntity usuario = this.iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario nao Encontrado"));

        log.info("Apagando usuário pelo GUID [{}]", guid);

        this.iUsuarioRepository.delete(usuario);

    }

    @Override
    public UsuarioResponse updateStatus(String guid, UpdateStatusResquest statusResquest) {

        UsuarioEntity usuario = iUsuarioRepository.findByGuid(guid)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario nao Encontrado"));

        usuario.setStatus(statusResquest.getStatus());

        log.info("Atualizando status do usuário [{}]", usuario.getUserName());

        return mapper.usuarioEntityToUsuarioResponse(iUsuarioRepository.save(usuario));

    }
    @Override
    public UsuariosPaginadaResponse obterUsuarioPaginadas(String nome, int page, int size) {

        return mapper.pageUsuariosEntityToUsuariosPaginadaResponse(
                this.iUsuarioRepository.findByUserNameContaining(nome, PageRequest.of(page, size)));

    }

    private Set<PerfilEntity> getPerfis(Set<PerfilRequest> perfis) {

        Set<PerfilEntity> listPerfils = perfis
                .stream()
                .map(p -> iPerfilRepository.findByNome(p.getNome()).get())
                .collect(Collectors.toSet());

        return listPerfils;
    }

    private boolean checkData(String data) {

        if (data != null && !data.isEmpty())
            return true;

        return false;
    }
    @Override
    public String printUsuarios(List<UsuarioPrintRequest> prints) {

        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
            parametros.put("logo", this.getClass().getResourceAsStream("/static/img/usuarioRelatorio.jpg"));
            InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/relatorio-de-usuarios.jasper");
/*
            List<UsuarioPrintResponse> responseList = new ArrayList<>();
            responseList.clear();

            prints.forEach(print -> {

                UsuarioPrintResponse response = UsuarioPrintResponse.builder()
                        .nome(print.getUserName())
                        .email(print.getEmail())
                        .status( print.getStatus() ? "Ativo" : "Inativo" )
                        .dataCadastro(print.getDataCadastro())
                        .dataAtualizacao(print.getDataAtualizacao())
                        .build();
                if (print.getPerfis() != null){
                    response.setPerfis( print.getPerfis()
                            .stream()
                            .map(PerfilResumidoResponse::getNome)
                            .toList());
                }

                responseList.add(response);

            });

 */
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
                    new JRBeanCollectionDataSource(prints));

            byte[] relatorio = JasperExportManager.exportReportToPdf(jasperPrint);

            String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(relatorio);

            return base64Pdf;

        }catch (JRException e) {
            throw new RuntimeException("Erro em gerar o PDF " + e);
        }

    }


}
