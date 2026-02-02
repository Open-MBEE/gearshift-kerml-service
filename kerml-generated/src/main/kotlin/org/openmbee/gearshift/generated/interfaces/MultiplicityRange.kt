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
 * A MultiplicityRange is a Multiplicity whose value is defined to be the (inclusive) range of natural numbers given by the result of a lowerBound Expression and the result of an upperBound Expression.
 */
interface MultiplicityRange : Multiplicity {

    val bound: List<Expression>

    val lowerBound: Expression?

    val upperBound: Expression

    /**
     * Check whether this MultiplicityRange represents the range bounded by the given values lower and upper, presuming the lowerBound and upperBound Expressions are model-level evaluable.
     */
    fun hasBounds(lower: Int, upper: Int): Boolean?

    /**
     * Evaluate the given bound Expression (at model level) and return the result represented as a MOF UnlimitedNatural value.
     */
    fun valueOf(bound: Expression?): Int?
}

