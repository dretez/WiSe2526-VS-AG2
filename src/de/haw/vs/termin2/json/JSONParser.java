package de.haw.vs.termin2.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JSONParser {
    private int index;
    private final String json;

    private JSONParser(String json) {
        this.json = json.trim();
        index = 0;
    }

    public static Object parse(String json) {
        return new JSONParser(json).parseValue();
    }

    private Object parseValue() {
        skipWhitespace();
        char c = peek();
        switch (c) {
            case '{': return parseObject();
            case '[': return parseArray();
            case '"': return parseString();
            case 't':
            case 'f': return parseBoolean();
            case 'n': return parseNull();
            default: return parseNumber();
        }
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> obj = new LinkedHashMap<>();
        expect('{');
        skipWhitespace();
        if (peek() == '}') {
            index++;
            return obj;
        }

        while (true) {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            expect(':');
            skipWhitespace();
            Object value = parseValue();
            obj.put(key, value);
            skipWhitespace();
            if (peek() == ',') {
                index++;
            } else if (peek() == '}') {
                index++;
                break;
            }
        }

        return obj;
    }

    private List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        expect('[');
        skipWhitespace();
        if (peek() == ']') {
            index++;
            return list;
        }

        while (true) {
            skipWhitespace();
            list.add(parseValue());
            skipWhitespace();
            if (peek() == ',') {
                index++;
            } else if (peek() == ']') {
                index++;
                break;
            }
        }

        return list;
    }

    private String parseString() {
        expect('"');
        StringBuilder sb = new StringBuilder();
        while (peek() != '"') {
            if (peek() == '\\') {
                index++;
                char esc = json.charAt(index++);
                switch (esc) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case 'r': sb.append('\r'); break;
                    default: sb.append(esc); break;
                }
            } else {
                sb.append(json.charAt(index++));
            }
        }
        index++; // skip closing "
        return sb.toString();
    }

    private Boolean parseBoolean() {
        if (json.startsWith("true", index)) {
            index += 4;
            return true;
        } else if (json.startsWith("false", index)) {
            index += 5;
            return false;
        }
        throw new RuntimeException("Invalid boolean at position " + index);
    }

    private Object parseNull() {
        if (json.startsWith("null", index)) {
            index += 4;
            return null;
        }
        throw new RuntimeException("Invalid null at position " + index);
    }

    private Number parseNumber() {
        int start = index;
        if (peek() == '-') index++;
        while (Character.isDigit(peek())) index++;
        if (peek() == '.') {
            index++;
            while (Character.isDigit(peek())) index++;
            return Double.parseDouble(json.substring(start, index));
        }
        return Integer.parseInt(json.substring(start, index));
    }

    private void skipWhitespace() {
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) index++;
    }

    private char peek() {
        return json.charAt(index);
    }

    private void expect(char c) {
        if (json.charAt(index) != c) throw new RuntimeException("Expected '" + c + "' at " + index);
        index++;
    }
}
