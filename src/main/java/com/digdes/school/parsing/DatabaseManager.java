package com.digdes.school.parsing;

import com.digdes.school.lexing.Lexeme;
import com.digdes.school.parsing.expressions.Expression;
import com.digdes.school.exception.SyntaxException;
import com.digdes.school.lexing.LexemeBuffer;
import com.digdes.school.lexing.LexemeType;
import com.digdes.school.parsing.handlers.ValuesHandler;
import com.digdes.school.parsing.handlers.WhereHandler;

import java.util.*;

public class DatabaseManager {
    private final List<Map<String, Object>> database = new ArrayList<>();
    private final Map<String, Object> databaseColumns = new HashMap<>();

    public DatabaseManager() {
        databaseColumns.put("id", 0L);
        databaseColumns.put("lastname", "");
        databaseColumns.put("age", 0L);
        databaseColumns.put("cost", 0.0);
        databaseColumns.put("active", false);
    }

    public Map<String, Object> getDatabaseColumns() {
        return Collections.unmodifiableMap(databaseColumns);
    }

    public List<Map<String, Object>> getDatabase() {
        return database;
    }

    public List<Map<String, Object>> insert(LexemeBuffer lexemes) {
        Map<String, Object> row = new HashMap<>();
        if (lexemes.next().type().equals(LexemeType.VALUES)) {
            ValuesHandler valuesHandler = new ValuesHandler(this);
            valuesHandler.getKeyValueList(lexemes, row);
            this.getDatabase().add(row);
            return this.getDatabase();
        } else {
            throw new SyntaxException("недопустимый оператор, следующий за INSERT: ");
        }
    }

    public List<Map<String, Object>> select(LexemeBuffer lexemes) {
        if (lexemes.current().type().equals(LexemeType.EOF)) {
            return this.getDatabase();
        } else if (lexemes.current().type().equals(LexemeType.WHERE)) {
            WhereHandler handler = new WhereHandler(this);
            Expression expression = handler.handle(lexemes.getLexemes());
            return this.getDatabase().stream().filter(expression::execute).toList();
        } else {
            throw new SyntaxException("недопустимый оператор, следующий за SELECT: ");
        }
    }

    public List<Map<String, Object>> delete(LexemeBuffer lexemes) {
        if (lexemes.current().type().equals(LexemeType.EOF)) {
            this.getDatabase().clear();
            return this.getDatabase();
        } else if (lexemes.current().type().equals(LexemeType.WHERE)) {
            WhereHandler handler = new WhereHandler(this);
            Expression expression = handler.handle(lexemes.getLexemes());
            this.getDatabase().removeIf(expression::execute);
            return this.getDatabase();
        } else {
            throw new IllegalArgumentException("недопустимый оператор, следующий за DELETE: ");
        }
    }

    public List<Map<String, Object>> update(LexemeBuffer lexemes) {
        boolean containsWhere = false;
        for (Lexeme lexeme : lexemes.getLexemes()) {
            if (lexeme.type().equals(LexemeType.WHERE)) {
                containsWhere = true;
                break;
            }
        }

        if (lexemes.next().type().equals(LexemeType.VALUES)) {
            if (containsWhere) {
                WhereHandler handler = new WhereHandler(this);
                Expression expression = handler.handle(lexemes.getLexemes());
                for (Map<String, Object> row : database) {
                    if (expression.execute(row)) {
                        lexemes.setPos(2);
                        ValuesHandler valuesHandler = new ValuesHandler(this);
                        valuesHandler.getKeyValueList(lexemes, row);
                    }
                }
            } else {
                for (Map<String, Object> row : database) {
                    lexemes.setPos(2);
                    ValuesHandler valuesHandler = new ValuesHandler(this);
                    valuesHandler.getKeyValueList(lexemes, row);
                }
            }
            return database;
        } else {
            throw new IllegalArgumentException("недопустимый оператор, следующий за UPDATE: ");
        }

    }

    @Override
    public String toString() {
        return database.toString();
    }
}
