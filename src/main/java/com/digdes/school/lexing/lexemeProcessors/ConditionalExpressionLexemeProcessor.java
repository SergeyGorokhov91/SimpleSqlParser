package com.digdes.school.lexing.lexemeProcessors;

import com.digdes.school.exception.LexicalException;
import com.digdes.school.lexing.Lexeme;
import com.digdes.school.lexing.LexemeType;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionalExpressionLexemeProcessor implements LexemeProcessor {
    @Override
    public void process(List<Lexeme> lexemes, String word) {
        if (itsBeforeWhereOperator(lexemes, word)) {
            addKeyValuePairToLexemeList(lexemes, word);
        } else if (itsAfterWhereOperator(lexemes)) {
            addConditionalStatementToLexemeList(lexemes, word);
        } else {
            throw new LexicalException("Лексема, содержащая оператор сравнения, идёт до ключевого слова WHERE: " + word);
        }
    }

    private static void addConditionalStatementToLexemeList(List<Lexeme> lexemes, String word) {
        Pattern pattern = Pattern.compile("" +
                "('[a-zA-Z ]+')" + //first group
                "(ilike|like|=|[<>]=?|!=)" + //second group
                "(null|true|false|\\d+(?:\\.\\d+)?|'%?[a-zA-Zа-яА-ЯёЁ ]+%?')"); //third group

        Matcher matcher = pattern.matcher(word);
        if (matcher.matches()) {
            lexemes.add(new Lexeme(LexemeType.COMPARABLE_FIELD, matcher.group(1).replaceAll("'", "").toLowerCase()));
            lexemes.add(new Lexeme(LexemeType.OPERATOR, matcher.group(2)));
            addConcreteValueToLexemeList(lexemes, matcher.group(3));
        } else {
            throw new LexicalException("Лексема имеет некорректную структуру: " + word);
        }
    }

    private static void addKeyValuePairToLexemeList(List<Lexeme> lexemes, String word) {
        String[] parts = word.split("=");
        lexemes.add(new Lexeme(LexemeType.KEY, parts[0].replaceAll("'", "").toLowerCase()));
        addConcreteValueToLexemeList(lexemes, parts[1]);
    }

    private static void addConcreteValueToLexemeList(List<Lexeme> lexemes, String word) {
        if (word.matches("^(true|false)$")) {
            lexemes.add(new Lexeme(LexemeType.BOOLEAN, word));
        } else if (word.matches("^(null)$")) {
            lexemes.add(new Lexeme(LexemeType.NULL, null));
        }else if(word.matches("\\b\\d+\\b")) {
            lexemes.add(new Lexeme(LexemeType.LONG,word));
        } else if(word.matches("\\b\\d+\\.\\d+\\b")){
            lexemes.add(new Lexeme(LexemeType.DOUBLE,word));
        } else if (word.matches("'%?[a-zA-Zа-яА-ЯёЁ ]+%?'")) {
            lexemes.add(new Lexeme(LexemeType.STRING,word.replaceAll("'","")));
        } else {
            throw new LexicalException("Некорректный тип переменной: " + word);
        }

    }

    private static boolean itsAfterWhereOperator(List<Lexeme> lexemes) {
        return lexemes.contains(new Lexeme(LexemeType.WHERE, "WHERE"));
    }

    private static boolean itsBeforeWhereOperator(List<Lexeme> lexemes, String word) {
        return !lexemes.contains(new Lexeme(LexemeType.WHERE, "WHERE")) && word.matches("'[\\w\\s]+'=(null|true|false|\\d+(?:\\.\\d+)?|'[^']*')");
    }
}