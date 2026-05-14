# Guia de Implementação - Otimizações de Performance
## VoucherBack API - Implementação Prática

---

## 🚀 **1. Cache Redis - Implementação Passo a Passo**

### **Dependências Maven**
```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

### **Configuração Redis**
```yaml
# application.yml
spring:
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
      password: ${REDIS_PASSWORD:}
      timeout: 2000ms
      database: 0
  cache:
    type: redis
    redis:
      time-to-live: 3600000  # 1 hora default
      key-prefix: voucherback:
      use-key-prefix: true
      cache-null-values: false
```

### **Configuração Java**
```java
// src/main/java/com/rematec/voucher/voucherbackapi/config/RedisConfig.java
package com.rematec.voucher.voucherbackapi.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1))
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .withCacheConfiguration("promocoes",
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)))
            .withCacheConfiguration("perfis",
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(2)))
            .withCacheConfiguration("lojas",
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1)))
            .build();
    }
}
```

### **Implementação em Serviços**
```java
// VoucherServiceImpl.java - Adicionar cache
@Service
@CacheConfig(cacheNames = "vouchers")
public class VoucherServiceImpl implements IVoucherService {

    @Cacheable(value = "promocoes", key = "#consulta.filialCnpj + '_' + #consulta.valorCompra")
    @Override
    public ConsultaVoucherApiResponse consultandoPromocoes(ConsultaVoucherApiRequest consulta) {
        // Lógica existente mantida
    }

    @Cacheable(value = "vouchers", key = "#codigo")
    @Override
    public VoucherApiResponse buscarVoucherPorCodigo(String codigo) {
        // Método de exemplo
    }

    @CacheEvict(value = "vouchers", key = "#voucher.guid")
    @Override
    public void atualizarVoucher(VoucherApiRequest voucher) {
        // Invalida cache específico
    }

    @CacheEvict(value = "promocoes", allEntries = true)
    @Override
    public void limparCachePromocoes() {
        // Invalida todo cache de promoções
    }
}
```

---

## 🗄️ **2. Otimização do Pool de Conexões MySQL**

### **Configuração HikariCP**
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:voucherback}?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8
    username: ${DB_USER:root}
    password: ${DB_PASSWORD:0381}
    driver-class-name: com.mysql.cj.jdbc.Driver

    hikari:
      # Pool configuration
      maximum-pool-size: ${DB_MAX_POOL_SIZE:20}          # Aumentado de 10
      minimum-idle: ${DB_MIN_IDLE:5}                     # Reduzido para economizar recursos
      idle-timeout: ${DB_IDLE_TIMEOUT:300000}            # 5 minutos
      max-lifetime: ${DB_MAX_LIFETIME:1200000}           # 20 minutos
      connection-timeout: ${DB_CONNECTION_TIMEOUT:20000} # 20 segundos

      # Performance
      leak-detection-threshold: ${DB_LEAK_DETECTION:60000} # 60 segundos
      validation-timeout: 5000
      login-timeout: 5

      # Monitoring
      pool-name: VoucherBackPool
      register-mbeans: true

      # Connection testing
      connection-test-query: SELECT 1
```

### **Monitoramento do Pool**
```java
// HealthCheck personalizado
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Autowired
    private DataSource dataSource;

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;

            return Health.up()
                .withDetail("activeConnections", hikariDataSource.getHikariPoolMXBean().getActiveConnections())
                .withDetail("idleConnections", hikariDataSource.getHikariPoolMXBean().getIdleConnections())
                .withDetail("totalConnections", hikariDataSource.getHikariPoolMXBean().getTotalConnections())
                .withDetail("threadsAwaitingConnection", hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection())
                .build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
```

---

## 📊 **3. Spring Boot Actuator + Métricas**

### **Dependências**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

### **Configuração**
```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,heapdump,threaddump
      base-path: /actuator
  endpoint:
    health:
      show-details: when-authorized
    metrics:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true
        step: 10s
    tags:
      application: voucherback-api
      environment: ${APP_ENV:development}
  health:
    diskspace:
      enabled: true
    db:
      enabled: true
```

### **Métricas Customizadas**
```java
// src/main/java/com/rematec/voucher/voucherbackapi/config/MetricsConfig.java
package com.rematec.voucher.voucherbackapi.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Timer.Sample voucherCreationTimer(MeterRegistry registry) {
        return Timer.start(registry, "voucher.creation.time", "operation", "create");
    }

    @Bean
    public Counter voucherCreatedCounter(MeterRegistry registry) {
        return Counter.builder("voucher.created.total")
            .description("Total number of vouchers created")
            .register(registry);
    }

    @Bean
    public Counter voucherRedemptionCounter(MeterRegistry registry) {
        return Counter.builder("voucher.redemption.total")
            .description("Total number of voucher redemptions")
            .register(registry);
    }
}
```

### **Uso em Serviços**
```java
@Service
public class VoucherServiceImpl implements IVoucherService {

    private final Timer.Sample voucherTimer;
    private final Counter voucherCounter;

    public VoucherServiceImpl(Timer.Sample voucherTimer, Counter voucherCounter) {
        this.voucherTimer = voucherTimer;
        this.voucherCounter = voucherCounter;
    }

    @Override
    public VoucherApiResponse criarVoucher(VoucherApiRequest request) {
        return voucherTimer.recordCallable(() -> {
            VoucherApiResponse response = // lógica existente
            voucherCounter.increment();
            return response;
        });
    }
}
```

---

## 🏗️ **4. Indexação Estratégica**

### **Script Flyway**
```sql
-- src/main/resources/db/migration/V03__performance_indexes.sql
-- Índices para otimização de performance

-- Voucher: Consultas por CPF + Status (operações mais frequentes)
CREATE INDEX idx_voucher_cpf_status_date ON voucher(cliente_cpf, voucher_status, data_cadastro DESC);

-- Voucher: Busca por código (resgate de vouchers)
CREATE INDEX idx_voucher_codigo_status ON voucher(codigo, voucher_status);

-- Voucher: Consultas por promoção (relatórios)
CREATE INDEX idx_voucher_promocao_status ON voucher(promocao_guid, voucher_status);

-- Promoção: Busca de promoções ativas por período e valor
CREATE INDEX idx_promocao_status_periodo_valor ON promocao(promocao_status, inicio, fim, valor_minimo_para_disparo);

-- Loja: Busca por CNPJ + Status
CREATE INDEX idx_loja_cnpj_status ON loja(cnpj, status);

-- Usuário: Login e autenticação
CREATE INDEX idx_usuario_email_status ON usuario(email, status);

-- Empresa: Consultas por CNPJ
CREATE INDEX idx_empresa_cnpj ON empresa(cnpj);

-- Índices compostos para queries complexas
CREATE INDEX idx_voucher_composite_search ON voucher(
    cliente_cpf,
    filial_cnpj,
    voucher_status,
    data_cadastro DESC,
    promocao_guid
);

-- Análise de uso dos índices (executar após deploy)
-- SELECT * FROM performance_schema.table_io_waits_summary_by_index_usage
-- WHERE object_schema = 'voucherback' AND object_name = 'voucher';
```

### **Validação dos Índices**
```sql
-- Query para verificar uso dos índices
SELECT
    TABLE_NAME,
    INDEX_NAME,
    CARDINALITY,
    PAGES,
    FILTER_CONDITION
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'voucherback'
ORDER BY TABLE_NAME, SEQ_IN_INDEX;

-- Análise de queries lentas
EXPLAIN FORMAT=JSON
SELECT v.* FROM voucher v
WHERE v.cliente_cpf = '12345678901'
  AND v.voucher_status = 'CONFIRMADO'
  AND v.data_cadastro >= '2024-01-01'
ORDER BY v.data_cadastro DESC
LIMIT 50;
```

---

## ⚡ **5. Configuração Avançada de Async**

### **Configuração de Executors**
```java
// src/main/java/com/rematec/voucher/voucherbackapi/config/AsyncConfig.java
package com.rematec.voucher.voucherbackapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "voucherExecutor")
    public Executor voucherExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Configuração otimizada para operações de voucher
        executor.setCorePoolSize(4);           // Threads sempre ativas
        executor.setMaxPoolSize(8);            // Máximo de threads
        executor.setQueueCapacity(100);        // Capacidade da fila
        executor.setThreadNamePrefix("VoucherAsync-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // Configurações adicionais
        executor.setAllowCoreThreadTimeOut(false);
        executor.setKeepAliveSeconds(60);
        executor.setAwaitTerminationSeconds(30);

        executor.initialize();
        return executor;
    }

    @Bean(name = "reportExecutor")
    public Executor reportExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Configuração para relatórios (operações mais pesadas)
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("ReportAsync-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }

    @Bean(name = "notificationExecutor")
    public Executor notificationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Configuração para notificações (baixa prioridade)
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("NotificationAsync-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

        executor.initialize();
        return executor;
    }
}
```

### **Uso nos Serviços**
```java
@Service
public class VoucherServiceImpl implements IVoucherService {

    @Async("voucherExecutor")
    @Override
    public void confirmandoVoucher(List<VoucherApiRequest> voucherApiRequest) {
        // Operação assíncrona de confirmação
    }

    @Async("voucherExecutor")
    @Override
    public void cancelandoVoucher(List<VoucherApiRequest> voucherApiRequest) {
        // Operação assíncrona de cancelamento
    }
}

@Service
public class ReportService {

    @Async("reportExecutor")
    public CompletableFuture<byte[]> gerarRelatorioAsync(String tipo, Map<String, Object> parametros) {
        // Geração assíncrona de relatórios
        return CompletableFuture.completedFuture(gerarRelatorio(tipo, parametros));
    }
}
```

---

## 🧠 **6. JVM Tuning para Produção**

### **Script de Inicialização**
```bash
#!/bin/bash
# startup.sh

JAVA_OPTS="
  -server
  -Xms2g
  -Xmx4g
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=200
  -XX:G1HeapRegionSize=16m
  -XX:+PrintGCDetails
  -XX:+PrintGCTimeStamps
  -XX:+PrintGCApplicationStoppedTime
  -XX:+UseGCLogFileRotation
  -XX:NumberOfGCLogFiles=5
  -XX:GCLogFileSize=10M
  -Xloggc:/var/log/voucherback/gc.log
  -XX:+HeapDumpOnOutOfMemoryError
  -XX:HeapDumpPath=/var/log/voucherback/
  -XX:+DisableExplicitGC
  -XX:+UseCompressedOops
  -Djava.security.egd=file:/dev/./urandom
  -Dspring.profiles.active=production
"

exec java $JAVA_OPTS -jar voucherback-api.jar
```

### **Configuração por Ambiente**
```yaml
# application-production.yml
spring:
  profiles: production

  datasource:
    hikari:
      maximum-pool-size: 50
      minimum-idle: 10

  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate

logging:
  level:
    com.rematec.voucher: INFO
    org.hibernate.SQL: WARN
    org.hibernate.type.descriptor.sql.BasicBinder: WARN
  file:
    name: /var/log/voucherback/application.log
    max-size: 10MB
    max-history: 30

management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
```

---

## 🔍 **7. Monitoramento com Prometheus + Grafana**

### **Configuração Prometheus**
```yaml
# monitoring/prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

rule_files:
  - "alert_rules.yml"

alerting:
  alertmanagers:
    - static_configs:
        - targets:
          - alertmanager:9093

scrape_configs:
  - job_name: 'voucherback-api'
    static_configs:
      - targets: ['localhost:8080']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 10s

  - job_name: 'mysql'
    static_configs:
      - targets: ['localhost:3306']
    scrape_interval: 30s
```

### **Regras de Alerta**
```yaml
# monitoring/alert_rules.yml
groups:
  - name: voucherback_alerts
    rules:
      - alert: HighResponseTime
        expr: histogram_quantile(0.95, rate(http_server_requests_duration_seconds_bucket{method="POST"}[5m])) > 2
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "High response time detected"
          description: "95th percentile response time > 2s for POST requests"

      - alert: HighErrorRate
        expr: rate(http_server_requests_total{status=~"5.."}[5m]) / rate(http_server_requests_total[5m]) > 0.05
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "High error rate detected"
          description: "Error rate > 5% for 2 minutes"

      - alert: HighDBConnections
        expr: hikaricp_connections_active > 40
        for: 3m
        labels:
          severity: warning
        annotations:
          summary: "High database connections"
          description: "Active DB connections > 40 for 3 minutes"

      - alert: HighMemoryUsage
        expr: jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"} > 0.9
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "High memory usage"
          description: "Heap memory usage > 90% for 5 minutes"
```

### **Dashboard Grafana Básico**
```json
{
  "dashboard": {
    "title": "VoucherBack API Performance",
    "panels": [
      {
        "title": "Response Time",
        "type": "graph",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, rate(http_server_requests_duration_seconds_bucket[5m]))",
            "legendFormat": "P95 Response Time"
          }
        ]
      },
      {
        "title": "Database Connections",
        "type": "graph",
        "targets": [
          {
            "expr": "hikaricp_connections_active",
            "legendFormat": "Active Connections"
          }
        ]
      },
      {
        "title": "Cache Hit Rate",
        "type": "graph",
        "targets": [
          {
            "expr": "cache_gets_total - cache_gets_misses_total) / cache_gets_total",
            "legendFormat": "Cache Hit Rate"
          }
        ]
      }
    ]
  }
}
```

---

## 🐳 **8. Docker Compose para Desenvolvimento**

### **docker-compose.yml**
```yaml
version: '3.8'

services:
  redis:
    image: redis:7-alpine
    container_name: voucherback-redis
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    command: redis-server --appendonly yes --requirepass ${REDIS_PASSWORD:voucherback}
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 30s
      timeout: 10s
      retries: 3

  mysql:
    image: mysql:8.0
    container_name: voucherback-mysql
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD:0381}
      MYSQL_DATABASE: voucherback
      MYSQL_USER: voucherback
      MYSQL_PASSWORD: voucherback
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./db/init:/docker-entrypoint-initdb.d
      - ./db/migrations:/migrations
    command: --default-authentication-plugin=mysql_native_password
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 30s
      timeout: 10s
      retries: 3

  prometheus:
    image: prom/prometheus:latest
    container_name: voucherback-prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./monitoring/prometheus.yml:/etc/prometheus/prometheus.yml
      - prometheus_data:/prometheus
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--web.console.libraries=/etc/prometheus/console_libraries'
      - '--web.console.templates=/etc/prometheus/consoles'

  grafana:
    image: grafana/grafana:latest
    container_name: voucherback-grafana
    environment:
      GF_SECURITY_ADMIN_PASSWORD: ${GRAFANA_PASSWORD:admin}
    ports:
      - "3000:3000"
    volumes:
      - grafana_data:/var/lib/grafana
      - ./monitoring/grafana/provisioning:/etc/grafana/provisioning
    depends_on:
      - prometheus

  app:
    build: .
    container_name: voucherback-api
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: docker
      REDIS_HOST: redis
      DB_HOST: mysql
      DB_USER: voucherback
      DB_PASSWORD: voucherback
    depends_on:
      redis:
        condition: service_healthy
      mysql:
        condition: service_healthy
    volumes:
      - ./logs:/app/logs

volumes:
  redis_data:
  mysql_data:
  prometheus_data:
  grafana_data:
```

### **Variáveis de Ambiente**
```bash
# .env
REDIS_PASSWORD=voucherback
DB_PASSWORD=0381
GRAFANA_PASSWORD=admin
APP_ENV=development
```

---

## ✅ **Checklist de Implementação**

### **Fase 1: Infraestrutura Básica**
- [ ] Adicionar dependências Redis e Actuator
- [ ] Configurar Redis no docker-compose
- [ ] Atualizar application.yml com configurações otimizadas
- [ ] Criar RedisConfig.java
- [ ] Testar conectividade Redis

### **Fase 2: Cache Implementation**
- [ ] Adicionar @Cacheable nas consultas frequentes
- [ ] Implementar @CacheEvict para invalidação
- [ ] Configurar TTLs apropriados por entidade
- [ ] Testar comportamento do cache

### **Fase 3: Database Optimization**
- [ ] Otimizar configurações HikariCP
- [ ] Criar script Flyway com índices
- [ ] Executar análise EXPLAIN nas queries principais
- [ ] Validar melhoria de performance

### **Fase 4: Monitoring**
- [ ] Configurar endpoints Actuator
- [ ] Adicionar métricas customizadas
- [ ] Configurar Prometheus
- [ ] Criar dashboard básico Grafana

### **Fase 5: Production Tuning**
- [ ] JVM tuning para produção
- [ ] Configuração async otimizada
- [ ] Compressão HTTP
- [ ] Testes de carga

---

## 📈 **Validação e Testes**

### **Testes de Performance**
```bash
# JMeter test script
# Simular 500 usuários simultâneos
jmeter -n -t voucherback-performance-test.jmx -l results.jtl

# Resultados esperados:
# - Throughput: > 1000 req/s
# - Response Time P95: < 500ms
# - Error Rate: < 1%
```

### **Queries de Validação**
```sql
-- Verificar uso dos índices
SELECT
    object_name,
    index_name,
    count_read,
    count_fetch,
    count_insert,
    count_update,
    count_delete
FROM performance_schema.table_io_waits_summary_by_index_usage
WHERE object_schema = 'voucherback'
ORDER BY count_read DESC;

-- Verificar conexões ativas
SHOW PROCESSLIST;

-- Verificar cache Redis
redis-cli keys "voucherback:*"
redis-cli info stats
```

---

**Este guia fornece implementação prática e completa para todas as otimizações de performance identificadas. Comece pela Fase 1 e valide cada mudança antes de prosseguir.**</content>
<parameter name="filePath">D:\rematec\workspace\refactory\voucherback-api\PERFORMANCE_IMPLEMENTATION_GUIDE.md
