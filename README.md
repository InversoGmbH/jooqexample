# jooqexample
[Jooq](https://www.jooq.org/learn/) is a Framework to write SQL - Statements in Java. It is much better than writing Strings in Java code.

## What is this example for
This is not a create-fresh-project example. If you want to see that just look at the jooq website.
This example is assumes you already have a project with configured persistence layer and want to integrate jooq in your project.

## Basic situation
We have a JPA - Application. The mapping Classes are in the package `de.inverso.jooqexample.model`.
Our Database is created and updated by [Hibernate](http://hibernate.org/). In this case we use a [H2](https://www.h2database.com/html/main.html) Database.

## Why should I use it?
If you just write a CRUD application it just depends your favorite way to access your database.
But there exist some usecases which need native querys. Most of the time marketing selections or statistics. Its also possible that you need to use some specific functions of your database. 

E.g. you want to know who has the most bank details. In this case you produce a query as you see in `de.inverso.jooqexample.testcases.InvalidSqlTest`.

You may notice multiple syntax errors in this statement. If not you will notice it when you run the application. And thats the main weakness of this approach. 
Jooq allows to write SQL querys with the benefit of syntax checking with the java compiler.
 