/*
 * Copyright 2013-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.sentinel.datasource.factorybean;

import java.io.File;
import java.nio.charset.Charset;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.FileRefreshableDataSource;
import org.springframework.beans.factory.FactoryBean;

/**
 * A {@link FactoryBean} for creating {@link FileRefreshableDataSource} instance.
 *
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 * @see FileRefreshableDataSource
 */
public class FileRefreshableDataSourceFactoryBean implements FactoryBean<FileRefreshableDataSource> {

    @Nullable
    private String file;

    @Nullable
    private String charset;

    private long recommendRefreshMs;

    private int bufSize;

    @Nullable
    private Converter converter;

    @Override
    public FileRefreshableDataSource getObject() throws Exception {
        if (file == null || charset == null) {
            throw new IllegalStateException("file and charset must not be null");
        }
        return new FileRefreshableDataSource(new File(file), converter, recommendRefreshMs, bufSize, Charset.forName(charset));
    }

    @Override
    public Class<?> getObjectType() {
        return FileRefreshableDataSource.class;
    }

    @Nullable
    public String getFile() {
        // TODO
        return null;
    }

    public void setFile(@Nullable String file) {
        // TODO
    }

    @Nullable
    public String getCharset() {
        // TODO
        return null;
    }

    public void setCharset(@Nullable String charset) {
        // TODO
    }

    public long getRecommendRefreshMs() {
        // TODO
        return 0L;
    }

    public void setRecommendRefreshMs(long recommendRefreshMs) {
        // TODO
    }

    public int getBufSize() {
        // TODO
        return 0;
    }

    public void setBufSize(int bufSize) {
        // TODO
    }

    @Nullable
    public Converter getConverter() {
        // TODO
        return null;
    }

    public void setConverter(@Nullable Converter converter) {
        this.converter = converter;
    }
}
