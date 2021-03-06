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

package org.kaazing.gateway.management;

import org.kaazing.gateway.management.config.ClusterConfigurationBean;
import org.kaazing.gateway.management.config.NetworkConfigurationBean;
import org.kaazing.gateway.management.config.RealmConfigurationBean;
import org.kaazing.gateway.management.config.SecurityConfigurationBean;
import org.kaazing.gateway.management.config.ServiceConfigurationBean;
import org.kaazing.gateway.management.config.ServiceDefaultsConfigurationBean;
import org.kaazing.gateway.management.gateway.GatewayManagementBean;
import org.kaazing.gateway.management.service.ServiceManagementBean;
import org.kaazing.gateway.management.session.SessionManagementBean;
import org.kaazing.gateway.management.system.CpuListManagementBean;
import org.kaazing.gateway.management.system.CpuManagementBean;
import org.kaazing.gateway.management.system.HostManagementBean;
import org.kaazing.gateway.management.system.JvmManagementBean;
import org.kaazing.gateway.management.system.NicListManagementBean;
import org.kaazing.gateway.management.system.NicManagementBean;
import org.kaazing.gateway.service.ServiceContext;

/**
 * A marker interface so that we can distinguish service instances that are being used for management from those that are being
 * used 'normally' as gateway services (e.g. echo, broadcast, etc.)  As of 3.3, there is only one ManagementService,
 * SnmpManagementService.  There may be more in future releases, if we have other protocols to support that actually require
 * service accepts and connects (JMX does not.)
 */
public interface ManagementServiceHandler {

    /**
     * Return the ServiceContext for the service this handler is processing.
     */
    ServiceContext getServiceContext();

    /**
     * Routine called when a new GatewayManagementBean has been added to the ManagementContext.  The service can wrap it in
     * whatever fashion is appropriate for the particular protocol (e.g. an OID for SNMP, an MBean for JMX.)
     */
    void addGatewayManagementBean(GatewayManagementBean gatewayManagementBean);

    /**
     * Routine called when a new ServiceManagementBean has been created and the service is supposed to 'wrap' it in whatever
     * fashion is appropriate for the particular protocol. (e.g. an OID for SNMP, an MBean for JMX.)
     */
    void addServiceManagementBean(ServiceManagementBean serviceManagementBean);

    /**
     * Process after a session-level data-bean has been added to the managementContext for a particular IoSession. Add any
     * wrapper that we need to the protocol-specific caches.
     *
     * @param sessionManagementBean
     */
    void addSessionManagementBean(SessionManagementBean sessionManagementBean);

    /**
     * Clean up state related to a specific session management bean.  For example, unregister the MXBean associated with the
     * session.
     *
     * @param sessionManagementBean
     */
    void removeSessionManagementBean(SessionManagementBean sessionManagementBean);

    void addClusterConfigurationBean(ClusterConfigurationBean clusterConfig);

    void addNetworkConfigurationBean(NetworkConfigurationBean networkMappingBean);

    void addSecurityConfigurationBean(SecurityConfigurationBean securityBean);

    void addRealmConfigurationBean(RealmConfigurationBean realmBean);

    void addServiceConfigurationBean(ServiceConfigurationBean serviceConfigurationBean);

    void addServiceDefaultsConfigurationBean(ServiceDefaultsConfigurationBean serviceDefaultsConfigurationBean);

    void addVersionInfo(GatewayManagementBean gatewayBean);

    void addSystemManagementBean(HostManagementBean systemManagementBean);

    void addCpuListManagementBean(CpuListManagementBean cpuListManagementBean);

    void addNicListManagementBean(NicListManagementBean nicListManagementBean);

    void addJvmManagementBean(JvmManagementBean jvmManagementBean);

    // Because the following two are actually contained within other management beans,
    // they're treated somewhat differently, and are not directly ManagementBean instances.
    void addCpuManagementBean(CpuManagementBean cpuManagementBean, String hostAndPid);

    void addNicManagementBean(NicManagementBean nicManagementBean, String hostAndPid);
}
