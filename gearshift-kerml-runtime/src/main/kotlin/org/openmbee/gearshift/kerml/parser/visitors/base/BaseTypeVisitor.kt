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

import org.openmbee.gearshift.generated.interfaces.Differencing
import org.openmbee.gearshift.generated.interfaces.Disjoining
import org.openmbee.gearshift.generated.interfaces.FeatureMembership
import org.openmbee.gearshift.generated.interfaces.Intersecting
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.generated.interfaces.Type
import org.openmbee.gearshift.generated.interfaces.Unioning
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
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
        kermlParseContext: KermlParseContext
    ) {
        ctx.typeBodyElement()?.forEach { bodyElement ->
            parseTypeBodyElement(bodyElement, kermlParseContext)
        }
    }

    /**
     * Parse a type body element.
     *
     * Grammar: typeBodyElement = nonFeatureMember | featureMember | aliasMember | import_ | REGULAR_COMMENT
     */
    protected fun parseTypeBodyElement(
        ctx: KerMLParser.TypeBodyElementContext,
        kermlParseContext: KermlParseContext
    ) {
        ctx.nonFeatureMember()?.let { nonFeature ->
            parseNonFeatureMember(nonFeature, kermlParseContext)
            return
        }

        ctx.featureMember()?.let { featureMember ->
            parseFeatureMember(featureMember, kermlParseContext)
            return
        }

        ctx.aliasMember()?.let { _ ->
            // TODO: Delegate to AliasMemberVisitor
            return
        }

        ctx.import_()?.let { imp ->
            ImportVisitor().visit(imp, kermlParseContext)
            return
        }

        // REGULAR_COMMENT - informational only, no model element created
    }

    /**
     * Parse a non-feature member within a type body.
     */
    protected fun parseNonFeatureMember(
        ctx: KerMLParser.NonFeatureMemberContext,
        kermlParseContext: KermlParseContext
    ) {
        val visibility = parseMemberPrefix(ctx.memberPrefix())
        val visCtx = kermlParseContext.copy(memberVisibility = visibility)

        ctx.memberElement()?.let { memberElement ->
            memberElement.nonFeatureElement()?.let { element ->
                NamespaceVisitor().parseNonFeatureElement(element, visCtx)
            }

            memberElement.annotatingElement()?.let { annotating ->
                parseAnnotatingElement(annotating, visCtx)
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
        kermlParseContext: KermlParseContext
    ) {
        ctx.typeFeatureMember()?.let { typeFeat ->
            val visibility = parseMemberPrefix(typeFeat.memberPrefix())
            val visCtx = kermlParseContext.copy(memberVisibility = visibility)
            typeFeat.featureElement()?.let { featureElement ->
                parseFeatureElement(featureElement, visCtx)
            }
            return
        }

        ctx.ownedFeatureMember()?.let { ownedFeat ->
            val visibility = parseMemberPrefix(ownedFeat.memberPrefix())
            val visCtx = kermlParseContext.copy(memberVisibility = visibility)
            ownedFeat.featureElement()?.let { featureElement ->
                parseFeatureElement(featureElement, visCtx)
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
        kermlParseContext: KermlParseContext
    ) {
        ctx.feature()?.let { feature ->
            FeatureVisitor().visit(feature, kermlParseContext)
            return
        }

        ctx.step()?.let { step ->
            StepVisitor().visit(step, kermlParseContext)
            return
        }

        ctx.expression()?.let { expr ->
            ExpressionVisitor().visit(expr, kermlParseContext)
            return
        }

        ctx.booleanExpression()?.let { boolExpr ->
            BooleanExpressionVisitor().visit(boolExpr, kermlParseContext)
            return
        }

        ctx.invariant()?.let { inv ->
            InvariantVisitor().visit(inv, kermlParseContext)
            return
        }

        ctx.connector()?.let { conn ->
            ConnectorVisitor().visit(conn, kermlParseContext)
            return
        }

        ctx.bindingConnector()?.let { binding ->
            BindingConnectorVisitor().visit(binding, kermlParseContext)
            return
        }

        ctx.succession()?.let { succ ->
            SuccessionVisitor().visit(succ, kermlParseContext)
            return
        }

        ctx.flow()?.let { flow ->
            FlowVisitor().visit(flow, kermlParseContext)
            return
        }

        ctx.successionFlow()?.let { succFlow ->
            SuccessionFlowVisitor().visit(succFlow, kermlParseContext)
            return
        }
    }

    /**
     * Parse a type relationship part (disjoining, unioning, intersecting, differencing).
     */
    protected fun parseTypeRelationshipPart(
        ctx: KerMLParser.TypeRelationshipPartContext,
        type: Type,
        kermlParseContext: KermlParseContext
    ) {
        ctx.disjoiningPart()?.let { disjPart ->
            disjPart.ownedDisjoining()?.forEach { ownedDisj ->
                val disjoining = kermlParseContext.create<Disjoining>()
                disjoining.typeDisjoined = type
                ownedDisj.qualifiedName()?.let { qn ->
                    val name = extractQualifiedName(qn)
                    kermlParseContext.registerReference(disjoining, "disjoiningType", name)
                }
            }
        }
        ctx.unioningPart()?.let { unionPart ->
            unionPart.unioning()?.forEach { u ->
                val unioning = kermlParseContext.create<Unioning>()
                u.unioningType?.let { qn ->
                    val name = extractQualifiedName(qn)
                    kermlParseContext.registerReference(unioning, "unioningType", name)
                }
            }
        }
        ctx.intersectingPart()?.let { interPart ->
            interPart.intersecting()?.forEach { i ->
                val intersecting = kermlParseContext.create<Intersecting>()
                i.intersectingType?.let { qn ->
                    val name = extractQualifiedName(qn)
                    kermlParseContext.registerReference(intersecting, "intersectingType", name)
                }
            }
        }
        ctx.differencingPart()?.let { diffPart ->
            diffPart.differencing()?.forEach { d ->
                val differencing = kermlParseContext.create<Differencing>()
                d.differencingType?.let { qn ->
                    val name = extractQualifiedName(qn)
                    kermlParseContext.registerReference(differencing, "differencingType", name)
                }
            }
        }
    }

    /**
     * Create an OwningMembership linking the element to its parent namespace.
     */
    protected fun createOwnershipMembership(
        element: org.openmbee.gearshift.generated.interfaces.Element,
        kermlParseContext: KermlParseContext
    ) {
        kermlParseContext.parent?.let { parent ->
            if (parent is Namespace) {
                val membership = kermlParseContext.create<OwningMembership>()
                // Use ownedMemberElement (redefines memberElement) for proper ownership link
                membership.ownedMemberElement = element
                element.declaredName?.let { membership.memberName = it }
                element.declaredShortName?.let { membership.memberShortName = it }
                membership.membershipOwningNamespace = parent
                membership.visibility = kermlParseContext.memberVisibility
            }
        }
    }

    /**
     * Create a FeatureMembership linking a feature to its owning type.
     */
    protected fun createFeatureMembership(
        feature: org.openmbee.gearshift.generated.interfaces.Feature,
        kermlParseContext: KermlParseContext
    ) {
        kermlParseContext.parent?.let { parent ->
            if (parent is Type) {
                val membership = kermlParseContext.create<FeatureMembership>()
                // Use ownedMemberElement (inherited from OwningMembership) for proper ownership link
                membership.ownedMemberElement = feature
                feature.declaredName?.let { membership.memberName = it }
                feature.declaredShortName?.let { membership.memberShortName = it }
                membership.visibility = kermlParseContext.memberVisibility
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
        kermlParseContext: KermlParseContext
    ) {
        ctx.comment()?.let { comment ->
            CommentVisitor().visit(comment, kermlParseContext)
            return
        }

        ctx.documentation()?.let { doc ->
            DocumentationVisitor().visit(doc, kermlParseContext)
            return
        }

        ctx.textualRepresentation()?.let { textRep ->
            TextualRepresentationVisitor().visit(textRep, kermlParseContext)
            return
        }

        ctx.metadataFeature()?.let { metadata ->
            MetadataFeatureVisitor().visit(metadata, kermlParseContext)
            return
        }
    }
}
