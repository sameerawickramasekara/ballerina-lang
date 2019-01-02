/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.ballerinalang.test.typedefs;

import org.ballerinalang.launcher.util.BCompileUtil;
import org.ballerinalang.launcher.util.BRunUtil;
import org.ballerinalang.launcher.util.CompileResult;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.model.values.BValueArray;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Type definition test cases.
 */
public class TypeDefinitionsTest {

    private static CompileResult compileResult;

    @BeforeClass
    public void setup() {
        compileResult = BCompileUtil.compile("test-src/typedefs/type-definitions.bal");
    }

    @Test
    public void testArray() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testArray");
        Assert.assertNotNull(returns[0]);
        Assert.assertEquals(((BValueArray) returns[0]).getInt(0), 1);
        Assert.assertEquals(((BValueArray) returns[0]).getInt(1), 2);
        Assert.assertEquals(((BValueArray) returns[0]).getInt(2), 3);
    }

    @Test
    public void testSimpleTuple() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testSimpleTuple");
        Assert.assertEquals(((BInteger) returns[0]).intValue(), 10);
        Assert.assertEquals(returns[1].stringValue(), "Ten");
    }

    @Test
    public void testMap() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testMap");
        Assert.assertNotNull(returns[0]);
        Assert.assertEquals(returns[0].stringValue(), "{\"Five\":5}");
    }

    @Test
    public void testValueType() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testValueType");
        Assert.assertNotNull(returns[0]);
        Assert.assertEquals(returns[0].stringValue(), "Ballerina");
    }

    @Test
    public void testRecord() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testRecord");
        Assert.assertNotNull(returns[0]);
        Assert.assertEquals(returns[0].stringValue(), "{f:\"Ballerina\"}");
    }

    @Test
    public void testObject() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testObject");
        Assert.assertNotNull(returns[0]);
        Assert.assertEquals(returns[0].stringValue(), "{}");
    }

    @Test
    public void testUnion() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testUnion");
        Assert.assertNotNull(returns[0]);
        Assert.assertEquals(returns[0].stringValue(), "{g:null}");
    }
}
