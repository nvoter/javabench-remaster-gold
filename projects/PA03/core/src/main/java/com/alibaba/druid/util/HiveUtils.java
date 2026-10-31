package com.alibaba.druid.util;

import java.util.Set;

public class HiveUtils {

    private static Set<String> builtinDataTypes;

    public static boolean isBuiltinDataType(String dataType) {
        // TODO
        return false;
    }

    public static void loadDataTypes(Set<String> dataTypes) {
        Utils.loadFromFile("META-INF/druid/parser/hive/builtin_datatypes", dataTypes);
    }
}
