#!/bin/bash

# 🔥 GUIA RÁPIDO - SUBIR ZENMEI MANUALMENTE
# Desenvolvido por: JamesCoder
# Data: 23/01/2026

echo "🔥 ZENMEI - Guia de Start Manual"
echo "================================"
echo ""
echo "Parece que o Docker está com problemas de permissão."
echo "Vamos subir de forma alternativa!"
echo ""

cat << 'EOF'

📦 OPÇÃO 1: SUBIR APENAS FRONTEND (Mais Rápido)
================================================

1. Abrir terminal e executar:
   cd /home/t102640/Desenvolvimento/zenmei/zenite-mei-app
   npm install  # Se ainda não fez
   npm run dev

2. Acessar:
   http://localhost:5173

✅ O que funciona:
   - Interface completa
   - Navegação
   - Componentes
   - Login (Firebase)

⚠️  O que não funciona (precisa backend):
   - Chamadas API
   - CRUD de dados


📦 OPÇÃO 2: BACKEND LOCAL (Um microsserviço)
==============================================

1. Subir PostgreSQL local ou Docker:
   docker run -d \
     --name postgres-zenmei \
     -e POSTGRES_DB=zenmei \
     -e POSTGRES_USER=zenmei \
     -e POSTGRES_PASSWORD=zenmei123 \
     -p 5433:5432 \
     postgres:16-alpine

2. Subir MEI API:
   cd /home/t102640/Desenvolvimento/zenmei/zenmei-mei-api

   # Configurar variáveis
   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/zenmei
   export SPRING_DATASOURCE_USERNAME=zenmei
   export SPRING_DATASOURCE_PASSWORD=zenmei123

   # Rodar
   mvn spring-boot:run

3. Testar:
   curl http://localhost:8080/actuator/health


📦 OPÇÃO 3: RESOLVER PERMISSÃO DO DOCKER
==========================================

O erro foi: "permission denied" no buildx

Solução:

1. Adicionar usuário ao grupo docker:
   sudo usermod -aG docker $USER

2. Dar permissões corretas:
   sudo chown -R $USER:$USER ~/.docker
   sudo chmod -R 755 ~/.docker

3. Reiniciar sessão:
   newgrp docker
   # ou fazer logout/login

4. Testar:
   docker ps

5. Rodar novamente:
   cd /home/t102640/Desenvolvimento/zenmei
   ./start.sh


📦 OPÇÃO 4: DOCKER SEM BUILD (Usar Imagens Base)
==================================================

Se o problema é no build, rode sem buildar:

1. Subir apenas banco:
   cd /home/t102640/Desenvolvimento/zenmei
   docker-compose up -d postgres redis

2. Rodar microsserviços manualmente:
   # Em terminais separados
   cd zenmei-mei-api && mvn spring-boot:run
   cd zenmei-client-api && mvn spring-boot:run
   # ... etc


🔧 TROUBLESHOOTING
===================

Problema: "Porta 5432 já em uso"
Solução: Mudei para porta 5433 no docker-compose.yml
         Ou pare o PostgreSQL local:
         sudo systemctl stop postgresql

Problema: "Permission denied no Docker"
Solução: Veja OPÇÃO 3 acima

Problema: "Firebase não configurado"
Solução: Edite .env e adicione FIREBASE_CREDENTIALS_JSON
         Ver: cat COMO_USAR_ENV.md


📊 STATUS ATUAL
================

✅ Configurado:
   - docker-compose.yml
   - PostgreSQL otimizado (200 conexões)
   - Pool de conexões (5 por serviço)
   - Schemas isolados
   - .env parcialmente configurado
   - Scripts de verificação

⚠️  Pendente:
   - Permissão do Docker buildx
   - Firebase Service Account no .env
   - Build das imagens Docker


🎯 RECOMENDAÇÃO IMEDIATA
==========================

Execute agora:

# Terminal 1 - Frontend
cd /home/t102640/Desenvolvimento/zenmei/zenite-mei-app
npm run dev

# Acesse: http://localhost:5173

# Quando resolver Docker, execute:
cd /home/t102640/Desenvolvimento/zenmei
./start.sh


📚 DOCUMENTAÇÃO
================

Ver guias completos em:
- COMO_USAR_ENV.md
- POSTGRES_POOL_RESOLVIDO.md
- README.md

EOF

echo ""
echo "================================"
echo "🔥 PRÓXIMO PASSO:"
echo ""
echo "Execute uma das opções acima!"
echo "Mais simples: OPÇÃO 1 (Frontend apenas)"
echo ""
echo "cd zenite-mei-app && npm run dev"
echo ""
