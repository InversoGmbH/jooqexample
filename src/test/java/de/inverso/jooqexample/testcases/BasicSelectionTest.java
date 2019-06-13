package de.inverso.jooqexample.testcases;

import static org.jooq.impl.DSL.*;

import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.DatabaseUtil;
import de.inverso.jooqexample.dto.BankStatisticsDTO;
import org.hibernate.transform.Transformers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author fabian
 * on 18.05.19.
 */

public class BasicSelectionTest extends AbstractTest {

    @Test
    @Tag("fast")
    public void selectDataTest() {

        var pid = field("p.id", Long.class);
        var bid = field("b.ID");
        var bPersonId = field("b.person_id", Long.class);
        var anzahl = count(bid).as("anzahl");
        var personTable = table("PERSON p");
        var bankverbindungTable = table("BANKDETAILS b");

        EntityManager entityManager = em();
        entityManager.getTransaction().begin();
        final var query = select(pid.as("id"), anzahl).//
                from(personTable).//
                join(bankverbindungTable) //
                .on(pid.eq(bPersonId)) //
                .groupBy(pid) //
                .having(anzahl.gt(1)).getQuery();
        final Logger logger = Logger.getAnonymousLogger();
        logger.info(query.getSQL());
        final Query nativeQuery = entityManager.createNativeQuery(query.getSQL());

        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            nativeQuery.setParameter(i + 1, values.get(i));
        }
        final List<BankStatisticsDTO> resultList = nativeQuery.unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(BankStatisticsDTO.class)).getResultList();
        Assertions.assertNotEquals(resultList.size(), 0);
        entityManager.flush();
        entityManager.close();
    }

    @Test
    @Tag("fast")
    public void selectWithUtilityTest() {
        var pid = field("p.id", Long.class);
        var bid = field("b.ID");
        var bPersonId = field("b.person_id", Long.class);
        var anzahl = count(bid).as("anzahl");
        var personTable = table("PERSON p");
        var bankverbindungTable = table("BANKDETAILS b");
        EntityManager entityManager = em();
        entityManager.getTransaction().begin();

        final List<BankStatisticsDTO> statisticsDTOS = DatabaseUtil.nativeQueryWithDTO(entityManager,
                select(pid.as("id"), anzahl).//
                        from(personTable).//
                        join(bankverbindungTable) //
                        .on(pid.eq(bPersonId)) //
                        .groupBy(pid) //
                        .having(anzahl.gt(1)).getQuery(),
                BankStatisticsDTO.class
        );
        Assertions.assertNotEquals(statisticsDTOS.size(), 0);
        entityManager.flush();
        entityManager.close();
    }


}
