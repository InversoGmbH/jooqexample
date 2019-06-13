package de.inverso.jooqexample.testcases;

import de.inverso.jooqexample.AbstractTest;
import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

/**
 * @author fabian
 * on 19.05.19.
 */
public class InvalidSqlTest extends AbstractTest {

    @Test

    public void executeSqLTest() {
        Assertions.assertThrows(PersistenceException.class, () -> {
                    //language=H2
                    String sql = "select p.ID count(b.ID) anzahl" +
                            "from PERSON p" +
                            "join BANKDETAILS B on p.ID = B.PERSON_ID\n" +
                            "group by p.ID\n" +
                            "having anzahl>1\n" +
                            "order by anzahl desc\n";
                    EntityManager entityManager = em();
                    entityManager.createNativeQuery(sql).getResultList();
                }
        );
    }

}
