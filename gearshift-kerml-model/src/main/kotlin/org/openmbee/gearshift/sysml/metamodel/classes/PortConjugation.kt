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
 * SysML PortConjugation metaclass.
 * Specializes: Conjugation
 * A PortConjugation is a Conjugation Relationship between a PortDefinition and its corresponding
 * ConjugatedPortDefinition. As a result of this Relationship, the ConjugatedPortDefinition inherits
 * all the features of the original PortDefinition, but input flows become outputs and output flows
 * become inputs.
 */
fun createPortConjugationMetaClass() = MetaClass(
    name = "PortConjugation",
    isAbstract = false,
    superclasses = listOf("Conjugation"),
    attributes = emptyList(),
    description = "A PortConjugation is a Conjugation Relationship between a PortDefinition and its corresponding ConjugatedPortDefinition."
)
