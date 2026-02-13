// Generated from KerML.g4 by ANTLR 4.13.1
package org.openmbee.gearshift.kerml.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link KerMLParser}.
 */
public interface KerMLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typedByToken}.
	 * @param ctx the parse tree
	 */
	void enterTypedByToken(KerMLParser.TypedByTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typedByToken}.
	 * @param ctx the parse tree
	 */
	void exitTypedByToken(KerMLParser.TypedByTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#specializesToken}.
	 * @param ctx the parse tree
	 */
	void enterSpecializesToken(KerMLParser.SpecializesTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#specializesToken}.
	 * @param ctx the parse tree
	 */
	void exitSpecializesToken(KerMLParser.SpecializesTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#subsetsToken}.
	 * @param ctx the parse tree
	 */
	void enterSubsetsToken(KerMLParser.SubsetsTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#subsetsToken}.
	 * @param ctx the parse tree
	 */
	void exitSubsetsToken(KerMLParser.SubsetsTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#referencesToken}.
	 * @param ctx the parse tree
	 */
	void enterReferencesToken(KerMLParser.ReferencesTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#referencesToken}.
	 * @param ctx the parse tree
	 */
	void exitReferencesToken(KerMLParser.ReferencesTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#crossesToken}.
	 * @param ctx the parse tree
	 */
	void enterCrossesToken(KerMLParser.CrossesTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#crossesToken}.
	 * @param ctx the parse tree
	 */
	void exitCrossesToken(KerMLParser.CrossesTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#redefinesToken}.
	 * @param ctx the parse tree
	 */
	void enterRedefinesToken(KerMLParser.RedefinesTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#redefinesToken}.
	 * @param ctx the parse tree
	 */
	void exitRedefinesToken(KerMLParser.RedefinesTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#conjugatesToken}.
	 * @param ctx the parse tree
	 */
	void enterConjugatesToken(KerMLParser.ConjugatesTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#conjugatesToken}.
	 * @param ctx the parse tree
	 */
	void exitConjugatesToken(KerMLParser.ConjugatesTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#identification}.
	 * @param ctx the parse tree
	 */
	void enterIdentification(KerMLParser.IdentificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#identification}.
	 * @param ctx the parse tree
	 */
	void exitIdentification(KerMLParser.IdentificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#relationshipBody}.
	 * @param ctx the parse tree
	 */
	void enterRelationshipBody(KerMLParser.RelationshipBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#relationshipBody}.
	 * @param ctx the parse tree
	 */
	void exitRelationshipBody(KerMLParser.RelationshipBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#relationshipOwnedElement}.
	 * @param ctx the parse tree
	 */
	void enterRelationshipOwnedElement(KerMLParser.RelationshipOwnedElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#relationshipOwnedElement}.
	 * @param ctx the parse tree
	 */
	void exitRelationshipOwnedElement(KerMLParser.RelationshipOwnedElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedRelatedElement}.
	 * @param ctx the parse tree
	 */
	void enterOwnedRelatedElement(KerMLParser.OwnedRelatedElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedRelatedElement}.
	 * @param ctx the parse tree
	 */
	void exitOwnedRelatedElement(KerMLParser.OwnedRelatedElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#dependency}.
	 * @param ctx the parse tree
	 */
	void enterDependency(KerMLParser.DependencyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#dependency}.
	 * @param ctx the parse tree
	 */
	void exitDependency(KerMLParser.DependencyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(KerMLParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(KerMLParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterOwnedAnnotation(KerMLParser.OwnedAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitOwnedAnnotation(KerMLParser.OwnedAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#annotatingElement}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatingElement(KerMLParser.AnnotatingElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#annotatingElement}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatingElement(KerMLParser.AnnotatingElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(KerMLParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(KerMLParser.CommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#documentation}.
	 * @param ctx the parse tree
	 */
	void enterDocumentation(KerMLParser.DocumentationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#documentation}.
	 * @param ctx the parse tree
	 */
	void exitDocumentation(KerMLParser.DocumentationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#textualRepresentation}.
	 * @param ctx the parse tree
	 */
	void enterTextualRepresentation(KerMLParser.TextualRepresentationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#textualRepresentation}.
	 * @param ctx the parse tree
	 */
	void exitTextualRepresentation(KerMLParser.TextualRepresentationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#rootNamespace}.
	 * @param ctx the parse tree
	 */
	void enterRootNamespace(KerMLParser.RootNamespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#rootNamespace}.
	 * @param ctx the parse tree
	 */
	void exitRootNamespace(KerMLParser.RootNamespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#namespace}.
	 * @param ctx the parse tree
	 */
	void enterNamespace(KerMLParser.NamespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#namespace}.
	 * @param ctx the parse tree
	 */
	void exitNamespace(KerMLParser.NamespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#namespaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceDeclaration(KerMLParser.NamespaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#namespaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceDeclaration(KerMLParser.NamespaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#namespaceBody}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceBody(KerMLParser.NamespaceBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#namespaceBody}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceBody(KerMLParser.NamespaceBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#namespaceBodyElement}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceBodyElement(KerMLParser.NamespaceBodyElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#namespaceBodyElement}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceBodyElement(KerMLParser.NamespaceBodyElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#memberPrefix}.
	 * @param ctx the parse tree
	 */
	void enterMemberPrefix(KerMLParser.MemberPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#memberPrefix}.
	 * @param ctx the parse tree
	 */
	void exitMemberPrefix(KerMLParser.MemberPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#visibilityIndicator}.
	 * @param ctx the parse tree
	 */
	void enterVisibilityIndicator(KerMLParser.VisibilityIndicatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#visibilityIndicator}.
	 * @param ctx the parse tree
	 */
	void exitVisibilityIndicator(KerMLParser.VisibilityIndicatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#namespaceMember}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceMember(KerMLParser.NamespaceMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#namespaceMember}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceMember(KerMLParser.NamespaceMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#nonFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterNonFeatureMember(KerMLParser.NonFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#nonFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitNonFeatureMember(KerMLParser.NonFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#namespaceFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceFeatureMember(KerMLParser.NamespaceFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#namespaceFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceFeatureMember(KerMLParser.NamespaceFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#aliasMember}.
	 * @param ctx the parse tree
	 */
	void enterAliasMember(KerMLParser.AliasMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#aliasMember}.
	 * @param ctx the parse tree
	 */
	void exitAliasMember(KerMLParser.AliasMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(KerMLParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(KerMLParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#import_}.
	 * @param ctx the parse tree
	 */
	void enterImport_(KerMLParser.Import_Context ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#import_}.
	 * @param ctx the parse tree
	 */
	void exitImport_(KerMLParser.Import_Context ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImportDeclaration(KerMLParser.ImportDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImportDeclaration(KerMLParser.ImportDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#membershipImport}.
	 * @param ctx the parse tree
	 */
	void enterMembershipImport(KerMLParser.MembershipImportContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#membershipImport}.
	 * @param ctx the parse tree
	 */
	void exitMembershipImport(KerMLParser.MembershipImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#namespaceImport}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceImport(KerMLParser.NamespaceImportContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#namespaceImport}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceImport(KerMLParser.NamespaceImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#filterPackageImport}.
	 * @param ctx the parse tree
	 */
	void enterFilterPackageImport(KerMLParser.FilterPackageImportContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#filterPackageImport}.
	 * @param ctx the parse tree
	 */
	void exitFilterPackageImport(KerMLParser.FilterPackageImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#filterPackageMember}.
	 * @param ctx the parse tree
	 */
	void enterFilterPackageMember(KerMLParser.FilterPackageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#filterPackageMember}.
	 * @param ctx the parse tree
	 */
	void exitFilterPackageMember(KerMLParser.FilterPackageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#memberElement}.
	 * @param ctx the parse tree
	 */
	void enterMemberElement(KerMLParser.MemberElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#memberElement}.
	 * @param ctx the parse tree
	 */
	void exitMemberElement(KerMLParser.MemberElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#nonFeatureElement}.
	 * @param ctx the parse tree
	 */
	void enterNonFeatureElement(KerMLParser.NonFeatureElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#nonFeatureElement}.
	 * @param ctx the parse tree
	 */
	void exitNonFeatureElement(KerMLParser.NonFeatureElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureElement}.
	 * @param ctx the parse tree
	 */
	void enterFeatureElement(KerMLParser.FeatureElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureElement}.
	 * @param ctx the parse tree
	 */
	void exitFeatureElement(KerMLParser.FeatureElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(KerMLParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(KerMLParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typePrefix}.
	 * @param ctx the parse tree
	 */
	void enterTypePrefix(KerMLParser.TypePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typePrefix}.
	 * @param ctx the parse tree
	 */
	void exitTypePrefix(KerMLParser.TypePrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeDeclaration(KerMLParser.TypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeDeclaration(KerMLParser.TypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#specializationPart}.
	 * @param ctx the parse tree
	 */
	void enterSpecializationPart(KerMLParser.SpecializationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#specializationPart}.
	 * @param ctx the parse tree
	 */
	void exitSpecializationPart(KerMLParser.SpecializationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#conjugationPart}.
	 * @param ctx the parse tree
	 */
	void enterConjugationPart(KerMLParser.ConjugationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#conjugationPart}.
	 * @param ctx the parse tree
	 */
	void exitConjugationPart(KerMLParser.ConjugationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typeRelationshipPart}.
	 * @param ctx the parse tree
	 */
	void enterTypeRelationshipPart(KerMLParser.TypeRelationshipPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typeRelationshipPart}.
	 * @param ctx the parse tree
	 */
	void exitTypeRelationshipPart(KerMLParser.TypeRelationshipPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#disjoiningPart}.
	 * @param ctx the parse tree
	 */
	void enterDisjoiningPart(KerMLParser.DisjoiningPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#disjoiningPart}.
	 * @param ctx the parse tree
	 */
	void exitDisjoiningPart(KerMLParser.DisjoiningPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#unioningPart}.
	 * @param ctx the parse tree
	 */
	void enterUnioningPart(KerMLParser.UnioningPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#unioningPart}.
	 * @param ctx the parse tree
	 */
	void exitUnioningPart(KerMLParser.UnioningPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#intersectingPart}.
	 * @param ctx the parse tree
	 */
	void enterIntersectingPart(KerMLParser.IntersectingPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#intersectingPart}.
	 * @param ctx the parse tree
	 */
	void exitIntersectingPart(KerMLParser.IntersectingPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#differencingPart}.
	 * @param ctx the parse tree
	 */
	void enterDifferencingPart(KerMLParser.DifferencingPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#differencingPart}.
	 * @param ctx the parse tree
	 */
	void exitDifferencingPart(KerMLParser.DifferencingPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typeBody}.
	 * @param ctx the parse tree
	 */
	void enterTypeBody(KerMLParser.TypeBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typeBody}.
	 * @param ctx the parse tree
	 */
	void exitTypeBody(KerMLParser.TypeBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typeBodyElement}.
	 * @param ctx the parse tree
	 */
	void enterTypeBodyElement(KerMLParser.TypeBodyElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typeBodyElement}.
	 * @param ctx the parse tree
	 */
	void exitTypeBodyElement(KerMLParser.TypeBodyElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#specialization}.
	 * @param ctx the parse tree
	 */
	void enterSpecialization(KerMLParser.SpecializationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#specialization}.
	 * @param ctx the parse tree
	 */
	void exitSpecialization(KerMLParser.SpecializationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedSpecialization}.
	 * @param ctx the parse tree
	 */
	void enterOwnedSpecialization(KerMLParser.OwnedSpecializationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedSpecialization}.
	 * @param ctx the parse tree
	 */
	void exitOwnedSpecialization(KerMLParser.OwnedSpecializationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#specificType}.
	 * @param ctx the parse tree
	 */
	void enterSpecificType(KerMLParser.SpecificTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#specificType}.
	 * @param ctx the parse tree
	 */
	void exitSpecificType(KerMLParser.SpecificTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#generalType}.
	 * @param ctx the parse tree
	 */
	void enterGeneralType(KerMLParser.GeneralTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#generalType}.
	 * @param ctx the parse tree
	 */
	void exitGeneralType(KerMLParser.GeneralTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#conjugation}.
	 * @param ctx the parse tree
	 */
	void enterConjugation(KerMLParser.ConjugationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#conjugation}.
	 * @param ctx the parse tree
	 */
	void exitConjugation(KerMLParser.ConjugationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedConjugation}.
	 * @param ctx the parse tree
	 */
	void enterOwnedConjugation(KerMLParser.OwnedConjugationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedConjugation}.
	 * @param ctx the parse tree
	 */
	void exitOwnedConjugation(KerMLParser.OwnedConjugationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#disjoining}.
	 * @param ctx the parse tree
	 */
	void enterDisjoining(KerMLParser.DisjoiningContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#disjoining}.
	 * @param ctx the parse tree
	 */
	void exitDisjoining(KerMLParser.DisjoiningContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedDisjoining}.
	 * @param ctx the parse tree
	 */
	void enterOwnedDisjoining(KerMLParser.OwnedDisjoiningContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedDisjoining}.
	 * @param ctx the parse tree
	 */
	void exitOwnedDisjoining(KerMLParser.OwnedDisjoiningContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#unioning}.
	 * @param ctx the parse tree
	 */
	void enterUnioning(KerMLParser.UnioningContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#unioning}.
	 * @param ctx the parse tree
	 */
	void exitUnioning(KerMLParser.UnioningContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#intersecting}.
	 * @param ctx the parse tree
	 */
	void enterIntersecting(KerMLParser.IntersectingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#intersecting}.
	 * @param ctx the parse tree
	 */
	void exitIntersecting(KerMLParser.IntersectingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#differencing}.
	 * @param ctx the parse tree
	 */
	void enterDifferencing(KerMLParser.DifferencingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#differencing}.
	 * @param ctx the parse tree
	 */
	void exitDifferencing(KerMLParser.DifferencingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureMember}.
	 * @param ctx the parse tree
	 */
	void enterFeatureMember(KerMLParser.FeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureMember}.
	 * @param ctx the parse tree
	 */
	void exitFeatureMember(KerMLParser.FeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typeFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterTypeFeatureMember(KerMLParser.TypeFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typeFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitTypeFeatureMember(KerMLParser.TypeFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterOwnedFeatureMember(KerMLParser.OwnedFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitOwnedFeatureMember(KerMLParser.OwnedFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#classifier}.
	 * @param ctx the parse tree
	 */
	void enterClassifier(KerMLParser.ClassifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#classifier}.
	 * @param ctx the parse tree
	 */
	void exitClassifier(KerMLParser.ClassifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#classifierDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassifierDeclaration(KerMLParser.ClassifierDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#classifierDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassifierDeclaration(KerMLParser.ClassifierDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#superclassingPart}.
	 * @param ctx the parse tree
	 */
	void enterSuperclassingPart(KerMLParser.SuperclassingPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#superclassingPart}.
	 * @param ctx the parse tree
	 */
	void exitSuperclassingPart(KerMLParser.SuperclassingPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#subclassification}.
	 * @param ctx the parse tree
	 */
	void enterSubclassification(KerMLParser.SubclassificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#subclassification}.
	 * @param ctx the parse tree
	 */
	void exitSubclassification(KerMLParser.SubclassificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedSubclassification}.
	 * @param ctx the parse tree
	 */
	void enterOwnedSubclassification(KerMLParser.OwnedSubclassificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedSubclassification}.
	 * @param ctx the parse tree
	 */
	void exitOwnedSubclassification(KerMLParser.OwnedSubclassificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#feature}.
	 * @param ctx the parse tree
	 */
	void enterFeature(KerMLParser.FeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#feature}.
	 * @param ctx the parse tree
	 */
	void exitFeature(KerMLParser.FeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#endFeaturePrefix}.
	 * @param ctx the parse tree
	 */
	void enterEndFeaturePrefix(KerMLParser.EndFeaturePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#endFeaturePrefix}.
	 * @param ctx the parse tree
	 */
	void exitEndFeaturePrefix(KerMLParser.EndFeaturePrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#basicFeaturePrefix}.
	 * @param ctx the parse tree
	 */
	void enterBasicFeaturePrefix(KerMLParser.BasicFeaturePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#basicFeaturePrefix}.
	 * @param ctx the parse tree
	 */
	void exitBasicFeaturePrefix(KerMLParser.BasicFeaturePrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featurePrefix}.
	 * @param ctx the parse tree
	 */
	void enterFeaturePrefix(KerMLParser.FeaturePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featurePrefix}.
	 * @param ctx the parse tree
	 */
	void exitFeaturePrefix(KerMLParser.FeaturePrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedCrossFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterOwnedCrossFeatureMember(KerMLParser.OwnedCrossFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedCrossFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitOwnedCrossFeatureMember(KerMLParser.OwnedCrossFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedCrossFeature}.
	 * @param ctx the parse tree
	 */
	void enterOwnedCrossFeature(KerMLParser.OwnedCrossFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedCrossFeature}.
	 * @param ctx the parse tree
	 */
	void exitOwnedCrossFeature(KerMLParser.OwnedCrossFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureDirection}.
	 * @param ctx the parse tree
	 */
	void enterFeatureDirection(KerMLParser.FeatureDirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureDirection}.
	 * @param ctx the parse tree
	 */
	void exitFeatureDirection(KerMLParser.FeatureDirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFeatureDeclaration(KerMLParser.FeatureDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFeatureDeclaration(KerMLParser.FeatureDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureIdentification}.
	 * @param ctx the parse tree
	 */
	void enterFeatureIdentification(KerMLParser.FeatureIdentificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureIdentification}.
	 * @param ctx the parse tree
	 */
	void exitFeatureIdentification(KerMLParser.FeatureIdentificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureRelationshipPart}.
	 * @param ctx the parse tree
	 */
	void enterFeatureRelationshipPart(KerMLParser.FeatureRelationshipPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureRelationshipPart}.
	 * @param ctx the parse tree
	 */
	void exitFeatureRelationshipPart(KerMLParser.FeatureRelationshipPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#chainingPart}.
	 * @param ctx the parse tree
	 */
	void enterChainingPart(KerMLParser.ChainingPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#chainingPart}.
	 * @param ctx the parse tree
	 */
	void exitChainingPart(KerMLParser.ChainingPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#invertingPart}.
	 * @param ctx the parse tree
	 */
	void enterInvertingPart(KerMLParser.InvertingPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#invertingPart}.
	 * @param ctx the parse tree
	 */
	void exitInvertingPart(KerMLParser.InvertingPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typeFeaturingPart}.
	 * @param ctx the parse tree
	 */
	void enterTypeFeaturingPart(KerMLParser.TypeFeaturingPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typeFeaturingPart}.
	 * @param ctx the parse tree
	 */
	void exitTypeFeaturingPart(KerMLParser.TypeFeaturingPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureSpecializationPart}.
	 * @param ctx the parse tree
	 */
	void enterFeatureSpecializationPart(KerMLParser.FeatureSpecializationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureSpecializationPart}.
	 * @param ctx the parse tree
	 */
	void exitFeatureSpecializationPart(KerMLParser.FeatureSpecializationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#multiplicityPart}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicityPart(KerMLParser.MultiplicityPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#multiplicityPart}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicityPart(KerMLParser.MultiplicityPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureSpecialization}.
	 * @param ctx the parse tree
	 */
	void enterFeatureSpecialization(KerMLParser.FeatureSpecializationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureSpecialization}.
	 * @param ctx the parse tree
	 */
	void exitFeatureSpecialization(KerMLParser.FeatureSpecializationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typings}.
	 * @param ctx the parse tree
	 */
	void enterTypings(KerMLParser.TypingsContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typings}.
	 * @param ctx the parse tree
	 */
	void exitTypings(KerMLParser.TypingsContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typedBy}.
	 * @param ctx the parse tree
	 */
	void enterTypedBy(KerMLParser.TypedByContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typedBy}.
	 * @param ctx the parse tree
	 */
	void exitTypedBy(KerMLParser.TypedByContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#subsettings}.
	 * @param ctx the parse tree
	 */
	void enterSubsettings(KerMLParser.SubsettingsContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#subsettings}.
	 * @param ctx the parse tree
	 */
	void exitSubsettings(KerMLParser.SubsettingsContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#subsets}.
	 * @param ctx the parse tree
	 */
	void enterSubsets(KerMLParser.SubsetsContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#subsets}.
	 * @param ctx the parse tree
	 */
	void exitSubsets(KerMLParser.SubsetsContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#references}.
	 * @param ctx the parse tree
	 */
	void enterReferences(KerMLParser.ReferencesContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#references}.
	 * @param ctx the parse tree
	 */
	void exitReferences(KerMLParser.ReferencesContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#crosses}.
	 * @param ctx the parse tree
	 */
	void enterCrosses(KerMLParser.CrossesContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#crosses}.
	 * @param ctx the parse tree
	 */
	void exitCrosses(KerMLParser.CrossesContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#redefinitions}.
	 * @param ctx the parse tree
	 */
	void enterRedefinitions(KerMLParser.RedefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#redefinitions}.
	 * @param ctx the parse tree
	 */
	void exitRedefinitions(KerMLParser.RedefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#redefines}.
	 * @param ctx the parse tree
	 */
	void enterRedefines(KerMLParser.RedefinesContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#redefines}.
	 * @param ctx the parse tree
	 */
	void exitRedefines(KerMLParser.RedefinesContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureTyping}.
	 * @param ctx the parse tree
	 */
	void enterFeatureTyping(KerMLParser.FeatureTypingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureTyping}.
	 * @param ctx the parse tree
	 */
	void exitFeatureTyping(KerMLParser.FeatureTypingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedFeatureTyping}.
	 * @param ctx the parse tree
	 */
	void enterOwnedFeatureTyping(KerMLParser.OwnedFeatureTypingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedFeatureTyping}.
	 * @param ctx the parse tree
	 */
	void exitOwnedFeatureTyping(KerMLParser.OwnedFeatureTypingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#subsetting}.
	 * @param ctx the parse tree
	 */
	void enterSubsetting(KerMLParser.SubsettingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#subsetting}.
	 * @param ctx the parse tree
	 */
	void exitSubsetting(KerMLParser.SubsettingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedSubsetting}.
	 * @param ctx the parse tree
	 */
	void enterOwnedSubsetting(KerMLParser.OwnedSubsettingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedSubsetting}.
	 * @param ctx the parse tree
	 */
	void exitOwnedSubsetting(KerMLParser.OwnedSubsettingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedReferenceSubsetting}.
	 * @param ctx the parse tree
	 */
	void enterOwnedReferenceSubsetting(KerMLParser.OwnedReferenceSubsettingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedReferenceSubsetting}.
	 * @param ctx the parse tree
	 */
	void exitOwnedReferenceSubsetting(KerMLParser.OwnedReferenceSubsettingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedCrossSubsetting}.
	 * @param ctx the parse tree
	 */
	void enterOwnedCrossSubsetting(KerMLParser.OwnedCrossSubsettingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedCrossSubsetting}.
	 * @param ctx the parse tree
	 */
	void exitOwnedCrossSubsetting(KerMLParser.OwnedCrossSubsettingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#redefinition}.
	 * @param ctx the parse tree
	 */
	void enterRedefinition(KerMLParser.RedefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#redefinition}.
	 * @param ctx the parse tree
	 */
	void exitRedefinition(KerMLParser.RedefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedRedefinition}.
	 * @param ctx the parse tree
	 */
	void enterOwnedRedefinition(KerMLParser.OwnedRedefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedRedefinition}.
	 * @param ctx the parse tree
	 */
	void exitOwnedRedefinition(KerMLParser.OwnedRedefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedFeatureChain}.
	 * @param ctx the parse tree
	 */
	void enterOwnedFeatureChain(KerMLParser.OwnedFeatureChainContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedFeatureChain}.
	 * @param ctx the parse tree
	 */
	void exitOwnedFeatureChain(KerMLParser.OwnedFeatureChainContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureChain}.
	 * @param ctx the parse tree
	 */
	void enterFeatureChain(KerMLParser.FeatureChainContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureChain}.
	 * @param ctx the parse tree
	 */
	void exitFeatureChain(KerMLParser.FeatureChainContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedFeatureChaining}.
	 * @param ctx the parse tree
	 */
	void enterOwnedFeatureChaining(KerMLParser.OwnedFeatureChainingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedFeatureChaining}.
	 * @param ctx the parse tree
	 */
	void exitOwnedFeatureChaining(KerMLParser.OwnedFeatureChainingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureInverting}.
	 * @param ctx the parse tree
	 */
	void enterFeatureInverting(KerMLParser.FeatureInvertingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureInverting}.
	 * @param ctx the parse tree
	 */
	void exitFeatureInverting(KerMLParser.FeatureInvertingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedFeatureInverting}.
	 * @param ctx the parse tree
	 */
	void enterOwnedFeatureInverting(KerMLParser.OwnedFeatureInvertingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedFeatureInverting}.
	 * @param ctx the parse tree
	 */
	void exitOwnedFeatureInverting(KerMLParser.OwnedFeatureInvertingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typeFeaturing}.
	 * @param ctx the parse tree
	 */
	void enterTypeFeaturing(KerMLParser.TypeFeaturingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typeFeaturing}.
	 * @param ctx the parse tree
	 */
	void exitTypeFeaturing(KerMLParser.TypeFeaturingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedTypeFeaturing}.
	 * @param ctx the parse tree
	 */
	void enterOwnedTypeFeaturing(KerMLParser.OwnedTypeFeaturingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedTypeFeaturing}.
	 * @param ctx the parse tree
	 */
	void exitOwnedTypeFeaturing(KerMLParser.OwnedTypeFeaturingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#datatype}.
	 * @param ctx the parse tree
	 */
	void enterDatatype(KerMLParser.DatatypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#datatype}.
	 * @param ctx the parse tree
	 */
	void exitDatatype(KerMLParser.DatatypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#class}.
	 * @param ctx the parse tree
	 */
	void enterClass(KerMLParser.ClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#class}.
	 * @param ctx the parse tree
	 */
	void exitClass(KerMLParser.ClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#structure}.
	 * @param ctx the parse tree
	 */
	void enterStructure(KerMLParser.StructureContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#structure}.
	 * @param ctx the parse tree
	 */
	void exitStructure(KerMLParser.StructureContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#association}.
	 * @param ctx the parse tree
	 */
	void enterAssociation(KerMLParser.AssociationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#association}.
	 * @param ctx the parse tree
	 */
	void exitAssociation(KerMLParser.AssociationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#associationStructure}.
	 * @param ctx the parse tree
	 */
	void enterAssociationStructure(KerMLParser.AssociationStructureContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#associationStructure}.
	 * @param ctx the parse tree
	 */
	void exitAssociationStructure(KerMLParser.AssociationStructureContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#connector}.
	 * @param ctx the parse tree
	 */
	void enterConnector(KerMLParser.ConnectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#connector}.
	 * @param ctx the parse tree
	 */
	void exitConnector(KerMLParser.ConnectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#connectorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConnectorDeclaration(KerMLParser.ConnectorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#connectorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConnectorDeclaration(KerMLParser.ConnectorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#binaryConnectorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterBinaryConnectorDeclaration(KerMLParser.BinaryConnectorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#binaryConnectorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitBinaryConnectorDeclaration(KerMLParser.BinaryConnectorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#naryConnectorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterNaryConnectorDeclaration(KerMLParser.NaryConnectorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#naryConnectorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitNaryConnectorDeclaration(KerMLParser.NaryConnectorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#connectorEndMember}.
	 * @param ctx the parse tree
	 */
	void enterConnectorEndMember(KerMLParser.ConnectorEndMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#connectorEndMember}.
	 * @param ctx the parse tree
	 */
	void exitConnectorEndMember(KerMLParser.ConnectorEndMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#connectorEnd}.
	 * @param ctx the parse tree
	 */
	void enterConnectorEnd(KerMLParser.ConnectorEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#connectorEnd}.
	 * @param ctx the parse tree
	 */
	void exitConnectorEnd(KerMLParser.ConnectorEndContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedCrossMultiplicityMember}.
	 * @param ctx the parse tree
	 */
	void enterOwnedCrossMultiplicityMember(KerMLParser.OwnedCrossMultiplicityMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedCrossMultiplicityMember}.
	 * @param ctx the parse tree
	 */
	void exitOwnedCrossMultiplicityMember(KerMLParser.OwnedCrossMultiplicityMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedCrossMultiplicity}.
	 * @param ctx the parse tree
	 */
	void enterOwnedCrossMultiplicity(KerMLParser.OwnedCrossMultiplicityContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedCrossMultiplicity}.
	 * @param ctx the parse tree
	 */
	void exitOwnedCrossMultiplicity(KerMLParser.OwnedCrossMultiplicityContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#bindingConnector}.
	 * @param ctx the parse tree
	 */
	void enterBindingConnector(KerMLParser.BindingConnectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#bindingConnector}.
	 * @param ctx the parse tree
	 */
	void exitBindingConnector(KerMLParser.BindingConnectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#bindingConnectorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterBindingConnectorDeclaration(KerMLParser.BindingConnectorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#bindingConnectorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitBindingConnectorDeclaration(KerMLParser.BindingConnectorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#succession}.
	 * @param ctx the parse tree
	 */
	void enterSuccession(KerMLParser.SuccessionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#succession}.
	 * @param ctx the parse tree
	 */
	void exitSuccession(KerMLParser.SuccessionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#successionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterSuccessionDeclaration(KerMLParser.SuccessionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#successionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitSuccessionDeclaration(KerMLParser.SuccessionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#behavior}.
	 * @param ctx the parse tree
	 */
	void enterBehavior(KerMLParser.BehaviorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#behavior}.
	 * @param ctx the parse tree
	 */
	void exitBehavior(KerMLParser.BehaviorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#step}.
	 * @param ctx the parse tree
	 */
	void enterStep(KerMLParser.StepContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#step}.
	 * @param ctx the parse tree
	 */
	void exitStep(KerMLParser.StepContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(KerMLParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(KerMLParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBody(KerMLParser.FunctionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBody(KerMLParser.FunctionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#functionBodyPart}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBodyPart(KerMLParser.FunctionBodyPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#functionBodyPart}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBodyPart(KerMLParser.FunctionBodyPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#returnFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterReturnFeatureMember(KerMLParser.ReturnFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#returnFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitReturnFeatureMember(KerMLParser.ReturnFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#resultExpressionMember}.
	 * @param ctx the parse tree
	 */
	void enterResultExpressionMember(KerMLParser.ResultExpressionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#resultExpressionMember}.
	 * @param ctx the parse tree
	 */
	void exitResultExpressionMember(KerMLParser.ResultExpressionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(KerMLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(KerMLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(KerMLParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(KerMLParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanExpression(KerMLParser.BooleanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanExpression(KerMLParser.BooleanExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#invariant}.
	 * @param ctx the parse tree
	 */
	void enterInvariant(KerMLParser.InvariantContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#invariant}.
	 * @param ctx the parse tree
	 */
	void exitInvariant(KerMLParser.InvariantContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedExpressionReferenceMember}.
	 * @param ctx the parse tree
	 */
	void enterOwnedExpressionReferenceMember(KerMLParser.OwnedExpressionReferenceMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedExpressionReferenceMember}.
	 * @param ctx the parse tree
	 */
	void exitOwnedExpressionReferenceMember(KerMLParser.OwnedExpressionReferenceMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedExpressionReference}.
	 * @param ctx the parse tree
	 */
	void enterOwnedExpressionReference(KerMLParser.OwnedExpressionReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedExpressionReference}.
	 * @param ctx the parse tree
	 */
	void exitOwnedExpressionReference(KerMLParser.OwnedExpressionReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedExpressionMember}.
	 * @param ctx the parse tree
	 */
	void enterOwnedExpressionMember(KerMLParser.OwnedExpressionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedExpressionMember}.
	 * @param ctx the parse tree
	 */
	void exitOwnedExpressionMember(KerMLParser.OwnedExpressionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedExpression}.
	 * @param ctx the parse tree
	 */
	void enterOwnedExpression(KerMLParser.OwnedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedExpression}.
	 * @param ctx the parse tree
	 */
	void exitOwnedExpression(KerMLParser.OwnedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#conditionalBinaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterConditionalBinaryOperator(KerMLParser.ConditionalBinaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#conditionalBinaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitConditionalBinaryOperator(KerMLParser.ConditionalBinaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#binaryOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOperatorExpression(KerMLParser.BinaryOperatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#binaryOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOperatorExpression(KerMLParser.BinaryOperatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#binaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOperator(KerMLParser.BinaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#binaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOperator(KerMLParser.BinaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#equalityOperator}.
	 * @param ctx the parse tree
	 */
	void enterEqualityOperator(KerMLParser.EqualityOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#equalityOperator}.
	 * @param ctx the parse tree
	 */
	void exitEqualityOperator(KerMLParser.EqualityOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#relationalOperator}.
	 * @param ctx the parse tree
	 */
	void enterRelationalOperator(KerMLParser.RelationalOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#relationalOperator}.
	 * @param ctx the parse tree
	 */
	void exitRelationalOperator(KerMLParser.RelationalOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#additiveOperator}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveOperator(KerMLParser.AdditiveOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#additiveOperator}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveOperator(KerMLParser.AdditiveOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#multiplicativeOperator}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeOperator(KerMLParser.MultiplicativeOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#multiplicativeOperator}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeOperator(KerMLParser.MultiplicativeOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#exponentialOperator}.
	 * @param ctx the parse tree
	 */
	void enterExponentialOperator(KerMLParser.ExponentialOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#exponentialOperator}.
	 * @param ctx the parse tree
	 */
	void exitExponentialOperator(KerMLParser.ExponentialOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#bitwiseOperator}.
	 * @param ctx the parse tree
	 */
	void enterBitwiseOperator(KerMLParser.BitwiseOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#bitwiseOperator}.
	 * @param ctx the parse tree
	 */
	void exitBitwiseOperator(KerMLParser.BitwiseOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#rangeOperator}.
	 * @param ctx the parse tree
	 */
	void enterRangeOperator(KerMLParser.RangeOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#rangeOperator}.
	 * @param ctx the parse tree
	 */
	void exitRangeOperator(KerMLParser.RangeOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#unaryOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperatorExpression(KerMLParser.UnaryOperatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#unaryOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperatorExpression(KerMLParser.UnaryOperatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(KerMLParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(KerMLParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#classificationExpression}.
	 * @param ctx the parse tree
	 */
	void enterClassificationExpression(KerMLParser.ClassificationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#classificationExpression}.
	 * @param ctx the parse tree
	 */
	void exitClassificationExpression(KerMLParser.ClassificationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#classificationTestOperator}.
	 * @param ctx the parse tree
	 */
	void enterClassificationTestOperator(KerMLParser.ClassificationTestOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#classificationTestOperator}.
	 * @param ctx the parse tree
	 */
	void exitClassificationTestOperator(KerMLParser.ClassificationTestOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#castOperator}.
	 * @param ctx the parse tree
	 */
	void enterCastOperator(KerMLParser.CastOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#castOperator}.
	 * @param ctx the parse tree
	 */
	void exitCastOperator(KerMLParser.CastOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metaclassificationExpression}.
	 * @param ctx the parse tree
	 */
	void enterMetaclassificationExpression(KerMLParser.MetaclassificationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metaclassificationExpression}.
	 * @param ctx the parse tree
	 */
	void exitMetaclassificationExpression(KerMLParser.MetaclassificationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#argumentMember}.
	 * @param ctx the parse tree
	 */
	void enterArgumentMember(KerMLParser.ArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#argumentMember}.
	 * @param ctx the parse tree
	 */
	void exitArgumentMember(KerMLParser.ArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(KerMLParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(KerMLParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#argumentValue}.
	 * @param ctx the parse tree
	 */
	void enterArgumentValue(KerMLParser.ArgumentValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#argumentValue}.
	 * @param ctx the parse tree
	 */
	void exitArgumentValue(KerMLParser.ArgumentValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#argumentExpressionMember}.
	 * @param ctx the parse tree
	 */
	void enterArgumentExpressionMember(KerMLParser.ArgumentExpressionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#argumentExpressionMember}.
	 * @param ctx the parse tree
	 */
	void exitArgumentExpressionMember(KerMLParser.ArgumentExpressionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#argumentExpression}.
	 * @param ctx the parse tree
	 */
	void enterArgumentExpression(KerMLParser.ArgumentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#argumentExpression}.
	 * @param ctx the parse tree
	 */
	void exitArgumentExpression(KerMLParser.ArgumentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#argumentExpressionValue}.
	 * @param ctx the parse tree
	 */
	void enterArgumentExpressionValue(KerMLParser.ArgumentExpressionValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#argumentExpressionValue}.
	 * @param ctx the parse tree
	 */
	void exitArgumentExpressionValue(KerMLParser.ArgumentExpressionValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metadataArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterMetadataArgumentMember(KerMLParser.MetadataArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metadataArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitMetadataArgumentMember(KerMLParser.MetadataArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metadataArgument}.
	 * @param ctx the parse tree
	 */
	void enterMetadataArgument(KerMLParser.MetadataArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metadataArgument}.
	 * @param ctx the parse tree
	 */
	void exitMetadataArgument(KerMLParser.MetadataArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metadataValue}.
	 * @param ctx the parse tree
	 */
	void enterMetadataValue(KerMLParser.MetadataValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metadataValue}.
	 * @param ctx the parse tree
	 */
	void exitMetadataValue(KerMLParser.MetadataValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metadataReference}.
	 * @param ctx the parse tree
	 */
	void enterMetadataReference(KerMLParser.MetadataReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metadataReference}.
	 * @param ctx the parse tree
	 */
	void exitMetadataReference(KerMLParser.MetadataReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metaclassificationTestOperator}.
	 * @param ctx the parse tree
	 */
	void enterMetaclassificationTestOperator(KerMLParser.MetaclassificationTestOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metaclassificationTestOperator}.
	 * @param ctx the parse tree
	 */
	void exitMetaclassificationTestOperator(KerMLParser.MetaclassificationTestOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metacastOperator}.
	 * @param ctx the parse tree
	 */
	void enterMetacastOperator(KerMLParser.MetacastOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metacastOperator}.
	 * @param ctx the parse tree
	 */
	void exitMetacastOperator(KerMLParser.MetacastOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#extentExpression}.
	 * @param ctx the parse tree
	 */
	void enterExtentExpression(KerMLParser.ExtentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#extentExpression}.
	 * @param ctx the parse tree
	 */
	void exitExtentExpression(KerMLParser.ExtentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typeReferenceMember}.
	 * @param ctx the parse tree
	 */
	void enterTypeReferenceMember(KerMLParser.TypeReferenceMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typeReferenceMember}.
	 * @param ctx the parse tree
	 */
	void exitTypeReferenceMember(KerMLParser.TypeReferenceMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typeResultMember}.
	 * @param ctx the parse tree
	 */
	void enterTypeResultMember(KerMLParser.TypeResultMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typeResultMember}.
	 * @param ctx the parse tree
	 */
	void exitTypeResultMember(KerMLParser.TypeResultMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#typeReference}.
	 * @param ctx the parse tree
	 */
	void enterTypeReference(KerMLParser.TypeReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#typeReference}.
	 * @param ctx the parse tree
	 */
	void exitTypeReference(KerMLParser.TypeReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#referenceTyping}.
	 * @param ctx the parse tree
	 */
	void enterReferenceTyping(KerMLParser.ReferenceTypingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#referenceTyping}.
	 * @param ctx the parse tree
	 */
	void exitReferenceTyping(KerMLParser.ReferenceTypingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#emptyResultMember}.
	 * @param ctx the parse tree
	 */
	void enterEmptyResultMember(KerMLParser.EmptyResultMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#emptyResultMember}.
	 * @param ctx the parse tree
	 */
	void exitEmptyResultMember(KerMLParser.EmptyResultMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#emptyFeature}.
	 * @param ctx the parse tree
	 */
	void enterEmptyFeature(KerMLParser.EmptyFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#emptyFeature}.
	 * @param ctx the parse tree
	 */
	void exitEmptyFeature(KerMLParser.EmptyFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(KerMLParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(KerMLParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#primaryArgumentValue}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryArgumentValue(KerMLParser.PrimaryArgumentValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#primaryArgumentValue}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryArgumentValue(KerMLParser.PrimaryArgumentValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#primaryArgument}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryArgument(KerMLParser.PrimaryArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#primaryArgument}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryArgument(KerMLParser.PrimaryArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#primaryArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryArgumentMember(KerMLParser.PrimaryArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#primaryArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryArgumentMember(KerMLParser.PrimaryArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterNonFeatureChainPrimaryExpression(KerMLParser.NonFeatureChainPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitNonFeatureChainPrimaryExpression(KerMLParser.NonFeatureChainPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryArgumentValue}.
	 * @param ctx the parse tree
	 */
	void enterNonFeatureChainPrimaryArgumentValue(KerMLParser.NonFeatureChainPrimaryArgumentValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryArgumentValue}.
	 * @param ctx the parse tree
	 */
	void exitNonFeatureChainPrimaryArgumentValue(KerMLParser.NonFeatureChainPrimaryArgumentValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryArgument}.
	 * @param ctx the parse tree
	 */
	void enterNonFeatureChainPrimaryArgument(KerMLParser.NonFeatureChainPrimaryArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryArgument}.
	 * @param ctx the parse tree
	 */
	void exitNonFeatureChainPrimaryArgument(KerMLParser.NonFeatureChainPrimaryArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterNonFeatureChainPrimaryArgumentMember(KerMLParser.NonFeatureChainPrimaryArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitNonFeatureChainPrimaryArgumentMember(KerMLParser.NonFeatureChainPrimaryArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#bracketExpression}.
	 * @param ctx the parse tree
	 */
	void enterBracketExpression(KerMLParser.BracketExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#bracketExpression}.
	 * @param ctx the parse tree
	 */
	void exitBracketExpression(KerMLParser.BracketExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#indexExpression}.
	 * @param ctx the parse tree
	 */
	void enterIndexExpression(KerMLParser.IndexExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#indexExpression}.
	 * @param ctx the parse tree
	 */
	void exitIndexExpression(KerMLParser.IndexExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#sequenceExpression}.
	 * @param ctx the parse tree
	 */
	void enterSequenceExpression(KerMLParser.SequenceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#sequenceExpression}.
	 * @param ctx the parse tree
	 */
	void exitSequenceExpression(KerMLParser.SequenceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#sequenceExpressionList}.
	 * @param ctx the parse tree
	 */
	void enterSequenceExpressionList(KerMLParser.SequenceExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#sequenceExpressionList}.
	 * @param ctx the parse tree
	 */
	void exitSequenceExpressionList(KerMLParser.SequenceExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#sequenceOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void enterSequenceOperatorExpression(KerMLParser.SequenceOperatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#sequenceOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void exitSequenceOperatorExpression(KerMLParser.SequenceOperatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#sequenceExpressionListMember}.
	 * @param ctx the parse tree
	 */
	void enterSequenceExpressionListMember(KerMLParser.SequenceExpressionListMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#sequenceExpressionListMember}.
	 * @param ctx the parse tree
	 */
	void exitSequenceExpressionListMember(KerMLParser.SequenceExpressionListMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureChainExpression}.
	 * @param ctx the parse tree
	 */
	void enterFeatureChainExpression(KerMLParser.FeatureChainExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureChainExpression}.
	 * @param ctx the parse tree
	 */
	void exitFeatureChainExpression(KerMLParser.FeatureChainExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#collectExpression}.
	 * @param ctx the parse tree
	 */
	void enterCollectExpression(KerMLParser.CollectExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#collectExpression}.
	 * @param ctx the parse tree
	 */
	void exitCollectExpression(KerMLParser.CollectExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#selectExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpression(KerMLParser.SelectExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#selectExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpression(KerMLParser.SelectExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#functionOperationExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionOperationExpression(KerMLParser.FunctionOperationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#functionOperationExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionOperationExpression(KerMLParser.FunctionOperationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#bodyArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterBodyArgumentMember(KerMLParser.BodyArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#bodyArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitBodyArgumentMember(KerMLParser.BodyArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#bodyArgument}.
	 * @param ctx the parse tree
	 */
	void enterBodyArgument(KerMLParser.BodyArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#bodyArgument}.
	 * @param ctx the parse tree
	 */
	void exitBodyArgument(KerMLParser.BodyArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#bodyArgumentValue}.
	 * @param ctx the parse tree
	 */
	void enterBodyArgumentValue(KerMLParser.BodyArgumentValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#bodyArgumentValue}.
	 * @param ctx the parse tree
	 */
	void exitBodyArgumentValue(KerMLParser.BodyArgumentValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#functionReferenceArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReferenceArgumentMember(KerMLParser.FunctionReferenceArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#functionReferenceArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReferenceArgumentMember(KerMLParser.FunctionReferenceArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#functionReferenceArgument}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReferenceArgument(KerMLParser.FunctionReferenceArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#functionReferenceArgument}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReferenceArgument(KerMLParser.FunctionReferenceArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#functionReferenceArgumentValue}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReferenceArgumentValue(KerMLParser.FunctionReferenceArgumentValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#functionReferenceArgumentValue}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReferenceArgumentValue(KerMLParser.FunctionReferenceArgumentValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#functionReferenceExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReferenceExpression(KerMLParser.FunctionReferenceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#functionReferenceExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReferenceExpression(KerMLParser.FunctionReferenceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#functionReferenceMember}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReferenceMember(KerMLParser.FunctionReferenceMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#functionReferenceMember}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReferenceMember(KerMLParser.FunctionReferenceMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#functionReference}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReference(KerMLParser.FunctionReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#functionReference}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReference(KerMLParser.FunctionReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureChainMember}.
	 * @param ctx the parse tree
	 */
	void enterFeatureChainMember(KerMLParser.FeatureChainMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureChainMember}.
	 * @param ctx the parse tree
	 */
	void exitFeatureChainMember(KerMLParser.FeatureChainMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#invocationTypeMember}.
	 * @param ctx the parse tree
	 */
	void enterInvocationTypeMember(KerMLParser.InvocationTypeMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#invocationTypeMember}.
	 * @param ctx the parse tree
	 */
	void exitInvocationTypeMember(KerMLParser.InvocationTypeMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#invocationType}.
	 * @param ctx the parse tree
	 */
	void enterInvocationType(KerMLParser.InvocationTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#invocationType}.
	 * @param ctx the parse tree
	 */
	void exitInvocationType(KerMLParser.InvocationTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#baseExpression}.
	 * @param ctx the parse tree
	 */
	void enterBaseExpression(KerMLParser.BaseExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#baseExpression}.
	 * @param ctx the parse tree
	 */
	void exitBaseExpression(KerMLParser.BaseExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#nullExpression}.
	 * @param ctx the parse tree
	 */
	void enterNullExpression(KerMLParser.NullExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#nullExpression}.
	 * @param ctx the parse tree
	 */
	void exitNullExpression(KerMLParser.NullExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureReferenceExpression}.
	 * @param ctx the parse tree
	 */
	void enterFeatureReferenceExpression(KerMLParser.FeatureReferenceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureReferenceExpression}.
	 * @param ctx the parse tree
	 */
	void exitFeatureReferenceExpression(KerMLParser.FeatureReferenceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureReferenceMember}.
	 * @param ctx the parse tree
	 */
	void enterFeatureReferenceMember(KerMLParser.FeatureReferenceMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureReferenceMember}.
	 * @param ctx the parse tree
	 */
	void exitFeatureReferenceMember(KerMLParser.FeatureReferenceMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureReference}.
	 * @param ctx the parse tree
	 */
	void enterFeatureReference(KerMLParser.FeatureReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureReference}.
	 * @param ctx the parse tree
	 */
	void exitFeatureReference(KerMLParser.FeatureReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metadataAccessExpression}.
	 * @param ctx the parse tree
	 */
	void enterMetadataAccessExpression(KerMLParser.MetadataAccessExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metadataAccessExpression}.
	 * @param ctx the parse tree
	 */
	void exitMetadataAccessExpression(KerMLParser.MetadataAccessExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#elementReferenceMember}.
	 * @param ctx the parse tree
	 */
	void enterElementReferenceMember(KerMLParser.ElementReferenceMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#elementReferenceMember}.
	 * @param ctx the parse tree
	 */
	void exitElementReferenceMember(KerMLParser.ElementReferenceMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#invocationExpression}.
	 * @param ctx the parse tree
	 */
	void enterInvocationExpression(KerMLParser.InvocationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#invocationExpression}.
	 * @param ctx the parse tree
	 */
	void exitInvocationExpression(KerMLParser.InvocationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#constructorExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstructorExpression(KerMLParser.ConstructorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#constructorExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstructorExpression(KerMLParser.ConstructorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#constructorResultMember}.
	 * @param ctx the parse tree
	 */
	void enterConstructorResultMember(KerMLParser.ConstructorResultMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#constructorResultMember}.
	 * @param ctx the parse tree
	 */
	void exitConstructorResultMember(KerMLParser.ConstructorResultMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#constructorResult}.
	 * @param ctx the parse tree
	 */
	void enterConstructorResult(KerMLParser.ConstructorResultContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#constructorResult}.
	 * @param ctx the parse tree
	 */
	void exitConstructorResult(KerMLParser.ConstructorResultContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#instantiatedTypeMember}.
	 * @param ctx the parse tree
	 */
	void enterInstantiatedTypeMember(KerMLParser.InstantiatedTypeMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#instantiatedTypeMember}.
	 * @param ctx the parse tree
	 */
	void exitInstantiatedTypeMember(KerMLParser.InstantiatedTypeMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#instantiatedTypeReference}.
	 * @param ctx the parse tree
	 */
	void enterInstantiatedTypeReference(KerMLParser.InstantiatedTypeReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#instantiatedTypeReference}.
	 * @param ctx the parse tree
	 */
	void exitInstantiatedTypeReference(KerMLParser.InstantiatedTypeReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedFeatureChainMember}.
	 * @param ctx the parse tree
	 */
	void enterOwnedFeatureChainMember(KerMLParser.OwnedFeatureChainMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedFeatureChainMember}.
	 * @param ctx the parse tree
	 */
	void exitOwnedFeatureChainMember(KerMLParser.OwnedFeatureChainMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(KerMLParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(KerMLParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#positionalArgumentList}.
	 * @param ctx the parse tree
	 */
	void enterPositionalArgumentList(KerMLParser.PositionalArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#positionalArgumentList}.
	 * @param ctx the parse tree
	 */
	void exitPositionalArgumentList(KerMLParser.PositionalArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#namedArgumentList}.
	 * @param ctx the parse tree
	 */
	void enterNamedArgumentList(KerMLParser.NamedArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#namedArgumentList}.
	 * @param ctx the parse tree
	 */
	void exitNamedArgumentList(KerMLParser.NamedArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#namedArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterNamedArgumentMember(KerMLParser.NamedArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#namedArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitNamedArgumentMember(KerMLParser.NamedArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#namedArgument}.
	 * @param ctx the parse tree
	 */
	void enterNamedArgument(KerMLParser.NamedArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#namedArgument}.
	 * @param ctx the parse tree
	 */
	void exitNamedArgument(KerMLParser.NamedArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#parameterRedefinition}.
	 * @param ctx the parse tree
	 */
	void enterParameterRedefinition(KerMLParser.ParameterRedefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#parameterRedefinition}.
	 * @param ctx the parse tree
	 */
	void exitParameterRedefinition(KerMLParser.ParameterRedefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#bodyExpression}.
	 * @param ctx the parse tree
	 */
	void enterBodyExpression(KerMLParser.BodyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#bodyExpression}.
	 * @param ctx the parse tree
	 */
	void exitBodyExpression(KerMLParser.BodyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#expressionBodyMember}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBodyMember(KerMLParser.ExpressionBodyMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#expressionBodyMember}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBodyMember(KerMLParser.ExpressionBodyMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#expressionBody}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBody(KerMLParser.ExpressionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#expressionBody}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBody(KerMLParser.ExpressionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#literalExpression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpression(KerMLParser.LiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#literalExpression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpression(KerMLParser.LiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#literalBoolean}.
	 * @param ctx the parse tree
	 */
	void enterLiteralBoolean(KerMLParser.LiteralBooleanContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#literalBoolean}.
	 * @param ctx the parse tree
	 */
	void exitLiteralBoolean(KerMLParser.LiteralBooleanContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(KerMLParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(KerMLParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#literalString}.
	 * @param ctx the parse tree
	 */
	void enterLiteralString(KerMLParser.LiteralStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#literalString}.
	 * @param ctx the parse tree
	 */
	void exitLiteralString(KerMLParser.LiteralStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#literalInteger}.
	 * @param ctx the parse tree
	 */
	void enterLiteralInteger(KerMLParser.LiteralIntegerContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#literalInteger}.
	 * @param ctx the parse tree
	 */
	void exitLiteralInteger(KerMLParser.LiteralIntegerContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#literalReal}.
	 * @param ctx the parse tree
	 */
	void enterLiteralReal(KerMLParser.LiteralRealContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#literalReal}.
	 * @param ctx the parse tree
	 */
	void exitLiteralReal(KerMLParser.LiteralRealContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#realValue}.
	 * @param ctx the parse tree
	 */
	void enterRealValue(KerMLParser.RealValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#realValue}.
	 * @param ctx the parse tree
	 */
	void exitRealValue(KerMLParser.RealValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#literalInfinity}.
	 * @param ctx the parse tree
	 */
	void enterLiteralInfinity(KerMLParser.LiteralInfinityContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#literalInfinity}.
	 * @param ctx the parse tree
	 */
	void exitLiteralInfinity(KerMLParser.LiteralInfinityContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#interaction}.
	 * @param ctx the parse tree
	 */
	void enterInteraction(KerMLParser.InteractionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#interaction}.
	 * @param ctx the parse tree
	 */
	void exitInteraction(KerMLParser.InteractionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#flow}.
	 * @param ctx the parse tree
	 */
	void enterFlow(KerMLParser.FlowContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#flow}.
	 * @param ctx the parse tree
	 */
	void exitFlow(KerMLParser.FlowContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#successionFlow}.
	 * @param ctx the parse tree
	 */
	void enterSuccessionFlow(KerMLParser.SuccessionFlowContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#successionFlow}.
	 * @param ctx the parse tree
	 */
	void exitSuccessionFlow(KerMLParser.SuccessionFlowContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#itemFlowDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterItemFlowDeclaration(KerMLParser.ItemFlowDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#itemFlowDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitItemFlowDeclaration(KerMLParser.ItemFlowDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#payloadFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterPayloadFeatureMember(KerMLParser.PayloadFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#payloadFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitPayloadFeatureMember(KerMLParser.PayloadFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#payloadFeature}.
	 * @param ctx the parse tree
	 */
	void enterPayloadFeature(KerMLParser.PayloadFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#payloadFeature}.
	 * @param ctx the parse tree
	 */
	void exitPayloadFeature(KerMLParser.PayloadFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#payloadFeatureSpecializationPart}.
	 * @param ctx the parse tree
	 */
	void enterPayloadFeatureSpecializationPart(KerMLParser.PayloadFeatureSpecializationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#payloadFeatureSpecializationPart}.
	 * @param ctx the parse tree
	 */
	void exitPayloadFeatureSpecializationPart(KerMLParser.PayloadFeatureSpecializationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#flowEndMember}.
	 * @param ctx the parse tree
	 */
	void enterFlowEndMember(KerMLParser.FlowEndMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#flowEndMember}.
	 * @param ctx the parse tree
	 */
	void exitFlowEndMember(KerMLParser.FlowEndMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#flowEnd}.
	 * @param ctx the parse tree
	 */
	void enterFlowEnd(KerMLParser.FlowEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#flowEnd}.
	 * @param ctx the parse tree
	 */
	void exitFlowEnd(KerMLParser.FlowEndContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#flowFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterFlowFeatureMember(KerMLParser.FlowFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#flowFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitFlowFeatureMember(KerMLParser.FlowFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#flowFeature}.
	 * @param ctx the parse tree
	 */
	void enterFlowFeature(KerMLParser.FlowFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#flowFeature}.
	 * @param ctx the parse tree
	 */
	void exitFlowFeature(KerMLParser.FlowFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#flowFeatureRedefinition}.
	 * @param ctx the parse tree
	 */
	void enterFlowFeatureRedefinition(KerMLParser.FlowFeatureRedefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#flowFeatureRedefinition}.
	 * @param ctx the parse tree
	 */
	void exitFlowFeatureRedefinition(KerMLParser.FlowFeatureRedefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#valuePart}.
	 * @param ctx the parse tree
	 */
	void enterValuePart(KerMLParser.ValuePartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#valuePart}.
	 * @param ctx the parse tree
	 */
	void exitValuePart(KerMLParser.ValuePartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#featureValue}.
	 * @param ctx the parse tree
	 */
	void enterFeatureValue(KerMLParser.FeatureValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#featureValue}.
	 * @param ctx the parse tree
	 */
	void exitFeatureValue(KerMLParser.FeatureValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#multiplicity}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicity(KerMLParser.MultiplicityContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#multiplicity}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicity(KerMLParser.MultiplicityContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#multiplicitySubset}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicitySubset(KerMLParser.MultiplicitySubsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#multiplicitySubset}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicitySubset(KerMLParser.MultiplicitySubsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#multiplicityRange}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicityRange(KerMLParser.MultiplicityRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#multiplicityRange}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicityRange(KerMLParser.MultiplicityRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedMultiplicity}.
	 * @param ctx the parse tree
	 */
	void enterOwnedMultiplicity(KerMLParser.OwnedMultiplicityContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedMultiplicity}.
	 * @param ctx the parse tree
	 */
	void exitOwnedMultiplicity(KerMLParser.OwnedMultiplicityContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#ownedMultiplicityRange}.
	 * @param ctx the parse tree
	 */
	void enterOwnedMultiplicityRange(KerMLParser.OwnedMultiplicityRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#ownedMultiplicityRange}.
	 * @param ctx the parse tree
	 */
	void exitOwnedMultiplicityRange(KerMLParser.OwnedMultiplicityRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#multiplicityBounds}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicityBounds(KerMLParser.MultiplicityBoundsContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#multiplicityBounds}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicityBounds(KerMLParser.MultiplicityBoundsContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#multiplicityExpressionMember}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicityExpressionMember(KerMLParser.MultiplicityExpressionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#multiplicityExpressionMember}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicityExpressionMember(KerMLParser.MultiplicityExpressionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metaclass}.
	 * @param ctx the parse tree
	 */
	void enterMetaclass(KerMLParser.MetaclassContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metaclass}.
	 * @param ctx the parse tree
	 */
	void exitMetaclass(KerMLParser.MetaclassContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#prefixMetadataAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterPrefixMetadataAnnotation(KerMLParser.PrefixMetadataAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#prefixMetadataAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitPrefixMetadataAnnotation(KerMLParser.PrefixMetadataAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#prefixMetadataMember}.
	 * @param ctx the parse tree
	 */
	void enterPrefixMetadataMember(KerMLParser.PrefixMetadataMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#prefixMetadataMember}.
	 * @param ctx the parse tree
	 */
	void exitPrefixMetadataMember(KerMLParser.PrefixMetadataMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#prefixMetadataFeature}.
	 * @param ctx the parse tree
	 */
	void enterPrefixMetadataFeature(KerMLParser.PrefixMetadataFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#prefixMetadataFeature}.
	 * @param ctx the parse tree
	 */
	void exitPrefixMetadataFeature(KerMLParser.PrefixMetadataFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metadataFeature}.
	 * @param ctx the parse tree
	 */
	void enterMetadataFeature(KerMLParser.MetadataFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metadataFeature}.
	 * @param ctx the parse tree
	 */
	void exitMetadataFeature(KerMLParser.MetadataFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metadataFeatureDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMetadataFeatureDeclaration(KerMLParser.MetadataFeatureDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metadataFeatureDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMetadataFeatureDeclaration(KerMLParser.MetadataFeatureDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metadataBody}.
	 * @param ctx the parse tree
	 */
	void enterMetadataBody(KerMLParser.MetadataBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metadataBody}.
	 * @param ctx the parse tree
	 */
	void exitMetadataBody(KerMLParser.MetadataBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metadataBodyElement}.
	 * @param ctx the parse tree
	 */
	void enterMetadataBodyElement(KerMLParser.MetadataBodyElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metadataBodyElement}.
	 * @param ctx the parse tree
	 */
	void exitMetadataBodyElement(KerMLParser.MetadataBodyElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metadataBodyFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterMetadataBodyFeatureMember(KerMLParser.MetadataBodyFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metadataBodyFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitMetadataBodyFeatureMember(KerMLParser.MetadataBodyFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#metadataBodyFeature}.
	 * @param ctx the parse tree
	 */
	void enterMetadataBodyFeature(KerMLParser.MetadataBodyFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#metadataBodyFeature}.
	 * @param ctx the parse tree
	 */
	void exitMetadataBodyFeature(KerMLParser.MetadataBodyFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#package}.
	 * @param ctx the parse tree
	 */
	void enterPackage(KerMLParser.PackageContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#package}.
	 * @param ctx the parse tree
	 */
	void exitPackage(KerMLParser.PackageContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#libraryPackage}.
	 * @param ctx the parse tree
	 */
	void enterLibraryPackage(KerMLParser.LibraryPackageContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#libraryPackage}.
	 * @param ctx the parse tree
	 */
	void exitLibraryPackage(KerMLParser.LibraryPackageContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPackageDeclaration(KerMLParser.PackageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPackageDeclaration(KerMLParser.PackageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#packageBody}.
	 * @param ctx the parse tree
	 */
	void enterPackageBody(KerMLParser.PackageBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#packageBody}.
	 * @param ctx the parse tree
	 */
	void exitPackageBody(KerMLParser.PackageBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#elementFilterMember}.
	 * @param ctx the parse tree
	 */
	void enterElementFilterMember(KerMLParser.ElementFilterMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#elementFilterMember}.
	 * @param ctx the parse tree
	 */
	void exitElementFilterMember(KerMLParser.ElementFilterMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#view}.
	 * @param ctx the parse tree
	 */
	void enterView(KerMLParser.ViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#view}.
	 * @param ctx the parse tree
	 */
	void exitView(KerMLParser.ViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#viewBody}.
	 * @param ctx the parse tree
	 */
	void enterViewBody(KerMLParser.ViewBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#viewBody}.
	 * @param ctx the parse tree
	 */
	void exitViewBody(KerMLParser.ViewBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#viewBodyElement}.
	 * @param ctx the parse tree
	 */
	void enterViewBodyElement(KerMLParser.ViewBodyElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#viewBodyElement}.
	 * @param ctx the parse tree
	 */
	void exitViewBodyElement(KerMLParser.ViewBodyElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#expose}.
	 * @param ctx the parse tree
	 */
	void enterExpose(KerMLParser.ExposeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#expose}.
	 * @param ctx the parse tree
	 */
	void exitExpose(KerMLParser.ExposeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#viewRenderingMember}.
	 * @param ctx the parse tree
	 */
	void enterViewRenderingMember(KerMLParser.ViewRenderingMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#viewRenderingMember}.
	 * @param ctx the parse tree
	 */
	void exitViewRenderingMember(KerMLParser.ViewRenderingMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#rendering}.
	 * @param ctx the parse tree
	 */
	void enterRendering(KerMLParser.RenderingContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#rendering}.
	 * @param ctx the parse tree
	 */
	void exitRendering(KerMLParser.RenderingContext ctx);
	/**
	 * Enter a parse tree produced by {@link KerMLParser#viewpoint}.
	 * @param ctx the parse tree
	 */
	void enterViewpoint(KerMLParser.ViewpointContext ctx);
	/**
	 * Exit a parse tree produced by {@link KerMLParser#viewpoint}.
	 * @param ctx the parse tree
	 */
	void exitViewpoint(KerMLParser.ViewpointContext ctx);
}