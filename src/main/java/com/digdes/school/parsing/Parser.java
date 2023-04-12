package com.digdes.school.parsing;

import com.digdes.school.lexing.Lexeme;
import com.digdes.school.lexing.LexemeAnalyzer;
import com.digdes.school.lexing.LexemeBuffer;
import com.digdes.school.lexing.LexemeType;
import com.digdes.school.exception.SyntaxException;

import java.util.List;
import java.util.Map;


public class Parser {
    DatabaseManager database;

    public Parser(DatabaseManager database) {
        this.database = database;
    }

    public List<Map<String, Object>> query(LexemeBuffer lexemes) {

        preChecking(lexemes);

        Lexeme lexeme = lexemes.next();

        switch (lexeme.type()) {

            case INSERT -> {
                return database.insert(lexemes);
            }
            case SELECT -> {
                return database.select(lexemes);
            }
            case DELETE -> {
                return database.delete(lexemes);
            }
            case UPDATE -> {
                return database.update(lexemes);
            }
            default -> throw new IllegalArgumentException("недопустимое начало предложения: ");
        }
    }


    private static void preChecking(LexemeBuffer lexemes) {
        if (lexemes.getLexemes().isEmpty()) {
            throw new SyntaxException("фигня какая-то.");
        }
        LexemeType lastLexemeType = lexemes.getLexemes().get(lexemes.getLexemes().size() - 1).type();
        if (lastLexemeType != LexemeType.EOF) {
            throw new SyntaxException("В предложении каким-то чудом не оказалось конца. Мистика.");
        }

        int count = 0;
        for (Lexeme lexeme : lexemes.getLexemes()) {
            if (lexeme.type() == LexemeType.EOF) {
                count++;
            }
        }

        if (count > 1) {
            throw new SyntaxException("В предложении каким-то чудом оказалось два конца. Это забавно.");
        }
    }

}
