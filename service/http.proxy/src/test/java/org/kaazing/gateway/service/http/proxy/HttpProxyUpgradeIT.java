/**
 * Copyright (c) 2007-2014 Kaazing Corporation. All rights reserved.
 *
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

package org.kaazing.gateway.service.http.proxy;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.gateway.server.test.GatewayRule;
import org.kaazing.gateway.server.test.config.GatewayConfiguration;
import org.kaazing.gateway.server.test.config.builder.GatewayConfigurationBuilder;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;

import java.net.URI;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;

public class HttpProxyUpgradeIT {

    @Rule
    public TestRule timeout = new DisableOnDebug(new Timeout(10, SECONDS));

    private final K3poRule robot = new K3poRule();

    private final GatewayRule gateway = new GatewayRule() {
        {
            // @formatter:off
            GatewayConfiguration configuration =
                    new GatewayConfigurationBuilder()
                        .service()
                            .accept(URI.create("http://localhost:8110"))
                            .connect(URI.create("http://localhost:8080"))
                            .type("http.proxy")
                        .done()
                    .done();
            // @formatter:on
            init(configuration);
        }
    };

    @Rule
    public TestRule chain = outerRule(robot).around(gateway);

    @Specification("http.proxy.upgrade.websocket")
    @Test
    public void upgradeWebSocket() throws Exception {
        robot.finish();
    }

    @Specification("http.proxy.upgrade.client.disconnect")
    @Test
    public void clientDisconnect() throws Exception {
        robot.finish();
    }

    @Specification("http.proxy.upgrade.server.disconnect")
    @Test
    public void serverDisconnect() throws Exception {
        robot.finish();
    }

    @Specification("http.proxy.upgrade.websocket.basic.auth")
    @Test
    public void upgradeBasicAuthWebSocket() throws Exception {
        robot.finish();
    }

}
