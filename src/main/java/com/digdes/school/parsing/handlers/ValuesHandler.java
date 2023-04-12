package com.digdes.school.parsing.handlers;

import com.digdes.school.exception.SyntaxException;
import com.digdes.school.parsing.DatabaseManager;
import com.digdes.school.lexing.Lexeme;
import com.digdes.school.lexing.LexemeBuffer;
import com.digdes.school.lexing.LexemeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValuesHandler {

    static DatabaseManager db;

    public ValuesHandler(DatabaseManager db) {
        this.db = db;
    }

    public void getKeyValueList(LexemeBuffer lexemes, Map<String, Object> row) {
        if (!lexemes.current().type().equals(LexemeType.KEY)) {
            throw new SyntaxException("Некорректный синтаксис после ключевого слова VALUES");
        }

        do {
            getKeyValuePairs(lexemes, row);
        }while(!lexemes.current().type().equals(LexemeType.EOF) && !lexemes.current().type().equals(LexemeType.WHERE));
    }

    private void getKeyValuePairs(LexemeBuffer lexemes, Map<String,Object> row) {
        Lexeme keyLexeme = lexemes.next();
        Lexeme valueLexeme = lexemes.next();

        if (!db.getDatabaseColumns().containsKey(keyLexeme.value())) {
            throw new SyntaxException("База данных не содержит поле: " + keyLexeme.value());
        }

        Object value = checkValueType(db.getDatabaseColumns().get(keyLexeme.value()), valueLexeme);

        if (row.containsKey(keyLexeme.value()) && !lexemes.getLexemes().get(0).type().equals(LexemeType.UPDATE)) {
            throw new SyntaxException("одно или более значений имеют одинаковые ключи: " + keyLexeme.value());
        }

        if (lexemes.getLexemes().get(0).type().equals(LexemeType.UPDATE)) {
            List<Lexeme> lexemesCopy = lexemes.getLexemes();
            List<String> list = new ArrayList<>();
            for (Lexeme lexeme:lexemesCopy) {
                if(list.contains(lexeme.value())) {
                    throw new SyntaxException("одно или более значений имеют одинаковые ключи: " + keyLexeme.value());
                } else if(lexeme.type().equals(LexemeType.KEY)){
                    list.add((String) lexeme.value());
                }
            }
        }

        row.put((String) keyLexeme.value(), value);

        if(lexemes.current().type().equals(LexemeType.WHERE) && lexemes.getLexemes().get(0).type().equals(LexemeType.UPDATE)) {
            //todo: эта ветка нужна для работы функции UPDATE
            // изначально модуль писался под INSERT и уже в конце адаптировался под UPDATE
            // в INSERT после списка пар ключ=значение всегда идёт EOF, в апдейте же всегда идёт WHERE
        } else if (!lexemes.current().type().equals(LexemeType.COMMA) && !lexemes.current().type().equals(LexemeType.EOF)) {
            throw new SyntaxException("Некорректный синтаксис после пары ключ-значение: '"+keyLexeme.value() +"'="+valueLexeme.value());
        } else if(lexemes.current().type().equals(LexemeType.COMMA)) {
            lexemes.next();
        }
    }

    private Object checkValueType(Object expectedType, Lexeme lexeme) {
        if(lexeme.type().equals(LexemeType.NULL)) {
            return null;
        } else if(expectedType instanceof Boolean && lexeme.type().equals(LexemeType.BOOLEAN)) {
            return lexeme.value().equals("true");
        } else if (expectedType instanceof Long && lexeme.type().equals(LexemeType.LONG)) {
            return Long.parseLong((String) lexeme.value());
        } else if(expectedType instanceof Double && lexeme.type().equals(LexemeType.DOUBLE)) {
            return Double.parseDouble((String) lexeme.value());
        } else if (expectedType instanceof String && lexeme.type().equals(LexemeType.STRING)) {
            return lexeme.value();
        }
        throw new SyntaxException("тип аргумента не соотвествует ключу. Ожидается: "+expectedType.getClass().getSimpleName() + ". Фактически: "+getTypeName(lexeme));
    }

    private String getTypeName(Lexeme value) {
        String str = value.type().toString();
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
