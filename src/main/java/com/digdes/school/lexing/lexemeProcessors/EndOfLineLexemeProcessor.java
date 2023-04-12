package com.digdes.school.lexing.lexemeProcessors;

import com.digdes.school.lexing.Lexeme;
import com.digdes.school.lexing.LexemeType;

import java.util.List;

public class EndOfLineLexemeProcessor implements LexemeProcessor {
    @Override
    public void process(List<Lexeme> lexemes, String word) {
        lexemes.add(new Lexeme(LexemeType.EOF, word));
    }
}
