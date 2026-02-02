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
package org.openmbee.gearshift.kerml.parser.visitors

import org.openmbee.gearshift.generated.interfaces.DataType
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor

/**
 * Visitor for DataType elements.
 *
 * Per KerML spec 8.2.5.1: DataTypes are classifiers that represent value types without identity.
 *
 * Grammar:
 * ```
 * datatype
 *     : typePrefix DATATYPE
 *       classifierDeclaration typeBody
 *     ;
 * ```
 *
 * DataType extends Classifier. Uses inherited parsing from BaseClassifierVisitor.
 */
class DataTypeVisitor : BaseClassifierVisitor<KerMLParser.DatatypeContext, DataType>() {

    override fun visit(ctx: KerMLParser.DatatypeContext, kermlParseContext: KermlParseContext): DataType {
        val dataType = kermlParseContext.create<DataType>()

        // Parse typePrefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), dataType)

        // Parse classifierDeclaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, dataType, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(dataType, dataType.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(dataType, kermlParseContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return dataType
    }
}
