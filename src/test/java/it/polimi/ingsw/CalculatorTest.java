package it.polimi.ingsw;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private static Calculator calculator;

    @BeforeAll
    public static void initCalculator() {
        calculator = new Calculator();
    }

    @BeforeEach
    public void beforeEachTest() {
        System.out.println("This is executed before each Test");
    }

    @AfterEach
    public void afterEachTest() {
        System.out.println("This is executed after each Test");
    }

    @Test
    public void testSum() {
        int result = calculator.sum(3, 4);

        assertEquals(7, result);
    }

    @Test
    public void testDivison() {
        try {
            int result = calculator.divison(10, 2);
            assertEquals(5, result);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Test
    public void testDivisionException() {
        assertThrows(Exception.class,
                () -> {
                    calculator.divison(10, 0);
                });
    }

    @Disabled
    @Test
    public void testEqual() {
        boolean result = calculator.equalIntegers(20, 20);

        assertFalse(result);
    }

    @Disabled
    @Test
    public void testSubstraction() {
        int result = 10 - 3;

        assertTrue(result == 9);
    }
}