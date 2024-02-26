# Documentando e Testando Sua API Rest com Kotlin
Solução do Desafio de Projeto: "Documentando e Testando Sua API Rest com Kotlin" do Bootcamp NTT Data - Desenvolvimento Backend com Kotlin  
Este desafio propõe a criação de documentação e de testes para uma API Rest em Kotlin.  
A API foi desenvolvida com base na aula "Criando uma API Rest com Kotlin e Persistência de Dados"

A API foi criada para poder cadastrar Clientes e Créditos, onde os clientes possuem um ou mais créditos vinculados a eles.  
A sua estrutura foi criada usando Spring Boot, JPA, Banco de Dados H2 e Flyway Migrations.  
Os testes por sua vez foram feitos com JUnit5, AssertJ e MockK. Foi usado também o Swagger para criar a documentação da API. 

O projeto foi desenvolvido seguindo os vídeos das aulas, tanto na aula de API como nos vídeos do desafio em si.

Além dos testes apresentados nos vídeos, criados para as classes CustomerController, CustomerService e CreditRepository, foram criados também testes para as classes CreditController e CreditService, de forma a cobrir 100% do projeto por testes.  
Além disso, a documentação da API foi expandida para conter uma descrição para todos os schemas e métodos.

