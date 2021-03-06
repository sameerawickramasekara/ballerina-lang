/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.ballerinalang.stdlib.auth;

import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.test.util.BCompileUtil;
import org.ballerinalang.test.util.BRunUtil;
import org.ballerinalang.test.util.CompileResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Basic auth header authentication handler testcase.
 */
public class BasicAuthHandlerTest {

    private CompileResult compileResult;

    @BeforeClass
    public void setup() {
        String resourceRoot = Paths.get("src", "test", "resources").toAbsolutePath().toString();
        Path sourceRoot = Paths.get(resourceRoot, "test-src", "auth");
        compileResult = BCompileUtil.compile(sourceRoot.resolve("basic-auth-handler-test.bal").toString());
    }

    @Test(description = "Test case for basic auth header interceptor canProcess method, without the basic auth header")
    public void testCanProcessHttpBasicAuthWithoutHeader() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testCanProcessHttpBasicAuthWithoutHeader");
        Assert.assertTrue(returns[0] instanceof BBoolean);
        Assert.assertFalse(((BBoolean) returns[0]).booleanValue());
    }

    @Test(description = "Test case for basic auth header interceptor canProcess method")
    public void testCanProcessHttpBasicAuth() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testCanProcessHttpBasicAuth");
        Assert.assertTrue(returns[0] instanceof BBoolean);
        Assert.assertTrue(((BBoolean) returns[0]).booleanValue());
    }

    @Test(description = "Test case for basic auth header interceptor authentication failure")
    public void testHandleHttpBasicAuthFailure() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testHandleHttpBasicAuthFailure");
        Assert.assertTrue(returns[0] instanceof BBoolean);
        Assert.assertFalse(((BBoolean) returns[0]).booleanValue());
    }

    @Test(description = "Test case for basic auth header interceptor authentication success")
    public void testHandleHttpBasicAuth() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testHandleHttpBasicAuth");
        Assert.assertTrue(returns[0] instanceof BBoolean);
        Assert.assertTrue(((BBoolean) returns[0]).booleanValue());
    }
}
