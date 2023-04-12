package com.digdes.school.parsing;

import com.digdes.school.lexing.Lexeme;
import com.digdes.school.lexing.LexemeType;
import com.digdes.school.exception.SyntaxException;

public class ValueParser {

    public static Object parse(Lexeme lexeme) {
        if(lexeme.type().equals(LexemeType.BOOLEAN)) {
            if(lexeme.value().equals("true")) {
                return true;
            } else {
                return false;
            }
        } else if (lexeme.type().equals(LexemeType.LONG)) {
            return Long.parseLong((String) lexeme.value());
        } else if(lexeme.type().equals(LexemeType.DOUBLE)) {
            return Double.parseDouble((String) lexeme.value());
        } else if ( lexeme.type().equals(LexemeType.STRING)) {
            return lexeme.value();
        }
        throw new SyntaxException("По идее сюда вообще невозможно добраться, ибо все вопросы по типам должны быть решены на более ранних этапах, но если добрались, то что-то не так с парсингом значений переменных");
    }
}
