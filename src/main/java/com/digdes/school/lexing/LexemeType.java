package com.digdes.school.lexing;

public enum LexemeType {
    SELECT, UPDATE, DELETE, INSERT,
    COMMA,
    VALUES,
    KEY_VALUE_PAIR, //TODO: подумать нужна ли эта лексема
    KEY,
    LONG, DOUBLE, STRING, BOOLEAN, WHERE, NULL,
    COMPARABLE_FIELD,
    OPERATOR,
    EXPRESSION,
    AND, OR,

    EOF;

    @Override
    public String toString() {
        return super.toString();
    }
}