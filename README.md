
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
# Tarefando

Tarefando é uma aplicação de gerenciamento de tarefas que ajuda você a acompanhar sua produtividade de forma visual e gamificada. O objetivo é criar um sistema que permita organizar tarefas, definir prioridades, estimar tempo gasto e categorizar as atividades por etiquetas. 

A partir dessas informações, a aplicação gerará gráficos que ajudarão a visualizar a produtividade ao longo do tempo, tornando o processo de realização de tarefas mais divertido e engajador.

## Objetivo do Projeto
O **Tarefando** nasce para resolver um problema pessoal do seu criador, **[Victor](https://github.com/Victor-Saraiva-P)**, que precisa de uma plataforma de tarefas com todas as funcionalidades que julga essenciais. Muitas opções existentes são pagas ou não possuem todos os recursos desejados, o que motivou o desenvolvimento deste projeto.
## Funcionalidades Principais
- Criar, editar e deletar tarefas (**CRUD completo**).
- Definir **tempo estimado** para cada tarefa.
- Adicionar **etiquetas** para classificação das tarefas.
- Definir **prioridades** (P1 a P4).
- Gerar **gráficos e relatórios** para acompanhamento da produtividade.
- Exibir **tarefas do dia**.
- Gamificação para tornar a gestão de tarefas mais motivadora.

## Stack utilizada
### Backend
O backend da aplicação é desenvolvido utilizando:
- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.4.3
- **Banco de Dados**: PostgreSQL
- **Tipo de API**: REST
- **Autenticação**: JWT

### Frontend
O frontend será desenvolvido com:
- **Framework**: Angular

## Rodando localmente
### Requisitos
- **Java 21**
- **Maven**
- **PostgreSQL**
- **Angular CLI** (para rodar o frontend)

### Como Rodar o Backend
1. Clone este repositório:
   ```sh
   git clone https://github.com/Victor-Saraiva-P/Tarefando
   cd tarefando
   ```
2. Configure o banco de dados PostgreSQL e ajuste as credenciais no `application.properties`.
3. Compile e execute a aplicação:
   ```sh
   mvn spring-boot:run
   ```

### Como Rodar o Frontend
1. Acesse a pasta do frontend:
   ```sh
   cd frontend
   ```
2. Instale as dependências:
   ```sh
   npm install
   ```
3. Execute a aplicação:
   ```sh
   ng serve
   ```

## Aprendizados

O que você aprendeu construindo esse projeto? Quais desafios você enfrentou e como você superou-os?


## 👨‍💻 Autor

Desenvolvido por **[Victor Saraiva](https://github.com/Victor-Saraiva-P)**
