#!/bin/bash

# Script para fazer push SOMENTE do BFF
# Autor: ZenMei Development Team
# Data: 21 de Janeiro de 2026

echo ""
echo "╔═══════════════════════════════════════════════════════════╗"
echo "║                                                           ║"
echo "║      🚀 Push do BFF - zenmei-bff-api                     ║"
echo "║                                                           ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""

REPO_URL="https://github.com/ITSoftHaus/zenmei-bff-api.git"

echo "📍 Repositório: $REPO_URL"
echo "📂 Diretório: zenmei-bff-api"
echo ""

# Ir para o diretório do BFF
cd /home/t102640/Desenvolvimento/zenmei/zenmei-bff-api || {
    echo "❌ Erro: Diretório zenmei-bff-api não encontrado!"
    exit 1
}

# Verificar se já é um repositório git
if [ ! -d ".git" ]; then
    echo "🔧 Inicializando repositório Git no BFF..."
    git init
    git checkout -b main
fi

# Adicionar todos os arquivos
echo "📦 Adicionando arquivos..."
git add .

# Fazer commit
echo "💾 Criando commit..."
git commit -m "feat: adicionar BFF completo com refactor User → Mei

- Backend for Frontend integrando 10 microsserviços
- Circuit Breaker e Retry implementados
- Feign Clients para todos os serviços
- Documentação completa incluída
- Endpoints: /api/bff/v1/*

Microsserviços integrados:
- MEI API (renomeado de user-api)
- Agenda API
- Chamado API
- Client API
- CNAE API
- Despesa API
- Nota Fiscal API
- Receita API
- Serviço API
- Produto API

BREAKING CHANGE: Refactor User → Mei aplicado"

# Verificar se já existe um remoto
if git remote -v | grep -q "origin"; then
    echo "⚠️  Repositório remoto 'origin' já existe!"
    git remote -v
else
    echo "➕ Adicionando repositório remoto..."
    git remote add origin "$REPO_URL"
fi

echo ""
echo "✅ Pronto para push!"
echo ""
read -p "🚀 Fazer push agora? (s/n): " resposta

if [[ $resposta == "s" || $resposta == "S" ]]; then
    echo ""
    echo "📤 Fazendo push do BFF..."

    if git push -u origin main --force; then
        echo ""
        echo "✅ Push do BFF realizado com sucesso!"
        echo ""
        echo "🌐 Acesse: $REPO_URL"
    else
        echo ""
        echo "❌ Erro ao fazer push!"
        echo ""
        echo "Verifique:"
        echo "  - Autenticação GitHub configurada"
        echo "  - Permissões no repositório"
        echo "  - Conexão com internet"
    fi
else
    echo ""
    echo "❌ Push cancelado."
    echo ""
    echo "Execute novamente quando estiver pronto:"
    echo "  bash push-bff-only.sh"
fi

echo ""
