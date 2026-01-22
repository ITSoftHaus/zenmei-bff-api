#!/bin/bash

# Script universal para fazer push do projeto ZenMei
# Autor: ZenMei Development Team
# Data: 21 de Janeiro de 2026

echo ""
echo "╔═══════════════════════════════════════════════════════════╗"
echo "║                                                           ║"
echo "║      🚀 Push Automático - Projeto ZenMei                ║"
echo "║                                                           ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""

# Verificar se a URL foi passada como parâmetro
if [ -z "$1" ]; then
    echo "❌ Erro: URL do repositório não fornecida!"
    echo ""
    echo "Uso:"
    echo "  bash push-to-remote.sh <URL-DO-REPOSITORIO>"
    echo ""
    echo "Exemplos:"
    echo "  bash push-to-remote.sh https://github.com/usuario/zenmei.git"
    echo "  bash push-to-remote.sh git@github.com:usuario/zenmei.git"
    echo ""
    exit 1
fi

REPO_URL="$1"

echo "📍 Repositório: $REPO_URL"
echo ""

# Ir para o diretório do projeto
cd /home/t102640/Desenvolvimento/zenmei || exit 1

# Verificar se já existe um remoto
if git remote -v | grep -q "origin"; then
    echo "⚠️  Repositório remoto 'origin' já existe!"
    echo ""
    git remote -v
    echo ""
    read -p "Deseja substituir? (s/n): " resposta
    if [[ $resposta == "s" || $resposta == "S" ]]; then
        echo "🔄 Removendo remoto antigo..."
        git remote remove origin
        echo "➕ Adicionando novo remoto..."
        git remote add origin "$REPO_URL"
    else
        echo "❌ Operação cancelada."
        exit 1
    fi
else
    echo "➕ Adicionando repositório remoto..."
    git remote add origin "$REPO_URL"
fi

echo ""
echo "✅ Remoto configurado:"
git remote -v
echo ""

# Confirmar antes de fazer push
read -p "🚀 Fazer push agora? (s/n): " resposta
if [[ $resposta != "s" && $resposta != "S" ]]; then
    echo "❌ Push cancelado. Execute novamente quando estiver pronto."
    exit 0
fi

echo ""
echo "═══════════════════════════════════════════════════════════"
echo "📤 Iniciando push do branch main..."
echo "═══════════════════════════════════════════════════════════"
echo ""

# Push do branch main
if git push -u origin main; then
    echo ""
    echo "✅ Push do branch main realizado com sucesso!"
else
    echo ""
    echo "❌ Erro ao fazer push do branch main!"
    echo ""
    echo "Possíveis causas:"
    echo "  - Problemas de autenticação"
    echo "  - Repositório remoto já tem conteúdo"
    echo "  - Problemas de rede"
    echo ""
    echo "Tente:"
    echo "  git push -u origin main --force  (use com cuidado!)"
    exit 1
fi

echo ""
echo "═══════════════════════════════════════════════════════════"
echo "🏷️  Fazendo push das tags..."
echo "═══════════════════════════════════════════════════════════"
echo ""

# Push das tags
if git push --tags; then
    echo ""
    echo "✅ Push das tags realizado com sucesso!"
else
    echo ""
    echo "⚠️  Erro ao fazer push das tags (não crítico)"
fi

echo ""
echo "╔═══════════════════════════════════════════════════════════╗"
echo "║                                                           ║"
echo "║            ✅  PUSH COMPLETO COM SUCESSO!                ║"
echo "║                                                           ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""
echo "📊 Resumo:"
echo "   • 7 commits enviados"
echo "   • 1 tag enviada (v1.0.0-refactor-user-to-mei)"
echo "   • Branch: main"
echo ""
echo "🌐 Acesse seu repositório:"
echo "   $REPO_URL"
echo ""
echo "═══════════════════════════════════════════════════════════"
echo ""
