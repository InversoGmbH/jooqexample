package de.inverso.jooqexample.testcases;

import javax.persistence.EntityManager;

import org.jooq.impl.DSL;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.DatabaseUtil;

import static de.inverso.jooqexample.gen.tables.Person.*;
import de.inverso.jooqexample.gen.tables.records.PersonRecord;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InteractionTest extends AbstractTest {

	@Test
	@Order(1)
	public void insertTest() {
		PersonRecord p = new PersonRecord();
		p.setFirstname("Franz");
		p.setLastname("Huber");

		EntityManager entityManager = em();
		entityManager.getTransaction().begin();
		DatabaseUtil.<Void>executeQuery(entityManager, connection -> {
			DSL.using(connection). //
			insertInto(PERSON). //
			set(p). //
			execute();
			return null;
		});
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Test
	@Order(2)
	public void deleteTest() {
		
		EntityManager entityManager = em();
		entityManager.getTransaction().begin();
		DatabaseUtil.<Void>executeQuery(entityManager, connection -> {
			DSL.using(connection).deleteFrom(PERSON). //
			where(PERSON.FIRSTNAME.eq("Franz").//
			and(PERSON.LASTNAME.eq("Huber"))).//
			execute();
			return null;
		});
		entityManager.getTransaction().commit();		
	}

}
