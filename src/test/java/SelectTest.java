import com.digdes.school.JavaSchoolStarter;
import com.digdes.school.exception.SyntaxException;
import com.digdes.school.parsing.DatabaseManager;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SelectTest {
    private  static JavaSchoolStarter javaSchoolStarter;
    @BeforeClass
    public static void setUp() throws Exception {
        javaSchoolStarter = new JavaSchoolStarter();
        javaSchoolStarter.execute("INSERT VALUES 'id'=1,'lastname'='Первов', 'age'=10,'cost'=100.0, 'active'=true");
        javaSchoolStarter.execute("INSERT VALUES 'id'=2,'lastname'='Второв', 'age'=20,'cost'=200.0, 'active'=false");
        javaSchoolStarter.execute("INSERT VALUES 'id'=3,'lastname'='Треть ов', 'age'=30,'cost'=300.0, 'active'=true");
        javaSchoolStarter.execute("INSERT VALUES 'id'=4,'lastname'='Четверов', 'age'=40,'cost'=400.0, 'active'=false");
        javaSchoolStarter.execute("INSERT VALUES 'id'=null,'lastname'='Пятеров', 'age'=null,'cost'=500.0, 'active'=false");
    }

    @Test
    public void comparableValueCantBeNull() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> table = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        //Act-----------------------------
        String selectQuery = "SELECT WHERE 'lastname'=null, 'id'=null,'active'=null,'age'=null,'cost'=null";
        row.put("id", null);
        row.put("age", null);
        row.put("lastname", null);
        row.put("active", null);
        row.put("cost", null);
        table.add(row);

        try {
            List<Map<String, Object>> result = javaSchoolStarter.execute(selectQuery);
            fail("Значения, которые передаются на сравнение, не могут быть null.");
        } catch (SyntaxException e) {
            assertEquals("Значения, которые передаются на сравнение, не могут быть null.", e.getMessage());
        }
    }

    @Test
    public void idColumnHaveLongType() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "select where 'id'=1";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 1L,//Long
                "lastname", "Первов",
                "age", 10L,
                "cost", 100.0,
                "active", true);
        exp.add(row);
        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void lastnameColumnHaveStringType() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "select where 'lastname'='Первов'";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 1L,
                "lastname", "Первов",//Sting
                "age", 10L,
                "cost", 100.0,
                "active", true);
        exp.add(row);
        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void ageColumnHaveLongType() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "select where 'age'=10";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 1L,
                "lastname", "Первов",
                "age", 10L,//Long
                "cost", 100.0,
                "active", true);
        exp.add(row);
        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void costColumnHaveDoubleType() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "select where 'cost'=100.0";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 1L,
                "lastname", "Первов",
                "age", 10L,
                "cost", 100.0,//Double
                "active", true);
        exp.add(row);
        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void activeColumnHaveBooleanType() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String insertQuery = "select where 'active'=true";
        List<Map<String, Object>> act = javaSchoolStarter.execute(insertQuery);
        row = Map.of(
                "id", 1L,
                "lastname", "Первов",
                "age", 10L,
                "cost", 100.0,
                "active", true);//Boolean
        exp.add(row);
        row = Map.of(
                "id", 3L,
                "lastname", "Треть ов",
                "age", 30L,
                "cost", 300.0,
                "active", true);//Boolean
        exp.add(row);
        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void keywordsNotCaseSensitiveTest() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "SeLeCt WhErE 'lastname'='Треть ов' AnD 'id'=3 oR 'lastname'='Второв'";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 3L,
                "lastname", "Треть ов",
                "age", 30L,
                "cost", 300.0,
                "active", true);
        exp.add(row);
        row = Map.of(
                "id", 2L,
                "lastname", "Второв",
                "age", 20L,
                "cost", 200.0,
                "active", false);
        exp.add(row);
        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void columnsNameNotCaseSensitiveTest() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "select where 'LaStNaMe'='Треть ов' AND 'ID'=3 AND 'aCtIvE'=true AND 'cOsT'=300.0 AND 'aGe'=30";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 3L,
                "lastname", "Треть ов",
                "age", 30L,
                "cost", 300.0,
                "active", true);
        exp.add(row);
        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void spacesBetweenLexemesDoNotAffectTest() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "  SELECT   WHERE   'lastname' ='Первов' AND   'id'= 1 AND 'active' = true  AND   'age'=10 AND 'cost'=100.0      ";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 1L,
                "lastname", "Первов",
                "age", 10L,
                "cost", 100.0,
                "active", true);
        exp.add(row);
        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void spacesInsideStingValuesMakeSenseTest() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "  SELECT   WHERE   'lastname' ='Треть ов' ";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 3L,
                "lastname", "Треть ов",
                "age", 30L,
                "cost", 300.0,
                "active", true);
        exp.add(row);
        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void selectLessThanTest() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "SELECT WHERE 'age' < 40 AND 'id' < 3";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 1L,
                "lastname", "Первов",
                "age", 10L,
                "cost", 100.0,
                "active", true);
        exp.add(row);
        row = Map.of(
                "id", 2L,
                "lastname", "Второв",
                "age", 20L,
                "cost", 200.0,
                "active", false);
        exp.add(row);
        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void selectGreaterThanTest() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "SELECT WHERE 'age' > 30 AND 'id' > 3";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 4L,
                "lastname", "Четверов",
                "age", 40L,
                "cost", 400.0,
                "active", false);
        exp.add(row);

        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void selectNotEqualTest() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "SELECT WHERE 'age' != 30";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 1L,//Long
                "lastname", "Первов",
                "age", 10L,
                "cost", 100.0,
                "active", true);
        exp.add(row);
        row = Map.of(
                "id", 2L,
                "lastname", "Второв",
                "age", 20L,
                "cost", 200.0,
                "active", false);
        exp.add(row);
        row = Map.of(
                "id", 4L,
                "lastname", "Четверов",
                "age", 40L,
                "cost", 400.0,
                "active", false);
        exp.add(row);

        row = new HashMap<>();//Map.of кидает нуллпоинтер, так что тут так
        row.put("id", null);
        row.put("lastname", "Пятеров");
        row.put("age", null);//30 != null
        row.put("cost", 500.0);
        row.put("active", false);
        exp.add(row);

        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void selectLikeTest() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "SELECT WHERE 'lastname' like '% ов'";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 3L,
                "lastname", "Треть ов",
                "age", 30L,
                "cost", 300.0,
                "active", true);
        exp.add(row);

        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));


        //Act-----------------------------
        selectQuery = "SELECT WHERE 'lastname' like 'Тр%'";
        act = javaSchoolStarter.execute(selectQuery);


        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));

        selectQuery = "SELECT WHERE 'lastname' like '%реть%'";
        act = javaSchoolStarter.execute(selectQuery);


        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void selectIlikeTest() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "SELECT WHERE 'lastname' ilike '% Ов'";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 3L,
                "lastname", "Треть ов",
                "age", 30L,
                "cost", 300.0,
                "active", true);
        exp.add(row);

        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));


        //Act-----------------------------
        selectQuery = "SELECT WHERE 'lastname' ilike 'тр%'";
        act = javaSchoolStarter.execute(selectQuery);


        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));

        selectQuery = "SELECT WHERE 'lastname' ilike '%рЕТь%'";
        act = javaSchoolStarter.execute(selectQuery);


        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void selectOrTest() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "SELECT WHERE 'lastname' ilike '% Ов' OR 'cost' = 200.0";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 3L,
                "lastname", "Треть ов",
                "age", 30L,
                "cost", 300.0,
                "active", true);
        exp.add(row);
        row = Map.of(
                "id", 2L,
                "lastname", "Второв",
                "age", 20L,
                "cost", 200.0,
                "active", false);
        exp.add(row);

        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void selectAndTest() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "SELECT WHERE 'lastname' ilike '%ов' AND 'cost' != 500.0";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 1L,//Long
                "lastname", "Первов",
                "age", 10L,
                "cost", 100.0,
                "active", true);
        exp.add(row);
        row = Map.of(
                "id", 2L,
                "lastname", "Второв",
                "age", 20L,
                "cost", 200.0,
                "active", false);
        exp.add(row);
        row = Map.of(
                "id", 3L,
                "lastname", "Треть ов",
                "age", 30L,
                "cost", 300.0,
                "active", true);
        exp.add(row);
        row = Map.of(
                "id", 4L,
                "lastname", "Четверов",
                "age", 40L,
                "cost", 400.0,
                "active", false);
        exp.add(row);

        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

    @Test
    public void selectAndOrPriorityWorksCorrect() throws Exception {
        //Arrange-------------------------
        List<Map<String, Object>> exp = new ArrayList<>();
        Map<String, Object> row;

        //Act-----------------------------
        String selectQuery = "SELECT WHERE 'lastname' ilike '%ов' AND 'cost' < 300.0 OR 'age'=40";
        List<Map<String, Object>> act = javaSchoolStarter.execute(selectQuery);
        row = Map.of(
                "id", 1L,//Long
                "lastname", "Первов",
                "age", 10L,
                "cost", 100.0,
                "active", true);
        exp.add(row);
        row = Map.of(
                "id", 2L,
                "lastname", "Второв",
                "age", 20L,
                "cost", 200.0,
                "active", false);
        exp.add(row);
        row = Map.of(
                "id", 4L,
                "lastname", "Четверов",
                "age", 40L,
                "cost", 400.0,
                "active", false);
        exp.add(row);

        //Assert--------------------------
        assertThat(exp, containsInAnyOrder(act.toArray()));
    }

}
