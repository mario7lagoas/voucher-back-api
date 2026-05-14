-- Flyway migration: V2__add_performance_indexes.sql
CREATE INDEX idx_voucher_status_date ON voucher(voucher_status, data_cadastro);
CREATE INDEX idx_voucher_cpf_status ON voucher(cliente_cpf, voucher_status);
CREATE INDEX idx_promocao_status_dates ON promocao(promocao_status, inicio, fim);
CREATE INDEX idx_loja_cnpj_status ON loja(cnpj, status);
CREATE INDEX idx_usuario_email_status ON usuario(email, status);

-- V03__performance_indexes.sql
CREATE INDEX idx_voucher_composite_search ON voucher(cliente_cpf, voucher_status, promocao_guid, data_cadastro DESC);
