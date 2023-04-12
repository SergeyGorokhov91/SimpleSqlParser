package com.digdes.school;

import com.digdes.school.lexing.LexemeAnalyzer;
import com.digdes.school.lexing.LexemeBuffer;
import com.digdes.school.parsing.DatabaseManager;
import com.digdes.school.parsing.Parser;

import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {

    DatabaseManager db = new DatabaseManager();
    public JavaSchoolStarter() {
    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        LexemeAnalyzer analyzer = new LexemeAnalyzer();
        LexemeBuffer lexemes = analyzer.lexAnalyze(request);
        Parser parser = new Parser(db);
        return parser.query(lexemes);
    }
}
