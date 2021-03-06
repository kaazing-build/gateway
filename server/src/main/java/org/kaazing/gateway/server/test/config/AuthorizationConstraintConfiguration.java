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

package org.kaazing.gateway.server.test.config;

import java.util.HashSet;
import java.util.Set;

public class AuthorizationConstraintConfiguration implements
        Configuration<SuppressibleAuthorizationConstraintConfiguration> {

    private final SuppressibleAuthorizationConstraintConfiguration _configuration;
    private final Set<Suppressible<String>> requiredRoles = new HashSet<>();
    private final Set<String> unsuppressibleRequiredRoles = Suppressibles.unsuppressibleSet(requiredRoles);

    public AuthorizationConstraintConfiguration() {
        _configuration = new SuppressibleAuthorizationConstraintConfigurationImpl();
        _configuration.setSuppression(Suppressibles.getDefaultSuppressions());
    }

    @Override
    public SuppressibleAuthorizationConstraintConfiguration getSuppressibleConfiguration() {
        return _configuration;
    }

    @Override
    public void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);
    }

    public void addRequireRole(String requiredRole) {
        unsuppressibleRequiredRoles.add(requiredRole);
    }

    public Set<String> getRequiredRoles() {
        return unsuppressibleRequiredRoles;
    }

    private class SuppressibleAuthorizationConstraintConfigurationImpl extends
            SuppressibleAuthorizationConstraintConfiguration {
        private Set<Suppression> _suppressions;

        @Override
        public Set<org.kaazing.gateway.server.test.config.SuppressibleConfiguration.Suppression> getSuppressions() {
            return _suppressions;
        }

        @Override
        public void setSuppression(Set<org.kaazing.gateway.server.test.config.SuppressibleConfiguration.Suppression>
                                                   suppressions) {
            _suppressions = suppressions;
        }

        @Override
        public void addRequiredRole(Suppressible<String> requiredRole) {
            requiredRoles.add(requiredRole);
        }

        @Override
        public Set<Suppressible<String>> getRequiredRoles() {
            return requiredRoles;
        }
    }

}
