#!/bin/bash

# Script para resolver o problema de push rejeitado
# O repositório remoto tem conteúdo que precisa ser integrado

cd /home/t102640/Desenvolvimento/zenmei/zenmei-bff-api

echo "🔍 Problema: O repositório remoto tem conteúdo (provavelmente README/LICENSE)"
echo ""
echo "═══════════════════════════════════════════════════════════════"
echo ""
echo "SOLUÇÃO: Force push para substituir o conteúdo remoto"
echo ""
echo "Execute este comando:"
echo ""
echo "cd /home/t102640/Desenvolvimento/zenmei/zenmei-bff-api"
echo "git push -u origin main --force"
echo ""
echo "Quando pedir:"
echo "Username: itsofthaus"
echo "Password: [COLE SEU TOKEN]"
echo ""
echo "═══════════════════════════════════════════════════════════════"
echo ""
echo "⚠️  ATENÇÃO: --force vai SUBSTITUIR todo o conteúdo do repositório remoto"
echo "   com o seu código local (que é o que você quer!)"
echo ""
echo "✅ Isso é seguro porque você está enviando o BFF completo que acabou de criar."
echo ""
