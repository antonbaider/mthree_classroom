package com.mthree;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App.
 */
/**
 * Unit test for simple App.
 */
public class AppTest {
    public boolean start() {
        System.out.println("Application Started");
        return true;
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testApp() {
        // Simple assertion that will always pass
        App app = new App();

        // Act
        boolean result = app.start();

        // Assert
        assertTrue(result, "The application should start successfully.");
    }
}