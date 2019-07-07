package de.inverso.jooqexample.testcases;

import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.DatabaseUtil;
import de.inverso.jooqexample.dto.BrokerStatistic;

import de.inverso.jooqexample.gen.tables.Broker;
import de.inverso.jooqexample.gen.tables.Request;
import de.inverso.jooqexample.gen.tables.records.BrokerRecord;
import org.jooq.DatePart;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import java.util.List;
import java.util.logging.Logger;

import static de.inverso.jooqexample.gen.tables.Broker.*;
import static org.jooq.impl.DSL.*;


/**
 * @author fabian
 * on 18.05.19.
 */

public class GeneratedClassesTest extends AbstractTest {

    @Test
    public void selectBrokerTest() {
        EntityManager entityManager = em();
        List<BrokerRecord> result = DatabaseUtil.executeQuery(entityManager, connection ->
                DSL.using(connection)//
                        .select(asterisk()) //
                        .from(BROKER) //
                        .where(BROKER.FIRSTNAME.eq("Thomas")) //
                        .fetchInto(BrokerRecord.class));

        Logger.getAnonymousLogger().info("\n" + result.toString());
        Assertions.assertNotEquals(result.size(), 0);
        entityManager.close();
    }

    @Test
    public void selectWithJoinTest() {
        Broker v = BROKER.as("v");
        Request a = Request.REQUEST.as("a");

        EntityManager entityManager = em();
        final var result = DatabaseUtil.executeQuery(entityManager, connection ->
                using(connection). //
                        select(v.BROKERID, count(a.ID).as("anzahl")). //
                        from(v). //
                        join(a).on(a.BROKERID.eq(v.BROKERID)). //
                        where(a.CREATIONDATE.ge( dateAdd( currentDate(), -1, DatePart.MONTH ))). //
                        groupBy(v.BROKERID). //
                        orderBy(count(a.ID).desc()). //
                        fetch()
        );
        Logger.getAnonymousLogger().info("\n" + result.toString());
    }

    @Test
    public void selectWithJoinDTOTest() {
        Broker v = BROKER.as("v");
        Request a = Request.REQUEST.as("a");
        Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        EntityManager entityManager = em();
        final var result = DatabaseUtil.executeQuery(entityManager, connection ->
                using(connection). //
                        select(v.BROKERID.as("vermittlernummer"), count(a.ID).as("anzahl")). //
                        from(v). //
                        join(a).on(a.BROKERID.eq(v.BROKERID)). //
                        where(a.CREATIONDATE.ge( dateAdd( currentDate(), -1, DatePart.MONTH ))). //
                        groupBy(v.BROKERID). //
                        orderBy(count(a.ID).desc()). //
                        fetchInto(BrokerStatistic.class)
        );
        Logger.getAnonymousLogger().info("\n" + result.toString());
    }

}
