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
 * A FeatureValue is a Membership that identifies a particular memberExpression that provides the value of the Feature that owns the FeatureValue.
 */
interface FeatureValue : OwningMembership {

    /**
     * Whether this FeatureValue is a concrete specification of the bound or initial value of the featureWithValue, or just a default value that may be overridden.
     */
    var isDefault: Boolean

    /**
     * Whether this FeatureValue specifies a bound value or an initial value for the featureWithValue.
     */
    var isInitial: Boolean

    val featureWithValue: Feature

    val value: Expression
}

