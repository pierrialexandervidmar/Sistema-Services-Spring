# Sistema de Serviços

![Sistema-Servico](https://github.com/pierrialexandervidmar/Sistema-Services-Spring/assets/155840339/7822832f-e9df-4217-8d3f-d25bdc2e5b0f)

> Aplicação que permite além de inserir, atualizar, excluir, cancelar, informar data de pagamento, inicio e término. Consultar por Status, e por datas, seja ela de pagamento, inicio ou término.
> A API possui paginação em seus endpoints, hoje, é solicitado de 15 em 15. 
> O foco deste desenvolvimento foi na API Java Spring. FrontEnd foi criado para apenas testes de consumo fora de um simples cliente HTTP como Insomnia. Portanto a aplicação React poderia conter inúmeras melhorias em sua arquitetura.

### Ajustes e melhorias

O projeto ainda está em desenvolvimento e as próximas atualizações serão voltadas nas seguintes tarefas:

- [ ] Tarefa 1: Não permitir que data do término seja menor que data de início.
- [ ] Tarefa 2: Status de "realizado pagamento parcial" ou somente aceitar pagamento integral. Se tiver pagamento parcial, gera nova coluna com valor restante.


## 💻 Pré-requisitos

Antes de começar, verifique se você atendeu aos seguintes requisitos:

- Você instalou a versão mais recente do `Java 17 | NodeJS`
- Banco de dados `MySQL`. Ou mudar o driver em `application.properties`
- Editar `application.properties` com nome do banco de dados desejado, username e password.

## ☕ Usando Sistema de Serviços

Para usar Sistema de Serviços, siga estas etapas:

Front-End
```
npm install
npm start
```

Backend - no IntelliJ ou Eclipse:
```
run > java application
```
