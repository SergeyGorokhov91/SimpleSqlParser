package com.digdes.school.parsing.expressions;

import com.digdes.school.lexing.Lexeme;
import com.digdes.school.parsing.DatabaseManager;
import com.digdes.school.parsing.TypeChecker;
import com.digdes.school.parsing.ValueParser;
import com.digdes.school.exception.SyntaxException;

import java.util.Map;

public class SimpleExpression implements Expression {
    DatabaseManager db;
    private Lexeme comparableField;
    private Lexeme comparisonOperator;
    private Lexeme value;

    public SimpleExpression(DatabaseManager db) {
        this.db = db;
    }

    public void setComparableField(Lexeme comparableField) {
        this.comparableField = comparableField;
    }

    public void setComparisonOperator(Lexeme comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public void setValue(Lexeme value) {
        this.value = value;
    }

    public boolean execute(Map<String, Object> row) {
        TypeChecker checker = new TypeChecker(db);
        checker.checkKeyValuePair(comparableField, value);
        Object finalValue = ValueParser.parse(value);

        if (comparisonOperator.value().equals("=")) {
            if (comparableField.value() != null) {
                return finalValue.equals(row.get(comparableField.value()));
            }
            return false;
        } else if (comparisonOperator.value().equals("!=")) {
            if (comparableField.value() != null) {
                return !finalValue.equals(row.get(comparableField.value()));
            }
            return true;
        } else if (comparisonOperator.value().equals(">=")) {
            if (finalValue instanceof Long) {
                if (row.containsKey((String) comparableField.value())) {
                    if (row.get(comparableField.value()) == null) {
                        return false;
                    }
                    return (Long) row.get((String) comparableField.value()) >= (Long) finalValue;
                } else {
                    return false;
                }
            } else if (finalValue instanceof Double) {
                if (row.containsKey(comparableField.value())) {
                    if (row.get(comparableField.value()) == null) {
                        return false;
                    }
                    return (Double) row.get(comparableField.value()) >= (Double) finalValue;
                } else {
                    return false;
                }
            } else {
                throw new SyntaxException("неподходящий тип для сравнения.");
            }
        } else if (comparisonOperator.value().equals("<=")) {
            if (finalValue instanceof Long) {
                if (row.containsKey(comparableField.value())) {
                    if (row.get(comparableField.value()) == null) {
                        return false;
                    }
                    return (Long) row.get(comparableField.value()) <= (Long) finalValue;
                } else {
                    return false;
                }
            } else if (finalValue instanceof Double) {
                if (row.containsKey(comparableField.value())) {
                    if (row.get(comparableField.value()) == null) {
                        return false;
                    }
                    return (Double) row.get(comparableField.value()) <= (Double) finalValue;
                } else {
                    return false;
                }
            } else {
                throw new SyntaxException("неподходящий тип для сравнения.");
            }
        } else if (comparisonOperator.value().equals(">")) {
            if (finalValue instanceof Long) {
                if (row.containsKey(comparableField.value())) {
                    if (row.get(comparableField.value()) == null) {
                        return false;
                    }
                    return (Long) row.get(comparableField.value()) > (Long) finalValue;
                } else {
                    return false;
                }
            } else if (finalValue instanceof Double) {
                if (row.containsKey(comparableField.value())) {
                    if (row.get(comparableField.value()) == null) {
                        return false;
                    }
                    return (Double) row.get(comparableField.value()) > (Double) finalValue;
                } else {
                    return false;
                }
            } else {
                throw new SyntaxException("неподходящий тип для сравнения.");
            }
        } else if (comparisonOperator.value().equals("<")) {
            if (finalValue instanceof Long) {
                if (row.containsKey(comparableField.value())) {
                    if (row.get(comparableField.value()) == null) {
                        return false;
                    }
                    return (Long) row.get(comparableField.value()) < (Long) finalValue;
                } else {
                    return false;
                }
            } else if (finalValue instanceof Double) {
                if (row.containsKey(comparableField.value())) {
                    if (row.get(comparableField.value()) == null) {
                        return false;
                    }
                    return (Double) row.get(comparableField.value()) < (Double) finalValue;
                } else {
                    return false;
                }
            } else {
                throw new SyntaxException("неподходящий тип для сравнения.");
            }
        } else if (comparisonOperator.value().equals("like")) {
            if (row.get(comparableField.value()) == null) {
                return false;
            }

            if (finalValue instanceof String str) {
                if (str.startsWith("%") && str.endsWith("%")) {
                    String substring = str.substring(1, str.length() - 1);
                    if (row.get(comparableField.value()) instanceof String str2) {
                        return str2.contains(substring);
                    }

                } else if (str.startsWith("%")) {
                    String substring = str.substring(1);
                    if (row.get(comparableField.value()) instanceof String str2) {
                        return str2.endsWith(substring);
                    }
                } else if (str.endsWith("%")) {
                    String substring = str.substring(0, str.length() - 1);
                    if (row.get(comparableField.value()) instanceof String str2) {
                        return str2.startsWith(substring);
                    }
                } else {
                    throw new SyntaxException("При использовании оператора like необходимо использование спецсимвола '%'");
                }
            } else {
                throw new SyntaxException("сравнение с помощью like возможно только со стороковыми литералами.");
            }

        } else if (comparisonOperator.value().equals("ilike")) {
            if (row.get(comparableField.value()) == null) {
                return false;
            }

            if (finalValue instanceof String str) {
                if (str.startsWith("%") && str.endsWith("%")) {
                    String substring = str.substring(1, str.length() - 1);
                    if (row.get(comparableField.value()) instanceof String str2) {
                        return str2.toLowerCase().contains(substring.toLowerCase());
                    }

                } else if (str.startsWith("%")) {
                    String substring = str.substring(1);
                    if (row.get(comparableField.value()) instanceof String str2) {
                        return str2.toLowerCase().endsWith(substring.toLowerCase());
                    }
                } else if (str.endsWith("%")) {
                    String substring = str.substring(0, str.length() - 1);
                    if (row.get(comparableField.value()) instanceof String str2) {
                        return str2.toLowerCase().startsWith(substring.toLowerCase());
                    }
                } else {
                    throw new SyntaxException("При использовании оператора ilike необходимо использование спецсимвола '%'");
                }
            } else {
                throw new SyntaxException("сравнение с помощью ilike возможно только со стороковыми литералами.");
            }

        }
        throw new SyntaxException("почему-то оператор сравнения некорректен, хотя на этапе лексического анализа это должно было быть проверено");
    }
}
