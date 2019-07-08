package de.inverso.jooqexample;

import de.inverso.jooqexample.testcases.BasicSelectionTest;
import de.inverso.jooqexample.testcases.GeneratedClassesTest;
import de.inverso.jooqexample.testcases.InitialTest;
import de.inverso.jooqexample.testcases.UseCaseTest;
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
        GeneratedClassesTest.class
})
public class JooqExampleSuite {

}
