package com.digdes.school.lexing.lexemeProcessors;

import com.digdes.school.lexing.Lexeme;
import com.digdes.school.lexing.LexemeType;

import java.util.List;

public class InsertLexemeProcessor implements LexemeProcessor {
    @Override
    public void process(List<Lexeme> lexemes, String word) {
        lexemes.add(new Lexeme(LexemeType.INSERT,word.toUpperCase()));
    }
}
