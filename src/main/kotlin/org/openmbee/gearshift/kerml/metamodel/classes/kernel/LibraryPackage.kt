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
import MetaProperty

/**
 * KerML LibraryPackage metaclass.
 * Specializes: Package
 * A package that contains library elements.
 */
fun createLibraryPackageMetaClass() = MetaClass(
    name = "LibraryPackage",
    isAbstract = false,
    superclasses = listOf("Package"),
    attributes = listOf(
        MetaProperty(
            name = "isStandard",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this is a standard library package"
        )
    ),
    description = "A package that contains library elements"
)
