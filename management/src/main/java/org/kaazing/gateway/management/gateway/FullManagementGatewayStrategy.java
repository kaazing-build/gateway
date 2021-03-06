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

package org.kaazing.gateway.management.gateway;

import org.apache.mina.core.write.WriteRequest;
import org.kaazing.gateway.management.Utils.ManagementSessionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * "Strategy" object to implement management processing. This is only done on non-management session requests.
 * <p/>
 * ALL REQUESTS WILL BE ON ONE OR ANOTHER OF THE IO THREADS, SO MUST NOT BLOCK!
 */
public class FullManagementGatewayStrategy extends CollectOnlyManagementGatewayStrategy {

    private static final Logger logger = LoggerFactory.getLogger(FullManagementGatewayStrategy.class);

    public FullManagementGatewayStrategy() {
    }

    @Override
    public void doSessionCreated(final GatewayManagementBean gatewayBean,
                                 long sessionId,
                                 ManagementSessionType managementSessionType) throws Exception {
        super.doSessionCreated(gatewayBean, sessionId, managementSessionType);
        gatewayBean.doSessionCreatedListeners(sessionId, managementSessionType);
    }

    @Override
    public void doSessionClosed(final GatewayManagementBean gatewayBean,
                                long sessionId,
                                ManagementSessionType managementSessionType) throws Exception {
        super.doSessionClosed(gatewayBean, sessionId, managementSessionType);
        gatewayBean.doSessionClosedListeners(sessionId, managementSessionType);
    }

    @Override
    public void doMessageReceived(final GatewayManagementBean gatewayBean,
                                  long sessionId,
                                  long sessionReadBytes,
                                  final Object message) throws Exception {
        super.doMessageReceived(gatewayBean, sessionId, sessionReadBytes, message);
        gatewayBean.doMessageReceivedListeners(sessionId, sessionReadBytes, message);
    }

    @Override
    public void doFilterWrite(final GatewayManagementBean gatewayBean,
                              long sessionId,
                              long sessionWrittenBytes,
                              final WriteRequest writeRequest) throws Exception {
        super.doFilterWrite(gatewayBean, sessionId, sessionWrittenBytes, writeRequest);
        gatewayBean.doFilterWriteListeners(sessionId, sessionWrittenBytes, writeRequest);
    }

    @Override
    public void doExceptionCaught(final GatewayManagementBean gatewayBean,
                                  long sessionId,
                                  final Throwable cause) throws Exception {
        super.doExceptionCaught(gatewayBean, sessionId, cause);
        gatewayBean.doExceptionCaughtListeners(sessionId, cause);
    }

    public String toString() {
        return "FULL_GATEWAY_STRATEGY";
    }

}
