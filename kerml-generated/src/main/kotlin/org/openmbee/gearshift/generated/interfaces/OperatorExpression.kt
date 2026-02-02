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

package org.openmbee.gearshift.generated.interfaces

/**
 * An OperatorExpression is an InvocationExpression whose function is determined by resolving its operator in the context of one of the standard packages from the Kernel Function Library.
 */
interface OperatorExpression : InvocationExpression {

    /**
     * An operator symbol that names a corresponding Function from one of the standard packages from the Kernel Function Library.
     */
    var operator: String

    /**
     * The instantiatedType of an OperatorExpression is the resolution of its operator from one of the packages BaseFunctions, DataFunctions, or ControlFunctions from the Kernel Function Library.
     */
    override fun instantiatedType(): Type?
}

