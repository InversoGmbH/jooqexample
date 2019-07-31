package de.inverso.jooqexample.testcases;

import de.inverso.jooqexample.AbstractTest;
import de.inverso.jooqexample.DatabaseUtil;
import de.inverso.jooqexample.gen.tables.records.PersonRecord;
import org.jooq.Cursor;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

import static de.inverso.jooqexample.gen.tables.BankDetails.BANK_DETAILS;
import static de.inverso.jooqexample.gen.tables.Person.PERSON;

public class ResultProcessingTest extends AbstractTest {

    private static Logger LOGGER = LoggerFactory.getLogger(ResultProcessingTest.class);

    @Test
    public void showPersonTest() {
        EntityManager entityManager = em();
        Result<PersonRecord> personRecords = DatabaseUtil.executeQuery(entityManager, connection -> {
            return DSL.using(connection).selectFrom(PERSON).fetch();
        });

        personRecords.stream().forEach( personRecord -> {
           LOGGER.info( personRecord.getFirstname() + " " + personRecord.getLastname());
        } );
        entityManager.close();
    }

    @Test
    public void processHugeResultsTest() {
        EntityManager entityManager = em();
        Cursor<Record> results = DatabaseUtil.executeQuery(entityManager, connection -> {
            return DSL.using(connection).selectFrom(PERSON.join(BANK_DETAILS).on(BANK_DETAILS.PERSON_ID.eq(PERSON.ID))).fetchLazy();
        });

        results.stream().forEach( record -> {
            LOGGER.info(record.get(PERSON.FIRSTNAME) + " " + record.get("LASTNAME") + " " + record.get(BANK_DETAILS.ID));
        } );
        entityManager.close();
    }

}
