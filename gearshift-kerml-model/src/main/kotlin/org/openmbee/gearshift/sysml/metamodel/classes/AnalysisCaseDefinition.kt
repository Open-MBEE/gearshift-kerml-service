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
 * KerML AnalysisCaseDefinition metaclass.
 * Specializes: CaseDefinition
 * A definition of an analysis case.
 */
fun createAnalysisCaseDefinitionMetaClass() = MetaClass(
    name = "AnalysisCaseDefinition",
    isAbstract = false,
    superclasses = listOf("CaseDefinition"),
    attributes = emptyList(),
    description = "A definition of an analysis case"
)
