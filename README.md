# SimpleSqlParser

Реализация синтаксического анализатора, который преобразует SQL-подобные запросы в "строки", которые хранятся в "базе данных".

## Установка
1. создать экземпляр класса JavaSchoolStarter.
    ```
    JavaSchoolStarter starter = new JavaSchoolStarter();
    ```
      

## Как работает
1.Для работы необходимо взывать у экземляра класса JavaSchoolStarter метод execute(query), где query - лексически и синтаксически корректный запрос.

  * Примерная грамматика языка, описывающая корректный запрос:
    ```
    insert : 'INSERT' 'VALUES' valueList;
    update : 'UPDATE' 'VALUES' valueList ('WHERE' condition)?;
    select : 'SELECT' 'WHERE' condition;
    delete : 'DELETE' 'WHERE' condition
    valueList : '(' value (COMMA value)* ')';
    value : '\'String\'' '=' (String| Double | Integer | Boolean | null);
    condition : andCondition | orCondition | simpleCondition;
    andCondition : condition AND condition;
    orCondition : condition OR condition;
    simpleCondition : ID comparisonOperator (String| Double | Integer | Boolean);
    comparisonOperator : '=' | '>' | '<' | '>=' | '<=' | 'LIKE' | 'ILIKE';
    ```

  * Примеры корректных запросов:
    ```
    "       INSERT VALUES 'id'=2, 'active'=false, 'lastName'='Ив анов', 'age'=25, 'cost'=4.3";
    "InSERT VAlUES    'id'=        1, 'lastName'       =null     ,     'age'  =        null,'cost'=null,'active'   =   null";
    "INSeRT        VaLUES 'age'=31, 'lastName'='Федоров'";
    "INSERT VALUES 'id'= 4, 'lastName'='Kevin','age'=19,'cost'=10.1,'active'=true";
    "INSERT VALUES 'id'= 5, 'lastName'='Коровин','age'=45,'cost'= 8.7,'active'=true";

    "UPdATE vALUES 'cOst'=250.2 WHeRE 'acTive' = true";
    "SeLeCt WHErE 'cost' != 0.0 AnD 'id' <= 2 Or 'aGe' = 45";
    "DeLETe WHERE 'id'>=1 AND 'active'=true AND 'id'!=5 Or 'LaStNaMe' like '% ан%' oR 'age' >= 19 AND 'age'<=44";
    "select";
    "delete";
    ```

2.Метод execute(query) возвращает List<Map<String,Object>>, где каждый Map<String,Object> представляет собой отдельную строку.
  * Пример вывода результата первых пяти запросов из примеров корректных строк пунткта 1:
    ```
    {
      {cost=4.3, active=false, id=2, age=25, lastname=Ив анов},
      {cost=null, active=null, id=1, age=null, lastname=null},
      {age=31, lastname=Федоров},
      {cost=10.1, active=true, id=4, age=19, lastname=Kevin},
      {cost=8.7, active=true, id=5, age=45, lastname=Коровин}
    }
    ```

  * Столбцы заранее определены и имеют следующий вид:
    ```
    ("id",Long);
    ("lastname",String);
    ("age",Long);
    ("cost",Double);
    ("active",Boolean);
    ```
