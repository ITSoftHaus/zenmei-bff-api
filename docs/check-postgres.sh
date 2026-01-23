#!/bin/bash

# Script para verificar configuração do PostgreSQL e Pools
# Desenvolvido por: JamesCoder

echo "🔍 Verificando Configuração PostgreSQL e Connection Pools"
echo "=========================================================="
echo ""

# Verificar se docker-compose.yml existe
if [ ! -f "docker-compose.yml" ]; then
    echo "❌ docker-compose.yml não encontrado!"
    exit 1
fi

echo "📊 Verificando PostgreSQL no docker-compose.yml..."
echo ""

# Verificar max_connections
if grep -q "max_connections=200" docker-compose.yml; then
    echo "✅ Max Connections: 200 (configurado)"
else
    echo "❌ Max Connections: NÃO configurado"
fi

# Verificar shared_buffers
if grep -q "shared_buffers=256MB" docker-compose.yml; then
    echo "✅ Shared Buffers: 256MB (configurado)"
else
    echo "⚠️  Shared Buffers: NÃO configurado"
fi

echo ""
echo "📊 Verificando Pool de Conexões dos Microsserviços..."
echo ""

# Contar quantos serviços têm pool configurado
pool_count=$(grep -c "SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE: 5" docker-compose.yml)

echo "  Microsserviços com pool configurado: $pool_count/11"

if [ $pool_count -eq 11 ]; then
    echo "✅ Todos os microsserviços configurados!"
elif [ $pool_count -gt 0 ]; then
    echo "⚠️  Alguns microsserviços faltando configuração"
else
    echo "❌ Nenhum microsserviço configurado"
fi

echo ""
echo "📊 Verificando Script de Inicialização..."
echo ""

if [ -f "postgres/init.sql" ]; then
    echo "✅ postgres/init.sql: Encontrado"

    # Verificar schemas
    schema_count=$(grep -c "CREATE SCHEMA IF NOT EXISTS" postgres/init.sql)
    echo "  Schemas definidos: $schema_count"

    # Verificar extensions
    ext_count=$(grep -c "CREATE EXTENSION IF NOT EXISTS" postgres/init.sql)
    echo "  Extensions: $ext_count"
else
    echo "❌ postgres/init.sql: NÃO encontrado"
fi

echo ""
echo "=========================================================="
echo "📊 RESUMO:"
echo ""

# Calcular uso estimado
pool_size=5
microservices=11
dbeaver=5
overhead=10

total_connections=$((pool_size * microservices + dbeaver + overhead))
max_connections=200
usage_percent=$((total_connections * 100 / max_connections))

echo "  PostgreSQL Max Connections: 200"
echo "  Uso Estimado:"
echo "    - 11 microsserviços × 5 = 55"
echo "    - DBeaver = 5"
echo "    - Overhead = 10"
echo "    - Total = $total_connections"
echo "  "
echo "  Capacidade: $total_connections/$max_connections ($usage_percent% usado)"
echo "  Disponível: $((max_connections - total_connections)) conexões livres"
echo ""

if [ $usage_percent -lt 50 ]; then
    echo "✅ Capacidade: EXCELENTE (< 50%)"
elif [ $usage_percent -lt 75 ]; then
    echo "✅ Capacidade: BOA (50-75%)"
elif [ $usage_percent -lt 90 ]; then
    echo "⚠️  Capacidade: ATENCAO (75-90%)"
else
    echo "❌ Capacidade: CRITICA (> 90%)"
fi

echo ""
echo "=========================================================="
echo "🚀 Próximos Passos:"
echo ""
echo "1. Se tudo está OK, execute: ./start.sh"
echo "2. Conecte no DBeaver: localhost:5432"
echo "3. Monitore conexões: docker-compose exec postgres psql -U zenmei"
echo ""
echo "📚 Ver documentação completa: cat POSTGRES_POOL_RESOLVIDO.md"
echo ""
