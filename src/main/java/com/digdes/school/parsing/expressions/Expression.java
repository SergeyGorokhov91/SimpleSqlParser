package com.digdes.school.parsing.expressions;

import java.util.Map;

public interface Expression {
    boolean execute(Map<String,Object> row);

}
