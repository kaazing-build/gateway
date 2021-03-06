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

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.kaazing.gateway.server.test.GatewayRule;
import org.kaazing.gateway.server.test.config.GatewayConfiguration;
import org.kaazing.gateway.server.test.config.builder.GatewayConfigurationBuilder;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.rules.RuleChain.outerRule;

public class HttpProxySecureIT {
    private static KeyStore keyStore;
    private static char[] password;

    private static SSLSocketFactory clientSocketFactory;

    @BeforeClass
    public static void initClass() throws Exception {
        BasicConfigurator.configure();

        // Initialize KeyStore of gateway
        password = "ab987c".toCharArray();
        keyStore = KeyStore.getInstance("JCEKS");
        FileInputStream kis = new FileInputStream("target/truststore/keystore.db");
        keyStore.load(kis, password);
        kis.close();

        // Initialize TrustStore of gateway
        KeyStore trustStore = KeyStore.getInstance("JKS");
        FileInputStream tis = new FileInputStream("target/truststore/truststore.db");
        trustStore.load(tis, null);
        tis.close();

        // Configure client socket factory to trust the gateway's certificate
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        sslContext.init(null, tmf.getTrustManagers(), null);
        clientSocketFactory = sslContext.getSocketFactory();
    }

    private final K3poRule robot = new K3poRule();

    private final GatewayRule gateway = new GatewayRule() {{

        // @formatter:off
        GatewayConfiguration configuration =
                new GatewayConfigurationBuilder()
                    .service()
                        .accept(URI.create("https://localhost:8110"))
                        .connect(URI.create("https://localhost:8080"))
                        .connectOption("ssl.encryption", "disabled")
                        .type("http.proxy")
                        .connectOption("http.keepalive", "disabled")
                        .done()
                    .security()
                        .keyStore(keyStore)
                        .keyStorePassword(password)
                    .done()
                .done();
        // @formatter:on
        init(configuration);

    }};

    @Rule
    public TestRule chain = outerRule(robot).around(gateway);

    // Test for gateway's ssl termination
    @Specification( "http.proxy.ssl.terminated")
    @Test(timeout = 5000)
    public void httpProxySslTerminated() throws Exception {
        URL url = new URL("https://localhost:8110/index.html");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setSSLSocketFactory(clientSocketFactory);
        try(BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line = r.readLine();
            assertEquals("<html>Hellooo</html>", line);
            assertNull(null, r.readLine());
        }
        
        robot.finish();
    }

}