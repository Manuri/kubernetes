/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinax.kubernetes.test.samples;

import org.ballerinax.kubernetes.exceptions.KubernetesPluginException;
import org.ballerinax.kubernetes.test.utils.DockerTestException;
import org.ballerinax.kubernetes.test.utils.KubernetesTestUtils;
import org.ballerinax.kubernetes.utils.KubernetesUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.ballerinax.kubernetes.KubernetesConstants.DOCKER;
import static org.ballerinax.kubernetes.KubernetesConstants.KUBERNETES;
import static org.ballerinax.kubernetes.test.utils.KubernetesTestUtils.getExposedPorts;

public class Sample13Test implements SampleTest {

    private final String sourceDirPath = SAMPLE_DIR + File.separator + "sample13";
    private final String targetPath = sourceDirPath + File.separator + "target" + File.separator + KUBERNETES;
    private final String coolDrinkPkgTargetPath = targetPath + File.separator + "cool_drink";
    private final String drinkStorePkgTargetPath = targetPath + File.separator + "drink_store";
    private final String hotDrinkPkgTargetPath = targetPath + File.separator + "hot_drink";
    private final String coolDrinkDockerImage = "cool_drink:latest";
    private final String drinkStoreDockerImage = "drink_store:latest";
    private final String hotDrinkDockerImage = "hot_drink:latest";

    @BeforeClass
    public void compileSample() throws IOException, InterruptedException {
        Assert.assertEquals(KubernetesTestUtils.compileBallerinaProject((SAMPLE_DIR + File.separator + "sample13")), 0);
    }

    @Test
    public void validateDockerfile() {
        Assert.assertTrue(new File(coolDrinkPkgTargetPath + File.separator + DOCKER + File.separator + "Dockerfile")
                .exists());
        Assert.assertTrue(new File(drinkStorePkgTargetPath + File.separator + DOCKER + File.separator + "Dockerfile")
                .exists());
        Assert.assertTrue(new File(hotDrinkPkgTargetPath + File.separator + DOCKER + File.separator + "Dockerfile")
                .exists());
    }

    @Test
    public void validateDockerImageCoolDrink() throws DockerTestException, InterruptedException {
        List<String> ports = getExposedPorts(coolDrinkDockerImage);
        Assert.assertEquals(ports.size(), 1);
        Assert.assertEquals(ports.get(0), "9090/tcp");
    }

    @Test
    public void validateDockerImageDrinkStore() throws DockerTestException, InterruptedException {
        List<String> ports = getExposedPorts(drinkStoreDockerImage);
        Assert.assertEquals(ports.size(), 1);
        Assert.assertEquals(ports.get(0), "9091/tcp");
    }
    
    @Test
    public void validateDockerImageHotDrink() throws DockerTestException, InterruptedException {
        List<String> ports = getExposedPorts(hotDrinkDockerImage);
        Assert.assertEquals(ports.size(), 1);
        Assert.assertEquals(ports.get(0), "9090/tcp");
    }

    @AfterClass
    public void cleanUp() throws KubernetesPluginException, DockerTestException, InterruptedException {
        KubernetesUtils.deleteDirectory(targetPath);
        KubernetesTestUtils.deleteDockerImage(drinkStoreDockerImage);
        KubernetesTestUtils.deleteDockerImage(coolDrinkDockerImage);
        KubernetesTestUtils.deleteDockerImage(hotDrinkDockerImage);
    }
}
