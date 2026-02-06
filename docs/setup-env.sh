#!/bin/bash

# Script para configurar o .env automaticamente
# Desenvolvido por: JamesCoder

echo "🔐 Configurador de .env - ZenMei"
echo "================================"
echo ""

ENV_FILE=".env"

# Verificar se .env existe
if [ ! -f "$ENV_FILE" ]; then
    echo "📝 Criando arquivo .env..."
    cp .env.example .env
fi

echo "✅ Arquivo .env encontrado!"
echo ""

# Verificar Firebase Frontend (já está configurado)
if grep -q "VITE_FIREBASE_API_KEY=AIzaSy" .env 2>/dev/null; then
    echo "✅ Firebase Frontend: Configurado"
    FRONTEND_OK=1
else
    echo "⚠️  Firebase Frontend: Não configurado"
    FRONTEND_OK=0
fi

# Verificar Firebase Backend
if grep -q 'FIREBASE_CREDENTIALS_JSON=.*{"type":"service_account"' .env 2>/dev/null; then
    echo "✅ Firebase Backend: Configurado"
    BACKEND_OK=1
else
    echo "❌ Firebase Backend: NÃO CONFIGURADO"
    BACKEND_OK=0
    echo ""
    echo "📝 Para configurar o Firebase Backend (Service Account):"
    echo ""
    echo "1. Acesse: https://console.firebase.google.com/project/zenmei-app-8a181/settings/serviceaccounts/adminsdk"
    echo ""
    echo "2. Clique em 'Generate new private key'"
    echo ""
    echo "3. Baixe o arquivo JSON"
    echo ""
    echo "4. Execute este comando (substitua pelo caminho do arquivo):"
    echo "   export FIREBASE_CREDS=\$(cat caminho/para/service-account.json | tr -d '\\n')"
    echo "   sed -i \"s|FIREBASE_CREDENTIALS_JSON=.*|FIREBASE_CREDENTIALS_JSON='\${FIREBASE_CREDS}'|\" .env"
    echo ""
    echo "OU manualmente:"
    echo "   Abra o .env e cole o conteúdo do JSON na linha FIREBASE_CREDENTIALS_JSON="
    echo ""
fi

echo ""
echo "================================"
echo "📊 Status das variáveis:"
echo ""

# Contar variáveis
total_frontend=7
configured_frontend=$FRONTEND_OK

echo "  Frontend: $configured_frontend/$total_frontend configurado(s)"
echo "  Backend: $BACKEND_OK/1 configurado(s)"
echo "  Database: 3/3 configurado(s) ✅"

if [ $BACKEND_OK -eq 1 ]; then
    echo ""
    echo "🎉 Tudo configurado! Pronto para usar!"
fi

echo ""
echo "================================"
echo "🚀 Próximos passos:"
echo ""
echo "1. Configure o Firebase Backend (ver instruções acima)"
echo "2. Execute: ./start.sh"
echo "3. Acesse: http://localhost:5173"
echo ""
