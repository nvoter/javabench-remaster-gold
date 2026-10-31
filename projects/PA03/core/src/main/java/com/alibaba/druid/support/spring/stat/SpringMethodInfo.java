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
package com.alibaba.druid.support.spring.stat;

import java.lang.reflect.Method;

public class SpringMethodInfo {

    private String signature;

    private Class<?> instanceClass;

    private Method method;

    public SpringMethodInfo(Class<?> instanceClass, Method method) {
        // TODO
    }

    public String getClassName() {
        // TODO
        return null;
    }

    public String getMethodName() {
        return method.getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + instanceClass.getName().hashCode();
        result = prime * result + method.getName().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO
        return false;
    }

    public static String getMethodSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        sb.append('(');
        Class<?>[] params = method.getParameterTypes();
        for (int j = 0; j < params.length; j++) {
            sb.append(params[j].getName());
            if (j < (params.length - 1)) {
                sb.append(',');
            }
        }
        sb.append(')');
        return sb.toString();
    }

    public String getSignature() {
        if (signature == null) {
            signature = getMethodSignature(method);
        }
        return signature;
    }
}
