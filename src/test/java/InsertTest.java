import com.digdes.school.JavaSchoolStarter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class InsertTest {

    @Test
    public void insertValueCanBeNullTest() throws Exception {
        //Arrange-------------------------
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String insertQuery = "insert values 'lastname'=null, 'id'=null,'active'=null,'age'=null,'cost'=null";
        row.put("id", null);
        row.put("age", null);
        row.put("lastname", null);
        row.put("active", null);
        row.put("cost", null);
        table.add(row);
        List<Map<String, Object>> result = javaSchoolStarter.execute(insertQuery);

        //Assert--------------------------
        assertThat(table, containsInAnyOrder(result.toArray()));
    }

    @Test
    public void idColumnHaveLongTypeTest() throws Exception {
        //Arrange-------------------------
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String insertQuery = "insert values 'id'=1";
        row.put("id", 1L);//без L работать не будет
        table.add(row);
        List<Map<String, Object>> result = javaSchoolStarter.execute(insertQuery);

        //Assert--------------------------
        assertThat(table, containsInAnyOrder(result.toArray()));
    }

    @Test
    public void ageColumnHaveLongTypeTest() throws Exception {
        //Arrange-------------------------
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String insertQuery = "insert values 'age'=18";
        row.put("age", 18L);//без L работать не будет
        table.add(row);
        List<Map<String, Object>> result = javaSchoolStarter.execute(insertQuery);

        //Assert--------------------------
        assertThat(table, containsInAnyOrder(result.toArray()));
    }

    @Test
    public void activeColumnHaveBooleanType() throws Exception {
        //Arrange-------------------------
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String insertQuery = "insert values 'active'=true";
        row.put("active", true);
        table.add(row);
        List<Map<String, Object>> result = javaSchoolStarter.execute(insertQuery);

        //Assert--------------------------
        assertThat(table, containsInAnyOrder(result.toArray()));
    }

    @Test
    public void lastnameColumnHaveStringType() throws Exception {
        //Arrange-------------------------
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String insertQuery = "insert values 'lastname'='Sergey'";
        row.put("lastname", "Sergey");
        table.add(row);
        List<Map<String, Object>> result = javaSchoolStarter.execute(insertQuery);

        //Assert--------------------------
        assertThat(table, containsInAnyOrder(result.toArray()));
    }

    @Test
    public void costColumnHaveDoubleTypeTest() throws Exception {
        //Arrange-------------------------
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String insertQuery = "insert values 'cost'=5.0";
        row.put("cost", 5.0);
        table.add(row);
        List<Map<String, Object>> result = javaSchoolStarter.execute(insertQuery);

        //Assert--------------------------
        assertThat(table, containsInAnyOrder(result.toArray()));
    }

    @Test
    public void keywordsNotCaseSensitiveTest() throws Exception {
        //Arrange-------------------------
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String insertQuery = "InSeRt VaLuEs 'lastname'=null, 'id'=null,'active'=null,'age'=null,'cost'=null";
        row.put("id", null);
        row.put("age", null);
        row.put("lastname", null);
        row.put("active", null);
        row.put("cost", null);
        table.add(row);
        List<Map<String, Object>> result = javaSchoolStarter.execute(insertQuery);

        //Assert--------------------------
        assertThat(table, containsInAnyOrder(result.toArray()));
    }

    @Test
    public void columnsNameNotCaseSensitiveTest() throws Exception {
        //Arrange-------------------------
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String insertQuery = "INSERT VALUES 'LaStNaMe'=null, 'iD'=null,'aCtIvE'=null,'AgE'=null,'cOsT'=null";
        row.put("id", null);
        row.put("age", null);
        row.put("lastname", null);
        row.put("active", null);
        row.put("cost", null);
        table.add(row);
        List<Map<String, Object>> result = javaSchoolStarter.execute(insertQuery);

        //Assert--------------------------
        assertThat(table, containsInAnyOrder(result.toArray()));
    }

    @Test
    public void spacesBetweenLexemesDoNotAffectTest() throws Exception {
        //Arrange-------------------------
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String insertQuery = "  INSERT   VALUES   'lastname' =null ,'id'= null,'active' = null, 'age'=null , 'cost'=null   ";
        row.put("id", null);
        row.put("age", null);
        row.put("lastname", null);
        row.put("active", null);
        row.put("cost", null);
        table.add(row);
        List<Map<String, Object>> result = javaSchoolStarter.execute(insertQuery);

        //Assert--------------------------
        assertThat(table, containsInAnyOrder(result.toArray()));
    }

    @Test
    public void spacesInsideStingValuesMakeSenseTest() throws Exception {
        //Arrange-------------------------
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String insertQuery = "INSERT VALUES  'lastname' = ' S a m '";
        row.put("lastname", " S a m ");
        table.add(row);
        List<Map<String, Object>> result = javaSchoolStarter.execute(insertQuery);

        //Assert--------------------------
        assertThat(table, containsInAnyOrder(result.toArray()));
    }

    @Test
    public void databaseStoreValuesTest() throws Exception {
        //Arrange-------------------------
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String insertQuery = "INSERT VALUES  'lastname' = ' S a m '";
        row.put("lastname", " S a m ");
        table.add(row);
        List<Map<String, Object>> result = javaSchoolStarter.execute(insertQuery);

        //Act-----------------------------
        row = new HashMap<>();
        String insertQuery2 = "INSERT VALUES  'lastname' = 'Bob'";
        row.put("lastname", "Bob");
        table.add(row);
        result = javaSchoolStarter.execute(insertQuery2);

        //Assert--------------------------
        assertThat(table, containsInAnyOrder(result.toArray()));
    }
}
