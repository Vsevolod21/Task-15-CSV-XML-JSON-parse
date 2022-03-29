import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> staff = new ArrayList<>();
        staff.add(new Employee(1, "John", "Smith", "USA", 25));
        staff.add(new Employee(2, "Inav", "Petrov", "RU", 23));

        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType((Employee.class));
        strategy.setColumnMapping(columnMapping);
        try (Writer writer = new FileWriter(fileName)) {
            StatefulBeanToCsv<Employee> sbc = new StatefulBeanToCsvBuilder<Employee>(writer)
                    .withMappingStrategy(strategy)
                    .build();
            sbc.write(staff);
            writer.flush();
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }

        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");

        List<Employee> list1 = parseXML("data.xml");
        String json1 = listToJson(list1);
        writeString(json1, "data2.json");

        String json2 = readString("data.json");
        List<Employee> list2 = jsonToList(json2);
        System.out.println(list2);
    }

    public static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(fileName));
        Node node = document.getDocumentElement();
        NodeList nodelist = node.getChildNodes();
        for (int i = 0; i < nodelist.getLength(); i++) {
            Node node_ = nodelist.item(i);
            if (node_.getNodeName().equals("employee")) {
                NodeList nodeList1 = node_.getChildNodes();
                long id = 0;
                String firstName = null, lastName = null, country = null;
                int age = 0;
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node node_1 = nodeList1.item(j);
                    String attrName = node_1.getNodeName();
                    String attrValue = node_1.getTextContent();
                    switch (attrName) {
                        case "id":
                            id = Long.parseLong(attrValue);
                            break;
                        case "firstName":
                            firstName = attrValue;
                            break;
                        case "lastName":
                            lastName = attrValue;
                            break;
                        case "country":
                            country = attrValue;
                            break;
                        case "age":
                            age = Integer.parseInt(attrValue);
                            break;
                    }
                }
                list.add(new Employee(id, firstName, lastName, country, age));
            }
        }
        return list;
    }

    public static List<Employee> jsonToList(String json) {
        List<Employee> list = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = (JsonArray) parser.parse(json);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        for (Object j : jsonArray) {
            Employee employee = gson.fromJson((JsonObject) j, Employee.class);
            list.add(employee);
        }
        return list;
    }

    public static String readString(String fileName) {
        String jsonString = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            jsonString = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(json);
    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy1 = new ColumnPositionMappingStrategy<>();
            strategy1.setType((Employee.class));
            strategy1.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy1)
                    .build();
            staff = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }

}
