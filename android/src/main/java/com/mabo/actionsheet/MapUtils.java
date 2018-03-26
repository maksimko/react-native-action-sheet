package com.mabo.actionsheet;

import android.support.annotation.NonNull;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maksim on 20/03/2018.
 */

class MapUtils {
    private static Object extract(ReadableMap data, @NonNull String field) {
        if (data == null || !data.hasKey(field)) {
            return null;
        }

        Dynamic value = data.getDynamic(field);

        switch (value.getType()) {
            case Array: return value.asArray().toArrayList();
            case Map: return value.asMap();
            case Number: return value.asInt();
            case String: return value.asString();
            case Boolean: return value.asBoolean();
            default: return null;
        }
    }

    static <T> T get(ReadableMap data, @NonNull String path) {
        return get(data, path, null);
    }

    static <T> T get(ReadableMap data, @NonNull String path, T defaultValue) {
        if (data == null) {
            return null;
        }

        String field = path;
        ReadableMap workingSet = data;

        if (path.contains(".")) {
            List<String> fields = new ArrayList<>(Arrays.asList(path.split("\\.")));

            field = fields.remove(fields.size() - 1);

            for (String pathField : fields) {
                workingSet = get(workingSet, pathField);
            }
        }

        Object value = extract(workingSet, field);

        try {
            return (T)value;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
