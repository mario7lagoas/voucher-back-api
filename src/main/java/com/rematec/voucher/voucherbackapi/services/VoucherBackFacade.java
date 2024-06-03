package com.rematec.voucher.voucherbackapi.services;

import com.rematec.voucher.models.BuscandoListaFiltroVoucher200Response;
import com.rematec.voucher.models.BuscandoListaPaginadaLoja200Response;
import com.rematec.voucher.models.BuscandoListaPaginadaPromocao200Response;
import com.rematec.voucher.models.BuscandoListaPaginadaUsuario200Response;
import com.rematec.voucher.models.ConsultaVoucherApiRequest;
import com.rematec.voucher.models.ConsultaVoucherApiResponse;
import com.rematec.voucher.models.EmpresaApiRequest;
import com.rematec.voucher.models.EmpresaApiResponse;
import com.rematec.voucher.models.LojaApiRequest;
import com.rematec.voucher.models.LojaApiResponse;
import com.rematec.voucher.models.LojaUpdateApiRequest;
import com.rematec.voucher.models.PerfilApiRequest;
import com.rematec.voucher.models.PerfilApiResponse;
import com.rematec.voucher.models.PerfilResumidoApiResponse;
import com.rematec.voucher.models.PerfilUpdateApiRequest;
import com.rematec.voucher.models.PromocaoApiRequest;
import com.rematec.voucher.models.PromocaoApiResponse;
import com.rematec.voucher.models.PromocaoUpdateApiRequest;
import com.rematec.voucher.models.UpdateStatusApiRequest;
import com.rematec.voucher.models.UsuarioApiRequest;
import com.rematec.voucher.models.UsuarioApiResponse;
import com.rematec.voucher.models.UsuarioUpdateApiRequest;
import com.rematec.voucher.models.VoucherApiRequest;
import com.rematec.voucher.models.VoucherFinalizeApiRequest;
import com.rematec.voucher.models.VoucherPromocaoApiRequest;
import com.rematec.voucher.models.VoucherPromocaoApiResponse;
import com.rematec.voucher.voucherbackapi.factories.ReportFactory;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoucherBackFacade {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private PerfilServiceImpl perfilService;

    @Autowired
    private LojaServiceImpl lojaService;

    @Autowired
    private PromocaoServiceImpl promocaoService;

    @Autowired
    private VoucherServiceImpl voucherService;

    @Autowired EmpresaServiceImpl empresaService;

    //Usuario
    public List<UsuarioApiResponse> buscandoListaUsuario() {
        return this.usuarioService.buscandoListaUsuario();
    }

    public BuscandoListaPaginadaUsuario200Response buscandoListaPaginadaUsuario(String nome, Integer page, Integer size) {
        return this.usuarioService.buscandoListaPaginadaUsuario(nome, page, size);
    }

    public UsuarioApiResponse buscandoUsuarioPeloGUID(String guid) {
        return this.usuarioService.buscandoUsuarioPeloGUID(guid);
    }

    public UsuarioApiResponse criandoUsuario(UsuarioApiRequest usuarioApiRequest) {
        return this.usuarioService.criandoUsuario(usuarioApiRequest);
    }

    public UsuarioApiResponse alterandoUsuario(String guid, UsuarioUpdateApiRequest usuarioUpdateApiRequest) {
        return this.usuarioService.alterandoUsuario(guid, usuarioUpdateApiRequest);
    }

    public void apagandoUsuario(String guid) {
        this.usuarioService.apagandoUsuario(guid);
    }

    public void alterandoStatusUsuario(String guid, UpdateStatusApiRequest updateStatusApiRequest) {
        this.usuarioService.alterandoStatusUsuario(guid, updateStatusApiRequest);
    }

    //Perfil
    public List<PerfilApiResponse> buscandoListaPerfil() {
        return this.perfilService.buscandoListaPerfil();
    }

    public List<PerfilResumidoApiResponse> buscandoListaResumidoPerfil() {
        return this.perfilService.buscandoListaResumidoPerfil();
    }

    public PerfilApiResponse buscandoPerfilPeloGUID(String guid) {
        return this.perfilService.buscandoPerfilPeloGUID(guid);
    }

    public PerfilApiResponse buscandoPerfilPeloNome(String nome) {
        return this.perfilService.buscandoPerfilPeloNome(nome);
    }

    public PerfilApiResponse criandoPerfil(PerfilApiRequest perfilApiRequest) {
        return this.perfilService.criandoPerfil(perfilApiRequest);
    }

    public PerfilApiResponse alterandoPerfil(String guid, PerfilUpdateApiRequest perfilApiRequest) {
        return this.perfilService.alterandoPerfil(guid, perfilApiRequest);
    }

    public void apagandoPerfil(String guid) {
        this.perfilService.apagandoPerfil(guid);
    }

    //loja
    public List<LojaApiResponse> buscandoListaLoja() {
        return this.lojaService.buscandoListaLoja();
    }

    public BuscandoListaPaginadaLoja200Response buscandoListaPaginadaLoja(String cnpj, Integer page, Integer size) {
        return this.lojaService.buscandoListaPaginadaLoja(cnpj, page, size);
    }

    public List<LojaApiResponse> buscandoListaLojaAtiva(String email) {
        return this.lojaService.buscandoListaLojaAtiva(email);
    }

    public LojaApiResponse buscandoLojaPeloGUID(String guid) {
        return this.lojaService.buscandoLojaPeloGUID(guid);
    }

    public LojaApiResponse criandoLoja(LojaApiRequest lojaApiRequest) {
        return this.lojaService.criandoLoja(lojaApiRequest);
    }

    public LojaApiResponse alterandoLoja(String guid, LojaUpdateApiRequest lojaApiRequest) {
        return this.lojaService.alterandoLoja(guid, lojaApiRequest);
    }

    public void apagandoLoja(String guid) {
        this.lojaService.apagandoLoja(guid);
    }

    public void alterandoStatusLoja(String guid, UpdateStatusApiRequest updateStatusApiRequest) {
        this.lojaService.alterandoStatusLoja(guid, updateStatusApiRequest);
    }

    //Promoções
    public List<PromocaoApiResponse> buscandoListaPromocao() {
        return this.promocaoService.buscandoListaPromocao();
    }

    public PromocaoApiResponse criandoPromocao(PromocaoApiRequest promocaoApiRequest) {
        return this.promocaoService.criandoPromocao(promocaoApiRequest);
    }

    public PromocaoApiResponse buscandoPromocaoPeloGUID(String guid) {
        return this.promocaoService.buscandoPromocaoPeloGUID(guid);
    }

    public PromocaoApiResponse alterandoPromocao(String guid, PromocaoUpdateApiRequest promocaoUpdateApiRequest) {
        return this.promocaoService.alterandoPromocao(guid, promocaoUpdateApiRequest);
    }

    public void ativandoPromocao(String guid, String autorAlteracao) {
        this.promocaoService.ativandoPromocao(guid, autorAlteracao);
    }

    public void apagandoPromocao(String guid) {
        this.promocaoService.apagandoPromocao(guid);
    }

    public BuscandoListaPaginadaPromocao200Response buscandoListaPaginadaPromocao(String descricao, Integer page,
                                                                                  Integer size) {
        return this.promocaoService.buscandoListaPaginadaPromocao(descricao, page, size);
    }

    public BuscandoListaPaginadaPromocao200Response buscandoListaFiltroPromocao(String descricao, String tipo,
                                                                                String status, String inicio, String fim,
                                                                                Integer page, Integer size, String email) {
        return this.promocaoService.buscandoListaFiltroPromocao(descricao, tipo, status, inicio, fim, page, size, email);
    }

    //Voucher
    public ConsultaVoucherApiResponse consultandoPromocoes(ConsultaVoucherApiRequest consulta) {
        return this.voucherService.consultandoPromocoes(consulta);
    }

    public void confirmandoVoucher(List<VoucherApiRequest> voucherApiRequest) {
        this.voucherService.confirmandoVoucher(voucherApiRequest);
    }

    public void cancelandoVoucher(List<VoucherApiRequest> voucherApiRequest) {
        this.voucherService.cancelandoVoucher(voucherApiRequest);
    }

    public VoucherPromocaoApiResponse resgatandoVoucher(VoucherPromocaoApiRequest promocaoRequest) {
        return this.voucherService.resgatandoVoucher(promocaoRequest);
    }

    public void consumindoVoucher(VoucherFinalizeApiRequest voucherFinalizeApiRequest) {
        this.voucherService.consumindoVoucher(voucherFinalizeApiRequest);
    }

    public void estornandoVoucher(VoucherFinalizeApiRequest voucherFinalizeApiRequest) {
        this.voucherService.estornandoVoucher(voucherFinalizeApiRequest);
    }

    public BuscandoListaFiltroVoucher200Response buscandoListaFiltroVoucher(
            Integer page, Integer size, String codigo, String descricao, String clienteCpf, String pdv, String cupomResgate,
            String inicio, String fim, String voucherStatus, String filialCnpj, String tipoDesconto) {
        return this.voucherService.buscandoListaFiltroVoucher(page, size, codigo, descricao, clienteCpf, pdv, cupomResgate,
                inicio, fim, voucherStatus, filialCnpj, tipoDesconto);
    }

    //Report Base64
    public String report(JRBeanCollectionDataSource collectionDataSource, String relatorio) {
        return ReportFactory.report(collectionDataSource, relatorio);
    }

    //Empresa
    public List<EmpresaApiResponse> buscandoListaEmpresa() {
        return this.empresaService.buscandoListaEmpresa();
    }

    public EmpresaApiResponse criandoEmpresa(EmpresaApiRequest empresaApiRequest) {
         return this.empresaService.criandoEmpresa(empresaApiRequest);
    }

}
