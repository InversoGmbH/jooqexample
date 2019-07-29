package de.inverso.jooqexample.testcases;

import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.DatabaseUtil;
import de.inverso.jooqexample.gen.tables.RequestProduct;
import de.inverso.jooqexample.model.Product;
import de.inverso.jooqexample.model.Request;
import de.inverso.jooqexample.model.Request_;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

/**
 * @author fabian
 * on 19.05.19.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UseCaseTest extends AbstractTest {

    private static Logger LOGGER = LoggerFactory.getLogger(UseCaseTest.class);

    @Test
    @Order(1)
    public void invalidSqLTest() {
        String p = "bob";
        Assertions.assertThrows(PersistenceException.class, () -> {
                    //language=H2
                    String sql = "select p.ID count(b.ID) anzahl" +
                            "from PERSON p" +
                            "join BANKDETAILS B on p.ID = B.PERSON_ID " +
                            "where p.firstName = '" + p + "' " +
                            "group by p.ID " +
                            "having anzahl>1 " +
                            "order by anzahl desc";
                    EntityManager entityManager = em();
                    entityManager.createNativeQuery(sql).getResultList();
                }
        );
    }

    @Test
    @Order(2)
    public void productsPerRequestJpaTest() {
        EntityManager entityManager = em();
        List<Request> requests = entityManager.createNamedQuery("requestWithProducts", Request.class).getResultList();
        final Map<Long, String> requestProduct = requests.stream() //
                .collect(Collectors.toMap(Request::getId, request -> request.getProducts().stream() //
                        .sorted(Comparator.comparing(Product::getId)) //
                        .map(Product::getName).collect(Collectors.toList()) //
                        .toString(), (a, b) -> b + "\n"));
        LOGGER.info(requestProduct.toString());
        entityManager.close();
    }

    @Test
    @Order(3)
    public void productsPerRequestJooqTest() {
        EntityManager entityManager = em();
        final var result = DatabaseUtil.executeQuery(entityManager, connection -> {
            var r = de.inverso.jooqexample.gen.tables.Request.REQUEST.as("r");
            var rp = RequestProduct.REQUEST_PRODUCT.as("rp");
            var p = de.inverso.jooqexample.gen.tables.Product.PRODUCT.as("p");
            return using(connection, SQLDialect.H2)
                    .select(r.ID, listAgg(p.NAME, ",").withinGroupOrderBy(p.ID).as("Products")) //
                    .from(r //
                            .join(rp).on(r.ID.eq(rp.REQUEST_ID)) //
                            .join(p).on(p.ID.eq(rp.PRODUCTS_ID)) //
                    ).groupBy(r.ID).fetch();
        });
        LOGGER.info(result.toString());
        entityManager.close();
    }

    @Test
    @Order(4)
    public void countBadWithCriteriaTest() {
        EntityManager entityManager = em();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Request> query = cb.createQuery(Request.class);
        final Root<Request> root = query.from(Request.class);
        query.where(cb.like(root.get(Request_.REQUEST_NUMBER), "KR%"));
        LOGGER.info("" + entityManager.createQuery(query).getResultList().size());
        entityManager.close();
    }

    @Test
    @Order(5)
    public void countWithCriteriaTest() {
        EntityManager entityManager = em();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = cb.createQuery(Long.class);
        final Root<Request> root = query.from(Request.class);
        query.select(cb.count(root));
        query.where(cb.like(root.get(Request_.REQUEST_NUMBER), "KR%"));
        LOGGER.info(entityManager.createQuery(query).getSingleResult().toString());
        entityManager.close();
    }

}
