/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.distribution.it;

import static org.apache.sling.distribution.it.DistributionUtils.assertExists;
import static org.apache.sling.distribution.it.DistributionUtils.distributeDeep;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.apache.sling.distribution.DistributionRequestType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ForwardBinaryDistributionTest extends DistributionIntegrationTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> generateData() {
        return Arrays.asList(new Object[][] {
                //{ true },
                { false },
        });
    }

	public ForwardBinaryDistributionTest(boolean useSharedDatastore) {
        // use instances with shared datastore
		super(useSharedDatastore);
	}

	@Test
	public void testBinaryDistribution() throws Exception {
        byte[] bytes = new byte[6000];
        new Random().nextBytes(bytes);
		InputStream data = new ByteArrayInputStream(bytes);
		String nodePath = "/content/asset.txt";
		authorClient.upload(nodePath, data, -1, true);

		assertExists(authorClient, nodePath);
        distributeDeep(author, "publish", DistributionRequestType.ADD, nodePath);
        assertExists(publishClient, nodePath);
        //TODO: also inspect the package size in binaryless case
	}
}
