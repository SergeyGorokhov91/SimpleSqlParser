package com.digdes.school.parsing;

import com.digdes.school.lexing.Lexeme;
import com.digdes.school.lexing.LexemeType;
import com.digdes.school.exception.SyntaxException;

public class TypeChecker {

    private DatabaseManager db;

    public TypeChecker(DatabaseManager db) {
        this.db = db;
    }

    public void checkKeyValuePair(Lexeme key, Lexeme value) {

        if (!db.getDatabaseColumns().containsKey(key.value())) {
            throw new SyntaxException("База данных не содержит поле: " + key.value());
        }

        if (!checkValueType(db.getDatabaseColumns().get(key.value()), value)) {
            String typeName = getTypeName(value);

            throw new SyntaxException("тип аргумента " + key.value() + " не соотвествует ключу " + value.value() + ". Ожидается: " + db.getDatabaseColumns().get(key.value()).getClass().getSimpleName() + ". Фактически: " + typeName);
        }
    }

    private String getTypeName(Lexeme value) {
        String str = value.type().toString();
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


    private boolean checkValueType(Object expectedType, Lexeme value) {
        return expectedType instanceof Boolean && value.type().equals(LexemeType.BOOLEAN) ||
                expectedType instanceof Long && value.type().equals(LexemeType.LONG) ||
                expectedType instanceof Double && value.type().equals(LexemeType.DOUBLE) ||
                expectedType instanceof String && value.type().equals(LexemeType.STRING);
    }
}
