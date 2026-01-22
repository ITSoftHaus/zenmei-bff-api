#!/bin/bash

# Script para facilitar o push do refactor User → Mei
# Autor: ZenMEI Development Team
# Data: 21 de Janeiro de 2026

echo ""
echo "╔═══════════════════════════════════════════════════════════╗"
echo "║                                                           ║"
echo "║      🚀 Script de Push - Refactor User → Mei            ║"
echo "║                                                           ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""

# Ir para o diretório do projeto
cd /home/t102640/Desenvolvimento/zenmei || exit 1

# Verificar se já existe remoto configurado
if git remote -v | grep -q "origin"; then
    echo "✅ Repositório remoto já configurado:"
    git remote -v
    echo ""

    # Perguntar se quer continuar
    read -p "Deseja fazer push agora? (s/n): " resposta
    if [[ $resposta == "s" || $resposta == "S" ]]; then
        echo ""
        echo "📤 Fazendo push do branch main..."
        git push -u origin main

        echo ""
        echo "🏷️  Fazendo push das tags..."
        git push --tags

        echo ""
        echo "✅ Push concluído com sucesso!"
        echo ""
        echo "📊 Verificar em:"
        git remote get-url origin
    fi
else
    echo "⚠️  Nenhum repositório remoto configurado."
    echo ""
    echo "Por favor, configure o repositório remoto primeiro:"
    echo ""
    echo "Para GitHub:"
    echo "  git remote add origin https://github.com/SEU-USUARIO/zenmei.git"
    echo ""
    echo "Para GitLab:"
    echo "  git remote add origin https://gitlab.com/SEU-USUARIO/zenmei.git"
    echo ""
    echo "Depois execute este script novamente:"
    echo "  bash push-refactor.sh"
    echo ""
fi

echo ""
echo "═══════════════════════════════════════════════════════════"
echo "📚 Para mais informações, veja: GIT_PUSH_INSTRUCTIONS.md"
echo "═══════════════════════════════════════════════════════════"
echo ""
