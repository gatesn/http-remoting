/*
 * Copyright 2017 Palantir Technologies, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.remoting3.okhttp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public final class QosRetryOtherInterceptorTest extends TestBase {

    private static final Request REQUEST = new Request.Builder().url("http://127.0.0.1").build();
    private static final Response SUCCESS_RESPONSE = responseWithCode(REQUEST, 200);

    @Mock
    private UrlSelector uris;
    @Mock
    private Interceptor.Chain chain;

    private Interceptor interceptor;

    @Before
    public void before() throws Exception {
        when(chain.request()).thenReturn(REQUEST);
        interceptor = new QosRetryOtherInterceptor(uris);
    }

    @Test
    public void interceptorTriesOriginalRequestBeforeCheckingForTheNextUrl() throws Exception {
        when(chain.proceed(REQUEST)).thenReturn(SUCCESS_RESPONSE);
        assertThat(interceptor.intercept(chain)).isEqualTo(SUCCESS_RESPONSE);
        verifyNoMoreInteractions(uris);
    }
}
