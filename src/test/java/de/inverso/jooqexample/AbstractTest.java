package de.inverso.jooqexample;

import javax.persistence.EntityManager;


/**
 * @author fabian
 * on 17.05.19.
 */
public abstract class AbstractTest {

    protected static EntityManager em() {
        return DatabaseUtil.getEntityManager();
    }
}
