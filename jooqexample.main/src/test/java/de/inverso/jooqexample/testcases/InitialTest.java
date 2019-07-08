package de.inverso.jooqexample.testcases;


import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.model.Person;

import org.junit.jupiter.api.*;


import javax.persistence.EntityManager;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author fabian
 * on 17.05.19.
 */
public class InitialTest extends AbstractTest {

    @Test
    @Tag("fast")
    public void connectionTest() {
        final EntityManager entityManager = em();
        entityManager.getTransaction().begin();
        List<Person> persons = entityManager.createNamedQuery("persons", Person.class).getResultList();
        Logger.getAnonymousLogger().info(persons.toString());

        Assertions.assertEquals(PERSON_AMOUNT, persons.size());

        entityManager.flush();
        entityManager.close();
    }

    @Test
    @Tag("fast")
    public void bankDetailsTest() {
        final EntityManager entityManager = em();
        entityManager.getTransaction().begin();
        Person person = entityManager.createNamedQuery("persons", Person.class).getResultList().stream().findFirst().get();
        Logger.getAnonymousLogger().info(person.getBankDetails().toString());
        entityManager.getTransaction().commit();
        entityManager.close();
    }




}
