package de.inverso.jooqexample;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.payment.IBAN;

import de.inverso.jooqexample.model.BankDetails;
import de.inverso.jooqexample.model.Request;

import de.inverso.jooqexample.model.Person;
import de.inverso.jooqexample.model.Broker;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.*;


/**
 * @author fabian
 * on 17.05.19.
 */
public abstract class AbstractTest {

    protected static EntityManager em() {
        return DatabaseUtil.getEntityManager();
    }


    private static void initPersons(Random rn, int randomMin, int randomMax) {
        EntityManager e = em();
        EntityTransaction transaction = e.getTransaction();
        Fairy fairy = getFairy();

        transaction.begin();
        for (int i = 0; i < 1000; i++) {
            final int random = rn.nextInt(randomMax - randomMin + 1);
            Person p = createPerson(fairy.person());
            List<BankDetails> bankDetails = new ArrayList<>();
            for(int j = 0 ; j<random; j++) {
                bankDetails.add(createBankDetails(fairy.iban()));
            }
            p.setBankDetails(bankDetails);
            e.persist(p);
        }
        transaction.commit();
        e.close();
    }

    @BeforeAll
    static void setup() {
        Random rn = new Random();
        int randomMin = 1;
        int randomMax = 10;

        initPersons(rn, randomMin, randomMax);

        final Fairy fairy = getFairy();
        final List<String> brokerIds = new LinkedList<>();
        for(int i = 0; i<50; i++){
            brokerIds.add(fairy.baseProducer().numerify("#############"));
        }
        initRequest(fairy, brokerIds);
        final EntityManager e = em();
        e.getTransaction().begin();
        brokerIds.stream().map(brokerId -> createBroker(fairy.person(), brokerId)).forEach(e::persist);
        final Broker broker = createBroker(fairy.person(), fairy.baseProducer().numerify("#############"));
        broker.setFirstName("Thomas");
        e.persist(broker);
        e.getTransaction().commit();
        e.close();
    }

    @AfterAll
    static void tearDown() {
        var em  = em();
       DatabaseUtil.executeQuery(em, connection -> {
           DSL.using(connection).truncate(de.inverso.jooqexample.gen.tables.Person.PERSON);
           DSL.using(connection).truncate(de.inverso.jooqexample.gen.tables.Request.REQUEST);
           DSL.using(connection).truncate(de.inverso.jooqexample.gen.tables.Broker.BROKER);
           return DSL.using(connection).truncate(de.inverso.jooqexample.gen.tables.BankDetails.BANK_DETAILS);
       });
    }

    private static void initRequest(Fairy fairy, List<String> vermittlernummern) {
        var rand = new Random();
        final EntityManager e = em();
        e.getTransaction().begin();
        final List<Person> persons = e.createNamedQuery("persons", Person.class).getResultList();
        for(int i = 0; i< fairy.baseProducer().randomBetween(10,10000); i++) {
            Request a = new Request();
            a.setCreationDate(fairy.dateProducer().randomDateInThePast(1).toLocalDate());
            var requestCandidates = List.of(fairy.baseProducer().numerify("SA###########"),fairy.baseProducer().numerify("KR###########"));
            var requestNumber = requestCandidates.get(rand.nextInt(requestCandidates.size()));
            a.setRequestNumber(requestNumber);;
            a.setPerson(fairy.baseProducer().randomElement(persons));
            a.setBrokerId(fairy.baseProducer().randomElement(vermittlernummern));
            e.persist(a);
        }
        e.getTransaction().commit();
        e.close();
    }

    private static Fairy getFairy() {
        return Fairy.create(Locale.GERMANY);
    }

    private static BankDetails createBankDetails(IBAN iban) {
        return new BankDetails(iban.getIbanNumber(), iban.getBban());
    }

    private static Person createPerson(com.devskiller.jfairy.producer.person.Person person) {
        return new Person(person.getFirstName(), person.getLastName());
    }

    private static Broker createBroker(com.devskiller.jfairy.producer.person.Person person, String vermittlerNummer){
        return new Broker(person.getFirstName(), person.getLastName(), vermittlerNummer);
    }

}
