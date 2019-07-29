package de.inverso.jooqexample.testcases;


import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.model.Person;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.persistence.EntityManager;
import java.util.*;


/**
 * @author fabian
 * on 17.05.19.
 */
public class InitialTest extends AbstractTest {

    private static Logger LOGGER = LoggerFactory.getLogger(InitialTest.class);

    @Test
    public void connectionTest() {
        final EntityManager entityManager = em();
        entityManager.getTransaction().begin();
        List<Person> persons = entityManager.createNamedQuery("persons", Person.class).getResultList();
        LOGGER.info(persons.toString());

        Assertions.assertEquals(PERSON_AMOUNT, persons.size());

        entityManager.flush();
        entityManager.close();
    }

    @Test
    public void bankDetailsTest() {
        final EntityManager entityManager = em();
        entityManager.getTransaction().begin();
        Person person = entityManager.createNamedQuery("persons", Person.class).getResultList().stream().findFirst().get();
        LOGGER.info(person.getBankDetails().toString());
        entityManager.getTransaction().commit();
        entityManager.close();
    }




}
