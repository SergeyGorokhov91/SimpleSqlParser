package com.digdes.school.lexing;

import java.util.List;

public class LexemeBuffer {
    private int pos;

    private List<Lexeme> lexemes;

    public LexemeBuffer(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    public Lexeme next() {
        return lexemes.get(pos++);
    }

    public Lexeme current() {
        return lexemes.get(pos);
    }

    public Lexeme back() {
        return lexemes.get(--pos);
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }


    public int size() {
        return lexemes.size();
    }

    public List<Lexeme> getLexemes() {
        return lexemes;
    }

    public Lexeme remove() {
        if (pos >= lexemes.size()) {
            return null;
        }
        Lexeme removedLexeme = lexemes.remove(pos);
        return removedLexeme;
    }
}