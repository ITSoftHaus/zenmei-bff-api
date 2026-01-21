# 🗄️ SCRIPT DE MIGRAÇÃO DO BANCO DE DADOS

## User → MEI Migration Script

**ATENÇÃO**: Execute este script com cuidado em ambiente de produção!

---

## 📋 PRÉ-REQUISITOS

1. ✅ Fazer backup completo do banco de dados
2. ✅ Testar o script em ambiente de desenvolvimento
3. ✅ Verificar se não há aplicações rodando
4. ✅ Ter permissões adequadas no PostgreSQL

---

## 🔍 VERIFICAÇÃO ANTES DA MIGRAÇÃO

```sql
-- Verificar tabela atual
SELECT table_name, table_type 
FROM information_schema.tables 
WHERE table_schema = 'public' 
AND table_name = 'users';

-- Contar registros
SELECT COUNT(*) as total_users FROM users;

-- Verificar constraints
SELECT constraint_name, constraint_type 
FROM information_schema.table_constraints 
WHERE table_name = 'users';

-- Verificar índices
SELECT indexname, indexdef 
FROM pg_indexes 
WHERE tablename = 'users';

-- Verificar foreign keys que referenciam users
SELECT
    tc.table_name, 
    kcu.column_name,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name 
FROM information_schema.table_constraints AS tc 
JOIN information_schema.key_column_usage AS kcu
  ON tc.constraint_name = kcu.constraint_name
  AND tc.table_schema = kcu.table_schema
JOIN information_schema.constraint_column_usage AS ccu
  ON ccu.constraint_name = tc.constraint_name
  AND ccu.table_schema = tc.table_schema
WHERE tc.constraint_type = 'FOREIGN KEY' 
AND ccu.table_name = 'users';
```

---

## 💾 BACKUP

```sql
-- Criar backup da tabela users
CREATE TABLE users_backup AS SELECT * FROM users;

-- Verificar backup
SELECT COUNT(*) FROM users_backup;
```

---

## 🔄 MIGRAÇÃO

### Opção 1: Renomear Tabela (Recomendado)

```sql
-- ============================================
-- INÍCIO DA TRANSAÇÃO
-- ============================================
BEGIN;

-- 1. Renomear a tabela
ALTER TABLE users RENAME TO meis;

-- 2. Renomear sequence (se existir)
ALTER SEQUENCE IF EXISTS users_id_seq RENAME TO meis_id_seq;

-- 3. Renomear constraints (ajustar nomes conforme seu schema)
-- Primary Key
ALTER TABLE meis RENAME CONSTRAINT users_pkey TO meis_pkey;

-- Unique Constraints
ALTER TABLE meis RENAME CONSTRAINT users_email_key TO meis_email_key;
ALTER TABLE meis RENAME CONSTRAINT users_cnpj_key TO meis_cnpj_key;
ALTER TABLE meis RENAME CONSTRAINT users_cpf_key TO meis_cpf_key;

-- 4. Renomear índices (se houver índices personalizados)
-- ALTER INDEX idx_users_email RENAME TO idx_meis_email;
-- ALTER INDEX idx_users_cnpj RENAME TO idx_meis_cnpj;

-- 5. Verificar resultado
SELECT 'meis' as table_name, COUNT(*) as total_records FROM meis;

-- Se tudo estiver correto, commitar
COMMIT;

-- ============================================
-- FIM DA TRANSAÇÃO
-- ============================================

-- Se algo der errado, executar:
-- ROLLBACK;
```

### Opção 2: Criar Nova Tabela e Migrar Dados

```sql
-- ============================================
-- CRIAR NOVA TABELA MEIS
-- ============================================
BEGIN;

-- 1. Criar nova tabela com estrutura idêntica
CREATE TABLE meis (LIKE users INCLUDING ALL);

-- 2. Copiar todos os dados
INSERT INTO meis SELECT * FROM users;

-- 3. Verificar contagem
DO $$
DECLARE
    users_count INTEGER;
    meis_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO users_count FROM users;
    SELECT COUNT(*) INTO meis_count FROM meis;
    
    IF users_count != meis_count THEN
        RAISE EXCEPTION 'Contagem de registros não confere! users: %, meis: %', 
                        users_count, meis_count;
    END IF;
    
    RAISE NOTICE 'Migração bem-sucedida! % registros copiados', meis_count;
END $$;

-- 4. Renomear tabela antiga (manter como backup)
ALTER TABLE users RENAME TO users_old;

COMMIT;
```

---

## 🔗 ATUALIZAR FOREIGN KEYS

Se outras tabelas referenciam a tabela users, você precisa atualizar as foreign keys:

```sql
-- Exemplo: Se a tabela 'nota_fiscal' tem FK para users
BEGIN;

-- 1. Remover constraint antiga
ALTER TABLE nota_fiscal 
DROP CONSTRAINT IF EXISTS fk_nota_fiscal_user_id;

-- 2. Adicionar nova constraint
ALTER TABLE nota_fiscal 
ADD CONSTRAINT fk_nota_fiscal_mei_id 
FOREIGN KEY (user_id) REFERENCES meis(id);

-- Opcionalmente, renomear a coluna
-- ALTER TABLE nota_fiscal RENAME COLUMN user_id TO mei_id;

COMMIT;
```

### Script Genérico para Todas as Foreign Keys

```sql
-- Listar todas as foreign keys que referenciam users
SELECT 
    tc.table_name,
    tc.constraint_name,
    kcu.column_name
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu 
    ON tc.constraint_name = kcu.constraint_name
JOIN information_schema.constraint_column_usage ccu 
    ON ccu.constraint_name = tc.constraint_name
WHERE tc.constraint_type = 'FOREIGN KEY' 
    AND ccu.table_name = 'users';

-- Para cada resultado acima, executar:
-- ALTER TABLE <table_name> DROP CONSTRAINT <constraint_name>;
-- ALTER TABLE <table_name> ADD CONSTRAINT <new_constraint_name> 
--     FOREIGN KEY (<column_name>) REFERENCES meis(id);
```

---

## ✅ VERIFICAÇÃO PÓS-MIGRAÇÃO

```sql
-- 1. Verificar se a tabela meis existe
SELECT table_name FROM information_schema.tables 
WHERE table_name = 'meis';

-- 2. Verificar contagem de registros
SELECT COUNT(*) FROM meis;

-- 3. Verificar alguns registros
SELECT id, nome_completo, email, cnpj 
FROM meis 
LIMIT 10;

-- 4. Verificar constraints
SELECT constraint_name, constraint_type 
FROM information_schema.table_constraints 
WHERE table_name = 'meis';

-- 5. Verificar foreign keys atualizadas
SELECT
    tc.table_name, 
    kcu.column_name,
    ccu.table_name AS foreign_table_name
FROM information_schema.table_constraints AS tc 
JOIN information_schema.key_column_usage AS kcu
    ON tc.constraint_name = kcu.constraint_name
JOIN information_schema.constraint_column_usage AS ccu
    ON ccu.constraint_name = tc.constraint_name
WHERE tc.constraint_type = 'FOREIGN KEY' 
    AND ccu.table_name = 'meis';
```

---

## 🧪 TESTAR APLICAÇÃO

Após a migração, testar:

1. ✅ Listar MEIs
2. ✅ Buscar MEI por ID
3. ✅ Criar novo MEI
4. ✅ Atualizar MEI
5. ✅ Deletar MEI
6. ✅ Operações que envolvem FK (ex: criar nota fiscal)

---

## 🔙 ROLLBACK (Se Necessário)

### Se usou Opção 1 (Renomear)

```sql
BEGIN;

-- Reverter renomeação
ALTER TABLE meis RENAME TO users;
ALTER SEQUENCE IF EXISTS meis_id_seq RENAME TO users_id_seq;

-- Reverter constraints
ALTER TABLE users RENAME CONSTRAINT meis_pkey TO users_pkey;
ALTER TABLE users RENAME CONSTRAINT meis_email_key TO users_email_key;
ALTER TABLE users RENAME CONSTRAINT meis_cnpj_key TO users_cnpj_key;

COMMIT;
```

### Se usou Opção 2 (Nova Tabela)

```sql
BEGIN;

-- Dropar nova tabela
DROP TABLE IF EXISTS meis;

-- Restaurar tabela original
ALTER TABLE users_old RENAME TO users;

COMMIT;
```

---

## 📊 MONITORAMENTO

Após a migração, monitorar:

- **Logs da aplicação**: Verificar erros relacionados a "users"
- **Performance**: Verificar se queries estão usando índices corretos
- **Integridade**: Verificar se foreign keys estão funcionando

---

## ⚠️ AVISOS IMPORTANTES

1. 🚨 **SEMPRE faça backup antes de qualquer migração**
2. 🚨 **Teste em desenvolvimento primeiro**
3. 🚨 **Execute em janela de manutenção**
4. 🚨 **Tenha plano de rollback pronto**
5. 🚨 **Monitore a aplicação após a migração**

---

## 📝 CHECKLIST DE EXECUÇÃO

### Pré-Migração
- [ ] Backup completo do banco de dados
- [ ] Aplicações paradas ou em modo manutenção
- [ ] Script de migração testado em dev
- [ ] Plano de rollback documentado
- [ ] Equipe avisada

### Durante a Migração
- [ ] Executar script em transação
- [ ] Verificar contagem de registros
- [ ] Atualizar foreign keys
- [ ] Verificar constraints e índices
- [ ] Commitar se tudo OK

### Pós-Migração
- [ ] Verificar estrutura da tabela meis
- [ ] Testar operações CRUD
- [ ] Verificar logs da aplicação
- [ ] Monitorar performance
- [ ] Documentar resultado

---

## 🎯 EXEMPLO COMPLETO - PRODUÇÃO

```sql
-- ============================================
-- MIGRAÇÃO COMPLETA - PRODUÇÃO
-- Execute este script completo em uma transação
-- ============================================

-- 1. BACKUP
CREATE TABLE users_backup_20260121 AS SELECT * FROM users;

-- 2. INÍCIO DA MIGRAÇÃO
BEGIN;

    -- 2.1 Renomear tabela
    ALTER TABLE users RENAME TO meis;
    
    -- 2.2 Renomear sequence
    ALTER SEQUENCE IF EXISTS users_id_seq RENAME TO meis_id_seq;
    
    -- 2.3 Renomear constraints
    ALTER TABLE meis RENAME CONSTRAINT users_pkey TO meis_pkey;
    ALTER TABLE meis RENAME CONSTRAINT users_email_key TO meis_email_key;
    ALTER TABLE meis RENAME CONSTRAINT users_cnpj_key TO meis_cnpj_key;
    
    -- 2.4 Verificar resultado
    DO $$
    DECLARE
        backup_count INTEGER;
        meis_count INTEGER;
    BEGIN
        SELECT COUNT(*) INTO backup_count FROM users_backup_20260121;
        SELECT COUNT(*) INTO meis_count FROM meis;
        
        IF backup_count != meis_count THEN
            RAISE EXCEPTION 'ERRO: Contagem não confere! backup: %, meis: %', 
                            backup_count, meis_count;
        END IF;
        
        RAISE NOTICE 'Sucesso! % registros migrados', meis_count;
    END $$;

-- 3. COMMIT (se tudo OK)
COMMIT;

-- Se houver erro, a transação fará rollback automático

-- 4. Verificação final
SELECT 'Migração concluída!' as status,
       (SELECT COUNT(*) FROM meis) as total_meis,
       (SELECT COUNT(*) FROM users_backup_20260121) as total_backup;
```

---

**Preparado por**: ZenMEI Development Team  
**Data**: 21 de Janeiro de 2026  
**Versão**: 1.0.0

---

**🔒 IMPORTANTE: Teste este script em desenvolvimento antes de executar em produção!**
