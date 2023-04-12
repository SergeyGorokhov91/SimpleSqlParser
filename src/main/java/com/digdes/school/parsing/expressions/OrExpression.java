package com.digdes.school.parsing.expressions;

import java.util.*;

public class OrExpression implements Expression {
    private Expression left;
    private Expression right;

    public OrExpression() {

    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    public boolean execute(Map<String, Object> row) {
        boolean listLeft = left.execute(row);
        boolean listRight = right.execute(row);
        return listLeft || listRight;
    }
}