# 📤 PUSH PARA REPOSITÓRIO REMOTO

## ✅ Commits Realizados com Sucesso!

Todos os commits do refactor User → Mei foram realizados localmente.

---

## 📋 COMMITS CRIADOS

```
✅ Commit 1: chore: adicionar .gitignore
✅ Commit 2: refactor(model): renomear User para Mei
✅ Commit 3: refactor(mei-api): renomear projeto user-api para mei-api
✅ Commit 4: refactor(bff): atualizar referências de user-api para mei-api
✅ Commit 5: refactor(nota-api): atualizar interop de user para mei
✅ Commit 6: docs: adicionar documentação completa do refactor
✅ Tag: v1.0.0-refactor-user-to-mei
```

---

## 🚀 COMO FAZER PUSH PARA O REMOTO

### 1. Adicionar Repositório Remoto

Se você ainda não tem um repositório remoto configurado:

```bash
cd /home/t102640/Desenvolvimento/zenmei

# GitHub
git remote add origin https://github.com/seu-usuario/zenmei.git

# GitLab
git remote add origin https://gitlab.com/seu-usuario/zenmei.git

# Bitbucket
git remote add origin https://bitbucket.org/seu-usuario/zenmei.git

# Azure DevOps
git remote add origin https://dev.azure.com/sua-org/zenmei/_git/zenmei
```

### 2. Verificar Repositório Remoto

```bash
git remote -v
```

### 3. Fazer Push do Branch Principal

```bash
# Push do branch main/master
git push -u origin main

# Ou se for master
git push -u origin master
```

### 4. Fazer Push das Tags

```bash
# Push de todas as tags
git push --tags

# Ou push da tag específica
git push origin v1.0.0-refactor-user-to-mei
```

---

## 📊 VERIFICAR STATUS

```bash
# Ver logs
git log --oneline --graph -10

# Ver tags
git tag -l

# Ver branches
git branch -a

# Ver remotos
git remote -v
```

---

## 🔄 CRIAR BRANCH DE DESENVOLVIMENTO (Opcional)

Se você quiser trabalhar em uma branch separada:

```bash
# Criar branch develop a partir do main
git checkout -b develop

# Push da branch develop
git push -u origin develop

# Criar branch para o refactor
git checkout -b feature/refactor-user-to-mei

# Push da feature branch
git push -u origin feature/refactor-user-to-mei
```

---

## 🌿 ESTRATÉGIA DE BRANCHES RECOMENDADA

### GitFlow

```
main (produção)
  ├── develop (desenvolvimento)
  │   ├── feature/refactor-user-to-mei
  │   ├── feature/nova-funcionalidade
  │   └── ...
  ├── release/v1.0.0
  └── hotfix/correcao-critica
```

### Comandos

```bash
# Branch principal (produção)
git checkout main

# Branch de desenvolvimento
git checkout -b develop
git push -u origin develop

# Features a partir de develop
git checkout develop
git checkout -b feature/nome-da-feature
git push -u origin feature/nome-da-feature

# Releases
git checkout develop
git checkout -b release/v1.0.0
git push -u origin release/v1.0.0

# Merge para main após testes
git checkout main
git merge release/v1.0.0
git tag v1.0.0
git push origin main --tags
```

---

## 🔐 AUTENTICAÇÃO

### GitHub - Personal Access Token

1. Ir em: https://github.com/settings/tokens
2. Generate new token (classic)
3. Selecionar scopes: `repo`, `workflow`
4. Copiar o token
5. Usar no push:

```bash
# Quando pedir senha, usar o token
git push -u origin main
```

### SSH (Recomendado)

```bash
# Gerar chave SSH
ssh-keygen -t ed25519 -C "dev@softhausit.com.br"

# Adicionar ao ssh-agent
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519

# Copiar chave pública
cat ~/.ssh/id_ed25519.pub

# Adicionar no GitHub/GitLab em Settings > SSH Keys

# Mudar remote para SSH
git remote set-url origin git@github.com:seu-usuario/zenmei.git
```

---

## 📝 CRIAR PULL REQUEST (Opcional)

Se você estiver trabalhando em equipe:

1. Fazer push da sua branch:
```bash
git push -u origin feature/refactor-user-to-mei
```

2. Ir no GitHub/GitLab e criar Pull Request/Merge Request

3. Adicionar descrição:
```markdown
# Refactor: User → Mei

## 📋 Resumo
Refactor completo renomeando User para Mei em todo o sistema.

## 🔄 Alterações
- Renomeação da entidade User para Mei
- Renomeação do projeto user-api para mei-api
- Atualização de todos os microsserviços
- BFF completamente atualizado
- Documentação completa incluída

## ⚠️ Breaking Changes
- Tabela do banco: users → meis (requer migração)
- Endpoints BFF: /users → /meis
- Nome do projeto alterado

## ✅ Checklist
- [x] Código refatorado
- [x] Documentação atualizada
- [x] Scripts de migração criados
- [ ] Testes executados
- [ ] Code review realizado
- [ ] Aprovação do time

## 📚 Documentação
Ver arquivos:
- REFACTOR_USER_TO_MEI_REPORT.md
- REFACTOR_SUMMARY.md
- DATABASE_MIGRATION_SCRIPT.md
```

---

## 🚨 ATENÇÃO ANTES DO PUSH

### Verificar Antes de Push

```bash
# 1. Verificar o que vai ser enviado
git log --oneline --graph -10

# 2. Verificar arquivos commitados
git show --stat

# 3. Verificar diferenças
git diff origin/main..HEAD

# 4. Verificar se não há arquivos sensíveis
git log --all --full-history -- "*.key" "*.pem" "*.env"
```

### Remover Arquivo Sensível (Se Necessário)

Se você commitou algum arquivo sensível por engano:

```bash
# Remover arquivo do histórico
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch arquivo-sensivel.key" \
  --prune-empty --tag-name-filter cat -- --all

# Forçar push
git push origin --force --all
```

---

## 📊 COMANDOS ÚTEIS

```bash
# Ver status
git status

# Ver últimos commits
git log --oneline -10

# Ver diferenças
git diff

# Desfazer último commit (manter alterações)
git reset --soft HEAD~1

# Desfazer último commit (descartar alterações)
git reset --hard HEAD~1

# Criar branch a partir de commit específico
git checkout -b hotfix/correcao abc123

# Ver histórico de um arquivo
git log --follow -- arquivo.java

# Ver quem alterou cada linha
git blame arquivo.java
```

---

## 🎯 EXEMPLO COMPLETO

```bash
# 1. Verificar estado atual
cd /home/t102640/Desenvolvimento/zenmei
git status
git log --oneline -5

# 2. Adicionar remoto (se não existir)
git remote add origin https://github.com/seu-usuario/zenmei.git

# 3. Verificar remoto
git remote -v

# 4. Push do branch principal
git push -u origin main

# 5. Push das tags
git push --tags

# 6. Verificar no GitHub/GitLab
# Abrir navegador e verificar repositório
```

---

## ✅ APÓS O PUSH

1. **Verificar no GitHub/GitLab**: Confirmar que todos os commits apareceram
2. **Verificar Tags**: Confirmar que a tag v1.0.0-refactor-user-to-mei está visível
3. **Criar Release**: Opcional - criar release no GitHub/GitLab
4. **Notificar Equipe**: Avisar sobre o refactor e breaking changes
5. **Atualizar CI/CD**: Se houver pipeline, atualizar configs

---

## 🔗 LINKS ÚTEIS

- [Git Documentation](https://git-scm.com/doc)
- [GitHub Guides](https://guides.github.com/)
- [GitLab Documentation](https://docs.gitlab.com/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)

---

## 📞 SUPORTE

Para dúvidas sobre o processo de Git:
- 📧 Email: dev@softhausit.com.br
- 📚 Documentação do projeto: Ver README.md

---

**Preparado por**: ZenMEI Development Team  
**Data**: 21 de Janeiro de 2026

**✅ COMMITS REALIZADOS E PRONTOS PARA PUSH!**
