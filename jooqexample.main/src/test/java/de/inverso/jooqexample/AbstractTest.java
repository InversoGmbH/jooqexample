package de.inverso.jooqexample;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.payment.IBAN;
import de.inverso.jooqexample.model.*;
import org.junit.jupiter.api.BeforeAll;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * @author fabian
 * on 17.05.19.
 */
public abstract class AbstractTest {

    public static final Integer PERSON_AMOUNT = 1000;

    private static Boolean initialized = false;

    public static synchronized Boolean isInitialized() {
        if(!initialized) {
            initialized = true;
            return false;
        }
        return initialized;
    }

    protected static EntityManager em() {
        return DatabaseUtil.getEntityManager();
    }


    private static void initPersons(Random rn, int randomMin, int randomMax) {
        EntityManager e = em();
        EntityTransaction transaction = e.getTransaction();
        Fairy fairy = getFairy();

        transaction.begin();
        for (int i = 0; i < PERSON_AMOUNT; i++) {
            final int random = rn.nextInt(randomMax - randomMin + 1);
            Person p = createPerson(fairy.person());
            List<BankDetails> bankDetails = new ArrayList<>();
            for (int j = 0; j < random; j++) {
                bankDetails.add(createBankDetails(fairy.iban()));
            }
            p.setBankDetails(bankDetails);
            e.persist(p);
        }
        transaction.commit();
        e.close();
    }

    private static void initProducts(Fairy fairy, Random rn) {
        final List<String> branches = List.of("KR", "SA");
        int randomMin = 10;
        int randomMax = 100;
        EntityManager entityManager = em();

        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        for (int i = 0; i < fairy.baseProducer().randomBetween(randomMin, randomMax); i++) {
            String productName = fairy.textProducer().latinWord(1);
            Product product = new Product(productName, branches.get(rn.nextInt(branches.size())));
            entityManager.persist(product);
        }
        transaction.commit();
        entityManager.close();
    }

    @BeforeAll
    static void setup() {
        if(isInitialized()){
            return;
        }
        Logger.getAnonymousLogger().info("Setup Tests");
        Random rn = new Random();
        int randomMin = 1;
        int randomMax = 10;

        initPersons(rn, randomMin, randomMax);

        final Fairy fairy = getFairy();
        final List<String> brokerIds = new LinkedList<>();
        for (int i = 0; i < 50; i++) {
            brokerIds.add(fairy.baseProducer().numerify("#############"));
        }
        initProducts(fairy, rn);
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

    private static void initRequest(Fairy fairy, List<String> brokerIds) {
        var rand = new Random();
        final EntityManager e = em();
        e.getTransaction().begin();

        final List<Product> products = getAllProducts(e);
        final List<Person> persons = e.createNamedQuery("persons", Person.class).getResultList();
        for (int i = 0; i < fairy.baseProducer().randomBetween(10, 10000); i++) {
            Request a = new Request();
            a.setCreationDate(fairy.dateProducer().randomDateInThePast(1).toLocalDate());
            var requestCandidates = List.of(fairy.baseProducer().numerify("SA###########"), fairy.baseProducer().numerify("KR###########"));
            var requestNumber = requestCandidates.get(rand.nextInt(requestCandidates.size()));
            var requestBranch = requestNumber.substring(0,2);
            final List<Product> productList = products.stream().filter(p -> p.getBranch().equals(requestBranch)).collect(Collectors.toList());
            Collections.shuffle(productList);
            a.setProducts(productList.stream().limit(fairy.baseProducer().randomBetween(1,10)).collect(Collectors.toList()));
            a.setRequestNumber(requestNumber);
            a.setPerson(fairy.baseProducer().randomElement(persons));
            a.setBrokerId(fairy.baseProducer().randomElement(brokerIds));
            e.persist(a);
        }
        e.getTransaction().commit();
        e.close();
    }

    private static List<Product> getAllProducts(EntityManager e) {
        final CriteriaBuilder cb = e.getCriteriaBuilder();
        final CriteriaQuery<Product> query = cb.createQuery(Product.class);
        final Root<Product> root = query.from(Product.class);
        final CriteriaQuery<Product> all = query.select(root);
        return e.createQuery(all).getResultList();
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

    private static Broker createBroker(com.devskiller.jfairy.producer.person.Person person, String vermittlerNummer) {
        return new Broker(person.getFirstName(), person.getLastName(), vermittlerNummer);
    }

}
