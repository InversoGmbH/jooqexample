package de.inverso.jooqexample.testcases;

import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.DatabaseUtil;
import de.inverso.jooqexample.gen.tables.RequestProduct;
import de.inverso.jooqexample.model.Product;
import de.inverso.jooqexample.model.Request;
import de.inverso.jooqexample.model.Request_;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    public void productsPerRequestJpaTest() {
        EntityManager entityManager = em();
        List<Request> requests = entityManager.createNamedQuery("requestWithProducts", Request.class).getResultList();
        final Map<Long, String> requestProduct = new HashMap<>();
        requests.stream().forEach(request ->
                requestProduct.put(request.getId(),
                        request.getProducts().stream().map(Product::getName).collect(Collectors.toList()).toString())
        );
        Logger.getAnonymousLogger().info(requestProduct.toString());
    }

    @Test
    public void productsPerRequestJooqTest() {
        EntityManager entityManager = em();
        final var result = DatabaseUtil.executeQuery(entityManager, connection -> {
            var r = de.inverso.jooqexample.gen.tables.Request.REQUEST.as("r");
            var rp = RequestProduct.REQUEST_PRODUCT.as("rp");
            var p = de.inverso.jooqexample.gen.tables.Product.PRODUCT.as("p");
            return DSL.using(connection, SQLDialect.H2)
                    .select(r.ID, DSL.listAgg(p.NAME, ",").withinGroupOrderBy(p.ID).as("Products")) //
                    .from(r //
                            .join(rp).on(r.ID.eq(rp.REQUEST_ID)) //
                            .join(p).on(p.ID.eq(rp.PRODUCTS_ID)) //
                    ).groupBy(r.ID).fetch();
        });
        Logger.getAnonymousLogger().info(result.toString());
    }

    @Test
    public void countBadWithCriteriaTest() {
        EntityManager entityManager = em();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Request> query = cb.createQuery(Request.class);
        final Root<Request> root = query.from(Request.class);
        query.where(cb.like(root.get(Request_.REQUEST_NUMBER), "KR%"));
        Logger.getAnonymousLogger().info("" + entityManager.createQuery(query).getResultList().size());
    }

    @Test
    public void countWithCriteriaTest() {
        EntityManager entityManager = em();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = cb.createQuery(Long.class);
        final Root<Request> root = query.from(Request.class);
        query.select(cb.count(root));
        query.where(cb.like(root.get(Request_.REQUEST_NUMBER), "KR%"));
        Logger.getAnonymousLogger().info(entityManager.createQuery(query).getSingleResult().toString());
    }

}
