/*
 * Copyright 2026 Charles Galey
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
package org.openmbee.gearshift.sysml.metamodel.classes

import org.openmbee.mdm.framework.meta.MetaClass

/**
 * SysML BindingConnectorAsUsage metaclass.
 * Specializes: ConnectorAsUsage, BindingConnector
 * A BindingConnectorAsUsage is both a BindingConnector and a ConnectorAsUsage.
 */
fun createBindingConnectorAsUsageMetaClass() = MetaClass(
    name = "BindingConnectorAsUsage",
    isAbstract = false,
    superclasses = listOf("ConnectorAsUsage", "BindingConnector"),
    attributes = emptyList(),
    description = "A BindingConnectorAsUsage is both a BindingConnector and a ConnectorAsUsage."
)
