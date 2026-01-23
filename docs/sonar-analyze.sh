#!/bin/bash
# Script para rodar análise SonarQube em todos os microsserviços
# Usage: ./sonar-analyze.sh [SONAR_TOKEN]
SONAR_TOKEN=${1:-$SONAR_TOKEN}
if [ -z "$SONAR_TOKEN" ]; then
    echo "❌ SONAR_TOKEN não definido!"
    echo "Usage: ./sonar-analyze.sh <token>"
    echo "Ou: export SONAR_TOKEN=<token> && ./sonar-analyze.sh"
    exit 1
fi
echo "🔍 Iniciando análise SonarQube dos microsserviços ZenMei..."
echo "============================================================"
SERVICES=(
    "zenmei-mei-api"
    "zenmei-client-api"
    "zenmei-agenda-api"
    "zenmei-chamado-api"
    "zenmei-cnae-api"
    "zenmei-despesa-api"
    "zenmei-nota-api"
    "zenmei-produto-api"
    "zenmei-receita-api"
    "zenmei-servico-api"
    "zenmei-bff-api"
)
SUCCESS=0
FAILED=0
for service in "${SERVICES[@]}"; do
    echo ""
    echo "📦 Analisando: $service"
    echo "------------------------------------------------------------"
    if [ -d "$service" ]; then
        cd "$service" || continue
        # Rodar testes com coverage
        echo "  🧪 Rodando testes..."
        mvn clean test jacoco:report -q
        # Rodar análise do Sonar
        echo "  🔍 Enviando para SonarQube..."
        mvn sonar:sonar             -Dsonar.token="$SONAR_TOKEN"             -Dsonar.host.url=https://sonarcloud.io             -q
        if [ $? -eq 0 ]; then
            echo "  ✅ $service - Análise completa!"
            ((SUCCESS++))
        else
            echo "  ❌ $service - Falhou!"
            ((FAILED++))
        fi
        cd ..
    else
        echo "  ⚠️  Diretório não encontrado: $service"
        ((FAILED++))
    fi
done
echo ""
echo "============================================================"
echo "📊 RESUMO:"
echo "  ✅ Sucessos: $SUCCESS"
echo "  ❌ Falhas: $FAILED"
echo "  📦 Total: ${#SERVICES[@]}"
echo ""
echo "🔗 Acesse: https://sonarcloud.io/organizations/zenmei"
echo "============================================================"
