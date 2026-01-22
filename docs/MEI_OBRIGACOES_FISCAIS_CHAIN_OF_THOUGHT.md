# Chain of Thought: Implementação do Sistema de Obrigações Fiscais do MEI

## 📋 Entendimento do Problema

### Contexto
O sistema precisa gerenciar as obrigações fiscais dos MEI (Microempreendedores Individuais), que incluem:

1. **DAS MEI** - Imposto mensal de arrecadação com vencimento todo dia 20
2. **DASN-SIMEI** - Declaração anual cobrada uma única vez no ano (31 de maio)
3. **Relatório Mensal de Faturamento** - Deve ser feito até o dia 20 de cada mês

### Objetivos Principais

O sistema deve:
- Registrar anualmente todas as obrigações do MEI
- Criar avisos aos MEI sobre a proximidade dos vencimentos
- Utilizar uma escala degradê de cores baseada na proximidade dos vencimentos
- Sempre enviar 3 obrigações para o frontend
- Gerenciar o ciclo de vida das obrigações (status: A vencer, Em dia, Atrasada)

## 🗂️ Estrutura de Dados

### Entidades

#### 1. ObrigacoesFiscais (Tabela de Referência)
```sql
CREATE TABLE public.obrigacoes_fiscais (
    id uuid NOT NULL,
    obrigacao varchar(255) NOT NULL,
    mes_competencia varchar(10) NOT NULL,
    dia_competencia varchar(10) NOT NULL,
    CONSTRAINT obrigacoes_fiscais_pk PRIMARY KEY (id)
);
```

**Dados Mestre:**
- DAS MEI (dia 20, mensal)
- DASN-SIMEI (dia 31, maio)
- Relatório Mensal de Faturamento (dia 21, mensal)

#### 2. MeiObrigacoesFiscais (Instâncias das Obrigações por MEI)
```sql
CREATE TABLE public.mei_obrigacoes_fiscais (
    id uuid NOT NULL,
    id_mei uuid NOT NULL,
    id_obrigacao uuid NOT NULL,
    mes_ano_competencia varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
    pdf_relatorio text NULL,
    CONSTRAINT mei_obrigacoes_fiscais_pk PRIMARY KEY (id)
);
```

**Status possíveis:**
- A vencer
- Em dia
- Atrasada

#### 3. Recibos (Nova Entidade)
```sql
CREATE TABLE public.recibos (
    id uuid NOT NULL,
    cpf varchar(14) NOT NULL,
    descricao varchar(255) NOT NULL,
    tipo_servico varchar(255) NOT NULL,
    cidade varchar(255) NOT NULL,
    data_recebimento date NOT NULL,
    valor numeric(38, 2) NOT NULL,
    pdf_recibo text NULL,
    user_id uuid NOT NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    updated_at timestamptz NULL,
    CONSTRAINT recibos_pk PRIMARY KEY (id),
    CONSTRAINT recibos_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE
);
```

### Tabelas Relacionadas (Já Existentes)
- despesas
- notas_fiscais
- nota_fiscal_servicos_ids
- nota_fiscal_produto_ids
- receitas (assumida)

## 🎯 Requisitos Detalhados

### 1. Endpoint: Listar Obrigações do MEI
**GET** `/api/bff/v1/mei/{idMei}/obrigacoes-fiscais`

**Resposta:**
```json
[
  {
    "id": "uuid",
    "idMei": "uuid",
    "idObrigacao": "uuid",
    "obrigacao": "DAS MEI",
    "diaCompetencia": "20",
    "mesAnoCompetencia": "022026",
    "status": "Em dia",
    "pdfRelatorio": "base64_string"
  }
]
```

**Regras:**
- Sempre retornar 3 obrigações
- DASN-SIMEI sempre deve aparecer
- Quando uma obrigação é fechada, outra igual deve tomar seu lugar na sequência

### 2. Utilização de Endpoints de zenmei-mei-api
**Ação:** Migrar as obrigações fiscais que hoje existem no BFF para o microserviço zenmei-mei-api

**Endpoints a criar no mei-api:**
- GET `/api/v1/obrigacoes-fiscais` - Lista tipos de obrigações
- GET `/api/v1/mei/{idMei}/obrigacoes-fiscais` - Lista obrigações de um MEI
- POST `/api/v1/mei/{idMei}/obrigacoes-fiscais` - Criar obrigação para MEI
- PUT `/api/v1/mei/{idMei}/obrigacoes-fiscais/{id}` - Atualizar obrigação
- GET `/api/v1/mei/obrigacoes-atrasadas` - Listar MEIs com obrigações atrasadas

### 3. Renovação Automática de Obrigações
**Lógica:**
- Quando uma obrigação é fechada (status = "Em dia"), criar automaticamente a próxima:
  - DAS MEI: próximo mês, dia 20
  - Relatório Mensal: próximo mês, dia 21
  - DASN-SIMEI: próximo ano, 31 de maio

### 4. Endpoint: MEIs com Obrigações Atrasadas
**GET** `/api/bff/v1/mei/obrigacoes-atrasadas`

**Resposta:**
```json
[
  {
    "idMei": "uuid",
    "nomeMei": "string",
    "quantidadeAtrasadas": 3
  }
]
```

### 5. Endpoint: Listar Obrigações Atrasadas
**GET** `/api/bff/v1/obrigacoes-fiscais/atrasadas`

**Resposta:**
```json
[
  {
    "id": "uuid",
    "idMei": "uuid",
    "obrigacao": "DAS MEI",
    "diaCompetencia": "20",
    "mesAnoCompetencia": "022026",
    "diasAtraso": 5
  }
]
```

### 6. Rotina: Atualização Automática de Status
**Scheduler:** Executar diariamente às 00:00

**Lógica:**
```java
@Scheduled(cron = "0 0 0 * * *")
public void atualizarStatusObrigacoes() {
    // 1. Buscar todas obrigações com status "A vencer"
    // 2. Comparar data de vencimento (dia + mesAnoCompetencia) com data atual
    // 3. Se vencida, atualizar status para "Atrasada"
}
```

### 7. Rotina: Inicialização de Obrigações para Novos MEIs
**Trigger:** Quando um novo MEI é cadastrado

**Lógica:**
- Criar DAS MEI para os próximos 12 meses
- Criar Relatório Mensal para os próximos 12 meses
- Criar DASN-SIMEI para o ano atual (31 de maio)

### 8. Rotina: Virada de Ano Fiscal
**Scheduler:** Executar em 01/01 às 00:00

**Lógica:**
```java
@Scheduled(cron = "0 0 0 1 1 *")
public void inicializarObrigacoesAnoNovo() {
    // 1. Buscar todos os MEIs cadastrados
    // 2. Para cada MEI, criar:
    //    - 12 DAS MEI (dia 20 de cada mês)
    //    - 12 Relatórios Mensais (dia 21 de cada mês)
    //    - 1 DASN-SIMEI (31 de maio)
    // 3. NÃO deletar obrigações existentes
}
```

### 9. Rotina: Fechamento Automático do Relatório Mensal
**Scheduler:** Executar no dia 21 de cada mês às 01:00

**Processo Complexo:**

#### Passo 1: Coleta de Dados
```java
@Scheduled(cron = "0 0 1 21 * *")
public void fecharRelatorioMensal() {
    // 1. Buscar todos os MEIs
    // 2. Para cada MEI, coletar dados do mês anterior:
    //    a. Notas Fiscais emitidas
    //    b. Recibos recebidos
    //    c. Despesas pagas
    //    d. Receitas registradas
}
```

#### Passo 2: Varredura nos Microsserviços
- **Notas Fiscais:** GET `/api/v1/notas?userId={id}&mes={mes}&ano={ano}`
- **Recibos:** GET `/api/v1/recibos?userId={id}&mes={mes}&ano={ano}`
- **Despesas:** GET `/api/v1/despesas?userId={id}&mes={mes}&ano={ano}`
- **Receitas:** GET `/api/v1/receitas?userId={id}&mes={mes}&ano={ano}`

#### Passo 3: Consolidação de PDFs
```java
// 1. Coletar todos os PDFs:
//    - Notas Fiscais: campo pdfUrl
//    - Recibos: campo pdf_recibo
//    - Despesas: campo fotoComprovantePagamentoUrl
//    - Receitas: via idNotaFiscal ou idRecibo

// 2. Mesclar todos os PDFs em um único documento

// 3. Converter para base64

// 4. Salvar no campo pdfRelatorio da MeiObrigacoesFiscais

// 5. Atualizar status para "Em dia"
```

#### Passo 4: Geração do Relatório
```java
public void gerarRelatorioMensal(UUID meiId, LocalDate competencia) {
    // 1. Buscar obrigação "Relatório Mensal" do MEI para a competência
    // 2. Coletar PDFs de todas as fontes
    // 3. Gerar PDF consolidado usando biblioteca como iText ou PDFBox
    // 4. Salvar PDF na obrigação
    // 5. Atualizar status para "Em dia"
    // 6. Criar próxima obrigação de relatório mensal
}
```

## 🏗️ Arquitetura da Solução

### Camada BFF (Backend for Frontend)

#### Controllers
```
MeiObrigacoesFiscaisBffController
├── GET /api/bff/v1/mei/{idMei}/obrigacoes-fiscais
├── GET /api/bff/v1/obrigacoes-fiscais/tipos
├── GET /api/bff/v1/mei/obrigacoes-atrasadas
├── GET /api/bff/v1/obrigacoes-fiscais/atrasadas
└── POST /api/bff/v1/mei/{idMei}/obrigacoes-fiscais/{id}/fechar
```

#### Services
```
ObrigacoesFiscaisService
├── listarObrigacoesPorMei()
├── listarTiposObrigacoes()
├── listarMeisComObrigacoesAtrasadas()
├── listarObrigacoesAtrasadas()
└── fecharObrigacao()
```

#### Clients (Feign)
```
ObrigacoesFiscaisClient → zenmei-mei-api
├── GET /api/v1/obrigacoes-fiscais
├── GET /api/v1/mei/{idMei}/obrigacoes-fiscais
├── POST /api/v1/mei/{idMei}/obrigacoes-fiscais
├── PUT /api/v1/mei/{idMei}/obrigacoes-fiscais/{id}
└── GET /api/v1/mei/obrigacoes-atrasadas

RecibosClient → zenmei-mei-api (ou novo serviço)
├── GET /api/v1/recibos
├── POST /api/v1/recibos
└── GET /api/v1/recibos/{id}
```

### Camada MEI-API (Microserviço)

**Nota:** Como este é um BFF, a implementação das entidades e persistência deve acontecer no microserviço zenmei-mei-api. O BFF apenas agrega e orquestra as chamadas.

#### Entities (no mei-api)
```
ObrigacoesFiscais
MeiObrigacoesFiscais
Recibos
```

#### Repositories (no mei-api)
```
ObrigacoesFiscaisRepository
MeiObrigacoesFiscaisRepository
RecibosRepository
```

#### Services (no mei-api)
```
ObrigacoesFiscaisService
├── inicializarObrigacoesMei()
├── atualizarStatusObrigacoes()
├── fecharObrigacao()
└── gerarRelatorioMensal()

RelatorioMensalService
├── coletarDadosMes()
├── consolidarPDFs()
└── salvarRelatorio()

ScheduledObrigacoesService
├── @Scheduled atualizarStatusDiario()
├── @Scheduled viradaAnoFiscal()
└── @Scheduled fecharRelatoriosMensais()
```

## 🔄 Fluxos de Trabalho

### Fluxo 1: Frontend Busca Obrigações
```
Frontend
   ↓ GET /api/bff/v1/mei/{id}/obrigacoes-fiscais
BFF Controller
   ↓
BFF Service (Circuit Breaker + Retry)
   ↓
Feign Client
   ↓ GET /api/v1/mei/{id}/obrigacoes-fiscais
MEI API
   ↓
Database Query
   ↓
Response (Always 3 obligations, DASN-SIMEI included)
```

### Fluxo 2: Atualização Diária de Status
```
@Scheduled (00:00 daily)
   ↓
MEI API: ScheduledObrigacoesService
   ↓
Buscar obrigações "A vencer"
   ↓
Para cada obrigação:
   - Verificar se data de vencimento < hoje
   - Se sim: status = "Atrasada"
   ↓
Update Database
```

### Fluxo 3: Fechamento Mensal (Dia 21)
```
@Scheduled (01:00 on day 21)
   ↓
MEI API: ScheduledObrigacoesService.fecharRelatoriosMensais()
   ↓
Para cada MEI:
   ↓
   1. Buscar obrigação "Relatório Mensal" do mês
   ↓
   2. Coletar dados:
      - GET Notas Fiscais API
      - GET Recibos API
      - GET Despesas API
      - GET Receitas API
   ↓
   3. RelatorioMensalService.consolidarPDFs()
      - Download de todos os PDFs
      - Merge usando PDFBox
      - Converter para Base64
   ↓
   4. Salvar PDF na MeiObrigacoesFiscais
   ↓
   5. Atualizar status para "Em dia"
   ↓
   6. Criar próxima obrigação (mês seguinte)
```

### Fluxo 4: Inicialização para Novo MEI
```
Novo MEI cadastrado
   ↓ Event/Trigger
ObrigacoesFiscaisService.inicializarObrigacoesMei(meiId)
   ↓
Criar 12 DAS MEI (dia 20, próximos 12 meses)
   ↓
Criar 12 Relatórios Mensais (dia 21, próximos 12 meses)
   ↓
Criar 1 DASN-SIMEI (31 maio, ano atual)
   ↓
Salvar no Database
```

### Fluxo 5: Virada de Ano
```
@Scheduled (00:00 on January 1st)
   ↓
ScheduledObrigacoesService.viradaAnoFiscal()
   ↓
Buscar todos os MEIs ativos
   ↓
Para cada MEI:
   ↓
   Criar obrigações para o novo ano:
   - 12 DAS MEI
   - 12 Relatórios Mensais
   - 1 DASN-SIMEI
   ↓
NÃO deletar obrigações anteriores
   ↓
Salvar no Database
```

## 🛠️ Tecnologias e Bibliotecas

### Já Disponíveis no Projeto
- Spring Boot 3.5.9
- Spring Cloud OpenFeign
- Resilience4j (Circuit Breaker, Retry)
- Java 21
- Lombok

### Novas Dependências Necessárias
```xml
<!-- PDF Processing -->
<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>3.0.1</version>
</dependency>

<!-- Spring Scheduling (já incluído no Spring Boot) -->
<!-- Para @Scheduled annotations -->
```

## 📝 Estrutura de Arquivos a Criar

### No BFF (zenmei-bff-api)
```
src/main/java/br/inf/softhausit/zenite/zenmei/bff/
├── controller/
│   └── MeiObrigacoesFiscaisBffController.java
├── service/
│   └── ObrigacoesFiscaisService.java
├── client/
│   ├── ObrigacoesFiscaisClient.java
│   └── RecibosClient.java
└── dto/ (se necessário para transformações)
    ├── ObrigacaoFiscalResponse.java
    └── MeiObrigacoesAtrasadasResponse.java
```

### No MEI-API (zenmei-mei-api) - Fora do escopo do BFF
```
src/main/java/br/inf/softhausit/zenite/zenmei/mei/
├── entity/
│   ├── ObrigacoesFiscais.java
│   ├── MeiObrigacoesFiscais.java
│   └── Recibos.java
├── repository/
│   ├── ObrigacoesFiscaisRepository.java
│   ├── MeiObrigacoesFiscaisRepository.java
│   └── RecibosRepository.java
├── service/
│   ├── ObrigacoesFiscaisService.java
│   ├── RelatorioMensalService.java
│   └── ScheduledObrigacoesService.java
├── controller/
│   ├── ObrigacoesFiscaisController.java
│   └── RecibosController.java
└── dto/
    ├── ObrigacaoFiscalDTO.java
    └── ReciboDTO.java
```

## ⚠️ Considerações Importantes

### 1. BFF vs Microserviço
**Decisão Arquitetural:**
- O BFF não deve ter persistência própria
- Toda a lógica de negócio e persistência deve estar no microserviço mei-api
- O BFF apenas agrega, orquestra e adiciona resiliência

### 2. Schedulers
- Os @Scheduled devem estar no microserviço mei-api, não no BFF
- O BFF não deve ter rotinas agendadas de negócio

### 3. Tratamento de PDFs
- PDFs grandes em base64 podem causar problemas de memória
- Considerar:
  - Limitar tamanho total do PDF consolidado
  - Usar storage externo (S3, MinIO) e armazenar apenas URL
  - Implementar paginação se necessário

### 4. Performance
- Consolidação de PDFs pode ser lenta
- Executar em background/async
- Implementar fila (RabbitMQ, Kafka) se necessário

### 5. Transações
- Fechamento de obrigação e criação da próxima devem ser atômicos
- Usar @Transactional adequadamente

### 6. Idempotência
- Schedulers podem executar múltiplas vezes
- Implementar verificações para evitar duplicação:
  - Verificar se obrigação já foi criada
  - Verificar se relatório já foi gerado

### 7. Testes
- Criar testes unitários para serviços
- Criar testes de integração para schedulers
- Mockar chamadas externas em testes

## 🎯 Estratégia de Implementação

### Fase 1: Estrutura Básica (BFF)
1. Criar client interfaces (Feign)
2. Criar services com circuit breaker
3. Criar controllers BFF
4. Adicionar configuração no application.yml

### Fase 2: Microserviço (mei-api) - Nota: Fora do escopo do BFF
1. Criar entities
2. Criar repositories
3. Criar services básicos
4. Criar controllers
5. Criar scripts de inicialização do banco

### Fase 3: Lógica de Negócio (mei-api)
1. Implementar inicialização de obrigações
2. Implementar lógica de renovação automática
3. Implementar atualização de status
4. Implementar entidade Recibos

### Fase 4: Schedulers (mei-api)
1. Implementar atualização diária de status
2. Implementar virada de ano fiscal
3. Implementar fechamento mensal (sem PDFs)

### Fase 5: Processamento de PDFs (mei-api)
1. Adicionar dependência PDFBox
2. Implementar serviço de consolidação
3. Integrar com scheduler de fechamento mensal
4. Testar com volumes reais

### Fase 6: Integração e Testes
1. Testes de integração end-to-end
2. Testes de carga
3. Ajustes de performance
4. Documentação

## 🔐 Segurança

### Considerações
1. Validar que MEI só acessa suas próprias obrigações
2. PDFs podem conter informações sensíveis
3. Logs não devem expor dados sensíveis
4. Implementar rate limiting para endpoints de lista

## 📊 Monitoramento

### Métricas a Acompanhar
1. Tempo de execução dos schedulers
2. Taxa de sucesso/falha na consolidação de PDFs
3. Número de obrigações atrasadas por MEI
4. Tempo de resposta dos endpoints
5. Circuit breaker status

### Alertas
1. Alerta se scheduler falhar
2. Alerta se muitos MEIs com obrigações atrasadas
3. Alerta se consolidação de PDF falhar repetidamente

## 🚀 Próximos Passos

### Imediatos
1. ✅ Criar este documento de Chain of Thought
2. ⏳ Aguardar aprovação/feedback do usuário
3. ⏳ Começar implementação conforme fases definidas

### Futuro
1. Implementar notificações push para MEIs
2. Dashboard de obrigações fiscais
3. Relatórios analíticos
4. Integração com sistemas da Receita Federal

## 📌 Conclusão

Esta implementação requer:
- **Complexidade Alta:** Múltiplos sistemas, schedulers, processamento de PDFs
- **Tempo Estimado:** 2-3 semanas para implementação completa
- **Recursos:** Conhecimento em Spring, schedulers, PDFs, microserviços
- **Riscos:** Performance com PDFs, sincronização de schedulers, integridade de dados

**Abordagem Recomendada:**
- Implementar em fases incrementais
- Testar cada fase antes de avançar
- Começar sem processamento de PDF, adicionar depois
- Priorizar funcionalidades core antes das automatizações

---

**Documento criado por:** JamesCoder, the man!  
**Data:** 2026-01-22  
**Status:** Aguardando validação para prosseguir com implementação

Wood in the Machine! 🪵🤖
