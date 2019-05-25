package de.inverso.jooqexample.testcases;

import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.PersistenceUtil;
import de.inverso.jooqexample.dto.VermittlerStatistic;
import de.inverso.jooqexample.gen.public_.tables.Antrag;
import de.inverso.jooqexample.gen.public_.tables.Vermittler;
import de.inverso.jooqexample.gen.public_.tables.records.VermittlerRecord;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Logger;

import static de.inverso.jooqexample.gen.public_.tables.Vermittler.VERMITTLER;
import static org.jooq.impl.DSL.*;


/**
 * @author fabian
 * on 18.05.19.
 */

public class GeneratedClassesTest extends AbstractTest {

    @Test
    public void selectVermittlerTest() {
        EntityManager entityManager = em();
        List<VermittlerRecord> result = PersistenceUtil.executeQuery(entityManager, connection ->
                DSL.using(connection)//
                        .select(asterisk()) //
                        .from(VERMITTLER) //
                        .where(VERMITTLER.FIRSTNAME.eq("Thomas")) //
                        .fetchInto(VermittlerRecord.class));

        Logger.getAnonymousLogger().info("\n"+result.toString());
        Assertions.assertNotEquals(result.size(), 0);
        entityManager.close();
    }

   @Test
    public void selectWithJoinTest() {
        Vermittler v = VERMITTLER.as("v");
        Antrag a = Antrag.ANTRAG.as("a");

        EntityManager entityManager = em();
        final var result = PersistenceUtil.executeQuery(entityManager, connection ->
                using(connection). //
                        select(v.VERMITTLERNUMMER, count(a.ID).as("anzahl")). //
                        from(v). //
                        join(a).on(a.VERMITTLERNUMMER.eq(v.VERMITTLERNUMMER)). //
                        groupBy(v.VERMITTLERNUMMER). //
                        orderBy(count(a.ID).desc()). //
                        fetch()
        );
        Logger.getAnonymousLogger().info("\n"+result.toString());
    }

    @Test
    public void selectWithJoinDTOTest() {
        Vermittler v = VERMITTLER.as("v");
        Antrag a = Antrag.ANTRAG.as("a");

        EntityManager entityManager = em();
        final var result = PersistenceUtil.executeQuery(entityManager, connection ->
                using(connection). //
                        select(v.VERMITTLERNUMMER, count(a.ID).as("anzahl")). //
                        from(v). //
                        join(a).on(a.VERMITTLERNUMMER.eq(v.VERMITTLERNUMMER)). //
                        groupBy(v.VERMITTLERNUMMER). //
                        orderBy(count(a.ID).desc()). //
                        fetchInto(VermittlerStatistic.class)
        );
        Logger.getAnonymousLogger().info("\n"+result.toString());
    }

}
