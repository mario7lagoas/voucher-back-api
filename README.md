# VoucherBack API

APIs de serviço para geração e gerenciamento de vouchers de desconto (valor fixo ou percentual) em sistemas PDV (Ponto de Venda) no varejo.

## 🚀 Quick Start

### Pré-requisitos
- **Java 17+**
- **Maven 3.8+** (ou use `./mvnw`)
- **MySQL 8+** rodando em `localhost:3306`
  - Usuário: `root`
  - Senha: `0381`
  - Banco de dados: `voucherback`

Se MySQL não estiver instalado, crie o banco manualmente:
```sql
CREATE DATABASE voucherback CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Instalação & Setup Inicial
1. Clone o repositório:
```powershell
git clone <repo-url>
cd voucherback-api
```

2. Compile e baixe dependências:
```powershell
./mvnw clean install
```

3. Inicie o servidor de desenvolvimento:
```powershell
./mvnw spring-boot:run
```

Servidor iniciará em `http://localhost:8080`

---

## 📋 Comandos Frequentes

### Build & Testes
```powershell
# Build completo com suite de testes
./mvnw clean package

# Apenas compilar (sem testes)
./mvnw clean compile

# Rodar testes específicos
./mvnw -Dtest=VoucherBackFacadeTest test

# Rodar suite de testes completa
./mvnw test

# Build local (instala no repositório .m2)
./mvnw clean install
```

### Desenvolvimento
```powershell
# Iniciar servidor em modo desenvolvimento (hot-reload)
./mvnw spring-boot:run

# Regenerar código gerado a partir do swagger.yml
./mvnw generate-sources

# Limpar arquivos compilados
./mvnw clean
```

### Análise & Validação
```powershell
# Verificar se há erros de compilação sem gerar JAR
./mvnw compile

# Executar testes e gerar relatório (surefire)
./mvnw test

# Construir JAR executável
./mvnw package
```

---

## 🏗️ Estrutura do Projeto

```
src/main/java/com/rematec/voucher/voucherbackapi/
├── controllers/         → Endpoints REST (gerados + delegados para Façade)
├── services/            → Lógica de negócio (VoucherBackFacade + Impl)
├── repositories/        → Acesso a dados via JPA + queries customizadas
├── mapper/              → Mapeamento de DTOs (MapStruct)
├── models/
│   ├── entities/        → Entidades JPA (todas estendem BaseEntity)
│   ├── filter/          → DTOs de filtro para queries dinâmicas
│   └── UsuarioAutenticado.java
├── security/            → Configuração de autenticação JWT
├── config/              → Spring Security + CORS configuration
├── constants/           → Constantes de domínio
├── utils/               → Utilidades auxiliares (validação, JWT, etc)
├── enums/               → Enums (PermissaoEnum, StatusEnum, etc)
└── factories/           → ReportFactory (geração de relatórios)

src/main/resources/
├── application.yml      → Configuração de ambiente (BD, JWT, porta)
├── swagger.yml          → Contrato OpenAPI (regenera modelos/controllers)
└── relatorios/          → Templates JasperReports (.jrxml)

src/test/
├── java/.../builders/   → Builders para fixtures de teste
└── resources/           → Dados de teste
```

---

## 🔐 Autenticação & Segurança

### Obter Token JWT
```bash
# POST para /login
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": "seu_usuario",
    "senha": "sua_senha"
  }'

# Resposta contém token JWT
# {
#   "token": "eyJhbGc...",
#   "refreshToken": "...",
#   "expiresIn": 120
# }
```

### Usar Token em Requisições
```bash
curl -X GET http://localhost:8080/api/endpoint \
  -H "Authorization: Bearer SEU_TOKEN_JWT"
```

### Endpoints Públicos (sem autenticação)
- `POST /login`
- `POST /login/refresh`
- `POST /login/revoke`
- `POST /voucher/consulta` - Consultar vouchers
- `POST /voucher/confirm` - Confirmar voucher
- `POST /voucher/cancel` - Cancelar voucher
- `POST /voucher/resgate` - Resgatar voucher
- `GET /empresa/resumido` - Dados resumidos de empresa
- Endpoints Swagger UI (`/swagger-ui.html`, `/v3/api-docs`)

---

## 🗄️ Banco de Dados

### Configuração (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/voucherback
    username: root
    password: 0381
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update  # Evolui schema automaticamente
```

### Migrações (Flyway)
Migrações SQL ficam em `src/main/resources/db/migration/`  
Executam automaticamente na inicialização.

### Recriar Banco de Dados
```powershell
# 1. Deletar banco
mysql -u root -p0381 -e "DROP DATABASE voucherback;"

# 2. Criar novo
mysql -u root -p0381 -e "CREATE DATABASE voucherback CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 3. Iniciar aplicação (Flyway executará migrações automaticamente)
./mvnw spring-boot:run
```

---

## 🧪 Testes

### Estrutura de Testes
- **Framework**: JUnit 5 + Mockito 5.11.0
- **Builders**: Fixtures em `src/test/java/.../builders/`
- **Exemplo de Teste**:
```java
@ExtendWith(MockitoExtension.class)
class VoucherServiceImplTest {
    
    @Mock
    private IVoucherRepository voucherRepository;
    
    @InjectMocks
    private VoucherServiceImpl voucherService;
    
    @Test
    void deveEmitirVoucherComSucesso() {
        // Arrange
        var voucherRequest = umVoucherApiRequest().build();
        var voucherEntity = umVoucherEntity().build();
        when(voucherRepository.save(any())).thenReturn(voucherEntity);
        
        // Act
        var response = voucherService.emitindoVoucher(voucherRequest);
        
        // Assert
        assertThat(response).isNotNull();
        verify(voucherRepository, times(1)).save(any());
    }
}
```

### Rodar Testes
```powershell
# Todos os testes
./mvnw test

# Teste específico
./mvnw -Dtest=VoucherServiceImplTest test

# Teste específico + método
./mvnw -Dtest=VoucherServiceImplTest#deveEmitirVoucherComSucesso test

# Com relatório de cobertura (Jacoco)
./mvnw clean test jacoco:report
# Relatório em: target/site/jacoco/index.html
```

---

## 📊 Relatórios (JasperReports)

### Gerar Relatório
```bash
curl -X POST http://localhost:8080/voucher/report \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nomeRelatorio": "promocoes",
    "filtros": { ... }
  }'

# Resposta: Base64-encoded PDF
```

### Templates Disponíveis
Localizados em `src/main/resources/relatorios/`:
- `promocoes.jrxml`
- `lojas.jrxml`
- Adicione novos templates aqui

---

## 🔧 Desenvolvimento

### Padrões de Arquitetura

#### Façade + Camada de Serviço
```java
// VoucherBackFacade é o ponto único de orquestração
@Component
public class VoucherBackFacade {
    // Injeta 6 serviços de domínio
    private final IUsuarioService usuarioService;
    private final IVoucherService voucherService;
    // ... etc
}

// Serviços são package-private (acessados via Façade)
class UsuarioServiceImpl implements IUsuarioService {
    // Lógica de negócio
}
```

#### Mapeamento de DTOs (MapStruct)
```java
@Mapper(componentModel = "spring")
public interface VouckBackMapper {
    UsuarioApiResponse usuarioParaResponse(UsuarioEntity entity);
    UsuarioEntity usuarioRequestParaEntity(UsuarioApiRequest request);
    // ... etc
}
```

#### Persistência (JPA + Repositórios)
```java
public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {
    Optional<UsuarioEntity> findByEmail(String email);
    // Query customizada com Criteria
    List<UsuarioEntity> buscandoComFiltro(UsuarioCriteria criteria);
}
```

### Geração de Código

#### OpenAPI Generator
```powershell
# Regenerar a partir do swagger.yml
./mvnw generate-sources

# Saída em: target/generated-sources/openapi/
```

#### Atualizar Contrato de API
1. Edite `src/main/resources/swagger.yml`
2. Execute `./mvnw generate-sources`
3. Controladores + Modelos são regenerados automaticamente

---

## 📚 Documentação

Para documentação detalhada sobre arquitetura, padrões e convenções, veja:
- **[AGENTS.md](./AGENTS.md)** — Guia completo de desenvolvimento
- **[HELP.md](./HELP.md)** — Perguntas frequentes

---

## 🚢 Deploy

### Build para Produção
```powershell
./mvnw clean package -DskipTests
# JAR gerado em: target/voucherback-api-0.0.1-SNAPSHOT.jar
```

### Executar JAR
```powershell
java -jar target/voucherback-api-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://prod-db:3306/voucherback \
  --spring.datasource.username=prod_user \
  --spring.datasource.password=prod_password
```

---

## 🐛 Troubleshooting

### Erro: "Connection refused" no MySQL
```powershell
# Verificar se MySQL está rodando
Get-Service MySQL*

# Iniciar MySQL (Windows)
net start MySQL80
```

### Erro: Port 8080 já está em uso
```powershell
# Encontrar processo usando porta 8080
Get-NetTCPConnection -LocalPort 8080

# Encerrar processo ou usar porta diferente
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Erro: "UnnecessaryStubbingException" em testes
Remova mocks/stubs não utilizados nos testes or use:
```java
@ExtendWith(MockitoExtension.class)
class MeuTesteTest {
    // Isso força MockitoExtension a modo lenient (menos rigoroso)
}
```

---

## 📝 Contribuindo

1. Crie uma branch para sua feature: `git checkout -b feature/sua-feature`
2. Commit com mensagem clara: `git commit -m "Add: nova feature"`
3. Execute testes: `./mvnw test`
4. Push: `git push origin feature/sua-feature`
5. Abra um Pull Request

---

## 📄 Licença

Proprietary - Rematec Solutions

---

**Last Updated**: 2026-05-16  
**Versão Java**: 17  
**Spring Boot**: 3.2.4  
**Maven**: 3.8+

