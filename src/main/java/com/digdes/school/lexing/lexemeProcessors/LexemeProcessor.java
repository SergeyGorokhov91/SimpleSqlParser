package com.digdes.school.lexing.lexemeProcessors;

import com.digdes.school.lexing.Lexeme;

import java.util.List;

public interface LexemeProcessor {
    void process(List<Lexeme> lexemes, String word);
}
