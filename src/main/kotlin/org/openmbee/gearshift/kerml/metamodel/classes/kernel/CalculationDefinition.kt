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
package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML CalculationDefinition metaclass.
 * Specializes: ActionDefinition, Function
 * A definition of a calculation.
 */
fun createCalculationDefinitionMetaClass() = MetaClass(
    name = "CalculationDefinition",
    isAbstract = false,
    superclasses = listOf("ActionDefinition", "Function"),
    attributes = emptyList(),
    description = "A definition of a calculation"
)
