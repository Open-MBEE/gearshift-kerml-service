// Generated from /Users/chasgaley/git/gearshift-kerml-service/gearshift-kerml-runtime/src/main/antlr/SysML.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SysMLParser}.
 */
public interface SysMLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SysMLParser#definedByToken}.
	 * @param ctx the parse tree
	 */
	void enterDefinedByToken(SysMLParser.DefinedByTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#definedByToken}.
	 * @param ctx the parse tree
	 */
	void exitDefinedByToken(SysMLParser.DefinedByTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#specializesToken}.
	 * @param ctx the parse tree
	 */
	void enterSpecializesToken(SysMLParser.SpecializesTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#specializesToken}.
	 * @param ctx the parse tree
	 */
	void exitSpecializesToken(SysMLParser.SpecializesTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#subsetsToken}.
	 * @param ctx the parse tree
	 */
	void enterSubsetsToken(SysMLParser.SubsetsTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#subsetsToken}.
	 * @param ctx the parse tree
	 */
	void exitSubsetsToken(SysMLParser.SubsetsTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#referencesToken}.
	 * @param ctx the parse tree
	 */
	void enterReferencesToken(SysMLParser.ReferencesTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#referencesToken}.
	 * @param ctx the parse tree
	 */
	void exitReferencesToken(SysMLParser.ReferencesTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#crossesToken}.
	 * @param ctx the parse tree
	 */
	void enterCrossesToken(SysMLParser.CrossesTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#crossesToken}.
	 * @param ctx the parse tree
	 */
	void exitCrossesToken(SysMLParser.CrossesTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#redefinesToken}.
	 * @param ctx the parse tree
	 */
	void enterRedefinesToken(SysMLParser.RedefinesTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#redefinesToken}.
	 * @param ctx the parse tree
	 */
	void exitRedefinesToken(SysMLParser.RedefinesTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(SysMLParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(SysMLParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#identification}.
	 * @param ctx the parse tree
	 */
	void enterIdentification(SysMLParser.IdentificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#identification}.
	 * @param ctx the parse tree
	 */
	void exitIdentification(SysMLParser.IdentificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#relationshipBody}.
	 * @param ctx the parse tree
	 */
	void enterRelationshipBody(SysMLParser.RelationshipBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#relationshipBody}.
	 * @param ctx the parse tree
	 */
	void exitRelationshipBody(SysMLParser.RelationshipBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#dependency}.
	 * @param ctx the parse tree
	 */
	void enterDependency(SysMLParser.DependencyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#dependency}.
	 * @param ctx the parse tree
	 */
	void exitDependency(SysMLParser.DependencyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#dependencyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterDependencyDeclaration(SysMLParser.DependencyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#dependencyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitDependencyDeclaration(SysMLParser.DependencyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(SysMLParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(SysMLParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterOwnedAnnotation(SysMLParser.OwnedAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitOwnedAnnotation(SysMLParser.OwnedAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#annotatingMember}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatingMember(SysMLParser.AnnotatingMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#annotatingMember}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatingMember(SysMLParser.AnnotatingMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#annotatingElement}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatingElement(SysMLParser.AnnotatingElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#annotatingElement}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatingElement(SysMLParser.AnnotatingElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(SysMLParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(SysMLParser.CommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#documentation}.
	 * @param ctx the parse tree
	 */
	void enterDocumentation(SysMLParser.DocumentationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#documentation}.
	 * @param ctx the parse tree
	 */
	void exitDocumentation(SysMLParser.DocumentationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#textualRepresentation}.
	 * @param ctx the parse tree
	 */
	void enterTextualRepresentation(SysMLParser.TextualRepresentationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#textualRepresentation}.
	 * @param ctx the parse tree
	 */
	void exitTextualRepresentation(SysMLParser.TextualRepresentationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#rootNamespace}.
	 * @param ctx the parse tree
	 */
	void enterRootNamespace(SysMLParser.RootNamespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#rootNamespace}.
	 * @param ctx the parse tree
	 */
	void exitRootNamespace(SysMLParser.RootNamespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#package}.
	 * @param ctx the parse tree
	 */
	void enterPackage(SysMLParser.PackageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#package}.
	 * @param ctx the parse tree
	 */
	void exitPackage(SysMLParser.PackageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#libraryPackage}.
	 * @param ctx the parse tree
	 */
	void enterLibraryPackage(SysMLParser.LibraryPackageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#libraryPackage}.
	 * @param ctx the parse tree
	 */
	void exitLibraryPackage(SysMLParser.LibraryPackageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPackageDeclaration(SysMLParser.PackageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPackageDeclaration(SysMLParser.PackageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#packageBody}.
	 * @param ctx the parse tree
	 */
	void enterPackageBody(SysMLParser.PackageBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#packageBody}.
	 * @param ctx the parse tree
	 */
	void exitPackageBody(SysMLParser.PackageBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#packageBodyElement}.
	 * @param ctx the parse tree
	 */
	void enterPackageBodyElement(SysMLParser.PackageBodyElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#packageBodyElement}.
	 * @param ctx the parse tree
	 */
	void exitPackageBodyElement(SysMLParser.PackageBodyElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#visibility}.
	 * @param ctx the parse tree
	 */
	void enterVisibility(SysMLParser.VisibilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#visibility}.
	 * @param ctx the parse tree
	 */
	void exitVisibility(SysMLParser.VisibilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#memberPrefix}.
	 * @param ctx the parse tree
	 */
	void enterMemberPrefix(SysMLParser.MemberPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#memberPrefix}.
	 * @param ctx the parse tree
	 */
	void exitMemberPrefix(SysMLParser.MemberPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#packageMember}.
	 * @param ctx the parse tree
	 */
	void enterPackageMember(SysMLParser.PackageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#packageMember}.
	 * @param ctx the parse tree
	 */
	void exitPackageMember(SysMLParser.PackageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#elementFilterMember}.
	 * @param ctx the parse tree
	 */
	void enterElementFilterMember(SysMLParser.ElementFilterMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#elementFilterMember}.
	 * @param ctx the parse tree
	 */
	void exitElementFilterMember(SysMLParser.ElementFilterMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#aliasMember}.
	 * @param ctx the parse tree
	 */
	void enterAliasMember(SysMLParser.AliasMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#aliasMember}.
	 * @param ctx the parse tree
	 */
	void exitAliasMember(SysMLParser.AliasMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#import_}.
	 * @param ctx the parse tree
	 */
	void enterImport_(SysMLParser.Import_Context ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#import_}.
	 * @param ctx the parse tree
	 */
	void exitImport_(SysMLParser.Import_Context ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImportDeclaration(SysMLParser.ImportDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImportDeclaration(SysMLParser.ImportDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#membershipImport}.
	 * @param ctx the parse tree
	 */
	void enterMembershipImport(SysMLParser.MembershipImportContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#membershipImport}.
	 * @param ctx the parse tree
	 */
	void exitMembershipImport(SysMLParser.MembershipImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#namespaceImport}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceImport(SysMLParser.NamespaceImportContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#namespaceImport}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceImport(SysMLParser.NamespaceImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#filterPackage}.
	 * @param ctx the parse tree
	 */
	void enterFilterPackage(SysMLParser.FilterPackageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#filterPackage}.
	 * @param ctx the parse tree
	 */
	void exitFilterPackage(SysMLParser.FilterPackageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#filterPackageMember}.
	 * @param ctx the parse tree
	 */
	void enterFilterPackageMember(SysMLParser.FilterPackageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#filterPackageMember}.
	 * @param ctx the parse tree
	 */
	void exitFilterPackageMember(SysMLParser.FilterPackageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#visibilityIndicator}.
	 * @param ctx the parse tree
	 */
	void enterVisibilityIndicator(SysMLParser.VisibilityIndicatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#visibilityIndicator}.
	 * @param ctx the parse tree
	 */
	void exitVisibilityIndicator(SysMLParser.VisibilityIndicatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#definitionElement}.
	 * @param ctx the parse tree
	 */
	void enterDefinitionElement(SysMLParser.DefinitionElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#definitionElement}.
	 * @param ctx the parse tree
	 */
	void exitDefinitionElement(SysMLParser.DefinitionElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#usageElement}.
	 * @param ctx the parse tree
	 */
	void enterUsageElement(SysMLParser.UsageElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#usageElement}.
	 * @param ctx the parse tree
	 */
	void exitUsageElement(SysMLParser.UsageElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#basicDefinitionPrefix}.
	 * @param ctx the parse tree
	 */
	void enterBasicDefinitionPrefix(SysMLParser.BasicDefinitionPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#basicDefinitionPrefix}.
	 * @param ctx the parse tree
	 */
	void exitBasicDefinitionPrefix(SysMLParser.BasicDefinitionPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#definitionExtensionKeyword}.
	 * @param ctx the parse tree
	 */
	void enterDefinitionExtensionKeyword(SysMLParser.DefinitionExtensionKeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#definitionExtensionKeyword}.
	 * @param ctx the parse tree
	 */
	void exitDefinitionExtensionKeyword(SysMLParser.DefinitionExtensionKeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#definitionPrefix}.
	 * @param ctx the parse tree
	 */
	void enterDefinitionPrefix(SysMLParser.DefinitionPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#definitionPrefix}.
	 * @param ctx the parse tree
	 */
	void exitDefinitionPrefix(SysMLParser.DefinitionPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(SysMLParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(SysMLParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#definitionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterDefinitionDeclaration(SysMLParser.DefinitionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#definitionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitDefinitionDeclaration(SysMLParser.DefinitionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#definitionBody}.
	 * @param ctx the parse tree
	 */
	void enterDefinitionBody(SysMLParser.DefinitionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#definitionBody}.
	 * @param ctx the parse tree
	 */
	void exitDefinitionBody(SysMLParser.DefinitionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#definitionBodyItem}.
	 * @param ctx the parse tree
	 */
	void enterDefinitionBodyItem(SysMLParser.DefinitionBodyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#definitionBodyItem}.
	 * @param ctx the parse tree
	 */
	void exitDefinitionBodyItem(SysMLParser.DefinitionBodyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#definitionMember}.
	 * @param ctx the parse tree
	 */
	void enterDefinitionMember(SysMLParser.DefinitionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#definitionMember}.
	 * @param ctx the parse tree
	 */
	void exitDefinitionMember(SysMLParser.DefinitionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#variantUsageMember}.
	 * @param ctx the parse tree
	 */
	void enterVariantUsageMember(SysMLParser.VariantUsageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#variantUsageMember}.
	 * @param ctx the parse tree
	 */
	void exitVariantUsageMember(SysMLParser.VariantUsageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#nonOccurrenceUsageMember}.
	 * @param ctx the parse tree
	 */
	void enterNonOccurrenceUsageMember(SysMLParser.NonOccurrenceUsageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#nonOccurrenceUsageMember}.
	 * @param ctx the parse tree
	 */
	void exitNonOccurrenceUsageMember(SysMLParser.NonOccurrenceUsageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#occurrenceUsageMember}.
	 * @param ctx the parse tree
	 */
	void enterOccurrenceUsageMember(SysMLParser.OccurrenceUsageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#occurrenceUsageMember}.
	 * @param ctx the parse tree
	 */
	void exitOccurrenceUsageMember(SysMLParser.OccurrenceUsageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#structureUsageMember}.
	 * @param ctx the parse tree
	 */
	void enterStructureUsageMember(SysMLParser.StructureUsageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#structureUsageMember}.
	 * @param ctx the parse tree
	 */
	void exitStructureUsageMember(SysMLParser.StructureUsageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#behaviorUsageMember}.
	 * @param ctx the parse tree
	 */
	void enterBehaviorUsageMember(SysMLParser.BehaviorUsageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#behaviorUsageMember}.
	 * @param ctx the parse tree
	 */
	void exitBehaviorUsageMember(SysMLParser.BehaviorUsageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureDirection}.
	 * @param ctx the parse tree
	 */
	void enterFeatureDirection(SysMLParser.FeatureDirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureDirection}.
	 * @param ctx the parse tree
	 */
	void exitFeatureDirection(SysMLParser.FeatureDirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#refPrefix}.
	 * @param ctx the parse tree
	 */
	void enterRefPrefix(SysMLParser.RefPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#refPrefix}.
	 * @param ctx the parse tree
	 */
	void exitRefPrefix(SysMLParser.RefPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#basicUsagePrefix}.
	 * @param ctx the parse tree
	 */
	void enterBasicUsagePrefix(SysMLParser.BasicUsagePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#basicUsagePrefix}.
	 * @param ctx the parse tree
	 */
	void exitBasicUsagePrefix(SysMLParser.BasicUsagePrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#endUsagePrefix}.
	 * @param ctx the parse tree
	 */
	void enterEndUsagePrefix(SysMLParser.EndUsagePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#endUsagePrefix}.
	 * @param ctx the parse tree
	 */
	void exitEndUsagePrefix(SysMLParser.EndUsagePrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedCrossFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterOwnedCrossFeatureMember(SysMLParser.OwnedCrossFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedCrossFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitOwnedCrossFeatureMember(SysMLParser.OwnedCrossFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedCrossFeature}.
	 * @param ctx the parse tree
	 */
	void enterOwnedCrossFeature(SysMLParser.OwnedCrossFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedCrossFeature}.
	 * @param ctx the parse tree
	 */
	void exitOwnedCrossFeature(SysMLParser.OwnedCrossFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#usageExtensionKeyword}.
	 * @param ctx the parse tree
	 */
	void enterUsageExtensionKeyword(SysMLParser.UsageExtensionKeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#usageExtensionKeyword}.
	 * @param ctx the parse tree
	 */
	void exitUsageExtensionKeyword(SysMLParser.UsageExtensionKeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#unextendedUsagePrefix}.
	 * @param ctx the parse tree
	 */
	void enterUnextendedUsagePrefix(SysMLParser.UnextendedUsagePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#unextendedUsagePrefix}.
	 * @param ctx the parse tree
	 */
	void exitUnextendedUsagePrefix(SysMLParser.UnextendedUsagePrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#usagePrefix}.
	 * @param ctx the parse tree
	 */
	void enterUsagePrefix(SysMLParser.UsagePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#usagePrefix}.
	 * @param ctx the parse tree
	 */
	void exitUsagePrefix(SysMLParser.UsagePrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#usage}.
	 * @param ctx the parse tree
	 */
	void enterUsage(SysMLParser.UsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#usage}.
	 * @param ctx the parse tree
	 */
	void exitUsage(SysMLParser.UsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#usageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterUsageDeclaration(SysMLParser.UsageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#usageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitUsageDeclaration(SysMLParser.UsageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#usageCompletion}.
	 * @param ctx the parse tree
	 */
	void enterUsageCompletion(SysMLParser.UsageCompletionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#usageCompletion}.
	 * @param ctx the parse tree
	 */
	void exitUsageCompletion(SysMLParser.UsageCompletionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#usageBody}.
	 * @param ctx the parse tree
	 */
	void enterUsageBody(SysMLParser.UsageBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#usageBody}.
	 * @param ctx the parse tree
	 */
	void exitUsageBody(SysMLParser.UsageBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#valuePart}.
	 * @param ctx the parse tree
	 */
	void enterValuePart(SysMLParser.ValuePartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#valuePart}.
	 * @param ctx the parse tree
	 */
	void exitValuePart(SysMLParser.ValuePartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureValue}.
	 * @param ctx the parse tree
	 */
	void enterFeatureValue(SysMLParser.FeatureValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureValue}.
	 * @param ctx the parse tree
	 */
	void exitFeatureValue(SysMLParser.FeatureValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#defaultReferenceUsage}.
	 * @param ctx the parse tree
	 */
	void enterDefaultReferenceUsage(SysMLParser.DefaultReferenceUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#defaultReferenceUsage}.
	 * @param ctx the parse tree
	 */
	void exitDefaultReferenceUsage(SysMLParser.DefaultReferenceUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#referenceUsage}.
	 * @param ctx the parse tree
	 */
	void enterReferenceUsage(SysMLParser.ReferenceUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#referenceUsage}.
	 * @param ctx the parse tree
	 */
	void exitReferenceUsage(SysMLParser.ReferenceUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#variantReference}.
	 * @param ctx the parse tree
	 */
	void enterVariantReference(SysMLParser.VariantReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#variantReference}.
	 * @param ctx the parse tree
	 */
	void exitVariantReference(SysMLParser.VariantReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#nonOccurrenceUsageElement}.
	 * @param ctx the parse tree
	 */
	void enterNonOccurrenceUsageElement(SysMLParser.NonOccurrenceUsageElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#nonOccurrenceUsageElement}.
	 * @param ctx the parse tree
	 */
	void exitNonOccurrenceUsageElement(SysMLParser.NonOccurrenceUsageElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#occurrenceUsageElement}.
	 * @param ctx the parse tree
	 */
	void enterOccurrenceUsageElement(SysMLParser.OccurrenceUsageElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#occurrenceUsageElement}.
	 * @param ctx the parse tree
	 */
	void exitOccurrenceUsageElement(SysMLParser.OccurrenceUsageElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#structureUsageElement}.
	 * @param ctx the parse tree
	 */
	void enterStructureUsageElement(SysMLParser.StructureUsageElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#structureUsageElement}.
	 * @param ctx the parse tree
	 */
	void exitStructureUsageElement(SysMLParser.StructureUsageElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#behaviorUsageElement}.
	 * @param ctx the parse tree
	 */
	void enterBehaviorUsageElement(SysMLParser.BehaviorUsageElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#behaviorUsageElement}.
	 * @param ctx the parse tree
	 */
	void exitBehaviorUsageElement(SysMLParser.BehaviorUsageElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#variantUsageElement}.
	 * @param ctx the parse tree
	 */
	void enterVariantUsageElement(SysMLParser.VariantUsageElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#variantUsageElement}.
	 * @param ctx the parse tree
	 */
	void exitVariantUsageElement(SysMLParser.VariantUsageElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#subclassificationPart}.
	 * @param ctx the parse tree
	 */
	void enterSubclassificationPart(SysMLParser.SubclassificationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#subclassificationPart}.
	 * @param ctx the parse tree
	 */
	void exitSubclassificationPart(SysMLParser.SubclassificationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedSubclassification}.
	 * @param ctx the parse tree
	 */
	void enterOwnedSubclassification(SysMLParser.OwnedSubclassificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedSubclassification}.
	 * @param ctx the parse tree
	 */
	void exitOwnedSubclassification(SysMLParser.OwnedSubclassificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureSpecializationPart}.
	 * @param ctx the parse tree
	 */
	void enterFeatureSpecializationPart(SysMLParser.FeatureSpecializationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureSpecializationPart}.
	 * @param ctx the parse tree
	 */
	void exitFeatureSpecializationPart(SysMLParser.FeatureSpecializationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureSpecialization}.
	 * @param ctx the parse tree
	 */
	void enterFeatureSpecialization(SysMLParser.FeatureSpecializationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureSpecialization}.
	 * @param ctx the parse tree
	 */
	void exitFeatureSpecialization(SysMLParser.FeatureSpecializationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#typings}.
	 * @param ctx the parse tree
	 */
	void enterTypings(SysMLParser.TypingsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#typings}.
	 * @param ctx the parse tree
	 */
	void exitTypings(SysMLParser.TypingsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#typedBy}.
	 * @param ctx the parse tree
	 */
	void enterTypedBy(SysMLParser.TypedByContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#typedBy}.
	 * @param ctx the parse tree
	 */
	void exitTypedBy(SysMLParser.TypedByContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureTyping}.
	 * @param ctx the parse tree
	 */
	void enterFeatureTyping(SysMLParser.FeatureTypingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureTyping}.
	 * @param ctx the parse tree
	 */
	void exitFeatureTyping(SysMLParser.FeatureTypingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedFeatureTyping}.
	 * @param ctx the parse tree
	 */
	void enterOwnedFeatureTyping(SysMLParser.OwnedFeatureTypingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedFeatureTyping}.
	 * @param ctx the parse tree
	 */
	void exitOwnedFeatureTyping(SysMLParser.OwnedFeatureTypingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#subsettings}.
	 * @param ctx the parse tree
	 */
	void enterSubsettings(SysMLParser.SubsettingsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#subsettings}.
	 * @param ctx the parse tree
	 */
	void exitSubsettings(SysMLParser.SubsettingsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#subsets}.
	 * @param ctx the parse tree
	 */
	void enterSubsets(SysMLParser.SubsetsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#subsets}.
	 * @param ctx the parse tree
	 */
	void exitSubsets(SysMLParser.SubsetsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedSubsetting}.
	 * @param ctx the parse tree
	 */
	void enterOwnedSubsetting(SysMLParser.OwnedSubsettingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedSubsetting}.
	 * @param ctx the parse tree
	 */
	void exitOwnedSubsetting(SysMLParser.OwnedSubsettingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#references}.
	 * @param ctx the parse tree
	 */
	void enterReferences(SysMLParser.ReferencesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#references}.
	 * @param ctx the parse tree
	 */
	void exitReferences(SysMLParser.ReferencesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedReferenceSubsetting}.
	 * @param ctx the parse tree
	 */
	void enterOwnedReferenceSubsetting(SysMLParser.OwnedReferenceSubsettingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedReferenceSubsetting}.
	 * @param ctx the parse tree
	 */
	void exitOwnedReferenceSubsetting(SysMLParser.OwnedReferenceSubsettingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#crosses}.
	 * @param ctx the parse tree
	 */
	void enterCrosses(SysMLParser.CrossesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#crosses}.
	 * @param ctx the parse tree
	 */
	void exitCrosses(SysMLParser.CrossesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedCrossSubsetting}.
	 * @param ctx the parse tree
	 */
	void enterOwnedCrossSubsetting(SysMLParser.OwnedCrossSubsettingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedCrossSubsetting}.
	 * @param ctx the parse tree
	 */
	void exitOwnedCrossSubsetting(SysMLParser.OwnedCrossSubsettingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#redefinitions}.
	 * @param ctx the parse tree
	 */
	void enterRedefinitions(SysMLParser.RedefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#redefinitions}.
	 * @param ctx the parse tree
	 */
	void exitRedefinitions(SysMLParser.RedefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#redefines}.
	 * @param ctx the parse tree
	 */
	void enterRedefines(SysMLParser.RedefinesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#redefines}.
	 * @param ctx the parse tree
	 */
	void exitRedefines(SysMLParser.RedefinesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedRedefinition}.
	 * @param ctx the parse tree
	 */
	void enterOwnedRedefinition(SysMLParser.OwnedRedefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedRedefinition}.
	 * @param ctx the parse tree
	 */
	void exitOwnedRedefinition(SysMLParser.OwnedRedefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedFeatureChain}.
	 * @param ctx the parse tree
	 */
	void enterOwnedFeatureChain(SysMLParser.OwnedFeatureChainContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedFeatureChain}.
	 * @param ctx the parse tree
	 */
	void exitOwnedFeatureChain(SysMLParser.OwnedFeatureChainContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedFeatureChaining}.
	 * @param ctx the parse tree
	 */
	void enterOwnedFeatureChaining(SysMLParser.OwnedFeatureChainingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedFeatureChaining}.
	 * @param ctx the parse tree
	 */
	void exitOwnedFeatureChaining(SysMLParser.OwnedFeatureChainingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#multiplicityPart}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicityPart(SysMLParser.MultiplicityPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#multiplicityPart}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicityPart(SysMLParser.MultiplicityPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedMultiplicity}.
	 * @param ctx the parse tree
	 */
	void enterOwnedMultiplicity(SysMLParser.OwnedMultiplicityContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedMultiplicity}.
	 * @param ctx the parse tree
	 */
	void exitOwnedMultiplicity(SysMLParser.OwnedMultiplicityContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#multiplicityRange}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicityRange(SysMLParser.MultiplicityRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#multiplicityRange}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicityRange(SysMLParser.MultiplicityRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#multiplicityExpressionMember}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicityExpressionMember(SysMLParser.MultiplicityExpressionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#multiplicityExpressionMember}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicityExpressionMember(SysMLParser.MultiplicityExpressionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#attributeDefinition}.
	 * @param ctx the parse tree
	 */
	void enterAttributeDefinition(SysMLParser.AttributeDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#attributeDefinition}.
	 * @param ctx the parse tree
	 */
	void exitAttributeDefinition(SysMLParser.AttributeDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#attributeUsage}.
	 * @param ctx the parse tree
	 */
	void enterAttributeUsage(SysMLParser.AttributeUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#attributeUsage}.
	 * @param ctx the parse tree
	 */
	void exitAttributeUsage(SysMLParser.AttributeUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#enumerationDefinition}.
	 * @param ctx the parse tree
	 */
	void enterEnumerationDefinition(SysMLParser.EnumerationDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#enumerationDefinition}.
	 * @param ctx the parse tree
	 */
	void exitEnumerationDefinition(SysMLParser.EnumerationDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#enumerationBody}.
	 * @param ctx the parse tree
	 */
	void enterEnumerationBody(SysMLParser.EnumerationBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#enumerationBody}.
	 * @param ctx the parse tree
	 */
	void exitEnumerationBody(SysMLParser.EnumerationBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#enumerationUsageMember}.
	 * @param ctx the parse tree
	 */
	void enterEnumerationUsageMember(SysMLParser.EnumerationUsageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#enumerationUsageMember}.
	 * @param ctx the parse tree
	 */
	void exitEnumerationUsageMember(SysMLParser.EnumerationUsageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#enumeratedValue}.
	 * @param ctx the parse tree
	 */
	void enterEnumeratedValue(SysMLParser.EnumeratedValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#enumeratedValue}.
	 * @param ctx the parse tree
	 */
	void exitEnumeratedValue(SysMLParser.EnumeratedValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#enumerationUsage}.
	 * @param ctx the parse tree
	 */
	void enterEnumerationUsage(SysMLParser.EnumerationUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#enumerationUsage}.
	 * @param ctx the parse tree
	 */
	void exitEnumerationUsage(SysMLParser.EnumerationUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#occurrenceDefinitionPrefix}.
	 * @param ctx the parse tree
	 */
	void enterOccurrenceDefinitionPrefix(SysMLParser.OccurrenceDefinitionPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#occurrenceDefinitionPrefix}.
	 * @param ctx the parse tree
	 */
	void exitOccurrenceDefinitionPrefix(SysMLParser.OccurrenceDefinitionPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#occurrenceDefinition}.
	 * @param ctx the parse tree
	 */
	void enterOccurrenceDefinition(SysMLParser.OccurrenceDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#occurrenceDefinition}.
	 * @param ctx the parse tree
	 */
	void exitOccurrenceDefinition(SysMLParser.OccurrenceDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#individualDefinition}.
	 * @param ctx the parse tree
	 */
	void enterIndividualDefinition(SysMLParser.IndividualDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#individualDefinition}.
	 * @param ctx the parse tree
	 */
	void exitIndividualDefinition(SysMLParser.IndividualDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#emptyMultiplicityMember}.
	 * @param ctx the parse tree
	 */
	void enterEmptyMultiplicityMember(SysMLParser.EmptyMultiplicityMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#emptyMultiplicityMember}.
	 * @param ctx the parse tree
	 */
	void exitEmptyMultiplicityMember(SysMLParser.EmptyMultiplicityMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#emptyMultiplicity}.
	 * @param ctx the parse tree
	 */
	void enterEmptyMultiplicity(SysMLParser.EmptyMultiplicityContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#emptyMultiplicity}.
	 * @param ctx the parse tree
	 */
	void exitEmptyMultiplicity(SysMLParser.EmptyMultiplicityContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#occurrenceUsagePrefix}.
	 * @param ctx the parse tree
	 */
	void enterOccurrenceUsagePrefix(SysMLParser.OccurrenceUsagePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#occurrenceUsagePrefix}.
	 * @param ctx the parse tree
	 */
	void exitOccurrenceUsagePrefix(SysMLParser.OccurrenceUsagePrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#occurrenceUsage}.
	 * @param ctx the parse tree
	 */
	void enterOccurrenceUsage(SysMLParser.OccurrenceUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#occurrenceUsage}.
	 * @param ctx the parse tree
	 */
	void exitOccurrenceUsage(SysMLParser.OccurrenceUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#individualUsage}.
	 * @param ctx the parse tree
	 */
	void enterIndividualUsage(SysMLParser.IndividualUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#individualUsage}.
	 * @param ctx the parse tree
	 */
	void exitIndividualUsage(SysMLParser.IndividualUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#portionUsage}.
	 * @param ctx the parse tree
	 */
	void enterPortionUsage(SysMLParser.PortionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#portionUsage}.
	 * @param ctx the parse tree
	 */
	void exitPortionUsage(SysMLParser.PortionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#portionKindToken}.
	 * @param ctx the parse tree
	 */
	void enterPortionKindToken(SysMLParser.PortionKindTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#portionKindToken}.
	 * @param ctx the parse tree
	 */
	void exitPortionKindToken(SysMLParser.PortionKindTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#eventOccurrenceUsage}.
	 * @param ctx the parse tree
	 */
	void enterEventOccurrenceUsage(SysMLParser.EventOccurrenceUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#eventOccurrenceUsage}.
	 * @param ctx the parse tree
	 */
	void exitEventOccurrenceUsage(SysMLParser.EventOccurrenceUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#sourceSuccessionMember}.
	 * @param ctx the parse tree
	 */
	void enterSourceSuccessionMember(SysMLParser.SourceSuccessionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#sourceSuccessionMember}.
	 * @param ctx the parse tree
	 */
	void exitSourceSuccessionMember(SysMLParser.SourceSuccessionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#sourceSuccession}.
	 * @param ctx the parse tree
	 */
	void enterSourceSuccession(SysMLParser.SourceSuccessionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#sourceSuccession}.
	 * @param ctx the parse tree
	 */
	void exitSourceSuccession(SysMLParser.SourceSuccessionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#sourceEndMember}.
	 * @param ctx the parse tree
	 */
	void enterSourceEndMember(SysMLParser.SourceEndMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#sourceEndMember}.
	 * @param ctx the parse tree
	 */
	void exitSourceEndMember(SysMLParser.SourceEndMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#sourceEnd}.
	 * @param ctx the parse tree
	 */
	void enterSourceEnd(SysMLParser.SourceEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#sourceEnd}.
	 * @param ctx the parse tree
	 */
	void exitSourceEnd(SysMLParser.SourceEndContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#itemDefinition}.
	 * @param ctx the parse tree
	 */
	void enterItemDefinition(SysMLParser.ItemDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#itemDefinition}.
	 * @param ctx the parse tree
	 */
	void exitItemDefinition(SysMLParser.ItemDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#itemUsage}.
	 * @param ctx the parse tree
	 */
	void enterItemUsage(SysMLParser.ItemUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#itemUsage}.
	 * @param ctx the parse tree
	 */
	void exitItemUsage(SysMLParser.ItemUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#partDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartDefinition(SysMLParser.PartDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#partDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartDefinition(SysMLParser.PartDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#partUsage}.
	 * @param ctx the parse tree
	 */
	void enterPartUsage(SysMLParser.PartUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#partUsage}.
	 * @param ctx the parse tree
	 */
	void exitPartUsage(SysMLParser.PartUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#portDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPortDefinition(SysMLParser.PortDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#portDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPortDefinition(SysMLParser.PortDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#conjugatedPortDefinitionMember}.
	 * @param ctx the parse tree
	 */
	void enterConjugatedPortDefinitionMember(SysMLParser.ConjugatedPortDefinitionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#conjugatedPortDefinitionMember}.
	 * @param ctx the parse tree
	 */
	void exitConjugatedPortDefinitionMember(SysMLParser.ConjugatedPortDefinitionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#conjugatedPortDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConjugatedPortDefinition(SysMLParser.ConjugatedPortDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#conjugatedPortDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConjugatedPortDefinition(SysMLParser.ConjugatedPortDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#portConjugation}.
	 * @param ctx the parse tree
	 */
	void enterPortConjugation(SysMLParser.PortConjugationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#portConjugation}.
	 * @param ctx the parse tree
	 */
	void exitPortConjugation(SysMLParser.PortConjugationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#portUsage}.
	 * @param ctx the parse tree
	 */
	void enterPortUsage(SysMLParser.PortUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#portUsage}.
	 * @param ctx the parse tree
	 */
	void exitPortUsage(SysMLParser.PortUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#conjugatedPortTyping}.
	 * @param ctx the parse tree
	 */
	void enterConjugatedPortTyping(SysMLParser.ConjugatedPortTypingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#conjugatedPortTyping}.
	 * @param ctx the parse tree
	 */
	void exitConjugatedPortTyping(SysMLParser.ConjugatedPortTypingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#connectionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConnectionDefinition(SysMLParser.ConnectionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#connectionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConnectionDefinition(SysMLParser.ConnectionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#connectionUsage}.
	 * @param ctx the parse tree
	 */
	void enterConnectionUsage(SysMLParser.ConnectionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#connectionUsage}.
	 * @param ctx the parse tree
	 */
	void exitConnectionUsage(SysMLParser.ConnectionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#connectorPart}.
	 * @param ctx the parse tree
	 */
	void enterConnectorPart(SysMLParser.ConnectorPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#connectorPart}.
	 * @param ctx the parse tree
	 */
	void exitConnectorPart(SysMLParser.ConnectorPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#binaryConnectorPart}.
	 * @param ctx the parse tree
	 */
	void enterBinaryConnectorPart(SysMLParser.BinaryConnectorPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#binaryConnectorPart}.
	 * @param ctx the parse tree
	 */
	void exitBinaryConnectorPart(SysMLParser.BinaryConnectorPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#naryConnectorPart}.
	 * @param ctx the parse tree
	 */
	void enterNaryConnectorPart(SysMLParser.NaryConnectorPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#naryConnectorPart}.
	 * @param ctx the parse tree
	 */
	void exitNaryConnectorPart(SysMLParser.NaryConnectorPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#connectorEndMember}.
	 * @param ctx the parse tree
	 */
	void enterConnectorEndMember(SysMLParser.ConnectorEndMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#connectorEndMember}.
	 * @param ctx the parse tree
	 */
	void exitConnectorEndMember(SysMLParser.ConnectorEndMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#connectorEnd}.
	 * @param ctx the parse tree
	 */
	void enterConnectorEnd(SysMLParser.ConnectorEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#connectorEnd}.
	 * @param ctx the parse tree
	 */
	void exitConnectorEnd(SysMLParser.ConnectorEndContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedCrossMultiplicityMember}.
	 * @param ctx the parse tree
	 */
	void enterOwnedCrossMultiplicityMember(SysMLParser.OwnedCrossMultiplicityMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedCrossMultiplicityMember}.
	 * @param ctx the parse tree
	 */
	void exitOwnedCrossMultiplicityMember(SysMLParser.OwnedCrossMultiplicityMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedCrossMultiplicity}.
	 * @param ctx the parse tree
	 */
	void enterOwnedCrossMultiplicity(SysMLParser.OwnedCrossMultiplicityContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedCrossMultiplicity}.
	 * @param ctx the parse tree
	 */
	void exitOwnedCrossMultiplicity(SysMLParser.OwnedCrossMultiplicityContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#bindingConnectorAsUsage}.
	 * @param ctx the parse tree
	 */
	void enterBindingConnectorAsUsage(SysMLParser.BindingConnectorAsUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#bindingConnectorAsUsage}.
	 * @param ctx the parse tree
	 */
	void exitBindingConnectorAsUsage(SysMLParser.BindingConnectorAsUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#successionAsUsage}.
	 * @param ctx the parse tree
	 */
	void enterSuccessionAsUsage(SysMLParser.SuccessionAsUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#successionAsUsage}.
	 * @param ctx the parse tree
	 */
	void exitSuccessionAsUsage(SysMLParser.SuccessionAsUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfaceDefinition}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDefinition(SysMLParser.InterfaceDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfaceDefinition}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDefinition(SysMLParser.InterfaceDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBody(SysMLParser.InterfaceBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBody(SysMLParser.InterfaceBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfaceBodyItem}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBodyItem(SysMLParser.InterfaceBodyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfaceBodyItem}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBodyItem(SysMLParser.InterfaceBodyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfaceNonOccurrenceUsageMember}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceNonOccurrenceUsageMember(SysMLParser.InterfaceNonOccurrenceUsageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfaceNonOccurrenceUsageMember}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceNonOccurrenceUsageMember(SysMLParser.InterfaceNonOccurrenceUsageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfaceNonOccurrenceUsageElement}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceNonOccurrenceUsageElement(SysMLParser.InterfaceNonOccurrenceUsageElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfaceNonOccurrenceUsageElement}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceNonOccurrenceUsageElement(SysMLParser.InterfaceNonOccurrenceUsageElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfaceOccurrenceUsageMember}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceOccurrenceUsageMember(SysMLParser.InterfaceOccurrenceUsageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfaceOccurrenceUsageMember}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceOccurrenceUsageMember(SysMLParser.InterfaceOccurrenceUsageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfaceOccurrenceUsageElement}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceOccurrenceUsageElement(SysMLParser.InterfaceOccurrenceUsageElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfaceOccurrenceUsageElement}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceOccurrenceUsageElement(SysMLParser.InterfaceOccurrenceUsageElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#defaultInterfaceEnd}.
	 * @param ctx the parse tree
	 */
	void enterDefaultInterfaceEnd(SysMLParser.DefaultInterfaceEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#defaultInterfaceEnd}.
	 * @param ctx the parse tree
	 */
	void exitDefaultInterfaceEnd(SysMLParser.DefaultInterfaceEndContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfaceUsage}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceUsage(SysMLParser.InterfaceUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfaceUsage}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceUsage(SysMLParser.InterfaceUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfaceUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceUsageDeclaration(SysMLParser.InterfaceUsageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfaceUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceUsageDeclaration(SysMLParser.InterfaceUsageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfacePart}.
	 * @param ctx the parse tree
	 */
	void enterInterfacePart(SysMLParser.InterfacePartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfacePart}.
	 * @param ctx the parse tree
	 */
	void exitInterfacePart(SysMLParser.InterfacePartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#binaryInterfacePart}.
	 * @param ctx the parse tree
	 */
	void enterBinaryInterfacePart(SysMLParser.BinaryInterfacePartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#binaryInterfacePart}.
	 * @param ctx the parse tree
	 */
	void exitBinaryInterfacePart(SysMLParser.BinaryInterfacePartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#naryInterfacePart}.
	 * @param ctx the parse tree
	 */
	void enterNaryInterfacePart(SysMLParser.NaryInterfacePartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#naryInterfacePart}.
	 * @param ctx the parse tree
	 */
	void exitNaryInterfacePart(SysMLParser.NaryInterfacePartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfaceEndMember}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceEndMember(SysMLParser.InterfaceEndMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfaceEndMember}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceEndMember(SysMLParser.InterfaceEndMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#interfaceEnd}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceEnd(SysMLParser.InterfaceEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#interfaceEnd}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceEnd(SysMLParser.InterfaceEndContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#allocationDefinition}.
	 * @param ctx the parse tree
	 */
	void enterAllocationDefinition(SysMLParser.AllocationDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#allocationDefinition}.
	 * @param ctx the parse tree
	 */
	void exitAllocationDefinition(SysMLParser.AllocationDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#allocationUsage}.
	 * @param ctx the parse tree
	 */
	void enterAllocationUsage(SysMLParser.AllocationUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#allocationUsage}.
	 * @param ctx the parse tree
	 */
	void exitAllocationUsage(SysMLParser.AllocationUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#allocationUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAllocationUsageDeclaration(SysMLParser.AllocationUsageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#allocationUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAllocationUsageDeclaration(SysMLParser.AllocationUsageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#flowDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFlowDefinition(SysMLParser.FlowDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#flowDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFlowDefinition(SysMLParser.FlowDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#message}.
	 * @param ctx the parse tree
	 */
	void enterMessage(SysMLParser.MessageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#message}.
	 * @param ctx the parse tree
	 */
	void exitMessage(SysMLParser.MessageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#messageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMessageDeclaration(SysMLParser.MessageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#messageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMessageDeclaration(SysMLParser.MessageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#messageEventMember}.
	 * @param ctx the parse tree
	 */
	void enterMessageEventMember(SysMLParser.MessageEventMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#messageEventMember}.
	 * @param ctx the parse tree
	 */
	void exitMessageEventMember(SysMLParser.MessageEventMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#messageEvent}.
	 * @param ctx the parse tree
	 */
	void enterMessageEvent(SysMLParser.MessageEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#messageEvent}.
	 * @param ctx the parse tree
	 */
	void exitMessageEvent(SysMLParser.MessageEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#flowUsage}.
	 * @param ctx the parse tree
	 */
	void enterFlowUsage(SysMLParser.FlowUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#flowUsage}.
	 * @param ctx the parse tree
	 */
	void exitFlowUsage(SysMLParser.FlowUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#successionFlowUsage}.
	 * @param ctx the parse tree
	 */
	void enterSuccessionFlowUsage(SysMLParser.SuccessionFlowUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#successionFlowUsage}.
	 * @param ctx the parse tree
	 */
	void exitSuccessionFlowUsage(SysMLParser.SuccessionFlowUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#flowDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFlowDeclaration(SysMLParser.FlowDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#flowDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFlowDeclaration(SysMLParser.FlowDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#flowPayloadFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterFlowPayloadFeatureMember(SysMLParser.FlowPayloadFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#flowPayloadFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitFlowPayloadFeatureMember(SysMLParser.FlowPayloadFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#flowPayloadFeature}.
	 * @param ctx the parse tree
	 */
	void enterFlowPayloadFeature(SysMLParser.FlowPayloadFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#flowPayloadFeature}.
	 * @param ctx the parse tree
	 */
	void exitFlowPayloadFeature(SysMLParser.FlowPayloadFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#payloadFeature}.
	 * @param ctx the parse tree
	 */
	void enterPayloadFeature(SysMLParser.PayloadFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#payloadFeature}.
	 * @param ctx the parse tree
	 */
	void exitPayloadFeature(SysMLParser.PayloadFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#payloadFeatureSpecializationPart}.
	 * @param ctx the parse tree
	 */
	void enterPayloadFeatureSpecializationPart(SysMLParser.PayloadFeatureSpecializationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#payloadFeatureSpecializationPart}.
	 * @param ctx the parse tree
	 */
	void exitPayloadFeatureSpecializationPart(SysMLParser.PayloadFeatureSpecializationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#flowEndMember}.
	 * @param ctx the parse tree
	 */
	void enterFlowEndMember(SysMLParser.FlowEndMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#flowEndMember}.
	 * @param ctx the parse tree
	 */
	void exitFlowEndMember(SysMLParser.FlowEndMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#flowEnd}.
	 * @param ctx the parse tree
	 */
	void enterFlowEnd(SysMLParser.FlowEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#flowEnd}.
	 * @param ctx the parse tree
	 */
	void exitFlowEnd(SysMLParser.FlowEndContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#flowEndSubsetting}.
	 * @param ctx the parse tree
	 */
	void enterFlowEndSubsetting(SysMLParser.FlowEndSubsettingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#flowEndSubsetting}.
	 * @param ctx the parse tree
	 */
	void exitFlowEndSubsetting(SysMLParser.FlowEndSubsettingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureChainPrefix}.
	 * @param ctx the parse tree
	 */
	void enterFeatureChainPrefix(SysMLParser.FeatureChainPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureChainPrefix}.
	 * @param ctx the parse tree
	 */
	void exitFeatureChainPrefix(SysMLParser.FeatureChainPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#flowFeatureMember}.
	 * @param ctx the parse tree
	 */
	void enterFlowFeatureMember(SysMLParser.FlowFeatureMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#flowFeatureMember}.
	 * @param ctx the parse tree
	 */
	void exitFlowFeatureMember(SysMLParser.FlowFeatureMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#flowFeature}.
	 * @param ctx the parse tree
	 */
	void enterFlowFeature(SysMLParser.FlowFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#flowFeature}.
	 * @param ctx the parse tree
	 */
	void exitFlowFeature(SysMLParser.FlowFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#flowFeatureRedefinition}.
	 * @param ctx the parse tree
	 */
	void enterFlowFeatureRedefinition(SysMLParser.FlowFeatureRedefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#flowFeatureRedefinition}.
	 * @param ctx the parse tree
	 */
	void exitFlowFeatureRedefinition(SysMLParser.FlowFeatureRedefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterActionDefinition(SysMLParser.ActionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitActionDefinition(SysMLParser.ActionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionBody}.
	 * @param ctx the parse tree
	 */
	void enterActionBody(SysMLParser.ActionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionBody}.
	 * @param ctx the parse tree
	 */
	void exitActionBody(SysMLParser.ActionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionBodyItem}.
	 * @param ctx the parse tree
	 */
	void enterActionBodyItem(SysMLParser.ActionBodyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionBodyItem}.
	 * @param ctx the parse tree
	 */
	void exitActionBodyItem(SysMLParser.ActionBodyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#nonBehaviorBodyItem}.
	 * @param ctx the parse tree
	 */
	void enterNonBehaviorBodyItem(SysMLParser.NonBehaviorBodyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#nonBehaviorBodyItem}.
	 * @param ctx the parse tree
	 */
	void exitNonBehaviorBodyItem(SysMLParser.NonBehaviorBodyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionBehaviorMember}.
	 * @param ctx the parse tree
	 */
	void enterActionBehaviorMember(SysMLParser.ActionBehaviorMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionBehaviorMember}.
	 * @param ctx the parse tree
	 */
	void exitActionBehaviorMember(SysMLParser.ActionBehaviorMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#initialNodeMember}.
	 * @param ctx the parse tree
	 */
	void enterInitialNodeMember(SysMLParser.InitialNodeMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#initialNodeMember}.
	 * @param ctx the parse tree
	 */
	void exitInitialNodeMember(SysMLParser.InitialNodeMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionNodeMember}.
	 * @param ctx the parse tree
	 */
	void enterActionNodeMember(SysMLParser.ActionNodeMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionNodeMember}.
	 * @param ctx the parse tree
	 */
	void exitActionNodeMember(SysMLParser.ActionNodeMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionTargetSuccessionMember}.
	 * @param ctx the parse tree
	 */
	void enterActionTargetSuccessionMember(SysMLParser.ActionTargetSuccessionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionTargetSuccessionMember}.
	 * @param ctx the parse tree
	 */
	void exitActionTargetSuccessionMember(SysMLParser.ActionTargetSuccessionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#guardedSuccessionMember}.
	 * @param ctx the parse tree
	 */
	void enterGuardedSuccessionMember(SysMLParser.GuardedSuccessionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#guardedSuccessionMember}.
	 * @param ctx the parse tree
	 */
	void exitGuardedSuccessionMember(SysMLParser.GuardedSuccessionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionUsage}.
	 * @param ctx the parse tree
	 */
	void enterActionUsage(SysMLParser.ActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionUsage}.
	 * @param ctx the parse tree
	 */
	void exitActionUsage(SysMLParser.ActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterActionUsageDeclaration(SysMLParser.ActionUsageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitActionUsageDeclaration(SysMLParser.ActionUsageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#performActionUsage}.
	 * @param ctx the parse tree
	 */
	void enterPerformActionUsage(SysMLParser.PerformActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#performActionUsage}.
	 * @param ctx the parse tree
	 */
	void exitPerformActionUsage(SysMLParser.PerformActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#performActionUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPerformActionUsageDeclaration(SysMLParser.PerformActionUsageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#performActionUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPerformActionUsageDeclaration(SysMLParser.PerformActionUsageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionNode}.
	 * @param ctx the parse tree
	 */
	void enterActionNode(SysMLParser.ActionNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionNode}.
	 * @param ctx the parse tree
	 */
	void exitActionNode(SysMLParser.ActionNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionNodeUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterActionNodeUsageDeclaration(SysMLParser.ActionNodeUsageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionNodeUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitActionNodeUsageDeclaration(SysMLParser.ActionNodeUsageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionNodePrefix}.
	 * @param ctx the parse tree
	 */
	void enterActionNodePrefix(SysMLParser.ActionNodePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionNodePrefix}.
	 * @param ctx the parse tree
	 */
	void exitActionNodePrefix(SysMLParser.ActionNodePrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#controlNode}.
	 * @param ctx the parse tree
	 */
	void enterControlNode(SysMLParser.ControlNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#controlNode}.
	 * @param ctx the parse tree
	 */
	void exitControlNode(SysMLParser.ControlNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#controlNodePrefix}.
	 * @param ctx the parse tree
	 */
	void enterControlNodePrefix(SysMLParser.ControlNodePrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#controlNodePrefix}.
	 * @param ctx the parse tree
	 */
	void exitControlNodePrefix(SysMLParser.ControlNodePrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#mergeNode}.
	 * @param ctx the parse tree
	 */
	void enterMergeNode(SysMLParser.MergeNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#mergeNode}.
	 * @param ctx the parse tree
	 */
	void exitMergeNode(SysMLParser.MergeNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#decisionNode}.
	 * @param ctx the parse tree
	 */
	void enterDecisionNode(SysMLParser.DecisionNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#decisionNode}.
	 * @param ctx the parse tree
	 */
	void exitDecisionNode(SysMLParser.DecisionNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#joinNode}.
	 * @param ctx the parse tree
	 */
	void enterJoinNode(SysMLParser.JoinNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#joinNode}.
	 * @param ctx the parse tree
	 */
	void exitJoinNode(SysMLParser.JoinNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#forkNode}.
	 * @param ctx the parse tree
	 */
	void enterForkNode(SysMLParser.ForkNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#forkNode}.
	 * @param ctx the parse tree
	 */
	void exitForkNode(SysMLParser.ForkNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#acceptNode}.
	 * @param ctx the parse tree
	 */
	void enterAcceptNode(SysMLParser.AcceptNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#acceptNode}.
	 * @param ctx the parse tree
	 */
	void exitAcceptNode(SysMLParser.AcceptNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#acceptNodeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAcceptNodeDeclaration(SysMLParser.AcceptNodeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#acceptNodeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAcceptNodeDeclaration(SysMLParser.AcceptNodeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#acceptParameterPart}.
	 * @param ctx the parse tree
	 */
	void enterAcceptParameterPart(SysMLParser.AcceptParameterPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#acceptParameterPart}.
	 * @param ctx the parse tree
	 */
	void exitAcceptParameterPart(SysMLParser.AcceptParameterPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#payloadParameterMember}.
	 * @param ctx the parse tree
	 */
	void enterPayloadParameterMember(SysMLParser.PayloadParameterMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#payloadParameterMember}.
	 * @param ctx the parse tree
	 */
	void exitPayloadParameterMember(SysMLParser.PayloadParameterMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#payloadParameter}.
	 * @param ctx the parse tree
	 */
	void enterPayloadParameter(SysMLParser.PayloadParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#payloadParameter}.
	 * @param ctx the parse tree
	 */
	void exitPayloadParameter(SysMLParser.PayloadParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#triggerValuePart}.
	 * @param ctx the parse tree
	 */
	void enterTriggerValuePart(SysMLParser.TriggerValuePartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#triggerValuePart}.
	 * @param ctx the parse tree
	 */
	void exitTriggerValuePart(SysMLParser.TriggerValuePartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#triggerFeatureValue}.
	 * @param ctx the parse tree
	 */
	void enterTriggerFeatureValue(SysMLParser.TriggerFeatureValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#triggerFeatureValue}.
	 * @param ctx the parse tree
	 */
	void exitTriggerFeatureValue(SysMLParser.TriggerFeatureValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#triggerExpression}.
	 * @param ctx the parse tree
	 */
	void enterTriggerExpression(SysMLParser.TriggerExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#triggerExpression}.
	 * @param ctx the parse tree
	 */
	void exitTriggerExpression(SysMLParser.TriggerExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#argumentMember}.
	 * @param ctx the parse tree
	 */
	void enterArgumentMember(SysMLParser.ArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#argumentMember}.
	 * @param ctx the parse tree
	 */
	void exitArgumentMember(SysMLParser.ArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(SysMLParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(SysMLParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#argumentValue}.
	 * @param ctx the parse tree
	 */
	void enterArgumentValue(SysMLParser.ArgumentValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#argumentValue}.
	 * @param ctx the parse tree
	 */
	void exitArgumentValue(SysMLParser.ArgumentValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#argumentExpressionMember}.
	 * @param ctx the parse tree
	 */
	void enterArgumentExpressionMember(SysMLParser.ArgumentExpressionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#argumentExpressionMember}.
	 * @param ctx the parse tree
	 */
	void exitArgumentExpressionMember(SysMLParser.ArgumentExpressionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#argumentExpression}.
	 * @param ctx the parse tree
	 */
	void enterArgumentExpression(SysMLParser.ArgumentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#argumentExpression}.
	 * @param ctx the parse tree
	 */
	void exitArgumentExpression(SysMLParser.ArgumentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#argumentExpressionValue}.
	 * @param ctx the parse tree
	 */
	void enterArgumentExpressionValue(SysMLParser.ArgumentExpressionValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#argumentExpressionValue}.
	 * @param ctx the parse tree
	 */
	void exitArgumentExpressionValue(SysMLParser.ArgumentExpressionValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#sendNode}.
	 * @param ctx the parse tree
	 */
	void enterSendNode(SysMLParser.SendNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#sendNode}.
	 * @param ctx the parse tree
	 */
	void exitSendNode(SysMLParser.SendNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#sendNodeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterSendNodeDeclaration(SysMLParser.SendNodeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#sendNodeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitSendNodeDeclaration(SysMLParser.SendNodeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#senderReceiverPart}.
	 * @param ctx the parse tree
	 */
	void enterSenderReceiverPart(SysMLParser.SenderReceiverPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#senderReceiverPart}.
	 * @param ctx the parse tree
	 */
	void exitSenderReceiverPart(SysMLParser.SenderReceiverPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#nodeParameterMember}.
	 * @param ctx the parse tree
	 */
	void enterNodeParameterMember(SysMLParser.NodeParameterMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#nodeParameterMember}.
	 * @param ctx the parse tree
	 */
	void exitNodeParameterMember(SysMLParser.NodeParameterMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#nodeParameter}.
	 * @param ctx the parse tree
	 */
	void enterNodeParameter(SysMLParser.NodeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#nodeParameter}.
	 * @param ctx the parse tree
	 */
	void exitNodeParameter(SysMLParser.NodeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureBinding}.
	 * @param ctx the parse tree
	 */
	void enterFeatureBinding(SysMLParser.FeatureBindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureBinding}.
	 * @param ctx the parse tree
	 */
	void exitFeatureBinding(SysMLParser.FeatureBindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#emptyParameterMember}.
	 * @param ctx the parse tree
	 */
	void enterEmptyParameterMember(SysMLParser.EmptyParameterMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#emptyParameterMember}.
	 * @param ctx the parse tree
	 */
	void exitEmptyParameterMember(SysMLParser.EmptyParameterMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#emptyUsage}.
	 * @param ctx the parse tree
	 */
	void enterEmptyUsage(SysMLParser.EmptyUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#emptyUsage}.
	 * @param ctx the parse tree
	 */
	void exitEmptyUsage(SysMLParser.EmptyUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#assignmentNode}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentNode(SysMLParser.AssignmentNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#assignmentNode}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentNode(SysMLParser.AssignmentNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#assignmentNodeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentNodeDeclaration(SysMLParser.AssignmentNodeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#assignmentNodeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentNodeDeclaration(SysMLParser.AssignmentNodeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#assignmentTargetMember}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentTargetMember(SysMLParser.AssignmentTargetMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#assignmentTargetMember}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentTargetMember(SysMLParser.AssignmentTargetMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#assignmentTargetParameter}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentTargetParameter(SysMLParser.AssignmentTargetParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#assignmentTargetParameter}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentTargetParameter(SysMLParser.AssignmentTargetParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#assignmentTargetBinding}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentTargetBinding(SysMLParser.AssignmentTargetBindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#assignmentTargetBinding}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentTargetBinding(SysMLParser.AssignmentTargetBindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureChainMember}.
	 * @param ctx the parse tree
	 */
	void enterFeatureChainMember(SysMLParser.FeatureChainMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureChainMember}.
	 * @param ctx the parse tree
	 */
	void exitFeatureChainMember(SysMLParser.FeatureChainMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedFeatureChainMember}.
	 * @param ctx the parse tree
	 */
	void enterOwnedFeatureChainMember(SysMLParser.OwnedFeatureChainMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedFeatureChainMember}.
	 * @param ctx the parse tree
	 */
	void exitOwnedFeatureChainMember(SysMLParser.OwnedFeatureChainMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#terminateNode}.
	 * @param ctx the parse tree
	 */
	void enterTerminateNode(SysMLParser.TerminateNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#terminateNode}.
	 * @param ctx the parse tree
	 */
	void exitTerminateNode(SysMLParser.TerminateNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ifNode}.
	 * @param ctx the parse tree
	 */
	void enterIfNode(SysMLParser.IfNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ifNode}.
	 * @param ctx the parse tree
	 */
	void exitIfNode(SysMLParser.IfNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#expressionParameterMember}.
	 * @param ctx the parse tree
	 */
	void enterExpressionParameterMember(SysMLParser.ExpressionParameterMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#expressionParameterMember}.
	 * @param ctx the parse tree
	 */
	void exitExpressionParameterMember(SysMLParser.ExpressionParameterMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionBodyParameterMember}.
	 * @param ctx the parse tree
	 */
	void enterActionBodyParameterMember(SysMLParser.ActionBodyParameterMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionBodyParameterMember}.
	 * @param ctx the parse tree
	 */
	void exitActionBodyParameterMember(SysMLParser.ActionBodyParameterMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionBodyParameter}.
	 * @param ctx the parse tree
	 */
	void enterActionBodyParameter(SysMLParser.ActionBodyParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionBodyParameter}.
	 * @param ctx the parse tree
	 */
	void exitActionBodyParameter(SysMLParser.ActionBodyParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ifNodeParameterMember}.
	 * @param ctx the parse tree
	 */
	void enterIfNodeParameterMember(SysMLParser.IfNodeParameterMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ifNodeParameterMember}.
	 * @param ctx the parse tree
	 */
	void exitIfNodeParameterMember(SysMLParser.IfNodeParameterMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#whileLoopNode}.
	 * @param ctx the parse tree
	 */
	void enterWhileLoopNode(SysMLParser.WhileLoopNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#whileLoopNode}.
	 * @param ctx the parse tree
	 */
	void exitWhileLoopNode(SysMLParser.WhileLoopNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#forLoopNode}.
	 * @param ctx the parse tree
	 */
	void enterForLoopNode(SysMLParser.ForLoopNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#forLoopNode}.
	 * @param ctx the parse tree
	 */
	void exitForLoopNode(SysMLParser.ForLoopNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#forVariableDeclarationMember}.
	 * @param ctx the parse tree
	 */
	void enterForVariableDeclarationMember(SysMLParser.ForVariableDeclarationMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#forVariableDeclarationMember}.
	 * @param ctx the parse tree
	 */
	void exitForVariableDeclarationMember(SysMLParser.ForVariableDeclarationMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#forVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterForVariableDeclaration(SysMLParser.ForVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#forVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitForVariableDeclaration(SysMLParser.ForVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actionTargetSuccession}.
	 * @param ctx the parse tree
	 */
	void enterActionTargetSuccession(SysMLParser.ActionTargetSuccessionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actionTargetSuccession}.
	 * @param ctx the parse tree
	 */
	void exitActionTargetSuccession(SysMLParser.ActionTargetSuccessionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#targetSuccession}.
	 * @param ctx the parse tree
	 */
	void enterTargetSuccession(SysMLParser.TargetSuccessionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#targetSuccession}.
	 * @param ctx the parse tree
	 */
	void exitTargetSuccession(SysMLParser.TargetSuccessionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#guardedTargetSuccession}.
	 * @param ctx the parse tree
	 */
	void enterGuardedTargetSuccession(SysMLParser.GuardedTargetSuccessionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#guardedTargetSuccession}.
	 * @param ctx the parse tree
	 */
	void exitGuardedTargetSuccession(SysMLParser.GuardedTargetSuccessionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#defaultTargetSuccession}.
	 * @param ctx the parse tree
	 */
	void enterDefaultTargetSuccession(SysMLParser.DefaultTargetSuccessionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#defaultTargetSuccession}.
	 * @param ctx the parse tree
	 */
	void exitDefaultTargetSuccession(SysMLParser.DefaultTargetSuccessionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#guardedSuccession}.
	 * @param ctx the parse tree
	 */
	void enterGuardedSuccession(SysMLParser.GuardedSuccessionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#guardedSuccession}.
	 * @param ctx the parse tree
	 */
	void exitGuardedSuccession(SysMLParser.GuardedSuccessionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#stateDefinition}.
	 * @param ctx the parse tree
	 */
	void enterStateDefinition(SysMLParser.StateDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#stateDefinition}.
	 * @param ctx the parse tree
	 */
	void exitStateDefinition(SysMLParser.StateDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#stateDefBody}.
	 * @param ctx the parse tree
	 */
	void enterStateDefBody(SysMLParser.StateDefBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#stateDefBody}.
	 * @param ctx the parse tree
	 */
	void exitStateDefBody(SysMLParser.StateDefBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#stateBodyItem}.
	 * @param ctx the parse tree
	 */
	void enterStateBodyItem(SysMLParser.StateBodyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#stateBodyItem}.
	 * @param ctx the parse tree
	 */
	void exitStateBodyItem(SysMLParser.StateBodyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#entryActionMember}.
	 * @param ctx the parse tree
	 */
	void enterEntryActionMember(SysMLParser.EntryActionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#entryActionMember}.
	 * @param ctx the parse tree
	 */
	void exitEntryActionMember(SysMLParser.EntryActionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#doActionMember}.
	 * @param ctx the parse tree
	 */
	void enterDoActionMember(SysMLParser.DoActionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#doActionMember}.
	 * @param ctx the parse tree
	 */
	void exitDoActionMember(SysMLParser.DoActionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#exitActionMember}.
	 * @param ctx the parse tree
	 */
	void enterExitActionMember(SysMLParser.ExitActionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#exitActionMember}.
	 * @param ctx the parse tree
	 */
	void exitExitActionMember(SysMLParser.ExitActionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#entryTransitionMember}.
	 * @param ctx the parse tree
	 */
	void enterEntryTransitionMember(SysMLParser.EntryTransitionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#entryTransitionMember}.
	 * @param ctx the parse tree
	 */
	void exitEntryTransitionMember(SysMLParser.EntryTransitionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#stateActionUsage}.
	 * @param ctx the parse tree
	 */
	void enterStateActionUsage(SysMLParser.StateActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#stateActionUsage}.
	 * @param ctx the parse tree
	 */
	void exitStateActionUsage(SysMLParser.StateActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#emptyActionUsage}.
	 * @param ctx the parse tree
	 */
	void enterEmptyActionUsage(SysMLParser.EmptyActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#emptyActionUsage}.
	 * @param ctx the parse tree
	 */
	void exitEmptyActionUsage(SysMLParser.EmptyActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#statePerformActionUsage}.
	 * @param ctx the parse tree
	 */
	void enterStatePerformActionUsage(SysMLParser.StatePerformActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#statePerformActionUsage}.
	 * @param ctx the parse tree
	 */
	void exitStatePerformActionUsage(SysMLParser.StatePerformActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#stateAcceptActionUsage}.
	 * @param ctx the parse tree
	 */
	void enterStateAcceptActionUsage(SysMLParser.StateAcceptActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#stateAcceptActionUsage}.
	 * @param ctx the parse tree
	 */
	void exitStateAcceptActionUsage(SysMLParser.StateAcceptActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#stateSendActionUsage}.
	 * @param ctx the parse tree
	 */
	void enterStateSendActionUsage(SysMLParser.StateSendActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#stateSendActionUsage}.
	 * @param ctx the parse tree
	 */
	void exitStateSendActionUsage(SysMLParser.StateSendActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#stateAssignmentActionUsage}.
	 * @param ctx the parse tree
	 */
	void enterStateAssignmentActionUsage(SysMLParser.StateAssignmentActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#stateAssignmentActionUsage}.
	 * @param ctx the parse tree
	 */
	void exitStateAssignmentActionUsage(SysMLParser.StateAssignmentActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#transitionUsageMember}.
	 * @param ctx the parse tree
	 */
	void enterTransitionUsageMember(SysMLParser.TransitionUsageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#transitionUsageMember}.
	 * @param ctx the parse tree
	 */
	void exitTransitionUsageMember(SysMLParser.TransitionUsageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#targetTransitionUsageMember}.
	 * @param ctx the parse tree
	 */
	void enterTargetTransitionUsageMember(SysMLParser.TargetTransitionUsageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#targetTransitionUsageMember}.
	 * @param ctx the parse tree
	 */
	void exitTargetTransitionUsageMember(SysMLParser.TargetTransitionUsageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#stateUsage}.
	 * @param ctx the parse tree
	 */
	void enterStateUsage(SysMLParser.StateUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#stateUsage}.
	 * @param ctx the parse tree
	 */
	void exitStateUsage(SysMLParser.StateUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#stateUsageBody}.
	 * @param ctx the parse tree
	 */
	void enterStateUsageBody(SysMLParser.StateUsageBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#stateUsageBody}.
	 * @param ctx the parse tree
	 */
	void exitStateUsageBody(SysMLParser.StateUsageBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#exhibitStateUsage}.
	 * @param ctx the parse tree
	 */
	void enterExhibitStateUsage(SysMLParser.ExhibitStateUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#exhibitStateUsage}.
	 * @param ctx the parse tree
	 */
	void exitExhibitStateUsage(SysMLParser.ExhibitStateUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#transitionUsage}.
	 * @param ctx the parse tree
	 */
	void enterTransitionUsage(SysMLParser.TransitionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#transitionUsage}.
	 * @param ctx the parse tree
	 */
	void exitTransitionUsage(SysMLParser.TransitionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#targetTransitionUsage}.
	 * @param ctx the parse tree
	 */
	void enterTargetTransitionUsage(SysMLParser.TargetTransitionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#targetTransitionUsage}.
	 * @param ctx the parse tree
	 */
	void exitTargetTransitionUsage(SysMLParser.TargetTransitionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#triggerActionMember}.
	 * @param ctx the parse tree
	 */
	void enterTriggerActionMember(SysMLParser.TriggerActionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#triggerActionMember}.
	 * @param ctx the parse tree
	 */
	void exitTriggerActionMember(SysMLParser.TriggerActionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#triggerAction}.
	 * @param ctx the parse tree
	 */
	void enterTriggerAction(SysMLParser.TriggerActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#triggerAction}.
	 * @param ctx the parse tree
	 */
	void exitTriggerAction(SysMLParser.TriggerActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#guardExpressionMember}.
	 * @param ctx the parse tree
	 */
	void enterGuardExpressionMember(SysMLParser.GuardExpressionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#guardExpressionMember}.
	 * @param ctx the parse tree
	 */
	void exitGuardExpressionMember(SysMLParser.GuardExpressionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#effectBehaviorMember}.
	 * @param ctx the parse tree
	 */
	void enterEffectBehaviorMember(SysMLParser.EffectBehaviorMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#effectBehaviorMember}.
	 * @param ctx the parse tree
	 */
	void exitEffectBehaviorMember(SysMLParser.EffectBehaviorMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#effectBehaviorUsage}.
	 * @param ctx the parse tree
	 */
	void enterEffectBehaviorUsage(SysMLParser.EffectBehaviorUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#effectBehaviorUsage}.
	 * @param ctx the parse tree
	 */
	void exitEffectBehaviorUsage(SysMLParser.EffectBehaviorUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#transitionPerformActionUsage}.
	 * @param ctx the parse tree
	 */
	void enterTransitionPerformActionUsage(SysMLParser.TransitionPerformActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#transitionPerformActionUsage}.
	 * @param ctx the parse tree
	 */
	void exitTransitionPerformActionUsage(SysMLParser.TransitionPerformActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#transitionAcceptActionUsage}.
	 * @param ctx the parse tree
	 */
	void enterTransitionAcceptActionUsage(SysMLParser.TransitionAcceptActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#transitionAcceptActionUsage}.
	 * @param ctx the parse tree
	 */
	void exitTransitionAcceptActionUsage(SysMLParser.TransitionAcceptActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#transitionSendActionUsage}.
	 * @param ctx the parse tree
	 */
	void enterTransitionSendActionUsage(SysMLParser.TransitionSendActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#transitionSendActionUsage}.
	 * @param ctx the parse tree
	 */
	void exitTransitionSendActionUsage(SysMLParser.TransitionSendActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#transitionAssignmentActionUsage}.
	 * @param ctx the parse tree
	 */
	void enterTransitionAssignmentActionUsage(SysMLParser.TransitionAssignmentActionUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#transitionAssignmentActionUsage}.
	 * @param ctx the parse tree
	 */
	void exitTransitionAssignmentActionUsage(SysMLParser.TransitionAssignmentActionUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#transitionSuccessionMember}.
	 * @param ctx the parse tree
	 */
	void enterTransitionSuccessionMember(SysMLParser.TransitionSuccessionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#transitionSuccessionMember}.
	 * @param ctx the parse tree
	 */
	void exitTransitionSuccessionMember(SysMLParser.TransitionSuccessionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#transitionSuccession}.
	 * @param ctx the parse tree
	 */
	void enterTransitionSuccession(SysMLParser.TransitionSuccessionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#transitionSuccession}.
	 * @param ctx the parse tree
	 */
	void exitTransitionSuccession(SysMLParser.TransitionSuccessionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#emptyEndMember}.
	 * @param ctx the parse tree
	 */
	void enterEmptyEndMember(SysMLParser.EmptyEndMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#emptyEndMember}.
	 * @param ctx the parse tree
	 */
	void exitEmptyEndMember(SysMLParser.EmptyEndMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#emptyFeature}.
	 * @param ctx the parse tree
	 */
	void enterEmptyFeature(SysMLParser.EmptyFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#emptyFeature}.
	 * @param ctx the parse tree
	 */
	void exitEmptyFeature(SysMLParser.EmptyFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#calculationDefinition}.
	 * @param ctx the parse tree
	 */
	void enterCalculationDefinition(SysMLParser.CalculationDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#calculationDefinition}.
	 * @param ctx the parse tree
	 */
	void exitCalculationDefinition(SysMLParser.CalculationDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#calculationUsage}.
	 * @param ctx the parse tree
	 */
	void enterCalculationUsage(SysMLParser.CalculationUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#calculationUsage}.
	 * @param ctx the parse tree
	 */
	void exitCalculationUsage(SysMLParser.CalculationUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#calculationUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterCalculationUsageDeclaration(SysMLParser.CalculationUsageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#calculationUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitCalculationUsageDeclaration(SysMLParser.CalculationUsageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#calculationBody}.
	 * @param ctx the parse tree
	 */
	void enterCalculationBody(SysMLParser.CalculationBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#calculationBody}.
	 * @param ctx the parse tree
	 */
	void exitCalculationBody(SysMLParser.CalculationBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#calculationBodyPart}.
	 * @param ctx the parse tree
	 */
	void enterCalculationBodyPart(SysMLParser.CalculationBodyPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#calculationBodyPart}.
	 * @param ctx the parse tree
	 */
	void exitCalculationBodyPart(SysMLParser.CalculationBodyPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#calculationBodyItem}.
	 * @param ctx the parse tree
	 */
	void enterCalculationBodyItem(SysMLParser.CalculationBodyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#calculationBodyItem}.
	 * @param ctx the parse tree
	 */
	void exitCalculationBodyItem(SysMLParser.CalculationBodyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#returnParameterMember}.
	 * @param ctx the parse tree
	 */
	void enterReturnParameterMember(SysMLParser.ReturnParameterMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#returnParameterMember}.
	 * @param ctx the parse tree
	 */
	void exitReturnParameterMember(SysMLParser.ReturnParameterMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#resultExpressionMember}.
	 * @param ctx the parse tree
	 */
	void enterResultExpressionMember(SysMLParser.ResultExpressionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#resultExpressionMember}.
	 * @param ctx the parse tree
	 */
	void exitResultExpressionMember(SysMLParser.ResultExpressionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#constraintDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConstraintDefinition(SysMLParser.ConstraintDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#constraintDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConstraintDefinition(SysMLParser.ConstraintDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#constraintUsage}.
	 * @param ctx the parse tree
	 */
	void enterConstraintUsage(SysMLParser.ConstraintUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#constraintUsage}.
	 * @param ctx the parse tree
	 */
	void exitConstraintUsage(SysMLParser.ConstraintUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#assertConstraintUsage}.
	 * @param ctx the parse tree
	 */
	void enterAssertConstraintUsage(SysMLParser.AssertConstraintUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#assertConstraintUsage}.
	 * @param ctx the parse tree
	 */
	void exitAssertConstraintUsage(SysMLParser.AssertConstraintUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#constraintUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstraintUsageDeclaration(SysMLParser.ConstraintUsageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#constraintUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstraintUsageDeclaration(SysMLParser.ConstraintUsageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#requirementDefinition}.
	 * @param ctx the parse tree
	 */
	void enterRequirementDefinition(SysMLParser.RequirementDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#requirementDefinition}.
	 * @param ctx the parse tree
	 */
	void exitRequirementDefinition(SysMLParser.RequirementDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#requirementBody}.
	 * @param ctx the parse tree
	 */
	void enterRequirementBody(SysMLParser.RequirementBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#requirementBody}.
	 * @param ctx the parse tree
	 */
	void exitRequirementBody(SysMLParser.RequirementBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#requirementBodyItem}.
	 * @param ctx the parse tree
	 */
	void enterRequirementBodyItem(SysMLParser.RequirementBodyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#requirementBodyItem}.
	 * @param ctx the parse tree
	 */
	void exitRequirementBodyItem(SysMLParser.RequirementBodyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#subjectMember}.
	 * @param ctx the parse tree
	 */
	void enterSubjectMember(SysMLParser.SubjectMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#subjectMember}.
	 * @param ctx the parse tree
	 */
	void exitSubjectMember(SysMLParser.SubjectMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#subjectUsage}.
	 * @param ctx the parse tree
	 */
	void enterSubjectUsage(SysMLParser.SubjectUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#subjectUsage}.
	 * @param ctx the parse tree
	 */
	void exitSubjectUsage(SysMLParser.SubjectUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#requirementConstraintMember}.
	 * @param ctx the parse tree
	 */
	void enterRequirementConstraintMember(SysMLParser.RequirementConstraintMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#requirementConstraintMember}.
	 * @param ctx the parse tree
	 */
	void exitRequirementConstraintMember(SysMLParser.RequirementConstraintMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#requirementKind}.
	 * @param ctx the parse tree
	 */
	void enterRequirementKind(SysMLParser.RequirementKindContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#requirementKind}.
	 * @param ctx the parse tree
	 */
	void exitRequirementKind(SysMLParser.RequirementKindContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#requirementConstraintUsage}.
	 * @param ctx the parse tree
	 */
	void enterRequirementConstraintUsage(SysMLParser.RequirementConstraintUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#requirementConstraintUsage}.
	 * @param ctx the parse tree
	 */
	void exitRequirementConstraintUsage(SysMLParser.RequirementConstraintUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#framedConcernMember}.
	 * @param ctx the parse tree
	 */
	void enterFramedConcernMember(SysMLParser.FramedConcernMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#framedConcernMember}.
	 * @param ctx the parse tree
	 */
	void exitFramedConcernMember(SysMLParser.FramedConcernMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#framedConcernUsage}.
	 * @param ctx the parse tree
	 */
	void enterFramedConcernUsage(SysMLParser.FramedConcernUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#framedConcernUsage}.
	 * @param ctx the parse tree
	 */
	void exitFramedConcernUsage(SysMLParser.FramedConcernUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actorMember}.
	 * @param ctx the parse tree
	 */
	void enterActorMember(SysMLParser.ActorMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actorMember}.
	 * @param ctx the parse tree
	 */
	void exitActorMember(SysMLParser.ActorMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#actorUsage}.
	 * @param ctx the parse tree
	 */
	void enterActorUsage(SysMLParser.ActorUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#actorUsage}.
	 * @param ctx the parse tree
	 */
	void exitActorUsage(SysMLParser.ActorUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#stakeholderMember}.
	 * @param ctx the parse tree
	 */
	void enterStakeholderMember(SysMLParser.StakeholderMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#stakeholderMember}.
	 * @param ctx the parse tree
	 */
	void exitStakeholderMember(SysMLParser.StakeholderMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#stakeholderUsage}.
	 * @param ctx the parse tree
	 */
	void enterStakeholderUsage(SysMLParser.StakeholderUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#stakeholderUsage}.
	 * @param ctx the parse tree
	 */
	void exitStakeholderUsage(SysMLParser.StakeholderUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#requirementUsage}.
	 * @param ctx the parse tree
	 */
	void enterRequirementUsage(SysMLParser.RequirementUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#requirementUsage}.
	 * @param ctx the parse tree
	 */
	void exitRequirementUsage(SysMLParser.RequirementUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#satisfyRequirementUsage}.
	 * @param ctx the parse tree
	 */
	void enterSatisfyRequirementUsage(SysMLParser.SatisfyRequirementUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#satisfyRequirementUsage}.
	 * @param ctx the parse tree
	 */
	void exitSatisfyRequirementUsage(SysMLParser.SatisfyRequirementUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#satisfactionSubjectMember}.
	 * @param ctx the parse tree
	 */
	void enterSatisfactionSubjectMember(SysMLParser.SatisfactionSubjectMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#satisfactionSubjectMember}.
	 * @param ctx the parse tree
	 */
	void exitSatisfactionSubjectMember(SysMLParser.SatisfactionSubjectMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#satisfactionParameter}.
	 * @param ctx the parse tree
	 */
	void enterSatisfactionParameter(SysMLParser.SatisfactionParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#satisfactionParameter}.
	 * @param ctx the parse tree
	 */
	void exitSatisfactionParameter(SysMLParser.SatisfactionParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#satisfactionFeatureValue}.
	 * @param ctx the parse tree
	 */
	void enterSatisfactionFeatureValue(SysMLParser.SatisfactionFeatureValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#satisfactionFeatureValue}.
	 * @param ctx the parse tree
	 */
	void exitSatisfactionFeatureValue(SysMLParser.SatisfactionFeatureValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#satisfactionReferenceExpression}.
	 * @param ctx the parse tree
	 */
	void enterSatisfactionReferenceExpression(SysMLParser.SatisfactionReferenceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#satisfactionReferenceExpression}.
	 * @param ctx the parse tree
	 */
	void exitSatisfactionReferenceExpression(SysMLParser.SatisfactionReferenceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#concernDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConcernDefinition(SysMLParser.ConcernDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#concernDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConcernDefinition(SysMLParser.ConcernDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#concernUsage}.
	 * @param ctx the parse tree
	 */
	void enterConcernUsage(SysMLParser.ConcernUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#concernUsage}.
	 * @param ctx the parse tree
	 */
	void exitConcernUsage(SysMLParser.ConcernUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#caseDefinition}.
	 * @param ctx the parse tree
	 */
	void enterCaseDefinition(SysMLParser.CaseDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#caseDefinition}.
	 * @param ctx the parse tree
	 */
	void exitCaseDefinition(SysMLParser.CaseDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#caseUsage}.
	 * @param ctx the parse tree
	 */
	void enterCaseUsage(SysMLParser.CaseUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#caseUsage}.
	 * @param ctx the parse tree
	 */
	void exitCaseUsage(SysMLParser.CaseUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#caseBody}.
	 * @param ctx the parse tree
	 */
	void enterCaseBody(SysMLParser.CaseBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#caseBody}.
	 * @param ctx the parse tree
	 */
	void exitCaseBody(SysMLParser.CaseBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#caseBodyItem}.
	 * @param ctx the parse tree
	 */
	void enterCaseBodyItem(SysMLParser.CaseBodyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#caseBodyItem}.
	 * @param ctx the parse tree
	 */
	void exitCaseBodyItem(SysMLParser.CaseBodyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#objectiveMember}.
	 * @param ctx the parse tree
	 */
	void enterObjectiveMember(SysMLParser.ObjectiveMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#objectiveMember}.
	 * @param ctx the parse tree
	 */
	void exitObjectiveMember(SysMLParser.ObjectiveMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#objectiveRequirementUsage}.
	 * @param ctx the parse tree
	 */
	void enterObjectiveRequirementUsage(SysMLParser.ObjectiveRequirementUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#objectiveRequirementUsage}.
	 * @param ctx the parse tree
	 */
	void exitObjectiveRequirementUsage(SysMLParser.ObjectiveRequirementUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#analysisCaseDefinition}.
	 * @param ctx the parse tree
	 */
	void enterAnalysisCaseDefinition(SysMLParser.AnalysisCaseDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#analysisCaseDefinition}.
	 * @param ctx the parse tree
	 */
	void exitAnalysisCaseDefinition(SysMLParser.AnalysisCaseDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#analysisCaseUsage}.
	 * @param ctx the parse tree
	 */
	void enterAnalysisCaseUsage(SysMLParser.AnalysisCaseUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#analysisCaseUsage}.
	 * @param ctx the parse tree
	 */
	void exitAnalysisCaseUsage(SysMLParser.AnalysisCaseUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#verificationCaseDefinition}.
	 * @param ctx the parse tree
	 */
	void enterVerificationCaseDefinition(SysMLParser.VerificationCaseDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#verificationCaseDefinition}.
	 * @param ctx the parse tree
	 */
	void exitVerificationCaseDefinition(SysMLParser.VerificationCaseDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#verificationCaseUsage}.
	 * @param ctx the parse tree
	 */
	void enterVerificationCaseUsage(SysMLParser.VerificationCaseUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#verificationCaseUsage}.
	 * @param ctx the parse tree
	 */
	void exitVerificationCaseUsage(SysMLParser.VerificationCaseUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#requirementVerificationMember}.
	 * @param ctx the parse tree
	 */
	void enterRequirementVerificationMember(SysMLParser.RequirementVerificationMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#requirementVerificationMember}.
	 * @param ctx the parse tree
	 */
	void exitRequirementVerificationMember(SysMLParser.RequirementVerificationMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#requirementVerificationUsage}.
	 * @param ctx the parse tree
	 */
	void enterRequirementVerificationUsage(SysMLParser.RequirementVerificationUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#requirementVerificationUsage}.
	 * @param ctx the parse tree
	 */
	void exitRequirementVerificationUsage(SysMLParser.RequirementVerificationUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#useCaseDefinition}.
	 * @param ctx the parse tree
	 */
	void enterUseCaseDefinition(SysMLParser.UseCaseDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#useCaseDefinition}.
	 * @param ctx the parse tree
	 */
	void exitUseCaseDefinition(SysMLParser.UseCaseDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#useCaseUsage}.
	 * @param ctx the parse tree
	 */
	void enterUseCaseUsage(SysMLParser.UseCaseUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#useCaseUsage}.
	 * @param ctx the parse tree
	 */
	void exitUseCaseUsage(SysMLParser.UseCaseUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#includeUseCaseUsage}.
	 * @param ctx the parse tree
	 */
	void enterIncludeUseCaseUsage(SysMLParser.IncludeUseCaseUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#includeUseCaseUsage}.
	 * @param ctx the parse tree
	 */
	void exitIncludeUseCaseUsage(SysMLParser.IncludeUseCaseUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#viewDefinition}.
	 * @param ctx the parse tree
	 */
	void enterViewDefinition(SysMLParser.ViewDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#viewDefinition}.
	 * @param ctx the parse tree
	 */
	void exitViewDefinition(SysMLParser.ViewDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#viewDefinitionBody}.
	 * @param ctx the parse tree
	 */
	void enterViewDefinitionBody(SysMLParser.ViewDefinitionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#viewDefinitionBody}.
	 * @param ctx the parse tree
	 */
	void exitViewDefinitionBody(SysMLParser.ViewDefinitionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#viewDefinitionBodyItem}.
	 * @param ctx the parse tree
	 */
	void enterViewDefinitionBodyItem(SysMLParser.ViewDefinitionBodyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#viewDefinitionBodyItem}.
	 * @param ctx the parse tree
	 */
	void exitViewDefinitionBodyItem(SysMLParser.ViewDefinitionBodyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#viewRenderingMember}.
	 * @param ctx the parse tree
	 */
	void enterViewRenderingMember(SysMLParser.ViewRenderingMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#viewRenderingMember}.
	 * @param ctx the parse tree
	 */
	void exitViewRenderingMember(SysMLParser.ViewRenderingMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#viewRenderingUsage}.
	 * @param ctx the parse tree
	 */
	void enterViewRenderingUsage(SysMLParser.ViewRenderingUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#viewRenderingUsage}.
	 * @param ctx the parse tree
	 */
	void exitViewRenderingUsage(SysMLParser.ViewRenderingUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#viewUsage}.
	 * @param ctx the parse tree
	 */
	void enterViewUsage(SysMLParser.ViewUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#viewUsage}.
	 * @param ctx the parse tree
	 */
	void exitViewUsage(SysMLParser.ViewUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#viewBody}.
	 * @param ctx the parse tree
	 */
	void enterViewBody(SysMLParser.ViewBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#viewBody}.
	 * @param ctx the parse tree
	 */
	void exitViewBody(SysMLParser.ViewBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#viewBodyItem}.
	 * @param ctx the parse tree
	 */
	void enterViewBodyItem(SysMLParser.ViewBodyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#viewBodyItem}.
	 * @param ctx the parse tree
	 */
	void exitViewBodyItem(SysMLParser.ViewBodyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#expose}.
	 * @param ctx the parse tree
	 */
	void enterExpose(SysMLParser.ExposeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#expose}.
	 * @param ctx the parse tree
	 */
	void exitExpose(SysMLParser.ExposeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#membershipExpose}.
	 * @param ctx the parse tree
	 */
	void enterMembershipExpose(SysMLParser.MembershipExposeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#membershipExpose}.
	 * @param ctx the parse tree
	 */
	void exitMembershipExpose(SysMLParser.MembershipExposeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#namespaceExpose}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceExpose(SysMLParser.NamespaceExposeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#namespaceExpose}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceExpose(SysMLParser.NamespaceExposeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#viewpointDefinition}.
	 * @param ctx the parse tree
	 */
	void enterViewpointDefinition(SysMLParser.ViewpointDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#viewpointDefinition}.
	 * @param ctx the parse tree
	 */
	void exitViewpointDefinition(SysMLParser.ViewpointDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#viewpointUsage}.
	 * @param ctx the parse tree
	 */
	void enterViewpointUsage(SysMLParser.ViewpointUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#viewpointUsage}.
	 * @param ctx the parse tree
	 */
	void exitViewpointUsage(SysMLParser.ViewpointUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#renderingDefinition}.
	 * @param ctx the parse tree
	 */
	void enterRenderingDefinition(SysMLParser.RenderingDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#renderingDefinition}.
	 * @param ctx the parse tree
	 */
	void exitRenderingDefinition(SysMLParser.RenderingDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#renderingUsage}.
	 * @param ctx the parse tree
	 */
	void enterRenderingUsage(SysMLParser.RenderingUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#renderingUsage}.
	 * @param ctx the parse tree
	 */
	void exitRenderingUsage(SysMLParser.RenderingUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataDefinition}.
	 * @param ctx the parse tree
	 */
	void enterMetadataDefinition(SysMLParser.MetadataDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataDefinition}.
	 * @param ctx the parse tree
	 */
	void exitMetadataDefinition(SysMLParser.MetadataDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#prefixMetadataAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterPrefixMetadataAnnotation(SysMLParser.PrefixMetadataAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#prefixMetadataAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitPrefixMetadataAnnotation(SysMLParser.PrefixMetadataAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#prefixMetadataMember}.
	 * @param ctx the parse tree
	 */
	void enterPrefixMetadataMember(SysMLParser.PrefixMetadataMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#prefixMetadataMember}.
	 * @param ctx the parse tree
	 */
	void exitPrefixMetadataMember(SysMLParser.PrefixMetadataMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#prefixMetadataUsage}.
	 * @param ctx the parse tree
	 */
	void enterPrefixMetadataUsage(SysMLParser.PrefixMetadataUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#prefixMetadataUsage}.
	 * @param ctx the parse tree
	 */
	void exitPrefixMetadataUsage(SysMLParser.PrefixMetadataUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataUsage}.
	 * @param ctx the parse tree
	 */
	void enterMetadataUsage(SysMLParser.MetadataUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataUsage}.
	 * @param ctx the parse tree
	 */
	void exitMetadataUsage(SysMLParser.MetadataUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMetadataUsageDeclaration(SysMLParser.MetadataUsageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataUsageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMetadataUsageDeclaration(SysMLParser.MetadataUsageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataBody}.
	 * @param ctx the parse tree
	 */
	void enterMetadataBody(SysMLParser.MetadataBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataBody}.
	 * @param ctx the parse tree
	 */
	void exitMetadataBody(SysMLParser.MetadataBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataBodyUsageMember}.
	 * @param ctx the parse tree
	 */
	void enterMetadataBodyUsageMember(SysMLParser.MetadataBodyUsageMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataBodyUsageMember}.
	 * @param ctx the parse tree
	 */
	void exitMetadataBodyUsageMember(SysMLParser.MetadataBodyUsageMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataBodyUsage}.
	 * @param ctx the parse tree
	 */
	void enterMetadataBodyUsage(SysMLParser.MetadataBodyUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataBodyUsage}.
	 * @param ctx the parse tree
	 */
	void exitMetadataBodyUsage(SysMLParser.MetadataBodyUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#extendedDefinition}.
	 * @param ctx the parse tree
	 */
	void enterExtendedDefinition(SysMLParser.ExtendedDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#extendedDefinition}.
	 * @param ctx the parse tree
	 */
	void exitExtendedDefinition(SysMLParser.ExtendedDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#extendedUsage}.
	 * @param ctx the parse tree
	 */
	void enterExtendedUsage(SysMLParser.ExtendedUsageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#extendedUsage}.
	 * @param ctx the parse tree
	 */
	void exitExtendedUsage(SysMLParser.ExtendedUsageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataFeature}.
	 * @param ctx the parse tree
	 */
	void enterMetadataFeature(SysMLParser.MetadataFeatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataFeature}.
	 * @param ctx the parse tree
	 */
	void exitMetadataFeature(SysMLParser.MetadataFeatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataFeatureDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMetadataFeatureDeclaration(SysMLParser.MetadataFeatureDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataFeatureDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMetadataFeatureDeclaration(SysMLParser.MetadataFeatureDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedExpressionReferenceMember}.
	 * @param ctx the parse tree
	 */
	void enterOwnedExpressionReferenceMember(SysMLParser.OwnedExpressionReferenceMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedExpressionReferenceMember}.
	 * @param ctx the parse tree
	 */
	void exitOwnedExpressionReferenceMember(SysMLParser.OwnedExpressionReferenceMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedExpressionReference}.
	 * @param ctx the parse tree
	 */
	void enterOwnedExpressionReference(SysMLParser.OwnedExpressionReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedExpressionReference}.
	 * @param ctx the parse tree
	 */
	void exitOwnedExpressionReference(SysMLParser.OwnedExpressionReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedExpressionMember}.
	 * @param ctx the parse tree
	 */
	void enterOwnedExpressionMember(SysMLParser.OwnedExpressionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedExpressionMember}.
	 * @param ctx the parse tree
	 */
	void exitOwnedExpressionMember(SysMLParser.OwnedExpressionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#ownedExpression}.
	 * @param ctx the parse tree
	 */
	void enterOwnedExpression(SysMLParser.OwnedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#ownedExpression}.
	 * @param ctx the parse tree
	 */
	void exitOwnedExpression(SysMLParser.OwnedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#conditionalBinaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterConditionalBinaryOperator(SysMLParser.ConditionalBinaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#conditionalBinaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitConditionalBinaryOperator(SysMLParser.ConditionalBinaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#binaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOperator(SysMLParser.BinaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#binaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOperator(SysMLParser.BinaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(SysMLParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(SysMLParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#classificationTestOperator}.
	 * @param ctx the parse tree
	 */
	void enterClassificationTestOperator(SysMLParser.ClassificationTestOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#classificationTestOperator}.
	 * @param ctx the parse tree
	 */
	void exitClassificationTestOperator(SysMLParser.ClassificationTestOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#castOperator}.
	 * @param ctx the parse tree
	 */
	void enterCastOperator(SysMLParser.CastOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#castOperator}.
	 * @param ctx the parse tree
	 */
	void exitCastOperator(SysMLParser.CastOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metaclassificationTestOperator}.
	 * @param ctx the parse tree
	 */
	void enterMetaclassificationTestOperator(SysMLParser.MetaclassificationTestOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metaclassificationTestOperator}.
	 * @param ctx the parse tree
	 */
	void exitMetaclassificationTestOperator(SysMLParser.MetaclassificationTestOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metacastOperator}.
	 * @param ctx the parse tree
	 */
	void enterMetacastOperator(SysMLParser.MetacastOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metacastOperator}.
	 * @param ctx the parse tree
	 */
	void exitMetacastOperator(SysMLParser.MetacastOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#typeReferenceMember}.
	 * @param ctx the parse tree
	 */
	void enterTypeReferenceMember(SysMLParser.TypeReferenceMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#typeReferenceMember}.
	 * @param ctx the parse tree
	 */
	void exitTypeReferenceMember(SysMLParser.TypeReferenceMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#typeResultMember}.
	 * @param ctx the parse tree
	 */
	void enterTypeResultMember(SysMLParser.TypeResultMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#typeResultMember}.
	 * @param ctx the parse tree
	 */
	void exitTypeResultMember(SysMLParser.TypeResultMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#typeReference}.
	 * @param ctx the parse tree
	 */
	void enterTypeReference(SysMLParser.TypeReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#typeReference}.
	 * @param ctx the parse tree
	 */
	void exitTypeReference(SysMLParser.TypeReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#referenceTyping}.
	 * @param ctx the parse tree
	 */
	void enterReferenceTyping(SysMLParser.ReferenceTypingContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#referenceTyping}.
	 * @param ctx the parse tree
	 */
	void exitReferenceTyping(SysMLParser.ReferenceTypingContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#emptyResultMember}.
	 * @param ctx the parse tree
	 */
	void enterEmptyResultMember(SysMLParser.EmptyResultMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#emptyResultMember}.
	 * @param ctx the parse tree
	 */
	void exitEmptyResultMember(SysMLParser.EmptyResultMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterMetadataArgumentMember(SysMLParser.MetadataArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitMetadataArgumentMember(SysMLParser.MetadataArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataArgument}.
	 * @param ctx the parse tree
	 */
	void enterMetadataArgument(SysMLParser.MetadataArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataArgument}.
	 * @param ctx the parse tree
	 */
	void exitMetadataArgument(SysMLParser.MetadataArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataValue}.
	 * @param ctx the parse tree
	 */
	void enterMetadataValue(SysMLParser.MetadataValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataValue}.
	 * @param ctx the parse tree
	 */
	void exitMetadataValue(SysMLParser.MetadataValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataReference}.
	 * @param ctx the parse tree
	 */
	void enterMetadataReference(SysMLParser.MetadataReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataReference}.
	 * @param ctx the parse tree
	 */
	void exitMetadataReference(SysMLParser.MetadataReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(SysMLParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(SysMLParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#primaryArgumentValue}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryArgumentValue(SysMLParser.PrimaryArgumentValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#primaryArgumentValue}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryArgumentValue(SysMLParser.PrimaryArgumentValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#primaryArgument}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryArgument(SysMLParser.PrimaryArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#primaryArgument}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryArgument(SysMLParser.PrimaryArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#primaryArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryArgumentMember(SysMLParser.PrimaryArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#primaryArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryArgumentMember(SysMLParser.PrimaryArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterNonFeatureChainPrimaryExpression(SysMLParser.NonFeatureChainPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitNonFeatureChainPrimaryExpression(SysMLParser.NonFeatureChainPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryArgumentValue}.
	 * @param ctx the parse tree
	 */
	void enterNonFeatureChainPrimaryArgumentValue(SysMLParser.NonFeatureChainPrimaryArgumentValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryArgumentValue}.
	 * @param ctx the parse tree
	 */
	void exitNonFeatureChainPrimaryArgumentValue(SysMLParser.NonFeatureChainPrimaryArgumentValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryArgument}.
	 * @param ctx the parse tree
	 */
	void enterNonFeatureChainPrimaryArgument(SysMLParser.NonFeatureChainPrimaryArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryArgument}.
	 * @param ctx the parse tree
	 */
	void exitNonFeatureChainPrimaryArgument(SysMLParser.NonFeatureChainPrimaryArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterNonFeatureChainPrimaryArgumentMember(SysMLParser.NonFeatureChainPrimaryArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#nonFeatureChainPrimaryArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitNonFeatureChainPrimaryArgumentMember(SysMLParser.NonFeatureChainPrimaryArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#bracketExpression}.
	 * @param ctx the parse tree
	 */
	void enterBracketExpression(SysMLParser.BracketExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#bracketExpression}.
	 * @param ctx the parse tree
	 */
	void exitBracketExpression(SysMLParser.BracketExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#indexExpression}.
	 * @param ctx the parse tree
	 */
	void enterIndexExpression(SysMLParser.IndexExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#indexExpression}.
	 * @param ctx the parse tree
	 */
	void exitIndexExpression(SysMLParser.IndexExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#sequenceExpression}.
	 * @param ctx the parse tree
	 */
	void enterSequenceExpression(SysMLParser.SequenceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#sequenceExpression}.
	 * @param ctx the parse tree
	 */
	void exitSequenceExpression(SysMLParser.SequenceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#sequenceExpressionList}.
	 * @param ctx the parse tree
	 */
	void enterSequenceExpressionList(SysMLParser.SequenceExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#sequenceExpressionList}.
	 * @param ctx the parse tree
	 */
	void exitSequenceExpressionList(SysMLParser.SequenceExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#sequenceOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void enterSequenceOperatorExpression(SysMLParser.SequenceOperatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#sequenceOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void exitSequenceOperatorExpression(SysMLParser.SequenceOperatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#sequenceExpressionListMember}.
	 * @param ctx the parse tree
	 */
	void enterSequenceExpressionListMember(SysMLParser.SequenceExpressionListMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#sequenceExpressionListMember}.
	 * @param ctx the parse tree
	 */
	void exitSequenceExpressionListMember(SysMLParser.SequenceExpressionListMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureChainExpression}.
	 * @param ctx the parse tree
	 */
	void enterFeatureChainExpression(SysMLParser.FeatureChainExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureChainExpression}.
	 * @param ctx the parse tree
	 */
	void exitFeatureChainExpression(SysMLParser.FeatureChainExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#collectExpression}.
	 * @param ctx the parse tree
	 */
	void enterCollectExpression(SysMLParser.CollectExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#collectExpression}.
	 * @param ctx the parse tree
	 */
	void exitCollectExpression(SysMLParser.CollectExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#selectExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpression(SysMLParser.SelectExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#selectExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpression(SysMLParser.SelectExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#functionOperationExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionOperationExpression(SysMLParser.FunctionOperationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#functionOperationExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionOperationExpression(SysMLParser.FunctionOperationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#bodyArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterBodyArgumentMember(SysMLParser.BodyArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#bodyArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitBodyArgumentMember(SysMLParser.BodyArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#bodyArgument}.
	 * @param ctx the parse tree
	 */
	void enterBodyArgument(SysMLParser.BodyArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#bodyArgument}.
	 * @param ctx the parse tree
	 */
	void exitBodyArgument(SysMLParser.BodyArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#bodyArgumentValue}.
	 * @param ctx the parse tree
	 */
	void enterBodyArgumentValue(SysMLParser.BodyArgumentValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#bodyArgumentValue}.
	 * @param ctx the parse tree
	 */
	void exitBodyArgumentValue(SysMLParser.BodyArgumentValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#functionReferenceArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReferenceArgumentMember(SysMLParser.FunctionReferenceArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#functionReferenceArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReferenceArgumentMember(SysMLParser.FunctionReferenceArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#functionReferenceArgument}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReferenceArgument(SysMLParser.FunctionReferenceArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#functionReferenceArgument}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReferenceArgument(SysMLParser.FunctionReferenceArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#functionReferenceArgumentValue}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReferenceArgumentValue(SysMLParser.FunctionReferenceArgumentValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#functionReferenceArgumentValue}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReferenceArgumentValue(SysMLParser.FunctionReferenceArgumentValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#functionReferenceExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReferenceExpression(SysMLParser.FunctionReferenceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#functionReferenceExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReferenceExpression(SysMLParser.FunctionReferenceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#functionReferenceMember}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReferenceMember(SysMLParser.FunctionReferenceMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#functionReferenceMember}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReferenceMember(SysMLParser.FunctionReferenceMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#functionReference}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReference(SysMLParser.FunctionReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#functionReference}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReference(SysMLParser.FunctionReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#invocationTypeMember}.
	 * @param ctx the parse tree
	 */
	void enterInvocationTypeMember(SysMLParser.InvocationTypeMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#invocationTypeMember}.
	 * @param ctx the parse tree
	 */
	void exitInvocationTypeMember(SysMLParser.InvocationTypeMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#invocationType}.
	 * @param ctx the parse tree
	 */
	void enterInvocationType(SysMLParser.InvocationTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#invocationType}.
	 * @param ctx the parse tree
	 */
	void exitInvocationType(SysMLParser.InvocationTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#baseExpression}.
	 * @param ctx the parse tree
	 */
	void enterBaseExpression(SysMLParser.BaseExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#baseExpression}.
	 * @param ctx the parse tree
	 */
	void exitBaseExpression(SysMLParser.BaseExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#nullExpression}.
	 * @param ctx the parse tree
	 */
	void enterNullExpression(SysMLParser.NullExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#nullExpression}.
	 * @param ctx the parse tree
	 */
	void exitNullExpression(SysMLParser.NullExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureReferenceExpression}.
	 * @param ctx the parse tree
	 */
	void enterFeatureReferenceExpression(SysMLParser.FeatureReferenceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureReferenceExpression}.
	 * @param ctx the parse tree
	 */
	void exitFeatureReferenceExpression(SysMLParser.FeatureReferenceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureReferenceMember}.
	 * @param ctx the parse tree
	 */
	void enterFeatureReferenceMember(SysMLParser.FeatureReferenceMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureReferenceMember}.
	 * @param ctx the parse tree
	 */
	void exitFeatureReferenceMember(SysMLParser.FeatureReferenceMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#featureReference}.
	 * @param ctx the parse tree
	 */
	void enterFeatureReference(SysMLParser.FeatureReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#featureReference}.
	 * @param ctx the parse tree
	 */
	void exitFeatureReference(SysMLParser.FeatureReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#metadataAccessExpression}.
	 * @param ctx the parse tree
	 */
	void enterMetadataAccessExpression(SysMLParser.MetadataAccessExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#metadataAccessExpression}.
	 * @param ctx the parse tree
	 */
	void exitMetadataAccessExpression(SysMLParser.MetadataAccessExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#elementReferenceMember}.
	 * @param ctx the parse tree
	 */
	void enterElementReferenceMember(SysMLParser.ElementReferenceMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#elementReferenceMember}.
	 * @param ctx the parse tree
	 */
	void exitElementReferenceMember(SysMLParser.ElementReferenceMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#invocationExpression}.
	 * @param ctx the parse tree
	 */
	void enterInvocationExpression(SysMLParser.InvocationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#invocationExpression}.
	 * @param ctx the parse tree
	 */
	void exitInvocationExpression(SysMLParser.InvocationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#constructorExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstructorExpression(SysMLParser.ConstructorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#constructorExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstructorExpression(SysMLParser.ConstructorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#constructorResultMember}.
	 * @param ctx the parse tree
	 */
	void enterConstructorResultMember(SysMLParser.ConstructorResultMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#constructorResultMember}.
	 * @param ctx the parse tree
	 */
	void exitConstructorResultMember(SysMLParser.ConstructorResultMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#constructorResult}.
	 * @param ctx the parse tree
	 */
	void enterConstructorResult(SysMLParser.ConstructorResultContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#constructorResult}.
	 * @param ctx the parse tree
	 */
	void exitConstructorResult(SysMLParser.ConstructorResultContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#instantiatedTypeMember}.
	 * @param ctx the parse tree
	 */
	void enterInstantiatedTypeMember(SysMLParser.InstantiatedTypeMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#instantiatedTypeMember}.
	 * @param ctx the parse tree
	 */
	void exitInstantiatedTypeMember(SysMLParser.InstantiatedTypeMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#instantiatedTypeReference}.
	 * @param ctx the parse tree
	 */
	void enterInstantiatedTypeReference(SysMLParser.InstantiatedTypeReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#instantiatedTypeReference}.
	 * @param ctx the parse tree
	 */
	void exitInstantiatedTypeReference(SysMLParser.InstantiatedTypeReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(SysMLParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(SysMLParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#positionalArgumentList}.
	 * @param ctx the parse tree
	 */
	void enterPositionalArgumentList(SysMLParser.PositionalArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#positionalArgumentList}.
	 * @param ctx the parse tree
	 */
	void exitPositionalArgumentList(SysMLParser.PositionalArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#namedArgumentList}.
	 * @param ctx the parse tree
	 */
	void enterNamedArgumentList(SysMLParser.NamedArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#namedArgumentList}.
	 * @param ctx the parse tree
	 */
	void exitNamedArgumentList(SysMLParser.NamedArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#namedArgumentMember}.
	 * @param ctx the parse tree
	 */
	void enterNamedArgumentMember(SysMLParser.NamedArgumentMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#namedArgumentMember}.
	 * @param ctx the parse tree
	 */
	void exitNamedArgumentMember(SysMLParser.NamedArgumentMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#namedArgument}.
	 * @param ctx the parse tree
	 */
	void enterNamedArgument(SysMLParser.NamedArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#namedArgument}.
	 * @param ctx the parse tree
	 */
	void exitNamedArgument(SysMLParser.NamedArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#parameterRedefinition}.
	 * @param ctx the parse tree
	 */
	void enterParameterRedefinition(SysMLParser.ParameterRedefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#parameterRedefinition}.
	 * @param ctx the parse tree
	 */
	void exitParameterRedefinition(SysMLParser.ParameterRedefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#bodyExpression}.
	 * @param ctx the parse tree
	 */
	void enterBodyExpression(SysMLParser.BodyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#bodyExpression}.
	 * @param ctx the parse tree
	 */
	void exitBodyExpression(SysMLParser.BodyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#expressionBodyMember}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBodyMember(SysMLParser.ExpressionBodyMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#expressionBodyMember}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBodyMember(SysMLParser.ExpressionBodyMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#expressionBody}.
	 * @param ctx the parse tree
	 */
	void enterExpressionBody(SysMLParser.ExpressionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#expressionBody}.
	 * @param ctx the parse tree
	 */
	void exitExpressionBody(SysMLParser.ExpressionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#literalExpression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpression(SysMLParser.LiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#literalExpression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpression(SysMLParser.LiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#literalBoolean}.
	 * @param ctx the parse tree
	 */
	void enterLiteralBoolean(SysMLParser.LiteralBooleanContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#literalBoolean}.
	 * @param ctx the parse tree
	 */
	void exitLiteralBoolean(SysMLParser.LiteralBooleanContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(SysMLParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#booleanValue}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(SysMLParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#literalString}.
	 * @param ctx the parse tree
	 */
	void enterLiteralString(SysMLParser.LiteralStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#literalString}.
	 * @param ctx the parse tree
	 */
	void exitLiteralString(SysMLParser.LiteralStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#literalInteger}.
	 * @param ctx the parse tree
	 */
	void enterLiteralInteger(SysMLParser.LiteralIntegerContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#literalInteger}.
	 * @param ctx the parse tree
	 */
	void exitLiteralInteger(SysMLParser.LiteralIntegerContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#literalReal}.
	 * @param ctx the parse tree
	 */
	void enterLiteralReal(SysMLParser.LiteralRealContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#literalReal}.
	 * @param ctx the parse tree
	 */
	void exitLiteralReal(SysMLParser.LiteralRealContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#realValue}.
	 * @param ctx the parse tree
	 */
	void enterRealValue(SysMLParser.RealValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#realValue}.
	 * @param ctx the parse tree
	 */
	void exitRealValue(SysMLParser.RealValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SysMLParser#literalInfinity}.
	 * @param ctx the parse tree
	 */
	void enterLiteralInfinity(SysMLParser.LiteralInfinityContext ctx);
	/**
	 * Exit a parse tree produced by {@link SysMLParser#literalInfinity}.
	 * @param ctx the parse tree
	 */
	void exitLiteralInfinity(SysMLParser.LiteralInfinityContext ctx);
}