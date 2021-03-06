package de.inverso.jooqexample;

import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.transform.Transformers;

import javax.persistence.*;
import java.util.List;

/**
 * @author fabian
 * on 17.05.19.
 */
public final class DatabaseUtil {

    private static final String PERSISTENCE_UNIT_NAME = "test";
    private static EntityManagerFactory factory;

    public static synchronized EntityManager getEntityManager() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return factory.createEntityManager();
    }

    @SuppressWarnings("unchecked")
	public static <E> List<E> nativeQueryWithEntity(EntityManager em, org.jooq.Query query, Class<E> type) {
        // There's an unsafe cast here, but we can be sure that we'll get the right type from JPA
        Query result = em.createNativeQuery(query.getSQL(), type);

        return (List<E>) bindParameters(result, query).getResultList();
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
	public static <E> List<E> nativeQueryWithDTO(EntityManager em, org.jooq.Query query, Class<E> type) {
        Query result = em.createNativeQuery(query.getSQL());
        return (List<E>) bindParameters(result, query) //
                .unwrap(org.hibernate.query.Query.class) //
                .setResultTransformer(Transformers.aliasToBean(type)).getResultList();
    }

    private static Query bindParameters(Query result, org.jooq.Query query) {
        // Extract the bind values from the jOOQ query:
        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }
        return result;
    }

    public static <E> E executeQuery(final EntityManager entityManager, final ReturningWork<E> worker) {
        return entityManager.unwrap(Session.class).doReturningWork(worker);
    }

    private DatabaseUtil() {

    }
}
