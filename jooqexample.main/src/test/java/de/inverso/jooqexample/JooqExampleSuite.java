package de.inverso.jooqexample;

import de.inverso.jooqexample.testcases.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

/**
 * @author fabian
 * on 18.05.19.
 */
@RunWith(JUnitPlatform.class)
@SelectClasses({
        InitialTest.class,
        UseCaseTest.class,
        BasicSelectionTest.class,
        GeneratedClassesTest.class,
        ResultProcessingTest.class,
        InteractionTest.class
})
public class JooqExampleSuite {

}
