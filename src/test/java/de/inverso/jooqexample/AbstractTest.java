package de.inverso.jooqexample;

import de.inverso.jooqexample.PersistenceUtil;

import javax.persistence.EntityManager;


/**
 * @author fabian
 * on 17.05.19.
 */
public abstract class AbstractTest {

    protected static EntityManager em() {
        return PersistenceUtil.getEntityManager();
    }
}
