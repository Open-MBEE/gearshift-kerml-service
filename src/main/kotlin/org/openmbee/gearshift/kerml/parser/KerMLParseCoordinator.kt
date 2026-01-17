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
package org.openmbee.gearshift.kerml.parser

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.kerml.antlr.KerMLLexer
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import java.nio.file.Path

/**
 * Coordinates the parsing of KerML grammar elements into Gearshift engine instances.
 * This class serves as the main entry point for parsing KerML files.
 */
class KerMLParseCoordinator(
    private val engine: GearshiftEngine
) {
    /**
     * Tracks all parsed elements for reference resolution.
     * Maps qualified name -> instance ID
     */
    private val parsedElements = mutableMapOf<String, String>()

    /**
     * Tracks simple name -> instance ID for elements without full qualified names.
     */
    private val simpleNameIndex = mutableMapOf<String, MutableList<String>>()

    /**
     * Tracks unresolved references to be resolved after parsing.
     */
    private val unresolvedReferences = mutableListOf<UnresolvedReference>()

    /**
     * Namespace stack for computing qualified names during parsing.
     */
    private val namespaceStack = ArrayDeque<String>()

    /**
     * Parse KerML source code from a string.
     *
     * @param input The KerML source code
     * @return The parse result containing root element ID or errors
     */
    fun parseString(input: String): KerMLParseResult {
        val errorListener = KerMLErrorListener()

        val lexer = KerMLLexer(CharStreams.fromString(input))
        lexer.removeErrorListeners()
        lexer.addErrorListener(errorListener)

        val tokens = CommonTokenStream(lexer)
        val parser = KerMLParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(errorListener)

        val tree = parser.rootNamespace()

        if (errorListener.hasErrors) {
            return KerMLParseResult(
                success = false,
                errors = errorListener.errors
            )
        }

        return visitAndResolve(tree)
    }

    /**
     * Parse KerML source code from a file.
     *
     * @param path Path to the KerML file
     * @return The parse result containing root element ID or errors
     */
    fun parseFile(path: Path): KerMLParseResult {
        val errorListener = KerMLErrorListener()

        val lexer = KerMLLexer(CharStreams.fromPath(path))
        lexer.removeErrorListeners()
        lexer.addErrorListener(errorListener)

        val tokens = CommonTokenStream(lexer)
        val parser = KerMLParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(errorListener)

        val tree = parser.rootNamespace()

        if (errorListener.hasErrors) {
            return KerMLParseResult(
                success = false,
                errors = errorListener.errors
            )
        }

        return visitAndResolve(tree)
    }

    /**
     * Visit the parse tree and resolve references.
     */
    private fun visitAndResolve(tree: KerMLParser.RootNamespaceContext): KerMLParseResult {
        val rootId = parseRootNamespace(tree)

        val stillUnresolved = resolveReferences()

        return KerMLParseResult(
            success = stillUnresolved.isEmpty(),
            rootElementId = rootId,
            unresolvedReferences = stillUnresolved
        )
    }

    /**
     * Parse a root namespace (the entry point for a KerML file).
     *
     * @param rootContext The ANTLR root namespace context
     * @return The root namespace instance ID
     */
    fun parseRootNamespace(rootContext: KerMLParser.RootNamespaceContext): String? {
        // Create a root namespace to hold all top-level elements
        val (rootId, _) = engine.createInstance("Namespace")
        registerParsedElement("", rootId)

        // Process each namespace body element
        for (bodyElement in rootContext.namespaceBodyElement()) {
            parseNamespaceBodyElement(bodyElement, rootId, "")
        }

        return rootId
    }

    /**
     * Parse a namespace body element.
     */
    fun parseNamespaceBodyElement(
        ctx: KerMLParser.NamespaceBodyElementContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        // Handle namespaceMember (contains nonFeatureMember or namespaceFeatureMember)
        ctx.namespaceMember()?.let { member ->
            return parseNamespaceMember(member, parentId, parentQualifiedName)
        }

        // Handle aliasMember
        ctx.aliasMember()?.let { alias ->
            // TODO: Implement alias member parsing
            return null
        }

        // Handle import
        ctx.import_()?.let { import ->
            // TODO: Implement import parsing
            return null
        }

        return null
    }

    /**
     * Parse a namespace member.
     */
    fun parseNamespaceMember(
        ctx: KerMLParser.NamespaceMemberContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        // Handle nonFeatureMember
        ctx.nonFeatureMember()?.let { nonFeature ->
            return parseNonFeatureMember(nonFeature, parentId, parentQualifiedName)
        }

        // Handle namespaceFeatureMember
        ctx.namespaceFeatureMember()?.let { featureMember ->
            return parseFeatureMember(featureMember, parentId, parentQualifiedName)
        }

        return null
    }

    /**
     * Parse a non-feature member (packages, classes, etc).
     */
    fun parseNonFeatureMember(
        ctx: KerMLParser.NonFeatureMemberContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        val memberElement = ctx.memberElement() ?: return null

        // Process the member element
        val elementId = parseMemberElement(memberElement, parentId, parentQualifiedName)

        // If we got an element, create an OwningMembership to link it to the parent
        elementId?.let { id ->
            createOwningMembership(parentId, id)
        }

        return elementId
    }

    /**
     * Parse a member element (the actual package, class, etc).
     */
    fun parseMemberElement(
        ctx: KerMLParser.MemberElementContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        // Handle annotating elements
        ctx.annotatingElement()?.let { annotating ->
            // TODO: Implement annotating element parsing
            return null
        }

        // Handle non-feature elements
        ctx.nonFeatureElement()?.let { nonFeature ->
            return parseNonFeatureElement(nonFeature, parentId, parentQualifiedName)
        }

        return null
    }

    /**
     * Parse a non-feature element (dispatches to specific element parsers).
     */
    fun parseNonFeatureElement(
        ctx: KerMLParser.NonFeatureElementContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        // Dispatch to specific element visitors based on what's present
        ctx.package_()?.let { pkg ->
            return parsePackage(pkg, parentId, parentQualifiedName)
        }

        ctx.class_()?.let { cls ->
            return parseClass(cls, parentId, parentQualifiedName)
        }

        ctx.namespace()?.let { ns ->
            return parseNamespace(ns, parentId, parentQualifiedName)
        }

        ctx.datatype()?.let { dt ->
            return parseDatatype(dt, parentId, parentQualifiedName)
        }

        ctx.structure()?.let { struct ->
            return parseStructure(struct, parentId, parentQualifiedName)
        }

        ctx.association()?.let { assoc ->
            return parseAssociation(assoc, parentId, parentQualifiedName)
        }

        // TODO: Add more element types as needed

        return null
    }

    /**
     * Parse a Package element.
     */
    fun parsePackage(
        ctx: KerMLParser.PackageContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        val (instanceId, _) = engine.createInstance("Package")

        // Parse identification from packageDeclaration
        ctx.packageDeclaration()?.identification()?.let { id ->
            parseIdentification(id, instanceId)
        }

        // Get the declared name for qualified name computation
        val declaredName = engine.getProperty(instanceId, "declaredName") as? String ?: ""
        val qualifiedName = computeQualifiedName(parentQualifiedName, declaredName)

        // Register the element
        registerParsedElement(qualifiedName, instanceId)

        // Push namespace for nested elements
        namespaceStack.addLast(declaredName)

        // Parse package body
        ctx.packageBody()?.let { body ->
            parsePackageBody(body, instanceId, qualifiedName)
        }

        // Pop namespace
        namespaceStack.removeLast()

        return instanceId
    }

    /**
     * Parse a package body.
     */
    fun parsePackageBody(
        ctx: KerMLParser.PackageBodyContext,
        parentId: String,
        parentQualifiedName: String
    ) {
        // Process namespace body elements
        for (bodyElement in ctx.namespaceBodyElement()) {
            parseNamespaceBodyElement(bodyElement, parentId, parentQualifiedName)
        }
    }

    /**
     * Parse a Class element.
     */
    fun parseClass(
        ctx: KerMLParser.ClassContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        val (instanceId, _) = engine.createInstance("Class")

        // Parse isAbstract from typePrefix
        ctx.typePrefix()?.isAbstract?.let {
            engine.setProperty(instanceId, "isAbstract", true)
        }

        // Parse classifierDeclaration
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, instanceId, parentId, parentQualifiedName)
        }

        // Get the declared name
        val declaredName = engine.getProperty(instanceId, "declaredName") as? String ?: ""
        val qualifiedName = computeQualifiedName(parentQualifiedName, declaredName)

        // Register the element
        registerParsedElement(qualifiedName, instanceId)

        // Push namespace for nested elements
        namespaceStack.addLast(declaredName)

        // Parse type body
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, instanceId, qualifiedName)
        }

        // Pop namespace
        namespaceStack.removeLast()

        return instanceId
    }

    /**
     * Parse a classifier declaration.
     */
    fun parseClassifierDeclaration(
        decl: KerMLParser.ClassifierDeclarationContext,
        instanceId: String,
        parentId: String,
        parentQualifiedName: String
    ) {
        // Parse identification
        decl.identification()?.let { id ->
            parseIdentification(id, instanceId)
        }

        // Parse isSufficient
        decl.isSufficient?.let {
            engine.setProperty(instanceId, "isSufficient", true)
        }

        // Parse superclassingPart (subclassifications)
        decl.superclassingPart()?.let { superclassing ->
            parseSuperclassingPart(superclassing, instanceId, parentQualifiedName)
        }
    }

    /**
     * Parse a superclassing part (subclassification relationships).
     */
    fun parseSuperclassingPart(
        ctx: KerMLParser.SuperclassingPartContext,
        instanceId: String,
        parentQualifiedName: String
    ) {
        for (subclassification in ctx.ownedSubclassification()) {
            parseOwnedSubclassification(subclassification, instanceId, parentQualifiedName)
        }
    }

    /**
     * Parse an owned subclassification.
     */
    fun parseOwnedSubclassification(
        ctx: KerMLParser.OwnedSubclassificationContext,
        classifierId: String,
        parentQualifiedName: String
    ) {
        // Create the Subclassification instance
        val (subId, _) = engine.createInstance("Subclassification")

        // Create link from classifier to subclassification
        // Classifier -> ownedSubclassification (owningClassifierOwnedSubclassificationAssociation)
        engine.createLink("owningClassifierOwnedSubclassificationAssociation", classifierId, subId)

        // Record the unresolved reference to the superclassifier
        // Subclassification -> superclassifier (superclassificationSuperclassifierAssociation)
        ctx.superclassifier?.let { superQn ->
            val superName = extractQualifiedName(superQn)
            recordUnresolvedReference(
                sourceInstanceId = subId,
                associationName = "superclassificationSuperclassifierAssociation",
                targetQualifiedName = superName,
                currentNamespace = parentQualifiedName
            )
        }
    }

    /**
     * Parse a type body.
     */
    fun parseTypeBody(
        ctx: KerMLParser.TypeBodyContext,
        parentId: String,
        parentQualifiedName: String
    ) {
        for (bodyElement in ctx.typeBodyElement()) {
            parseTypeBodyElement(bodyElement, parentId, parentQualifiedName)
        }
    }

    /**
     * Parse a type body element.
     */
    fun parseTypeBodyElement(
        ctx: KerMLParser.TypeBodyElementContext,
        parentId: String,
        parentQualifiedName: String
    ) {
        // Handle feature members
        ctx.featureMember()?.let { featureMember ->
            parseFeatureMember(featureMember, parentId, parentQualifiedName)
        }

        // Handle non-feature members
        ctx.nonFeatureMember()?.let { nonFeature ->
            parseNonFeatureMember(nonFeature, parentId, parentQualifiedName)
        }
    }

    /**
     * Parse a feature member.
     */
    fun parseFeatureMember(
        ctx: Any,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        when (ctx) {
            is KerMLParser.NamespaceFeatureMemberContext -> {
                // NamespaceFeatureMemberContext has featureElement() directly
                ctx.featureElement()?.let { featureElement ->
                    return parseFeatureElement(featureElement, parentId, parentQualifiedName)
                }
            }
            is KerMLParser.FeatureMemberContext -> {
                ctx.ownedFeatureMember()?.featureElement()?.let { featureElement ->
                    return parseFeatureElement(featureElement, parentId, parentQualifiedName)
                }
                ctx.typeFeatureMember()?.let { typeFeature ->
                    // TODO: Handle type feature member
                }
            }
        }
        return null
    }

    /**
     * Parse an owned feature member.
     */
    fun parseOwnedFeatureMember(
        ctx: KerMLParser.OwnedFeatureMemberContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        ctx.featureElement()?.let { featureElement ->
            return parseFeatureElement(featureElement, parentId, parentQualifiedName)
        }
        return null
    }

    /**
     * Parse a feature element.
     */
    fun parseFeatureElement(
        ctx: KerMLParser.FeatureElementContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        ctx.feature()?.let { feature ->
            return parseFeature(feature, parentId, parentQualifiedName)
        }
        // TODO: Handle other feature element types (step, expression, connector, etc.)
        return null
    }

    /**
     * Parse a Feature element.
     */
    fun parseFeature(
        ctx: KerMLParser.FeatureContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        val (instanceId, _) = engine.createInstance("Feature")

        // Parse feature prefix modifiers
        ctx.featurePrefix()?.let { prefix ->
            parseFeaturePrefix(prefix, instanceId)
        }

        // Parse feature declaration
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, instanceId, parentId, parentQualifiedName)
        }

        // Get the declared name
        val declaredName = engine.getProperty(instanceId, "declaredName") as? String ?: ""
        val qualifiedName = computeQualifiedName(parentQualifiedName, declaredName)

        // Register the element
        if (declaredName.isNotEmpty()) {
            registerParsedElement(qualifiedName, instanceId)
        }

        // Create OwningMembership to link feature to parent type
        createFeatureMembership(parentId, instanceId)

        return instanceId
    }

    /**
     * Parse a feature prefix.
     */
    fun parseFeaturePrefix(ctx: KerMLParser.FeaturePrefixContext, instanceId: String) {
        ctx.basicFeaturePrefix()?.let { basic ->
            basic.ABSTRACT()?.let { engine.setProperty(instanceId, "isAbstract", true) }
            basic.COMPOSITE()?.let { engine.setProperty(instanceId, "isComposite", true) }
            basic.PORTION()?.let { engine.setProperty(instanceId, "isPortion", true) }
            basic.DERIVED()?.let { engine.setProperty(instanceId, "isDerived", true) }
        }
    }

    /**
     * Parse a feature declaration.
     */
    fun parseFeatureDeclaration(
        decl: KerMLParser.FeatureDeclarationContext,
        instanceId: String,
        parentId: String,
        parentQualifiedName: String
    ) {
        // Parse feature identification
        decl.featureIdentification()?.let { id ->
            parseFeatureIdentification(id, instanceId)
        }

        // Parse feature specialization part (typings, subsettings, etc.)
        decl.featureSpecializationPart()?.let { specPart ->
            parseFeatureSpecializationPart(specPart, instanceId, parentQualifiedName)
        }
    }

    /**
     * Parse feature identification.
     */
    fun parseFeatureIdentification(id: KerMLParser.FeatureIdentificationContext, instanceId: String) {
        id.declaredShortName?.let { shortName ->
            engine.setProperty(instanceId, "declaredShortName", shortName.text)
        }
        id.declaredName?.let { name ->
            engine.setProperty(instanceId, "declaredName", name.text)
        }
    }

    /**
     * Parse feature specialization part.
     */
    fun parseFeatureSpecializationPart(
        ctx: KerMLParser.FeatureSpecializationPartContext,
        instanceId: String,
        parentQualifiedName: String
    ) {
        // Process each feature specialization
        for (featureSpec in ctx.featureSpecialization()) {
            parseFeatureSpecialization(featureSpec, instanceId, parentQualifiedName)
        }

        // Process multiplicity part if present
        ctx.multiplicityPart()?.let { multPart ->
            parseMultiplicityPart(multPart, instanceId)
        }
    }

    /**
     * Parse a feature specialization.
     */
    fun parseFeatureSpecialization(
        ctx: KerMLParser.FeatureSpecializationContext,
        instanceId: String,
        parentQualifiedName: String
    ) {
        // Handle typings (:)
        ctx.typings()?.let { typings ->
            parseTypings(typings, instanceId, parentQualifiedName)
        }

        // Handle subsettings (:>)
        ctx.subsettings()?.let { subsettings ->
            parseSubsettings(subsettings, instanceId, parentQualifiedName)
        }

        // Handle redefinitions (:>>)
        ctx.redefinitions()?.let { redefs ->
            parseRedefinitions(redefs, instanceId, parentQualifiedName)
        }
    }

    /**
     * Parse typings.
     * TypingsContext structure:
     * - typedBy(): first typing (has ownedFeatureTyping())
     * - ownedFeatureTyping(): list of additional typings
     */
    fun parseTypings(
        ctx: KerMLParser.TypingsContext,
        featureId: String,
        parentQualifiedName: String
    ) {
        // Process the first typing from typedBy()
        ctx.typedBy()?.ownedFeatureTyping()?.let { typing ->
            createFeatureTyping(typing, featureId, parentQualifiedName)
        }

        // Process additional typings from ownedFeatureTyping() list
        for (typing in ctx.ownedFeatureTyping()) {
            createFeatureTyping(typing, featureId, parentQualifiedName)
        }
    }

    /**
     * Create a FeatureTyping instance from an OwnedFeatureTypingContext.
     */
    private fun createFeatureTyping(
        typing: KerMLParser.OwnedFeatureTypingContext,
        featureId: String,
        parentQualifiedName: String
    ) {
        val (typingId, _) = engine.createInstance("FeatureTyping")
        // Feature -> ownedTyping (owningFeatureOwnedTypingAssociation)
        engine.createLink("owningFeatureOwnedTypingAssociation", featureId, typingId)

        // OwnedFeatureTypingContext.generalType().qualifiedName()
        // FeatureTyping -> type (typingByTypeTypeAssociation)
        typing.generalType()?.qualifiedName()?.let { qn ->
            val typeName = extractQualifiedName(qn)
            recordUnresolvedReference(
                sourceInstanceId = typingId,
                associationName = "typingByTypeTypeAssociation",
                targetQualifiedName = typeName,
                currentNamespace = parentQualifiedName
            )
        }
    }

    /**
     * Parse subsettings.
     * SubsettingsContext structure:
     * - subsets(): first subsetting (has ownedSubsetting())
     * - ownedSubsetting(): list of additional subsettings
     */
    fun parseSubsettings(
        ctx: KerMLParser.SubsettingsContext,
        featureId: String,
        parentQualifiedName: String
    ) {
        // Process the first subsetting from subsets()
        ctx.subsets()?.ownedSubsetting()?.let { subsetting ->
            createSubsetting(subsetting, featureId, parentQualifiedName)
        }

        // Process additional subsettings from ownedSubsetting() list
        for (subsetting in ctx.ownedSubsetting()) {
            createSubsetting(subsetting, featureId, parentQualifiedName)
        }
    }

    /**
     * Create a Subsetting instance from an OwnedSubsettingContext.
     */
    private fun createSubsetting(
        subsetting: KerMLParser.OwnedSubsettingContext,
        featureId: String,
        parentQualifiedName: String
    ) {
        val (subsettingId, _) = engine.createInstance("Subsetting")
        // Feature -> ownedSubsetting (owningFeatureOwnedSubsettingAssociation)
        engine.createLink("owningFeatureOwnedSubsettingAssociation", featureId, subsettingId)

        // OwnedSubsettingContext.generalType().qualifiedName()
        // Subsetting -> subsettedFeature (supersettingSubsettedFeatureAssociation)
        subsetting.generalType()?.qualifiedName()?.let { qn ->
            val targetName = extractQualifiedName(qn)
            recordUnresolvedReference(
                sourceInstanceId = subsettingId,
                associationName = "supersettingSubsettedFeatureAssociation",
                targetQualifiedName = targetName,
                currentNamespace = parentQualifiedName
            )
        }
    }

    /**
     * Parse redefinitions.
     * RedefinitionsContext structure:
     * - redefines(): first redefinition (has ownedRedefinition())
     * - ownedRedefinition(): list of additional redefinitions
     */
    fun parseRedefinitions(
        ctx: KerMLParser.RedefinitionsContext,
        featureId: String,
        parentQualifiedName: String
    ) {
        // Process the first redefinition from redefines()
        ctx.redefines()?.ownedRedefinition()?.let { redef ->
            createRedefinition(redef, featureId, parentQualifiedName)
        }

        // Process additional redefinitions from ownedRedefinition() list
        for (redef in ctx.ownedRedefinition()) {
            createRedefinition(redef, featureId, parentQualifiedName)
        }
    }

    /**
     * Create a Redefinition instance from an OwnedRedefinitionContext.
     */
    private fun createRedefinition(
        redef: KerMLParser.OwnedRedefinitionContext,
        featureId: String,
        parentQualifiedName: String
    ) {
        val (redefId, _) = engine.createInstance("Redefinition")
        // Feature -> ownedRedefinition (owningFeatureOwnedRedefinitionAssociation)
        engine.createLink("owningFeatureOwnedRedefinitionAssociation", featureId, redefId)

        // OwnedRedefinitionContext.generalType().qualifiedName()
        // Redefinition -> redefinedFeature (redefiningRedefinedFeatureAssociation)
        redef.generalType()?.qualifiedName()?.let { qn ->
            val targetName = extractQualifiedName(qn)
            recordUnresolvedReference(
                sourceInstanceId = redefId,
                associationName = "redefiningRedefinedFeatureAssociation",
                targetQualifiedName = targetName,
                currentNamespace = parentQualifiedName
            )
        }
    }

    /**
     * Parse multiplicity part.
     */
    fun parseMultiplicityPart(ctx: KerMLParser.MultiplicityPartContext, featureId: String) {
        ctx.ownedMultiplicity()?.let { mult ->
            mult.ownedMultiplicityRange()?.let { range ->
                // TODO: Parse multiplicity range and create Multiplicity instance
            }
        }
    }

    /**
     * Parse a Namespace element.
     */
    fun parseNamespace(
        ctx: KerMLParser.NamespaceContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        val (instanceId, _) = engine.createInstance("Namespace")

        // Parse namespaceDeclaration
        ctx.namespaceDeclaration()?.identification()?.let { id ->
            parseIdentification(id, instanceId)
        }

        // Get the declared name
        val declaredName = engine.getProperty(instanceId, "declaredName") as? String ?: ""
        val qualifiedName = computeQualifiedName(parentQualifiedName, declaredName)

        // Register the element
        registerParsedElement(qualifiedName, instanceId)

        // Push namespace for nested elements
        namespaceStack.addLast(declaredName)

        // Parse namespace body
        ctx.namespaceBody()?.namespaceBodyElement()?.forEach { bodyElement ->
            parseNamespaceBodyElement(bodyElement, instanceId, qualifiedName)
        }

        // Pop namespace
        namespaceStack.removeLast()

        return instanceId
    }

    /**
     * Parse a DataType element.
     */
    fun parseDatatype(
        ctx: KerMLParser.DatatypeContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        val (instanceId, _) = engine.createInstance("DataType")

        // Parse typePrefix
        ctx.typePrefix()?.isAbstract?.let {
            engine.setProperty(instanceId, "isAbstract", true)
        }

        // Parse classifierDeclaration
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, instanceId, parentId, parentQualifiedName)
        }

        // Get the declared name
        val declaredName = engine.getProperty(instanceId, "declaredName") as? String ?: ""
        val qualifiedName = computeQualifiedName(parentQualifiedName, declaredName)

        // Register the element
        registerParsedElement(qualifiedName, instanceId)

        // Parse type body
        namespaceStack.addLast(declaredName)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, instanceId, qualifiedName)
        }
        namespaceStack.removeLast()

        return instanceId
    }

    /**
     * Parse a Structure element.
     */
    fun parseStructure(
        ctx: KerMLParser.StructureContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        val (instanceId, _) = engine.createInstance("Structure")

        // Parse typePrefix
        ctx.typePrefix()?.isAbstract?.let {
            engine.setProperty(instanceId, "isAbstract", true)
        }

        // Parse classifierDeclaration
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, instanceId, parentId, parentQualifiedName)
        }

        // Get the declared name
        val declaredName = engine.getProperty(instanceId, "declaredName") as? String ?: ""
        val qualifiedName = computeQualifiedName(parentQualifiedName, declaredName)

        // Register the element
        registerParsedElement(qualifiedName, instanceId)

        // Parse type body
        namespaceStack.addLast(declaredName)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, instanceId, qualifiedName)
        }
        namespaceStack.removeLast()

        return instanceId
    }

    /**
     * Parse an Association element.
     */
    fun parseAssociation(
        ctx: KerMLParser.AssociationContext,
        parentId: String,
        parentQualifiedName: String
    ): String? {
        val (instanceId, _) = engine.createInstance("Association")

        // Parse typePrefix
        ctx.typePrefix()?.isAbstract?.let {
            engine.setProperty(instanceId, "isAbstract", true)
        }

        // Parse classifierDeclaration
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, instanceId, parentId, parentQualifiedName)
        }

        // Get the declared name
        val declaredName = engine.getProperty(instanceId, "declaredName") as? String ?: ""
        val qualifiedName = computeQualifiedName(parentQualifiedName, declaredName)

        // Register the element
        registerParsedElement(qualifiedName, instanceId)

        // Parse type body
        namespaceStack.addLast(declaredName)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, instanceId, qualifiedName)
        }
        namespaceStack.removeLast()

        return instanceId
    }

    /**
     * Parse identification (declaredName, declaredShortName).
     */
    fun parseIdentification(id: KerMLParser.IdentificationContext, instanceId: String) {
        id.declaredShortName?.let { shortName ->
            engine.setProperty(instanceId, "declaredShortName", shortName.text)
        }
        id.declaredName?.let { name ->
            engine.setProperty(instanceId, "declaredName", name.text)
        }
    }

    /**
     * Extract qualified name text from a QualifiedNameContext.
     */
    fun extractQualifiedName(ctx: KerMLParser.QualifiedNameContext): String {
        val names = ctx.NAME().map { it.text }
        return if (ctx.DOLLAR() != null) {
            "\$::" + names.joinToString("::")
        } else {
            names.joinToString("::")
        }
    }

    /**
     * Compute the qualified name for an element.
     */
    fun computeQualifiedName(parentQualifiedName: String, declaredName: String): String {
        return when {
            declaredName.isEmpty() -> parentQualifiedName
            parentQualifiedName.isEmpty() -> declaredName
            else -> "$parentQualifiedName::$declaredName"
        }
    }

    /**
     * Create an OwningMembership to link an element to its parent namespace.
     */
    fun createOwningMembership(parentId: String, ownedElementId: String) {
        val (membershipId, _) = engine.createInstance("OwningMembership")
        // Namespace -> ownedMembership (membershipOwningNamespaceOwnedMembershipAssociation)
        engine.createLink("membershipOwningNamespaceOwnedMembershipAssociation", parentId, membershipId)
        // OwningMembership -> ownedMemberElement (owningMembershipOwnedMemberElementAssociation)
        engine.createLink("owningMembershipOwnedMemberElementAssociation", membershipId, ownedElementId)
    }

    /**
     * Create a FeatureMembership to link a feature to its owning type.
     */
    fun createFeatureMembership(parentId: String, featureId: String) {
        val (membershipId, _) = engine.createInstance("FeatureMembership")
        // Type -> ownedFeatureMembership (owningTypeOwnedFeatureMembershipAssociation)
        engine.createLink("owningTypeOwnedFeatureMembershipAssociation", parentId, membershipId)
        // FeatureMembership -> ownedMemberFeature (owningFeatureMembershipOwnedMemberFeatureAssociation)
        engine.createLink("owningFeatureMembershipOwnedMemberFeatureAssociation", membershipId, featureId)
    }

    /**
     * Parse a root namespace (legacy method - kept for compatibility).
     *
     * @param rootContext The ANTLR root namespace context
     * @return The root namespace instance ID
     */
    @Suppress("UNCHECKED_CAST")
    fun parseRootNamespace(rootContext: Any): String? {
        return when (rootContext) {
            is KerMLParser.RootNamespaceContext -> parseRootNamespace(rootContext)
            else -> {
                val visitor = KerMLVisitorFactory.NonFeatureElements.namespace
                val instanceId = visitor.visit(rootContext, engine) as? String
                instanceId?.let { registerParsedElement("", it) }
                instanceId
            }
        }
    }

    /**
     * Register a parsed element for later reference resolution.
     */
    private fun registerParsedElement(qualifiedName: String, instanceId: String) {
        parsedElements[qualifiedName] = instanceId

        // Also index by simple name for fallback resolution
        val simpleName = qualifiedName.substringAfterLast("::")
        if (simpleName.isNotEmpty()) {
            simpleNameIndex.getOrPut(simpleName) { mutableListOf() }.add(instanceId)
        }
    }

    /**
     * Record an unresolved reference to be resolved later.
     *
     * @param sourceInstanceId The instance that has the reference
     * @param associationName The name of the association to create the link through
     * @param targetQualifiedName The qualified name of the target element
     * @param currentNamespace The current namespace for relative name resolution
     */
    fun recordUnresolvedReference(
        sourceInstanceId: String,
        associationName: String,
        targetQualifiedName: String,
        currentNamespace: String = ""
    ) {
        unresolvedReferences.add(
            UnresolvedReference(
                sourceInstanceId = sourceInstanceId,
                associationName = associationName,
                targetQualifiedName = targetQualifiedName,
                currentNamespace = currentNamespace
            )
        )
    }

    /**
     * Resolve all unresolved references after parsing is complete.
     * Uses engine.createLink() to establish association links.
     *
     * @return List of references that could not be resolved
     */
    fun resolveReferences(): List<UnresolvedReference> {
        val stillUnresolved = mutableListOf<UnresolvedReference>()

        for (ref in unresolvedReferences) {
            val targetInstanceId = resolveReference(ref)

            if (targetInstanceId != null) {
                // Create the link through the association
                try {
                    engine.createLink(ref.associationName, ref.sourceInstanceId, targetInstanceId)
                } catch (e: Exception) {
                    // If link creation fails, add to unresolved
                    stillUnresolved.add(ref)
                }
            } else {
                // Could not resolve - add to unresolved list
                stillUnresolved.add(ref)
            }
        }

        return stillUnresolved
    }

    /**
     * Resolve a single reference, trying different resolution strategies.
     */
    private fun resolveReference(ref: UnresolvedReference): String? {
        val targetName = ref.targetQualifiedName

        // Strategy 1: Try exact qualified name match
        parsedElements[targetName]?.let { return it }

        // Strategy 2: Try relative to current namespace
        if (ref.currentNamespace.isNotEmpty()) {
            val relativeQualified = "${ref.currentNamespace}::$targetName"
            parsedElements[relativeQualified]?.let { return it }
        }

        // Strategy 3: Try simple name lookup (if unambiguous)
        val simpleName = targetName.substringAfterLast("::")
        simpleNameIndex[simpleName]?.let { candidates ->
            if (candidates.size == 1) {
                return candidates.first()
            }
            // Multiple matches - try to find best match based on namespace context
            if (ref.currentNamespace.isNotEmpty() && candidates.size > 1) {
                // Prefer element in same or parent namespace
                for (candidate in candidates) {
                    val candidateQn = parsedElements.entries.find { it.value == candidate }?.key ?: continue
                    if (candidateQn.startsWith(ref.currentNamespace) ||
                        ref.currentNamespace.startsWith(candidateQn.substringBeforeLast("::"))) {
                        return candidate
                    }
                }
            }
        }

        return null
    }

    /**
     * Get statistics about the parsed model.
     */
    fun getParseStatistics(): ParseStatistics {
        return ParseStatistics(
            totalElements = parsedElements.size,
            unresolvedReferences = unresolvedReferences.size,
            elementsByType = engine.getStatistics().objects.typeDistribution
        )
    }

    /**
     * Clear all parsing state (useful for parsing a new file).
     */
    fun reset() {
        parsedElements.clear()
        simpleNameIndex.clear()
        unresolvedReferences.clear()
        namespaceStack.clear()
    }

    /**
     * Get the GearshiftEngine used by this coordinator.
     */
    fun getEngine(): GearshiftEngine = engine

    /**
     * Get all parsed elements by qualified name.
     */
    fun getParsedElements(): Map<String, String> = parsedElements.toMap()
}

/**
 * Result of a KerML parsing operation.
 */
data class KerMLParseResult(
    val success: Boolean,
    val rootElementId: String? = null,
    val errors: List<KerMLParseError> = emptyList(),
    val unresolvedReferences: List<UnresolvedReference> = emptyList()
)

/**
 * Represents an unresolved reference during parsing.
 */
data class UnresolvedReference(
    val sourceInstanceId: String,
    val associationName: String,
    val targetQualifiedName: String,
    val currentNamespace: String = ""
)

/**
 * Statistics about a parsing session.
 */
data class ParseStatistics(
    val totalElements: Int,
    val unresolvedReferences: Int,
    val elementsByType: Map<String, Int>
)
