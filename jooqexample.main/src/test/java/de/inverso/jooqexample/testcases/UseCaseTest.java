package de.inverso.jooqexample.testcases;

import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.model.*;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.logging.Logger;

/**
 * @author fabian
 * on 19.05.19.
 */
public class UseCaseTest extends AbstractTest {

    @Test
    public void invalidSqLTest() {
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

    @Test
    public void countBadWithCriteriaTest() {
        EntityManager entityManager = em();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Request> query = cb.createQuery(Request.class);
        final Root<Request> root = query.from(Request.class);
        query.where(cb.like(root.get(Request_.REQUEST_NUMBER),"KR%"));
        Logger.getAnonymousLogger().info("" + entityManager.createQuery(query).getResultList().size());
    }

    @Test
    public void countWithCriteriaTest() {
        EntityManager entityManager = em();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = cb.createQuery(Long.class);
        final Root<Request> root = query.from(Request.class);
        query.select(cb.count(root));
        query.where(cb.like(root.get(Request_.REQUEST_NUMBER),"KR%"));
        Logger.getAnonymousLogger().info(entityManager.createQuery(query).getSingleResult().toString());
    }

}
