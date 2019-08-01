package de.inverso.jooqexample.testcases;

import javax.persistence.EntityManager;

import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.DatabaseUtil;
import de.inverso.jooqexample.gen.tables.Person;
import de.inverso.jooqexample.gen.tables.records.PersonRecord;

public class InterActionTest extends AbstractTest {

	@Test
	public void insertTest() {
		PersonRecord p = new PersonRecord();
		p.setFirstname("Franz");
		p.setLastname("Huber");
		

		EntityManager entityManager = em();
		DatabaseUtil.<Void>executeQuery(entityManager, connection -> {			
			DSL.using(connection). //
			insertInto(Person.PERSON). //
			set(p). //
			execute();
			return null;
		});
	}

}
