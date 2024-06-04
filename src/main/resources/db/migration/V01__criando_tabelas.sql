CREATE TABLE empresa (
	id BIGINT NOT NULL AUTO_INCREMENT,
	data_atualizacao DATETIME(6),
	data_cadastro DATETIME(6),
	guid VARCHAR(255),
	identificacao VARCHAR(255),
	nome VARCHAR(255),
	status BIT,
	PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE TABLE loja (
	id BIGINT NOT NULL AUTO_INCREMENT,
	data_atualizacao DATETIME(6),
	data_cadastro DATETIME(6),
	guid VARCHAR(255),
	cnpj VARCHAR(255),
	identificacao VARCHAR(255),
	nome VARCHAR(255),
	status BIT,
	PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE TABLE perfil (
	id BIGINT NOT NULL AUTO_INCREMENT,
	data_atualizacao DATETIME(6),
	data_cadastro DATETIME(6),
	guid VARCHAR(255),
	nome VARCHAR(255),
	PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE TABLE perfil_roles (
	perfil_id BIGINT NOT NULL,
	role_id BIGINT NOT NULL
)ENGINE=InnoDB;

CREATE TABLE promocao (
	id BIGINT NOT NULL AUTO_INCREMENT,
	data_atualizacao DATETIME(6),
	data_cadastro DATETIME(6),
	guid VARCHAR(255),
	autor_alteracao VARCHAR(255),
	descricao VARCHAR(255),
	dias_validade_voucher INTEGER,
	desconto_percentual DECIMAL(38,2),
	desconto_valor DECIMAL(38,2),
	fim DATETIME(6),
	inicio DATETIME(6),
	promocao_status ENUM ('ATIVA','PROGRESSO','BLOQUEADA','FINALIZADA'),
	tipo_desconto ENUM ('VALOR','PERCENTUAL'),
	valor_maximo_desconto DECIMAL(38,2),
	valor_minimo_para_disparo DECIMAL(38,2),
	PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE TABLE promocoes_lojas (
	promocao_id BIGINT NOT NULL,
	loja_id BIGINT NOT NULL
)ENGINE=InnoDB;

CREATE TABLE role (
	id BIGINT NOT NULL AUTO_INCREMENT,
	nome ENUM ('MENU_LOJA','MENU_USUARIO','MENU_PROMOCAO','MENU_PERFIL','MENU_VOUCHER','BUSCAR_LOJA','ALTERAR_LOJA',
	'CADASTRAR_LOJA','APAGAR_LOJA','BUSCAR_USUARIO','ALTERAR_USUARIO','CADASTRAR_USUARIO','APAGAR_USUARIO',
	'BUSCAR_PROMOCAO','ALTERAR_PROMOCAO','CADASTRAR_PROMOCAO','APAGAR_PROMOCAO','BUSCAR_PERFIL','ALTERAR_PERFIL',
	'CADASTRAR_PERFIL','APAGAR_PERFIL','BUSCAR_VOUCHER','ALTERAR_VOUCHER','CADASTRAR_VOUCHER','APAGAR_VOUCHER',
	'BUSCAR_EMPRESA','ALTERAR_EMPRESA','CADASTRAR_EMPRESA','APAGAR_EMPRESA'),
	PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE TABLE usuario (
	id BIGINT NOT NULL AUTO_INCREMENT,
	data_atualizacao DATETIME(6),
	data_cadastro DATETIME(6),
	guid VARCHAR(255),
	email VARCHAR(50),
	password VARCHAR(150),
	status BIT,
	user_name VARCHAR(255),
	empresa_id BIGINT,
	PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE TABLE usuario_perfil (
	usuario_id BIGINT NOT NULL,
	perfil_id BIGINT NOT NULL,
	PRIMARY KEY (usuario_id, perfil_id)
)ENGINE=InnoDB;

CREATE TABLE usuario_loja (
	usuario_id BIGINT NOT NULL,
	loja_id BIGINT NOT NULL,
	PRIMARY KEY (usuario_id, loja_id)
)ENGINE=InnoDB;

CREATE TABLE voucher (
	id BIGINT NOT NULL AUTO_INCREMENT,
	data_atualizacao DATETIME(6),
	data_cadastro DATETIME(6),
	guid VARCHAR(255),
	cliente_cpf VARCHAR(255),
	codigo VARCHAR(255),
	cupom VARCHAR(255),
	cupom_resgate VARCHAR(255),
	data_resgate DATETIME(6),
	descricao VARCHAR(255),
	dias_validade_voucher INTEGER,
	filial_cnpj VARCHAR(255),
	filial_cnpj_resgate VARCHAR(255),
	fim DATETIME(6),
	fim_resgate DATETIME(6),
	inicio DATETIME(6),
	pdv VARCHAR(255),
	pdv_resgate VARCHAR(255),
	promocao_guid VARCHAR(255),
	promocao_status ENUM ('DISPONIVEL','EM_USO','UTILIZADO','CANCELADO','EXPIRADO'),
	tipo_desconto ENUM ('VALOR','PERCENTUAL'),
	valor_desconto DECIMAL(38,2),
	valor_maximo_desconto DECIMAL(38,2),
	valor_pagamento DECIMAL(38,2),
	valor_pago DECIMAL(38,2),
	voucher_status ENUM ('DISPONIBILIZADO','CANCELADO','CONFIRMADO','UTILIZADO','EXPIRADO'),
	PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE INDEX IDX_GUID_EMPRESA ON empresa (guid);

CREATE INDEX IDX_IDENTIFICACAO_EMPRESA ON empresa (identificacao);

CREATE INDEX IDX_STATUS_EMPRESA ON empresa (status);

CREATE INDEX IDX_GUID_LOJA ON loja (guid);

CREATE INDEX IDX_CNPJ_LOJA ON loja (cnpj);

CREATE INDEX IDX_STATUS_LOJA ON loja (status);

CREATE INDEX IDX_GUID_PERFIL ON perfil (guid);

CREATE INDEX IDX_GUID_PROMO  ON promocao (guid);

CREATE INDEX IDX_INICIO_PROMO ON promocao (inicio);

CREATE INDEX IDX_FIM_PROMO  ON promocao (fim);

CREATE INDEX IDX_VLR_MIN_PROMO  ON promocao (valor_minimo_para_disparo);

CREATE INDEX IDX_GUID_USU ON usuario (guid);

CREATE INDEX IDX_GUID_VOUCHER  ON voucher (guid);

CREATE INDEX IDX_COD_VOUCHER ON voucher (codigo);

CREATE INDEX IDX_CNPJ_VOUCHER ON voucher (filial_cnpj);

CREATE INDEX IDX_CPF_VOUCHER ON voucher (cliente_cpf);

CREATE INDEX IDX_PROMO_GUID_VOUCHER ON voucher (promocao_guid);

ALTER TABLE loja ADD CONSTRAINT UK_LojaCnpj UNIQUE (cnpj);

ALTER TABLE usuario  ADD CONSTRAINT UK_UsuarioEmail UNIQUE (email);

ALTER TABLE promocoes_lojas ADD CONSTRAINT UKiqrxcawavvo1rybihlapphm28 UNIQUE (promocao_id, loja_id);

ALTER TABLE perfil_roles ADD CONSTRAINT UKmvyn5fhyitlav9mbkxp4pauqu UNIQUE (perfil_id, role_id);

ALTER TABLE usuario_loja ADD CONSTRAINT UK52bbm870o4b1n3aiqvw5p1fcv unique (usuario_id, loja_id);

ALTER TABLE perfil_roles  ADD CONSTRAINT FK_PerilRolesAndRoleId FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE perfil_roles ADD CONSTRAINT FK_PerfilRolesAndPerfilId FOREIGN KEY (perfil_id) REFERENCES perfil (id);

ALTER TABLE promocoes_lojas ADD CONSTRAINT FK_PromocoesLojasAndLojaId FOREIGN KEY (loja_id) REFERENCES loja (id);

ALTER TABLE promocoes_lojas ADD CONSTRAINT FK_PromocoesLojasAndPromocaoId FOREIGN KEY (promocao_id) REFERENCES promocao (id);

ALTER TABLE usuario_perfil ADD CONSTRAINT FK_UsuarioPerfilAndPerfilId FOREIGN KEY (perfil_id) REFERENCES perfil (id);

ALTER TABLE usuario_perfil ADD CONSTRAINT FK_UsuarioPerfilAndUsuarioId FOREIGN KEY (usuario_id) REFERENCES usuario (id);

ALTER TABLE usuario_loja ADD CONSTRAINT FK_UsuarioLojaAndLojaId FOREIGN KEY (loja_id) REFERENCES loja (id);

ALTER TABLE usuario_loja ADD CONSTRAINT FK_UsuarioLojaAndUsuarioId FOREIGN KEY (usuario_id) REFERENCES usuario (id);

ALTER TABLE usuario ADD CONSTRAINT FK_UsuarioAndEmpresaId FOREIGN KEY (empresa_id) REFERENCES empresa (id);