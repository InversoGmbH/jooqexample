# jooqexample
[Jooq](https://www.jooq.org/learn/) is a Framework to write SQL - Statements in Java. It is much better than writing Strings in Java code.

## What is this example for
This is not a create-fresh-project example. If you want to see that just look at the jooq website.
This example is assumes you already have a project with configured persistence layer and want to integrate jooq in your project.

## Basic situation
We have a JPA - Application. The mapping Classes are in the package `de.inverso.jooqexample.model`.
Our Database is created and updated by [Hibernate](http://hibernate.org/). In this case we use a [H2](https://www.h2database.com/html/main.html) Database.

## Why should I use it?
### I'm just using JDBC
When using JDBC working with prepared statements is needed. Most of the time it will be written in strings.
E.g. you want to know who has the most bank details. In this case you produce a query as you see in `de.inverso.jooqexample.testcases.UseCaseTest.invalidSqLTest`.
You may notice multiple syntax errors in this statement. If not you will notice it when you run the application. And thats the main weakness of this approach. 
Jooq allows to write SQL querys with the benefit of syntax checking with the java compiler.

### I already have JPA, so why should I use it?
If you just write a CRUD application it just depends your favorite way to access your database.
But there exist some usecases in which native querys are more efficient.  

For example:  Assume we want to get all Products per Request as concatinated String. JPQL does not provide an operation for this.
A JPA example is provided in `de.inverso.jooqexample.testcases.UseCaseTest.productsPerRequestJpaTest`.
The application will now fetch all Requests and their products. But we only need the id and the productnames as concated string.
Every other fetched data is useless.

Databases provide functions like `listagg` to achieve the same.
The testcase `de.inverso.jooqexample.testcases.UseCaseTest.productsPerRequestJooqTest` shows the same result. 
This will fetch the information we need direct from the database. 