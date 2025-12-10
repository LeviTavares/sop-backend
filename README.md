
# SOP - Back-end (Spring Boot)

API para controle financeiro (Despesa, Empenho, Pagamento).

## Tecnologias
- Spring Boot
- Spring Data JPA
- Bean Validation
- PostgreSQL
- DTOs

## Executando
1. Suba o PostgreSQL (ex.: Docker):
```bash
docker run --name sop-pg -e POSTGRES_USER=sop -e POSTGRES_PASSWORD=sop   -e POSTGRES_DB=sop_db -p 5432:5432 -d postgres:16
```
2. Configure `src/main/resources/application.yml` com suas credenciais.
3. Rode a aplicação:
```bash
mvn spring-boot:run
```

## Endpoints principais (com paginação)
- **Despesas:** `GET /api/despesas?page=0&size=20&tipoDespesa=OBRA_DE_RODOVIAS&credor=ACME&numeroProtocolo=43022.123456/2025-01&dataInicio=01/01/2025&dataFim=31/12/2025`
- **Empenhos:** `GET /api/empenhos?page=0&size=20&despesaId=1&numeroEmpenho=2025NE0003&dataInicio=01/01/2025&dataFim=31/12/2025`
- **Pagamentos:** `GET /api/pagamentos?page=0&size=20&empenhoId=1&numeroPagamento=2025NP0012&dataInicio=01/01/2025&dataFim=31/12/2025`

## Regras de negócio
- Número de protocolo/empenho/pagamento únicos (com regex de formato).
- Soma dos empenhos não pode exceder valor da despesa.
- Soma dos pagamentos não pode exceder valor do empenho.
- Não excluir despesa com empenhos, nem empenho com pagamentos.

## Scripts SQL
Veja `src/main/resources/schema.sql`.
