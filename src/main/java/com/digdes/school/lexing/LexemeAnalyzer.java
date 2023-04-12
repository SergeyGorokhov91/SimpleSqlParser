package com.digdes.school.lexing;

import com.digdes.school.exception.LexicalException;
import com.digdes.school.lexing.lexemeProcessors.LexemeProcessor;

import java.util.ArrayList;
import java.util.List;

public class LexemeAnalyzer {
    private List<Lexeme> lexemes = new ArrayList<>();
    LexemeProcessorFactory lexemeProcessorFactory = new LexemeProcessorFactory();

    public LexemeBuffer lexAnalyze(String expText) {
        String text = deleteBlanksInsideLexemes(addBlanksToCommas(expText));
        String[] words = text.split("\\s+(?=([^\']*\'[^\']*\')*[^\']*$)");
        for (String word : words) addLexemeToList(lexemes, word);
        if (lexemes.get(lexemes.size() - 1).type().equals(LexemeType.DOUBLE) ||
                lexemes.get(lexemes.size() - 1).type().equals(LexemeType.LONG) ||
                lexemes.get(lexemes.size() - 1).type().equals(LexemeType.STRING) ||
                lexemes.get(lexemes.size() - 1).type().equals(LexemeType.BOOLEAN) ||
                lexemes.get(lexemes.size() - 1).type().equals(LexemeType.NULL) ||
                lexemes.get(lexemes.size() - 1).type().equals(LexemeType.DELETE) ||
                lexemes.get(lexemes.size() - 1).type().equals(LexemeType.SELECT)) {
            addLexemeToList(lexemes, "");
        } else {
            throw new LexicalException("Некорректное окончание предложения: " + lexemes.get(lexemes.size() - 1).value());
        }
        return new LexemeBuffer(lexemes);
    }

    void addLexemeToList(List<Lexeme> lexemes, String word) {
        LexemeProcessor lexemeProcessor = lexemeProcessorFactory.create(word);
        lexemeProcessor.process(lexemes, word);
    }

    private static String deleteBlanksInsideLexemes(String expText) {
        expText = expText.replaceAll("^\\s+", "");
        return expText
                .replaceAll("\\s*=\\s*", "=")
                .replaceAll("\\s*like\\s*", "like")
                .replaceAll("\\s*ilike\\s*", "ilike")
                .replaceAll("\\s*>=\\s*", ">=")
                .replaceAll("\\s*<=\\s*", "<=")
                .replaceAll("\\s*!=\\s*", "!=")
                .replaceAll("\\s*<\\s*", "<")
                .replaceAll("\\s*>\\s*", ">");
    }

    private String addBlanksToCommas(String text1) {
        return text1.replaceAll("\\s*,\\s*", " , ");
    }
}