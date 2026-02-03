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

import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * KerML LibraryPackage metaclass.
 * Specializes: Package
 * A Package that is the container for a model library. A LibraryPackage is itself a library
 * Element as are all Elements that are directly or indirectly contained in it.
 */
fun createLibraryPackageMetaClass() = MetaClass(
    name = "LibraryPackage",
    isAbstract = false,
    superclasses = listOf("Package"),
    attributes = listOf(
        MetaProperty(
            name = "isStandard",
            type = "Boolean",
            description = "Whether this LibraryPackage contains a standard library model. This should only be set to true for LibraryPackages in the standard Kernel Model Libraries or in normative model libraries for a language built on KerML."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "libraryNamespace",
            returnType = "Namespace",
            description = "The libraryNamespace for a LibraryPackage is itself.",
            body = MetaOperation.ocl("self"),
            isQuery = true,
            redefines = "libraryNamespace"
        )
    ),
    description = "A Package that is the container for a model library"
)
