# Política de Branches e Convenção de Commits

Este documento descreve a convenção de branches e o formato de mensagens de commit recomendado para o repositório "GCS Microserviço Java".

> Observação: a branch de integração no repositório atual é `develop`. Recomenda-se manter uma branch de release estável `main` (ou `master`) protegida para deploys em produção.

## Objetivos

- Ter nomes de branches previsíveis e legíveis.
- Garantir histórico de commits claro e automatizável (release semântica, changelogs, CI).
- Facilitar revisão por pares e fluxo de PRs controlado.

## Tipos de branches e convenção de nomes

- main (ou master)
  - Branch de produção; somente receberá merges via PRs aprovados e CI verde.
- develop
  - Branch de integração/integração contínua — alvo padrão para features e fixes.
- feature/<nome>
  - Novas funcionalidades. Ex.: `feature/login-oauth2`.
- fix/<nome> or bugfix/<nome>
  - Correção de bugs não críticos. Ex.: `fix/nullpointer-controller`.
- hotfix/<nome>
  - Correções emergenciais aplicadas diretamente sobre `main` e depois mescladas para `develop`. Ex.: `hotfix/timeout-db`.
- release/<versão>
  - Preparação para release (ajustes de documentação, bump de versão). Ex.: `release/1.2.0`.
- chore/<nome>
  - Tarefas de manutenção que não alteram comportamento (deps, config). Ex.: `chore/update-deps`.
- docs/<nome>
  - Alterações na documentação.

Regras rápidas:
- Use `-` (hífen) ou `/` para separar palavras, evite espaços e caracteres especiais.
- Mantenha o nome curto e significativo (máx. ~50 caracteres quando possível).

## Fluxo de trabalho (exemplo)

1. Crie uma branch a partir de `develop` para novas features:

   git checkout develop
   git pull origin develop
   git checkout -b feature/nome-curto

2. Faça commits seguindo o Conventional Commits (veja abaixo).
3. Abra um Pull Request (PR) contra `develop` (ou `main` para hotfixes/patches críticos).
4. Requerimentos para merge:
   - CI verde
   - Pelo menos 1 (ou 2) reviews aprovados (configurável)
   - Nenhum comentário crítico pendente
5. Use squash-and-merge ou merge com padrão escolhido pela equipe; mantenha mensagens de PR/merge compatíveis com conventional commits quando possível.

## Convenção de commits (Conventional Commits)

Formato básico:

  <type>[optional scope]: <short description>

Exemplos:

- feat(auth): add OAuth2 login
- fix(user): handle null pointer in user service
- docs(readme): update docker instructions
- chore(deps): bump spring-boot to 3.1.0

Tipos recomendados:

- feat: nova funcionalidade
- fix: correção de bug
- docs: mudanças na documentação
- style: formatação, ponto-e-vírgula, semântica do código que não altera comportamento
- refactor: refatoração de código que não adiciona nem corrige comportamento
- perf: mudanças que melhoram performance
- test: adição/ajuste de testes
- build: alterações em scripts de build/configuração (ex.: Maven, Gradle)
- ci: mudanças na configuração de CI
- chore: tarefas de manutenção
- revert: reverte um commit anterior

Breaking changes:
- Para mudanças que quebram compatibilidade, inclua `BREAKING CHANGE:` no corpo do commit ou utilize `!` após o type/scope:

  feat!: drop support for legacy API

ou

  feat(auth): remove legacy tokens

  BREAKING CHANGE: endpoint /v1/token foi removido; use /v2/token

Rodar ferramentas de lint de commit ajuda a garantir conformidade (ex.: commitlint, Husky, Commitizen).

## Mensagens de PR

- Título do PR: idealmente um commit resumido no formato conventional (ex.: `feat(payment): add stripe integration`).
- Descrição PR: contexto, referências a issues (ex.: `Closes #123`), passos para testar, screenshots se aplicável.

## Política de merge

- Não permitir pushes diretos em `main` ou `develop`.
- Exigir PRs com pelo menos um aprovador e CI verde.
- Preferir squash merges para manter histórico limpo ou usar um padrão que a equipe concordar.

## Integração com CI/CD e versionamento semântico

- Recomendado usar ferramentas de automação que leiam conventional commits para gerar changelogs e tags semânticas (ex.: semantic-release).
- Tags de release devem seguir SemVer (MAJOR.MINOR.PATCH).

## Exemplos de commits úteis

- feat(api): add /users endpoint
- fix(cache): fix cache key collision
- docs: update README with Docker usage
- chore: run format on src/

## Ferramentas sugeridas (opcionais)

- commitlint + @commitlint/config-conventional
- husky para hooks (ex.: checar mensagens antes do commit)
- commitizen para mensagens guiadas
- semantic-release para automação de releases e changelogs

## Recomendações finais

- Documente no repositório (este arquivo) e habilite proteções de branch no GitHub (branch protection rules): exigir PRs, CI verde, revisores, e proibir push direto.
- Automatize validações locais e no CI (commitlint, testes) para manter qualidade.

---

Se desejar, eu posso:

- Adicionar um `commitlint` + `husky` mínimo ao projeto.
- Criar um template de PR (`.github/PULL_REQUEST_TEMPLATE.md`) para padronizar descrições.
- Gerar um exemplo de workflow GitHub Actions que roda testes e valida commit messages.
