package com.digdes.school.lexing.lexemeProcessors;

import com.digdes.school.lexing.Lexeme;
import com.digdes.school.lexing.LexemeType;

import java.util.List;

public class UpdateLexemeProcessor implements LexemeProcessor {
    @Override
    public void process(List<Lexeme> lexemes, String word) {
        lexemes.add(new Lexeme(LexemeType.UPDATE, word.toUpperCase()));
    }
}
