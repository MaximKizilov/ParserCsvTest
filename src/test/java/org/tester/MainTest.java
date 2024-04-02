package org.tester;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    static String[] columnMapping;
    static String[] files;
    static List<Employee> list;

    @BeforeAll
    static void setUp() {
        list = Arrays.asList(new Employee(1, "John", "Smith", "USA", 25),
                new Employee(2, "Inav", "Petrov", "RU", 23));
        columnMapping = new String[]{"id", "firstName", "lastName", "country", "age"};
        files = new String[]{
                "data.csv",
                "pseudodata.csv"
        };
    }

    @Test
    void parseCSV_speedTimeOut() {
        List<Employee> list1 = Main.parseCSV(columnMapping, files[0]);
        assertTimeout(Duration.ofMillis(400), () -> {
            Thread.sleep(200);
            assertArrayEquals(list1.toArray(), list.toArray());
        });
    }

    @Test
    void testParseCSV_ExpectNonEmptyList() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = files[1]; // Provide a test CSV file
        List<Employee> employees = Main.parseCSV(columnMapping, fileName);
        assertThat(employees, notNullValue());
        assertThat(employees, hasSize(2));
    }

    @Test
    void testParseCSV_ExpectRuntimeException() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "pseudo_data.csv";
        try {
            Main.parseCSV(columnMapping, fileName);
        } catch (RuntimeException e) {
            assertEquals(FileNotFoundException.class, e.getCause().getClass());
        }
    }


    @ParameterizedTest
    @CsvSource({
            "id,firstName,lastName,country,age, data.csv, 2",
            "id,firstName,lastName,country,age, pseudodata.csv, 2"
    })
    void testParseCSV_Parameterized(String id, String firstName, String lastName, String country, String age, String fileName, int expectedSize) {
        String[] columnMapping = {id, firstName, lastName, country, age};

        List<Employee> employees = Main.parseCSV(columnMapping, fileName);

        assertNotNull(employees);
        assertEquals(expectedSize, employees.size());
    }
}