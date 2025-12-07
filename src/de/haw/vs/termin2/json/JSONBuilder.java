package de.haw.vs.termin2.json;

import java.sql.Array;
import java.util.*;

public class JSONBuilder {
    private final HashMap<String, Object> json;

    public JSONBuilder() {
        this.json = new HashMap<>();
    }

    public void putObject(String key, Map<String, Object> value) {
        put(key, value);
    }

    public void putArray(String key, Array array) {
        put(key, array);
    }

    public void putString(String key, String value) {
        put(key, value);
    }

    public void putBoolean(String key, boolean value) {
        put(key, value);
    }

    public void putNumber(String key, int value) {
        put(key, value);
    }

    public void putNumber(String key, double value) {
        put(key, value);
    }

    @Override
    public String toString() {
        return formatValue(json);
    }

    private String formatValue(Object value) {
        if (value instanceof Map<?,?>) return formatObject((Map<?, ?>) value);
        if (value == null) return "null";
        if (value instanceof Number || value instanceof Boolean) return value.toString();
        if (value instanceof List<?>) return formatArray((List<?>) value);
        return "\"" + value.toString().replace("\"", "\\\"") + "\"";
    }

    private String formatObject(Map<?, ?> value) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        var it = value.entrySet().iterator();
        while (it.hasNext()) {
            var entry = it.next();
            sb.append("\"").append(entry.getKey()).append("\":");
            sb.append(formatValue(entry.getValue()));
            if (it.hasNext()) sb.append(",");
        }
        sb.append("}");
        return sb.toString();
    }

    private String formatArray(List<?> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        var it = list.iterator();
        while (it.hasNext()) {
            var entry = it.next();
            sb.append(formatValue(entry));
            if (it.hasNext()) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private void put(String key, Object value) {
        ArrayList<String> parts = new ArrayList<>(Arrays.asList(key.split("\\.")));
        HashMap<String, Object> path = this.json;
        for (int i = 0; i < parts.size(); i++) {
            String part = parts.get(i).replaceFirst("\\[.*", "");
            ArrayList<String> indexes = new ArrayList<>(Arrays.asList(parts.get(i)
                    .replaceFirst(".*\\[", "[")
                    .replaceAll("(\\[[0-9]*]).*", "$1,")
                    .split(",")));
            for (var idx : indexes) { // Validate indexes
                try { Integer _ = Integer.parseInt(idx); }
                catch (NumberFormatException e) { throw new IllegalArgumentException("Expected an Integer, found \"" + idx + "\" instead"); }
            }

            if (i + 1 == parts.size()) {
                if (indexes.isEmpty()) {
                    path.put(part, value);
                } else {
                    Object obj = path.get(part);
                    if (!(obj instanceof List<?>))
                        obj = new ArrayList<>();
                    path.put(part, obj);
                    @SuppressWarnings("unchecked")
                    ArrayList<Object> list = (ArrayList<Object>) obj;
                    for (ListIterator<String> it = indexes.listIterator(); it.hasNext(); ) {
                        int index = Integer.parseInt(it.next());
                        if (it.hasNext()) {
                            ArrayList<Object> sublist = new ArrayList<>();
                            list.add(index, sublist);
                            list = sublist;
                        } else {
                            list.add(index, value);
                        }
                    }
                }
            } else {
                path = addPath(path, part);
            }
        }
    }

    private HashMap<String, Object> addPath(HashMap<String, Object> path, String keypath) {
        String part = keypath.replaceFirst("\\[.*", "");
        ArrayList<String> indexes = new ArrayList<>(Arrays.asList(keypath
                .replaceFirst(".*\\[", "[")
                .replaceAll("(\\[[0-9]*]).*", "$1,")
                .split(",")));
        for (var idx : indexes) { // Validate indexes
            try { Integer _ = Integer.parseInt(idx); }
            catch (NumberFormatException e) { throw new IllegalArgumentException("Expected an Integer, found \"" + idx + "\" instead"); }
        }
        var subpath = ((Map<?, ?>) path).get(part);
        if (subpath != null && !(subpath instanceof Map<?,?>) && !(subpath instanceof List<?>))
            throw new IllegalArgumentException("Invalid key path");
        if (subpath == null) {
            if (!indexes.isEmpty()) {
                ArrayList<Object> list = new ArrayList<>();
                path.put(part, list);
                for (ListIterator<String> it = indexes.listIterator(); it.hasNext(); ) {
                    int index = Integer.parseInt(it.next());
                    if (it.hasNext()) {
                        ArrayList<Object> sublist = new ArrayList<>();
                        list.add(index, sublist);
                        list = sublist;
                    } else {
                        HashMap<String, Object> obj = new HashMap<>();
                        list.add(index, obj);
                        path = obj;
                    }
                }
            } else {
                HashMap<String, Object> obj = new HashMap<>();
                path.put(part, obj);
                path = obj;
            }
        }
        return path;
    }
}
