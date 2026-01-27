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
package org.openmbee.gearshift.kerml.parser.visitors.base

import org.openmbee.gearshift.generated.interfaces.FeatureMembership
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.generated.interfaces.Type
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.*

/**
 * Abstract base visitor for Type elements and their subclasses.
 *
 * Provides shared parsing methods for:
 * - typeBody / typeBodyElement
 * - featureMember / featureElement
 * - typeRelationshipPart
 * - ownership membership creation
 *
 * This follows the KerML inheritance hierarchy where Type is the base
 * for Classifier, Feature, and other typed elements.
 *
 * @param Ctx The ANTLR parser context type
 * @param Result The typed wrapper type (must extend Type)
 */
abstract class BaseTypeVisitor<Ctx, Result : Type> : BaseTypedVisitor<Ctx, Result>() {

    /**
     * Parse a type body (contains features, nested types, etc.).
     *
     * Grammar: typeBody = ';' | '{' typeBodyElement* '}'
     */
    protected fun parseTypeBody(
        ctx: KerMLParser.TypeBodyContext,
        parseContext: ParseContext
    ) {
        ctx.typeBodyElement()?.forEach { bodyElement ->
            parseTypeBodyElement(bodyElement, parseContext)
        }
    }

    /**
     * Parse a type body element.
     *
     * Grammar: typeBodyElement = nonFeatureMember | featureMember | aliasMember | import_ | REGULAR_COMMENT
     */
    protected fun parseTypeBodyElement(
        ctx: KerMLParser.TypeBodyElementContext,
        parseContext: ParseContext
    ) {
        ctx.nonFeatureMember()?.let { nonFeature ->
            parseNonFeatureMember(nonFeature, parseContext)
            return
        }

        ctx.featureMember()?.let { featureMember ->
            parseFeatureMember(featureMember, parseContext)
            return
        }

        ctx.aliasMember()?.let { _ ->
            // TODO: Delegate to AliasMemberVisitor
            return
        }

        ctx.import_()?.let { imp ->
            ImportVisitor().visit(imp, parseContext)
            return
        }

        // REGULAR_COMMENT - informational only, no model element created
    }

    /**
     * Parse a non-feature member within a type body.
     */
    protected fun parseNonFeatureMember(
        ctx: KerMLParser.NonFeatureMemberContext,
        parseContext: ParseContext
    ) {
        // TODO: Parse memberPrefix for visibility

        ctx.memberElement()?.let { memberElement ->
            memberElement.nonFeatureElement()?.let { element ->
                // Delegate to NamespaceVisitor which has the full dispatch logic
                NamespaceVisitor().parseNonFeatureElement(element, parseContext)
            }

            memberElement.annotatingElement()?.let { annotating ->
                // Delegate annotation parsing to NamespaceVisitor
                parseAnnotatingElement(annotating, parseContext)
            }
        }
    }

    /**
     * Parse a feature member.
     *
     * Grammar: featureMember = typeFeatureMember | ownedFeatureMember
     */
    protected fun parseFeatureMember(
        ctx: KerMLParser.FeatureMemberContext,
        parseContext: ParseContext
    ) {
        ctx.typeFeatureMember()?.let { typeFeat ->
            typeFeat.featureElement()?.let { featureElement ->
                parseFeatureElement(featureElement, parseContext)
            }
            return
        }

        ctx.ownedFeatureMember()?.let { ownedFeat ->
            ownedFeat.featureElement()?.let { featureElement ->
                parseFeatureElement(featureElement, parseContext)
            }
        }
    }

    /**
     * Parse a feature element (dispatches to specific feature visitor).
     *
     * Grammar: featureElement = feature | step | expression | booleanExpression | invariant
     *                          | connector | bindingConnector | succession | flow | successionFlow
     */
    protected fun parseFeatureElement(
        ctx: KerMLParser.FeatureElementContext,
        parseContext: ParseContext
    ) {
        ctx.feature()?.let { feature ->
            FeatureVisitor().visit(feature, parseContext)
            return
        }

        ctx.step()?.let { step ->
            StepVisitor().visit(step, parseContext)
            return
        }

        ctx.expression()?.let { expr ->
            ExpressionVisitor().visit(expr, parseContext)
            return
        }

        ctx.booleanExpression()?.let { boolExpr ->
            BooleanExpressionVisitor().visit(boolExpr, parseContext)
            return
        }

        ctx.invariant()?.let { inv ->
            InvariantVisitor().visit(inv, parseContext)
            return
        }

        ctx.connector()?.let { conn ->
            ConnectorVisitor().visit(conn, parseContext)
            return
        }

        ctx.bindingConnector()?.let { binding ->
            BindingConnectorVisitor().visit(binding, parseContext)
            return
        }

        ctx.succession()?.let { succ ->
            SuccessionVisitor().visit(succ, parseContext)
            return
        }

        ctx.flow()?.let { flow ->
            FlowVisitor().visit(flow, parseContext)
            return
        }

        ctx.successionFlow()?.let { succFlow ->
            SuccessionFlowVisitor().visit(succFlow, parseContext)
            return
        }
    }

    /**
     * Parse a type relationship part (disjoining, unioning, intersecting, differencing).
     */
    protected fun parseTypeRelationshipPart(
        ctx: KerMLParser.TypeRelationshipPartContext,
        type: Type,
        parseContext: ParseContext
    ) {
        ctx.disjoiningPart()?.let { _ ->
            // TODO: Parse disjoining relationships
        }
        ctx.unioningPart()?.let { _ ->
            // TODO: Parse unioning relationships
        }
        ctx.intersectingPart()?.let { _ ->
            // TODO: Parse intersecting relationships
        }
        ctx.differencingPart()?.let { _ ->
            // TODO: Parse differencing relationships
        }
    }

    /**
     * Create an OwningMembership linking the element to its parent namespace.
     */
    protected fun createOwnershipMembership(
        element: org.openmbee.gearshift.generated.interfaces.Element,
        parseContext: ParseContext
    ) {
        parseContext.parent?.let { parent ->
            if (parent is Namespace) {
                val membership = parseContext.create<OwningMembership>()
                membership.memberElement = element
                element.declaredName?.let { membership.memberName = it }
                element.declaredShortName?.let { membership.memberShortName = it }
                membership.membershipOwningNamespace = parent
            }
        }
    }

    /**
     * Create a FeatureMembership linking a feature to its owning type.
     */
    protected fun createFeatureMembership(
        feature: org.openmbee.gearshift.generated.interfaces.Feature,
        parseContext: ParseContext
    ) {
        parseContext.parent?.let { parent ->
            if (parent is Type) {
                val membership = parseContext.create<FeatureMembership>()
                membership.memberElement = feature
                feature.declaredName?.let { membership.memberName = it }
                feature.declaredShortName?.let { membership.memberShortName = it }
                if (parent is Namespace) {
                    membership.membershipOwningNamespace = parent
                }
            }
        }
    }

    /**
     * Parse typePrefix for isAbstract flag.
     */
    protected fun parseTypePrefix(ctx: KerMLParser.TypePrefixContext?, type: Type) {
        ctx?.isAbstract?.let {
            type.isAbstract = true
        }
        // TODO: Parse prefixMetadataMember
    }

    /**
     * Parse an annotating element (dispatches to specific annotation visitor).
     *
     * Grammar: annotatingElement = comment | documentation | textualRepresentation | metadataFeature
     */
    protected fun parseAnnotatingElement(
        ctx: KerMLParser.AnnotatingElementContext,
        parseContext: ParseContext
    ) {
        ctx.comment()?.let { comment ->
            CommentVisitor().visit(comment, parseContext)
            return
        }

        ctx.documentation()?.let { doc ->
            DocumentationVisitor().visit(doc, parseContext)
            return
        }

        ctx.textualRepresentation()?.let { textRep ->
            TextualRepresentationVisitor().visit(textRep, parseContext)
            return
        }

        ctx.metadataFeature()?.let { metadata ->
            MetadataFeatureVisitor().visit(metadata, parseContext)
            return
        }
    }
}
