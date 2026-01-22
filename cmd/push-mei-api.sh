#!/bin/bash

# Script para fazer push do zenmei-mei-api
# Autor: ZenMei Development Team
# Data: 21 de Janeiro de 2026

echo ""
echo "╔═══════════════════════════════════════════════════════════╗"
echo "║                                                           ║"
echo "║      🚀 Push do MEI API para GitHub                      ║"
echo "║                                                           ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""

# Ir para o diretório do mei-api
cd /home/t102640/Desenvolvimento/zenmei/zenmei-mei-api || {
    echo "❌ Erro: Diretório zenmei-mei-api não encontrado!"
    exit 1
}

echo "📂 Diretório: $(pwd)"
echo ""

# Verificar se já é um repositório git
if [ ! -d ".git" ]; then
    echo "🔧 Inicializando repositório Git no MEI API..."
    git init
    git checkout -b main
else
    echo "✅ Git já inicializado"
fi

# Adicionar todos os arquivos
echo "📦 Adicionando arquivos..."
git add .

# Verificar se há mudanças para commitar
if git diff --staged --quiet; then
    echo "⚠️  Nenhuma mudança para commitar"
else
    # Fazer commit
    echo "💾 Criando commit..."
    git commit -m "feat: MEI API completa com refactor User → Mei

- Renomeado de zenmei-user-api para zenmei-mei-api
- Entidade User renomeada para Mei
- Controller MeiController (antigo UserController)
- Service MeiService (antigo UserService)
- Repository MeiRepository (antigo UserRepository)
- Endpoint: /api/v1/profile
- Tabela: meis (antiga users)

BREAKING CHANGE: Entidade User foi renomeada para Mei"
fi

# Configurar remoto
REPO_URL="https://github.com/ITSoftHaus/zenmei-mei-api.git"

if git remote -v | grep -q "origin"; then
    echo "⚠️  Repositório remoto 'origin' já existe"
    CURRENT_URL=$(git remote get-url origin)
    if [ "$CURRENT_URL" != "$REPO_URL" ]; then
        echo "🔄 Atualizando URL do remoto..."
        git remote set-url origin "$REPO_URL"
    fi
else
    echo "➕ Adicionando repositório remoto..."
    git remote add origin "$REPO_URL"
fi

echo ""
echo "✅ Configuração completa!"
echo ""
echo "📍 Remoto: $REPO_URL"
echo ""

read -p "🚀 Fazer push agora? (s/n): " resposta

if [[ $resposta == "s" || $resposta == "S" ]]; then
    echo ""
    echo "📤 Fazendo push do MEI API..."

    if git push -u origin main --force; then
        echo ""
        echo "✅ Push do MEI API realizado com sucesso!"
        echo ""
        echo "🌐 Acesse: $REPO_URL"
    else
        echo ""
        echo "❌ Erro ao fazer push!"
        echo ""
        echo "Se for problema de autenticação, execute manualmente:"
        echo "  cd /home/t102640/Desenvolvimento/zenmei/zenmei-mei-api"
        echo "  git push -u origin main --force"
        echo ""
        echo "Credenciais:"
        echo "  Username: itsofthaus"
        echo "  Password: [SEU TOKEN GITHUB]"
    fi
else
    echo ""
    echo "❌ Push cancelado."
    echo ""
    echo "Execute quando estiver pronto:"
    echo "  cd /home/t102640/Desenvolvimento/zenmei/zenmei-mei-api"
    echo "  git push -u origin main --force"
fi

echo ""
