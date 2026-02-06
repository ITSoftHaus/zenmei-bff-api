# 🔐 GUIA COMPLETO - Como Usar o .env Local

**Status:** ✅ `.env` já configurado parcialmente!

---

## ✅ O QUE JÁ ESTÁ CONFIGURADO

Acabei de configurar o `.env` com as credenciais do Firebase que já estão no seu projeto:

```bash
✅ VITE_FIREBASE_API_KEY
✅ VITE_FIREBASE_AUTH_DOMAIN
✅ VITE_FIREBASE_PROJECT_ID
✅ VITE_FIREBASE_STORAGE_BUCKET
✅ VITE_FIREBASE_MESSAGING_SENDER_ID
✅ VITE_FIREBASE_APP_ID
✅ VITE_FIREBASE_MEASUREMENT_ID
✅ POSTGRES_DB
✅ POSTGRES_USER
✅ POSTGRES_PASSWORD
```

---

## ⚠️ O QUE AINDA FALTA

Você precisa adicionar apenas **1 coisa**:

### Firebase Service Account (Backend)

**O que é:** Credenciais para o backend validar os tokens JWT do Firebase.

---

## 🚀 COMO CONFIGURAR (2 MINUTOS)

### Opção 1: Automática (Recomendado)

```bash
# 1. Baixe o service account do Firebase Console
# URL: https://console.firebase.google.com/project/zenmei-app-8a181/settings/serviceaccounts/adminsdk

# 2. Clique em "Generate new private key" e baixe o arquivo

# 3. Execute (substitua pelo caminho do seu arquivo):
export FIREBASE_CREDS=$(cat ~/Downloads/zenmei-app-8a181-firebase-adminsdk.json | tr -d '\n')
sed -i "s|FIREBASE_CREDENTIALS_JSON=.*|FIREBASE_CREDENTIALS_JSON='${FIREBASE_CREDS}'|" .env

# 4. Pronto! Verifique:
./setup-env.sh
```

### Opção 2: Manual

```bash
# 1. Baixe o service account (mesmo link acima)

# 2. Abra o arquivo JSON baixado e copie TODO o conteúdo

# 3. Abra o .env:
nano .env

# 4. Encontre a linha:
FIREBASE_CREDENTIALS_JSON=

# 5. Cole o JSON em UMA LINHA SÓ entre aspas simples:
FIREBASE_CREDENTIALS_JSON='{"type":"service_account","project_id":"zenmei-app-8a181",...}'

# 6. Salve (Ctrl+O, Enter, Ctrl+X)
```

---

## 📋 CHECKLIST RÁPIDO

Execute este comando para ver o status:

```bash
./setup-env.sh
```

**Output esperado:**
```
✅ Firebase Frontend: Configurado
❌ Firebase Backend: NÃO CONFIGURADO  ← Você precisa configurar isso
```

**Depois de configurar:**
```
✅ Firebase Frontend: Configurado
✅ Firebase Backend: Configurado  ← Perfeito!
```

---

## 🚀 COMO USAR O .ENV

### 1. Com Docker Compose (Recomendado)

O `.env` é lido **automaticamente** pelo Docker Compose:

```bash
# Start tudo
./start.sh

# Ou manualmente
docker-compose up -d
```

As variáveis serão injetadas nos containers automaticamente! ✨

### 2. Desenvolvimento Local (sem Docker)

#### Backend (Spring Boot):

```bash
# O Spring Boot lê .env automaticamente via dotenv-java
# Ou você pode exportar manualmente:
export $(cat .env | grep -v '^#' | xargs)

cd zenmei-mei-api
mvn spring-boot:run
```

#### Frontend (Vite):

```bash
# Vite lê .env automaticamente (variáveis com VITE_)
cd zenite-mei-app
npm run dev
```

---

## 🔍 VERIFICAR SE ESTÁ FUNCIONANDO

### 1. Verificar .env

```bash
# Ver variáveis configuradas (sem mostrar valores sensíveis)
grep -E "^[A-Z].*=" .env | cut -d'=' -f1
```

**Saída esperada:**
```
VITE_FIREBASE_API_KEY
VITE_FIREBASE_AUTH_DOMAIN
VITE_FIREBASE_PROJECT_ID
VITE_FIREBASE_STORAGE_BUCKET
VITE_FIREBASE_MESSAGING_SENDER_ID
VITE_FIREBASE_APP_ID
VITE_FIREBASE_MEASUREMENT_ID
FIREBASE_CREDENTIALS_JSON
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD
REDIS_HOST
REDIS_PORT
SPRING_PROFILES_ACTIVE
NODE_ENV
```

### 2. Testar Docker Compose

```bash
# Start apenas o banco
docker-compose up -d postgres

# Verificar se pegou as variáveis
docker-compose exec postgres env | grep POSTGRES
```

**Saída esperada:**
```
POSTGRES_DB=zenmei
POSTGRES_USER=zenmei
POSTGRES_PASSWORD=zenmei123
```

### 3. Testar Aplicação

```bash
# Start tudo
./start.sh

# Verificar logs do MEI API
docker-compose logs zenmei-mei-api | grep Firebase
```

**Saída esperada:**
```
✅ Firebase App inicializado com sucesso
```

---

## 🛡️ SEGURANÇA DO .ENV

### ✅ O que já está protegido:

```bash
# .env está no .gitignore
cat .gitignore | grep .env
```

**Saída:**
```
.env
.env.local
.env.*.local
```

### ⚠️ NUNCA faça:

```bash
# ❌ NÃO commite o .env
git add .env  # NÃO FAÇA ISSO!

# ✅ Commite apenas o .env.example
git add .env.example  # OK!
```

---

## 📊 ARQUIVO .ENV ATUAL

Seu `.env` agora está assim:

```bash
# Frontend - ✅ Configurado
VITE_FIREBASE_API_KEY=AIzaSy... (configurado)
VITE_FIREBASE_AUTH_DOMAIN=zenmei-app-8a181.firebaseapp.com
VITE_FIREBASE_PROJECT_ID=zenmei-app-8a181
# ... etc

# Backend - ⚠️ Precisa configurar
FIREBASE_CREDENTIALS_JSON=  ← VAZIO (você precisa preencher)

# Database - ✅ Configurado
POSTGRES_DB=zenmei
POSTGRES_USER=zenmei
POSTGRES_PASSWORD=zenmei123
```

---

## 🎯 PRÓXIMOS PASSOS

### 1. Configure Firebase Backend (2 minutos)

```bash
# Download Service Account
# https://console.firebase.google.com/project/zenmei-app-8a181/settings/serviceaccounts/adminsdk

# Cole no .env
nano .env  # Linha: FIREBASE_CREDENTIALS_JSON=
```

### 2. Verifique a configuração

```bash
./setup-env.sh
```

### 3. Inicie tudo

```bash
./start.sh
```

### 4. Acesse

```
http://localhost:5173  # Frontend
http://localhost:8091  # BFF API
```

---

## 🔧 TROUBLESHOOTING

### Problema: "Firebase not configured"

**Solução:** Verifique se `FIREBASE_CREDENTIALS_JSON` está preenchido no `.env`

```bash
grep "FIREBASE_CREDENTIALS_JSON=" .env
```

### Problema: "Invalid credentials"

**Solução:** Verifique se o JSON está em uma linha só e entre aspas simples

```bash
# Correto:
FIREBASE_CREDENTIALS_JSON='{"type":"service_account",...}'

# Errado (quebrado em várias linhas):
FIREBASE_CREDENTIALS_JSON='{"type":"service_account",
"project_id":"..."}'
```

### Problema: "Variables not loaded"

**Solução:** Reinicie o Docker Compose

```bash
docker-compose down
docker-compose up -d
```

---

## 📚 COMANDOS ÚTEIS

```bash
# Ver status do .env
./setup-env.sh

# Editar .env
nano .env

# Testar variáveis no Docker
docker-compose config

# Ver variáveis de um serviço
docker-compose exec zenmei-mei-api env

# Recarregar .env (restart serviços)
docker-compose restart
```

---

## ✅ CHECKLIST FINAL

- [x] `.env` criado
- [x] Firebase Frontend configurado (✅ feito automaticamente)
- [ ] Firebase Backend configurado (⚠️ você precisa fazer)
- [x] Database configurado (✅ feito automaticamente)
- [x] `.gitignore` configurado (✅ .env não será commitado)

**Falta apenas:** Configurar Firebase Service Account!

---

## 🎉 RESUMO

**O que está pronto:**
- ✅ Arquivo `.env` criado
- ✅ Firebase Frontend configurado (7 variáveis)
- ✅ Database configurado (3 variáveis)
- ✅ Script de verificação (`setup-env.sh`)

**O que você precisa fazer:**
1. Baixar Firebase Service Account (1 minuto)
2. Colar no `.env` (30 segundos)
3. Rodar `./start.sh` (automático)

**Total: 2 minutos para ter tudo rodando!** ⚡

---

**Próximo comando:** 
```bash
./setup-env.sh  # Ver status atual
```
