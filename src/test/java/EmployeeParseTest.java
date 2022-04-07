import junit.framework.TestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeParseTest extends TestCase {
    EmployeeParse employeeParse;
    List<Employee> expectedList;
    List<Employee> listJson;

    @BeforeEach
    public void setUp() {
        employeeParse = new EmployeeParse();
        expectedList = new ArrayList<>();
        expectedList.add(new Employee(1, "John", "Smith", "USA", 25));
        expectedList.add(new Employee(2, "Inav", "Petrov", "RU", 23));
        listJson = new ArrayList<>();
        listJson.add(new Employee(1, "John", "Smith", "USA", 25));
    }
    @AfterEach
    public void tearDown() {
        employeeParse = null;
        expectedList = null;
        listJson = null;
    }

    @Test
    public void testParseCSV() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileNameCSV = "data.csv";
        final List<Employee> resultList = employeeParse.parseCSV(columnMapping, fileNameCSV);
        Assertions.assertNotNull(resultList);
        Assertions.assertEquals(expectedList,resultList);
    }

    @Test
    public void testListToJson() {
        final String expectedStringJson =
                "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25}]";
        final String resultStringJson = employeeParse.listToJson(listJson);
        Assertions.assertNotNull(resultStringJson);
        Assertions.assertEquals(expectedStringJson, resultStringJson);
    }

    @Test
    public void testParseXML() throws ParserConfigurationException, IOException, SAXException {
        final String fileNameXML = "data.xml";
        final List<Employee> resultList = employeeParse.parseXML(fileNameXML);
        Assertions.assertNotNull(resultList);
        Assertions.assertEquals(expectedList, resultList);
    }

    @Test
    public void testReadString() {
        final String fileName = "data.json";
        final String expectedJsonString =
                "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25}," +
                        "{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
        final String resultJsonString = employeeParse.readString(fileName);
        Assertions.assertEquals(expectedJsonString, resultJsonString);
    }
    
    @Test
    public void testJsonToList() {
        final String jsonString =
                "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25}]";
        final List<Employee> resultJsonList = employeeParse.jsonToList(jsonString);
        Assertions.assertEquals(listJson, resultJsonList);
    }
}