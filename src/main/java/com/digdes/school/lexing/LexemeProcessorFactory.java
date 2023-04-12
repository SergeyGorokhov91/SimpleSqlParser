package com.digdes.school.lexing;

import com.digdes.school.lexing.lexemeProcessors.*;

public class LexemeProcessorFactory {
    public LexemeProcessor create(String word) {
        switch (word.toUpperCase()) {
            case "INSERT":
                return new InsertLexemeProcessor();
            case "SELECT":
                return new SelectLexemeProcessor();
            case "UPDATE":
                return new UpdateLexemeProcessor();
            case "DELETE":
                return new DeleteLexemeProcessor();
            case "VALUES":
                return new ValuesLexemeProcessor();
            case ",":
                return new CommaLexemeProcessor();
            case "WHERE":
                return new WhereLexemeProcessor();
            case "OR":
                return new OrLexemeProcessor();
            case "AND":
                return new AndLexemeProcessor();
            case "":
                return new EndOfLineLexemeProcessor();

            default:
                if (isaCorrectConditionalExpression(word)) {
                    return new ConditionalExpressionLexemeProcessor();
                } else {
                    throw new IllegalArgumentException("Ошибка при создании лексем. Некорректное условное выражение: " + word + ".");
                }
        }
    }

    private static boolean isaCorrectConditionalExpression(String word) {
        return word.matches("'[^']*'\\s*(ilike|like|[<>]=?|!=|=)\\s*(null|true|false|\\d+(?:\\.\\d+)?|'[^']*')");
    }
}