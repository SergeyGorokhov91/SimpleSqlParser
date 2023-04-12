package com.digdes.school.parsing.handlers;

import com.digdes.school.exception.SyntaxException;
import com.digdes.school.parsing.DatabaseManager;
import com.digdes.school.parsing.expressions.AndExpression;
import com.digdes.school.parsing.expressions.Expression;
import com.digdes.school.parsing.expressions.OrExpression;
import com.digdes.school.parsing.expressions.SimpleExpression;
import com.digdes.school.lexing.Lexeme;
import com.digdes.school.lexing.LexemeBuffer;
import com.digdes.school.lexing.LexemeType;

import java.util.ArrayList;
import java.util.List;

public class WhereHandler {

    DatabaseManager db;

    public WhereHandler(DatabaseManager db) {
        this.db = db;
    }

    public Expression handle(List<Lexeme> lexemes) {
      return createExpressionFromLexemeList(getLexemeList(lexemes));
    }
    private static List<Lexeme> getLexemeList(List<Lexeme> lexemes) {
        LexemeBuffer buffer = new LexemeBuffer(lexemes);
        List<Lexeme> result = new ArrayList<>();
        while(!buffer.current().type().equals(LexemeType.WHERE)) {
            if(buffer.getPos() == lexemes.size()-1) {
                break;
            }
            buffer.next();
        }

        while(!buffer.current().type().equals(LexemeType.EOF)) {
            result.add(buffer.next());
        }
        result.add(new Lexeme(LexemeType.EOF,""));

        return result;

    }
    private Expression createExpressionFromLexemeList(List<Lexeme> lexemes) {
        if(isaCorrectWhereClause(lexemes)) {
            LexemeBuffer buffer = new LexemeBuffer(lexemes);
            while (true) {
                if (buffer.getLexemes().contains(new Lexeme(LexemeType.AND, "AND"))) {
                    AndExpression andExpression = new AndExpression();
                    while (!buffer.current().type().equals(LexemeType.AND)) {
                        buffer.next();
                    }
                    if (buffer.back().type().equals(LexemeType.EXPRESSION)) {
                        andExpression.setLeft((Expression) buffer.remove().value());
                    } else {
                        buffer.back();
                        buffer.back();
                        SimpleExpression expression = new SimpleExpression(db);
                        expression.setComparableField(buffer.remove());
                        expression.setComparisonOperator(buffer.remove());
                        expression.setValue(buffer.remove());
                        andExpression.setLeft(expression);
                    }
                    buffer.next();
                    if (buffer.current().type().equals(LexemeType.EXPRESSION)) {
                        andExpression.setRight((Expression) buffer.remove().value());
                        buffer.back();
                    } else {
                        SimpleExpression expression = new SimpleExpression(db);
                        expression.setComparableField(buffer.remove());
                        expression.setComparisonOperator(buffer.remove());
                        expression.setValue(buffer.remove());
                        andExpression.setRight(expression);
                    }
                    buffer.back();
                    buffer.getLexemes().set(buffer.getPos(), new Lexeme(LexemeType.EXPRESSION, andExpression));
                    buffer.setPos(0);
                }
                else if (buffer.getLexemes().contains(new Lexeme(LexemeType.OR, "OR"))) {
                    OrExpression orExpression = new OrExpression();
                    while (!buffer.current().type().equals(LexemeType.OR)) {
                        buffer.next();
                    }
                    if (buffer.back().type().equals(LexemeType.EXPRESSION)) {
                        orExpression.setLeft((Expression) buffer.remove().value());
                    } else {
                        buffer.back();
                        buffer.back();
                        SimpleExpression expression = new SimpleExpression(db);
                        expression.setComparableField(buffer.remove());
                        expression.setComparisonOperator(buffer.remove());
                        expression.setValue(buffer.remove());
                        orExpression.setLeft(expression);
                    }
                    buffer.next();
                    if (buffer.current().type().equals(LexemeType.EXPRESSION)) {
                        orExpression.setRight((Expression) buffer.remove().value());
                    } else {
                        SimpleExpression expression = new SimpleExpression(db);
                        expression.setComparableField(buffer.remove());
                        expression.setComparisonOperator(buffer.remove());
                        expression.setValue(buffer.remove());
                        orExpression.setRight(expression);
                    }
                    buffer.back();
                    buffer.getLexemes().set(buffer.getPos(), new Lexeme(LexemeType.EXPRESSION, orExpression));
                    buffer.setPos(0);
                }
                else {
                    buffer.next();
                    SimpleExpression expression = new SimpleExpression(db);
                    expression.setComparableField(buffer.remove());
                    expression.setComparisonOperator(buffer.remove());
                    expression.setValue(buffer.current());
                    buffer.getLexemes().set(buffer.getPos(), new Lexeme(LexemeType.EXPRESSION, expression));
                    buffer.setPos(0);
                }
                buffer.next();
                if (lexemes.size() == 3) {
                    buffer.setPos(0);
                    buffer.next();
                    return (Expression) buffer.current().value();
                }
            }
        }
        throw new SyntaxException("unreachable");
    }
    private boolean isaCorrectWhereClause(List<Lexeme> lexemes) {
        LexemeBuffer buffer = new LexemeBuffer(lexemes);
        if(!buffer.current().type().equals(LexemeType.WHERE)) {
            throw new SyntaxException("При анализе подвыражения WHERE оказалось, что в листе отстутсвует начальный элемент");
        }
        if(!lexemes.get(lexemes.size()-1).type().equals(LexemeType.EOF)) {
            throw new SyntaxException("При анализе подвыражения WHERE оказалось, что в листе отстутсвует конечный элемент");
        }
        buffer.next();
        while(true) {
            boolean simpleExpression = false;
            if(buffer.next().type().equals(LexemeType.COMPARABLE_FIELD)) {
                if(buffer.next().type().equals(LexemeType.OPERATOR)) {
                    if(buffer.current().type().equals(LexemeType.DOUBLE) ||
                       buffer.current().type().equals(LexemeType.LONG) ||
                       buffer.current().type().equals(LexemeType.BOOLEAN) ||
                       buffer.current().type().equals(LexemeType.STRING)) {
                        simpleExpression = true;
                        buffer.next();
                    } else if(buffer.current().type().equals(LexemeType.NULL)) {
                        throw new SyntaxException("Значения, которые передаются на сравнение, не могут быть null.");
                    }
                }
            }

            if(!simpleExpression) {
                throw new SyntaxException("сравниваемое выражение некорректно");
            }

            if(buffer.current().type().equals(LexemeType.EOF) && buffer.getPos() == lexemes.size()-1) {
                return true;
            }

            if(!buffer.current().type().equals(LexemeType.OR) && !buffer.current().type().equals(LexemeType.AND)) {
                throw new SyntaxException("Некорректный оператор, идущий за сравниваемым выражением: "+buffer.current().type());
            }
            buffer.next();
        }
    }
}