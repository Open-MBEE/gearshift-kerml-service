// Generated from /Users/chasgaley/git/gearshift-kerml-service/gearshift-kerml-runtime/src/main/antlr/KerML.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link KerMLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface KerMLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typedByToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedByToken(KerMLParser.TypedByTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#specializesToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecializesToken(KerMLParser.SpecializesTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#subsetsToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubsetsToken(KerMLParser.SubsetsTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#referencesToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferencesToken(KerMLParser.ReferencesTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#crossesToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCrossesToken(KerMLParser.CrossesTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#redefinesToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRedefinesToken(KerMLParser.RedefinesTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#conjugatesToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjugatesToken(KerMLParser.ConjugatesTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#identification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentification(KerMLParser.IdentificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#relationshipBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationshipBody(KerMLParser.RelationshipBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#relationshipOwnedElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationshipOwnedElement(KerMLParser.RelationshipOwnedElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedRelatedElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedRelatedElement(KerMLParser.OwnedRelatedElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#dependency}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDependency(KerMLParser.DependencyContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(KerMLParser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedAnnotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedAnnotation(KerMLParser.OwnedAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#annotatingElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotatingElement(KerMLParser.AnnotatingElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(KerMLParser.CommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#documentation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocumentation(KerMLParser.DocumentationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#textualRepresentation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextualRepresentation(KerMLParser.TextualRepresentationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#rootNamespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRootNamespace(KerMLParser.RootNamespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#namespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespace(KerMLParser.NamespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#namespaceDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceDeclaration(KerMLParser.NamespaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#namespaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceBody(KerMLParser.NamespaceBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#namespaceBodyElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceBodyElement(KerMLParser.NamespaceBodyElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#memberPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberPrefix(KerMLParser.MemberPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#visibilityIndicator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisibilityIndicator(KerMLParser.VisibilityIndicatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#namespaceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceMember(KerMLParser.NamespaceMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#nonFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonFeatureMember(KerMLParser.NonFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#namespaceFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceFeatureMember(KerMLParser.NamespaceFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#aliasMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAliasMember(KerMLParser.AliasMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(KerMLParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#import_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_(KerMLParser.Import_Context ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#importDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeclaration(KerMLParser.ImportDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#membershipImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembershipImport(KerMLParser.MembershipImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#namespaceImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceImport(KerMLParser.NamespaceImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#filterPackageImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterPackageImport(KerMLParser.FilterPackageImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#filterPackageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterPackageMember(KerMLParser.FilterPackageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#memberElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberElement(KerMLParser.MemberElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#nonFeatureElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonFeatureElement(KerMLParser.NonFeatureElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureElement(KerMLParser.FeatureElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(KerMLParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypePrefix(KerMLParser.TypePrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDeclaration(KerMLParser.TypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#specializationPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecializationPart(KerMLParser.SpecializationPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#conjugationPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjugationPart(KerMLParser.ConjugationPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typeRelationshipPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeRelationshipPart(KerMLParser.TypeRelationshipPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#disjoiningPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjoiningPart(KerMLParser.DisjoiningPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#unioningPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnioningPart(KerMLParser.UnioningPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#intersectingPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntersectingPart(KerMLParser.IntersectingPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#differencingPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDifferencingPart(KerMLParser.DifferencingPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typeBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeBody(KerMLParser.TypeBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typeBodyElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeBodyElement(KerMLParser.TypeBodyElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#specialization}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecialization(KerMLParser.SpecializationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedSpecialization}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedSpecialization(KerMLParser.OwnedSpecializationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#specificType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecificType(KerMLParser.SpecificTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#generalType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneralType(KerMLParser.GeneralTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#conjugation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjugation(KerMLParser.ConjugationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedConjugation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedConjugation(KerMLParser.OwnedConjugationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#disjoining}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjoining(KerMLParser.DisjoiningContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedDisjoining}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedDisjoining(KerMLParser.OwnedDisjoiningContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#unioning}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnioning(KerMLParser.UnioningContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#intersecting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntersecting(KerMLParser.IntersectingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#differencing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDifferencing(KerMLParser.DifferencingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureMember(KerMLParser.FeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typeFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeFeatureMember(KerMLParser.TypeFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedFeatureMember(KerMLParser.OwnedFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#classifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifier(KerMLParser.ClassifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#classifierDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassifierDeclaration(KerMLParser.ClassifierDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#superclassingPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuperclassingPart(KerMLParser.SuperclassingPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#subclassification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubclassification(KerMLParser.SubclassificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedSubclassification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedSubclassification(KerMLParser.OwnedSubclassificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#feature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeature(KerMLParser.FeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#endFeaturePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndFeaturePrefix(KerMLParser.EndFeaturePrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#basicFeaturePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicFeaturePrefix(KerMLParser.BasicFeaturePrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featurePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeaturePrefix(KerMLParser.FeaturePrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedCrossFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedCrossFeatureMember(KerMLParser.OwnedCrossFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedCrossFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedCrossFeature(KerMLParser.OwnedCrossFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureDirection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureDirection(KerMLParser.FeatureDirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureDeclaration(KerMLParser.FeatureDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureIdentification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureIdentification(KerMLParser.FeatureIdentificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureRelationshipPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureRelationshipPart(KerMLParser.FeatureRelationshipPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#chainingPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChainingPart(KerMLParser.ChainingPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#invertingPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvertingPart(KerMLParser.InvertingPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typeFeaturingPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeFeaturingPart(KerMLParser.TypeFeaturingPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureSpecializationPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureSpecializationPart(KerMLParser.FeatureSpecializationPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#multiplicityPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicityPart(KerMLParser.MultiplicityPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureSpecialization}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureSpecialization(KerMLParser.FeatureSpecializationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typings}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypings(KerMLParser.TypingsContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typedBy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedBy(KerMLParser.TypedByContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#subsettings}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubsettings(KerMLParser.SubsettingsContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#subsets}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubsets(KerMLParser.SubsetsContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#references}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferences(KerMLParser.ReferencesContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#crosses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCrosses(KerMLParser.CrossesContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#redefinitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRedefinitions(KerMLParser.RedefinitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#redefines}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRedefines(KerMLParser.RedefinesContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureTyping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureTyping(KerMLParser.FeatureTypingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedFeatureTyping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedFeatureTyping(KerMLParser.OwnedFeatureTypingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#subsetting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubsetting(KerMLParser.SubsettingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedSubsetting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedSubsetting(KerMLParser.OwnedSubsettingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedReferenceSubsetting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedReferenceSubsetting(KerMLParser.OwnedReferenceSubsettingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedCrossSubsetting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedCrossSubsetting(KerMLParser.OwnedCrossSubsettingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#redefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRedefinition(KerMLParser.RedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedRedefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedRedefinition(KerMLParser.OwnedRedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedFeatureChain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedFeatureChain(KerMLParser.OwnedFeatureChainContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureChain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureChain(KerMLParser.FeatureChainContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedFeatureChaining}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedFeatureChaining(KerMLParser.OwnedFeatureChainingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureInverting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureInverting(KerMLParser.FeatureInvertingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedFeatureInverting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedFeatureInverting(KerMLParser.OwnedFeatureInvertingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typeFeaturing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeFeaturing(KerMLParser.TypeFeaturingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedTypeFeaturing}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedTypeFeaturing(KerMLParser.OwnedTypeFeaturingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#datatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatype(KerMLParser.DatatypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#class}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass(KerMLParser.ClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#structure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructure(KerMLParser.StructureContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#association}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssociation(KerMLParser.AssociationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#associationStructure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssociationStructure(KerMLParser.AssociationStructureContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#connector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnector(KerMLParser.ConnectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#connectorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectorDeclaration(KerMLParser.ConnectorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#binaryConnectorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryConnectorDeclaration(KerMLParser.BinaryConnectorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#naryConnectorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNaryConnectorDeclaration(KerMLParser.NaryConnectorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#connectorEndMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectorEndMember(KerMLParser.ConnectorEndMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#connectorEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectorEnd(KerMLParser.ConnectorEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedCrossMultiplicityMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedCrossMultiplicityMember(KerMLParser.OwnedCrossMultiplicityMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedCrossMultiplicity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedCrossMultiplicity(KerMLParser.OwnedCrossMultiplicityContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#bindingConnector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBindingConnector(KerMLParser.BindingConnectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#bindingConnectorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBindingConnectorDeclaration(KerMLParser.BindingConnectorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#succession}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuccession(KerMLParser.SuccessionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#successionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuccessionDeclaration(KerMLParser.SuccessionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#behavior}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBehavior(KerMLParser.BehaviorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#step}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStep(KerMLParser.StepContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(KerMLParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#functionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBody(KerMLParser.FunctionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#functionBodyPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBodyPart(KerMLParser.FunctionBodyPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#returnFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnFeatureMember(KerMLParser.ReturnFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#resultExpressionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResultExpressionMember(KerMLParser.ResultExpressionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(KerMLParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(KerMLParser.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#booleanExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanExpression(KerMLParser.BooleanExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#invariant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvariant(KerMLParser.InvariantContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedExpressionReferenceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedExpressionReferenceMember(KerMLParser.OwnedExpressionReferenceMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedExpressionReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedExpressionReference(KerMLParser.OwnedExpressionReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedExpressionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedExpressionMember(KerMLParser.OwnedExpressionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedExpression(KerMLParser.OwnedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#conditionalBinaryOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalBinaryOperator(KerMLParser.ConditionalBinaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#binaryOperatorExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryOperatorExpression(KerMLParser.BinaryOperatorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#binaryOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryOperator(KerMLParser.BinaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#unaryOperatorExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOperatorExpression(KerMLParser.UnaryOperatorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#unaryOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOperator(KerMLParser.UnaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#classificationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassificationExpression(KerMLParser.ClassificationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#classificationTestOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassificationTestOperator(KerMLParser.ClassificationTestOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#castOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastOperator(KerMLParser.CastOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metaclassificationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetaclassificationExpression(KerMLParser.MetaclassificationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#argumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentMember(KerMLParser.ArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(KerMLParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#argumentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentValue(KerMLParser.ArgumentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#argumentExpressionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentExpressionMember(KerMLParser.ArgumentExpressionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#argumentExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentExpression(KerMLParser.ArgumentExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#argumentExpressionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentExpressionValue(KerMLParser.ArgumentExpressionValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metadataArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataArgumentMember(KerMLParser.MetadataArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metadataArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataArgument(KerMLParser.MetadataArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metadataValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataValue(KerMLParser.MetadataValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metadataReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataReference(KerMLParser.MetadataReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metaclassificationTestOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetaclassificationTestOperator(KerMLParser.MetaclassificationTestOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metacastOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetacastOperator(KerMLParser.MetacastOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#extentExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtentExpression(KerMLParser.ExtentExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typeReferenceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeReferenceMember(KerMLParser.TypeReferenceMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typeResultMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeResultMember(KerMLParser.TypeResultMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#typeReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeReference(KerMLParser.TypeReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#referenceTyping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceTyping(KerMLParser.ReferenceTypingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#emptyResultMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyResultMember(KerMLParser.EmptyResultMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#emptyFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyFeature(KerMLParser.EmptyFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(KerMLParser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#primaryArgumentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryArgumentValue(KerMLParser.PrimaryArgumentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#primaryArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryArgument(KerMLParser.PrimaryArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#primaryArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryArgumentMember(KerMLParser.PrimaryArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonFeatureChainPrimaryExpression(KerMLParser.NonFeatureChainPrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryArgumentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonFeatureChainPrimaryArgumentValue(KerMLParser.NonFeatureChainPrimaryArgumentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonFeatureChainPrimaryArgument(KerMLParser.NonFeatureChainPrimaryArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#nonFeatureChainPrimaryArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonFeatureChainPrimaryArgumentMember(KerMLParser.NonFeatureChainPrimaryArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#bracketExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketExpression(KerMLParser.BracketExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#indexExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexExpression(KerMLParser.IndexExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#sequenceExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceExpression(KerMLParser.SequenceExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#sequenceExpressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceExpressionList(KerMLParser.SequenceExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#sequenceOperatorExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceOperatorExpression(KerMLParser.SequenceOperatorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#sequenceExpressionListMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceExpressionListMember(KerMLParser.SequenceExpressionListMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureChainExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureChainExpression(KerMLParser.FeatureChainExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#collectExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectExpression(KerMLParser.CollectExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#selectExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectExpression(KerMLParser.SelectExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#functionOperationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionOperationExpression(KerMLParser.FunctionOperationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#bodyArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyArgumentMember(KerMLParser.BodyArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#bodyArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyArgument(KerMLParser.BodyArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#bodyArgumentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyArgumentValue(KerMLParser.BodyArgumentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#functionReferenceArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReferenceArgumentMember(KerMLParser.FunctionReferenceArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#functionReferenceArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReferenceArgument(KerMLParser.FunctionReferenceArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#functionReferenceArgumentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReferenceArgumentValue(KerMLParser.FunctionReferenceArgumentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#functionReferenceExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReferenceExpression(KerMLParser.FunctionReferenceExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#functionReferenceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReferenceMember(KerMLParser.FunctionReferenceMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#functionReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReference(KerMLParser.FunctionReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureChainMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureChainMember(KerMLParser.FeatureChainMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#invocationTypeMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvocationTypeMember(KerMLParser.InvocationTypeMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#invocationType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvocationType(KerMLParser.InvocationTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#baseExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseExpression(KerMLParser.BaseExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#nullExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullExpression(KerMLParser.NullExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureReferenceExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureReferenceExpression(KerMLParser.FeatureReferenceExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureReferenceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureReferenceMember(KerMLParser.FeatureReferenceMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureReference(KerMLParser.FeatureReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metadataAccessExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataAccessExpression(KerMLParser.MetadataAccessExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#elementReferenceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementReferenceMember(KerMLParser.ElementReferenceMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#invocationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvocationExpression(KerMLParser.InvocationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#constructorExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorExpression(KerMLParser.ConstructorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#constructorResultMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorResultMember(KerMLParser.ConstructorResultMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#constructorResult}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorResult(KerMLParser.ConstructorResultContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#instantiatedTypeMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstantiatedTypeMember(KerMLParser.InstantiatedTypeMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#instantiatedTypeReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstantiatedTypeReference(KerMLParser.InstantiatedTypeReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedFeatureChainMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedFeatureChainMember(KerMLParser.OwnedFeatureChainMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(KerMLParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#positionalArgumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositionalArgumentList(KerMLParser.PositionalArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#namedArgumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedArgumentList(KerMLParser.NamedArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#namedArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedArgumentMember(KerMLParser.NamedArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#namedArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedArgument(KerMLParser.NamedArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#parameterRedefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterRedefinition(KerMLParser.ParameterRedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#bodyExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyExpression(KerMLParser.BodyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#expressionBodyMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBodyMember(KerMLParser.ExpressionBodyMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#expressionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBody(KerMLParser.ExpressionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpression(KerMLParser.LiteralExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#literalBoolean}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralBoolean(KerMLParser.LiteralBooleanContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#booleanValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanValue(KerMLParser.BooleanValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#literalString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralString(KerMLParser.LiteralStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#literalInteger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralInteger(KerMLParser.LiteralIntegerContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#literalReal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralReal(KerMLParser.LiteralRealContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#realValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealValue(KerMLParser.RealValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#literalInfinity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralInfinity(KerMLParser.LiteralInfinityContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#interaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteraction(KerMLParser.InteractionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#flow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlow(KerMLParser.FlowContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#successionFlow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuccessionFlow(KerMLParser.SuccessionFlowContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#itemFlowDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitItemFlowDeclaration(KerMLParser.ItemFlowDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#payloadFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPayloadFeatureMember(KerMLParser.PayloadFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#payloadFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPayloadFeature(KerMLParser.PayloadFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#payloadFeatureSpecializationPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPayloadFeatureSpecializationPart(KerMLParser.PayloadFeatureSpecializationPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#flowEndMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowEndMember(KerMLParser.FlowEndMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#flowEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowEnd(KerMLParser.FlowEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#flowFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowFeatureMember(KerMLParser.FlowFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#flowFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowFeature(KerMLParser.FlowFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#flowFeatureRedefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowFeatureRedefinition(KerMLParser.FlowFeatureRedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#valuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuePart(KerMLParser.ValuePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#featureValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureValue(KerMLParser.FeatureValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#multiplicity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicity(KerMLParser.MultiplicityContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#multiplicitySubset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicitySubset(KerMLParser.MultiplicitySubsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#multiplicityRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicityRange(KerMLParser.MultiplicityRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedMultiplicity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedMultiplicity(KerMLParser.OwnedMultiplicityContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#ownedMultiplicityRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedMultiplicityRange(KerMLParser.OwnedMultiplicityRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#multiplicityBounds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicityBounds(KerMLParser.MultiplicityBoundsContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#multiplicityExpressionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicityExpressionMember(KerMLParser.MultiplicityExpressionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metaclass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetaclass(KerMLParser.MetaclassContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#prefixMetadataAnnotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixMetadataAnnotation(KerMLParser.PrefixMetadataAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#prefixMetadataMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixMetadataMember(KerMLParser.PrefixMetadataMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#prefixMetadataFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixMetadataFeature(KerMLParser.PrefixMetadataFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metadataFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataFeature(KerMLParser.MetadataFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metadataFeatureDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataFeatureDeclaration(KerMLParser.MetadataFeatureDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metadataBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataBody(KerMLParser.MetadataBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metadataBodyElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataBodyElement(KerMLParser.MetadataBodyElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metadataBodyFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataBodyFeatureMember(KerMLParser.MetadataBodyFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#metadataBodyFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataBodyFeature(KerMLParser.MetadataBodyFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage(KerMLParser.PackageContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#libraryPackage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibraryPackage(KerMLParser.LibraryPackageContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#packageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageDeclaration(KerMLParser.PackageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#packageBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageBody(KerMLParser.PackageBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#elementFilterMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementFilterMember(KerMLParser.ElementFilterMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitView(KerMLParser.ViewContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#viewBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewBody(KerMLParser.ViewBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#viewBodyElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewBodyElement(KerMLParser.ViewBodyElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#expose}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpose(KerMLParser.ExposeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#viewRenderingMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewRenderingMember(KerMLParser.ViewRenderingMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#rendering}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRendering(KerMLParser.RenderingContext ctx);
	/**
	 * Visit a parse tree produced by {@link KerMLParser#viewpoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewpoint(KerMLParser.ViewpointContext ctx);
}