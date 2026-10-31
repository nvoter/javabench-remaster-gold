/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.wall;

import com.alibaba.druid.wall.spi.*;

public class WallUtils {

    public static boolean isValidateDB2(String sql) {
        DB2WallProvider provider = new DB2WallProvider();
        return provider.checkValid(sql);
    }

    public static boolean isValidateDB2(String sql, WallConfig config) {
        DB2WallProvider provider = new DB2WallProvider(config);
        return provider.checkValid(sql);
    }

    public static boolean isValidatePostgres(String sql) {
        // TODO
        return false;
    }

    public static boolean isValidatePostgres(String sql, WallConfig config) {
        // TODO
        return false;
    }

    public static boolean isValidateMySql(String sql) {
        // TODO
        return false;
    }

    public static boolean isValidateMySql(String sql, WallConfig config) {
        // TODO
        return false;
    }

    public static boolean isValidateOracle(String sql) {
        // TODO
        return false;
    }

    public static boolean isValidateOracle(String sql, WallConfig config) {
        // TODO
        return false;
    }

    public static boolean isValidateSqlServer(String sql) {
        // TODO
        return false;
    }

    public static boolean isValidateSqlServer(String sql, WallConfig config) {
        // TODO
        return false;
    }
}
