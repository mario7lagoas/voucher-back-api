# AGENTS.md - Guia de Desenvolvimento de IA para VoucherBack API

## Visão Geral do Projeto
**VoucherBack API** é um microserviço Spring Boot 3.2.4 (Java 17) para geração de vouchers de desconto (valor fixo ou percentual) para sistemas PDV (Ponto de Venda) no varejo. Todas as entidades rastreiam mudanças com identificadores `guid` e timestamps automáticos via `BaseEntity`.

**Valor Principal**: Permite que sistemas PDV consultem promoções disponíveis, emitam/confirmem/cancelem vouchers e registrem consumo final com relatórios via JasperReports.

## Padrão de Arquitetura: Façade + Camada de Serviço + Repositórios

### Padrão Principal: VoucherBackFacade
O **`VoucherBackFacade` é o ponto único de orquestração** para todas as operações de negócio. Ele:
- Atua como um @Component (não um controlador) que agrega 6 serviços de domínio
- Delega todas as requisições para implementações `IXxxService` subjacentes
- **Motivo**: Centraliza lógica transversal e facilita testes via injeção de dependência
- **Todos os controladores chamam essa façade** - nunca chamar serviços diretamente

Exemplo de padrão de delegação:
```java
// Façade delega para o serviço
public UsuarioApiResponse criandoUsuario(UsuarioApiRequest usuarioApiRequest) {
    return this.usuarioService.criandoUsuario(usuarioApiRequest);
}
```

### Arquitetura da Camada de Serviço
- **Padrão**: Interfaces de serviço + Implementações com package-private (`IXxxService` → `XxxServiceImpl`)
- **Serviços**: `IUsuarioService`, `IPerfilService`, `ILojaService`, `IPromocaoService`, `IVoucherService`, `IEmpresaService`
- **Por que privado**: Garante que façade seja o único ponto de entrada; testes usam `@InjectMocks` em implementações
- **Persistência**: Cada serviço usa diretamente seu repositório (ex: `IUsuarioRepository`)

### Mapeamento de Dados: MapStruct
- **Mapper**: `VouckBackMapper` (@Mapper com componentModel="spring")
- **Padrão**: Entidade → ApiResponse, Requisição → operações internas
- **Modelos Gerados**: Vivem em `target/generated-sources/openapi/` (do `swagger.yml`)
- **Nota**: Modelos no pacote `com.rematec.voucher.models.*` (saída do gerador OpenAPI)

## Autenticação & Segurança

### Fluxo de Token JWT
- **Serviço**: `AutenticacaoService` gerencia geração/validação de tokens
- **Credenciais**: Armazenadas em `application.yml` sob `jwt.secret` (codificado em base64) + `jwt.expiration` (120s) e `jwt.refreshToken` (1h)
- **Claims**: Inclui autoridades do usuário do banco de dados
- **Cadeia de Filtros**: `SecurityConfiguration` usa beans `LoginFiltro` e `AutenticacaoFiltro`

### Endpoints Públicos (Sem Autenticação Obrigatória)
Configurados em `SecurityConfiguration.PUBLIC_MATCHERS`:
- `/login`, `/login/refresh`, `/login/revoke`
- `/voucher/consulta`, `/voucher/cancel`, `/voucher/confirm`, `/voucher/resgate`, `/voucher/consumer`, `/voucher/rollback`
- `/empresa/resumido`
- Endpoints da UI do Swagger

### Enum de Permissão
- Localizado em `enums.PermissaoEnum`
- Usado no controle de acesso baseado em roles nos controladores

## Convenções de Testes

### Padrão Builder para Dados de Teste
- **Localização**: `src/test/java/.../builders/` (ex: `ConsultaVoucherApiRequestBuilder`, `PromocaoEntityBuilder`)
- **Uso**: Métodos builder `static` com nomenclatura fluida (ex: `umaConsultaVoucherApiRequest()`)
- **Por que**: Substitui sobrecarga de construtores; torna assertions de teste legíveis

Exemplo de teste:
```java
import static com.rematec.voucher.voucherbackapi.builders.ConsultaVoucherApiRequestBuilder.umaConsultaVoucherApiRequest;
// Teste usa: umaConsultaVoucherApiRequest() para construir fixture
```

### Estratégia de Mock
- **Framework**: JUnit 5 + Mockito 5.11.0
- **Anotações**: `@Mock` para dependências, `@InjectMocks` em implementações de serviço
- **Extensão**: `@ExtendWith(MockitoExtension.class)` em classes de teste
- **Nota**: Testes verificam **apenas comportamento do serviço** (não controladores); controladores são testados separadamente

## Banco de Dados & Persistência

### Configuração (application.yml)
- **Driver**: MySQL 8+ (`com.mysql.cj.jdbc.Driver`)
- **URL**: `jdbc:mysql://localhost:3306/voucherback`
- **Credenciais**: `root:0381` (codificado em config de desenvolvimento)
- **Flyway**: Habilitado para gestão de migrações (`org.flywaydb:flyway-mysql`)
- **JPA/Hibernate**: `ddl-auto: update` (evolui schema automaticamente)

### Classe Base de Entidade
- **Todas as entidades estendem** `BaseEntity` (Jakarta @MappedSuperclass)
- **Campos**: `guid` (identificador único), `dataCadastro` (CreationTimestamp), `dataAtualizacao` (UpdateTimestamp)
- **Lombok**: Usa `@SuperBuilder` para construção segura com herança

### Entidades
1. `UsuarioEntity` - Contas de usuário
2. `PerfilEntity` - Perfis/roles de usuário
3. `EmpresaEntity` - Organização (pai de Lojas)
4. `LojaEntity` - Loja/filial (filha de Empresa)
5. `PromocaoEntity` - Definição de promoção
6. `VoucherEntity` - Instância de voucher emitida (vinculada a Promocao)
7. `RoleEntity` - Mapeamento de autoridades

## Padrão de Resposta de API

### Paginação
- **Implementada em serviços**: Métodos terminados com `buscandoListaPaginada*` retornam `BuscandoListaPaginada*200Response`
- **Campos**: Número da página (baseado em 0), tamanho (registros por página), contagem total
- **Biblioteca**: `Page<T>` do Spring Data mapeado para resposta customizada por `VouckBackMapper`

### Filtragem
- **Buscas complexas**: Use métodos `buscandoListaFiltro*` (ex: `buscandoListaFiltroPromocao`, `buscandoListaFiltroVoucher`)
- **Baseada em critérios**: Pasta `repositories/criteria/` contém construtores de query reutilizáveis

### Exemplo de Ciclo de Vida do Voucher
```
1. consultandoPromocoes()  → Consulta promoções disponíveis para cliente PDV
2. confirmandoVoucher()    → Servidor registra emissão
3. resgatandoVoucher()     → Cliente usa voucher
4. consumindoVoucher()     → Finaliza e aplica desconto
5. estornandoVoucher()     → Reversão se necessário
```

## Build & Desenvolvimento

### Comandos de Build (Maven)
```powershell
./mvnw clean package  # Build completo com testes
./mvnw clean install  # Instala no repositório local
./mvnw spring-boot:run  # Servidor de desenvolvimento
```

### Geração de Código
- **Gerador OpenAPI**: Executa durante fase `mvn compile`
- **Entrada**: `src/main/resources/swagger.yml`
- **Saída**: Controladores + Modelos em `target/generated-sources/openapi/`
- **Reexecuta em**: Qualquer mudança no `swagger.yml`; reconstrói DTOs e stubs de delegação
- **Nota**: Não edite arquivos gerados diretamente (perdidos no rebuild)

### Processamento de Anotações
- **Lombok**: Gera getters/setters/builders em tempo de compilação
- **MapStruct**: Gera implementações de mapeamento em tempo de compilação
- **Ordem**: Lombok deve processar antes de MapStruct; configurado em `maven-compiler-plugin` `annotationProcessorPaths`

## Relatórios (JasperReports)

### Configuração
- **Dependência**: `jasperreports` 6.20.6 + add-ons de fontes/funções
- **Factory**: `ReportFactory` gerencia compilação/renderização de relatórios
- **Uso**: `VoucherBackFacade.report()` aceita `JRBeanCollectionDataSource` + nome do relatório
- **Formato**: Saída codificada em Base64 para respostas de API

### Localização dos Arquivos de Relatório
- `src/main/resources/relatorios/` (templates `.jrxml`)

## Constantes & Utilitários

### VoucherConstants
- Localizado em `constants/VoucherConstants`
- Importado em `VouckBackMapper` para mapeamentos baseados em anotações
- Use para valores fixos específicos do domínio (códigos de status, definições de tipos)

### VoucherUtil
- Classe utilitária para lógica de validação de promoção/voucher
- Métodos auxiliares sem estado (não é um serviço)

### JWTUtil
- Helpers para análise e validação de tokens
- Usados por filtros e `AutenticacaoService`

## Referência Rápida de Organização de Arquivos
```
src/main/java/com/rematec/voucher/voucherbackapi/
├── controllers/       → Endpoints REST (gerados + delegados)
├── services/          → Lógica de negócio (façade + impl de serviço)
├── repositories/      → Acesso a dados (JPA + critérios)
├── mapper/            → DTOs MapStruct
├── models/
│   ├── entities/      → Entidades JPA (todas estendem BaseEntity)
│   ├── filter/        → DTOs de filtro de query
│   └── UsuarioAutenticado.java
├── security/          → Implementação de UserDetail
├── config/            → Configuração de Segurança + CORS
├── constants/         → Constantes de domínio
├── utils/             → Utilitários auxiliares
├── builders/          → Builders de factory
├── enums/             → Enums de Permissão + Status
├── factories/         → ReportFactory
└── schedule/          → Tarefas agendadas (se houver)

src/main/resources/
├── application.yml    → Config de ambiente (BD, JWT, datasource)
├── swagger.yml        → Contrato de API (regenera modelos/controladores)
└── relatorios/        → Templates JasperReports (.jrxml)

src/test/java/...     → Classes de teste + builders
```

## Armadilhas Comuns & Padrões

1. **Nunca contorne a Façade**: Toda lógica de negócio deve fluir através de `VoucherBackFacade`, não diretamente para serviços
2. **Entidade vs. Modelo**: Entidades JPA usam `@Entity` + `@Table`; modelos de API são POJOs OpenAPI gerados (pacotes diferentes)
3. **Geração de GUID**: Garanta que `guid` seja definido antes de salvar entidades (tipicamente na camada de serviço ou construtor)
4. **Serviços com Package-Private**: Serviços não são públicos; são acessados via injeção de dependência da façade apenas
5. **Auditoria de Timestamp**: Use `@CreationTimestamp` / `@UpdateTimestamp` do Hibernate; não defina datas manualmente

## Fluxo de Desenvolvimento

1. **Defina o contrato de API primeiro**: Atualize `swagger.yml`, execute `mvn generate-sources`
2. **Implemente lógica de serviço**: Adicione método à façade, implemente em serviço + repositório
3. **Escreva testes**: Use builders, faça mock de repositórios, assert na saída do serviço
4. **Execute localmente**: `mvn spring-boot:run` (MySQL deve estar rodando em `localhost:3306`)
5. **Build & verificar**: `mvn clean package` (inclui suite de testes, geração OpenAPI, processamento de anotações)

