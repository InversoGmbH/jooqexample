package de.inverso.jooqexample.testcases;


import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.payment.IBAN;
import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.model.Antrag;
import de.inverso.jooqexample.model.Bankverbindung;
import de.inverso.jooqexample.model.Person;

import de.inverso.jooqexample.model.Vermittler;
import org.junit.jupiter.api.*;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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

        Assertions.assertEquals(1000, persons.size());

        entityManager.flush();
        entityManager.close();
    }

    @Test
    @Tag("fast")
    public void bankverbindungTest() {
        final EntityManager entityManager = em();
        entityManager.getTransaction().begin();
        Person person = entityManager.createNamedQuery("persons", Person.class).getResultList().stream().findFirst().get();
        Logger.getAnonymousLogger().info(person.getBankverbindungen().toString());
        entityManager.getTransaction().commit();
        entityManager.close();
    }


    @BeforeAll
    static void setup() {
        Random rn = new Random();
        int randomMin = 1;
        int randomMax = 10;

        initPersons(rn, randomMin, randomMax);

        final Fairy fairy = getFairy();
        final List<String> vermittlernummern = new LinkedList<>();
        for(int i = 0; i<50; i++){
            vermittlernummern.add(fairy.baseProducer().numerify("#############"));
        }
        initAntrag(fairy, vermittlernummern);
        final EntityManager e = em();
        e.getTransaction().begin();
        vermittlernummern.stream().map(nummer -> {
            return createVermittler(fairy.person(), nummer);
        }).forEach(e::persist);
        final Vermittler vermittler = createVermittler(fairy.person(), fairy.baseProducer().numerify("#############"));
        vermittler.setFirstName("Thomas");
        e.persist(vermittler);
        e.getTransaction().commit();
        e.close();
    }

    private static void initAntrag(Fairy fairy, List<String> vermittlernummern) {
        final EntityManager e = em();
        e.getTransaction().begin();
        final List<Person> persons = e.createNamedQuery("persons", Person.class).getResultList();
        for(int i = 0; i< fairy.baseProducer().randomBetween(10,1000); i++) {
           Antrag a = new Antrag();
           a.setAntragsnummer(fairy.baseProducer().numerify("SA###########"));;
           a.setPerson(fairy.baseProducer().randomElement(persons));
           a.setVermittlernummer(fairy.baseProducer().randomElement(vermittlernummern));
           e.persist(a);
       }
        e.getTransaction().commit();
        e.close();
    }

    // @AfterAll
    static void tearDown() {
        EntityManager e = em();
        e.getTransaction().begin();
        final List<Person> persons = e.createNamedQuery("persons", Person.class).getResultList();
        persons.stream().forEach(e::remove);
        e.flush();
        e.close();
    }

    private static void initPersons(Random rn, int randomMin, int randomMax) {
        EntityManager e = em();
        EntityTransaction transaction = e.getTransaction();
        Fairy fairy = getFairy();

        transaction.begin();
        for (int i = 0; i < 1000; i++) {
            final int random = rn.nextInt(randomMax - randomMin + 1);
            Person p = createPerson(fairy.person());
            List<Bankverbindung> bankverbindung = new ArrayList<>();
            for(int j = 0 ; j<random; j++) {
                bankverbindung.add(createBankVerbindung(fairy.iban()));
            }
            p.setBankverbindungen(bankverbindung);
            e.persist(p);
        }
        transaction.commit();
        e.close();
    }

    private static Fairy getFairy() {
        return Fairy.create(Locale.GERMANY);
    }

    private static Bankverbindung createBankVerbindung(IBAN iban) {
        return new Bankverbindung(iban.getIbanNumber(), iban.getBban());
    }

    private static Person createPerson(com.devskiller.jfairy.producer.person.Person person) {
        return new Person(person.getFirstName(), person.getLastName());
    }

    private static Vermittler createVermittler(com.devskiller.jfairy.producer.person.Person person, String vermittlerNummer){
        return new Vermittler(person.getFirstName(), person.getLastName(), vermittlerNummer);
    }
}
