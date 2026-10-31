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
package com.alibaba.cloud.sentinel.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
public class FallbackProperties {

    /**
     * The fallback mode for sentinel spring-cloud-gateway. choose `redirect` or
     * `response`.
     */
    @Nullable
    private String mode;

    /**
     * Redirect Url for `redirect` mode.
     */
    @Nullable
    private String redirect;

    /**
     * Response Body for `response` mode.
     */
    @Nullable
    private String responseBody;

    /**
     * Response Status for `response` mode.
     */
    private Integer responseStatus = HttpStatus.TOO_MANY_REQUESTS.value();

    /**
     * Content-Type for `response` mode.
     */
    private String contentType = MediaType.APPLICATION_JSON.toString();

    @Nullable
    public String getMode() {
        // TODO
        return null;
    }

    public FallbackProperties setMode(@Nullable String mode) {
        // TODO
        return null;
    }

    @Nullable
    public String getRedirect() {
        // TODO
        return null;
    }

    public FallbackProperties setRedirect(@Nullable String redirect) {
        // TODO
        return null;
    }

    @Nullable
    public String getResponseBody() {
        // TODO
        return null;
    }

    public FallbackProperties setResponseBody(@Nullable String responseBody) {
        // TODO
        return null;
    }

    public Integer getResponseStatus() {
        // TODO
        return null;
    }

    public FallbackProperties setResponseStatus(Integer responseStatus) {
        // TODO
        return null;
    }

    public String getContentType() {
        // TODO
        return null;
    }

    public FallbackProperties setContentType(@Nullable String contentType) {
        this.contentType = contentType != null ? contentType : MediaType.APPLICATION_JSON.toString();
        return this;
    }
}
