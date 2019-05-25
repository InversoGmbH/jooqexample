# jooqexample
[Jooq](https://www.jooq.org/learn/) is a Framework to write SQL - Statements in Java. It is much better than writing Strings in Java code.

## What is this example for
This is not a create-fresh-project example. If you want to see that just look at the jooq website.
This example is assums you allready have a project with configured persistence layer and want to integrate jooq in your project.

## Basic situation
We have a JPA - Application. The mapping Classes are in the package `de.inverso.jooqexample.model`.
Our Database is created and updated by [Hibernate](http://hibernate.org/). In this case we use a [H2](https://www.h2database.com/html/main.html) Database.
