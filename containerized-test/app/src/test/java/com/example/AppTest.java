/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.example;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class AppTest {
    private static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test public void appHasAGreeting() {
        App classUnderTest = new App();
        logger.info("Logging");
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}