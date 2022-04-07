import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        EmployeeParse employeeParse = new EmployeeParse();

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> staff = new ArrayList<>();
        staff.add(new Employee(1, "John", "Smith", "USA", 25));
        staff.add(new Employee(2, "Inav", "Petrov", "RU", 23));

        employeeParse.createCSV(columnMapping, fileName, staff);

        List<Employee> list = employeeParse.parseCSV(columnMapping, fileName);
        String json = employeeParse.listToJson(list);
        employeeParse.writeString(json, "data.json");

        List<Employee> list1 = employeeParse.parseXML("data.xml");
        String json1 = employeeParse.listToJson(list1);
        employeeParse.writeString(json1, "data2.json");

        String json2 = employeeParse.readString("data.json");
        List<Employee> list2 = employeeParse.jsonToList(json2);
        System.out.println(list2);
    }
}
