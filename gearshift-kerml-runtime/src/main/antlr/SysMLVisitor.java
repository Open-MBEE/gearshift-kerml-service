// Generated from /Users/chasgaley/git/gearshift-kerml-service/gearshift-kerml-runtime/src/main/antlr/SysML.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SysMLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SysMLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SysMLParser#definedByToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinedByToken(SysMLParser.DefinedByTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#specializesToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecializesToken(SysMLParser.SpecializesTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#subsetsToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubsetsToken(SysMLParser.SubsetsTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#referencesToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferencesToken(SysMLParser.ReferencesTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#crossesToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCrossesToken(SysMLParser.CrossesTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#redefinesToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRedefinesToken(SysMLParser.RedefinesTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(SysMLParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#identification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentification(SysMLParser.IdentificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#relationshipBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationshipBody(SysMLParser.RelationshipBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#dependency}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDependency(SysMLParser.DependencyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#dependencyDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDependencyDeclaration(SysMLParser.DependencyDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(SysMLParser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedAnnotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedAnnotation(SysMLParser.OwnedAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#annotatingMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotatingMember(SysMLParser.AnnotatingMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#annotatingElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotatingElement(SysMLParser.AnnotatingElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(SysMLParser.CommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#documentation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocumentation(SysMLParser.DocumentationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#textualRepresentation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextualRepresentation(SysMLParser.TextualRepresentationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#rootNamespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRootNamespace(SysMLParser.RootNamespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage(SysMLParser.PackageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#libraryPackage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibraryPackage(SysMLParser.LibraryPackageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#packageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageDeclaration(SysMLParser.PackageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#packageBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageBody(SysMLParser.PackageBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#packageBodyElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageBodyElement(SysMLParser.PackageBodyElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#visibility}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisibility(SysMLParser.VisibilityContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#memberPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberPrefix(SysMLParser.MemberPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#packageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageMember(SysMLParser.PackageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#elementFilterMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementFilterMember(SysMLParser.ElementFilterMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#aliasMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAliasMember(SysMLParser.AliasMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#import_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_(SysMLParser.Import_Context ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#importDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeclaration(SysMLParser.ImportDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#membershipImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembershipImport(SysMLParser.MembershipImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#namespaceImport}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceImport(SysMLParser.NamespaceImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#filterPackage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterPackage(SysMLParser.FilterPackageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#filterPackageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterPackageMember(SysMLParser.FilterPackageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#visibilityIndicator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisibilityIndicator(SysMLParser.VisibilityIndicatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#definitionElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionElement(SysMLParser.DefinitionElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#usageElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsageElement(SysMLParser.UsageElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#basicDefinitionPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicDefinitionPrefix(SysMLParser.BasicDefinitionPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#definitionExtensionKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionExtensionKeyword(SysMLParser.DefinitionExtensionKeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#definitionPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionPrefix(SysMLParser.DefinitionPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(SysMLParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#definitionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionDeclaration(SysMLParser.DefinitionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#definitionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionBody(SysMLParser.DefinitionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#definitionBodyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionBodyItem(SysMLParser.DefinitionBodyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#definitionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinitionMember(SysMLParser.DefinitionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#variantUsageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariantUsageMember(SysMLParser.VariantUsageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#nonOccurrenceUsageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonOccurrenceUsageMember(SysMLParser.NonOccurrenceUsageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#occurrenceUsageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOccurrenceUsageMember(SysMLParser.OccurrenceUsageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#structureUsageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructureUsageMember(SysMLParser.StructureUsageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#behaviorUsageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBehaviorUsageMember(SysMLParser.BehaviorUsageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureDirection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureDirection(SysMLParser.FeatureDirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#refPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefPrefix(SysMLParser.RefPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#basicUsagePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicUsagePrefix(SysMLParser.BasicUsagePrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#endUsagePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndUsagePrefix(SysMLParser.EndUsagePrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedCrossFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedCrossFeatureMember(SysMLParser.OwnedCrossFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedCrossFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedCrossFeature(SysMLParser.OwnedCrossFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#usageExtensionKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsageExtensionKeyword(SysMLParser.UsageExtensionKeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#unextendedUsagePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnextendedUsagePrefix(SysMLParser.UnextendedUsagePrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#usagePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsagePrefix(SysMLParser.UsagePrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#usage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsage(SysMLParser.UsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#usageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsageDeclaration(SysMLParser.UsageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#usageCompletion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsageCompletion(SysMLParser.UsageCompletionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#usageBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsageBody(SysMLParser.UsageBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#valuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuePart(SysMLParser.ValuePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureValue(SysMLParser.FeatureValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#defaultReferenceUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultReferenceUsage(SysMLParser.DefaultReferenceUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#referenceUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceUsage(SysMLParser.ReferenceUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#variantReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariantReference(SysMLParser.VariantReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#nonOccurrenceUsageElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonOccurrenceUsageElement(SysMLParser.NonOccurrenceUsageElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#occurrenceUsageElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOccurrenceUsageElement(SysMLParser.OccurrenceUsageElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#structureUsageElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructureUsageElement(SysMLParser.StructureUsageElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#behaviorUsageElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBehaviorUsageElement(SysMLParser.BehaviorUsageElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#variantUsageElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariantUsageElement(SysMLParser.VariantUsageElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#subclassificationPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubclassificationPart(SysMLParser.SubclassificationPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedSubclassification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedSubclassification(SysMLParser.OwnedSubclassificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureSpecializationPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureSpecializationPart(SysMLParser.FeatureSpecializationPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureSpecialization}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureSpecialization(SysMLParser.FeatureSpecializationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#typings}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypings(SysMLParser.TypingsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#typedBy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedBy(SysMLParser.TypedByContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureTyping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureTyping(SysMLParser.FeatureTypingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedFeatureTyping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedFeatureTyping(SysMLParser.OwnedFeatureTypingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#subsettings}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubsettings(SysMLParser.SubsettingsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#subsets}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubsets(SysMLParser.SubsetsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedSubsetting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedSubsetting(SysMLParser.OwnedSubsettingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#references}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferences(SysMLParser.ReferencesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedReferenceSubsetting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedReferenceSubsetting(SysMLParser.OwnedReferenceSubsettingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#crosses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCrosses(SysMLParser.CrossesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedCrossSubsetting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedCrossSubsetting(SysMLParser.OwnedCrossSubsettingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#redefinitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRedefinitions(SysMLParser.RedefinitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#redefines}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRedefines(SysMLParser.RedefinesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedRedefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedRedefinition(SysMLParser.OwnedRedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedFeatureChain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedFeatureChain(SysMLParser.OwnedFeatureChainContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedFeatureChaining}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedFeatureChaining(SysMLParser.OwnedFeatureChainingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#multiplicityPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicityPart(SysMLParser.MultiplicityPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedMultiplicity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedMultiplicity(SysMLParser.OwnedMultiplicityContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#multiplicityRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicityRange(SysMLParser.MultiplicityRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#multiplicityExpressionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicityExpressionMember(SysMLParser.MultiplicityExpressionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#attributeDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeDefinition(SysMLParser.AttributeDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#attributeUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeUsage(SysMLParser.AttributeUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#enumerationDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumerationDefinition(SysMLParser.EnumerationDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#enumerationBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumerationBody(SysMLParser.EnumerationBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#enumerationUsageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumerationUsageMember(SysMLParser.EnumerationUsageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#enumeratedValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumeratedValue(SysMLParser.EnumeratedValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#enumerationUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumerationUsage(SysMLParser.EnumerationUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#occurrenceDefinitionPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOccurrenceDefinitionPrefix(SysMLParser.OccurrenceDefinitionPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#occurrenceDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOccurrenceDefinition(SysMLParser.OccurrenceDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#individualDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndividualDefinition(SysMLParser.IndividualDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#emptyMultiplicityMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyMultiplicityMember(SysMLParser.EmptyMultiplicityMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#emptyMultiplicity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyMultiplicity(SysMLParser.EmptyMultiplicityContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#occurrenceUsagePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOccurrenceUsagePrefix(SysMLParser.OccurrenceUsagePrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#occurrenceUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOccurrenceUsage(SysMLParser.OccurrenceUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#individualUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndividualUsage(SysMLParser.IndividualUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#portionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortionUsage(SysMLParser.PortionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#portionKindToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortionKindToken(SysMLParser.PortionKindTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#eventOccurrenceUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventOccurrenceUsage(SysMLParser.EventOccurrenceUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#sourceSuccessionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceSuccessionMember(SysMLParser.SourceSuccessionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#sourceSuccession}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceSuccession(SysMLParser.SourceSuccessionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#sourceEndMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceEndMember(SysMLParser.SourceEndMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#sourceEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceEnd(SysMLParser.SourceEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#itemDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitItemDefinition(SysMLParser.ItemDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#itemUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitItemUsage(SysMLParser.ItemUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#partDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartDefinition(SysMLParser.PartDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#partUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartUsage(SysMLParser.PartUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#portDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortDefinition(SysMLParser.PortDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#conjugatedPortDefinitionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjugatedPortDefinitionMember(SysMLParser.ConjugatedPortDefinitionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#conjugatedPortDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjugatedPortDefinition(SysMLParser.ConjugatedPortDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#portConjugation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortConjugation(SysMLParser.PortConjugationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#portUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortUsage(SysMLParser.PortUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#conjugatedPortTyping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjugatedPortTyping(SysMLParser.ConjugatedPortTypingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#connectionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectionDefinition(SysMLParser.ConnectionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#connectionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectionUsage(SysMLParser.ConnectionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#connectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectorPart(SysMLParser.ConnectorPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#binaryConnectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryConnectorPart(SysMLParser.BinaryConnectorPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#naryConnectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNaryConnectorPart(SysMLParser.NaryConnectorPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#connectorEndMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectorEndMember(SysMLParser.ConnectorEndMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#connectorEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectorEnd(SysMLParser.ConnectorEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedCrossMultiplicityMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedCrossMultiplicityMember(SysMLParser.OwnedCrossMultiplicityMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedCrossMultiplicity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedCrossMultiplicity(SysMLParser.OwnedCrossMultiplicityContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#bindingConnectorAsUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBindingConnectorAsUsage(SysMLParser.BindingConnectorAsUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#successionAsUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuccessionAsUsage(SysMLParser.SuccessionAsUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfaceDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceDefinition(SysMLParser.InterfaceDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBody(SysMLParser.InterfaceBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfaceBodyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBodyItem(SysMLParser.InterfaceBodyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfaceNonOccurrenceUsageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceNonOccurrenceUsageMember(SysMLParser.InterfaceNonOccurrenceUsageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfaceNonOccurrenceUsageElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceNonOccurrenceUsageElement(SysMLParser.InterfaceNonOccurrenceUsageElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfaceOccurrenceUsageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceOccurrenceUsageMember(SysMLParser.InterfaceOccurrenceUsageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfaceOccurrenceUsageElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceOccurrenceUsageElement(SysMLParser.InterfaceOccurrenceUsageElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#defaultInterfaceEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultInterfaceEnd(SysMLParser.DefaultInterfaceEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfaceUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceUsage(SysMLParser.InterfaceUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfaceUsageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceUsageDeclaration(SysMLParser.InterfaceUsageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfacePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfacePart(SysMLParser.InterfacePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#binaryInterfacePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryInterfacePart(SysMLParser.BinaryInterfacePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#naryInterfacePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNaryInterfacePart(SysMLParser.NaryInterfacePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfaceEndMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceEndMember(SysMLParser.InterfaceEndMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#interfaceEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceEnd(SysMLParser.InterfaceEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#allocationDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllocationDefinition(SysMLParser.AllocationDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#allocationUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllocationUsage(SysMLParser.AllocationUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#allocationUsageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllocationUsageDeclaration(SysMLParser.AllocationUsageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#flowDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowDefinition(SysMLParser.FlowDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#message}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessage(SysMLParser.MessageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#messageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageDeclaration(SysMLParser.MessageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#messageEventMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageEventMember(SysMLParser.MessageEventMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#messageEvent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessageEvent(SysMLParser.MessageEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#flowUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowUsage(SysMLParser.FlowUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#successionFlowUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuccessionFlowUsage(SysMLParser.SuccessionFlowUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#flowDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowDeclaration(SysMLParser.FlowDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#flowPayloadFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowPayloadFeatureMember(SysMLParser.FlowPayloadFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#flowPayloadFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowPayloadFeature(SysMLParser.FlowPayloadFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#payloadFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPayloadFeature(SysMLParser.PayloadFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#payloadFeatureSpecializationPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPayloadFeatureSpecializationPart(SysMLParser.PayloadFeatureSpecializationPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#flowEndMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowEndMember(SysMLParser.FlowEndMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#flowEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowEnd(SysMLParser.FlowEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#flowEndSubsetting}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowEndSubsetting(SysMLParser.FlowEndSubsettingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureChainPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureChainPrefix(SysMLParser.FeatureChainPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#flowFeatureMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowFeatureMember(SysMLParser.FlowFeatureMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#flowFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowFeature(SysMLParser.FlowFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#flowFeatureRedefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowFeatureRedefinition(SysMLParser.FlowFeatureRedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionDefinition(SysMLParser.ActionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionBody(SysMLParser.ActionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionBodyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionBodyItem(SysMLParser.ActionBodyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#nonBehaviorBodyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonBehaviorBodyItem(SysMLParser.NonBehaviorBodyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionBehaviorMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionBehaviorMember(SysMLParser.ActionBehaviorMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#initialNodeMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitialNodeMember(SysMLParser.InitialNodeMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionNodeMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionNodeMember(SysMLParser.ActionNodeMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionTargetSuccessionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionTargetSuccessionMember(SysMLParser.ActionTargetSuccessionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#guardedSuccessionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuardedSuccessionMember(SysMLParser.GuardedSuccessionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionUsage(SysMLParser.ActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionUsageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionUsageDeclaration(SysMLParser.ActionUsageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#performActionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPerformActionUsage(SysMLParser.PerformActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#performActionUsageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPerformActionUsageDeclaration(SysMLParser.PerformActionUsageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionNode(SysMLParser.ActionNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionNodeUsageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionNodeUsageDeclaration(SysMLParser.ActionNodeUsageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionNodePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionNodePrefix(SysMLParser.ActionNodePrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#controlNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlNode(SysMLParser.ControlNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#controlNodePrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlNodePrefix(SysMLParser.ControlNodePrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#mergeNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergeNode(SysMLParser.MergeNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#decisionNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecisionNode(SysMLParser.DecisionNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#joinNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinNode(SysMLParser.JoinNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#forkNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForkNode(SysMLParser.ForkNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#acceptNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAcceptNode(SysMLParser.AcceptNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#acceptNodeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAcceptNodeDeclaration(SysMLParser.AcceptNodeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#acceptParameterPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAcceptParameterPart(SysMLParser.AcceptParameterPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#payloadParameterMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPayloadParameterMember(SysMLParser.PayloadParameterMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#payloadParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPayloadParameter(SysMLParser.PayloadParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#triggerValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerValuePart(SysMLParser.TriggerValuePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#triggerFeatureValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerFeatureValue(SysMLParser.TriggerFeatureValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#triggerExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerExpression(SysMLParser.TriggerExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#argumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentMember(SysMLParser.ArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(SysMLParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#argumentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentValue(SysMLParser.ArgumentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#argumentExpressionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentExpressionMember(SysMLParser.ArgumentExpressionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#argumentExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentExpression(SysMLParser.ArgumentExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#argumentExpressionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentExpressionValue(SysMLParser.ArgumentExpressionValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#sendNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSendNode(SysMLParser.SendNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#sendNodeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSendNodeDeclaration(SysMLParser.SendNodeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#senderReceiverPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSenderReceiverPart(SysMLParser.SenderReceiverPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#nodeParameterMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeParameterMember(SysMLParser.NodeParameterMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#nodeParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeParameter(SysMLParser.NodeParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureBinding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureBinding(SysMLParser.FeatureBindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#emptyParameterMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyParameterMember(SysMLParser.EmptyParameterMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#emptyUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyUsage(SysMLParser.EmptyUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#assignmentNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentNode(SysMLParser.AssignmentNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#assignmentNodeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentNodeDeclaration(SysMLParser.AssignmentNodeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#assignmentTargetMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentTargetMember(SysMLParser.AssignmentTargetMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#assignmentTargetParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentTargetParameter(SysMLParser.AssignmentTargetParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#assignmentTargetBinding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentTargetBinding(SysMLParser.AssignmentTargetBindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureChainMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureChainMember(SysMLParser.FeatureChainMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedFeatureChainMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedFeatureChainMember(SysMLParser.OwnedFeatureChainMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#terminateNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerminateNode(SysMLParser.TerminateNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ifNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfNode(SysMLParser.IfNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#expressionParameterMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionParameterMember(SysMLParser.ExpressionParameterMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionBodyParameterMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionBodyParameterMember(SysMLParser.ActionBodyParameterMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionBodyParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionBodyParameter(SysMLParser.ActionBodyParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ifNodeParameterMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfNodeParameterMember(SysMLParser.IfNodeParameterMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#whileLoopNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileLoopNode(SysMLParser.WhileLoopNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#forLoopNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForLoopNode(SysMLParser.ForLoopNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#forVariableDeclarationMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForVariableDeclarationMember(SysMLParser.ForVariableDeclarationMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#forVariableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForVariableDeclaration(SysMLParser.ForVariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actionTargetSuccession}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionTargetSuccession(SysMLParser.ActionTargetSuccessionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#targetSuccession}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetSuccession(SysMLParser.TargetSuccessionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#guardedTargetSuccession}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuardedTargetSuccession(SysMLParser.GuardedTargetSuccessionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#defaultTargetSuccession}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultTargetSuccession(SysMLParser.DefaultTargetSuccessionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#guardedSuccession}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuardedSuccession(SysMLParser.GuardedSuccessionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#stateDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateDefinition(SysMLParser.StateDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#stateDefBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateDefBody(SysMLParser.StateDefBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#stateBodyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateBodyItem(SysMLParser.StateBodyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#entryActionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntryActionMember(SysMLParser.EntryActionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#doActionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoActionMember(SysMLParser.DoActionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#exitActionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExitActionMember(SysMLParser.ExitActionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#entryTransitionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntryTransitionMember(SysMLParser.EntryTransitionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#stateActionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateActionUsage(SysMLParser.StateActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#emptyActionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyActionUsage(SysMLParser.EmptyActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#statePerformActionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatePerformActionUsage(SysMLParser.StatePerformActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#stateAcceptActionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateAcceptActionUsage(SysMLParser.StateAcceptActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#stateSendActionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateSendActionUsage(SysMLParser.StateSendActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#stateAssignmentActionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateAssignmentActionUsage(SysMLParser.StateAssignmentActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#transitionUsageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionUsageMember(SysMLParser.TransitionUsageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#targetTransitionUsageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetTransitionUsageMember(SysMLParser.TargetTransitionUsageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#stateUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateUsage(SysMLParser.StateUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#stateUsageBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateUsageBody(SysMLParser.StateUsageBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#exhibitStateUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExhibitStateUsage(SysMLParser.ExhibitStateUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#transitionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionUsage(SysMLParser.TransitionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#targetTransitionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetTransitionUsage(SysMLParser.TargetTransitionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#triggerActionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerActionMember(SysMLParser.TriggerActionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#triggerAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerAction(SysMLParser.TriggerActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#guardExpressionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGuardExpressionMember(SysMLParser.GuardExpressionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#effectBehaviorMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEffectBehaviorMember(SysMLParser.EffectBehaviorMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#effectBehaviorUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEffectBehaviorUsage(SysMLParser.EffectBehaviorUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#transitionPerformActionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionPerformActionUsage(SysMLParser.TransitionPerformActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#transitionAcceptActionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionAcceptActionUsage(SysMLParser.TransitionAcceptActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#transitionSendActionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionSendActionUsage(SysMLParser.TransitionSendActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#transitionAssignmentActionUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionAssignmentActionUsage(SysMLParser.TransitionAssignmentActionUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#transitionSuccessionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionSuccessionMember(SysMLParser.TransitionSuccessionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#transitionSuccession}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionSuccession(SysMLParser.TransitionSuccessionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#emptyEndMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyEndMember(SysMLParser.EmptyEndMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#emptyFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyFeature(SysMLParser.EmptyFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#calculationDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalculationDefinition(SysMLParser.CalculationDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#calculationUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalculationUsage(SysMLParser.CalculationUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#calculationUsageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalculationUsageDeclaration(SysMLParser.CalculationUsageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#calculationBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalculationBody(SysMLParser.CalculationBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#calculationBodyPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalculationBodyPart(SysMLParser.CalculationBodyPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#calculationBodyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalculationBodyItem(SysMLParser.CalculationBodyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#returnParameterMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnParameterMember(SysMLParser.ReturnParameterMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#resultExpressionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResultExpressionMember(SysMLParser.ResultExpressionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#constraintDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintDefinition(SysMLParser.ConstraintDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#constraintUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintUsage(SysMLParser.ConstraintUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#assertConstraintUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssertConstraintUsage(SysMLParser.AssertConstraintUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#constraintUsageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintUsageDeclaration(SysMLParser.ConstraintUsageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#requirementDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequirementDefinition(SysMLParser.RequirementDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#requirementBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequirementBody(SysMLParser.RequirementBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#requirementBodyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequirementBodyItem(SysMLParser.RequirementBodyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#subjectMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubjectMember(SysMLParser.SubjectMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#subjectUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubjectUsage(SysMLParser.SubjectUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#requirementConstraintMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequirementConstraintMember(SysMLParser.RequirementConstraintMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#requirementKind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequirementKind(SysMLParser.RequirementKindContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#requirementConstraintUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequirementConstraintUsage(SysMLParser.RequirementConstraintUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#framedConcernMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFramedConcernMember(SysMLParser.FramedConcernMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#framedConcernUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFramedConcernUsage(SysMLParser.FramedConcernUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actorMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActorMember(SysMLParser.ActorMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#actorUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActorUsage(SysMLParser.ActorUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#stakeholderMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStakeholderMember(SysMLParser.StakeholderMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#stakeholderUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStakeholderUsage(SysMLParser.StakeholderUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#requirementUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequirementUsage(SysMLParser.RequirementUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#satisfyRequirementUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSatisfyRequirementUsage(SysMLParser.SatisfyRequirementUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#satisfactionSubjectMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSatisfactionSubjectMember(SysMLParser.SatisfactionSubjectMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#satisfactionParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSatisfactionParameter(SysMLParser.SatisfactionParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#satisfactionFeatureValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSatisfactionFeatureValue(SysMLParser.SatisfactionFeatureValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#satisfactionReferenceExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSatisfactionReferenceExpression(SysMLParser.SatisfactionReferenceExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#concernDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcernDefinition(SysMLParser.ConcernDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#concernUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcernUsage(SysMLParser.ConcernUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#caseDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseDefinition(SysMLParser.CaseDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#caseUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseUsage(SysMLParser.CaseUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#caseBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseBody(SysMLParser.CaseBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#caseBodyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseBodyItem(SysMLParser.CaseBodyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#objectiveMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectiveMember(SysMLParser.ObjectiveMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#objectiveRequirementUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectiveRequirementUsage(SysMLParser.ObjectiveRequirementUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#analysisCaseDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalysisCaseDefinition(SysMLParser.AnalysisCaseDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#analysisCaseUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalysisCaseUsage(SysMLParser.AnalysisCaseUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#verificationCaseDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVerificationCaseDefinition(SysMLParser.VerificationCaseDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#verificationCaseUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVerificationCaseUsage(SysMLParser.VerificationCaseUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#requirementVerificationMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequirementVerificationMember(SysMLParser.RequirementVerificationMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#requirementVerificationUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequirementVerificationUsage(SysMLParser.RequirementVerificationUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#useCaseDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseCaseDefinition(SysMLParser.UseCaseDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#useCaseUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseCaseUsage(SysMLParser.UseCaseUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#includeUseCaseUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncludeUseCaseUsage(SysMLParser.IncludeUseCaseUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#viewDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewDefinition(SysMLParser.ViewDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#viewDefinitionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewDefinitionBody(SysMLParser.ViewDefinitionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#viewDefinitionBodyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewDefinitionBodyItem(SysMLParser.ViewDefinitionBodyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#viewRenderingMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewRenderingMember(SysMLParser.ViewRenderingMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#viewRenderingUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewRenderingUsage(SysMLParser.ViewRenderingUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#viewUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewUsage(SysMLParser.ViewUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#viewBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewBody(SysMLParser.ViewBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#viewBodyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewBodyItem(SysMLParser.ViewBodyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#expose}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpose(SysMLParser.ExposeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#membershipExpose}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembershipExpose(SysMLParser.MembershipExposeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#namespaceExpose}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceExpose(SysMLParser.NamespaceExposeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#viewpointDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewpointDefinition(SysMLParser.ViewpointDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#viewpointUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewpointUsage(SysMLParser.ViewpointUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#renderingDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenderingDefinition(SysMLParser.RenderingDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#renderingUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenderingUsage(SysMLParser.RenderingUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataDefinition(SysMLParser.MetadataDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#prefixMetadataAnnotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixMetadataAnnotation(SysMLParser.PrefixMetadataAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#prefixMetadataMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixMetadataMember(SysMLParser.PrefixMetadataMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#prefixMetadataUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixMetadataUsage(SysMLParser.PrefixMetadataUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataUsage(SysMLParser.MetadataUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataUsageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataUsageDeclaration(SysMLParser.MetadataUsageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataBody(SysMLParser.MetadataBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataBodyUsageMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataBodyUsageMember(SysMLParser.MetadataBodyUsageMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataBodyUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataBodyUsage(SysMLParser.MetadataBodyUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#extendedDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtendedDefinition(SysMLParser.ExtendedDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#extendedUsage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtendedUsage(SysMLParser.ExtendedUsageContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataFeature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataFeature(SysMLParser.MetadataFeatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataFeatureDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataFeatureDeclaration(SysMLParser.MetadataFeatureDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedExpressionReferenceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedExpressionReferenceMember(SysMLParser.OwnedExpressionReferenceMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedExpressionReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedExpressionReference(SysMLParser.OwnedExpressionReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedExpressionMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedExpressionMember(SysMLParser.OwnedExpressionMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#ownedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnedExpression(SysMLParser.OwnedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#conditionalBinaryOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalBinaryOperator(SysMLParser.ConditionalBinaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#binaryOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryOperator(SysMLParser.BinaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#unaryOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOperator(SysMLParser.UnaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#classificationTestOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassificationTestOperator(SysMLParser.ClassificationTestOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#castOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastOperator(SysMLParser.CastOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metaclassificationTestOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetaclassificationTestOperator(SysMLParser.MetaclassificationTestOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metacastOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetacastOperator(SysMLParser.MetacastOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#typeReferenceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeReferenceMember(SysMLParser.TypeReferenceMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#typeResultMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeResultMember(SysMLParser.TypeResultMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#typeReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeReference(SysMLParser.TypeReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#referenceTyping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceTyping(SysMLParser.ReferenceTypingContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#emptyResultMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyResultMember(SysMLParser.EmptyResultMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataArgumentMember(SysMLParser.MetadataArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataArgument(SysMLParser.MetadataArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataValue(SysMLParser.MetadataValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataReference(SysMLParser.MetadataReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(SysMLParser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#primaryArgumentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryArgumentValue(SysMLParser.PrimaryArgumentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#primaryArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryArgument(SysMLParser.PrimaryArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#primaryArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryArgumentMember(SysMLParser.PrimaryArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonFeatureChainPrimaryExpression(SysMLParser.NonFeatureChainPrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryArgumentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonFeatureChainPrimaryArgumentValue(SysMLParser.NonFeatureChainPrimaryArgumentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonFeatureChainPrimaryArgument(SysMLParser.NonFeatureChainPrimaryArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonFeatureChainPrimaryArgumentMember(SysMLParser.NonFeatureChainPrimaryArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#bracketExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketExpression(SysMLParser.BracketExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#indexExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexExpression(SysMLParser.IndexExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#sequenceExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceExpression(SysMLParser.SequenceExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#sequenceExpressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceExpressionList(SysMLParser.SequenceExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#sequenceOperatorExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceOperatorExpression(SysMLParser.SequenceOperatorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#sequenceExpressionListMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceExpressionListMember(SysMLParser.SequenceExpressionListMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureChainExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureChainExpression(SysMLParser.FeatureChainExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#collectExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectExpression(SysMLParser.CollectExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#selectExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectExpression(SysMLParser.SelectExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#functionOperationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionOperationExpression(SysMLParser.FunctionOperationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#bodyArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyArgumentMember(SysMLParser.BodyArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#bodyArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyArgument(SysMLParser.BodyArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#bodyArgumentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyArgumentValue(SysMLParser.BodyArgumentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#functionReferenceArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReferenceArgumentMember(SysMLParser.FunctionReferenceArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#functionReferenceArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReferenceArgument(SysMLParser.FunctionReferenceArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#functionReferenceArgumentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReferenceArgumentValue(SysMLParser.FunctionReferenceArgumentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#functionReferenceExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReferenceExpression(SysMLParser.FunctionReferenceExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#functionReferenceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReferenceMember(SysMLParser.FunctionReferenceMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#functionReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReference(SysMLParser.FunctionReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#invocationTypeMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvocationTypeMember(SysMLParser.InvocationTypeMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#invocationType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvocationType(SysMLParser.InvocationTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#baseExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseExpression(SysMLParser.BaseExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#nullExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullExpression(SysMLParser.NullExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureReferenceExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureReferenceExpression(SysMLParser.FeatureReferenceExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureReferenceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureReferenceMember(SysMLParser.FeatureReferenceMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#featureReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatureReference(SysMLParser.FeatureReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#metadataAccessExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetadataAccessExpression(SysMLParser.MetadataAccessExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#elementReferenceMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementReferenceMember(SysMLParser.ElementReferenceMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#invocationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvocationExpression(SysMLParser.InvocationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#constructorExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorExpression(SysMLParser.ConstructorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#constructorResultMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorResultMember(SysMLParser.ConstructorResultMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#constructorResult}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorResult(SysMLParser.ConstructorResultContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#instantiatedTypeMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstantiatedTypeMember(SysMLParser.InstantiatedTypeMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#instantiatedTypeReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstantiatedTypeReference(SysMLParser.InstantiatedTypeReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(SysMLParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#positionalArgumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositionalArgumentList(SysMLParser.PositionalArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#namedArgumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedArgumentList(SysMLParser.NamedArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#namedArgumentMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedArgumentMember(SysMLParser.NamedArgumentMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#namedArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedArgument(SysMLParser.NamedArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#parameterRedefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterRedefinition(SysMLParser.ParameterRedefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#bodyExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBodyExpression(SysMLParser.BodyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#expressionBodyMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBodyMember(SysMLParser.ExpressionBodyMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#expressionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionBody(SysMLParser.ExpressionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpression(SysMLParser.LiteralExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#literalBoolean}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralBoolean(SysMLParser.LiteralBooleanContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#booleanValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanValue(SysMLParser.BooleanValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#literalString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralString(SysMLParser.LiteralStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#literalInteger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralInteger(SysMLParser.LiteralIntegerContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#literalReal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralReal(SysMLParser.LiteralRealContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#realValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealValue(SysMLParser.RealValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SysMLParser#literalInfinity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralInfinity(SysMLParser.LiteralInfinityContext ctx);
}