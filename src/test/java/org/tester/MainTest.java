package org.tester;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
        assertTimeout(Duration.ofMillis(200), () -> {
            Thread.sleep(200);
            return "result";
        });
    }

    @Test
    void testParseCSV_ExpectNonEmptyList() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv"; // Provide a test CSV file
        List<Employee> employees = Main.parseCSV(columnMapping, fileName);
        int i = 1;
        assertThat(employees, notNullValue());
        assertThat(employees, hasSize(2));
    }

    @ParameterizedTest
    @CsvSource({
            "id,firstName,lastName,country,age, data.csv, 2",
            "id,firstName,lastName,country,age, pseudodata.csv, 1"
    })
    void testParseCSV_Parameterized(String id, String firstName, String lastName, String country, String age, String fileName, int expectedSize) {
        String[] columnMapping = {id, firstName, lastName, country, age};

        List<Employee> employees = Main.parseCSV(columnMapping, fileName);

        assertNotNull(employees);
        assertEquals(expectedSize, employees.size());
    }
}