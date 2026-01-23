#!/bin/bash

# ZenMei - Script de Inicialização Rápida
# Autor: JamesCoder

set -e

echo "🚀 ZenMei - Iniciando ambiente completo..."
echo "=========================================="

# Verificar se .env existe
if [ ! -f .env ]; then
    echo "⚠️  Arquivo .env não encontrado!"
    echo "📝 Copiando .env.example para .env..."
    cp .env.example .env
    echo "✅ Arquivo .env criado. Por favor, configure as variáveis de ambiente."
    echo "   Edite o arquivo .env com suas credenciais do Firebase."
    read -p "Pressione ENTER após configurar o .env..."
fi

# Verificar se Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando!"
    echo "   Inicie o Docker e tente novamente."
    exit 1
fi

# Verificar se Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose não está instalado!"
    exit 1
fi

echo ""
echo "🔧 Parando containers anteriores (se existirem)..."
docker-compose down -v

echo ""
echo "🏗️  Buildando imagens..."
docker-compose build --parallel

echo ""
echo "🚀 Iniciando serviços..."
docker-compose up -d

echo ""
echo "⏳ Aguardando serviços iniciarem..."
sleep 30

echo ""
echo "✅ Verificando health dos serviços..."
echo ""

services=(
    "postgres:5432"
    "redis:6379"
    "zenmei-mei-api:8081"
    "zenmei-client-api:8082"
    "zenmei-agenda-api:8083"
    "zenmei-chamado-api:8084"
    "zenmei-cnae-api:8085"
    "zenmei-despesa-api:8086"
    "zenmei-nota-api:8087"
    "zenmei-produto-api:8088"
    "zenmei-receita-api:8089"
    "zenmei-servico-api:8090"
    "zenmei-bff-api:8091"
    "zenite-mei-app:5173"
)

for service in "${services[@]}"; do
    name=$(echo $service | cut -d: -f1)
    port=$(echo $service | cut -d: -f2)

    if nc -z localhost $port 2>/dev/null; then
        echo "✅ $name - OK (porta $port)"
    else
        echo "❌ $name - FALHOU (porta $port)"
    fi
done

echo ""
echo "=========================================="
echo "🎉 ZenMei iniciado com sucesso!"
echo ""
echo "📦 Serviços disponíveis:"
echo "   🗄️  PostgreSQL:        localhost:5432"
echo "   🔴 Redis:              localhost:6379"
echo "   👤 MEI API:            http://localhost:8081"
echo "   👥 Client API:         http://localhost:8082"
echo "   📅 Agenda API:         http://localhost:8083"
echo "   🎫 Chamado API:        http://localhost:8084"
echo "   📊 CNAE API:           http://localhost:8085"
echo "   💰 Despesa API:        http://localhost:8086"
echo "   📄 Nota API:           http://localhost:8087"
echo "   📦 Produto API:        http://localhost:8088"
echo "   💵 Receita API:        http://localhost:8089"
echo "   🛠️  Servico API:        http://localhost:8090"
echo "   🌐 BFF API:            http://localhost:8091"
echo "   🎨 Frontend:           http://localhost:5173"
echo ""
echo "📚 Comandos úteis:"
echo "   Ver logs:              docker-compose logs -f"
echo "   Ver logs de um serviço: docker-compose logs -f zenmei-mei-api"
echo "   Parar tudo:            docker-compose down"
echo "   Rebuild:               docker-compose up -d --build"
echo ""
echo "🔗 Acesse o frontend: http://localhost:5173"
echo "=========================================="
