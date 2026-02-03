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
 * KerML OperatorExpression metaclass.
 * Specializes: InvocationExpression
 * An OperatorExpression is an InvocationExpression whose function is determined by resolving its
 * operator in the context of one of the standard packages from the Kernel Function Library.
 */
fun createOperatorExpressionMetaClass() = MetaClass(
    name = "OperatorExpression",
    isAbstract = false,
    superclasses = listOf("InvocationExpression"),
    attributes = listOf(
        MetaProperty(
            name = "operator",
            type = "String",
            description = "An operator symbol that names a corresponding Function from one of the standard packages from the Kernel Function Library."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "instantiatedType",
            returnType = "Type",
            returnLowerBound = 0,
            returnUpperBound = 1,
            redefines = "instantiatedType",
            body = MetaOperation.ocl("""let libFunctions : Sequence(Element) = Sequence{'BaseFunctions', 'DataFunctions', 'ControlFunctions'}->collect(ns | resolveGlobal(ns + "::'" + operator + "'").memberElement) in if libFunctions->isEmpty() then null else libFunctions->first().oclAsType(Type) endif"""),
            description = "The instantiatedType of an OperatorExpression is the resolution of its operator from one of the packages BaseFunctions, DataFunctions, or ControlFunctions from the Kernel Function Library."
        )
    ),
    description = "An OperatorExpression is an InvocationExpression whose function is determined by resolving its operator in the context of one of the standard packages from the Kernel Function Library."
)
