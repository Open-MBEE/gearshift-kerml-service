// Generated from /Users/chasgaley/git/gearshift-kerml-service/gearshift-kerml-runtime/src/main/antlr/KerML.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class KerMLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ABOUT=1, ABSTRACT=2, ALIAS=3, ALL=4, AND=5, AS=6, ASSOC=7, BEHAVIOR=8, 
		BINDING=9, BOOL=10, BY=11, CHAINS=12, CLASS=13, CLASSIFIER=14, COMMENT=15, 
		COMPOSITE=16, CONJUGATE=17, CONJUGATES=18, CONJUGATION=19, CONNECTOR=20, 
		CONST=21, CROSSES=22, DATATYPE=23, DEFAULT=24, DEPENDENCY=25, DERIVED=26, 
		DIFFERENCES=27, DISJOINING=28, DISJOINT=29, DOC=30, ELSE=31, END=32, EXPOSE=33, 
		EXPR=34, FALSE=35, FEATURE=36, FEATURED=37, FEATURING=38, FILTER=39, FIRST=40, 
		FLOW=41, FOR=42, FROM=43, FUNCTION=44, HASTYPE=45, IF=46, IMPLIES=47, 
		IMPORT=48, IN=49, INOUT=50, INTERACTION=51, INTERSECTS=52, INV=53, INVERSE=54, 
		INVERTING=55, ISTYPE=56, LANGUAGE=57, LIBRARY=58, LOCALE=59, MEMBER=60, 
		META=61, METACLASS=62, METADATA=63, MULTIPLICITY=64, NAMESPACE=65, NEW=66, 
		NONUNIQUE=67, NOT=68, NULL=69, OF=70, OR=71, ORDERED=72, OUT=73, PACKAGE=74, 
		PORTION=75, PREDICATE=76, PRIVATE=77, PROTECTED=78, PUBLIC=79, REDEFINES=80, 
		REDEFINITION=81, REFERENCES=82, RENDER=83, RENDERING=84, REP=85, RETURN=86, 
		SPECIALIZATION=87, SPECIALIZES=88, STANDARD=89, STEP=90, STRUCT=91, SUBCLASSIFIER=92, 
		SUBSET=93, SUBSETS=94, SUBTYPE=95, SUCCESSION=96, THEN=97, TO=98, TRUE=99, 
		TYPE=100, TYPED=101, TYPING=102, UNIONS=103, VAR=104, VIEW=105, VIEWPOINT=106, 
		XOR=107, DOUBLE_COLON_GT=108, COLON_GT_GT=109, TRIPLE_EQUALS=110, EXCLAIM_EQUALS_EQUALS=111, 
		DOUBLE_STAR=112, DOUBLE_EQUALS=113, EXCLAIM_EQUALS=114, LESS_EQUALS=115, 
		GREATER_EQUALS=116, COLON_EQUALS=117, DOUBLE_COLON=118, COLON_GT=119, 
		ARROW=120, DOUBLE_DOT=121, EQUALS_GT=122, DOUBLE_QUESTION=123, DOT_QUESTION=124, 
		LPAREN=125, RPAREN=126, LBRACE=127, RBRACE=128, LBRACKET=129, RBRACKET=130, 
		SEMICOLON=131, COMMA=132, TILDE=133, AT=134, HASH=135, PERCENT=136, AMPERSAND=137, 
		CARET=138, PIPE=139, STAR=140, PLUS=141, MINUS=142, SLASH=143, DOLLAR=144, 
		DOT=145, COLON=146, LESS=147, EQUALS=148, GREATER=149, QUESTION=150, LINE_TERMINATOR=151, 
		SINGLE_LINE_NOTE=152, MULTILINE_NOTE=153, REGULAR_COMMENT=154, UNRESTRICTED_NAME=155, 
		DECIMAL_VALUE=156, EXPONENTIAL_VALUE=157, STRING_VALUE=158, NAME=159, 
		WS=160;
	public static final int
		RULE_typedByToken = 0, RULE_specializesToken = 1, RULE_subsetsToken = 2, 
		RULE_referencesToken = 3, RULE_crossesToken = 4, RULE_redefinesToken = 5, 
		RULE_conjugatesToken = 6, RULE_identification = 7, RULE_relationshipBody = 8, 
		RULE_relationshipOwnedElement = 9, RULE_ownedRelatedElement = 10, RULE_dependency = 11, 
		RULE_annotation = 12, RULE_ownedAnnotation = 13, RULE_annotatingElement = 14, 
		RULE_comment = 15, RULE_documentation = 16, RULE_textualRepresentation = 17, 
		RULE_rootNamespace = 18, RULE_namespace = 19, RULE_namespaceDeclaration = 20, 
		RULE_namespaceBody = 21, RULE_namespaceBodyElement = 22, RULE_memberPrefix = 23, 
		RULE_visibilityIndicator = 24, RULE_namespaceMember = 25, RULE_nonFeatureMember = 26, 
		RULE_namespaceFeatureMember = 27, RULE_aliasMember = 28, RULE_qualifiedName = 29, 
		RULE_import_ = 30, RULE_importDeclaration = 31, RULE_membershipImport = 32, 
		RULE_namespaceImport = 33, RULE_filterPackageImport = 34, RULE_filterPackageMember = 35, 
		RULE_memberElement = 36, RULE_nonFeatureElement = 37, RULE_featureElement = 38, 
		RULE_type = 39, RULE_typePrefix = 40, RULE_typeDeclaration = 41, RULE_specializationPart = 42, 
		RULE_conjugationPart = 43, RULE_typeRelationshipPart = 44, RULE_disjoiningPart = 45, 
		RULE_unioningPart = 46, RULE_intersectingPart = 47, RULE_differencingPart = 48, 
		RULE_typeBody = 49, RULE_typeBodyElement = 50, RULE_specialization = 51, 
		RULE_ownedSpecialization = 52, RULE_specificType = 53, RULE_generalType = 54, 
		RULE_conjugation = 55, RULE_ownedConjugation = 56, RULE_disjoining = 57, 
		RULE_ownedDisjoining = 58, RULE_unioning = 59, RULE_intersecting = 60, 
		RULE_differencing = 61, RULE_featureMember = 62, RULE_typeFeatureMember = 63, 
		RULE_ownedFeatureMember = 64, RULE_classifier = 65, RULE_classifierDeclaration = 66, 
		RULE_superclassingPart = 67, RULE_subclassification = 68, RULE_ownedSubclassification = 69, 
		RULE_feature = 70, RULE_endFeaturePrefix = 71, RULE_basicFeaturePrefix = 72, 
		RULE_featurePrefix = 73, RULE_ownedCrossFeatureMember = 74, RULE_ownedCrossFeature = 75, 
		RULE_featureDirection = 76, RULE_featureDeclaration = 77, RULE_featureIdentification = 78, 
		RULE_featureRelationshipPart = 79, RULE_chainingPart = 80, RULE_invertingPart = 81, 
		RULE_typeFeaturingPart = 82, RULE_featureSpecializationPart = 83, RULE_multiplicityPart = 84, 
		RULE_featureSpecialization = 85, RULE_typings = 86, RULE_typedBy = 87, 
		RULE_subsettings = 88, RULE_subsets = 89, RULE_references = 90, RULE_crosses = 91, 
		RULE_redefinitions = 92, RULE_redefines = 93, RULE_featureTyping = 94, 
		RULE_ownedFeatureTyping = 95, RULE_subsetting = 96, RULE_ownedSubsetting = 97, 
		RULE_ownedReferenceSubsetting = 98, RULE_ownedCrossSubsetting = 99, RULE_redefinition = 100, 
		RULE_ownedRedefinition = 101, RULE_ownedFeatureChain = 102, RULE_featureChain = 103, 
		RULE_ownedFeatureChaining = 104, RULE_featureInverting = 105, RULE_ownedFeatureInverting = 106, 
		RULE_typeFeaturing = 107, RULE_ownedTypeFeaturing = 108, RULE_datatype = 109, 
		RULE_class = 110, RULE_structure = 111, RULE_association = 112, RULE_associationStructure = 113, 
		RULE_connector = 114, RULE_connectorDeclaration = 115, RULE_binaryConnectorDeclaration = 116, 
		RULE_naryConnectorDeclaration = 117, RULE_connectorEndMember = 118, RULE_connectorEnd = 119, 
		RULE_ownedCrossMultiplicityMember = 120, RULE_ownedCrossMultiplicity = 121, 
		RULE_bindingConnector = 122, RULE_bindingConnectorDeclaration = 123, RULE_succession = 124, 
		RULE_successionDeclaration = 125, RULE_behavior = 126, RULE_step = 127, 
		RULE_function = 128, RULE_functionBody = 129, RULE_functionBodyPart = 130, 
		RULE_returnFeatureMember = 131, RULE_resultExpressionMember = 132, RULE_expression = 133, 
		RULE_predicate = 134, RULE_booleanExpression = 135, RULE_invariant = 136, 
		RULE_ownedExpressionReferenceMember = 137, RULE_ownedExpressionReference = 138, 
		RULE_ownedExpressionMember = 139, RULE_ownedExpression = 140, RULE_conditionalBinaryOperator = 141, 
		RULE_binaryOperatorExpression = 142, RULE_binaryOperator = 143, RULE_unaryOperatorExpression = 144, 
		RULE_unaryOperator = 145, RULE_classificationExpression = 146, RULE_classificationTestOperator = 147, 
		RULE_castOperator = 148, RULE_metaclassificationExpression = 149, RULE_argumentMember = 150, 
		RULE_argument = 151, RULE_argumentValue = 152, RULE_argumentExpressionMember = 153, 
		RULE_argumentExpression = 154, RULE_argumentExpressionValue = 155, RULE_metadataArgumentMember = 156, 
		RULE_metadataArgument = 157, RULE_metadataValue = 158, RULE_metadataReference = 159, 
		RULE_metaclassificationTestOperator = 160, RULE_metacastOperator = 161, 
		RULE_extentExpression = 162, RULE_typeReferenceMember = 163, RULE_typeResultMember = 164, 
		RULE_typeReference = 165, RULE_referenceTyping = 166, RULE_emptyResultMember = 167, 
		RULE_emptyFeature = 168, RULE_primaryExpression = 169, RULE_primaryArgumentValue = 170, 
		RULE_primaryArgument = 171, RULE_primaryArgumentMember = 172, RULE_nonFeatureChainPrimaryExpression = 173, 
		RULE_nonFeatureChainPrimaryArgumentValue = 174, RULE_nonFeatureChainPrimaryArgument = 175, 
		RULE_nonFeatureChainPrimaryArgumentMember = 176, RULE_bracketExpression = 177, 
		RULE_indexExpression = 178, RULE_sequenceExpression = 179, RULE_sequenceExpressionList = 180, 
		RULE_sequenceOperatorExpression = 181, RULE_sequenceExpressionListMember = 182, 
		RULE_featureChainExpression = 183, RULE_collectExpression = 184, RULE_selectExpression = 185, 
		RULE_functionOperationExpression = 186, RULE_bodyArgumentMember = 187, 
		RULE_bodyArgument = 188, RULE_bodyArgumentValue = 189, RULE_functionReferenceArgumentMember = 190, 
		RULE_functionReferenceArgument = 191, RULE_functionReferenceArgumentValue = 192, 
		RULE_functionReferenceExpression = 193, RULE_functionReferenceMember = 194, 
		RULE_functionReference = 195, RULE_featureChainMember = 196, RULE_invocationTypeMember = 197, 
		RULE_invocationType = 198, RULE_baseExpression = 199, RULE_nullExpression = 200, 
		RULE_featureReferenceExpression = 201, RULE_featureReferenceMember = 202, 
		RULE_featureReference = 203, RULE_metadataAccessExpression = 204, RULE_elementReferenceMember = 205, 
		RULE_invocationExpression = 206, RULE_constructorExpression = 207, RULE_constructorResultMember = 208, 
		RULE_constructorResult = 209, RULE_instantiatedTypeMember = 210, RULE_instantiatedTypeReference = 211, 
		RULE_ownedFeatureChainMember = 212, RULE_argumentList = 213, RULE_positionalArgumentList = 214, 
		RULE_namedArgumentList = 215, RULE_namedArgumentMember = 216, RULE_namedArgument = 217, 
		RULE_parameterRedefinition = 218, RULE_bodyExpression = 219, RULE_expressionBodyMember = 220, 
		RULE_expressionBody = 221, RULE_literalExpression = 222, RULE_literalBoolean = 223, 
		RULE_booleanValue = 224, RULE_literalString = 225, RULE_literalInteger = 226, 
		RULE_literalReal = 227, RULE_realValue = 228, RULE_literalInfinity = 229, 
		RULE_interaction = 230, RULE_flow = 231, RULE_successionFlow = 232, RULE_itemFlowDeclaration = 233, 
		RULE_payloadFeatureMember = 234, RULE_payloadFeature = 235, RULE_payloadFeatureSpecializationPart = 236, 
		RULE_flowEndMember = 237, RULE_flowEnd = 238, RULE_flowFeatureMember = 239, 
		RULE_flowFeature = 240, RULE_flowFeatureRedefinition = 241, RULE_valuePart = 242, 
		RULE_featureValue = 243, RULE_multiplicity = 244, RULE_multiplicitySubset = 245, 
		RULE_multiplicityRange = 246, RULE_ownedMultiplicity = 247, RULE_ownedMultiplicityRange = 248, 
		RULE_multiplicityBounds = 249, RULE_multiplicityExpressionMember = 250, 
		RULE_metaclass = 251, RULE_prefixMetadataAnnotation = 252, RULE_prefixMetadataMember = 253, 
		RULE_prefixMetadataFeature = 254, RULE_metadataFeature = 255, RULE_metadataFeatureDeclaration = 256, 
		RULE_metadataBody = 257, RULE_metadataBodyElement = 258, RULE_metadataBodyFeatureMember = 259, 
		RULE_metadataBodyFeature = 260, RULE_package = 261, RULE_libraryPackage = 262, 
		RULE_packageDeclaration = 263, RULE_packageBody = 264, RULE_elementFilterMember = 265, 
		RULE_view = 266, RULE_viewBody = 267, RULE_viewBodyElement = 268, RULE_expose = 269, 
		RULE_viewRenderingMember = 270, RULE_rendering = 271, RULE_viewpoint = 272;
	private static String[] makeRuleNames() {
		return new String[] {
			"typedByToken", "specializesToken", "subsetsToken", "referencesToken", 
			"crossesToken", "redefinesToken", "conjugatesToken", "identification", 
			"relationshipBody", "relationshipOwnedElement", "ownedRelatedElement", 
			"dependency", "annotation", "ownedAnnotation", "annotatingElement", "comment", 
			"documentation", "textualRepresentation", "rootNamespace", "namespace", 
			"namespaceDeclaration", "namespaceBody", "namespaceBodyElement", "memberPrefix", 
			"visibilityIndicator", "namespaceMember", "nonFeatureMember", "namespaceFeatureMember", 
			"aliasMember", "qualifiedName", "import_", "importDeclaration", "membershipImport", 
			"namespaceImport", "filterPackageImport", "filterPackageMember", "memberElement", 
			"nonFeatureElement", "featureElement", "type", "typePrefix", "typeDeclaration", 
			"specializationPart", "conjugationPart", "typeRelationshipPart", "disjoiningPart", 
			"unioningPart", "intersectingPart", "differencingPart", "typeBody", "typeBodyElement", 
			"specialization", "ownedSpecialization", "specificType", "generalType", 
			"conjugation", "ownedConjugation", "disjoining", "ownedDisjoining", "unioning", 
			"intersecting", "differencing", "featureMember", "typeFeatureMember", 
			"ownedFeatureMember", "classifier", "classifierDeclaration", "superclassingPart", 
			"subclassification", "ownedSubclassification", "feature", "endFeaturePrefix", 
			"basicFeaturePrefix", "featurePrefix", "ownedCrossFeatureMember", "ownedCrossFeature", 
			"featureDirection", "featureDeclaration", "featureIdentification", "featureRelationshipPart", 
			"chainingPart", "invertingPart", "typeFeaturingPart", "featureSpecializationPart", 
			"multiplicityPart", "featureSpecialization", "typings", "typedBy", "subsettings", 
			"subsets", "references", "crosses", "redefinitions", "redefines", "featureTyping", 
			"ownedFeatureTyping", "subsetting", "ownedSubsetting", "ownedReferenceSubsetting", 
			"ownedCrossSubsetting", "redefinition", "ownedRedefinition", "ownedFeatureChain", 
			"featureChain", "ownedFeatureChaining", "featureInverting", "ownedFeatureInverting", 
			"typeFeaturing", "ownedTypeFeaturing", "datatype", "class", "structure", 
			"association", "associationStructure", "connector", "connectorDeclaration", 
			"binaryConnectorDeclaration", "naryConnectorDeclaration", "connectorEndMember", 
			"connectorEnd", "ownedCrossMultiplicityMember", "ownedCrossMultiplicity", 
			"bindingConnector", "bindingConnectorDeclaration", "succession", "successionDeclaration", 
			"behavior", "step", "function", "functionBody", "functionBodyPart", "returnFeatureMember", 
			"resultExpressionMember", "expression", "predicate", "booleanExpression", 
			"invariant", "ownedExpressionReferenceMember", "ownedExpressionReference", 
			"ownedExpressionMember", "ownedExpression", "conditionalBinaryOperator", 
			"binaryOperatorExpression", "binaryOperator", "unaryOperatorExpression", 
			"unaryOperator", "classificationExpression", "classificationTestOperator", 
			"castOperator", "metaclassificationExpression", "argumentMember", "argument", 
			"argumentValue", "argumentExpressionMember", "argumentExpression", "argumentExpressionValue", 
			"metadataArgumentMember", "metadataArgument", "metadataValue", "metadataReference", 
			"metaclassificationTestOperator", "metacastOperator", "extentExpression", 
			"typeReferenceMember", "typeResultMember", "typeReference", "referenceTyping", 
			"emptyResultMember", "emptyFeature", "primaryExpression", "primaryArgumentValue", 
			"primaryArgument", "primaryArgumentMember", "nonFeatureChainPrimaryExpression", 
			"nonFeatureChainPrimaryArgumentValue", "nonFeatureChainPrimaryArgument", 
			"nonFeatureChainPrimaryArgumentMember", "bracketExpression", "indexExpression", 
			"sequenceExpression", "sequenceExpressionList", "sequenceOperatorExpression", 
			"sequenceExpressionListMember", "featureChainExpression", "collectExpression", 
			"selectExpression", "functionOperationExpression", "bodyArgumentMember", 
			"bodyArgument", "bodyArgumentValue", "functionReferenceArgumentMember", 
			"functionReferenceArgument", "functionReferenceArgumentValue", "functionReferenceExpression", 
			"functionReferenceMember", "functionReference", "featureChainMember", 
			"invocationTypeMember", "invocationType", "baseExpression", "nullExpression", 
			"featureReferenceExpression", "featureReferenceMember", "featureReference", 
			"metadataAccessExpression", "elementReferenceMember", "invocationExpression", 
			"constructorExpression", "constructorResultMember", "constructorResult", 
			"instantiatedTypeMember", "instantiatedTypeReference", "ownedFeatureChainMember", 
			"argumentList", "positionalArgumentList", "namedArgumentList", "namedArgumentMember", 
			"namedArgument", "parameterRedefinition", "bodyExpression", "expressionBodyMember", 
			"expressionBody", "literalExpression", "literalBoolean", "booleanValue", 
			"literalString", "literalInteger", "literalReal", "realValue", "literalInfinity", 
			"interaction", "flow", "successionFlow", "itemFlowDeclaration", "payloadFeatureMember", 
			"payloadFeature", "payloadFeatureSpecializationPart", "flowEndMember", 
			"flowEnd", "flowFeatureMember", "flowFeature", "flowFeatureRedefinition", 
			"valuePart", "featureValue", "multiplicity", "multiplicitySubset", "multiplicityRange", 
			"ownedMultiplicity", "ownedMultiplicityRange", "multiplicityBounds", 
			"multiplicityExpressionMember", "metaclass", "prefixMetadataAnnotation", 
			"prefixMetadataMember", "prefixMetadataFeature", "metadataFeature", "metadataFeatureDeclaration", 
			"metadataBody", "metadataBodyElement", "metadataBodyFeatureMember", "metadataBodyFeature", 
			"package", "libraryPackage", "packageDeclaration", "packageBody", "elementFilterMember", 
			"view", "viewBody", "viewBodyElement", "expose", "viewRenderingMember", 
			"rendering", "viewpoint"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'about'", "'abstract'", "'alias'", "'all'", "'and'", "'as'", "'assoc'", 
			"'behavior'", "'binding'", "'bool'", "'by'", "'chains'", "'class'", "'classifier'", 
			"'comment'", "'composite'", "'conjugate'", "'conjugates'", "'conjugation'", 
			"'connector'", "'const'", "'crosses'", "'datatype'", "'default'", "'dependency'", 
			"'derived'", "'differences'", "'disjoining'", "'disjoint'", "'doc'", 
			"'else'", "'end'", "'expose'", "'expr'", "'false'", "'feature'", "'featured'", 
			"'featuring'", "'filter'", "'first'", "'flow'", "'for'", "'from'", "'function'", 
			"'hastype'", "'if'", "'implies'", "'import'", "'in'", "'inout'", "'interaction'", 
			"'intersects'", "'inv'", "'inverse'", "'inverting'", "'istype'", "'language'", 
			"'library'", "'locale'", "'member'", "'meta'", "'metaclass'", "'metadata'", 
			"'multiplicity'", "'namespace'", "'new'", "'nonunique'", "'not'", "'null'", 
			"'of'", "'or'", "'ordered'", "'out'", "'package'", "'portion'", "'predicate'", 
			"'private'", "'protected'", "'public'", "'redefines'", "'redefinition'", 
			"'references'", "'render'", "'rendering'", "'rep'", "'return'", "'specialization'", 
			"'specializes'", "'standard'", "'step'", "'struct'", "'subclassifier'", 
			"'subset'", "'subsets'", "'subtype'", "'succession'", "'then'", "'to'", 
			"'true'", "'type'", "'typed'", "'typing'", "'unions'", "'var'", "'view'", 
			"'viewpoint'", "'xor'", "'::>'", "':>>'", "'==='", "'!=='", "'**'", "'=='", 
			"'!='", "'<='", "'>='", "':='", "'::'", "':>'", "'->'", "'..'", "'=>'", 
			"'??'", "'.?'", "'('", "')'", "'{'", "'}'", "'['", "']'", "';'", "','", 
			"'~'", "'@'", "'#'", "'%'", "'&'", "'^'", "'|'", "'*'", "'+'", "'-'", 
			"'/'", "'$'", "'.'", "':'", "'<'", "'='", "'>'", "'?'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ABOUT", "ABSTRACT", "ALIAS", "ALL", "AND", "AS", "ASSOC", "BEHAVIOR", 
			"BINDING", "BOOL", "BY", "CHAINS", "CLASS", "CLASSIFIER", "COMMENT", 
			"COMPOSITE", "CONJUGATE", "CONJUGATES", "CONJUGATION", "CONNECTOR", "CONST", 
			"CROSSES", "DATATYPE", "DEFAULT", "DEPENDENCY", "DERIVED", "DIFFERENCES", 
			"DISJOINING", "DISJOINT", "DOC", "ELSE", "END", "EXPOSE", "EXPR", "FALSE", 
			"FEATURE", "FEATURED", "FEATURING", "FILTER", "FIRST", "FLOW", "FOR", 
			"FROM", "FUNCTION", "HASTYPE", "IF", "IMPLIES", "IMPORT", "IN", "INOUT", 
			"INTERACTION", "INTERSECTS", "INV", "INVERSE", "INVERTING", "ISTYPE", 
			"LANGUAGE", "LIBRARY", "LOCALE", "MEMBER", "META", "METACLASS", "METADATA", 
			"MULTIPLICITY", "NAMESPACE", "NEW", "NONUNIQUE", "NOT", "NULL", "OF", 
			"OR", "ORDERED", "OUT", "PACKAGE", "PORTION", "PREDICATE", "PRIVATE", 
			"PROTECTED", "PUBLIC", "REDEFINES", "REDEFINITION", "REFERENCES", "RENDER", 
			"RENDERING", "REP", "RETURN", "SPECIALIZATION", "SPECIALIZES", "STANDARD", 
			"STEP", "STRUCT", "SUBCLASSIFIER", "SUBSET", "SUBSETS", "SUBTYPE", "SUCCESSION", 
			"THEN", "TO", "TRUE", "TYPE", "TYPED", "TYPING", "UNIONS", "VAR", "VIEW", 
			"VIEWPOINT", "XOR", "DOUBLE_COLON_GT", "COLON_GT_GT", "TRIPLE_EQUALS", 
			"EXCLAIM_EQUALS_EQUALS", "DOUBLE_STAR", "DOUBLE_EQUALS", "EXCLAIM_EQUALS", 
			"LESS_EQUALS", "GREATER_EQUALS", "COLON_EQUALS", "DOUBLE_COLON", "COLON_GT", 
			"ARROW", "DOUBLE_DOT", "EQUALS_GT", "DOUBLE_QUESTION", "DOT_QUESTION", 
			"LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACKET", "RBRACKET", "SEMICOLON", 
			"COMMA", "TILDE", "AT", "HASH", "PERCENT", "AMPERSAND", "CARET", "PIPE", 
			"STAR", "PLUS", "MINUS", "SLASH", "DOLLAR", "DOT", "COLON", "LESS", "EQUALS", 
			"GREATER", "QUESTION", "LINE_TERMINATOR", "SINGLE_LINE_NOTE", "MULTILINE_NOTE", 
			"REGULAR_COMMENT", "UNRESTRICTED_NAME", "DECIMAL_VALUE", "EXPONENTIAL_VALUE", 
			"STRING_VALUE", "NAME", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "KerML.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public KerMLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypedByTokenContext extends ParserRuleContext {
		public TerminalNode COLON() { return getToken(KerMLParser.COLON, 0); }
		public TerminalNode TYPED() { return getToken(KerMLParser.TYPED, 0); }
		public TerminalNode BY() { return getToken(KerMLParser.BY, 0); }
		public TypedByTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedByToken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypedByToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypedByToken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypedByToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedByTokenContext typedByToken() throws RecognitionException {
		TypedByTokenContext _localctx = new TypedByTokenContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_typedByToken);
		try {
			setState(549);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(546);
				match(COLON);
				}
				break;
			case TYPED:
				enterOuterAlt(_localctx, 2);
				{
				setState(547);
				match(TYPED);
				setState(548);
				match(BY);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SpecializesTokenContext extends ParserRuleContext {
		public TerminalNode COLON_GT() { return getToken(KerMLParser.COLON_GT, 0); }
		public TerminalNode SPECIALIZES() { return getToken(KerMLParser.SPECIALIZES, 0); }
		public SpecializesTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specializesToken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSpecializesToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSpecializesToken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSpecializesToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpecializesTokenContext specializesToken() throws RecognitionException {
		SpecializesTokenContext _localctx = new SpecializesTokenContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_specializesToken);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(551);
			_la = _input.LA(1);
			if ( !(_la==SPECIALIZES || _la==COLON_GT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubsetsTokenContext extends ParserRuleContext {
		public TerminalNode COLON_GT() { return getToken(KerMLParser.COLON_GT, 0); }
		public TerminalNode SUBSETS() { return getToken(KerMLParser.SUBSETS, 0); }
		public SubsetsTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subsetsToken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSubsetsToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSubsetsToken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSubsetsToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubsetsTokenContext subsetsToken() throws RecognitionException {
		SubsetsTokenContext _localctx = new SubsetsTokenContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_subsetsToken);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(553);
			_la = _input.LA(1);
			if ( !(_la==SUBSETS || _la==COLON_GT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReferencesTokenContext extends ParserRuleContext {
		public TerminalNode DOUBLE_COLON_GT() { return getToken(KerMLParser.DOUBLE_COLON_GT, 0); }
		public TerminalNode REFERENCES() { return getToken(KerMLParser.REFERENCES, 0); }
		public ReferencesTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_referencesToken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterReferencesToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitReferencesToken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitReferencesToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReferencesTokenContext referencesToken() throws RecognitionException {
		ReferencesTokenContext _localctx = new ReferencesTokenContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_referencesToken);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(555);
			_la = _input.LA(1);
			if ( !(_la==REFERENCES || _la==DOUBLE_COLON_GT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CrossesTokenContext extends ParserRuleContext {
		public TerminalNode EQUALS_GT() { return getToken(KerMLParser.EQUALS_GT, 0); }
		public TerminalNode CROSSES() { return getToken(KerMLParser.CROSSES, 0); }
		public CrossesTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_crossesToken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterCrossesToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitCrossesToken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitCrossesToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CrossesTokenContext crossesToken() throws RecognitionException {
		CrossesTokenContext _localctx = new CrossesTokenContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_crossesToken);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(557);
			_la = _input.LA(1);
			if ( !(_la==CROSSES || _la==EQUALS_GT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RedefinesTokenContext extends ParserRuleContext {
		public TerminalNode COLON_GT_GT() { return getToken(KerMLParser.COLON_GT_GT, 0); }
		public TerminalNode REDEFINES() { return getToken(KerMLParser.REDEFINES, 0); }
		public RedefinesTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_redefinesToken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterRedefinesToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitRedefinesToken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitRedefinesToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RedefinesTokenContext redefinesToken() throws RecognitionException {
		RedefinesTokenContext _localctx = new RedefinesTokenContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_redefinesToken);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(559);
			_la = _input.LA(1);
			if ( !(_la==REDEFINES || _la==COLON_GT_GT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConjugatesTokenContext extends ParserRuleContext {
		public TerminalNode TILDE() { return getToken(KerMLParser.TILDE, 0); }
		public TerminalNode CONJUGATES() { return getToken(KerMLParser.CONJUGATES, 0); }
		public ConjugatesTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjugatesToken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterConjugatesToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitConjugatesToken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitConjugatesToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConjugatesTokenContext conjugatesToken() throws RecognitionException {
		ConjugatesTokenContext _localctx = new ConjugatesTokenContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_conjugatesToken);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(561);
			_la = _input.LA(1);
			if ( !(_la==CONJUGATES || _la==TILDE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdentificationContext extends ParserRuleContext {
		public Token declaredShortName;
		public Token declaredName;
		public TerminalNode LESS() { return getToken(KerMLParser.LESS, 0); }
		public TerminalNode GREATER() { return getToken(KerMLParser.GREATER, 0); }
		public List<TerminalNode> NAME() { return getTokens(KerMLParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(KerMLParser.NAME, i);
		}
		public IdentificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterIdentification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitIdentification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitIdentification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentificationContext identification() throws RecognitionException {
		IdentificationContext _localctx = new IdentificationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_identification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(566);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LESS) {
				{
				setState(563);
				match(LESS);
				setState(564);
				((IdentificationContext)_localctx).declaredShortName = match(NAME);
				setState(565);
				match(GREATER);
				}
			}

			setState(569);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAME) {
				{
				setState(568);
				((IdentificationContext)_localctx).declaredName = match(NAME);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelationshipBodyContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(KerMLParser.SEMICOLON, 0); }
		public TerminalNode LBRACE() { return getToken(KerMLParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(KerMLParser.RBRACE, 0); }
		public List<RelationshipOwnedElementContext> relationshipOwnedElement() {
			return getRuleContexts(RelationshipOwnedElementContext.class);
		}
		public RelationshipOwnedElementContext relationshipOwnedElement(int i) {
			return getRuleContext(RelationshipOwnedElementContext.class,i);
		}
		public RelationshipBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationshipBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterRelationshipBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitRelationshipBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitRelationshipBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationshipBodyContext relationshipBody() throws RecognitionException {
		RelationshipBodyContext _localctx = new RelationshipBodyContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_relationshipBody);
		int _la;
		try {
			setState(580);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEMICOLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(571);
				match(SEMICOLON);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(572);
				match(LBRACE);
				setState(576);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3824098875312969836L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 324320135914921739L) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & 1107689585L) != 0)) {
					{
					{
					setState(573);
					relationshipOwnedElement();
					}
					}
					setState(578);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(579);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelationshipOwnedElementContext extends ParserRuleContext {
		public OwnedRelatedElementContext ownedRelatedElement() {
			return getRuleContext(OwnedRelatedElementContext.class,0);
		}
		public OwnedAnnotationContext ownedAnnotation() {
			return getRuleContext(OwnedAnnotationContext.class,0);
		}
		public RelationshipOwnedElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationshipOwnedElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterRelationshipOwnedElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitRelationshipOwnedElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitRelationshipOwnedElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationshipOwnedElementContext relationshipOwnedElement() throws RecognitionException {
		RelationshipOwnedElementContext _localctx = new RelationshipOwnedElementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_relationshipOwnedElement);
		try {
			setState(584);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(582);
				ownedRelatedElement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(583);
				ownedAnnotation();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedRelatedElementContext extends ParserRuleContext {
		public NonFeatureElementContext nonFeatureElement() {
			return getRuleContext(NonFeatureElementContext.class,0);
		}
		public FeatureElementContext featureElement() {
			return getRuleContext(FeatureElementContext.class,0);
		}
		public OwnedRelatedElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedRelatedElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedRelatedElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedRelatedElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedRelatedElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedRelatedElementContext ownedRelatedElement() throws RecognitionException {
		OwnedRelatedElementContext _localctx = new OwnedRelatedElementContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_ownedRelatedElement);
		try {
			setState(588);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(586);
				nonFeatureElement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(587);
				featureElement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DependencyContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName;
		public List<QualifiedNameContext> client = new ArrayList<QualifiedNameContext>();
		public List<QualifiedNameContext> supplier = new ArrayList<QualifiedNameContext>();
		public TerminalNode DEPENDENCY() { return getToken(KerMLParser.DEPENDENCY, 0); }
		public TerminalNode TO() { return getToken(KerMLParser.TO, 0); }
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public List<PrefixMetadataAnnotationContext> prefixMetadataAnnotation() {
			return getRuleContexts(PrefixMetadataAnnotationContext.class);
		}
		public PrefixMetadataAnnotationContext prefixMetadataAnnotation(int i) {
			return getRuleContext(PrefixMetadataAnnotationContext.class,i);
		}
		public TerminalNode FROM() { return getToken(KerMLParser.FROM, 0); }
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public DependencyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dependency; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterDependency(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitDependency(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitDependency(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DependencyContext dependency() throws RecognitionException {
		DependencyContext _localctx = new DependencyContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_dependency);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(593);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==HASH) {
				{
				{
				setState(590);
				prefixMetadataAnnotation();
				}
				}
				setState(595);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(596);
			match(DEPENDENCY);
			setState(601);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(598);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
				case 1:
					{
					setState(597);
					identification();
					}
					break;
				}
				setState(600);
				match(FROM);
				}
				break;
			}
			setState(603);
			((DependencyContext)_localctx).qualifiedName = qualifiedName();
			((DependencyContext)_localctx).client.add(((DependencyContext)_localctx).qualifiedName);
			setState(608);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(604);
				match(COMMA);
				setState(605);
				((DependencyContext)_localctx).qualifiedName = qualifiedName();
				((DependencyContext)_localctx).client.add(((DependencyContext)_localctx).qualifiedName);
				}
				}
				setState(610);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(611);
			match(TO);
			setState(612);
			((DependencyContext)_localctx).qualifiedName = qualifiedName();
			((DependencyContext)_localctx).supplier.add(((DependencyContext)_localctx).qualifiedName);
			setState(617);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(613);
				match(COMMA);
				setState(614);
				((DependencyContext)_localctx).qualifiedName = qualifiedName();
				((DependencyContext)_localctx).supplier.add(((DependencyContext)_localctx).qualifiedName);
				}
				}
				setState(619);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(620);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotationContext extends ParserRuleContext {
		public QualifiedNameContext annotatedElement;
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_annotation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(622);
			((AnnotationContext)_localctx).annotatedElement = qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedAnnotationContext extends ParserRuleContext {
		public AnnotatingElementContext annotatingElement() {
			return getRuleContext(AnnotatingElementContext.class,0);
		}
		public OwnedAnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedAnnotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedAnnotationContext ownedAnnotation() throws RecognitionException {
		OwnedAnnotationContext _localctx = new OwnedAnnotationContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_ownedAnnotation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(624);
			annotatingElement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AnnotatingElementContext extends ParserRuleContext {
		public CommentContext comment() {
			return getRuleContext(CommentContext.class,0);
		}
		public DocumentationContext documentation() {
			return getRuleContext(DocumentationContext.class,0);
		}
		public TextualRepresentationContext textualRepresentation() {
			return getRuleContext(TextualRepresentationContext.class,0);
		}
		public MetadataFeatureContext metadataFeature() {
			return getRuleContext(MetadataFeatureContext.class,0);
		}
		public AnnotatingElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotatingElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterAnnotatingElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitAnnotatingElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitAnnotatingElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotatingElementContext annotatingElement() throws RecognitionException {
		AnnotatingElementContext _localctx = new AnnotatingElementContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_annotatingElement);
		try {
			setState(630);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COMMENT:
			case LOCALE:
			case REGULAR_COMMENT:
				enterOuterAlt(_localctx, 1);
				{
				setState(626);
				comment();
				}
				break;
			case DOC:
				enterOuterAlt(_localctx, 2);
				{
				setState(627);
				documentation();
				}
				break;
			case LANGUAGE:
			case REP:
				enterOuterAlt(_localctx, 3);
				{
				setState(628);
				textualRepresentation();
				}
				break;
			case METADATA:
			case AT:
			case HASH:
				enterOuterAlt(_localctx, 4);
				{
				setState(629);
				metadataFeature();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CommentContext extends ParserRuleContext {
		public AnnotationContext annotation;
		public List<AnnotationContext> ownedRelationship = new ArrayList<AnnotationContext>();
		public Token locale;
		public Token body;
		public TerminalNode REGULAR_COMMENT() { return getToken(KerMLParser.REGULAR_COMMENT, 0); }
		public TerminalNode COMMENT() { return getToken(KerMLParser.COMMENT, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public TerminalNode LOCALE() { return getToken(KerMLParser.LOCALE, 0); }
		public TerminalNode STRING_VALUE() { return getToken(KerMLParser.STRING_VALUE, 0); }
		public TerminalNode ABOUT() { return getToken(KerMLParser.ABOUT, 0); }
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public CommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentContext comment() throws RecognitionException {
		CommentContext _localctx = new CommentContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_comment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(645);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMENT) {
				{
				setState(632);
				match(COMMENT);
				setState(633);
				identification();
				setState(643);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ABOUT) {
					{
					setState(634);
					match(ABOUT);
					setState(635);
					((CommentContext)_localctx).annotation = annotation();
					((CommentContext)_localctx).ownedRelationship.add(((CommentContext)_localctx).annotation);
					setState(640);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(636);
						match(COMMA);
						setState(637);
						((CommentContext)_localctx).annotation = annotation();
						((CommentContext)_localctx).ownedRelationship.add(((CommentContext)_localctx).annotation);
						}
						}
						setState(642);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				}
			}

			setState(649);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LOCALE) {
				{
				setState(647);
				match(LOCALE);
				setState(648);
				((CommentContext)_localctx).locale = match(STRING_VALUE);
				}
			}

			setState(651);
			((CommentContext)_localctx).body = match(REGULAR_COMMENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DocumentationContext extends ParserRuleContext {
		public Token locale;
		public Token body;
		public TerminalNode DOC() { return getToken(KerMLParser.DOC, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public TerminalNode REGULAR_COMMENT() { return getToken(KerMLParser.REGULAR_COMMENT, 0); }
		public TerminalNode LOCALE() { return getToken(KerMLParser.LOCALE, 0); }
		public TerminalNode STRING_VALUE() { return getToken(KerMLParser.STRING_VALUE, 0); }
		public DocumentationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_documentation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterDocumentation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitDocumentation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitDocumentation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocumentationContext documentation() throws RecognitionException {
		DocumentationContext _localctx = new DocumentationContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_documentation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(653);
			match(DOC);
			setState(654);
			identification();
			setState(657);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LOCALE) {
				{
				setState(655);
				match(LOCALE);
				setState(656);
				((DocumentationContext)_localctx).locale = match(STRING_VALUE);
				}
			}

			setState(659);
			((DocumentationContext)_localctx).body = match(REGULAR_COMMENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TextualRepresentationContext extends ParserRuleContext {
		public Token language;
		public Token body;
		public TerminalNode LANGUAGE() { return getToken(KerMLParser.LANGUAGE, 0); }
		public TerminalNode STRING_VALUE() { return getToken(KerMLParser.STRING_VALUE, 0); }
		public TerminalNode REGULAR_COMMENT() { return getToken(KerMLParser.REGULAR_COMMENT, 0); }
		public TerminalNode REP() { return getToken(KerMLParser.REP, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public TextualRepresentationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_textualRepresentation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTextualRepresentation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTextualRepresentation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTextualRepresentation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TextualRepresentationContext textualRepresentation() throws RecognitionException {
		TextualRepresentationContext _localctx = new TextualRepresentationContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_textualRepresentation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(663);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==REP) {
				{
				setState(661);
				match(REP);
				setState(662);
				identification();
				}
			}

			setState(665);
			match(LANGUAGE);
			setState(666);
			((TextualRepresentationContext)_localctx).language = match(STRING_VALUE);
			setState(667);
			((TextualRepresentationContext)_localctx).body = match(REGULAR_COMMENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RootNamespaceContext extends ParserRuleContext {
		public List<NamespaceBodyElementContext> namespaceBodyElement() {
			return getRuleContexts(NamespaceBodyElementContext.class);
		}
		public NamespaceBodyElementContext namespaceBodyElement(int i) {
			return getRuleContext(NamespaceBodyElementContext.class,i);
		}
		public RootNamespaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rootNamespace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterRootNamespace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitRootNamespace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitRootNamespace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RootNamespaceContext rootNamespace() throws RecognitionException {
		RootNamespaceContext _localctx = new RootNamespaceContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_rootNamespace);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(672);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3824098875312969828L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 324320135914979083L) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & 1107689585L) != 0)) {
				{
				{
				setState(669);
				namespaceBodyElement();
				}
				}
				setState(674);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceContext extends ParserRuleContext {
		public NamespaceDeclarationContext namespaceDeclaration() {
			return getRuleContext(NamespaceDeclarationContext.class,0);
		}
		public NamespaceBodyContext namespaceBody() {
			return getRuleContext(NamespaceBodyContext.class,0);
		}
		public List<PrefixMetadataMemberContext> prefixMetadataMember() {
			return getRuleContexts(PrefixMetadataMemberContext.class);
		}
		public PrefixMetadataMemberContext prefixMetadataMember(int i) {
			return getRuleContext(PrefixMetadataMemberContext.class,i);
		}
		public NamespaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNamespace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNamespace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNamespace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceContext namespace() throws RecognitionException {
		NamespaceContext _localctx = new NamespaceContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_namespace);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(678);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==HASH) {
				{
				{
				setState(675);
				prefixMetadataMember();
				}
				}
				setState(680);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(681);
			namespaceDeclaration();
			setState(682);
			namespaceBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceDeclarationContext extends ParserRuleContext {
		public TerminalNode NAMESPACE() { return getToken(KerMLParser.NAMESPACE, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public NamespaceDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNamespaceDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNamespaceDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNamespaceDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceDeclarationContext namespaceDeclaration() throws RecognitionException {
		NamespaceDeclarationContext _localctx = new NamespaceDeclarationContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_namespaceDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(684);
			match(NAMESPACE);
			setState(685);
			identification();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceBodyContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(KerMLParser.SEMICOLON, 0); }
		public TerminalNode LBRACE() { return getToken(KerMLParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(KerMLParser.RBRACE, 0); }
		public List<NamespaceBodyElementContext> namespaceBodyElement() {
			return getRuleContexts(NamespaceBodyElementContext.class);
		}
		public NamespaceBodyElementContext namespaceBodyElement(int i) {
			return getRuleContext(NamespaceBodyElementContext.class,i);
		}
		public NamespaceBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNamespaceBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNamespaceBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNamespaceBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceBodyContext namespaceBody() throws RecognitionException {
		NamespaceBodyContext _localctx = new NamespaceBodyContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_namespaceBody);
		int _la;
		try {
			setState(696);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEMICOLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(687);
				match(SEMICOLON);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(688);
				match(LBRACE);
				setState(692);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3824098875312969828L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 324320135914979083L) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & 1107689585L) != 0)) {
					{
					{
					setState(689);
					namespaceBodyElement();
					}
					}
					setState(694);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(695);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceBodyElementContext extends ParserRuleContext {
		public NamespaceMemberContext namespaceMember() {
			return getRuleContext(NamespaceMemberContext.class,0);
		}
		public AliasMemberContext aliasMember() {
			return getRuleContext(AliasMemberContext.class,0);
		}
		public Import_Context import_() {
			return getRuleContext(Import_Context.class,0);
		}
		public TerminalNode REGULAR_COMMENT() { return getToken(KerMLParser.REGULAR_COMMENT, 0); }
		public NamespaceBodyElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceBodyElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNamespaceBodyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNamespaceBodyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNamespaceBodyElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceBodyElementContext namespaceBodyElement() throws RecognitionException {
		NamespaceBodyElementContext _localctx = new NamespaceBodyElementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_namespaceBodyElement);
		try {
			setState(702);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(698);
				namespaceMember();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(699);
				aliasMember();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(700);
				import_();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(701);
				match(REGULAR_COMMENT);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MemberPrefixContext extends ParserRuleContext {
		public VisibilityIndicatorContext visibilityIndicator() {
			return getRuleContext(VisibilityIndicatorContext.class,0);
		}
		public MemberPrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberPrefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMemberPrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMemberPrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMemberPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberPrefixContext memberPrefix() throws RecognitionException {
		MemberPrefixContext _localctx = new MemberPrefixContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_memberPrefix);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(705);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & 7L) != 0)) {
				{
				setState(704);
				visibilityIndicator();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VisibilityIndicatorContext extends ParserRuleContext {
		public TerminalNode PUBLIC() { return getToken(KerMLParser.PUBLIC, 0); }
		public TerminalNode PRIVATE() { return getToken(KerMLParser.PRIVATE, 0); }
		public TerminalNode PROTECTED() { return getToken(KerMLParser.PROTECTED, 0); }
		public VisibilityIndicatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_visibilityIndicator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterVisibilityIndicator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitVisibilityIndicator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitVisibilityIndicator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VisibilityIndicatorContext visibilityIndicator() throws RecognitionException {
		VisibilityIndicatorContext _localctx = new VisibilityIndicatorContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_visibilityIndicator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(707);
			_la = _input.LA(1);
			if ( !(((((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & 7L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceMemberContext extends ParserRuleContext {
		public NonFeatureMemberContext nonFeatureMember() {
			return getRuleContext(NonFeatureMemberContext.class,0);
		}
		public NamespaceFeatureMemberContext namespaceFeatureMember() {
			return getRuleContext(NamespaceFeatureMemberContext.class,0);
		}
		public NamespaceMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNamespaceMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNamespaceMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNamespaceMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceMemberContext namespaceMember() throws RecognitionException {
		NamespaceMemberContext _localctx = new NamespaceMemberContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_namespaceMember);
		try {
			setState(711);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(709);
				nonFeatureMember();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(710);
				namespaceFeatureMember();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NonFeatureMemberContext extends ParserRuleContext {
		public MemberPrefixContext memberPrefix() {
			return getRuleContext(MemberPrefixContext.class,0);
		}
		public MemberElementContext memberElement() {
			return getRuleContext(MemberElementContext.class,0);
		}
		public NonFeatureMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonFeatureMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNonFeatureMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNonFeatureMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNonFeatureMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonFeatureMemberContext nonFeatureMember() throws RecognitionException {
		NonFeatureMemberContext _localctx = new NonFeatureMemberContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_nonFeatureMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(713);
			memberPrefix();
			setState(714);
			memberElement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceFeatureMemberContext extends ParserRuleContext {
		public MemberPrefixContext memberPrefix() {
			return getRuleContext(MemberPrefixContext.class,0);
		}
		public FeatureElementContext featureElement() {
			return getRuleContext(FeatureElementContext.class,0);
		}
		public NamespaceFeatureMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceFeatureMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNamespaceFeatureMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNamespaceFeatureMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNamespaceFeatureMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceFeatureMemberContext namespaceFeatureMember() throws RecognitionException {
		NamespaceFeatureMemberContext _localctx = new NamespaceFeatureMemberContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_namespaceFeatureMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(716);
			memberPrefix();
			setState(717);
			featureElement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AliasMemberContext extends ParserRuleContext {
		public Token memberShortName;
		public Token memberName;
		public QualifiedNameContext memberEl;
		public MemberPrefixContext memberPrefix() {
			return getRuleContext(MemberPrefixContext.class,0);
		}
		public TerminalNode ALIAS() { return getToken(KerMLParser.ALIAS, 0); }
		public TerminalNode FOR() { return getToken(KerMLParser.FOR, 0); }
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode LESS() { return getToken(KerMLParser.LESS, 0); }
		public TerminalNode GREATER() { return getToken(KerMLParser.GREATER, 0); }
		public List<TerminalNode> NAME() { return getTokens(KerMLParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(KerMLParser.NAME, i);
		}
		public AliasMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aliasMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterAliasMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitAliasMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitAliasMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasMemberContext aliasMember() throws RecognitionException {
		AliasMemberContext _localctx = new AliasMemberContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_aliasMember);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(719);
			memberPrefix();
			setState(720);
			match(ALIAS);
			setState(724);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LESS) {
				{
				setState(721);
				match(LESS);
				setState(722);
				((AliasMemberContext)_localctx).memberShortName = match(NAME);
				setState(723);
				match(GREATER);
				}
			}

			setState(727);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NAME) {
				{
				setState(726);
				((AliasMemberContext)_localctx).memberName = match(NAME);
				}
			}

			setState(729);
			match(FOR);
			setState(730);
			((AliasMemberContext)_localctx).memberEl = qualifiedName();
			setState(731);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QualifiedNameContext extends ParserRuleContext {
		public List<TerminalNode> NAME() { return getTokens(KerMLParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(KerMLParser.NAME, i);
		}
		public TerminalNode DOLLAR() { return getToken(KerMLParser.DOLLAR, 0); }
		public List<TerminalNode> DOUBLE_COLON() { return getTokens(KerMLParser.DOUBLE_COLON); }
		public TerminalNode DOUBLE_COLON(int i) {
			return getToken(KerMLParser.DOUBLE_COLON, i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterQualifiedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitQualifiedName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitQualifiedName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_qualifiedName);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(735);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOLLAR) {
				{
				setState(733);
				match(DOLLAR);
				setState(734);
				match(DOUBLE_COLON);
				}
			}

			setState(741);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(737);
					match(NAME);
					setState(738);
					match(DOUBLE_COLON);
					}
					} 
				}
				setState(743);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			}
			setState(744);
			match(NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Import_Context extends ParserRuleContext {
		public Token isImportAll;
		public VisibilityIndicatorContext visibilityIndicator() {
			return getRuleContext(VisibilityIndicatorContext.class,0);
		}
		public TerminalNode IMPORT() { return getToken(KerMLParser.IMPORT, 0); }
		public ImportDeclarationContext importDeclaration() {
			return getRuleContext(ImportDeclarationContext.class,0);
		}
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public TerminalNode ALL() { return getToken(KerMLParser.ALL, 0); }
		public Import_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_import_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterImport_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitImport_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitImport_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Import_Context import_() throws RecognitionException {
		Import_Context _localctx = new Import_Context(_ctx, getState());
		enterRule(_localctx, 60, RULE_import_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(746);
			visibilityIndicator();
			setState(747);
			match(IMPORT);
			setState(749);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALL) {
				{
				setState(748);
				((Import_Context)_localctx).isImportAll = match(ALL);
				}
			}

			setState(751);
			importDeclaration();
			setState(752);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImportDeclarationContext extends ParserRuleContext {
		public MembershipImportContext membershipImport() {
			return getRuleContext(MembershipImportContext.class,0);
		}
		public NamespaceImportContext namespaceImport() {
			return getRuleContext(NamespaceImportContext.class,0);
		}
		public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterImportDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitImportDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitImportDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDeclarationContext importDeclaration() throws RecognitionException {
		ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_importDeclaration);
		try {
			setState(756);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(754);
				membershipImport();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(755);
				namespaceImport();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MembershipImportContext extends ParserRuleContext {
		public QualifiedNameContext importedMembership;
		public Token isRecursive;
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode DOUBLE_COLON() { return getToken(KerMLParser.DOUBLE_COLON, 0); }
		public TerminalNode DOUBLE_STAR() { return getToken(KerMLParser.DOUBLE_STAR, 0); }
		public MembershipImportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_membershipImport; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMembershipImport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMembershipImport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMembershipImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MembershipImportContext membershipImport() throws RecognitionException {
		MembershipImportContext _localctx = new MembershipImportContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_membershipImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(758);
			((MembershipImportContext)_localctx).importedMembership = qualifiedName();
			setState(761);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOUBLE_COLON) {
				{
				setState(759);
				match(DOUBLE_COLON);
				setState(760);
				((MembershipImportContext)_localctx).isRecursive = match(DOUBLE_STAR);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamespaceImportContext extends ParserRuleContext {
		public Token isRecursive;
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public List<TerminalNode> DOUBLE_COLON() { return getTokens(KerMLParser.DOUBLE_COLON); }
		public TerminalNode DOUBLE_COLON(int i) {
			return getToken(KerMLParser.DOUBLE_COLON, i);
		}
		public TerminalNode STAR() { return getToken(KerMLParser.STAR, 0); }
		public TerminalNode DOUBLE_STAR() { return getToken(KerMLParser.DOUBLE_STAR, 0); }
		public FilterPackageImportContext filterPackageImport() {
			return getRuleContext(FilterPackageImportContext.class,0);
		}
		public NamespaceImportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceImport; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNamespaceImport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNamespaceImport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNamespaceImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceImportContext namespaceImport() throws RecognitionException {
		NamespaceImportContext _localctx = new NamespaceImportContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_namespaceImport);
		int _la;
		try {
			setState(771);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(763);
				qualifiedName();
				setState(764);
				match(DOUBLE_COLON);
				setState(765);
				match(STAR);
				setState(768);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DOUBLE_COLON) {
					{
					setState(766);
					match(DOUBLE_COLON);
					setState(767);
					((NamespaceImportContext)_localctx).isRecursive = match(DOUBLE_STAR);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(770);
				filterPackageImport();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FilterPackageImportContext extends ParserRuleContext {
		public Token isRecursive;
		public MembershipImportContext membershipImport() {
			return getRuleContext(MembershipImportContext.class,0);
		}
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public List<TerminalNode> DOUBLE_COLON() { return getTokens(KerMLParser.DOUBLE_COLON); }
		public TerminalNode DOUBLE_COLON(int i) {
			return getToken(KerMLParser.DOUBLE_COLON, i);
		}
		public TerminalNode STAR() { return getToken(KerMLParser.STAR, 0); }
		public List<FilterPackageMemberContext> filterPackageMember() {
			return getRuleContexts(FilterPackageMemberContext.class);
		}
		public FilterPackageMemberContext filterPackageMember(int i) {
			return getRuleContext(FilterPackageMemberContext.class,i);
		}
		public TerminalNode DOUBLE_STAR() { return getToken(KerMLParser.DOUBLE_STAR, 0); }
		public FilterPackageImportContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filterPackageImport; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFilterPackageImport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFilterPackageImport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFilterPackageImport(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterPackageImportContext filterPackageImport() throws RecognitionException {
		FilterPackageImportContext _localctx = new FilterPackageImportContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_filterPackageImport);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(781);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(773);
				membershipImport();
				}
				break;
			case 2:
				{
				setState(774);
				qualifiedName();
				setState(775);
				match(DOUBLE_COLON);
				setState(776);
				match(STAR);
				setState(779);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DOUBLE_COLON) {
					{
					setState(777);
					match(DOUBLE_COLON);
					setState(778);
					((FilterPackageImportContext)_localctx).isRecursive = match(DOUBLE_STAR);
					}
				}

				}
				break;
			}
			setState(784); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(783);
				filterPackageMember();
				}
				}
				setState(786); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==LBRACKET );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FilterPackageMemberContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(KerMLParser.LBRACKET, 0); }
		public OwnedExpressionContext ownedExpression() {
			return getRuleContext(OwnedExpressionContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(KerMLParser.RBRACKET, 0); }
		public FilterPackageMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filterPackageMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFilterPackageMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFilterPackageMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFilterPackageMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterPackageMemberContext filterPackageMember() throws RecognitionException {
		FilterPackageMemberContext _localctx = new FilterPackageMemberContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_filterPackageMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(788);
			match(LBRACKET);
			setState(789);
			ownedExpression(0);
			setState(790);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MemberElementContext extends ParserRuleContext {
		public AnnotatingElementContext annotatingElement() {
			return getRuleContext(AnnotatingElementContext.class,0);
		}
		public NonFeatureElementContext nonFeatureElement() {
			return getRuleContext(NonFeatureElementContext.class,0);
		}
		public MemberElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMemberElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMemberElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMemberElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberElementContext memberElement() throws RecognitionException {
		MemberElementContext _localctx = new MemberElementContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_memberElement);
		try {
			setState(794);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(792);
				annotatingElement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(793);
				nonFeatureElement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NonFeatureElementContext extends ParserRuleContext {
		public DependencyContext dependency() {
			return getRuleContext(DependencyContext.class,0);
		}
		public NamespaceContext namespace() {
			return getRuleContext(NamespaceContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ClassifierContext classifier() {
			return getRuleContext(ClassifierContext.class,0);
		}
		public DatatypeContext datatype() {
			return getRuleContext(DatatypeContext.class,0);
		}
		public ClassContext class_() {
			return getRuleContext(ClassContext.class,0);
		}
		public StructureContext structure() {
			return getRuleContext(StructureContext.class,0);
		}
		public MetaclassContext metaclass() {
			return getRuleContext(MetaclassContext.class,0);
		}
		public AssociationContext association() {
			return getRuleContext(AssociationContext.class,0);
		}
		public AssociationStructureContext associationStructure() {
			return getRuleContext(AssociationStructureContext.class,0);
		}
		public InteractionContext interaction() {
			return getRuleContext(InteractionContext.class,0);
		}
		public BehaviorContext behavior() {
			return getRuleContext(BehaviorContext.class,0);
		}
		public FunctionContext function() {
			return getRuleContext(FunctionContext.class,0);
		}
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public MultiplicityContext multiplicity() {
			return getRuleContext(MultiplicityContext.class,0);
		}
		public PackageContext package_() {
			return getRuleContext(PackageContext.class,0);
		}
		public LibraryPackageContext libraryPackage() {
			return getRuleContext(LibraryPackageContext.class,0);
		}
		public RenderingContext rendering() {
			return getRuleContext(RenderingContext.class,0);
		}
		public SpecializationContext specialization() {
			return getRuleContext(SpecializationContext.class,0);
		}
		public ConjugationContext conjugation() {
			return getRuleContext(ConjugationContext.class,0);
		}
		public SubclassificationContext subclassification() {
			return getRuleContext(SubclassificationContext.class,0);
		}
		public DisjoiningContext disjoining() {
			return getRuleContext(DisjoiningContext.class,0);
		}
		public FeatureInvertingContext featureInverting() {
			return getRuleContext(FeatureInvertingContext.class,0);
		}
		public FeatureTypingContext featureTyping() {
			return getRuleContext(FeatureTypingContext.class,0);
		}
		public SubsettingContext subsetting() {
			return getRuleContext(SubsettingContext.class,0);
		}
		public RedefinitionContext redefinition() {
			return getRuleContext(RedefinitionContext.class,0);
		}
		public TypeFeaturingContext typeFeaturing() {
			return getRuleContext(TypeFeaturingContext.class,0);
		}
		public ViewContext view() {
			return getRuleContext(ViewContext.class,0);
		}
		public ViewpointContext viewpoint() {
			return getRuleContext(ViewpointContext.class,0);
		}
		public NonFeatureElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonFeatureElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNonFeatureElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNonFeatureElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNonFeatureElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonFeatureElementContext nonFeatureElement() throws RecognitionException {
		NonFeatureElementContext _localctx = new NonFeatureElementContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_nonFeatureElement);
		try {
			setState(825);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(796);
				dependency();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(797);
				namespace();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(798);
				type();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(799);
				classifier();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(800);
				datatype();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(801);
				class_();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(802);
				structure();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(803);
				metaclass();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(804);
				association();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(805);
				associationStructure();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(806);
				interaction();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(807);
				behavior();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(808);
				function();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(809);
				predicate();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(810);
				multiplicity();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(811);
				package_();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(812);
				libraryPackage();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(813);
				rendering();
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(814);
				specialization();
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(815);
				conjugation();
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(816);
				subclassification();
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(817);
				disjoining();
				}
				break;
			case 23:
				enterOuterAlt(_localctx, 23);
				{
				setState(818);
				featureInverting();
				}
				break;
			case 24:
				enterOuterAlt(_localctx, 24);
				{
				setState(819);
				featureTyping();
				}
				break;
			case 25:
				enterOuterAlt(_localctx, 25);
				{
				setState(820);
				subsetting();
				}
				break;
			case 26:
				enterOuterAlt(_localctx, 26);
				{
				setState(821);
				redefinition();
				}
				break;
			case 27:
				enterOuterAlt(_localctx, 27);
				{
				setState(822);
				typeFeaturing();
				}
				break;
			case 28:
				enterOuterAlt(_localctx, 28);
				{
				setState(823);
				view();
				}
				break;
			case 29:
				enterOuterAlt(_localctx, 29);
				{
				setState(824);
				viewpoint();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureElementContext extends ParserRuleContext {
		public FeatureContext feature() {
			return getRuleContext(FeatureContext.class,0);
		}
		public StepContext step() {
			return getRuleContext(StepContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public InvariantContext invariant() {
			return getRuleContext(InvariantContext.class,0);
		}
		public ConnectorContext connector() {
			return getRuleContext(ConnectorContext.class,0);
		}
		public BindingConnectorContext bindingConnector() {
			return getRuleContext(BindingConnectorContext.class,0);
		}
		public SuccessionContext succession() {
			return getRuleContext(SuccessionContext.class,0);
		}
		public FlowContext flow() {
			return getRuleContext(FlowContext.class,0);
		}
		public SuccessionFlowContext successionFlow() {
			return getRuleContext(SuccessionFlowContext.class,0);
		}
		public FeatureElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureElementContext featureElement() throws RecognitionException {
		FeatureElementContext _localctx = new FeatureElementContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_featureElement);
		try {
			setState(837);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(827);
				feature();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(828);
				step();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(829);
				expression();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(830);
				booleanExpression();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(831);
				invariant();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(832);
				connector();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(833);
				bindingConnector();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(834);
				succession();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(835);
				flow();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(836);
				successionFlow();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode TYPE() { return getToken(KerMLParser.TYPE, 0); }
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(839);
			typePrefix();
			setState(840);
			match(TYPE);
			setState(841);
			typeDeclaration();
			setState(842);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypePrefixContext extends ParserRuleContext {
		public Token isAbstract;
		public List<PrefixMetadataMemberContext> prefixMetadataMember() {
			return getRuleContexts(PrefixMetadataMemberContext.class);
		}
		public PrefixMetadataMemberContext prefixMetadataMember(int i) {
			return getRuleContext(PrefixMetadataMemberContext.class,i);
		}
		public TerminalNode ABSTRACT() { return getToken(KerMLParser.ABSTRACT, 0); }
		public TypePrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typePrefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypePrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypePrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypePrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypePrefixContext typePrefix() throws RecognitionException {
		TypePrefixContext _localctx = new TypePrefixContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_typePrefix);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(845);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ABSTRACT) {
				{
				setState(844);
				((TypePrefixContext)_localctx).isAbstract = match(ABSTRACT);
				}
			}

			setState(850);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==HASH) {
				{
				{
				setState(847);
				prefixMetadataMember();
				}
				}
				setState(852);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeDeclarationContext extends ParserRuleContext {
		public Token isSufficient;
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public OwnedMultiplicityContext ownedMultiplicity() {
			return getRuleContext(OwnedMultiplicityContext.class,0);
		}
		public List<SpecializationPartContext> specializationPart() {
			return getRuleContexts(SpecializationPartContext.class);
		}
		public SpecializationPartContext specializationPart(int i) {
			return getRuleContext(SpecializationPartContext.class,i);
		}
		public List<ConjugationPartContext> conjugationPart() {
			return getRuleContexts(ConjugationPartContext.class);
		}
		public ConjugationPartContext conjugationPart(int i) {
			return getRuleContext(ConjugationPartContext.class,i);
		}
		public List<TypeRelationshipPartContext> typeRelationshipPart() {
			return getRuleContexts(TypeRelationshipPartContext.class);
		}
		public TypeRelationshipPartContext typeRelationshipPart(int i) {
			return getRuleContext(TypeRelationshipPartContext.class,i);
		}
		public TerminalNode ALL() { return getToken(KerMLParser.ALL, 0); }
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_typeDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(854);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALL) {
				{
				setState(853);
				((TypeDeclarationContext)_localctx).isSufficient = match(ALL);
				}
			}

			setState(856);
			identification();
			setState(858);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACKET) {
				{
				setState(857);
				ownedMultiplicity();
				}
			}

			setState(862); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(862);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case SPECIALIZES:
				case COLON_GT:
					{
					setState(860);
					specializationPart();
					}
					break;
				case CONJUGATES:
				case TILDE:
					{
					setState(861);
					conjugationPart();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(864); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==CONJUGATES || ((((_la - 88)) & ~0x3f) == 0 && ((1L << (_la - 88)) & 35186519572481L) != 0) );
			setState(869);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4503600298459136L) != 0) || _la==UNIONS) {
				{
				{
				setState(866);
				typeRelationshipPart();
				}
				}
				setState(871);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SpecializationPartContext extends ParserRuleContext {
		public SpecializesTokenContext specializesToken() {
			return getRuleContext(SpecializesTokenContext.class,0);
		}
		public List<OwnedSpecializationContext> ownedSpecialization() {
			return getRuleContexts(OwnedSpecializationContext.class);
		}
		public OwnedSpecializationContext ownedSpecialization(int i) {
			return getRuleContext(OwnedSpecializationContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public SpecializationPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specializationPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSpecializationPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSpecializationPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSpecializationPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpecializationPartContext specializationPart() throws RecognitionException {
		SpecializationPartContext _localctx = new SpecializationPartContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_specializationPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(872);
			specializesToken();
			setState(873);
			ownedSpecialization();
			setState(878);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(874);
				match(COMMA);
				setState(875);
				ownedSpecialization();
				}
				}
				setState(880);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConjugationPartContext extends ParserRuleContext {
		public ConjugatesTokenContext conjugatesToken() {
			return getRuleContext(ConjugatesTokenContext.class,0);
		}
		public OwnedConjugationContext ownedConjugation() {
			return getRuleContext(OwnedConjugationContext.class,0);
		}
		public ConjugationPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjugationPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterConjugationPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitConjugationPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitConjugationPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConjugationPartContext conjugationPart() throws RecognitionException {
		ConjugationPartContext _localctx = new ConjugationPartContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_conjugationPart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(881);
			conjugatesToken();
			setState(882);
			ownedConjugation();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeRelationshipPartContext extends ParserRuleContext {
		public DisjoiningPartContext disjoiningPart() {
			return getRuleContext(DisjoiningPartContext.class,0);
		}
		public UnioningPartContext unioningPart() {
			return getRuleContext(UnioningPartContext.class,0);
		}
		public IntersectingPartContext intersectingPart() {
			return getRuleContext(IntersectingPartContext.class,0);
		}
		public DifferencingPartContext differencingPart() {
			return getRuleContext(DifferencingPartContext.class,0);
		}
		public TypeRelationshipPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeRelationshipPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypeRelationshipPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypeRelationshipPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypeRelationshipPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeRelationshipPartContext typeRelationshipPart() throws RecognitionException {
		TypeRelationshipPartContext _localctx = new TypeRelationshipPartContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_typeRelationshipPart);
		try {
			setState(888);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DISJOINT:
				enterOuterAlt(_localctx, 1);
				{
				setState(884);
				disjoiningPart();
				}
				break;
			case UNIONS:
				enterOuterAlt(_localctx, 2);
				{
				setState(885);
				unioningPart();
				}
				break;
			case INTERSECTS:
				enterOuterAlt(_localctx, 3);
				{
				setState(886);
				intersectingPart();
				}
				break;
			case DIFFERENCES:
				enterOuterAlt(_localctx, 4);
				{
				setState(887);
				differencingPart();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DisjoiningPartContext extends ParserRuleContext {
		public TerminalNode DISJOINT() { return getToken(KerMLParser.DISJOINT, 0); }
		public TerminalNode FROM() { return getToken(KerMLParser.FROM, 0); }
		public List<OwnedDisjoiningContext> ownedDisjoining() {
			return getRuleContexts(OwnedDisjoiningContext.class);
		}
		public OwnedDisjoiningContext ownedDisjoining(int i) {
			return getRuleContext(OwnedDisjoiningContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public DisjoiningPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjoiningPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterDisjoiningPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitDisjoiningPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitDisjoiningPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjoiningPartContext disjoiningPart() throws RecognitionException {
		DisjoiningPartContext _localctx = new DisjoiningPartContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_disjoiningPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(890);
			match(DISJOINT);
			setState(891);
			match(FROM);
			setState(892);
			ownedDisjoining();
			setState(897);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(893);
				match(COMMA);
				setState(894);
				ownedDisjoining();
				}
				}
				setState(899);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnioningPartContext extends ParserRuleContext {
		public TerminalNode UNIONS() { return getToken(KerMLParser.UNIONS, 0); }
		public List<UnioningContext> unioning() {
			return getRuleContexts(UnioningContext.class);
		}
		public UnioningContext unioning(int i) {
			return getRuleContext(UnioningContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public UnioningPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unioningPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterUnioningPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitUnioningPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitUnioningPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnioningPartContext unioningPart() throws RecognitionException {
		UnioningPartContext _localctx = new UnioningPartContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_unioningPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(900);
			match(UNIONS);
			setState(901);
			unioning();
			setState(906);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(902);
				match(COMMA);
				setState(903);
				unioning();
				}
				}
				setState(908);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IntersectingPartContext extends ParserRuleContext {
		public TerminalNode INTERSECTS() { return getToken(KerMLParser.INTERSECTS, 0); }
		public List<IntersectingContext> intersecting() {
			return getRuleContexts(IntersectingContext.class);
		}
		public IntersectingContext intersecting(int i) {
			return getRuleContext(IntersectingContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public IntersectingPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intersectingPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterIntersectingPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitIntersectingPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitIntersectingPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntersectingPartContext intersectingPart() throws RecognitionException {
		IntersectingPartContext _localctx = new IntersectingPartContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_intersectingPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(909);
			match(INTERSECTS);
			setState(910);
			intersecting();
			setState(915);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(911);
				match(COMMA);
				setState(912);
				intersecting();
				}
				}
				setState(917);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DifferencingPartContext extends ParserRuleContext {
		public TerminalNode DIFFERENCES() { return getToken(KerMLParser.DIFFERENCES, 0); }
		public List<DifferencingContext> differencing() {
			return getRuleContexts(DifferencingContext.class);
		}
		public DifferencingContext differencing(int i) {
			return getRuleContext(DifferencingContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public DifferencingPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_differencingPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterDifferencingPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitDifferencingPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitDifferencingPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DifferencingPartContext differencingPart() throws RecognitionException {
		DifferencingPartContext _localctx = new DifferencingPartContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_differencingPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(918);
			match(DIFFERENCES);
			setState(919);
			differencing();
			setState(924);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(920);
				match(COMMA);
				setState(921);
				differencing();
				}
				}
				setState(926);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeBodyContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(KerMLParser.SEMICOLON, 0); }
		public TerminalNode LBRACE() { return getToken(KerMLParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(KerMLParser.RBRACE, 0); }
		public List<TypeBodyElementContext> typeBodyElement() {
			return getRuleContexts(TypeBodyElementContext.class);
		}
		public TypeBodyElementContext typeBodyElement(int i) {
			return getRuleContext(TypeBodyElementContext.class,i);
		}
		public TypeBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypeBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypeBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypeBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeBodyContext typeBody() throws RecognitionException {
		TypeBodyContext _localctx = new TypeBodyContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_typeBody);
		int _la;
		try {
			setState(936);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEMICOLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(927);
				match(SEMICOLON);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(928);
				match(LBRACE);
				setState(932);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2671177370706122852L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 324320135914979083L) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & 1107689585L) != 0)) {
					{
					{
					setState(929);
					typeBodyElement();
					}
					}
					setState(934);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(935);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeBodyElementContext extends ParserRuleContext {
		public NonFeatureMemberContext nonFeatureMember() {
			return getRuleContext(NonFeatureMemberContext.class,0);
		}
		public FeatureMemberContext featureMember() {
			return getRuleContext(FeatureMemberContext.class,0);
		}
		public AliasMemberContext aliasMember() {
			return getRuleContext(AliasMemberContext.class,0);
		}
		public Import_Context import_() {
			return getRuleContext(Import_Context.class,0);
		}
		public TerminalNode REGULAR_COMMENT() { return getToken(KerMLParser.REGULAR_COMMENT, 0); }
		public TypeBodyElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeBodyElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypeBodyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypeBodyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypeBodyElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeBodyElementContext typeBodyElement() throws RecognitionException {
		TypeBodyElementContext _localctx = new TypeBodyElementContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_typeBodyElement);
		try {
			setState(943);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(938);
				nonFeatureMember();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(939);
				featureMember();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(940);
				aliasMember();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(941);
				import_();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(942);
				match(REGULAR_COMMENT);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SpecializationContext extends ParserRuleContext {
		public TerminalNode SUBTYPE() { return getToken(KerMLParser.SUBTYPE, 0); }
		public SpecificTypeContext specificType() {
			return getRuleContext(SpecificTypeContext.class,0);
		}
		public SpecializesTokenContext specializesToken() {
			return getRuleContext(SpecializesTokenContext.class,0);
		}
		public GeneralTypeContext generalType() {
			return getRuleContext(GeneralTypeContext.class,0);
		}
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public TerminalNode SPECIALIZATION() { return getToken(KerMLParser.SPECIALIZATION, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public SpecializationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specialization; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSpecialization(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSpecialization(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSpecialization(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpecializationContext specialization() throws RecognitionException {
		SpecializationContext _localctx = new SpecializationContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_specialization);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(947);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SPECIALIZATION) {
				{
				setState(945);
				match(SPECIALIZATION);
				setState(946);
				identification();
				}
			}

			setState(949);
			match(SUBTYPE);
			setState(950);
			specificType();
			setState(951);
			specializesToken();
			setState(952);
			generalType();
			setState(953);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedSpecializationContext extends ParserRuleContext {
		public GeneralTypeContext generalType() {
			return getRuleContext(GeneralTypeContext.class,0);
		}
		public OwnedSpecializationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedSpecialization; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedSpecialization(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedSpecialization(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedSpecialization(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedSpecializationContext ownedSpecialization() throws RecognitionException {
		OwnedSpecializationContext _localctx = new OwnedSpecializationContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_ownedSpecialization);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(955);
			generalType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SpecificTypeContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public OwnedFeatureChainContext ownedFeatureChain() {
			return getRuleContext(OwnedFeatureChainContext.class,0);
		}
		public SpecificTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specificType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSpecificType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSpecificType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSpecificType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpecificTypeContext specificType() throws RecognitionException {
		SpecificTypeContext _localctx = new SpecificTypeContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_specificType);
		try {
			setState(959);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(957);
				qualifiedName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(958);
				ownedFeatureChain();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GeneralTypeContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public OwnedFeatureChainContext ownedFeatureChain() {
			return getRuleContext(OwnedFeatureChainContext.class,0);
		}
		public GeneralTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_generalType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterGeneralType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitGeneralType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitGeneralType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GeneralTypeContext generalType() throws RecognitionException {
		GeneralTypeContext _localctx = new GeneralTypeContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_generalType);
		try {
			setState(963);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(961);
				qualifiedName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(962);
				ownedFeatureChain();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConjugationContext extends ParserRuleContext {
		public TerminalNode CONJUGATE() { return getToken(KerMLParser.CONJUGATE, 0); }
		public ConjugatesTokenContext conjugatesToken() {
			return getRuleContext(ConjugatesTokenContext.class,0);
		}
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public List<FeatureChainContext> featureChain() {
			return getRuleContexts(FeatureChainContext.class);
		}
		public FeatureChainContext featureChain(int i) {
			return getRuleContext(FeatureChainContext.class,i);
		}
		public TerminalNode CONJUGATION() { return getToken(KerMLParser.CONJUGATION, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public ConjugationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjugation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterConjugation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitConjugation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitConjugation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConjugationContext conjugation() throws RecognitionException {
		ConjugationContext _localctx = new ConjugationContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_conjugation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(967);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CONJUGATION) {
				{
				setState(965);
				match(CONJUGATION);
				setState(966);
				identification();
				}
			}

			setState(969);
			match(CONJUGATE);
			setState(972);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
			case 1:
				{
				setState(970);
				qualifiedName();
				}
				break;
			case 2:
				{
				setState(971);
				featureChain();
				}
				break;
			}
			setState(974);
			conjugatesToken();
			setState(977);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(975);
				qualifiedName();
				}
				break;
			case 2:
				{
				setState(976);
				featureChain();
				}
				break;
			}
			setState(979);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedConjugationContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public FeatureChainContext featureChain() {
			return getRuleContext(FeatureChainContext.class,0);
		}
		public OwnedConjugationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedConjugation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedConjugation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedConjugation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedConjugation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedConjugationContext ownedConjugation() throws RecognitionException {
		OwnedConjugationContext _localctx = new OwnedConjugationContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_ownedConjugation);
		try {
			setState(983);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(981);
				qualifiedName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(982);
				featureChain();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DisjoiningContext extends ParserRuleContext {
		public TerminalNode DISJOINT() { return getToken(KerMLParser.DISJOINT, 0); }
		public TerminalNode FROM() { return getToken(KerMLParser.FROM, 0); }
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public List<FeatureChainContext> featureChain() {
			return getRuleContexts(FeatureChainContext.class);
		}
		public FeatureChainContext featureChain(int i) {
			return getRuleContext(FeatureChainContext.class,i);
		}
		public TerminalNode DISJOINING() { return getToken(KerMLParser.DISJOINING, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public DisjoiningContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjoining; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterDisjoining(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitDisjoining(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitDisjoining(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjoiningContext disjoining() throws RecognitionException {
		DisjoiningContext _localctx = new DisjoiningContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_disjoining);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(987);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DISJOINING) {
				{
				setState(985);
				match(DISJOINING);
				setState(986);
				identification();
				}
			}

			setState(989);
			match(DISJOINT);
			setState(992);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,65,_ctx) ) {
			case 1:
				{
				setState(990);
				qualifiedName();
				}
				break;
			case 2:
				{
				setState(991);
				featureChain();
				}
				break;
			}
			setState(994);
			match(FROM);
			setState(997);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				{
				setState(995);
				qualifiedName();
				}
				break;
			case 2:
				{
				setState(996);
				featureChain();
				}
				break;
			}
			setState(999);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedDisjoiningContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public FeatureChainContext featureChain() {
			return getRuleContext(FeatureChainContext.class,0);
		}
		public OwnedDisjoiningContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedDisjoining; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedDisjoining(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedDisjoining(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedDisjoining(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedDisjoiningContext ownedDisjoining() throws RecognitionException {
		OwnedDisjoiningContext _localctx = new OwnedDisjoiningContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_ownedDisjoining);
		try {
			setState(1003);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1001);
				qualifiedName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1002);
				featureChain();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnioningContext extends ParserRuleContext {
		public QualifiedNameContext unioningType;
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public OwnedFeatureChainContext ownedFeatureChain() {
			return getRuleContext(OwnedFeatureChainContext.class,0);
		}
		public UnioningContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unioning; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterUnioning(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitUnioning(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitUnioning(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnioningContext unioning() throws RecognitionException {
		UnioningContext _localctx = new UnioningContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_unioning);
		try {
			setState(1007);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1005);
				((UnioningContext)_localctx).unioningType = qualifiedName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1006);
				ownedFeatureChain();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IntersectingContext extends ParserRuleContext {
		public QualifiedNameContext intersectingType;
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public OwnedFeatureChainContext ownedFeatureChain() {
			return getRuleContext(OwnedFeatureChainContext.class,0);
		}
		public IntersectingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intersecting; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterIntersecting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitIntersecting(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitIntersecting(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntersectingContext intersecting() throws RecognitionException {
		IntersectingContext _localctx = new IntersectingContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_intersecting);
		try {
			setState(1011);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1009);
				((IntersectingContext)_localctx).intersectingType = qualifiedName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1010);
				ownedFeatureChain();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DifferencingContext extends ParserRuleContext {
		public QualifiedNameContext differencingType;
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public OwnedFeatureChainContext ownedFeatureChain() {
			return getRuleContext(OwnedFeatureChainContext.class,0);
		}
		public DifferencingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_differencing; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterDifferencing(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitDifferencing(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitDifferencing(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DifferencingContext differencing() throws RecognitionException {
		DifferencingContext _localctx = new DifferencingContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_differencing);
		try {
			setState(1015);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,70,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1013);
				((DifferencingContext)_localctx).differencingType = qualifiedName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1014);
				ownedFeatureChain();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureMemberContext extends ParserRuleContext {
		public TypeFeatureMemberContext typeFeatureMember() {
			return getRuleContext(TypeFeatureMemberContext.class,0);
		}
		public OwnedFeatureMemberContext ownedFeatureMember() {
			return getRuleContext(OwnedFeatureMemberContext.class,0);
		}
		public FeatureMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureMemberContext featureMember() throws RecognitionException {
		FeatureMemberContext _localctx = new FeatureMemberContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_featureMember);
		try {
			setState(1019);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1017);
				typeFeatureMember();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1018);
				ownedFeatureMember();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeFeatureMemberContext extends ParserRuleContext {
		public MemberPrefixContext memberPrefix() {
			return getRuleContext(MemberPrefixContext.class,0);
		}
		public TerminalNode MEMBER() { return getToken(KerMLParser.MEMBER, 0); }
		public FeatureElementContext featureElement() {
			return getRuleContext(FeatureElementContext.class,0);
		}
		public TypeFeatureMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeFeatureMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypeFeatureMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypeFeatureMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypeFeatureMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeFeatureMemberContext typeFeatureMember() throws RecognitionException {
		TypeFeatureMemberContext _localctx = new TypeFeatureMemberContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_typeFeatureMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1021);
			memberPrefix();
			setState(1022);
			match(MEMBER);
			setState(1023);
			featureElement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedFeatureMemberContext extends ParserRuleContext {
		public MemberPrefixContext memberPrefix() {
			return getRuleContext(MemberPrefixContext.class,0);
		}
		public FeatureElementContext featureElement() {
			return getRuleContext(FeatureElementContext.class,0);
		}
		public OwnedFeatureMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedFeatureMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedFeatureMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedFeatureMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedFeatureMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedFeatureMemberContext ownedFeatureMember() throws RecognitionException {
		OwnedFeatureMemberContext _localctx = new OwnedFeatureMemberContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_ownedFeatureMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1025);
			memberPrefix();
			setState(1026);
			featureElement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassifierContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode CLASSIFIER() { return getToken(KerMLParser.CLASSIFIER, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public ClassifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterClassifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitClassifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitClassifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierContext classifier() throws RecognitionException {
		ClassifierContext _localctx = new ClassifierContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_classifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1028);
			typePrefix();
			setState(1029);
			match(CLASSIFIER);
			setState(1030);
			classifierDeclaration();
			setState(1031);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassifierDeclarationContext extends ParserRuleContext {
		public Token isSufficient;
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public OwnedMultiplicityContext ownedMultiplicity() {
			return getRuleContext(OwnedMultiplicityContext.class,0);
		}
		public SuperclassingPartContext superclassingPart() {
			return getRuleContext(SuperclassingPartContext.class,0);
		}
		public ConjugationPartContext conjugationPart() {
			return getRuleContext(ConjugationPartContext.class,0);
		}
		public List<TypeRelationshipPartContext> typeRelationshipPart() {
			return getRuleContexts(TypeRelationshipPartContext.class);
		}
		public TypeRelationshipPartContext typeRelationshipPart(int i) {
			return getRuleContext(TypeRelationshipPartContext.class,i);
		}
		public TerminalNode ALL() { return getToken(KerMLParser.ALL, 0); }
		public ClassifierDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classifierDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterClassifierDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitClassifierDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitClassifierDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassifierDeclarationContext classifierDeclaration() throws RecognitionException {
		ClassifierDeclarationContext _localctx = new ClassifierDeclarationContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_classifierDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1034);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALL) {
				{
				setState(1033);
				((ClassifierDeclarationContext)_localctx).isSufficient = match(ALL);
				}
			}

			setState(1036);
			identification();
			setState(1038);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACKET) {
				{
				setState(1037);
				ownedMultiplicity();
				}
			}

			setState(1042);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SPECIALIZES:
			case COLON_GT:
				{
				setState(1040);
				superclassingPart();
				}
				break;
			case CONJUGATES:
			case TILDE:
				{
				setState(1041);
				conjugationPart();
				}
				break;
			case DIFFERENCES:
			case DISJOINT:
			case INTERSECTS:
			case UNIONS:
			case LBRACE:
			case SEMICOLON:
				break;
			default:
				break;
			}
			setState(1047);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4503600298459136L) != 0) || _la==UNIONS) {
				{
				{
				setState(1044);
				typeRelationshipPart();
				}
				}
				setState(1049);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SuperclassingPartContext extends ParserRuleContext {
		public SpecializesTokenContext specializesToken() {
			return getRuleContext(SpecializesTokenContext.class,0);
		}
		public List<OwnedSubclassificationContext> ownedSubclassification() {
			return getRuleContexts(OwnedSubclassificationContext.class);
		}
		public OwnedSubclassificationContext ownedSubclassification(int i) {
			return getRuleContext(OwnedSubclassificationContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public SuperclassingPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_superclassingPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSuperclassingPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSuperclassingPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSuperclassingPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SuperclassingPartContext superclassingPart() throws RecognitionException {
		SuperclassingPartContext _localctx = new SuperclassingPartContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_superclassingPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1050);
			specializesToken();
			setState(1051);
			ownedSubclassification();
			setState(1056);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1052);
				match(COMMA);
				setState(1053);
				ownedSubclassification();
				}
				}
				setState(1058);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubclassificationContext extends ParserRuleContext {
		public QualifiedNameContext subclassifier;
		public QualifiedNameContext superclassifier;
		public TerminalNode SUBCLASSIFIER() { return getToken(KerMLParser.SUBCLASSIFIER, 0); }
		public SpecializesTokenContext specializesToken() {
			return getRuleContext(SpecializesTokenContext.class,0);
		}
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public TerminalNode SPECIALIZATION() { return getToken(KerMLParser.SPECIALIZATION, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public SubclassificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subclassification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSubclassification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSubclassification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSubclassification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubclassificationContext subclassification() throws RecognitionException {
		SubclassificationContext _localctx = new SubclassificationContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_subclassification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1061);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SPECIALIZATION) {
				{
				setState(1059);
				match(SPECIALIZATION);
				setState(1060);
				identification();
				}
			}

			setState(1063);
			match(SUBCLASSIFIER);
			setState(1064);
			((SubclassificationContext)_localctx).subclassifier = qualifiedName();
			setState(1065);
			specializesToken();
			setState(1066);
			((SubclassificationContext)_localctx).superclassifier = qualifiedName();
			setState(1067);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedSubclassificationContext extends ParserRuleContext {
		public QualifiedNameContext superclassifier;
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public OwnedSubclassificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedSubclassification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedSubclassification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedSubclassification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedSubclassification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedSubclassificationContext ownedSubclassification() throws RecognitionException {
		OwnedSubclassificationContext _localctx = new OwnedSubclassificationContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_ownedSubclassification);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1069);
			((OwnedSubclassificationContext)_localctx).superclassifier = qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureContext extends ParserRuleContext {
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public FeaturePrefixContext featurePrefix() {
			return getRuleContext(FeaturePrefixContext.class,0);
		}
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public ValuePartContext valuePart() {
			return getRuleContext(ValuePartContext.class,0);
		}
		public TerminalNode FEATURE() { return getToken(KerMLParser.FEATURE, 0); }
		public PrefixMetadataMemberContext prefixMetadataMember() {
			return getRuleContext(PrefixMetadataMemberContext.class,0);
		}
		public EndFeaturePrefixContext endFeaturePrefix() {
			return getRuleContext(EndFeaturePrefixContext.class,0);
		}
		public BasicFeaturePrefixContext basicFeaturePrefix() {
			return getRuleContext(BasicFeaturePrefixContext.class,0);
		}
		public FeatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_feature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureContext feature() throws RecognitionException {
		FeatureContext _localctx = new FeatureContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_feature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1085);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				{
				setState(1071);
				featurePrefix();
				setState(1074);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case FEATURE:
					{
					setState(1072);
					match(FEATURE);
					}
					break;
				case HASH:
					{
					setState(1073);
					prefixMetadataMember();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1077);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4456464L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4652225029457616929L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 67133441L) != 0)) {
					{
					setState(1076);
					featureDeclaration();
					}
				}

				}
				break;
			case 2:
				{
				setState(1081);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
				case 1:
					{
					setState(1079);
					endFeaturePrefix();
					}
					break;
				case 2:
					{
					setState(1080);
					basicFeaturePrefix();
					}
					break;
				}
				setState(1083);
				featureDeclaration();
				}
				break;
			}
			setState(1088);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT || _la==COLON_EQUALS || _la==EQUALS) {
				{
				setState(1087);
				valuePart();
				}
			}

			setState(1090);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EndFeaturePrefixContext extends ParserRuleContext {
		public Token isConstant;
		public Token isEnd;
		public TerminalNode END() { return getToken(KerMLParser.END, 0); }
		public TerminalNode CONST() { return getToken(KerMLParser.CONST, 0); }
		public EndFeaturePrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endFeaturePrefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterEndFeaturePrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitEndFeaturePrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitEndFeaturePrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EndFeaturePrefixContext endFeaturePrefix() throws RecognitionException {
		EndFeaturePrefixContext _localctx = new EndFeaturePrefixContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_endFeaturePrefix);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1093);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CONST) {
				{
				setState(1092);
				((EndFeaturePrefixContext)_localctx).isConstant = match(CONST);
				}
			}

			setState(1095);
			((EndFeaturePrefixContext)_localctx).isEnd = match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BasicFeaturePrefixContext extends ParserRuleContext {
		public FeatureDirectionContext direction;
		public Token isDerived;
		public Token isAbstract;
		public Token isComposite;
		public Token isPortion;
		public Token isVariable;
		public Token isConstant;
		public FeatureDirectionContext featureDirection() {
			return getRuleContext(FeatureDirectionContext.class,0);
		}
		public TerminalNode DERIVED() { return getToken(KerMLParser.DERIVED, 0); }
		public TerminalNode ABSTRACT() { return getToken(KerMLParser.ABSTRACT, 0); }
		public TerminalNode COMPOSITE() { return getToken(KerMLParser.COMPOSITE, 0); }
		public TerminalNode PORTION() { return getToken(KerMLParser.PORTION, 0); }
		public TerminalNode VAR() { return getToken(KerMLParser.VAR, 0); }
		public TerminalNode CONST() { return getToken(KerMLParser.CONST, 0); }
		public BasicFeaturePrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_basicFeaturePrefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBasicFeaturePrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBasicFeaturePrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBasicFeaturePrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BasicFeaturePrefixContext basicFeaturePrefix() throws RecognitionException {
		BasicFeaturePrefixContext _localctx = new BasicFeaturePrefixContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_basicFeaturePrefix);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1098);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 49)) & ~0x3f) == 0 && ((1L << (_la - 49)) & 16777219L) != 0)) {
				{
				setState(1097);
				((BasicFeaturePrefixContext)_localctx).direction = featureDirection();
				}
			}

			setState(1101);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DERIVED) {
				{
				setState(1100);
				((BasicFeaturePrefixContext)_localctx).isDerived = match(DERIVED);
				}
			}

			setState(1104);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ABSTRACT) {
				{
				setState(1103);
				((BasicFeaturePrefixContext)_localctx).isAbstract = match(ABSTRACT);
				}
			}

			setState(1108);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COMPOSITE:
				{
				setState(1106);
				((BasicFeaturePrefixContext)_localctx).isComposite = match(COMPOSITE);
				}
				break;
			case PORTION:
				{
				setState(1107);
				((BasicFeaturePrefixContext)_localctx).isPortion = match(PORTION);
				}
				break;
			case ALL:
			case BINDING:
			case BOOL:
			case CONJUGATES:
			case CONNECTOR:
			case CONST:
			case CROSSES:
			case EXPR:
			case FEATURE:
			case FLOW:
			case INV:
			case NONUNIQUE:
			case ORDERED:
			case REDEFINES:
			case REFERENCES:
			case STEP:
			case SUBSETS:
			case SUCCESSION:
			case TYPED:
			case VAR:
			case DOUBLE_COLON_GT:
			case COLON_GT_GT:
			case COLON_GT:
			case EQUALS_GT:
			case LBRACKET:
			case TILDE:
			case HASH:
			case COLON:
			case LESS:
			case NAME:
				break;
			default:
				break;
			}
			setState(1112);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VAR:
				{
				setState(1110);
				((BasicFeaturePrefixContext)_localctx).isVariable = match(VAR);
				}
				break;
			case CONST:
				{
				setState(1111);
				((BasicFeaturePrefixContext)_localctx).isConstant = match(CONST);
				}
				break;
			case ALL:
			case BINDING:
			case BOOL:
			case CONJUGATES:
			case CONNECTOR:
			case CROSSES:
			case EXPR:
			case FEATURE:
			case FLOW:
			case INV:
			case NONUNIQUE:
			case ORDERED:
			case REDEFINES:
			case REFERENCES:
			case STEP:
			case SUBSETS:
			case SUCCESSION:
			case TYPED:
			case DOUBLE_COLON_GT:
			case COLON_GT_GT:
			case COLON_GT:
			case EQUALS_GT:
			case LBRACKET:
			case TILDE:
			case HASH:
			case COLON:
			case LESS:
			case NAME:
				break;
			default:
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeaturePrefixContext extends ParserRuleContext {
		public EndFeaturePrefixContext endFeaturePrefix() {
			return getRuleContext(EndFeaturePrefixContext.class,0);
		}
		public BasicFeaturePrefixContext basicFeaturePrefix() {
			return getRuleContext(BasicFeaturePrefixContext.class,0);
		}
		public List<PrefixMetadataMemberContext> prefixMetadataMember() {
			return getRuleContexts(PrefixMetadataMemberContext.class);
		}
		public PrefixMetadataMemberContext prefixMetadataMember(int i) {
			return getRuleContext(PrefixMetadataMemberContext.class,i);
		}
		public OwnedCrossFeatureMemberContext ownedCrossFeatureMember() {
			return getRuleContext(OwnedCrossFeatureMemberContext.class,0);
		}
		public FeaturePrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featurePrefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeaturePrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeaturePrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeaturePrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeaturePrefixContext featurePrefix() throws RecognitionException {
		FeaturePrefixContext _localctx = new FeaturePrefixContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_featurePrefix);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1119);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				{
				setState(1114);
				endFeaturePrefix();
				setState(1116);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1688849933991956L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4652225166896570721L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 67133441L) != 0)) {
					{
					setState(1115);
					ownedCrossFeatureMember();
					}
				}

				}
				break;
			case 2:
				{
				setState(1118);
				basicFeaturePrefix();
				}
				break;
			}
			setState(1124);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,91,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1121);
					prefixMetadataMember();
					}
					} 
				}
				setState(1126);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,91,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedCrossFeatureMemberContext extends ParserRuleContext {
		public OwnedCrossFeatureContext ownedCrossFeature() {
			return getRuleContext(OwnedCrossFeatureContext.class,0);
		}
		public OwnedCrossFeatureMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedCrossFeatureMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedCrossFeatureMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedCrossFeatureMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedCrossFeatureMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedCrossFeatureMemberContext ownedCrossFeatureMember() throws RecognitionException {
		OwnedCrossFeatureMemberContext _localctx = new OwnedCrossFeatureMemberContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_ownedCrossFeatureMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1127);
			ownedCrossFeature();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedCrossFeatureContext extends ParserRuleContext {
		public BasicFeaturePrefixContext basicFeaturePrefix() {
			return getRuleContext(BasicFeaturePrefixContext.class,0);
		}
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public OwnedCrossFeatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedCrossFeature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedCrossFeature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedCrossFeature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedCrossFeature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedCrossFeatureContext ownedCrossFeature() throws RecognitionException {
		OwnedCrossFeatureContext _localctx = new OwnedCrossFeatureContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_ownedCrossFeature);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1129);
			basicFeaturePrefix();
			setState(1130);
			featureDeclaration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureDirectionContext extends ParserRuleContext {
		public TerminalNode IN() { return getToken(KerMLParser.IN, 0); }
		public TerminalNode OUT() { return getToken(KerMLParser.OUT, 0); }
		public TerminalNode INOUT() { return getToken(KerMLParser.INOUT, 0); }
		public FeatureDirectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureDirection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureDirection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureDirection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureDirection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureDirectionContext featureDirection() throws RecognitionException {
		FeatureDirectionContext _localctx = new FeatureDirectionContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_featureDirection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1132);
			_la = _input.LA(1);
			if ( !(((((_la - 49)) & ~0x3f) == 0 && ((1L << (_la - 49)) & 16777219L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureDeclarationContext extends ParserRuleContext {
		public Token isSufficient;
		public FeatureIdentificationContext featureIdentification() {
			return getRuleContext(FeatureIdentificationContext.class,0);
		}
		public FeatureSpecializationPartContext featureSpecializationPart() {
			return getRuleContext(FeatureSpecializationPartContext.class,0);
		}
		public ConjugationPartContext conjugationPart() {
			return getRuleContext(ConjugationPartContext.class,0);
		}
		public List<FeatureRelationshipPartContext> featureRelationshipPart() {
			return getRuleContexts(FeatureRelationshipPartContext.class);
		}
		public FeatureRelationshipPartContext featureRelationshipPart(int i) {
			return getRuleContext(FeatureRelationshipPartContext.class,i);
		}
		public TerminalNode ALL() { return getToken(KerMLParser.ALL, 0); }
		public FeatureDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureDeclarationContext featureDeclaration() throws RecognitionException {
		FeatureDeclarationContext _localctx = new FeatureDeclarationContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_featureDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1135);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALL) {
				{
				setState(1134);
				((FeatureDeclarationContext)_localctx).isSufficient = match(ALL);
				}
			}

			setState(1144);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LESS:
			case NAME:
				{
				setState(1137);
				featureIdentification();
				setState(1140);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CROSSES:
				case NONUNIQUE:
				case ORDERED:
				case REDEFINES:
				case REFERENCES:
				case SUBSETS:
				case TYPED:
				case DOUBLE_COLON_GT:
				case COLON_GT_GT:
				case COLON_GT:
				case EQUALS_GT:
				case LBRACKET:
				case COLON:
					{
					setState(1138);
					featureSpecializationPart();
					}
					break;
				case CONJUGATES:
				case TILDE:
					{
					setState(1139);
					conjugationPart();
					}
					break;
				case BINDING:
				case BOOL:
				case CHAINS:
				case CONNECTOR:
				case DEFAULT:
				case DIFFERENCES:
				case DISJOINT:
				case EXPR:
				case FEATURE:
				case FEATURED:
				case FIRST:
				case FLOW:
				case FROM:
				case INTERSECTS:
				case INV:
				case INVERSE:
				case OF:
				case STEP:
				case SUCCESSION:
				case UNIONS:
				case COLON_EQUALS:
				case LPAREN:
				case LBRACE:
				case SEMICOLON:
				case HASH:
				case EQUALS:
					break;
				default:
					break;
				}
				}
				break;
			case CROSSES:
			case NONUNIQUE:
			case ORDERED:
			case REDEFINES:
			case REFERENCES:
			case SUBSETS:
			case TYPED:
			case DOUBLE_COLON_GT:
			case COLON_GT_GT:
			case COLON_GT:
			case EQUALS_GT:
			case LBRACKET:
			case COLON:
				{
				setState(1142);
				featureSpecializationPart();
				}
				break;
			case CONJUGATES:
			case TILDE:
				{
				setState(1143);
				conjugationPart();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 22518136246898688L) != 0) || _la==UNIONS) {
				{
				{
				setState(1146);
				featureRelationshipPart();
				}
				}
				setState(1151);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureIdentificationContext extends ParserRuleContext {
		public Token declaredShortName;
		public Token declaredName;
		public TerminalNode LESS() { return getToken(KerMLParser.LESS, 0); }
		public TerminalNode GREATER() { return getToken(KerMLParser.GREATER, 0); }
		public List<TerminalNode> NAME() { return getTokens(KerMLParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(KerMLParser.NAME, i);
		}
		public FeatureIdentificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureIdentification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureIdentification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureIdentification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureIdentification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureIdentificationContext featureIdentification() throws RecognitionException {
		FeatureIdentificationContext _localctx = new FeatureIdentificationContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_featureIdentification);
		int _la;
		try {
			setState(1159);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LESS:
				enterOuterAlt(_localctx, 1);
				{
				setState(1152);
				match(LESS);
				setState(1153);
				((FeatureIdentificationContext)_localctx).declaredShortName = match(NAME);
				setState(1154);
				match(GREATER);
				setState(1156);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NAME) {
					{
					setState(1155);
					((FeatureIdentificationContext)_localctx).declaredName = match(NAME);
					}
				}

				}
				break;
			case NAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(1158);
				((FeatureIdentificationContext)_localctx).declaredName = match(NAME);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureRelationshipPartContext extends ParserRuleContext {
		public TypeRelationshipPartContext typeRelationshipPart() {
			return getRuleContext(TypeRelationshipPartContext.class,0);
		}
		public ChainingPartContext chainingPart() {
			return getRuleContext(ChainingPartContext.class,0);
		}
		public InvertingPartContext invertingPart() {
			return getRuleContext(InvertingPartContext.class,0);
		}
		public TypeFeaturingPartContext typeFeaturingPart() {
			return getRuleContext(TypeFeaturingPartContext.class,0);
		}
		public FeatureRelationshipPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureRelationshipPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureRelationshipPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureRelationshipPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureRelationshipPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureRelationshipPartContext featureRelationshipPart() throws RecognitionException {
		FeatureRelationshipPartContext _localctx = new FeatureRelationshipPartContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_featureRelationshipPart);
		try {
			setState(1165);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DIFFERENCES:
			case DISJOINT:
			case INTERSECTS:
			case UNIONS:
				enterOuterAlt(_localctx, 1);
				{
				setState(1161);
				typeRelationshipPart();
				}
				break;
			case CHAINS:
				enterOuterAlt(_localctx, 2);
				{
				setState(1162);
				chainingPart();
				}
				break;
			case INVERSE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1163);
				invertingPart();
				}
				break;
			case FEATURED:
				enterOuterAlt(_localctx, 4);
				{
				setState(1164);
				typeFeaturingPart();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ChainingPartContext extends ParserRuleContext {
		public TerminalNode CHAINS() { return getToken(KerMLParser.CHAINS, 0); }
		public OwnedFeatureChainingContext ownedFeatureChaining() {
			return getRuleContext(OwnedFeatureChainingContext.class,0);
		}
		public FeatureChainContext featureChain() {
			return getRuleContext(FeatureChainContext.class,0);
		}
		public ChainingPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_chainingPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterChainingPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitChainingPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitChainingPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ChainingPartContext chainingPart() throws RecognitionException {
		ChainingPartContext _localctx = new ChainingPartContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_chainingPart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1167);
			match(CHAINS);
			setState(1170);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				{
				setState(1168);
				ownedFeatureChaining();
				}
				break;
			case 2:
				{
				setState(1169);
				featureChain();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InvertingPartContext extends ParserRuleContext {
		public TerminalNode INVERSE() { return getToken(KerMLParser.INVERSE, 0); }
		public TerminalNode OF() { return getToken(KerMLParser.OF, 0); }
		public OwnedFeatureInvertingContext ownedFeatureInverting() {
			return getRuleContext(OwnedFeatureInvertingContext.class,0);
		}
		public InvertingPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_invertingPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterInvertingPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitInvertingPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitInvertingPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InvertingPartContext invertingPart() throws RecognitionException {
		InvertingPartContext _localctx = new InvertingPartContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_invertingPart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1172);
			match(INVERSE);
			setState(1173);
			match(OF);
			setState(1174);
			ownedFeatureInverting();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeFeaturingPartContext extends ParserRuleContext {
		public TerminalNode FEATURED() { return getToken(KerMLParser.FEATURED, 0); }
		public TerminalNode BY() { return getToken(KerMLParser.BY, 0); }
		public List<OwnedTypeFeaturingContext> ownedTypeFeaturing() {
			return getRuleContexts(OwnedTypeFeaturingContext.class);
		}
		public OwnedTypeFeaturingContext ownedTypeFeaturing(int i) {
			return getRuleContext(OwnedTypeFeaturingContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public TypeFeaturingPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeFeaturingPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypeFeaturingPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypeFeaturingPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypeFeaturingPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeFeaturingPartContext typeFeaturingPart() throws RecognitionException {
		TypeFeaturingPartContext _localctx = new TypeFeaturingPartContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_typeFeaturingPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1176);
			match(FEATURED);
			setState(1177);
			match(BY);
			setState(1178);
			ownedTypeFeaturing();
			setState(1183);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1179);
				match(COMMA);
				setState(1180);
				ownedTypeFeaturing();
				}
				}
				setState(1185);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureSpecializationPartContext extends ParserRuleContext {
		public List<FeatureSpecializationContext> featureSpecialization() {
			return getRuleContexts(FeatureSpecializationContext.class);
		}
		public FeatureSpecializationContext featureSpecialization(int i) {
			return getRuleContext(FeatureSpecializationContext.class,i);
		}
		public MultiplicityPartContext multiplicityPart() {
			return getRuleContext(MultiplicityPartContext.class,0);
		}
		public FeatureSpecializationPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureSpecializationPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureSpecializationPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureSpecializationPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureSpecializationPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureSpecializationPartContext featureSpecializationPart() throws RecognitionException {
		FeatureSpecializationPartContext _localctx = new FeatureSpecializationPartContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_featureSpecializationPart);
		int _la;
		try {
			int _alt;
			setState(1207);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CROSSES:
			case REDEFINES:
			case REFERENCES:
			case SUBSETS:
			case TYPED:
			case DOUBLE_COLON_GT:
			case COLON_GT_GT:
			case COLON_GT:
			case EQUALS_GT:
			case COLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(1187); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(1186);
						featureSpecialization();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(1189); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,101,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(1192);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4611686018427387937L) != 0)) {
					{
					setState(1191);
					multiplicityPart();
					}
				}

				setState(1197);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 22)) & ~0x3f) == 0 && ((1L << (_la - 22)) & 1441151880758558721L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 4503599929409665L) != 0)) {
					{
					{
					setState(1194);
					featureSpecialization();
					}
					}
					setState(1199);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case NONUNIQUE:
			case ORDERED:
			case LBRACKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(1200);
				multiplicityPart();
				setState(1204);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 22)) & ~0x3f) == 0 && ((1L << (_la - 22)) & 1441151880758558721L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 4503599929409665L) != 0)) {
					{
					{
					setState(1201);
					featureSpecialization();
					}
					}
					setState(1206);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicityPartContext extends ParserRuleContext {
		public Token isOrdered;
		public Token isNonunique;
		public OwnedMultiplicityContext ownedMultiplicity() {
			return getRuleContext(OwnedMultiplicityContext.class,0);
		}
		public TerminalNode ORDERED() { return getToken(KerMLParser.ORDERED, 0); }
		public TerminalNode NONUNIQUE() { return getToken(KerMLParser.NONUNIQUE, 0); }
		public MultiplicityPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicityPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMultiplicityPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMultiplicityPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMultiplicityPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicityPartContext multiplicityPart() throws RecognitionException {
		MultiplicityPartContext _localctx = new MultiplicityPartContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_multiplicityPart);
		int _la;
		try {
			setState(1223);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,110,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1209);
				ownedMultiplicity();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1211);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LBRACKET) {
					{
					setState(1210);
					ownedMultiplicity();
					}
				}

				setState(1221);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ORDERED:
					{
					setState(1213);
					((MultiplicityPartContext)_localctx).isOrdered = match(ORDERED);
					setState(1215);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==NONUNIQUE) {
						{
						setState(1214);
						((MultiplicityPartContext)_localctx).isNonunique = match(NONUNIQUE);
						}
					}

					}
					break;
				case NONUNIQUE:
					{
					setState(1217);
					((MultiplicityPartContext)_localctx).isNonunique = match(NONUNIQUE);
					setState(1219);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==ORDERED) {
						{
						setState(1218);
						((MultiplicityPartContext)_localctx).isOrdered = match(ORDERED);
						}
					}

					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureSpecializationContext extends ParserRuleContext {
		public TypingsContext typings() {
			return getRuleContext(TypingsContext.class,0);
		}
		public SubsettingsContext subsettings() {
			return getRuleContext(SubsettingsContext.class,0);
		}
		public ReferencesContext references() {
			return getRuleContext(ReferencesContext.class,0);
		}
		public CrossesContext crosses() {
			return getRuleContext(CrossesContext.class,0);
		}
		public RedefinitionsContext redefinitions() {
			return getRuleContext(RedefinitionsContext.class,0);
		}
		public FeatureSpecializationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureSpecialization; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureSpecialization(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureSpecialization(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureSpecialization(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureSpecializationContext featureSpecialization() throws RecognitionException {
		FeatureSpecializationContext _localctx = new FeatureSpecializationContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_featureSpecialization);
		try {
			setState(1230);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TYPED:
			case COLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(1225);
				typings();
				}
				break;
			case SUBSETS:
			case COLON_GT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1226);
				subsettings();
				}
				break;
			case REFERENCES:
			case DOUBLE_COLON_GT:
				enterOuterAlt(_localctx, 3);
				{
				setState(1227);
				references();
				}
				break;
			case CROSSES:
			case EQUALS_GT:
				enterOuterAlt(_localctx, 4);
				{
				setState(1228);
				crosses();
				}
				break;
			case REDEFINES:
			case COLON_GT_GT:
				enterOuterAlt(_localctx, 5);
				{
				setState(1229);
				redefinitions();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypingsContext extends ParserRuleContext {
		public TypedByContext typedBy() {
			return getRuleContext(TypedByContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public List<OwnedFeatureTypingContext> ownedFeatureTyping() {
			return getRuleContexts(OwnedFeatureTypingContext.class);
		}
		public OwnedFeatureTypingContext ownedFeatureTyping(int i) {
			return getRuleContext(OwnedFeatureTypingContext.class,i);
		}
		public TypingsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typings; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypings(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypings(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypings(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypingsContext typings() throws RecognitionException {
		TypingsContext _localctx = new TypingsContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_typings);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1232);
			typedBy();
			setState(1237);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1233);
				match(COMMA);
				setState(1234);
				ownedFeatureTyping();
				}
				}
				setState(1239);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypedByContext extends ParserRuleContext {
		public TypedByTokenContext typedByToken() {
			return getRuleContext(TypedByTokenContext.class,0);
		}
		public OwnedFeatureTypingContext ownedFeatureTyping() {
			return getRuleContext(OwnedFeatureTypingContext.class,0);
		}
		public TypedByContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedBy; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypedBy(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypedBy(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypedBy(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedByContext typedBy() throws RecognitionException {
		TypedByContext _localctx = new TypedByContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_typedBy);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1240);
			typedByToken();
			setState(1241);
			ownedFeatureTyping();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubsettingsContext extends ParserRuleContext {
		public SubsetsContext subsets() {
			return getRuleContext(SubsetsContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public List<OwnedSubsettingContext> ownedSubsetting() {
			return getRuleContexts(OwnedSubsettingContext.class);
		}
		public OwnedSubsettingContext ownedSubsetting(int i) {
			return getRuleContext(OwnedSubsettingContext.class,i);
		}
		public SubsettingsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subsettings; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSubsettings(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSubsettings(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSubsettings(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubsettingsContext subsettings() throws RecognitionException {
		SubsettingsContext _localctx = new SubsettingsContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_subsettings);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1243);
			subsets();
			setState(1248);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1244);
				match(COMMA);
				setState(1245);
				ownedSubsetting();
				}
				}
				setState(1250);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubsetsContext extends ParserRuleContext {
		public SubsetsTokenContext subsetsToken() {
			return getRuleContext(SubsetsTokenContext.class,0);
		}
		public OwnedSubsettingContext ownedSubsetting() {
			return getRuleContext(OwnedSubsettingContext.class,0);
		}
		public SubsetsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subsets; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSubsets(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSubsets(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSubsets(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubsetsContext subsets() throws RecognitionException {
		SubsetsContext _localctx = new SubsetsContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_subsets);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1251);
			subsetsToken();
			setState(1252);
			ownedSubsetting();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReferencesContext extends ParserRuleContext {
		public ReferencesTokenContext referencesToken() {
			return getRuleContext(ReferencesTokenContext.class,0);
		}
		public OwnedReferenceSubsettingContext ownedReferenceSubsetting() {
			return getRuleContext(OwnedReferenceSubsettingContext.class,0);
		}
		public ReferencesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_references; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterReferences(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitReferences(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitReferences(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReferencesContext references() throws RecognitionException {
		ReferencesContext _localctx = new ReferencesContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_references);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1254);
			referencesToken();
			setState(1255);
			ownedReferenceSubsetting();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CrossesContext extends ParserRuleContext {
		public CrossesTokenContext crossesToken() {
			return getRuleContext(CrossesTokenContext.class,0);
		}
		public OwnedCrossSubsettingContext ownedCrossSubsetting() {
			return getRuleContext(OwnedCrossSubsettingContext.class,0);
		}
		public CrossesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_crosses; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterCrosses(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitCrosses(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitCrosses(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CrossesContext crosses() throws RecognitionException {
		CrossesContext _localctx = new CrossesContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_crosses);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1257);
			crossesToken();
			setState(1258);
			ownedCrossSubsetting();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RedefinitionsContext extends ParserRuleContext {
		public RedefinesContext redefines() {
			return getRuleContext(RedefinesContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public List<OwnedRedefinitionContext> ownedRedefinition() {
			return getRuleContexts(OwnedRedefinitionContext.class);
		}
		public OwnedRedefinitionContext ownedRedefinition(int i) {
			return getRuleContext(OwnedRedefinitionContext.class,i);
		}
		public RedefinitionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_redefinitions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterRedefinitions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitRedefinitions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitRedefinitions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RedefinitionsContext redefinitions() throws RecognitionException {
		RedefinitionsContext _localctx = new RedefinitionsContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_redefinitions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1260);
			redefines();
			setState(1265);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1261);
				match(COMMA);
				setState(1262);
				ownedRedefinition();
				}
				}
				setState(1267);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RedefinesContext extends ParserRuleContext {
		public RedefinesTokenContext redefinesToken() {
			return getRuleContext(RedefinesTokenContext.class,0);
		}
		public OwnedRedefinitionContext ownedRedefinition() {
			return getRuleContext(OwnedRedefinitionContext.class,0);
		}
		public RedefinesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_redefines; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterRedefines(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitRedefines(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitRedefines(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RedefinesContext redefines() throws RecognitionException {
		RedefinesContext _localctx = new RedefinesContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_redefines);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1268);
			redefinesToken();
			setState(1269);
			ownedRedefinition();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureTypingContext extends ParserRuleContext {
		public QualifiedNameContext typedFeature;
		public TerminalNode TYPING() { return getToken(KerMLParser.TYPING, 0); }
		public TypedByContext typedBy() {
			return getRuleContext(TypedByContext.class,0);
		}
		public GeneralTypeContext generalType() {
			return getRuleContext(GeneralTypeContext.class,0);
		}
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode SPECIALIZATION() { return getToken(KerMLParser.SPECIALIZATION, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public FeatureTypingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureTyping; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureTyping(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureTyping(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureTyping(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureTypingContext featureTyping() throws RecognitionException {
		FeatureTypingContext _localctx = new FeatureTypingContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_featureTyping);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1273);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SPECIALIZATION) {
				{
				setState(1271);
				match(SPECIALIZATION);
				setState(1272);
				identification();
				}
			}

			setState(1275);
			match(TYPING);
			setState(1276);
			((FeatureTypingContext)_localctx).typedFeature = qualifiedName();
			setState(1277);
			typedBy();
			setState(1278);
			generalType();
			setState(1279);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedFeatureTypingContext extends ParserRuleContext {
		public GeneralTypeContext generalType() {
			return getRuleContext(GeneralTypeContext.class,0);
		}
		public OwnedFeatureTypingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedFeatureTyping; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedFeatureTyping(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedFeatureTyping(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedFeatureTyping(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedFeatureTypingContext ownedFeatureTyping() throws RecognitionException {
		OwnedFeatureTypingContext _localctx = new OwnedFeatureTypingContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_ownedFeatureTyping);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1281);
			generalType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubsettingContext extends ParserRuleContext {
		public TerminalNode SUBSET() { return getToken(KerMLParser.SUBSET, 0); }
		public SpecificTypeContext specificType() {
			return getRuleContext(SpecificTypeContext.class,0);
		}
		public SubsetsTokenContext subsetsToken() {
			return getRuleContext(SubsetsTokenContext.class,0);
		}
		public GeneralTypeContext generalType() {
			return getRuleContext(GeneralTypeContext.class,0);
		}
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public TerminalNode SPECIALIZATION() { return getToken(KerMLParser.SPECIALIZATION, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public SubsettingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subsetting; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSubsetting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSubsetting(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSubsetting(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubsettingContext subsetting() throws RecognitionException {
		SubsettingContext _localctx = new SubsettingContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_subsetting);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1285);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SPECIALIZATION) {
				{
				setState(1283);
				match(SPECIALIZATION);
				setState(1284);
				identification();
				}
			}

			setState(1287);
			match(SUBSET);
			setState(1288);
			specificType();
			setState(1289);
			subsetsToken();
			setState(1290);
			generalType();
			setState(1291);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedSubsettingContext extends ParserRuleContext {
		public GeneralTypeContext generalType() {
			return getRuleContext(GeneralTypeContext.class,0);
		}
		public OwnedSubsettingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedSubsetting; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedSubsetting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedSubsetting(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedSubsetting(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedSubsettingContext ownedSubsetting() throws RecognitionException {
		OwnedSubsettingContext _localctx = new OwnedSubsettingContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_ownedSubsetting);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1293);
			generalType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedReferenceSubsettingContext extends ParserRuleContext {
		public GeneralTypeContext generalType() {
			return getRuleContext(GeneralTypeContext.class,0);
		}
		public OwnedReferenceSubsettingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedReferenceSubsetting; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedReferenceSubsetting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedReferenceSubsetting(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedReferenceSubsetting(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedReferenceSubsettingContext ownedReferenceSubsetting() throws RecognitionException {
		OwnedReferenceSubsettingContext _localctx = new OwnedReferenceSubsettingContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_ownedReferenceSubsetting);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1295);
			generalType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedCrossSubsettingContext extends ParserRuleContext {
		public GeneralTypeContext generalType() {
			return getRuleContext(GeneralTypeContext.class,0);
		}
		public OwnedCrossSubsettingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedCrossSubsetting; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedCrossSubsetting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedCrossSubsetting(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedCrossSubsetting(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedCrossSubsettingContext ownedCrossSubsetting() throws RecognitionException {
		OwnedCrossSubsettingContext _localctx = new OwnedCrossSubsettingContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_ownedCrossSubsetting);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1297);
			generalType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RedefinitionContext extends ParserRuleContext {
		public TerminalNode REDEFINITION() { return getToken(KerMLParser.REDEFINITION, 0); }
		public SpecificTypeContext specificType() {
			return getRuleContext(SpecificTypeContext.class,0);
		}
		public RedefinesTokenContext redefinesToken() {
			return getRuleContext(RedefinesTokenContext.class,0);
		}
		public GeneralTypeContext generalType() {
			return getRuleContext(GeneralTypeContext.class,0);
		}
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public TerminalNode SPECIALIZATION() { return getToken(KerMLParser.SPECIALIZATION, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public RedefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_redefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterRedefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitRedefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitRedefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RedefinitionContext redefinition() throws RecognitionException {
		RedefinitionContext _localctx = new RedefinitionContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_redefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1301);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SPECIALIZATION) {
				{
				setState(1299);
				match(SPECIALIZATION);
				setState(1300);
				identification();
				}
			}

			setState(1303);
			match(REDEFINITION);
			setState(1304);
			specificType();
			setState(1305);
			redefinesToken();
			setState(1306);
			generalType();
			setState(1307);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedRedefinitionContext extends ParserRuleContext {
		public GeneralTypeContext generalType() {
			return getRuleContext(GeneralTypeContext.class,0);
		}
		public OwnedRedefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedRedefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedRedefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedRedefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedRedefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedRedefinitionContext ownedRedefinition() throws RecognitionException {
		OwnedRedefinitionContext _localctx = new OwnedRedefinitionContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_ownedRedefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1309);
			generalType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedFeatureChainContext extends ParserRuleContext {
		public FeatureChainContext featureChain() {
			return getRuleContext(FeatureChainContext.class,0);
		}
		public OwnedFeatureChainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedFeatureChain; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedFeatureChain(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedFeatureChain(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedFeatureChain(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedFeatureChainContext ownedFeatureChain() throws RecognitionException {
		OwnedFeatureChainContext _localctx = new OwnedFeatureChainContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_ownedFeatureChain);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1311);
			featureChain();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureChainContext extends ParserRuleContext {
		public List<OwnedFeatureChainingContext> ownedFeatureChaining() {
			return getRuleContexts(OwnedFeatureChainingContext.class);
		}
		public OwnedFeatureChainingContext ownedFeatureChaining(int i) {
			return getRuleContext(OwnedFeatureChainingContext.class,i);
		}
		public List<TerminalNode> DOT() { return getTokens(KerMLParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(KerMLParser.DOT, i);
		}
		public FeatureChainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureChain; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureChain(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureChain(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureChain(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureChainContext featureChain() throws RecognitionException {
		FeatureChainContext _localctx = new FeatureChainContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_featureChain);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1313);
			ownedFeatureChaining();
			setState(1316); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1314);
					match(DOT);
					setState(1315);
					ownedFeatureChaining();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1318); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,118,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedFeatureChainingContext extends ParserRuleContext {
		public QualifiedNameContext chainingFeature;
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public OwnedFeatureChainingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedFeatureChaining; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedFeatureChaining(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedFeatureChaining(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedFeatureChaining(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedFeatureChainingContext ownedFeatureChaining() throws RecognitionException {
		OwnedFeatureChainingContext _localctx = new OwnedFeatureChainingContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_ownedFeatureChaining);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1320);
			((OwnedFeatureChainingContext)_localctx).chainingFeature = qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureInvertingContext extends ParserRuleContext {
		public TerminalNode INVERSE() { return getToken(KerMLParser.INVERSE, 0); }
		public TerminalNode OF() { return getToken(KerMLParser.OF, 0); }
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public List<OwnedFeatureChainContext> ownedFeatureChain() {
			return getRuleContexts(OwnedFeatureChainContext.class);
		}
		public OwnedFeatureChainContext ownedFeatureChain(int i) {
			return getRuleContext(OwnedFeatureChainContext.class,i);
		}
		public TerminalNode INVERTING() { return getToken(KerMLParser.INVERTING, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public FeatureInvertingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureInverting; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureInverting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureInverting(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureInverting(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureInvertingContext featureInverting() throws RecognitionException {
		FeatureInvertingContext _localctx = new FeatureInvertingContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_featureInverting);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1326);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INVERTING) {
				{
				setState(1322);
				match(INVERTING);
				setState(1324);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,119,_ctx) ) {
				case 1:
					{
					setState(1323);
					identification();
					}
					break;
				}
				}
			}

			setState(1328);
			match(INVERSE);
			setState(1331);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,121,_ctx) ) {
			case 1:
				{
				setState(1329);
				qualifiedName();
				}
				break;
			case 2:
				{
				setState(1330);
				ownedFeatureChain();
				}
				break;
			}
			setState(1333);
			match(OF);
			setState(1336);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
			case 1:
				{
				setState(1334);
				qualifiedName();
				}
				break;
			case 2:
				{
				setState(1335);
				ownedFeatureChain();
				}
				break;
			}
			setState(1338);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedFeatureInvertingContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public OwnedFeatureChainContext ownedFeatureChain() {
			return getRuleContext(OwnedFeatureChainContext.class,0);
		}
		public OwnedFeatureInvertingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedFeatureInverting; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedFeatureInverting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedFeatureInverting(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedFeatureInverting(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedFeatureInvertingContext ownedFeatureInverting() throws RecognitionException {
		OwnedFeatureInvertingContext _localctx = new OwnedFeatureInvertingContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_ownedFeatureInverting);
		try {
			setState(1342);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1340);
				qualifiedName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1341);
				ownedFeatureChain();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeFeaturingContext extends ParserRuleContext {
		public QualifiedNameContext featureOfType;
		public QualifiedNameContext featuringType;
		public TerminalNode FEATURING() { return getToken(KerMLParser.FEATURING, 0); }
		public TerminalNode BY() { return getToken(KerMLParser.BY, 0); }
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public TerminalNode OF() { return getToken(KerMLParser.OF, 0); }
		public TypeFeaturingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeFeaturing; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypeFeaturing(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypeFeaturing(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypeFeaturing(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeFeaturingContext typeFeaturing() throws RecognitionException {
		TypeFeaturingContext _localctx = new TypeFeaturingContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_typeFeaturing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1344);
			match(FEATURING);
			setState(1348);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,124,_ctx) ) {
			case 1:
				{
				setState(1345);
				identification();
				setState(1346);
				match(OF);
				}
				break;
			}
			setState(1350);
			((TypeFeaturingContext)_localctx).featureOfType = qualifiedName();
			setState(1351);
			match(BY);
			setState(1352);
			((TypeFeaturingContext)_localctx).featuringType = qualifiedName();
			setState(1353);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedTypeFeaturingContext extends ParserRuleContext {
		public QualifiedNameContext featuringType;
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public OwnedTypeFeaturingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedTypeFeaturing; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedTypeFeaturing(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedTypeFeaturing(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedTypeFeaturing(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedTypeFeaturingContext ownedTypeFeaturing() throws RecognitionException {
		OwnedTypeFeaturingContext _localctx = new OwnedTypeFeaturingContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_ownedTypeFeaturing);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1355);
			((OwnedTypeFeaturingContext)_localctx).featuringType = qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DatatypeContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode DATATYPE() { return getToken(KerMLParser.DATATYPE, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public DatatypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_datatype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterDatatype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitDatatype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitDatatype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatatypeContext datatype() throws RecognitionException {
		DatatypeContext _localctx = new DatatypeContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_datatype);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1357);
			typePrefix();
			setState(1358);
			match(DATATYPE);
			setState(1359);
			classifierDeclaration();
			setState(1360);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode CLASS() { return getToken(KerMLParser.CLASS, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public ClassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_class; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterClass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitClass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitClass(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassContext class_() throws RecognitionException {
		ClassContext _localctx = new ClassContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_class);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1362);
			typePrefix();
			setState(1363);
			match(CLASS);
			setState(1364);
			classifierDeclaration();
			setState(1365);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StructureContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode STRUCT() { return getToken(KerMLParser.STRUCT, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public StructureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structure; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterStructure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitStructure(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitStructure(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StructureContext structure() throws RecognitionException {
		StructureContext _localctx = new StructureContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_structure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1367);
			typePrefix();
			setState(1368);
			match(STRUCT);
			setState(1369);
			classifierDeclaration();
			setState(1370);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssociationContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode ASSOC() { return getToken(KerMLParser.ASSOC, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public AssociationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_association; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterAssociation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitAssociation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitAssociation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssociationContext association() throws RecognitionException {
		AssociationContext _localctx = new AssociationContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_association);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1372);
			typePrefix();
			setState(1373);
			match(ASSOC);
			setState(1374);
			classifierDeclaration();
			setState(1375);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssociationStructureContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode ASSOC() { return getToken(KerMLParser.ASSOC, 0); }
		public TerminalNode STRUCT() { return getToken(KerMLParser.STRUCT, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public AssociationStructureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_associationStructure; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterAssociationStructure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitAssociationStructure(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitAssociationStructure(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssociationStructureContext associationStructure() throws RecognitionException {
		AssociationStructureContext _localctx = new AssociationStructureContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_associationStructure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1377);
			typePrefix();
			setState(1378);
			match(ASSOC);
			setState(1379);
			match(STRUCT);
			setState(1380);
			classifierDeclaration();
			setState(1381);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConnectorContext extends ParserRuleContext {
		public FeaturePrefixContext featurePrefix() {
			return getRuleContext(FeaturePrefixContext.class,0);
		}
		public TerminalNode CONNECTOR() { return getToken(KerMLParser.CONNECTOR, 0); }
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public ConnectorDeclarationContext connectorDeclaration() {
			return getRuleContext(ConnectorDeclarationContext.class,0);
		}
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public ValuePartContext valuePart() {
			return getRuleContext(ValuePartContext.class,0);
		}
		public ConnectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_connector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterConnector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitConnector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitConnector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConnectorContext connector() throws RecognitionException {
		ConnectorContext _localctx = new ConnectorContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_connector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1383);
			featurePrefix();
			setState(1384);
			match(CONNECTOR);
			setState(1392);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,127,_ctx) ) {
			case 1:
				{
				setState(1386);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4456464L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4652225029457616929L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 67133441L) != 0)) {
					{
					setState(1385);
					featureDeclaration();
					}
				}

				setState(1389);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DEFAULT || _la==COLON_EQUALS || _la==EQUALS) {
					{
					setState(1388);
					valuePart();
					}
				}

				}
				break;
			case 2:
				{
				setState(1391);
				connectorDeclaration();
				}
				break;
			}
			setState(1394);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConnectorDeclarationContext extends ParserRuleContext {
		public BinaryConnectorDeclarationContext binaryConnectorDeclaration() {
			return getRuleContext(BinaryConnectorDeclarationContext.class,0);
		}
		public NaryConnectorDeclarationContext naryConnectorDeclaration() {
			return getRuleContext(NaryConnectorDeclarationContext.class,0);
		}
		public ConnectorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_connectorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterConnectorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitConnectorDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitConnectorDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConnectorDeclarationContext connectorDeclaration() throws RecognitionException {
		ConnectorDeclarationContext _localctx = new ConnectorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_connectorDeclaration);
		try {
			setState(1398);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1396);
				binaryConnectorDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1397);
				naryConnectorDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BinaryConnectorDeclarationContext extends ParserRuleContext {
		public Token isSufficient;
		public List<ConnectorEndMemberContext> connectorEndMember() {
			return getRuleContexts(ConnectorEndMemberContext.class);
		}
		public ConnectorEndMemberContext connectorEndMember(int i) {
			return getRuleContext(ConnectorEndMemberContext.class,i);
		}
		public TerminalNode TO() { return getToken(KerMLParser.TO, 0); }
		public TerminalNode FROM() { return getToken(KerMLParser.FROM, 0); }
		public TerminalNode ALL() { return getToken(KerMLParser.ALL, 0); }
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public BinaryConnectorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binaryConnectorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBinaryConnectorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBinaryConnectorDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBinaryConnectorDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinaryConnectorDeclarationContext binaryConnectorDeclaration() throws RecognitionException {
		BinaryConnectorDeclarationContext _localctx = new BinaryConnectorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_binaryConnectorDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1408);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
			case 1:
				{
				setState(1401);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4456464L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4652225029457616929L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 67133441L) != 0)) {
					{
					setState(1400);
					featureDeclaration();
					}
				}

				setState(1403);
				match(FROM);
				}
				break;
			case 2:
				{
				setState(1404);
				((BinaryConnectorDeclarationContext)_localctx).isSufficient = match(ALL);
				setState(1406);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FROM) {
					{
					setState(1405);
					match(FROM);
					}
				}

				}
				break;
			}
			setState(1410);
			connectorEndMember();
			setState(1411);
			match(TO);
			setState(1412);
			connectorEndMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NaryConnectorDeclarationContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(KerMLParser.LPAREN, 0); }
		public List<ConnectorEndMemberContext> connectorEndMember() {
			return getRuleContexts(ConnectorEndMemberContext.class);
		}
		public ConnectorEndMemberContext connectorEndMember(int i) {
			return getRuleContext(ConnectorEndMemberContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public TerminalNode RPAREN() { return getToken(KerMLParser.RPAREN, 0); }
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public NaryConnectorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_naryConnectorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNaryConnectorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNaryConnectorDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNaryConnectorDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NaryConnectorDeclarationContext naryConnectorDeclaration() throws RecognitionException {
		NaryConnectorDeclarationContext _localctx = new NaryConnectorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_naryConnectorDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1415);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4456464L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4652225029457616929L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 67133441L) != 0)) {
				{
				setState(1414);
				featureDeclaration();
				}
			}

			setState(1417);
			match(LPAREN);
			setState(1418);
			connectorEndMember();
			setState(1419);
			match(COMMA);
			setState(1420);
			connectorEndMember();
			setState(1425);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1421);
				match(COMMA);
				setState(1422);
				connectorEndMember();
				}
				}
				setState(1427);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1428);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConnectorEndMemberContext extends ParserRuleContext {
		public ConnectorEndContext connectorEnd() {
			return getRuleContext(ConnectorEndContext.class,0);
		}
		public ConnectorEndMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_connectorEndMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterConnectorEndMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitConnectorEndMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitConnectorEndMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConnectorEndMemberContext connectorEndMember() throws RecognitionException {
		ConnectorEndMemberContext _localctx = new ConnectorEndMemberContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_connectorEndMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1430);
			connectorEnd();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConnectorEndContext extends ParserRuleContext {
		public Token declaredName;
		public OwnedReferenceSubsettingContext ownedReferenceSubsetting() {
			return getRuleContext(OwnedReferenceSubsettingContext.class,0);
		}
		public OwnedCrossMultiplicityMemberContext ownedCrossMultiplicityMember() {
			return getRuleContext(OwnedCrossMultiplicityMemberContext.class,0);
		}
		public ReferencesTokenContext referencesToken() {
			return getRuleContext(ReferencesTokenContext.class,0);
		}
		public TerminalNode NAME() { return getToken(KerMLParser.NAME, 0); }
		public ConnectorEndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_connectorEnd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterConnectorEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitConnectorEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitConnectorEnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConnectorEndContext connectorEnd() throws RecognitionException {
		ConnectorEndContext _localctx = new ConnectorEndContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_connectorEnd);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1433);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACKET) {
				{
				setState(1432);
				ownedCrossMultiplicityMember();
				}
			}

			setState(1437);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,135,_ctx) ) {
			case 1:
				{
				setState(1435);
				((ConnectorEndContext)_localctx).declaredName = match(NAME);
				setState(1436);
				referencesToken();
				}
				break;
			}
			setState(1439);
			ownedReferenceSubsetting();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedCrossMultiplicityMemberContext extends ParserRuleContext {
		public OwnedCrossMultiplicityContext ownedCrossMultiplicity() {
			return getRuleContext(OwnedCrossMultiplicityContext.class,0);
		}
		public OwnedCrossMultiplicityMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedCrossMultiplicityMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedCrossMultiplicityMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedCrossMultiplicityMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedCrossMultiplicityMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedCrossMultiplicityMemberContext ownedCrossMultiplicityMember() throws RecognitionException {
		OwnedCrossMultiplicityMemberContext _localctx = new OwnedCrossMultiplicityMemberContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_ownedCrossMultiplicityMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1441);
			ownedCrossMultiplicity();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedCrossMultiplicityContext extends ParserRuleContext {
		public OwnedMultiplicityContext ownedMultiplicity() {
			return getRuleContext(OwnedMultiplicityContext.class,0);
		}
		public OwnedCrossMultiplicityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedCrossMultiplicity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedCrossMultiplicity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedCrossMultiplicity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedCrossMultiplicity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedCrossMultiplicityContext ownedCrossMultiplicity() throws RecognitionException {
		OwnedCrossMultiplicityContext _localctx = new OwnedCrossMultiplicityContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_ownedCrossMultiplicity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1443);
			ownedMultiplicity();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BindingConnectorContext extends ParserRuleContext {
		public FeaturePrefixContext featurePrefix() {
			return getRuleContext(FeaturePrefixContext.class,0);
		}
		public TerminalNode BINDING() { return getToken(KerMLParser.BINDING, 0); }
		public BindingConnectorDeclarationContext bindingConnectorDeclaration() {
			return getRuleContext(BindingConnectorDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public BindingConnectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bindingConnector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBindingConnector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBindingConnector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBindingConnector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BindingConnectorContext bindingConnector() throws RecognitionException {
		BindingConnectorContext _localctx = new BindingConnectorContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_bindingConnector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1445);
			featurePrefix();
			setState(1446);
			match(BINDING);
			setState(1447);
			bindingConnectorDeclaration();
			setState(1448);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BindingConnectorDeclarationContext extends ParserRuleContext {
		public Token isSufficient;
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public TerminalNode OF() { return getToken(KerMLParser.OF, 0); }
		public List<ConnectorEndMemberContext> connectorEndMember() {
			return getRuleContexts(ConnectorEndMemberContext.class);
		}
		public ConnectorEndMemberContext connectorEndMember(int i) {
			return getRuleContext(ConnectorEndMemberContext.class,i);
		}
		public TerminalNode EQUALS() { return getToken(KerMLParser.EQUALS, 0); }
		public TerminalNode ALL() { return getToken(KerMLParser.ALL, 0); }
		public BindingConnectorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bindingConnectorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBindingConnectorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBindingConnectorDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBindingConnectorDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BindingConnectorDeclarationContext bindingConnectorDeclaration() throws RecognitionException {
		BindingConnectorDeclarationContext _localctx = new BindingConnectorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_bindingConnectorDeclaration);
		int _la;
		try {
			setState(1470);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,140,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1450);
				featureDeclaration();
				setState(1456);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OF) {
					{
					setState(1451);
					match(OF);
					setState(1452);
					connectorEndMember();
					setState(1453);
					match(EQUALS);
					setState(1454);
					connectorEndMember();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1459);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALL) {
					{
					setState(1458);
					((BindingConnectorDeclarationContext)_localctx).isSufficient = match(ALL);
					}
				}

				setState(1468);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OF || _la==LBRACKET || _la==DOLLAR || _la==NAME) {
					{
					setState(1462);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==OF) {
						{
						setState(1461);
						match(OF);
						}
					}

					setState(1464);
					connectorEndMember();
					setState(1465);
					match(EQUALS);
					setState(1466);
					connectorEndMember();
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SuccessionContext extends ParserRuleContext {
		public FeaturePrefixContext featurePrefix() {
			return getRuleContext(FeaturePrefixContext.class,0);
		}
		public TerminalNode SUCCESSION() { return getToken(KerMLParser.SUCCESSION, 0); }
		public SuccessionDeclarationContext successionDeclaration() {
			return getRuleContext(SuccessionDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public SuccessionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_succession; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSuccession(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSuccession(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSuccession(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SuccessionContext succession() throws RecognitionException {
		SuccessionContext _localctx = new SuccessionContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_succession);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1472);
			featurePrefix();
			setState(1473);
			match(SUCCESSION);
			setState(1474);
			successionDeclaration();
			setState(1475);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SuccessionDeclarationContext extends ParserRuleContext {
		public Token isSufficient;
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public TerminalNode FIRST() { return getToken(KerMLParser.FIRST, 0); }
		public List<ConnectorEndMemberContext> connectorEndMember() {
			return getRuleContexts(ConnectorEndMemberContext.class);
		}
		public ConnectorEndMemberContext connectorEndMember(int i) {
			return getRuleContext(ConnectorEndMemberContext.class,i);
		}
		public TerminalNode THEN() { return getToken(KerMLParser.THEN, 0); }
		public TerminalNode ALL() { return getToken(KerMLParser.ALL, 0); }
		public SuccessionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_successionDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSuccessionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSuccessionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSuccessionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SuccessionDeclarationContext successionDeclaration() throws RecognitionException {
		SuccessionDeclarationContext _localctx = new SuccessionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_successionDeclaration);
		int _la;
		try {
			setState(1497);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,145,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1477);
				featureDeclaration();
				setState(1483);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FIRST) {
					{
					setState(1478);
					match(FIRST);
					setState(1479);
					connectorEndMember();
					setState(1480);
					match(THEN);
					setState(1481);
					connectorEndMember();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1486);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALL) {
					{
					setState(1485);
					((SuccessionDeclarationContext)_localctx).isSufficient = match(ALL);
					}
				}

				setState(1495);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FIRST || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & 1073774593L) != 0)) {
					{
					setState(1489);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==FIRST) {
						{
						setState(1488);
						match(FIRST);
						}
					}

					setState(1491);
					connectorEndMember();
					setState(1492);
					match(THEN);
					setState(1493);
					connectorEndMember();
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BehaviorContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode BEHAVIOR() { return getToken(KerMLParser.BEHAVIOR, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public BehaviorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_behavior; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBehavior(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBehavior(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBehavior(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BehaviorContext behavior() throws RecognitionException {
		BehaviorContext _localctx = new BehaviorContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_behavior);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1499);
			typePrefix();
			setState(1500);
			match(BEHAVIOR);
			setState(1501);
			classifierDeclaration();
			setState(1502);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StepContext extends ParserRuleContext {
		public FeaturePrefixContext featurePrefix() {
			return getRuleContext(FeaturePrefixContext.class,0);
		}
		public TerminalNode STEP() { return getToken(KerMLParser.STEP, 0); }
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public ValuePartContext valuePart() {
			return getRuleContext(ValuePartContext.class,0);
		}
		public StepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_step; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterStep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitStep(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitStep(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StepContext step() throws RecognitionException {
		StepContext _localctx = new StepContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_step);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1504);
			featurePrefix();
			setState(1505);
			match(STEP);
			setState(1507);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4456464L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4652225029457616929L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 67133441L) != 0)) {
				{
				setState(1506);
				featureDeclaration();
				}
			}

			setState(1510);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT || _la==COLON_EQUALS || _la==EQUALS) {
				{
				setState(1509);
				valuePart();
				}
			}

			setState(1512);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode FUNCTION() { return getToken(KerMLParser.FUNCTION, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_function);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1514);
			typePrefix();
			setState(1515);
			match(FUNCTION);
			setState(1516);
			classifierDeclaration();
			setState(1517);
			functionBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionBodyContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(KerMLParser.SEMICOLON, 0); }
		public TerminalNode LBRACE() { return getToken(KerMLParser.LBRACE, 0); }
		public FunctionBodyPartContext functionBodyPart() {
			return getRuleContext(FunctionBodyPartContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(KerMLParser.RBRACE, 0); }
		public FunctionBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFunctionBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFunctionBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFunctionBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionBodyContext functionBody() throws RecognitionException {
		FunctionBodyContext _localctx = new FunctionBodyContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_functionBody);
		try {
			setState(1524);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEMICOLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(1519);
				match(SEMICOLON);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1520);
				match(LBRACE);
				setState(1521);
				functionBodyPart();
				setState(1522);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionBodyPartContext extends ParserRuleContext {
		public List<TypeBodyElementContext> typeBodyElement() {
			return getRuleContexts(TypeBodyElementContext.class);
		}
		public TypeBodyElementContext typeBodyElement(int i) {
			return getRuleContext(TypeBodyElementContext.class,i);
		}
		public List<ReturnFeatureMemberContext> returnFeatureMember() {
			return getRuleContexts(ReturnFeatureMemberContext.class);
		}
		public ReturnFeatureMemberContext returnFeatureMember(int i) {
			return getRuleContext(ReturnFeatureMemberContext.class,i);
		}
		public ResultExpressionMemberContext resultExpressionMember() {
			return getRuleContext(ResultExpressionMemberContext.class,0);
		}
		public FunctionBodyPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionBodyPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFunctionBodyPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFunctionBodyPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFunctionBodyPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionBodyPartContext functionBodyPart() throws RecognitionException {
		FunctionBodyPartContext _localctx = new FunctionBodyPartContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_functionBodyPart);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1530);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,150,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(1528);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,149,_ctx) ) {
					case 1:
						{
						setState(1526);
						typeBodyElement();
						}
						break;
					case 2:
						{
						setState(1527);
						returnFeatureMember();
						}
						break;
					}
					} 
				}
				setState(1532);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,150,_ctx);
			}
			setState(1534);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 72163181513932880L) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 2882303770107066381L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 125836163L) != 0)) {
				{
				setState(1533);
				resultExpressionMember();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnFeatureMemberContext extends ParserRuleContext {
		public MemberPrefixContext memberPrefix() {
			return getRuleContext(MemberPrefixContext.class,0);
		}
		public TerminalNode RETURN() { return getToken(KerMLParser.RETURN, 0); }
		public FeatureElementContext featureElement() {
			return getRuleContext(FeatureElementContext.class,0);
		}
		public ReturnFeatureMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnFeatureMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterReturnFeatureMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitReturnFeatureMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitReturnFeatureMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnFeatureMemberContext returnFeatureMember() throws RecognitionException {
		ReturnFeatureMemberContext _localctx = new ReturnFeatureMemberContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_returnFeatureMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1536);
			memberPrefix();
			setState(1537);
			match(RETURN);
			setState(1538);
			featureElement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ResultExpressionMemberContext extends ParserRuleContext {
		public MemberPrefixContext memberPrefix() {
			return getRuleContext(MemberPrefixContext.class,0);
		}
		public OwnedExpressionContext ownedExpression() {
			return getRuleContext(OwnedExpressionContext.class,0);
		}
		public ResultExpressionMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resultExpressionMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterResultExpressionMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitResultExpressionMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitResultExpressionMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ResultExpressionMemberContext resultExpressionMember() throws RecognitionException {
		ResultExpressionMemberContext _localctx = new ResultExpressionMemberContext(_ctx, getState());
		enterRule(_localctx, 264, RULE_resultExpressionMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1540);
			memberPrefix();
			setState(1541);
			ownedExpression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public FeaturePrefixContext featurePrefix() {
			return getRuleContext(FeaturePrefixContext.class,0);
		}
		public TerminalNode EXPR() { return getToken(KerMLParser.EXPR, 0); }
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public ValuePartContext valuePart() {
			return getRuleContext(ValuePartContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 266, RULE_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1543);
			featurePrefix();
			setState(1544);
			match(EXPR);
			setState(1546);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4456464L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4652225029457616929L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 67133441L) != 0)) {
				{
				setState(1545);
				featureDeclaration();
				}
			}

			setState(1549);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT || _la==COLON_EQUALS || _la==EQUALS) {
				{
				setState(1548);
				valuePart();
				}
			}

			setState(1551);
			functionBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PredicateContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode PREDICATE() { return getToken(KerMLParser.PREDICATE, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 268, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1553);
			typePrefix();
			setState(1554);
			match(PREDICATE);
			setState(1555);
			classifierDeclaration();
			setState(1556);
			functionBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BooleanExpressionContext extends ParserRuleContext {
		public FeaturePrefixContext featurePrefix() {
			return getRuleContext(FeaturePrefixContext.class,0);
		}
		public TerminalNode BOOL() { return getToken(KerMLParser.BOOL, 0); }
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public ValuePartContext valuePart() {
			return getRuleContext(ValuePartContext.class,0);
		}
		public BooleanExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBooleanExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBooleanExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBooleanExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanExpressionContext booleanExpression() throws RecognitionException {
		BooleanExpressionContext _localctx = new BooleanExpressionContext(_ctx, getState());
		enterRule(_localctx, 270, RULE_booleanExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1558);
			featurePrefix();
			setState(1559);
			match(BOOL);
			setState(1561);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4456464L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4652225029457616929L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 67133441L) != 0)) {
				{
				setState(1560);
				featureDeclaration();
				}
			}

			setState(1564);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT || _la==COLON_EQUALS || _la==EQUALS) {
				{
				setState(1563);
				valuePart();
				}
			}

			setState(1566);
			functionBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InvariantContext extends ParserRuleContext {
		public Token isNegated;
		public FeaturePrefixContext featurePrefix() {
			return getRuleContext(FeaturePrefixContext.class,0);
		}
		public TerminalNode INV() { return getToken(KerMLParser.INV, 0); }
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public TerminalNode TRUE() { return getToken(KerMLParser.TRUE, 0); }
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public ValuePartContext valuePart() {
			return getRuleContext(ValuePartContext.class,0);
		}
		public TerminalNode FALSE() { return getToken(KerMLParser.FALSE, 0); }
		public InvariantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_invariant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterInvariant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitInvariant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitInvariant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InvariantContext invariant() throws RecognitionException {
		InvariantContext _localctx = new InvariantContext(_ctx, getState());
		enterRule(_localctx, 272, RULE_invariant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1568);
			featurePrefix();
			setState(1569);
			match(INV);
			setState(1572);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
				{
				setState(1570);
				match(TRUE);
				}
				break;
			case FALSE:
				{
				setState(1571);
				((InvariantContext)_localctx).isNegated = match(FALSE);
				}
				break;
			case ALL:
			case CONJUGATES:
			case CROSSES:
			case DEFAULT:
			case NONUNIQUE:
			case ORDERED:
			case REDEFINES:
			case REFERENCES:
			case SUBSETS:
			case TYPED:
			case DOUBLE_COLON_GT:
			case COLON_GT_GT:
			case COLON_EQUALS:
			case COLON_GT:
			case EQUALS_GT:
			case LBRACE:
			case LBRACKET:
			case SEMICOLON:
			case TILDE:
			case COLON:
			case LESS:
			case EQUALS:
			case NAME:
				break;
			default:
				break;
			}
			setState(1575);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4456464L) != 0) || ((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4652225029457616929L) != 0) || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 67133441L) != 0)) {
				{
				setState(1574);
				featureDeclaration();
				}
			}

			setState(1578);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT || _la==COLON_EQUALS || _la==EQUALS) {
				{
				setState(1577);
				valuePart();
				}
			}

			setState(1580);
			functionBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedExpressionReferenceMemberContext extends ParserRuleContext {
		public OwnedExpressionReferenceContext ownedExpressionReference() {
			return getRuleContext(OwnedExpressionReferenceContext.class,0);
		}
		public OwnedExpressionReferenceMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedExpressionReferenceMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedExpressionReferenceMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedExpressionReferenceMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedExpressionReferenceMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedExpressionReferenceMemberContext ownedExpressionReferenceMember() throws RecognitionException {
		OwnedExpressionReferenceMemberContext _localctx = new OwnedExpressionReferenceMemberContext(_ctx, getState());
		enterRule(_localctx, 274, RULE_ownedExpressionReferenceMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1582);
			ownedExpressionReference();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedExpressionReferenceContext extends ParserRuleContext {
		public OwnedExpressionMemberContext ownedExpressionMember() {
			return getRuleContext(OwnedExpressionMemberContext.class,0);
		}
		public OwnedExpressionReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedExpressionReference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedExpressionReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedExpressionReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedExpressionReference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedExpressionReferenceContext ownedExpressionReference() throws RecognitionException {
		OwnedExpressionReferenceContext _localctx = new OwnedExpressionReferenceContext(_ctx, getState());
		enterRule(_localctx, 276, RULE_ownedExpressionReference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1584);
			ownedExpressionMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedExpressionMemberContext extends ParserRuleContext {
		public OwnedExpressionContext ownedExpression() {
			return getRuleContext(OwnedExpressionContext.class,0);
		}
		public OwnedExpressionMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedExpressionMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedExpressionMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedExpressionMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedExpressionMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedExpressionMemberContext ownedExpressionMember() throws RecognitionException {
		OwnedExpressionMemberContext _localctx = new OwnedExpressionMemberContext(_ctx, getState());
		enterRule(_localctx, 278, RULE_ownedExpressionMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1586);
			ownedExpression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedExpressionContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(KerMLParser.IF, 0); }
		public List<OwnedExpressionContext> ownedExpression() {
			return getRuleContexts(OwnedExpressionContext.class);
		}
		public OwnedExpressionContext ownedExpression(int i) {
			return getRuleContext(OwnedExpressionContext.class,i);
		}
		public TerminalNode QUESTION() { return getToken(KerMLParser.QUESTION, 0); }
		public TerminalNode ELSE() { return getToken(KerMLParser.ELSE, 0); }
		public EmptyResultMemberContext emptyResultMember() {
			return getRuleContext(EmptyResultMemberContext.class,0);
		}
		public UnaryOperatorContext unaryOperator() {
			return getRuleContext(UnaryOperatorContext.class,0);
		}
		public ClassificationTestOperatorContext classificationTestOperator() {
			return getRuleContext(ClassificationTestOperatorContext.class,0);
		}
		public TypeReferenceMemberContext typeReferenceMember() {
			return getRuleContext(TypeReferenceMemberContext.class,0);
		}
		public CastOperatorContext castOperator() {
			return getRuleContext(CastOperatorContext.class,0);
		}
		public TypeResultMemberContext typeResultMember() {
			return getRuleContext(TypeResultMemberContext.class,0);
		}
		public MetadataAccessExpressionContext metadataAccessExpression() {
			return getRuleContext(MetadataAccessExpressionContext.class,0);
		}
		public MetaclassificationTestOperatorContext metaclassificationTestOperator() {
			return getRuleContext(MetaclassificationTestOperatorContext.class,0);
		}
		public MetacastOperatorContext metacastOperator() {
			return getRuleContext(MetacastOperatorContext.class,0);
		}
		public TerminalNode ALL() { return getToken(KerMLParser.ALL, 0); }
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public ConditionalBinaryOperatorContext conditionalBinaryOperator() {
			return getRuleContext(ConditionalBinaryOperatorContext.class,0);
		}
		public BinaryOperatorContext binaryOperator() {
			return getRuleContext(BinaryOperatorContext.class,0);
		}
		public OwnedExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedExpressionContext ownedExpression() throws RecognitionException {
		return ownedExpression(0);
	}

	private OwnedExpressionContext ownedExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		OwnedExpressionContext _localctx = new OwnedExpressionContext(_ctx, _parentState);
		OwnedExpressionContext _prevctx = _localctx;
		int _startState = 280;
		enterRecursionRule(_localctx, 280, RULE_ownedExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1622);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,159,_ctx) ) {
			case 1:
				{
				setState(1589);
				match(IF);
				setState(1590);
				ownedExpression(0);
				setState(1591);
				match(QUESTION);
				setState(1592);
				ownedExpression(0);
				setState(1593);
				match(ELSE);
				setState(1594);
				ownedExpression(0);
				setState(1595);
				emptyResultMember();
				}
				break;
			case 2:
				{
				setState(1597);
				unaryOperator();
				setState(1598);
				ownedExpression(0);
				setState(1599);
				emptyResultMember();
				}
				break;
			case 3:
				{
				setState(1601);
				classificationTestOperator();
				setState(1602);
				typeReferenceMember();
				setState(1603);
				emptyResultMember();
				}
				break;
			case 4:
				{
				setState(1605);
				castOperator();
				setState(1606);
				typeResultMember();
				setState(1607);
				emptyResultMember();
				}
				break;
			case 5:
				{
				setState(1609);
				metadataAccessExpression();
				setState(1610);
				metaclassificationTestOperator();
				setState(1611);
				typeReferenceMember();
				setState(1612);
				emptyResultMember();
				}
				break;
			case 6:
				{
				setState(1614);
				metadataAccessExpression();
				setState(1615);
				metacastOperator();
				setState(1616);
				typeResultMember();
				setState(1617);
				emptyResultMember();
				}
				break;
			case 7:
				{
				setState(1619);
				match(ALL);
				setState(1620);
				typeReferenceMember();
				}
				break;
			case 8:
				{
				setState(1621);
				primaryExpression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1644);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,161,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1642);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,160,_ctx) ) {
					case 1:
						{
						_localctx = new OwnedExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_ownedExpression);
						setState(1624);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(1625);
						conditionalBinaryOperator();
						setState(1626);
						ownedExpression(12);
						}
						break;
					case 2:
						{
						_localctx = new OwnedExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_ownedExpression);
						setState(1628);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(1629);
						binaryOperator();
						setState(1630);
						ownedExpression(11);
						}
						break;
					case 3:
						{
						_localctx = new OwnedExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_ownedExpression);
						setState(1632);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1633);
						classificationTestOperator();
						setState(1634);
						typeReferenceMember();
						setState(1635);
						emptyResultMember();
						}
						break;
					case 4:
						{
						_localctx = new OwnedExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_ownedExpression);
						setState(1637);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1638);
						castOperator();
						setState(1639);
						typeResultMember();
						setState(1640);
						emptyResultMember();
						}
						break;
					}
					} 
				}
				setState(1646);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,161,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalBinaryOperatorContext extends ParserRuleContext {
		public TerminalNode DOUBLE_QUESTION() { return getToken(KerMLParser.DOUBLE_QUESTION, 0); }
		public TerminalNode OR() { return getToken(KerMLParser.OR, 0); }
		public TerminalNode AND() { return getToken(KerMLParser.AND, 0); }
		public TerminalNode IMPLIES() { return getToken(KerMLParser.IMPLIES, 0); }
		public ConditionalBinaryOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalBinaryOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterConditionalBinaryOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitConditionalBinaryOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitConditionalBinaryOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionalBinaryOperatorContext conditionalBinaryOperator() throws RecognitionException {
		ConditionalBinaryOperatorContext _localctx = new ConditionalBinaryOperatorContext(_ctx, getState());
		enterRule(_localctx, 282, RULE_conditionalBinaryOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1647);
			_la = _input.LA(1);
			if ( !(_la==AND || _la==IMPLIES || _la==OR || _la==DOUBLE_QUESTION) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BinaryOperatorExpressionContext extends ParserRuleContext {
		public BinaryOperatorContext operator;
		public List<ArgumentMemberContext> argumentMember() {
			return getRuleContexts(ArgumentMemberContext.class);
		}
		public ArgumentMemberContext argumentMember(int i) {
			return getRuleContext(ArgumentMemberContext.class,i);
		}
		public EmptyResultMemberContext emptyResultMember() {
			return getRuleContext(EmptyResultMemberContext.class,0);
		}
		public BinaryOperatorContext binaryOperator() {
			return getRuleContext(BinaryOperatorContext.class,0);
		}
		public BinaryOperatorExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binaryOperatorExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBinaryOperatorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBinaryOperatorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBinaryOperatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinaryOperatorExpressionContext binaryOperatorExpression() throws RecognitionException {
		BinaryOperatorExpressionContext _localctx = new BinaryOperatorExpressionContext(_ctx, getState());
		enterRule(_localctx, 284, RULE_binaryOperatorExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1649);
			argumentMember();
			setState(1650);
			((BinaryOperatorExpressionContext)_localctx).operator = binaryOperator();
			setState(1651);
			argumentMember();
			setState(1652);
			emptyResultMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BinaryOperatorContext extends ParserRuleContext {
		public TerminalNode PIPE() { return getToken(KerMLParser.PIPE, 0); }
		public TerminalNode AMPERSAND() { return getToken(KerMLParser.AMPERSAND, 0); }
		public TerminalNode XOR() { return getToken(KerMLParser.XOR, 0); }
		public TerminalNode DOUBLE_DOT() { return getToken(KerMLParser.DOUBLE_DOT, 0); }
		public TerminalNode DOUBLE_EQUALS() { return getToken(KerMLParser.DOUBLE_EQUALS, 0); }
		public TerminalNode EXCLAIM_EQUALS() { return getToken(KerMLParser.EXCLAIM_EQUALS, 0); }
		public TerminalNode TRIPLE_EQUALS() { return getToken(KerMLParser.TRIPLE_EQUALS, 0); }
		public TerminalNode EXCLAIM_EQUALS_EQUALS() { return getToken(KerMLParser.EXCLAIM_EQUALS_EQUALS, 0); }
		public TerminalNode LESS() { return getToken(KerMLParser.LESS, 0); }
		public TerminalNode GREATER() { return getToken(KerMLParser.GREATER, 0); }
		public TerminalNode LESS_EQUALS() { return getToken(KerMLParser.LESS_EQUALS, 0); }
		public TerminalNode GREATER_EQUALS() { return getToken(KerMLParser.GREATER_EQUALS, 0); }
		public TerminalNode PLUS() { return getToken(KerMLParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(KerMLParser.MINUS, 0); }
		public TerminalNode STAR() { return getToken(KerMLParser.STAR, 0); }
		public TerminalNode SLASH() { return getToken(KerMLParser.SLASH, 0); }
		public TerminalNode PERCENT() { return getToken(KerMLParser.PERCENT, 0); }
		public TerminalNode CARET() { return getToken(KerMLParser.CARET, 0); }
		public TerminalNode DOUBLE_STAR() { return getToken(KerMLParser.DOUBLE_STAR, 0); }
		public BinaryOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binaryOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBinaryOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBinaryOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBinaryOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinaryOperatorContext binaryOperator() throws RecognitionException {
		BinaryOperatorContext _localctx = new BinaryOperatorContext(_ctx, getState());
		enterRule(_localctx, 286, RULE_binaryOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1654);
			_la = _input.LA(1);
			if ( !(((((_la - 107)) & ~0x3f) == 0 && ((1L << (_la - 107)) & 5634460238841L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnaryOperatorExpressionContext extends ParserRuleContext {
		public UnaryOperatorContext operator;
		public ArgumentMemberContext argumentMember() {
			return getRuleContext(ArgumentMemberContext.class,0);
		}
		public EmptyResultMemberContext emptyResultMember() {
			return getRuleContext(EmptyResultMemberContext.class,0);
		}
		public UnaryOperatorContext unaryOperator() {
			return getRuleContext(UnaryOperatorContext.class,0);
		}
		public UnaryOperatorExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryOperatorExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterUnaryOperatorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitUnaryOperatorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitUnaryOperatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryOperatorExpressionContext unaryOperatorExpression() throws RecognitionException {
		UnaryOperatorExpressionContext _localctx = new UnaryOperatorExpressionContext(_ctx, getState());
		enterRule(_localctx, 288, RULE_unaryOperatorExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1656);
			((UnaryOperatorExpressionContext)_localctx).operator = unaryOperator();
			setState(1657);
			argumentMember();
			setState(1658);
			emptyResultMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnaryOperatorContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(KerMLParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(KerMLParser.MINUS, 0); }
		public TerminalNode TILDE() { return getToken(KerMLParser.TILDE, 0); }
		public TerminalNode NOT() { return getToken(KerMLParser.NOT, 0); }
		public UnaryOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterUnaryOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitUnaryOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitUnaryOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryOperatorContext unaryOperator() throws RecognitionException {
		UnaryOperatorContext _localctx = new UnaryOperatorContext(_ctx, getState());
		enterRule(_localctx, 290, RULE_unaryOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1660);
			_la = _input.LA(1);
			if ( !(_la==NOT || ((((_la - 133)) & ~0x3f) == 0 && ((1L << (_la - 133)) & 769L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassificationExpressionContext extends ParserRuleContext {
		public EmptyResultMemberContext emptyResultMember() {
			return getRuleContext(EmptyResultMemberContext.class,0);
		}
		public ClassificationTestOperatorContext classificationTestOperator() {
			return getRuleContext(ClassificationTestOperatorContext.class,0);
		}
		public TypeReferenceMemberContext typeReferenceMember() {
			return getRuleContext(TypeReferenceMemberContext.class,0);
		}
		public CastOperatorContext castOperator() {
			return getRuleContext(CastOperatorContext.class,0);
		}
		public TypeResultMemberContext typeResultMember() {
			return getRuleContext(TypeResultMemberContext.class,0);
		}
		public ArgumentMemberContext argumentMember() {
			return getRuleContext(ArgumentMemberContext.class,0);
		}
		public ClassificationExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classificationExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterClassificationExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitClassificationExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitClassificationExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassificationExpressionContext classificationExpression() throws RecognitionException {
		ClassificationExpressionContext _localctx = new ClassificationExpressionContext(_ctx, getState());
		enterRule(_localctx, 292, RULE_classificationExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1663);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,162,_ctx) ) {
			case 1:
				{
				setState(1662);
				argumentMember();
				}
				break;
			}
			setState(1671);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case HASTYPE:
			case ISTYPE:
			case AT:
				{
				setState(1665);
				classificationTestOperator();
				setState(1666);
				typeReferenceMember();
				}
				break;
			case AS:
				{
				setState(1668);
				castOperator();
				setState(1669);
				typeResultMember();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1673);
			emptyResultMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassificationTestOperatorContext extends ParserRuleContext {
		public TerminalNode ISTYPE() { return getToken(KerMLParser.ISTYPE, 0); }
		public TerminalNode HASTYPE() { return getToken(KerMLParser.HASTYPE, 0); }
		public TerminalNode AT() { return getToken(KerMLParser.AT, 0); }
		public ClassificationTestOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classificationTestOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterClassificationTestOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitClassificationTestOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitClassificationTestOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassificationTestOperatorContext classificationTestOperator() throws RecognitionException {
		ClassificationTestOperatorContext _localctx = new ClassificationTestOperatorContext(_ctx, getState());
		enterRule(_localctx, 294, RULE_classificationTestOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1675);
			_la = _input.LA(1);
			if ( !(_la==HASTYPE || _la==ISTYPE || _la==AT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CastOperatorContext extends ParserRuleContext {
		public TerminalNode AS() { return getToken(KerMLParser.AS, 0); }
		public CastOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_castOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterCastOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitCastOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitCastOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastOperatorContext castOperator() throws RecognitionException {
		CastOperatorContext _localctx = new CastOperatorContext(_ctx, getState());
		enterRule(_localctx, 296, RULE_castOperator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1677);
			match(AS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetaclassificationExpressionContext extends ParserRuleContext {
		public MetadataArgumentMemberContext metadataArgumentMember() {
			return getRuleContext(MetadataArgumentMemberContext.class,0);
		}
		public EmptyResultMemberContext emptyResultMember() {
			return getRuleContext(EmptyResultMemberContext.class,0);
		}
		public MetaclassificationTestOperatorContext metaclassificationTestOperator() {
			return getRuleContext(MetaclassificationTestOperatorContext.class,0);
		}
		public TypeReferenceMemberContext typeReferenceMember() {
			return getRuleContext(TypeReferenceMemberContext.class,0);
		}
		public MetacastOperatorContext metacastOperator() {
			return getRuleContext(MetacastOperatorContext.class,0);
		}
		public TypeResultMemberContext typeResultMember() {
			return getRuleContext(TypeResultMemberContext.class,0);
		}
		public MetaclassificationExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metaclassificationExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetaclassificationExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetaclassificationExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetaclassificationExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetaclassificationExpressionContext metaclassificationExpression() throws RecognitionException {
		MetaclassificationExpressionContext _localctx = new MetaclassificationExpressionContext(_ctx, getState());
		enterRule(_localctx, 298, RULE_metaclassificationExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1679);
			metadataArgumentMember();
			setState(1686);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AT:
				{
				setState(1680);
				metaclassificationTestOperator();
				setState(1681);
				typeReferenceMember();
				}
				break;
			case META:
				{
				setState(1683);
				metacastOperator();
				setState(1684);
				typeResultMember();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1688);
			emptyResultMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentMemberContext extends ParserRuleContext {
		public ArgumentContext argument() {
			return getRuleContext(ArgumentContext.class,0);
		}
		public ArgumentMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterArgumentMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitArgumentMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitArgumentMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentMemberContext argumentMember() throws RecognitionException {
		ArgumentMemberContext _localctx = new ArgumentMemberContext(_ctx, getState());
		enterRule(_localctx, 300, RULE_argumentMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1690);
			argument();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentContext extends ParserRuleContext {
		public ArgumentValueContext argumentValue() {
			return getRuleContext(ArgumentValueContext.class,0);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 302, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1692);
			argumentValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentValueContext extends ParserRuleContext {
		public OwnedExpressionContext ownedExpression() {
			return getRuleContext(OwnedExpressionContext.class,0);
		}
		public ArgumentValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterArgumentValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitArgumentValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitArgumentValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentValueContext argumentValue() throws RecognitionException {
		ArgumentValueContext _localctx = new ArgumentValueContext(_ctx, getState());
		enterRule(_localctx, 304, RULE_argumentValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1694);
			ownedExpression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentExpressionMemberContext extends ParserRuleContext {
		public ArgumentExpressionContext argumentExpression() {
			return getRuleContext(ArgumentExpressionContext.class,0);
		}
		public ArgumentExpressionMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentExpressionMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterArgumentExpressionMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitArgumentExpressionMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitArgumentExpressionMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentExpressionMemberContext argumentExpressionMember() throws RecognitionException {
		ArgumentExpressionMemberContext _localctx = new ArgumentExpressionMemberContext(_ctx, getState());
		enterRule(_localctx, 306, RULE_argumentExpressionMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1696);
			argumentExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentExpressionContext extends ParserRuleContext {
		public ArgumentExpressionValueContext argumentExpressionValue() {
			return getRuleContext(ArgumentExpressionValueContext.class,0);
		}
		public ArgumentExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterArgumentExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitArgumentExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitArgumentExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentExpressionContext argumentExpression() throws RecognitionException {
		ArgumentExpressionContext _localctx = new ArgumentExpressionContext(_ctx, getState());
		enterRule(_localctx, 308, RULE_argumentExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1698);
			argumentExpressionValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentExpressionValueContext extends ParserRuleContext {
		public OwnedExpressionReferenceContext ownedExpressionReference() {
			return getRuleContext(OwnedExpressionReferenceContext.class,0);
		}
		public ArgumentExpressionValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentExpressionValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterArgumentExpressionValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitArgumentExpressionValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitArgumentExpressionValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentExpressionValueContext argumentExpressionValue() throws RecognitionException {
		ArgumentExpressionValueContext _localctx = new ArgumentExpressionValueContext(_ctx, getState());
		enterRule(_localctx, 310, RULE_argumentExpressionValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1700);
			ownedExpressionReference();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetadataArgumentMemberContext extends ParserRuleContext {
		public MetadataArgumentContext metadataArgument() {
			return getRuleContext(MetadataArgumentContext.class,0);
		}
		public MetadataArgumentMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metadataArgumentMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetadataArgumentMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetadataArgumentMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetadataArgumentMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetadataArgumentMemberContext metadataArgumentMember() throws RecognitionException {
		MetadataArgumentMemberContext _localctx = new MetadataArgumentMemberContext(_ctx, getState());
		enterRule(_localctx, 312, RULE_metadataArgumentMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1702);
			metadataArgument();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetadataArgumentContext extends ParserRuleContext {
		public MetadataValueContext metadataValue() {
			return getRuleContext(MetadataValueContext.class,0);
		}
		public MetadataArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metadataArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetadataArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetadataArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetadataArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetadataArgumentContext metadataArgument() throws RecognitionException {
		MetadataArgumentContext _localctx = new MetadataArgumentContext(_ctx, getState());
		enterRule(_localctx, 314, RULE_metadataArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1704);
			metadataValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetadataValueContext extends ParserRuleContext {
		public MetadataReferenceContext metadataReference() {
			return getRuleContext(MetadataReferenceContext.class,0);
		}
		public MetadataValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metadataValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetadataValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetadataValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetadataValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetadataValueContext metadataValue() throws RecognitionException {
		MetadataValueContext _localctx = new MetadataValueContext(_ctx, getState());
		enterRule(_localctx, 316, RULE_metadataValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1706);
			metadataReference();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetadataReferenceContext extends ParserRuleContext {
		public ElementReferenceMemberContext elementReferenceMember() {
			return getRuleContext(ElementReferenceMemberContext.class,0);
		}
		public MetadataReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metadataReference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetadataReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetadataReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetadataReference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetadataReferenceContext metadataReference() throws RecognitionException {
		MetadataReferenceContext _localctx = new MetadataReferenceContext(_ctx, getState());
		enterRule(_localctx, 318, RULE_metadataReference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1708);
			elementReferenceMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetaclassificationTestOperatorContext extends ParserRuleContext {
		public List<TerminalNode> AT() { return getTokens(KerMLParser.AT); }
		public TerminalNode AT(int i) {
			return getToken(KerMLParser.AT, i);
		}
		public MetaclassificationTestOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metaclassificationTestOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetaclassificationTestOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetaclassificationTestOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetaclassificationTestOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetaclassificationTestOperatorContext metaclassificationTestOperator() throws RecognitionException {
		MetaclassificationTestOperatorContext _localctx = new MetaclassificationTestOperatorContext(_ctx, getState());
		enterRule(_localctx, 320, RULE_metaclassificationTestOperator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1710);
			match(AT);
			setState(1711);
			match(AT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetacastOperatorContext extends ParserRuleContext {
		public TerminalNode META() { return getToken(KerMLParser.META, 0); }
		public MetacastOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metacastOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetacastOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetacastOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetacastOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetacastOperatorContext metacastOperator() throws RecognitionException {
		MetacastOperatorContext _localctx = new MetacastOperatorContext(_ctx, getState());
		enterRule(_localctx, 322, RULE_metacastOperator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1713);
			match(META);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExtentExpressionContext extends ParserRuleContext {
		public Token operator;
		public TypeReferenceMemberContext typeReferenceMember() {
			return getRuleContext(TypeReferenceMemberContext.class,0);
		}
		public TerminalNode ALL() { return getToken(KerMLParser.ALL, 0); }
		public ExtentExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extentExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterExtentExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitExtentExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitExtentExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExtentExpressionContext extentExpression() throws RecognitionException {
		ExtentExpressionContext _localctx = new ExtentExpressionContext(_ctx, getState());
		enterRule(_localctx, 324, RULE_extentExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1715);
			((ExtentExpressionContext)_localctx).operator = match(ALL);
			setState(1716);
			typeReferenceMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeReferenceMemberContext extends ParserRuleContext {
		public TypeReferenceContext typeReference() {
			return getRuleContext(TypeReferenceContext.class,0);
		}
		public TypeReferenceMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeReferenceMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypeReferenceMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypeReferenceMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypeReferenceMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeReferenceMemberContext typeReferenceMember() throws RecognitionException {
		TypeReferenceMemberContext _localctx = new TypeReferenceMemberContext(_ctx, getState());
		enterRule(_localctx, 326, RULE_typeReferenceMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1718);
			typeReference();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeResultMemberContext extends ParserRuleContext {
		public TypeReferenceContext typeReference() {
			return getRuleContext(TypeReferenceContext.class,0);
		}
		public TypeResultMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeResultMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypeResultMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypeResultMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypeResultMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeResultMemberContext typeResultMember() throws RecognitionException {
		TypeResultMemberContext _localctx = new TypeResultMemberContext(_ctx, getState());
		enterRule(_localctx, 328, RULE_typeResultMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1720);
			typeReference();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeReferenceContext extends ParserRuleContext {
		public ReferenceTypingContext referenceTyping() {
			return getRuleContext(ReferenceTypingContext.class,0);
		}
		public TypeReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeReference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterTypeReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitTypeReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitTypeReference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeReferenceContext typeReference() throws RecognitionException {
		TypeReferenceContext _localctx = new TypeReferenceContext(_ctx, getState());
		enterRule(_localctx, 330, RULE_typeReference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1722);
			referenceTyping();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReferenceTypingContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public ReferenceTypingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_referenceTyping; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterReferenceTyping(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitReferenceTyping(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitReferenceTyping(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReferenceTypingContext referenceTyping() throws RecognitionException {
		ReferenceTypingContext _localctx = new ReferenceTypingContext(_ctx, getState());
		enterRule(_localctx, 332, RULE_referenceTyping);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1724);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EmptyResultMemberContext extends ParserRuleContext {
		public EmptyFeatureContext emptyFeature() {
			return getRuleContext(EmptyFeatureContext.class,0);
		}
		public EmptyResultMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyResultMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterEmptyResultMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitEmptyResultMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitEmptyResultMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyResultMemberContext emptyResultMember() throws RecognitionException {
		EmptyResultMemberContext _localctx = new EmptyResultMemberContext(_ctx, getState());
		enterRule(_localctx, 334, RULE_emptyResultMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1726);
			emptyFeature();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EmptyFeatureContext extends ParserRuleContext {
		public EmptyFeatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyFeature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterEmptyFeature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitEmptyFeature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitEmptyFeature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyFeatureContext emptyFeature() throws RecognitionException {
		EmptyFeatureContext _localctx = new EmptyFeatureContext(_ctx, getState());
		enterRule(_localctx, 336, RULE_emptyFeature);
		try {
			enterOuterAlt(_localctx, 1);
			{
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryExpressionContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(KerMLParser.LPAREN, 0); }
		public SequenceExpressionListContext sequenceExpressionList() {
			return getRuleContext(SequenceExpressionListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(KerMLParser.RPAREN, 0); }
		public BaseExpressionContext baseExpression() {
			return getRuleContext(BaseExpressionContext.class,0);
		}
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public TerminalNode LBRACKET() { return getToken(KerMLParser.LBRACKET, 0); }
		public SequenceExpressionListMemberContext sequenceExpressionListMember() {
			return getRuleContext(SequenceExpressionListMemberContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(KerMLParser.RBRACKET, 0); }
		public TerminalNode HASH() { return getToken(KerMLParser.HASH, 0); }
		public TerminalNode DOT() { return getToken(KerMLParser.DOT, 0); }
		public FeatureChainMemberContext featureChainMember() {
			return getRuleContext(FeatureChainMemberContext.class,0);
		}
		public BodyArgumentMemberContext bodyArgumentMember() {
			return getRuleContext(BodyArgumentMemberContext.class,0);
		}
		public TerminalNode DOT_QUESTION() { return getToken(KerMLParser.DOT_QUESTION, 0); }
		public TerminalNode ARROW() { return getToken(KerMLParser.ARROW, 0); }
		public InvocationTypeMemberContext invocationTypeMember() {
			return getRuleContext(InvocationTypeMemberContext.class,0);
		}
		public EmptyResultMemberContext emptyResultMember() {
			return getRuleContext(EmptyResultMemberContext.class,0);
		}
		public FunctionReferenceArgumentMemberContext functionReferenceArgumentMember() {
			return getRuleContext(FunctionReferenceArgumentMemberContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		return primaryExpression(0);
	}

	private PrimaryExpressionContext primaryExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, _parentState);
		PrimaryExpressionContext _prevctx = _localctx;
		int _startState = 338;
		enterRecursionRule(_localctx, 338, RULE_primaryExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1736);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,165,_ctx) ) {
			case 1:
				{
				setState(1731);
				match(LPAREN);
				setState(1732);
				sequenceExpressionList();
				setState(1733);
				match(RPAREN);
				}
				break;
			case 2:
				{
				setState(1735);
				baseExpression();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1770);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,168,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1768);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,167,_ctx) ) {
					case 1:
						{
						_localctx = new PrimaryExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpression);
						setState(1738);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1739);
						match(LBRACKET);
						setState(1740);
						sequenceExpressionListMember();
						setState(1741);
						match(RBRACKET);
						}
						break;
					case 2:
						{
						_localctx = new PrimaryExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpression);
						setState(1743);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1744);
						match(HASH);
						setState(1745);
						match(LPAREN);
						setState(1746);
						sequenceExpressionListMember();
						setState(1747);
						match(RPAREN);
						}
						break;
					case 3:
						{
						_localctx = new PrimaryExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpression);
						setState(1749);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1750);
						match(DOT);
						setState(1751);
						featureChainMember();
						}
						break;
					case 4:
						{
						_localctx = new PrimaryExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpression);
						setState(1752);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1753);
						match(DOT);
						setState(1754);
						bodyArgumentMember();
						}
						break;
					case 5:
						{
						_localctx = new PrimaryExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpression);
						setState(1755);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1756);
						match(DOT_QUESTION);
						setState(1757);
						bodyArgumentMember();
						}
						break;
					case 6:
						{
						_localctx = new PrimaryExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExpression);
						setState(1758);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(1759);
						match(ARROW);
						setState(1760);
						invocationTypeMember();
						setState(1764);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case LBRACE:
							{
							setState(1761);
							bodyArgumentMember();
							}
							break;
						case DOLLAR:
						case NAME:
							{
							setState(1762);
							functionReferenceArgumentMember();
							}
							break;
						case LPAREN:
							{
							setState(1763);
							argumentList();
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(1766);
						emptyResultMember();
						}
						break;
					}
					} 
				}
				setState(1772);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,168,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryArgumentValueContext extends ParserRuleContext {
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public PrimaryArgumentValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryArgumentValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPrimaryArgumentValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPrimaryArgumentValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPrimaryArgumentValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryArgumentValueContext primaryArgumentValue() throws RecognitionException {
		PrimaryArgumentValueContext _localctx = new PrimaryArgumentValueContext(_ctx, getState());
		enterRule(_localctx, 340, RULE_primaryArgumentValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1773);
			primaryExpression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryArgumentContext extends ParserRuleContext {
		public PrimaryArgumentValueContext primaryArgumentValue() {
			return getRuleContext(PrimaryArgumentValueContext.class,0);
		}
		public PrimaryArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPrimaryArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPrimaryArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPrimaryArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryArgumentContext primaryArgument() throws RecognitionException {
		PrimaryArgumentContext _localctx = new PrimaryArgumentContext(_ctx, getState());
		enterRule(_localctx, 342, RULE_primaryArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1775);
			primaryArgumentValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryArgumentMemberContext extends ParserRuleContext {
		public PrimaryArgumentContext primaryArgument() {
			return getRuleContext(PrimaryArgumentContext.class,0);
		}
		public PrimaryArgumentMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryArgumentMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPrimaryArgumentMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPrimaryArgumentMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPrimaryArgumentMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryArgumentMemberContext primaryArgumentMember() throws RecognitionException {
		PrimaryArgumentMemberContext _localctx = new PrimaryArgumentMemberContext(_ctx, getState());
		enterRule(_localctx, 344, RULE_primaryArgumentMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1777);
			primaryArgument();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NonFeatureChainPrimaryExpressionContext extends ParserRuleContext {
		public BracketExpressionContext bracketExpression() {
			return getRuleContext(BracketExpressionContext.class,0);
		}
		public IndexExpressionContext indexExpression() {
			return getRuleContext(IndexExpressionContext.class,0);
		}
		public SequenceExpressionContext sequenceExpression() {
			return getRuleContext(SequenceExpressionContext.class,0);
		}
		public SelectExpressionContext selectExpression() {
			return getRuleContext(SelectExpressionContext.class,0);
		}
		public CollectExpressionContext collectExpression() {
			return getRuleContext(CollectExpressionContext.class,0);
		}
		public FunctionOperationExpressionContext functionOperationExpression() {
			return getRuleContext(FunctionOperationExpressionContext.class,0);
		}
		public BaseExpressionContext baseExpression() {
			return getRuleContext(BaseExpressionContext.class,0);
		}
		public NonFeatureChainPrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonFeatureChainPrimaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNonFeatureChainPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNonFeatureChainPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNonFeatureChainPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonFeatureChainPrimaryExpressionContext nonFeatureChainPrimaryExpression() throws RecognitionException {
		NonFeatureChainPrimaryExpressionContext _localctx = new NonFeatureChainPrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 346, RULE_nonFeatureChainPrimaryExpression);
		try {
			setState(1786);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,169,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1779);
				bracketExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1780);
				indexExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1781);
				sequenceExpression();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1782);
				selectExpression();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1783);
				collectExpression();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1784);
				functionOperationExpression();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1785);
				baseExpression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NonFeatureChainPrimaryArgumentValueContext extends ParserRuleContext {
		public NonFeatureChainPrimaryExpressionContext nonFeatureChainPrimaryExpression() {
			return getRuleContext(NonFeatureChainPrimaryExpressionContext.class,0);
		}
		public NonFeatureChainPrimaryArgumentValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonFeatureChainPrimaryArgumentValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNonFeatureChainPrimaryArgumentValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNonFeatureChainPrimaryArgumentValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNonFeatureChainPrimaryArgumentValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonFeatureChainPrimaryArgumentValueContext nonFeatureChainPrimaryArgumentValue() throws RecognitionException {
		NonFeatureChainPrimaryArgumentValueContext _localctx = new NonFeatureChainPrimaryArgumentValueContext(_ctx, getState());
		enterRule(_localctx, 348, RULE_nonFeatureChainPrimaryArgumentValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1788);
			nonFeatureChainPrimaryExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NonFeatureChainPrimaryArgumentContext extends ParserRuleContext {
		public NonFeatureChainPrimaryArgumentValueContext nonFeatureChainPrimaryArgumentValue() {
			return getRuleContext(NonFeatureChainPrimaryArgumentValueContext.class,0);
		}
		public NonFeatureChainPrimaryArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonFeatureChainPrimaryArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNonFeatureChainPrimaryArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNonFeatureChainPrimaryArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNonFeatureChainPrimaryArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonFeatureChainPrimaryArgumentContext nonFeatureChainPrimaryArgument() throws RecognitionException {
		NonFeatureChainPrimaryArgumentContext _localctx = new NonFeatureChainPrimaryArgumentContext(_ctx, getState());
		enterRule(_localctx, 350, RULE_nonFeatureChainPrimaryArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1790);
			nonFeatureChainPrimaryArgumentValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NonFeatureChainPrimaryArgumentMemberContext extends ParserRuleContext {
		public PrimaryArgumentContext primaryArgument() {
			return getRuleContext(PrimaryArgumentContext.class,0);
		}
		public NonFeatureChainPrimaryArgumentMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonFeatureChainPrimaryArgumentMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNonFeatureChainPrimaryArgumentMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNonFeatureChainPrimaryArgumentMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNonFeatureChainPrimaryArgumentMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonFeatureChainPrimaryArgumentMemberContext nonFeatureChainPrimaryArgumentMember() throws RecognitionException {
		NonFeatureChainPrimaryArgumentMemberContext _localctx = new NonFeatureChainPrimaryArgumentMemberContext(_ctx, getState());
		enterRule(_localctx, 352, RULE_nonFeatureChainPrimaryArgumentMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1792);
			primaryArgument();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BracketExpressionContext extends ParserRuleContext {
		public Token operator;
		public PrimaryArgumentMemberContext primaryArgumentMember() {
			return getRuleContext(PrimaryArgumentMemberContext.class,0);
		}
		public SequenceExpressionListMemberContext sequenceExpressionListMember() {
			return getRuleContext(SequenceExpressionListMemberContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(KerMLParser.RBRACKET, 0); }
		public TerminalNode LBRACKET() { return getToken(KerMLParser.LBRACKET, 0); }
		public BracketExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bracketExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBracketExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBracketExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBracketExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BracketExpressionContext bracketExpression() throws RecognitionException {
		BracketExpressionContext _localctx = new BracketExpressionContext(_ctx, getState());
		enterRule(_localctx, 354, RULE_bracketExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1794);
			primaryArgumentMember();
			setState(1795);
			((BracketExpressionContext)_localctx).operator = match(LBRACKET);
			setState(1796);
			sequenceExpressionListMember();
			setState(1797);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IndexExpressionContext extends ParserRuleContext {
		public PrimaryArgumentMemberContext primaryArgumentMember() {
			return getRuleContext(PrimaryArgumentMemberContext.class,0);
		}
		public TerminalNode HASH() { return getToken(KerMLParser.HASH, 0); }
		public TerminalNode LPAREN() { return getToken(KerMLParser.LPAREN, 0); }
		public SequenceExpressionListMemberContext sequenceExpressionListMember() {
			return getRuleContext(SequenceExpressionListMemberContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(KerMLParser.RPAREN, 0); }
		public IndexExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indexExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterIndexExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitIndexExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitIndexExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexExpressionContext indexExpression() throws RecognitionException {
		IndexExpressionContext _localctx = new IndexExpressionContext(_ctx, getState());
		enterRule(_localctx, 356, RULE_indexExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1799);
			primaryArgumentMember();
			setState(1800);
			match(HASH);
			setState(1801);
			match(LPAREN);
			setState(1802);
			sequenceExpressionListMember();
			setState(1803);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SequenceExpressionContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(KerMLParser.LPAREN, 0); }
		public SequenceExpressionListContext sequenceExpressionList() {
			return getRuleContext(SequenceExpressionListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(KerMLParser.RPAREN, 0); }
		public SequenceExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequenceExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSequenceExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSequenceExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSequenceExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceExpressionContext sequenceExpression() throws RecognitionException {
		SequenceExpressionContext _localctx = new SequenceExpressionContext(_ctx, getState());
		enterRule(_localctx, 358, RULE_sequenceExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1805);
			match(LPAREN);
			setState(1806);
			sequenceExpressionList();
			setState(1807);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SequenceExpressionListContext extends ParserRuleContext {
		public OwnedExpressionContext ownedExpression() {
			return getRuleContext(OwnedExpressionContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(KerMLParser.COMMA, 0); }
		public SequenceOperatorExpressionContext sequenceOperatorExpression() {
			return getRuleContext(SequenceOperatorExpressionContext.class,0);
		}
		public SequenceExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequenceExpressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSequenceExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSequenceExpressionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSequenceExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceExpressionListContext sequenceExpressionList() throws RecognitionException {
		SequenceExpressionListContext _localctx = new SequenceExpressionListContext(_ctx, getState());
		enterRule(_localctx, 360, RULE_sequenceExpressionList);
		int _la;
		try {
			setState(1814);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,171,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1809);
				ownedExpression(0);
				setState(1811);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(1810);
					match(COMMA);
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1813);
				sequenceOperatorExpression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SequenceOperatorExpressionContext extends ParserRuleContext {
		public Token operator;
		public OwnedExpressionMemberContext ownedExpressionMember() {
			return getRuleContext(OwnedExpressionMemberContext.class,0);
		}
		public SequenceExpressionListMemberContext sequenceExpressionListMember() {
			return getRuleContext(SequenceExpressionListMemberContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(KerMLParser.COMMA, 0); }
		public SequenceOperatorExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequenceOperatorExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSequenceOperatorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSequenceOperatorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSequenceOperatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceOperatorExpressionContext sequenceOperatorExpression() throws RecognitionException {
		SequenceOperatorExpressionContext _localctx = new SequenceOperatorExpressionContext(_ctx, getState());
		enterRule(_localctx, 362, RULE_sequenceOperatorExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1816);
			ownedExpressionMember();
			setState(1817);
			((SequenceOperatorExpressionContext)_localctx).operator = match(COMMA);
			setState(1818);
			sequenceExpressionListMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SequenceExpressionListMemberContext extends ParserRuleContext {
		public SequenceExpressionListContext sequenceExpressionList() {
			return getRuleContext(SequenceExpressionListContext.class,0);
		}
		public SequenceExpressionListMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequenceExpressionListMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSequenceExpressionListMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSequenceExpressionListMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSequenceExpressionListMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceExpressionListMemberContext sequenceExpressionListMember() throws RecognitionException {
		SequenceExpressionListMemberContext _localctx = new SequenceExpressionListMemberContext(_ctx, getState());
		enterRule(_localctx, 364, RULE_sequenceExpressionListMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1820);
			sequenceExpressionList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureChainExpressionContext extends ParserRuleContext {
		public NonFeatureChainPrimaryArgumentMemberContext nonFeatureChainPrimaryArgumentMember() {
			return getRuleContext(NonFeatureChainPrimaryArgumentMemberContext.class,0);
		}
		public TerminalNode DOT() { return getToken(KerMLParser.DOT, 0); }
		public FeatureChainMemberContext featureChainMember() {
			return getRuleContext(FeatureChainMemberContext.class,0);
		}
		public FeatureChainExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureChainExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureChainExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureChainExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureChainExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureChainExpressionContext featureChainExpression() throws RecognitionException {
		FeatureChainExpressionContext _localctx = new FeatureChainExpressionContext(_ctx, getState());
		enterRule(_localctx, 366, RULE_featureChainExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1822);
			nonFeatureChainPrimaryArgumentMember();
			setState(1823);
			match(DOT);
			setState(1824);
			featureChainMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CollectExpressionContext extends ParserRuleContext {
		public PrimaryArgumentMemberContext primaryArgumentMember() {
			return getRuleContext(PrimaryArgumentMemberContext.class,0);
		}
		public TerminalNode DOT() { return getToken(KerMLParser.DOT, 0); }
		public BodyArgumentMemberContext bodyArgumentMember() {
			return getRuleContext(BodyArgumentMemberContext.class,0);
		}
		public CollectExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collectExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterCollectExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitCollectExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitCollectExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CollectExpressionContext collectExpression() throws RecognitionException {
		CollectExpressionContext _localctx = new CollectExpressionContext(_ctx, getState());
		enterRule(_localctx, 368, RULE_collectExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1826);
			primaryArgumentMember();
			setState(1827);
			match(DOT);
			setState(1828);
			bodyArgumentMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectExpressionContext extends ParserRuleContext {
		public PrimaryArgumentMemberContext primaryArgumentMember() {
			return getRuleContext(PrimaryArgumentMemberContext.class,0);
		}
		public TerminalNode DOT_QUESTION() { return getToken(KerMLParser.DOT_QUESTION, 0); }
		public BodyArgumentMemberContext bodyArgumentMember() {
			return getRuleContext(BodyArgumentMemberContext.class,0);
		}
		public SelectExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSelectExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSelectExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSelectExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectExpressionContext selectExpression() throws RecognitionException {
		SelectExpressionContext _localctx = new SelectExpressionContext(_ctx, getState());
		enterRule(_localctx, 370, RULE_selectExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1830);
			primaryArgumentMember();
			setState(1831);
			match(DOT_QUESTION);
			setState(1832);
			bodyArgumentMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionOperationExpressionContext extends ParserRuleContext {
		public PrimaryArgumentMemberContext primaryArgumentMember() {
			return getRuleContext(PrimaryArgumentMemberContext.class,0);
		}
		public TerminalNode ARROW() { return getToken(KerMLParser.ARROW, 0); }
		public InvocationTypeMemberContext invocationTypeMember() {
			return getRuleContext(InvocationTypeMemberContext.class,0);
		}
		public EmptyResultMemberContext emptyResultMember() {
			return getRuleContext(EmptyResultMemberContext.class,0);
		}
		public BodyArgumentMemberContext bodyArgumentMember() {
			return getRuleContext(BodyArgumentMemberContext.class,0);
		}
		public FunctionReferenceArgumentMemberContext functionReferenceArgumentMember() {
			return getRuleContext(FunctionReferenceArgumentMemberContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public FunctionOperationExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionOperationExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFunctionOperationExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFunctionOperationExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFunctionOperationExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionOperationExpressionContext functionOperationExpression() throws RecognitionException {
		FunctionOperationExpressionContext _localctx = new FunctionOperationExpressionContext(_ctx, getState());
		enterRule(_localctx, 372, RULE_functionOperationExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1834);
			primaryArgumentMember();
			setState(1835);
			match(ARROW);
			setState(1836);
			invocationTypeMember();
			setState(1840);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACE:
				{
				setState(1837);
				bodyArgumentMember();
				}
				break;
			case DOLLAR:
			case NAME:
				{
				setState(1838);
				functionReferenceArgumentMember();
				}
				break;
			case LPAREN:
				{
				setState(1839);
				argumentList();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1842);
			emptyResultMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BodyArgumentMemberContext extends ParserRuleContext {
		public BodyArgumentContext bodyArgument() {
			return getRuleContext(BodyArgumentContext.class,0);
		}
		public BodyArgumentMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bodyArgumentMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBodyArgumentMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBodyArgumentMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBodyArgumentMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyArgumentMemberContext bodyArgumentMember() throws RecognitionException {
		BodyArgumentMemberContext _localctx = new BodyArgumentMemberContext(_ctx, getState());
		enterRule(_localctx, 374, RULE_bodyArgumentMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1844);
			bodyArgument();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BodyArgumentContext extends ParserRuleContext {
		public BodyArgumentValueContext bodyArgumentValue() {
			return getRuleContext(BodyArgumentValueContext.class,0);
		}
		public BodyArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bodyArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBodyArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBodyArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBodyArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyArgumentContext bodyArgument() throws RecognitionException {
		BodyArgumentContext _localctx = new BodyArgumentContext(_ctx, getState());
		enterRule(_localctx, 376, RULE_bodyArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1846);
			bodyArgumentValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BodyArgumentValueContext extends ParserRuleContext {
		public BodyExpressionContext bodyExpression() {
			return getRuleContext(BodyExpressionContext.class,0);
		}
		public BodyArgumentValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bodyArgumentValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBodyArgumentValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBodyArgumentValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBodyArgumentValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyArgumentValueContext bodyArgumentValue() throws RecognitionException {
		BodyArgumentValueContext _localctx = new BodyArgumentValueContext(_ctx, getState());
		enterRule(_localctx, 378, RULE_bodyArgumentValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1848);
			bodyExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionReferenceArgumentMemberContext extends ParserRuleContext {
		public FunctionReferenceArgumentContext functionReferenceArgument() {
			return getRuleContext(FunctionReferenceArgumentContext.class,0);
		}
		public FunctionReferenceArgumentMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionReferenceArgumentMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFunctionReferenceArgumentMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFunctionReferenceArgumentMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFunctionReferenceArgumentMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionReferenceArgumentMemberContext functionReferenceArgumentMember() throws RecognitionException {
		FunctionReferenceArgumentMemberContext _localctx = new FunctionReferenceArgumentMemberContext(_ctx, getState());
		enterRule(_localctx, 380, RULE_functionReferenceArgumentMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1850);
			functionReferenceArgument();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionReferenceArgumentContext extends ParserRuleContext {
		public FunctionReferenceArgumentValueContext functionReferenceArgumentValue() {
			return getRuleContext(FunctionReferenceArgumentValueContext.class,0);
		}
		public FunctionReferenceArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionReferenceArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFunctionReferenceArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFunctionReferenceArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFunctionReferenceArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionReferenceArgumentContext functionReferenceArgument() throws RecognitionException {
		FunctionReferenceArgumentContext _localctx = new FunctionReferenceArgumentContext(_ctx, getState());
		enterRule(_localctx, 382, RULE_functionReferenceArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1852);
			functionReferenceArgumentValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionReferenceArgumentValueContext extends ParserRuleContext {
		public FunctionReferenceExpressionContext functionReferenceExpression() {
			return getRuleContext(FunctionReferenceExpressionContext.class,0);
		}
		public FunctionReferenceArgumentValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionReferenceArgumentValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFunctionReferenceArgumentValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFunctionReferenceArgumentValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFunctionReferenceArgumentValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionReferenceArgumentValueContext functionReferenceArgumentValue() throws RecognitionException {
		FunctionReferenceArgumentValueContext _localctx = new FunctionReferenceArgumentValueContext(_ctx, getState());
		enterRule(_localctx, 384, RULE_functionReferenceArgumentValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1854);
			functionReferenceExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionReferenceExpressionContext extends ParserRuleContext {
		public FunctionReferenceMemberContext functionReferenceMember() {
			return getRuleContext(FunctionReferenceMemberContext.class,0);
		}
		public FunctionReferenceExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionReferenceExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFunctionReferenceExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFunctionReferenceExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFunctionReferenceExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionReferenceExpressionContext functionReferenceExpression() throws RecognitionException {
		FunctionReferenceExpressionContext _localctx = new FunctionReferenceExpressionContext(_ctx, getState());
		enterRule(_localctx, 386, RULE_functionReferenceExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1856);
			functionReferenceMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionReferenceMemberContext extends ParserRuleContext {
		public FunctionReferenceContext functionReference() {
			return getRuleContext(FunctionReferenceContext.class,0);
		}
		public FunctionReferenceMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionReferenceMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFunctionReferenceMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFunctionReferenceMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFunctionReferenceMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionReferenceMemberContext functionReferenceMember() throws RecognitionException {
		FunctionReferenceMemberContext _localctx = new FunctionReferenceMemberContext(_ctx, getState());
		enterRule(_localctx, 388, RULE_functionReferenceMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1858);
			functionReference();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionReferenceContext extends ParserRuleContext {
		public ReferenceTypingContext referenceTyping() {
			return getRuleContext(ReferenceTypingContext.class,0);
		}
		public FunctionReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionReference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFunctionReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFunctionReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFunctionReference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionReferenceContext functionReference() throws RecognitionException {
		FunctionReferenceContext _localctx = new FunctionReferenceContext(_ctx, getState());
		enterRule(_localctx, 390, RULE_functionReference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1860);
			referenceTyping();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureChainMemberContext extends ParserRuleContext {
		public FeatureReferenceMemberContext featureReferenceMember() {
			return getRuleContext(FeatureReferenceMemberContext.class,0);
		}
		public OwnedFeatureChainMemberContext ownedFeatureChainMember() {
			return getRuleContext(OwnedFeatureChainMemberContext.class,0);
		}
		public FeatureChainMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureChainMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureChainMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureChainMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureChainMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureChainMemberContext featureChainMember() throws RecognitionException {
		FeatureChainMemberContext _localctx = new FeatureChainMemberContext(_ctx, getState());
		enterRule(_localctx, 392, RULE_featureChainMember);
		try {
			setState(1864);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,173,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1862);
				featureReferenceMember();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1863);
				ownedFeatureChainMember();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InvocationTypeMemberContext extends ParserRuleContext {
		public InvocationTypeContext invocationType() {
			return getRuleContext(InvocationTypeContext.class,0);
		}
		public InvocationTypeMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_invocationTypeMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterInvocationTypeMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitInvocationTypeMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitInvocationTypeMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InvocationTypeMemberContext invocationTypeMember() throws RecognitionException {
		InvocationTypeMemberContext _localctx = new InvocationTypeMemberContext(_ctx, getState());
		enterRule(_localctx, 394, RULE_invocationTypeMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1866);
			invocationType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InvocationTypeContext extends ParserRuleContext {
		public OwnedFeatureTypingContext ownedFeatureTyping() {
			return getRuleContext(OwnedFeatureTypingContext.class,0);
		}
		public InvocationTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_invocationType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterInvocationType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitInvocationType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitInvocationType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InvocationTypeContext invocationType() throws RecognitionException {
		InvocationTypeContext _localctx = new InvocationTypeContext(_ctx, getState());
		enterRule(_localctx, 396, RULE_invocationType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1868);
			ownedFeatureTyping();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BaseExpressionContext extends ParserRuleContext {
		public NullExpressionContext nullExpression() {
			return getRuleContext(NullExpressionContext.class,0);
		}
		public LiteralExpressionContext literalExpression() {
			return getRuleContext(LiteralExpressionContext.class,0);
		}
		public FeatureReferenceExpressionContext featureReferenceExpression() {
			return getRuleContext(FeatureReferenceExpressionContext.class,0);
		}
		public MetadataAccessExpressionContext metadataAccessExpression() {
			return getRuleContext(MetadataAccessExpressionContext.class,0);
		}
		public InvocationExpressionContext invocationExpression() {
			return getRuleContext(InvocationExpressionContext.class,0);
		}
		public ConstructorExpressionContext constructorExpression() {
			return getRuleContext(ConstructorExpressionContext.class,0);
		}
		public BodyExpressionContext bodyExpression() {
			return getRuleContext(BodyExpressionContext.class,0);
		}
		public BaseExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBaseExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBaseExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBaseExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseExpressionContext baseExpression() throws RecognitionException {
		BaseExpressionContext _localctx = new BaseExpressionContext(_ctx, getState());
		enterRule(_localctx, 398, RULE_baseExpression);
		try {
			setState(1877);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,174,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1870);
				nullExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1871);
				literalExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1872);
				featureReferenceExpression();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1873);
				metadataAccessExpression();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1874);
				invocationExpression();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1875);
				constructorExpression();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1876);
				bodyExpression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NullExpressionContext extends ParserRuleContext {
		public TerminalNode NULL() { return getToken(KerMLParser.NULL, 0); }
		public TerminalNode LPAREN() { return getToken(KerMLParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(KerMLParser.RPAREN, 0); }
		public NullExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nullExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNullExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNullExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNullExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NullExpressionContext nullExpression() throws RecognitionException {
		NullExpressionContext _localctx = new NullExpressionContext(_ctx, getState());
		enterRule(_localctx, 400, RULE_nullExpression);
		try {
			setState(1882);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL:
				enterOuterAlt(_localctx, 1);
				{
				setState(1879);
				match(NULL);
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(1880);
				match(LPAREN);
				setState(1881);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureReferenceExpressionContext extends ParserRuleContext {
		public FeatureReferenceMemberContext featureReferenceMember() {
			return getRuleContext(FeatureReferenceMemberContext.class,0);
		}
		public EmptyResultMemberContext emptyResultMember() {
			return getRuleContext(EmptyResultMemberContext.class,0);
		}
		public FeatureReferenceExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureReferenceExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureReferenceExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureReferenceExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureReferenceExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureReferenceExpressionContext featureReferenceExpression() throws RecognitionException {
		FeatureReferenceExpressionContext _localctx = new FeatureReferenceExpressionContext(_ctx, getState());
		enterRule(_localctx, 402, RULE_featureReferenceExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1884);
			featureReferenceMember();
			setState(1885);
			emptyResultMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureReferenceMemberContext extends ParserRuleContext {
		public FeatureReferenceContext featureReference() {
			return getRuleContext(FeatureReferenceContext.class,0);
		}
		public FeatureReferenceMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureReferenceMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureReferenceMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureReferenceMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureReferenceMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureReferenceMemberContext featureReferenceMember() throws RecognitionException {
		FeatureReferenceMemberContext _localctx = new FeatureReferenceMemberContext(_ctx, getState());
		enterRule(_localctx, 404, RULE_featureReferenceMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1887);
			featureReference();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureReferenceContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public FeatureReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureReference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureReference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureReferenceContext featureReference() throws RecognitionException {
		FeatureReferenceContext _localctx = new FeatureReferenceContext(_ctx, getState());
		enterRule(_localctx, 406, RULE_featureReference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1889);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetadataAccessExpressionContext extends ParserRuleContext {
		public ElementReferenceMemberContext elementReferenceMember() {
			return getRuleContext(ElementReferenceMemberContext.class,0);
		}
		public TerminalNode DOT() { return getToken(KerMLParser.DOT, 0); }
		public TerminalNode METADATA() { return getToken(KerMLParser.METADATA, 0); }
		public MetadataAccessExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metadataAccessExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetadataAccessExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetadataAccessExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetadataAccessExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetadataAccessExpressionContext metadataAccessExpression() throws RecognitionException {
		MetadataAccessExpressionContext _localctx = new MetadataAccessExpressionContext(_ctx, getState());
		enterRule(_localctx, 408, RULE_metadataAccessExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1891);
			elementReferenceMember();
			setState(1892);
			match(DOT);
			setState(1893);
			match(METADATA);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElementReferenceMemberContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public ElementReferenceMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementReferenceMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterElementReferenceMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitElementReferenceMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitElementReferenceMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementReferenceMemberContext elementReferenceMember() throws RecognitionException {
		ElementReferenceMemberContext _localctx = new ElementReferenceMemberContext(_ctx, getState());
		enterRule(_localctx, 410, RULE_elementReferenceMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1895);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InvocationExpressionContext extends ParserRuleContext {
		public InstantiatedTypeMemberContext instantiatedTypeMember() {
			return getRuleContext(InstantiatedTypeMemberContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public EmptyResultMemberContext emptyResultMember() {
			return getRuleContext(EmptyResultMemberContext.class,0);
		}
		public InvocationExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_invocationExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterInvocationExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitInvocationExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitInvocationExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InvocationExpressionContext invocationExpression() throws RecognitionException {
		InvocationExpressionContext _localctx = new InvocationExpressionContext(_ctx, getState());
		enterRule(_localctx, 412, RULE_invocationExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1897);
			instantiatedTypeMember();
			setState(1898);
			argumentList();
			setState(1899);
			emptyResultMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstructorExpressionContext extends ParserRuleContext {
		public TerminalNode NEW() { return getToken(KerMLParser.NEW, 0); }
		public InstantiatedTypeMemberContext instantiatedTypeMember() {
			return getRuleContext(InstantiatedTypeMemberContext.class,0);
		}
		public ConstructorResultMemberContext constructorResultMember() {
			return getRuleContext(ConstructorResultMemberContext.class,0);
		}
		public ConstructorExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterConstructorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitConstructorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitConstructorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructorExpressionContext constructorExpression() throws RecognitionException {
		ConstructorExpressionContext _localctx = new ConstructorExpressionContext(_ctx, getState());
		enterRule(_localctx, 414, RULE_constructorExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1901);
			match(NEW);
			setState(1902);
			instantiatedTypeMember();
			setState(1903);
			constructorResultMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstructorResultMemberContext extends ParserRuleContext {
		public ConstructorResultContext constructorResult() {
			return getRuleContext(ConstructorResultContext.class,0);
		}
		public ConstructorResultMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorResultMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterConstructorResultMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitConstructorResultMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitConstructorResultMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructorResultMemberContext constructorResultMember() throws RecognitionException {
		ConstructorResultMemberContext _localctx = new ConstructorResultMemberContext(_ctx, getState());
		enterRule(_localctx, 416, RULE_constructorResultMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1905);
			constructorResult();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstructorResultContext extends ParserRuleContext {
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ConstructorResultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorResult; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterConstructorResult(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitConstructorResult(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitConstructorResult(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructorResultContext constructorResult() throws RecognitionException {
		ConstructorResultContext _localctx = new ConstructorResultContext(_ctx, getState());
		enterRule(_localctx, 418, RULE_constructorResult);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1907);
			argumentList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InstantiatedTypeMemberContext extends ParserRuleContext {
		public InstantiatedTypeReferenceContext instantiatedTypeReference() {
			return getRuleContext(InstantiatedTypeReferenceContext.class,0);
		}
		public OwnedFeatureChainMemberContext ownedFeatureChainMember() {
			return getRuleContext(OwnedFeatureChainMemberContext.class,0);
		}
		public InstantiatedTypeMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instantiatedTypeMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterInstantiatedTypeMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitInstantiatedTypeMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitInstantiatedTypeMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstantiatedTypeMemberContext instantiatedTypeMember() throws RecognitionException {
		InstantiatedTypeMemberContext _localctx = new InstantiatedTypeMemberContext(_ctx, getState());
		enterRule(_localctx, 420, RULE_instantiatedTypeMember);
		try {
			setState(1911);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,176,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1909);
				instantiatedTypeReference();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1910);
				ownedFeatureChainMember();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InstantiatedTypeReferenceContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public InstantiatedTypeReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instantiatedTypeReference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterInstantiatedTypeReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitInstantiatedTypeReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitInstantiatedTypeReference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstantiatedTypeReferenceContext instantiatedTypeReference() throws RecognitionException {
		InstantiatedTypeReferenceContext _localctx = new InstantiatedTypeReferenceContext(_ctx, getState());
		enterRule(_localctx, 422, RULE_instantiatedTypeReference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1913);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedFeatureChainMemberContext extends ParserRuleContext {
		public OwnedFeatureChainContext ownedFeatureChain() {
			return getRuleContext(OwnedFeatureChainContext.class,0);
		}
		public OwnedFeatureChainMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedFeatureChainMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedFeatureChainMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedFeatureChainMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedFeatureChainMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedFeatureChainMemberContext ownedFeatureChainMember() throws RecognitionException {
		OwnedFeatureChainMemberContext _localctx = new OwnedFeatureChainMemberContext(_ctx, getState());
		enterRule(_localctx, 424, RULE_ownedFeatureChainMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1915);
			ownedFeatureChain();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentListContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(KerMLParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(KerMLParser.RPAREN, 0); }
		public PositionalArgumentListContext positionalArgumentList() {
			return getRuleContext(PositionalArgumentListContext.class,0);
		}
		public NamedArgumentListContext namedArgumentList() {
			return getRuleContext(NamedArgumentListContext.class,0);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 426, RULE_argumentList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1917);
			match(LPAREN);
			setState(1920);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,177,_ctx) ) {
			case 1:
				{
				setState(1918);
				positionalArgumentList();
				}
				break;
			case 2:
				{
				setState(1919);
				namedArgumentList();
				}
				break;
			}
			setState(1922);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PositionalArgumentListContext extends ParserRuleContext {
		public List<ArgumentMemberContext> argumentMember() {
			return getRuleContexts(ArgumentMemberContext.class);
		}
		public ArgumentMemberContext argumentMember(int i) {
			return getRuleContext(ArgumentMemberContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public PositionalArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_positionalArgumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPositionalArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPositionalArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPositionalArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PositionalArgumentListContext positionalArgumentList() throws RecognitionException {
		PositionalArgumentListContext _localctx = new PositionalArgumentListContext(_ctx, getState());
		enterRule(_localctx, 428, RULE_positionalArgumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1924);
			argumentMember();
			setState(1929);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1925);
				match(COMMA);
				setState(1926);
				argumentMember();
				}
				}
				setState(1931);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamedArgumentListContext extends ParserRuleContext {
		public List<NamedArgumentMemberContext> namedArgumentMember() {
			return getRuleContexts(NamedArgumentMemberContext.class);
		}
		public NamedArgumentMemberContext namedArgumentMember(int i) {
			return getRuleContext(NamedArgumentMemberContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public NamedArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedArgumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNamedArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNamedArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNamedArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedArgumentListContext namedArgumentList() throws RecognitionException {
		NamedArgumentListContext _localctx = new NamedArgumentListContext(_ctx, getState());
		enterRule(_localctx, 430, RULE_namedArgumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1932);
			namedArgumentMember();
			setState(1937);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1933);
				match(COMMA);
				setState(1934);
				namedArgumentMember();
				}
				}
				setState(1939);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamedArgumentMemberContext extends ParserRuleContext {
		public NamedArgumentContext namedArgument() {
			return getRuleContext(NamedArgumentContext.class,0);
		}
		public NamedArgumentMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedArgumentMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNamedArgumentMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNamedArgumentMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNamedArgumentMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedArgumentMemberContext namedArgumentMember() throws RecognitionException {
		NamedArgumentMemberContext _localctx = new NamedArgumentMemberContext(_ctx, getState());
		enterRule(_localctx, 432, RULE_namedArgumentMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1940);
			namedArgument();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamedArgumentContext extends ParserRuleContext {
		public ParameterRedefinitionContext parameterRedefinition() {
			return getRuleContext(ParameterRedefinitionContext.class,0);
		}
		public TerminalNode EQUALS() { return getToken(KerMLParser.EQUALS, 0); }
		public ArgumentValueContext argumentValue() {
			return getRuleContext(ArgumentValueContext.class,0);
		}
		public NamedArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterNamedArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitNamedArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitNamedArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedArgumentContext namedArgument() throws RecognitionException {
		NamedArgumentContext _localctx = new NamedArgumentContext(_ctx, getState());
		enterRule(_localctx, 434, RULE_namedArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1942);
			parameterRedefinition();
			setState(1943);
			match(EQUALS);
			setState(1944);
			argumentValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterRedefinitionContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public ParameterRedefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterRedefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterParameterRedefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitParameterRedefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitParameterRedefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterRedefinitionContext parameterRedefinition() throws RecognitionException {
		ParameterRedefinitionContext _localctx = new ParameterRedefinitionContext(_ctx, getState());
		enterRule(_localctx, 436, RULE_parameterRedefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1946);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BodyExpressionContext extends ParserRuleContext {
		public ExpressionBodyMemberContext expressionBodyMember() {
			return getRuleContext(ExpressionBodyMemberContext.class,0);
		}
		public BodyExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bodyExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBodyExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBodyExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBodyExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyExpressionContext bodyExpression() throws RecognitionException {
		BodyExpressionContext _localctx = new BodyExpressionContext(_ctx, getState());
		enterRule(_localctx, 438, RULE_bodyExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1948);
			expressionBodyMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionBodyMemberContext extends ParserRuleContext {
		public ExpressionBodyContext expressionBody() {
			return getRuleContext(ExpressionBodyContext.class,0);
		}
		public ExpressionBodyMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionBodyMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterExpressionBodyMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitExpressionBodyMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitExpressionBodyMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionBodyMemberContext expressionBodyMember() throws RecognitionException {
		ExpressionBodyMemberContext _localctx = new ExpressionBodyMemberContext(_ctx, getState());
		enterRule(_localctx, 440, RULE_expressionBodyMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1950);
			expressionBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(KerMLParser.LBRACE, 0); }
		public FunctionBodyPartContext functionBodyPart() {
			return getRuleContext(FunctionBodyPartContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(KerMLParser.RBRACE, 0); }
		public ExpressionBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterExpressionBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitExpressionBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitExpressionBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionBodyContext expressionBody() throws RecognitionException {
		ExpressionBodyContext _localctx = new ExpressionBodyContext(_ctx, getState());
		enterRule(_localctx, 442, RULE_expressionBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1952);
			match(LBRACE);
			setState(1953);
			functionBodyPart();
			setState(1954);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralExpressionContext extends ParserRuleContext {
		public LiteralBooleanContext literalBoolean() {
			return getRuleContext(LiteralBooleanContext.class,0);
		}
		public LiteralStringContext literalString() {
			return getRuleContext(LiteralStringContext.class,0);
		}
		public LiteralIntegerContext literalInteger() {
			return getRuleContext(LiteralIntegerContext.class,0);
		}
		public LiteralRealContext literalReal() {
			return getRuleContext(LiteralRealContext.class,0);
		}
		public LiteralInfinityContext literalInfinity() {
			return getRuleContext(LiteralInfinityContext.class,0);
		}
		public LiteralExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterLiteralExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitLiteralExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitLiteralExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralExpressionContext literalExpression() throws RecognitionException {
		LiteralExpressionContext _localctx = new LiteralExpressionContext(_ctx, getState());
		enterRule(_localctx, 444, RULE_literalExpression);
		try {
			setState(1961);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,180,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1956);
				literalBoolean();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1957);
				literalString();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1958);
				literalInteger();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1959);
				literalReal();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1960);
				literalInfinity();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralBooleanContext extends ParserRuleContext {
		public BooleanValueContext value;
		public BooleanValueContext booleanValue() {
			return getRuleContext(BooleanValueContext.class,0);
		}
		public LiteralBooleanContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalBoolean; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterLiteralBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitLiteralBoolean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitLiteralBoolean(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralBooleanContext literalBoolean() throws RecognitionException {
		LiteralBooleanContext _localctx = new LiteralBooleanContext(_ctx, getState());
		enterRule(_localctx, 446, RULE_literalBoolean);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1963);
			((LiteralBooleanContext)_localctx).value = booleanValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BooleanValueContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(KerMLParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(KerMLParser.FALSE, 0); }
		public BooleanValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterBooleanValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitBooleanValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitBooleanValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanValueContext booleanValue() throws RecognitionException {
		BooleanValueContext _localctx = new BooleanValueContext(_ctx, getState());
		enterRule(_localctx, 448, RULE_booleanValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1965);
			_la = _input.LA(1);
			if ( !(_la==FALSE || _la==TRUE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralStringContext extends ParserRuleContext {
		public Token value;
		public TerminalNode STRING_VALUE() { return getToken(KerMLParser.STRING_VALUE, 0); }
		public LiteralStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterLiteralString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitLiteralString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitLiteralString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralStringContext literalString() throws RecognitionException {
		LiteralStringContext _localctx = new LiteralStringContext(_ctx, getState());
		enterRule(_localctx, 450, RULE_literalString);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1967);
			((LiteralStringContext)_localctx).value = match(STRING_VALUE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralIntegerContext extends ParserRuleContext {
		public Token value;
		public TerminalNode DECIMAL_VALUE() { return getToken(KerMLParser.DECIMAL_VALUE, 0); }
		public LiteralIntegerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalInteger; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterLiteralInteger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitLiteralInteger(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitLiteralInteger(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralIntegerContext literalInteger() throws RecognitionException {
		LiteralIntegerContext _localctx = new LiteralIntegerContext(_ctx, getState());
		enterRule(_localctx, 452, RULE_literalInteger);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1969);
			((LiteralIntegerContext)_localctx).value = match(DECIMAL_VALUE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralRealContext extends ParserRuleContext {
		public RealValueContext value;
		public RealValueContext realValue() {
			return getRuleContext(RealValueContext.class,0);
		}
		public LiteralRealContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalReal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterLiteralReal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitLiteralReal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitLiteralReal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralRealContext literalReal() throws RecognitionException {
		LiteralRealContext _localctx = new LiteralRealContext(_ctx, getState());
		enterRule(_localctx, 454, RULE_literalReal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1971);
			((LiteralRealContext)_localctx).value = realValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RealValueContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(KerMLParser.DOT, 0); }
		public List<TerminalNode> DECIMAL_VALUE() { return getTokens(KerMLParser.DECIMAL_VALUE); }
		public TerminalNode DECIMAL_VALUE(int i) {
			return getToken(KerMLParser.DECIMAL_VALUE, i);
		}
		public TerminalNode EXPONENTIAL_VALUE() { return getToken(KerMLParser.EXPONENTIAL_VALUE, 0); }
		public RealValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_realValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterRealValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitRealValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitRealValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RealValueContext realValue() throws RecognitionException {
		RealValueContext _localctx = new RealValueContext(_ctx, getState());
		enterRule(_localctx, 456, RULE_realValue);
		int _la;
		try {
			setState(1979);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOT:
			case DECIMAL_VALUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(1974);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DECIMAL_VALUE) {
					{
					setState(1973);
					match(DECIMAL_VALUE);
					}
				}

				setState(1976);
				match(DOT);
				setState(1977);
				_la = _input.LA(1);
				if ( !(_la==DECIMAL_VALUE || _la==EXPONENTIAL_VALUE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case EXPONENTIAL_VALUE:
				enterOuterAlt(_localctx, 2);
				{
				setState(1978);
				match(EXPONENTIAL_VALUE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralInfinityContext extends ParserRuleContext {
		public TerminalNode STAR() { return getToken(KerMLParser.STAR, 0); }
		public LiteralInfinityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalInfinity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterLiteralInfinity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitLiteralInfinity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitLiteralInfinity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralInfinityContext literalInfinity() throws RecognitionException {
		LiteralInfinityContext _localctx = new LiteralInfinityContext(_ctx, getState());
		enterRule(_localctx, 458, RULE_literalInfinity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1981);
			match(STAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InteractionContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode INTERACTION() { return getToken(KerMLParser.INTERACTION, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public InteractionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interaction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterInteraction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitInteraction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitInteraction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InteractionContext interaction() throws RecognitionException {
		InteractionContext _localctx = new InteractionContext(_ctx, getState());
		enterRule(_localctx, 460, RULE_interaction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1983);
			typePrefix();
			setState(1984);
			match(INTERACTION);
			setState(1985);
			classifierDeclaration();
			setState(1986);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FlowContext extends ParserRuleContext {
		public FeaturePrefixContext featurePrefix() {
			return getRuleContext(FeaturePrefixContext.class,0);
		}
		public TerminalNode FLOW() { return getToken(KerMLParser.FLOW, 0); }
		public ItemFlowDeclarationContext itemFlowDeclaration() {
			return getRuleContext(ItemFlowDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public FlowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flow; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFlow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFlow(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFlow(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlowContext flow() throws RecognitionException {
		FlowContext _localctx = new FlowContext(_ctx, getState());
		enterRule(_localctx, 462, RULE_flow);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1988);
			featurePrefix();
			setState(1989);
			match(FLOW);
			setState(1990);
			itemFlowDeclaration();
			setState(1991);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SuccessionFlowContext extends ParserRuleContext {
		public FeaturePrefixContext featurePrefix() {
			return getRuleContext(FeaturePrefixContext.class,0);
		}
		public TerminalNode SUCCESSION() { return getToken(KerMLParser.SUCCESSION, 0); }
		public TerminalNode FLOW() { return getToken(KerMLParser.FLOW, 0); }
		public ItemFlowDeclarationContext itemFlowDeclaration() {
			return getRuleContext(ItemFlowDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public SuccessionFlowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_successionFlow; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterSuccessionFlow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitSuccessionFlow(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitSuccessionFlow(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SuccessionFlowContext successionFlow() throws RecognitionException {
		SuccessionFlowContext _localctx = new SuccessionFlowContext(_ctx, getState());
		enterRule(_localctx, 464, RULE_successionFlow);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1993);
			featurePrefix();
			setState(1994);
			match(SUCCESSION);
			setState(1995);
			match(FLOW);
			setState(1996);
			itemFlowDeclaration();
			setState(1997);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ItemFlowDeclarationContext extends ParserRuleContext {
		public Token isSufficient;
		public FeatureDeclarationContext featureDeclaration() {
			return getRuleContext(FeatureDeclarationContext.class,0);
		}
		public ValuePartContext valuePart() {
			return getRuleContext(ValuePartContext.class,0);
		}
		public TerminalNode OF() { return getToken(KerMLParser.OF, 0); }
		public PayloadFeatureMemberContext payloadFeatureMember() {
			return getRuleContext(PayloadFeatureMemberContext.class,0);
		}
		public TerminalNode FROM() { return getToken(KerMLParser.FROM, 0); }
		public List<FlowEndMemberContext> flowEndMember() {
			return getRuleContexts(FlowEndMemberContext.class);
		}
		public FlowEndMemberContext flowEndMember(int i) {
			return getRuleContext(FlowEndMemberContext.class,i);
		}
		public TerminalNode TO() { return getToken(KerMLParser.TO, 0); }
		public TerminalNode ALL() { return getToken(KerMLParser.ALL, 0); }
		public ItemFlowDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_itemFlowDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterItemFlowDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitItemFlowDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitItemFlowDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ItemFlowDeclarationContext itemFlowDeclaration() throws RecognitionException {
		ItemFlowDeclarationContext _localctx = new ItemFlowDeclarationContext(_ctx, getState());
		enterRule(_localctx, 466, RULE_itemFlowDeclaration);
		int _la;
		try {
			setState(2021);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,187,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1999);
				featureDeclaration();
				setState(2001);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DEFAULT || _la==COLON_EQUALS || _la==EQUALS) {
					{
					setState(2000);
					valuePart();
					}
				}

				setState(2005);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OF) {
					{
					setState(2003);
					match(OF);
					setState(2004);
					payloadFeatureMember();
					}
				}

				setState(2012);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FROM) {
					{
					setState(2007);
					match(FROM);
					setState(2008);
					flowEndMember();
					setState(2009);
					match(TO);
					setState(2010);
					flowEndMember();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2015);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ALL) {
					{
					setState(2014);
					((ItemFlowDeclarationContext)_localctx).isSufficient = match(ALL);
					}
				}

				setState(2017);
				flowEndMember();
				setState(2018);
				match(TO);
				setState(2019);
				flowEndMember();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PayloadFeatureMemberContext extends ParserRuleContext {
		public PayloadFeatureContext payloadFeature() {
			return getRuleContext(PayloadFeatureContext.class,0);
		}
		public PayloadFeatureMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_payloadFeatureMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPayloadFeatureMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPayloadFeatureMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPayloadFeatureMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PayloadFeatureMemberContext payloadFeatureMember() throws RecognitionException {
		PayloadFeatureMemberContext _localctx = new PayloadFeatureMemberContext(_ctx, getState());
		enterRule(_localctx, 468, RULE_payloadFeatureMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2023);
			payloadFeature();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PayloadFeatureContext extends ParserRuleContext {
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public PayloadFeatureSpecializationPartContext payloadFeatureSpecializationPart() {
			return getRuleContext(PayloadFeatureSpecializationPartContext.class,0);
		}
		public ValuePartContext valuePart() {
			return getRuleContext(ValuePartContext.class,0);
		}
		public OwnedFeatureTypingContext ownedFeatureTyping() {
			return getRuleContext(OwnedFeatureTypingContext.class,0);
		}
		public OwnedMultiplicityContext ownedMultiplicity() {
			return getRuleContext(OwnedMultiplicityContext.class,0);
		}
		public PayloadFeatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_payloadFeature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPayloadFeature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPayloadFeature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPayloadFeature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PayloadFeatureContext payloadFeature() throws RecognitionException {
		PayloadFeatureContext _localctx = new PayloadFeatureContext(_ctx, getState());
		enterRule(_localctx, 470, RULE_payloadFeature);
		int _la;
		try {
			setState(2043);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,192,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2025);
				identification();
				setState(2026);
				payloadFeatureSpecializationPart();
				setState(2028);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DEFAULT || _la==COLON_EQUALS || _la==EQUALS) {
					{
					setState(2027);
					valuePart();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2030);
				identification();
				setState(2031);
				valuePart();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2041);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case DOLLAR:
				case NAME:
					{
					setState(2033);
					ownedFeatureTyping();
					setState(2035);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LBRACKET) {
						{
						setState(2034);
						ownedMultiplicity();
						}
					}

					}
					break;
				case LBRACKET:
					{
					setState(2037);
					ownedMultiplicity();
					setState(2039);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==DOLLAR || _la==NAME) {
						{
						setState(2038);
						ownedFeatureTyping();
						}
					}

					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PayloadFeatureSpecializationPartContext extends ParserRuleContext {
		public List<FeatureSpecializationContext> featureSpecialization() {
			return getRuleContexts(FeatureSpecializationContext.class);
		}
		public FeatureSpecializationContext featureSpecialization(int i) {
			return getRuleContext(FeatureSpecializationContext.class,i);
		}
		public MultiplicityPartContext multiplicityPart() {
			return getRuleContext(MultiplicityPartContext.class,0);
		}
		public PayloadFeatureSpecializationPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_payloadFeatureSpecializationPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPayloadFeatureSpecializationPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPayloadFeatureSpecializationPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPayloadFeatureSpecializationPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PayloadFeatureSpecializationPartContext payloadFeatureSpecializationPart() throws RecognitionException {
		PayloadFeatureSpecializationPartContext _localctx = new PayloadFeatureSpecializationPartContext(_ctx, getState());
		enterRule(_localctx, 472, RULE_payloadFeatureSpecializationPart);
		int _la;
		try {
			int _alt;
			setState(2065);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CROSSES:
			case REDEFINES:
			case REFERENCES:
			case SUBSETS:
			case TYPED:
			case DOUBLE_COLON_GT:
			case COLON_GT_GT:
			case COLON_GT:
			case EQUALS_GT:
			case COLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(2046); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(2045);
						featureSpecialization();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(2048); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,193,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(2051);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 4611686018427387937L) != 0)) {
					{
					setState(2050);
					multiplicityPart();
					}
				}

				setState(2056);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 22)) & ~0x3f) == 0 && ((1L << (_la - 22)) & 1441151880758558721L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 4503599929409665L) != 0)) {
					{
					{
					setState(2053);
					featureSpecialization();
					}
					}
					setState(2058);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case NONUNIQUE:
			case ORDERED:
			case LBRACKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(2059);
				multiplicityPart();
				setState(2061); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(2060);
					featureSpecialization();
					}
					}
					setState(2063); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( ((((_la - 22)) & ~0x3f) == 0 && ((1L << (_la - 22)) & 1441151880758558721L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 4503599929409665L) != 0) );
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FlowEndMemberContext extends ParserRuleContext {
		public FlowEndContext flowEnd() {
			return getRuleContext(FlowEndContext.class,0);
		}
		public FlowEndMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flowEndMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFlowEndMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFlowEndMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFlowEndMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlowEndMemberContext flowEndMember() throws RecognitionException {
		FlowEndMemberContext _localctx = new FlowEndMemberContext(_ctx, getState());
		enterRule(_localctx, 474, RULE_flowEndMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2067);
			flowEnd();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FlowEndContext extends ParserRuleContext {
		public FlowFeatureMemberContext flowFeatureMember() {
			return getRuleContext(FlowFeatureMemberContext.class,0);
		}
		public OwnedReferenceSubsettingContext ownedReferenceSubsetting() {
			return getRuleContext(OwnedReferenceSubsettingContext.class,0);
		}
		public TerminalNode DOT() { return getToken(KerMLParser.DOT, 0); }
		public FlowEndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flowEnd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFlowEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFlowEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFlowEnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlowEndContext flowEnd() throws RecognitionException {
		FlowEndContext _localctx = new FlowEndContext(_ctx, getState());
		enterRule(_localctx, 476, RULE_flowEnd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2072);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,198,_ctx) ) {
			case 1:
				{
				setState(2069);
				ownedReferenceSubsetting();
				setState(2070);
				match(DOT);
				}
				break;
			}
			setState(2074);
			flowFeatureMember();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FlowFeatureMemberContext extends ParserRuleContext {
		public FlowFeatureContext flowFeature() {
			return getRuleContext(FlowFeatureContext.class,0);
		}
		public FlowFeatureMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flowFeatureMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFlowFeatureMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFlowFeatureMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFlowFeatureMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlowFeatureMemberContext flowFeatureMember() throws RecognitionException {
		FlowFeatureMemberContext _localctx = new FlowFeatureMemberContext(_ctx, getState());
		enterRule(_localctx, 478, RULE_flowFeatureMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2076);
			flowFeature();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FlowFeatureContext extends ParserRuleContext {
		public FlowFeatureRedefinitionContext flowFeatureRedefinition() {
			return getRuleContext(FlowFeatureRedefinitionContext.class,0);
		}
		public FlowFeatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flowFeature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFlowFeature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFlowFeature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFlowFeature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlowFeatureContext flowFeature() throws RecognitionException {
		FlowFeatureContext _localctx = new FlowFeatureContext(_ctx, getState());
		enterRule(_localctx, 480, RULE_flowFeature);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2078);
			flowFeatureRedefinition();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FlowFeatureRedefinitionContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public FlowFeatureRedefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flowFeatureRedefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFlowFeatureRedefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFlowFeatureRedefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFlowFeatureRedefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FlowFeatureRedefinitionContext flowFeatureRedefinition() throws RecognitionException {
		FlowFeatureRedefinitionContext _localctx = new FlowFeatureRedefinitionContext(_ctx, getState());
		enterRule(_localctx, 482, RULE_flowFeatureRedefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2080);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValuePartContext extends ParserRuleContext {
		public FeatureValueContext featureValue() {
			return getRuleContext(FeatureValueContext.class,0);
		}
		public ValuePartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valuePart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterValuePart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitValuePart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitValuePart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValuePartContext valuePart() throws RecognitionException {
		ValuePartContext _localctx = new ValuePartContext(_ctx, getState());
		enterRule(_localctx, 484, RULE_valuePart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2082);
			featureValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureValueContext extends ParserRuleContext {
		public Token isInitial;
		public Token isDefault;
		public OwnedExpressionContext ownedExpression() {
			return getRuleContext(OwnedExpressionContext.class,0);
		}
		public TerminalNode EQUALS() { return getToken(KerMLParser.EQUALS, 0); }
		public TerminalNode COLON_EQUALS() { return getToken(KerMLParser.COLON_EQUALS, 0); }
		public TerminalNode DEFAULT() { return getToken(KerMLParser.DEFAULT, 0); }
		public FeatureValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_featureValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterFeatureValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitFeatureValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitFeatureValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureValueContext featureValue() throws RecognitionException {
		FeatureValueContext _localctx = new FeatureValueContext(_ctx, getState());
		enterRule(_localctx, 486, RULE_featureValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2091);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EQUALS:
				{
				setState(2084);
				match(EQUALS);
				}
				break;
			case COLON_EQUALS:
				{
				setState(2085);
				((FeatureValueContext)_localctx).isInitial = match(COLON_EQUALS);
				}
				break;
			case DEFAULT:
				{
				setState(2086);
				((FeatureValueContext)_localctx).isDefault = match(DEFAULT);
				setState(2089);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case EQUALS:
					{
					setState(2087);
					match(EQUALS);
					}
					break;
				case COLON_EQUALS:
					{
					setState(2088);
					((FeatureValueContext)_localctx).isInitial = match(COLON_EQUALS);
					}
					break;
				case ALL:
				case AS:
				case FALSE:
				case HASTYPE:
				case IF:
				case ISTYPE:
				case NEW:
				case NOT:
				case NULL:
				case TRUE:
				case LPAREN:
				case LBRACE:
				case TILDE:
				case AT:
				case STAR:
				case PLUS:
				case MINUS:
				case DOLLAR:
				case DOT:
				case DECIMAL_VALUE:
				case EXPONENTIAL_VALUE:
				case STRING_VALUE:
				case NAME:
					break;
				default:
					break;
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(2093);
			ownedExpression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicityContext extends ParserRuleContext {
		public MultiplicitySubsetContext multiplicitySubset() {
			return getRuleContext(MultiplicitySubsetContext.class,0);
		}
		public MultiplicityRangeContext multiplicityRange() {
			return getRuleContext(MultiplicityRangeContext.class,0);
		}
		public MultiplicityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMultiplicity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMultiplicity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMultiplicity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicityContext multiplicity() throws RecognitionException {
		MultiplicityContext _localctx = new MultiplicityContext(_ctx, getState());
		enterRule(_localctx, 488, RULE_multiplicity);
		try {
			setState(2097);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,201,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2095);
				multiplicitySubset();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2096);
				multiplicityRange();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicitySubsetContext extends ParserRuleContext {
		public TerminalNode MULTIPLICITY() { return getToken(KerMLParser.MULTIPLICITY, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public SubsetsContext subsets() {
			return getRuleContext(SubsetsContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public MultiplicitySubsetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicitySubset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMultiplicitySubset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMultiplicitySubset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMultiplicitySubset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicitySubsetContext multiplicitySubset() throws RecognitionException {
		MultiplicitySubsetContext _localctx = new MultiplicitySubsetContext(_ctx, getState());
		enterRule(_localctx, 490, RULE_multiplicitySubset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2099);
			match(MULTIPLICITY);
			setState(2100);
			identification();
			setState(2101);
			subsets();
			setState(2102);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicityRangeContext extends ParserRuleContext {
		public TerminalNode MULTIPLICITY() { return getToken(KerMLParser.MULTIPLICITY, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public MultiplicityBoundsContext multiplicityBounds() {
			return getRuleContext(MultiplicityBoundsContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public MultiplicityRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicityRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMultiplicityRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMultiplicityRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMultiplicityRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicityRangeContext multiplicityRange() throws RecognitionException {
		MultiplicityRangeContext _localctx = new MultiplicityRangeContext(_ctx, getState());
		enterRule(_localctx, 492, RULE_multiplicityRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2104);
			match(MULTIPLICITY);
			setState(2105);
			identification();
			setState(2106);
			multiplicityBounds();
			setState(2107);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedMultiplicityContext extends ParserRuleContext {
		public OwnedMultiplicityRangeContext ownedMultiplicityRange() {
			return getRuleContext(OwnedMultiplicityRangeContext.class,0);
		}
		public OwnedMultiplicityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedMultiplicity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedMultiplicity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedMultiplicity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedMultiplicity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedMultiplicityContext ownedMultiplicity() throws RecognitionException {
		OwnedMultiplicityContext _localctx = new OwnedMultiplicityContext(_ctx, getState());
		enterRule(_localctx, 494, RULE_ownedMultiplicity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2109);
			ownedMultiplicityRange();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OwnedMultiplicityRangeContext extends ParserRuleContext {
		public MultiplicityBoundsContext multiplicityBounds() {
			return getRuleContext(MultiplicityBoundsContext.class,0);
		}
		public OwnedMultiplicityRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ownedMultiplicityRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterOwnedMultiplicityRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitOwnedMultiplicityRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitOwnedMultiplicityRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OwnedMultiplicityRangeContext ownedMultiplicityRange() throws RecognitionException {
		OwnedMultiplicityRangeContext _localctx = new OwnedMultiplicityRangeContext(_ctx, getState());
		enterRule(_localctx, 496, RULE_ownedMultiplicityRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2111);
			multiplicityBounds();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicityBoundsContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(KerMLParser.LBRACKET, 0); }
		public List<MultiplicityExpressionMemberContext> multiplicityExpressionMember() {
			return getRuleContexts(MultiplicityExpressionMemberContext.class);
		}
		public MultiplicityExpressionMemberContext multiplicityExpressionMember(int i) {
			return getRuleContext(MultiplicityExpressionMemberContext.class,i);
		}
		public TerminalNode RBRACKET() { return getToken(KerMLParser.RBRACKET, 0); }
		public TerminalNode DOUBLE_DOT() { return getToken(KerMLParser.DOUBLE_DOT, 0); }
		public MultiplicityBoundsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicityBounds; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMultiplicityBounds(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMultiplicityBounds(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMultiplicityBounds(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicityBoundsContext multiplicityBounds() throws RecognitionException {
		MultiplicityBoundsContext _localctx = new MultiplicityBoundsContext(_ctx, getState());
		enterRule(_localctx, 498, RULE_multiplicityBounds);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2113);
			match(LBRACKET);
			setState(2117);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,202,_ctx) ) {
			case 1:
				{
				setState(2114);
				multiplicityExpressionMember();
				setState(2115);
				match(DOUBLE_DOT);
				}
				break;
			}
			setState(2119);
			multiplicityExpressionMember();
			setState(2120);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiplicityExpressionMemberContext extends ParserRuleContext {
		public LiteralExpressionContext literalExpression() {
			return getRuleContext(LiteralExpressionContext.class,0);
		}
		public FeatureReferenceExpressionContext featureReferenceExpression() {
			return getRuleContext(FeatureReferenceExpressionContext.class,0);
		}
		public MultiplicityExpressionMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicityExpressionMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMultiplicityExpressionMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMultiplicityExpressionMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMultiplicityExpressionMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicityExpressionMemberContext multiplicityExpressionMember() throws RecognitionException {
		MultiplicityExpressionMemberContext _localctx = new MultiplicityExpressionMemberContext(_ctx, getState());
		enterRule(_localctx, 500, RULE_multiplicityExpressionMember);
		try {
			setState(2124);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FALSE:
			case TRUE:
			case STAR:
			case DOT:
			case DECIMAL_VALUE:
			case EXPONENTIAL_VALUE:
			case STRING_VALUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(2122);
				literalExpression();
				}
				break;
			case DOLLAR:
			case NAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(2123);
				featureReferenceExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetaclassContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode METACLASS() { return getToken(KerMLParser.METACLASS, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public MetaclassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metaclass; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetaclass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetaclass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetaclass(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetaclassContext metaclass() throws RecognitionException {
		MetaclassContext _localctx = new MetaclassContext(_ctx, getState());
		enterRule(_localctx, 502, RULE_metaclass);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2126);
			typePrefix();
			setState(2127);
			match(METACLASS);
			setState(2128);
			classifierDeclaration();
			setState(2129);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrefixMetadataAnnotationContext extends ParserRuleContext {
		public TerminalNode HASH() { return getToken(KerMLParser.HASH, 0); }
		public PrefixMetadataFeatureContext prefixMetadataFeature() {
			return getRuleContext(PrefixMetadataFeatureContext.class,0);
		}
		public PrefixMetadataAnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixMetadataAnnotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPrefixMetadataAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPrefixMetadataAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPrefixMetadataAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixMetadataAnnotationContext prefixMetadataAnnotation() throws RecognitionException {
		PrefixMetadataAnnotationContext _localctx = new PrefixMetadataAnnotationContext(_ctx, getState());
		enterRule(_localctx, 504, RULE_prefixMetadataAnnotation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2131);
			match(HASH);
			setState(2132);
			prefixMetadataFeature();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrefixMetadataMemberContext extends ParserRuleContext {
		public TerminalNode HASH() { return getToken(KerMLParser.HASH, 0); }
		public PrefixMetadataFeatureContext prefixMetadataFeature() {
			return getRuleContext(PrefixMetadataFeatureContext.class,0);
		}
		public PrefixMetadataMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixMetadataMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPrefixMetadataMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPrefixMetadataMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPrefixMetadataMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixMetadataMemberContext prefixMetadataMember() throws RecognitionException {
		PrefixMetadataMemberContext _localctx = new PrefixMetadataMemberContext(_ctx, getState());
		enterRule(_localctx, 506, RULE_prefixMetadataMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2134);
			match(HASH);
			setState(2135);
			prefixMetadataFeature();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrefixMetadataFeatureContext extends ParserRuleContext {
		public OwnedFeatureTypingContext ownedFeatureTyping() {
			return getRuleContext(OwnedFeatureTypingContext.class,0);
		}
		public PrefixMetadataFeatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixMetadataFeature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPrefixMetadataFeature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPrefixMetadataFeature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPrefixMetadataFeature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixMetadataFeatureContext prefixMetadataFeature() throws RecognitionException {
		PrefixMetadataFeatureContext _localctx = new PrefixMetadataFeatureContext(_ctx, getState());
		enterRule(_localctx, 508, RULE_prefixMetadataFeature);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2137);
			ownedFeatureTyping();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetadataFeatureContext extends ParserRuleContext {
		public MetadataFeatureDeclarationContext metadataFeatureDeclaration() {
			return getRuleContext(MetadataFeatureDeclarationContext.class,0);
		}
		public MetadataBodyContext metadataBody() {
			return getRuleContext(MetadataBodyContext.class,0);
		}
		public TerminalNode AT() { return getToken(KerMLParser.AT, 0); }
		public TerminalNode METADATA() { return getToken(KerMLParser.METADATA, 0); }
		public List<PrefixMetadataMemberContext> prefixMetadataMember() {
			return getRuleContexts(PrefixMetadataMemberContext.class);
		}
		public PrefixMetadataMemberContext prefixMetadataMember(int i) {
			return getRuleContext(PrefixMetadataMemberContext.class,i);
		}
		public TerminalNode ABOUT() { return getToken(KerMLParser.ABOUT, 0); }
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KerMLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KerMLParser.COMMA, i);
		}
		public MetadataFeatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metadataFeature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetadataFeature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetadataFeature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetadataFeature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetadataFeatureContext metadataFeature() throws RecognitionException {
		MetadataFeatureContext _localctx = new MetadataFeatureContext(_ctx, getState());
		enterRule(_localctx, 510, RULE_metadataFeature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2142);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==HASH) {
				{
				{
				setState(2139);
				prefixMetadataMember();
				}
				}
				setState(2144);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2145);
			_la = _input.LA(1);
			if ( !(_la==METADATA || _la==AT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(2146);
			metadataFeatureDeclaration();
			setState(2156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ABOUT) {
				{
				setState(2147);
				match(ABOUT);
				setState(2148);
				annotation();
				setState(2153);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(2149);
					match(COMMA);
					setState(2150);
					annotation();
					}
					}
					setState(2155);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(2158);
			metadataBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetadataFeatureDeclarationContext extends ParserRuleContext {
		public OwnedFeatureTypingContext ownedFeatureTyping() {
			return getRuleContext(OwnedFeatureTypingContext.class,0);
		}
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public TerminalNode COLON() { return getToken(KerMLParser.COLON, 0); }
		public TerminalNode TYPED() { return getToken(KerMLParser.TYPED, 0); }
		public TerminalNode BY() { return getToken(KerMLParser.BY, 0); }
		public MetadataFeatureDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metadataFeatureDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetadataFeatureDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetadataFeatureDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetadataFeatureDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetadataFeatureDeclarationContext metadataFeatureDeclaration() throws RecognitionException {
		MetadataFeatureDeclarationContext _localctx = new MetadataFeatureDeclarationContext(_ctx, getState());
		enterRule(_localctx, 512, RULE_metadataFeatureDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2166);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,208,_ctx) ) {
			case 1:
				{
				setState(2160);
				identification();
				setState(2164);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case COLON:
					{
					setState(2161);
					match(COLON);
					}
					break;
				case TYPED:
					{
					setState(2162);
					match(TYPED);
					setState(2163);
					match(BY);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			}
			setState(2168);
			ownedFeatureTyping();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetadataBodyContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(KerMLParser.SEMICOLON, 0); }
		public TerminalNode LBRACE() { return getToken(KerMLParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(KerMLParser.RBRACE, 0); }
		public List<MetadataBodyElementContext> metadataBodyElement() {
			return getRuleContexts(MetadataBodyElementContext.class);
		}
		public MetadataBodyElementContext metadataBodyElement(int i) {
			return getRuleContext(MetadataBodyElementContext.class,i);
		}
		public MetadataBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metadataBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetadataBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetadataBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetadataBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetadataBodyContext metadataBody() throws RecognitionException {
		MetadataBodyContext _localctx = new MetadataBodyContext(_ctx, getState());
		enterRule(_localctx, 514, RULE_metadataBody);
		int _la;
		try {
			setState(2179);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEMICOLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(2170);
				match(SEMICOLON);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(2171);
				match(LBRACE);
				setState(2175);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3834797145000844916L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 42128171594755L) != 0) || ((((_la - 134)) & ~0x3f) == 0 && ((1L << (_la - 134)) & 34604035L) != 0)) {
					{
					{
					setState(2172);
					metadataBodyElement();
					}
					}
					setState(2177);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(2178);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetadataBodyElementContext extends ParserRuleContext {
		public NonFeatureMemberContext nonFeatureMember() {
			return getRuleContext(NonFeatureMemberContext.class,0);
		}
		public MetadataBodyFeatureMemberContext metadataBodyFeatureMember() {
			return getRuleContext(MetadataBodyFeatureMemberContext.class,0);
		}
		public AliasMemberContext aliasMember() {
			return getRuleContext(AliasMemberContext.class,0);
		}
		public Import_Context import_() {
			return getRuleContext(Import_Context.class,0);
		}
		public MetadataBodyElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metadataBodyElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetadataBodyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetadataBodyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetadataBodyElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetadataBodyElementContext metadataBodyElement() throws RecognitionException {
		MetadataBodyElementContext _localctx = new MetadataBodyElementContext(_ctx, getState());
		enterRule(_localctx, 516, RULE_metadataBodyElement);
		try {
			setState(2185);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,211,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2181);
				nonFeatureMember();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2182);
				metadataBodyFeatureMember();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2183);
				aliasMember();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2184);
				import_();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetadataBodyFeatureMemberContext extends ParserRuleContext {
		public MetadataBodyFeatureContext metadataBodyFeature() {
			return getRuleContext(MetadataBodyFeatureContext.class,0);
		}
		public MetadataBodyFeatureMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metadataBodyFeatureMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetadataBodyFeatureMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetadataBodyFeatureMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetadataBodyFeatureMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetadataBodyFeatureMemberContext metadataBodyFeatureMember() throws RecognitionException {
		MetadataBodyFeatureMemberContext _localctx = new MetadataBodyFeatureMemberContext(_ctx, getState());
		enterRule(_localctx, 518, RULE_metadataBodyFeatureMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2187);
			metadataBodyFeature();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetadataBodyFeatureContext extends ParserRuleContext {
		public OwnedRedefinitionContext ownedRedefinition() {
			return getRuleContext(OwnedRedefinitionContext.class,0);
		}
		public MetadataBodyContext metadataBody() {
			return getRuleContext(MetadataBodyContext.class,0);
		}
		public TerminalNode FEATURE() { return getToken(KerMLParser.FEATURE, 0); }
		public FeatureSpecializationPartContext featureSpecializationPart() {
			return getRuleContext(FeatureSpecializationPartContext.class,0);
		}
		public ValuePartContext valuePart() {
			return getRuleContext(ValuePartContext.class,0);
		}
		public TerminalNode COLON_GT_GT() { return getToken(KerMLParser.COLON_GT_GT, 0); }
		public TerminalNode REDEFINES() { return getToken(KerMLParser.REDEFINES, 0); }
		public MetadataBodyFeatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metadataBodyFeature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterMetadataBodyFeature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitMetadataBodyFeature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitMetadataBodyFeature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetadataBodyFeatureContext metadataBodyFeature() throws RecognitionException {
		MetadataBodyFeatureContext _localctx = new MetadataBodyFeatureContext(_ctx, getState());
		enterRule(_localctx, 520, RULE_metadataBodyFeature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2190);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FEATURE) {
				{
				setState(2189);
				match(FEATURE);
				}
			}

			setState(2193);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==REDEFINES || _la==COLON_GT_GT) {
				{
				setState(2192);
				_la = _input.LA(1);
				if ( !(_la==REDEFINES || _la==COLON_GT_GT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(2195);
			ownedRedefinition();
			setState(2197);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 22)) & ~0x3f) == 0 && ((1L << (_la - 22)) & 1442312965037490177L) != 0) || ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & 4503634289148033L) != 0)) {
				{
				setState(2196);
				featureSpecializationPart();
				}
			}

			setState(2200);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT || _la==COLON_EQUALS || _la==EQUALS) {
				{
				setState(2199);
				valuePart();
				}
			}

			setState(2202);
			metadataBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PackageContext extends ParserRuleContext {
		public PackageDeclarationContext packageDeclaration() {
			return getRuleContext(PackageDeclarationContext.class,0);
		}
		public PackageBodyContext packageBody() {
			return getRuleContext(PackageBodyContext.class,0);
		}
		public List<PrefixMetadataMemberContext> prefixMetadataMember() {
			return getRuleContexts(PrefixMetadataMemberContext.class);
		}
		public PrefixMetadataMemberContext prefixMetadataMember(int i) {
			return getRuleContext(PrefixMetadataMemberContext.class,i);
		}
		public PackageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_package; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPackage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPackage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPackage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageContext package_() throws RecognitionException {
		PackageContext _localctx = new PackageContext(_ctx, getState());
		enterRule(_localctx, 522, RULE_package);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2207);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==HASH) {
				{
				{
				setState(2204);
				prefixMetadataMember();
				}
				}
				setState(2209);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2210);
			packageDeclaration();
			setState(2211);
			packageBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LibraryPackageContext extends ParserRuleContext {
		public Token isStandard;
		public TerminalNode LIBRARY() { return getToken(KerMLParser.LIBRARY, 0); }
		public PackageDeclarationContext packageDeclaration() {
			return getRuleContext(PackageDeclarationContext.class,0);
		}
		public PackageBodyContext packageBody() {
			return getRuleContext(PackageBodyContext.class,0);
		}
		public TerminalNode STANDARD() { return getToken(KerMLParser.STANDARD, 0); }
		public List<PrefixMetadataMemberContext> prefixMetadataMember() {
			return getRuleContexts(PrefixMetadataMemberContext.class);
		}
		public PrefixMetadataMemberContext prefixMetadataMember(int i) {
			return getRuleContext(PrefixMetadataMemberContext.class,i);
		}
		public LibraryPackageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_libraryPackage; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterLibraryPackage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitLibraryPackage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitLibraryPackage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LibraryPackageContext libraryPackage() throws RecognitionException {
		LibraryPackageContext _localctx = new LibraryPackageContext(_ctx, getState());
		enterRule(_localctx, 524, RULE_libraryPackage);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2213);
			((LibraryPackageContext)_localctx).isStandard = match(STANDARD);
			setState(2214);
			match(LIBRARY);
			setState(2218);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==HASH) {
				{
				{
				setState(2215);
				prefixMetadataMember();
				}
				}
				setState(2220);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(2221);
			packageDeclaration();
			setState(2222);
			packageBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PackageDeclarationContext extends ParserRuleContext {
		public TerminalNode PACKAGE() { return getToken(KerMLParser.PACKAGE, 0); }
		public IdentificationContext identification() {
			return getRuleContext(IdentificationContext.class,0);
		}
		public PackageDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPackageDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPackageDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPackageDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageDeclarationContext packageDeclaration() throws RecognitionException {
		PackageDeclarationContext _localctx = new PackageDeclarationContext(_ctx, getState());
		enterRule(_localctx, 526, RULE_packageDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2224);
			match(PACKAGE);
			setState(2225);
			identification();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PackageBodyContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(KerMLParser.SEMICOLON, 0); }
		public TerminalNode LBRACE() { return getToken(KerMLParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(KerMLParser.RBRACE, 0); }
		public List<NamespaceBodyElementContext> namespaceBodyElement() {
			return getRuleContexts(NamespaceBodyElementContext.class);
		}
		public NamespaceBodyElementContext namespaceBodyElement(int i) {
			return getRuleContext(NamespaceBodyElementContext.class,i);
		}
		public List<ElementFilterMemberContext> elementFilterMember() {
			return getRuleContexts(ElementFilterMemberContext.class);
		}
		public ElementFilterMemberContext elementFilterMember(int i) {
			return getRuleContext(ElementFilterMemberContext.class,i);
		}
		public PackageBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterPackageBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitPackageBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitPackageBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageBodyContext packageBody() throws RecognitionException {
		PackageBodyContext _localctx = new PackageBodyContext(_ctx, getState());
		enterRule(_localctx, 528, RULE_packageBody);
		int _la;
		try {
			setState(2237);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEMICOLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(2227);
				match(SEMICOLON);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(2228);
				match(LBRACE);
				setState(2233);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3824098325557155940L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 324320135914979083L) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & 1107689585L) != 0)) {
					{
					setState(2231);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,218,_ctx) ) {
					case 1:
						{
						setState(2229);
						namespaceBodyElement();
						}
						break;
					case 2:
						{
						setState(2230);
						elementFilterMember();
						}
						break;
					}
					}
					setState(2235);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(2236);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElementFilterMemberContext extends ParserRuleContext {
		public OwnedExpressionContext condition;
		public MemberPrefixContext memberPrefix() {
			return getRuleContext(MemberPrefixContext.class,0);
		}
		public TerminalNode FILTER() { return getToken(KerMLParser.FILTER, 0); }
		public TerminalNode SEMICOLON() { return getToken(KerMLParser.SEMICOLON, 0); }
		public OwnedExpressionContext ownedExpression() {
			return getRuleContext(OwnedExpressionContext.class,0);
		}
		public ElementFilterMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementFilterMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterElementFilterMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitElementFilterMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitElementFilterMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementFilterMemberContext elementFilterMember() throws RecognitionException {
		ElementFilterMemberContext _localctx = new ElementFilterMemberContext(_ctx, getState());
		enterRule(_localctx, 530, RULE_elementFilterMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2239);
			memberPrefix();
			setState(2240);
			match(FILTER);
			setState(2241);
			((ElementFilterMemberContext)_localctx).condition = ownedExpression(0);
			setState(2242);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ViewContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode VIEW() { return getToken(KerMLParser.VIEW, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public ViewBodyContext viewBody() {
			return getRuleContext(ViewBodyContext.class,0);
		}
		public ViewContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_view; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterView(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitView(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitView(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ViewContext view() throws RecognitionException {
		ViewContext _localctx = new ViewContext(_ctx, getState());
		enterRule(_localctx, 532, RULE_view);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2244);
			typePrefix();
			setState(2245);
			match(VIEW);
			setState(2246);
			classifierDeclaration();
			setState(2247);
			viewBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ViewBodyContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(KerMLParser.SEMICOLON, 0); }
		public TerminalNode LBRACE() { return getToken(KerMLParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(KerMLParser.RBRACE, 0); }
		public List<ViewBodyElementContext> viewBodyElement() {
			return getRuleContexts(ViewBodyElementContext.class);
		}
		public ViewBodyElementContext viewBodyElement(int i) {
			return getRuleContext(ViewBodyElementContext.class,i);
		}
		public ViewBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_viewBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterViewBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitViewBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitViewBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ViewBodyContext viewBody() throws RecognitionException {
		ViewBodyContext _localctx = new ViewBodyContext(_ctx, getState());
		enterRule(_localctx, 534, RULE_viewBody);
		int _la;
		try {
			setState(2258);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SEMICOLON:
				enterOuterAlt(_localctx, 1);
				{
				setState(2249);
				match(SEMICOLON);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(2250);
				match(LBRACE);
				setState(2254);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2671177362116188260L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 324320135915503371L) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & 1107689585L) != 0)) {
					{
					{
					setState(2251);
					viewBodyElement();
					}
					}
					setState(2256);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(2257);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ViewBodyElementContext extends ParserRuleContext {
		public TypeBodyElementContext typeBodyElement() {
			return getRuleContext(TypeBodyElementContext.class,0);
		}
		public ExposeContext expose() {
			return getRuleContext(ExposeContext.class,0);
		}
		public ViewRenderingMemberContext viewRenderingMember() {
			return getRuleContext(ViewRenderingMemberContext.class,0);
		}
		public ViewBodyElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_viewBodyElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterViewBodyElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitViewBodyElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitViewBodyElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ViewBodyElementContext viewBodyElement() throws RecognitionException {
		ViewBodyElementContext _localctx = new ViewBodyElementContext(_ctx, getState());
		enterRule(_localctx, 536, RULE_viewBodyElement);
		try {
			setState(2263);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,223,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2260);
				typeBodyElement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2261);
				expose();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2262);
				viewRenderingMember();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExposeContext extends ParserRuleContext {
		public Token isImportAll;
		public TerminalNode EXPOSE() { return getToken(KerMLParser.EXPOSE, 0); }
		public ImportDeclarationContext importDeclaration() {
			return getRuleContext(ImportDeclarationContext.class,0);
		}
		public RelationshipBodyContext relationshipBody() {
			return getRuleContext(RelationshipBodyContext.class,0);
		}
		public TerminalNode ALL() { return getToken(KerMLParser.ALL, 0); }
		public ExposeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expose; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterExpose(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitExpose(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitExpose(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExposeContext expose() throws RecognitionException {
		ExposeContext _localctx = new ExposeContext(_ctx, getState());
		enterRule(_localctx, 538, RULE_expose);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2265);
			match(EXPOSE);
			setState(2267);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ALL) {
				{
				setState(2266);
				((ExposeContext)_localctx).isImportAll = match(ALL);
				}
			}

			setState(2269);
			importDeclaration();
			setState(2270);
			relationshipBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ViewRenderingMemberContext extends ParserRuleContext {
		public MemberPrefixContext memberPrefix() {
			return getRuleContext(MemberPrefixContext.class,0);
		}
		public TerminalNode RENDER() { return getToken(KerMLParser.RENDER, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public ViewRenderingMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_viewRenderingMember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterViewRenderingMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitViewRenderingMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitViewRenderingMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ViewRenderingMemberContext viewRenderingMember() throws RecognitionException {
		ViewRenderingMemberContext _localctx = new ViewRenderingMemberContext(_ctx, getState());
		enterRule(_localctx, 540, RULE_viewRenderingMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2272);
			memberPrefix();
			setState(2273);
			match(RENDER);
			setState(2274);
			qualifiedName();
			setState(2275);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RenderingContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode RENDERING() { return getToken(KerMLParser.RENDERING, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public TypeBodyContext typeBody() {
			return getRuleContext(TypeBodyContext.class,0);
		}
		public RenderingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rendering; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterRendering(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitRendering(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitRendering(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RenderingContext rendering() throws RecognitionException {
		RenderingContext _localctx = new RenderingContext(_ctx, getState());
		enterRule(_localctx, 542, RULE_rendering);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2277);
			typePrefix();
			setState(2278);
			match(RENDERING);
			setState(2279);
			classifierDeclaration();
			setState(2280);
			typeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ViewpointContext extends ParserRuleContext {
		public TypePrefixContext typePrefix() {
			return getRuleContext(TypePrefixContext.class,0);
		}
		public TerminalNode VIEWPOINT() { return getToken(KerMLParser.VIEWPOINT, 0); }
		public ClassifierDeclarationContext classifierDeclaration() {
			return getRuleContext(ClassifierDeclarationContext.class,0);
		}
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public ViewpointContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_viewpoint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).enterViewpoint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KerMLListener ) ((KerMLListener)listener).exitViewpoint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KerMLVisitor ) return ((KerMLVisitor<? extends T>)visitor).visitViewpoint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ViewpointContext viewpoint() throws RecognitionException {
		ViewpointContext _localctx = new ViewpointContext(_ctx, getState());
		enterRule(_localctx, 544, RULE_viewpoint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2282);
			typePrefix();
			setState(2283);
			match(VIEWPOINT);
			setState(2284);
			classifierDeclaration();
			setState(2285);
			functionBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 140:
			return ownedExpression_sempred((OwnedExpressionContext)_localctx, predIndex);
		case 169:
			return primaryExpression_sempred((PrimaryExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean ownedExpression_sempred(OwnedExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 11);
		case 1:
			return precpred(_ctx, 10);
		case 2:
			return precpred(_ctx, 7);
		case 3:
			return precpred(_ctx, 5);
		}
		return true;
	}
	private boolean primaryExpression_sempred(PrimaryExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 8);
		case 5:
			return precpred(_ctx, 7);
		case 6:
			return precpred(_ctx, 6);
		case 7:
			return precpred(_ctx, 5);
		case 8:
			return precpred(_ctx, 4);
		case 9:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u00a0\u08f0\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007"+
		"\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007"+
		"\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007"+
		",\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u0007"+
		"1\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u0007"+
		"6\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007"+
		";\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007"+
		"@\u0002A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007"+
		"E\u0002F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0002J\u0007"+
		"J\u0002K\u0007K\u0002L\u0007L\u0002M\u0007M\u0002N\u0007N\u0002O\u0007"+
		"O\u0002P\u0007P\u0002Q\u0007Q\u0002R\u0007R\u0002S\u0007S\u0002T\u0007"+
		"T\u0002U\u0007U\u0002V\u0007V\u0002W\u0007W\u0002X\u0007X\u0002Y\u0007"+
		"Y\u0002Z\u0007Z\u0002[\u0007[\u0002\\\u0007\\\u0002]\u0007]\u0002^\u0007"+
		"^\u0002_\u0007_\u0002`\u0007`\u0002a\u0007a\u0002b\u0007b\u0002c\u0007"+
		"c\u0002d\u0007d\u0002e\u0007e\u0002f\u0007f\u0002g\u0007g\u0002h\u0007"+
		"h\u0002i\u0007i\u0002j\u0007j\u0002k\u0007k\u0002l\u0007l\u0002m\u0007"+
		"m\u0002n\u0007n\u0002o\u0007o\u0002p\u0007p\u0002q\u0007q\u0002r\u0007"+
		"r\u0002s\u0007s\u0002t\u0007t\u0002u\u0007u\u0002v\u0007v\u0002w\u0007"+
		"w\u0002x\u0007x\u0002y\u0007y\u0002z\u0007z\u0002{\u0007{\u0002|\u0007"+
		"|\u0002}\u0007}\u0002~\u0007~\u0002\u007f\u0007\u007f\u0002\u0080\u0007"+
		"\u0080\u0002\u0081\u0007\u0081\u0002\u0082\u0007\u0082\u0002\u0083\u0007"+
		"\u0083\u0002\u0084\u0007\u0084\u0002\u0085\u0007\u0085\u0002\u0086\u0007"+
		"\u0086\u0002\u0087\u0007\u0087\u0002\u0088\u0007\u0088\u0002\u0089\u0007"+
		"\u0089\u0002\u008a\u0007\u008a\u0002\u008b\u0007\u008b\u0002\u008c\u0007"+
		"\u008c\u0002\u008d\u0007\u008d\u0002\u008e\u0007\u008e\u0002\u008f\u0007"+
		"\u008f\u0002\u0090\u0007\u0090\u0002\u0091\u0007\u0091\u0002\u0092\u0007"+
		"\u0092\u0002\u0093\u0007\u0093\u0002\u0094\u0007\u0094\u0002\u0095\u0007"+
		"\u0095\u0002\u0096\u0007\u0096\u0002\u0097\u0007\u0097\u0002\u0098\u0007"+
		"\u0098\u0002\u0099\u0007\u0099\u0002\u009a\u0007\u009a\u0002\u009b\u0007"+
		"\u009b\u0002\u009c\u0007\u009c\u0002\u009d\u0007\u009d\u0002\u009e\u0007"+
		"\u009e\u0002\u009f\u0007\u009f\u0002\u00a0\u0007\u00a0\u0002\u00a1\u0007"+
		"\u00a1\u0002\u00a2\u0007\u00a2\u0002\u00a3\u0007\u00a3\u0002\u00a4\u0007"+
		"\u00a4\u0002\u00a5\u0007\u00a5\u0002\u00a6\u0007\u00a6\u0002\u00a7\u0007"+
		"\u00a7\u0002\u00a8\u0007\u00a8\u0002\u00a9\u0007\u00a9\u0002\u00aa\u0007"+
		"\u00aa\u0002\u00ab\u0007\u00ab\u0002\u00ac\u0007\u00ac\u0002\u00ad\u0007"+
		"\u00ad\u0002\u00ae\u0007\u00ae\u0002\u00af\u0007\u00af\u0002\u00b0\u0007"+
		"\u00b0\u0002\u00b1\u0007\u00b1\u0002\u00b2\u0007\u00b2\u0002\u00b3\u0007"+
		"\u00b3\u0002\u00b4\u0007\u00b4\u0002\u00b5\u0007\u00b5\u0002\u00b6\u0007"+
		"\u00b6\u0002\u00b7\u0007\u00b7\u0002\u00b8\u0007\u00b8\u0002\u00b9\u0007"+
		"\u00b9\u0002\u00ba\u0007\u00ba\u0002\u00bb\u0007\u00bb\u0002\u00bc\u0007"+
		"\u00bc\u0002\u00bd\u0007\u00bd\u0002\u00be\u0007\u00be\u0002\u00bf\u0007"+
		"\u00bf\u0002\u00c0\u0007\u00c0\u0002\u00c1\u0007\u00c1\u0002\u00c2\u0007"+
		"\u00c2\u0002\u00c3\u0007\u00c3\u0002\u00c4\u0007\u00c4\u0002\u00c5\u0007"+
		"\u00c5\u0002\u00c6\u0007\u00c6\u0002\u00c7\u0007\u00c7\u0002\u00c8\u0007"+
		"\u00c8\u0002\u00c9\u0007\u00c9\u0002\u00ca\u0007\u00ca\u0002\u00cb\u0007"+
		"\u00cb\u0002\u00cc\u0007\u00cc\u0002\u00cd\u0007\u00cd\u0002\u00ce\u0007"+
		"\u00ce\u0002\u00cf\u0007\u00cf\u0002\u00d0\u0007\u00d0\u0002\u00d1\u0007"+
		"\u00d1\u0002\u00d2\u0007\u00d2\u0002\u00d3\u0007\u00d3\u0002\u00d4\u0007"+
		"\u00d4\u0002\u00d5\u0007\u00d5\u0002\u00d6\u0007\u00d6\u0002\u00d7\u0007"+
		"\u00d7\u0002\u00d8\u0007\u00d8\u0002\u00d9\u0007\u00d9\u0002\u00da\u0007"+
		"\u00da\u0002\u00db\u0007\u00db\u0002\u00dc\u0007\u00dc\u0002\u00dd\u0007"+
		"\u00dd\u0002\u00de\u0007\u00de\u0002\u00df\u0007\u00df\u0002\u00e0\u0007"+
		"\u00e0\u0002\u00e1\u0007\u00e1\u0002\u00e2\u0007\u00e2\u0002\u00e3\u0007"+
		"\u00e3\u0002\u00e4\u0007\u00e4\u0002\u00e5\u0007\u00e5\u0002\u00e6\u0007"+
		"\u00e6\u0002\u00e7\u0007\u00e7\u0002\u00e8\u0007\u00e8\u0002\u00e9\u0007"+
		"\u00e9\u0002\u00ea\u0007\u00ea\u0002\u00eb\u0007\u00eb\u0002\u00ec\u0007"+
		"\u00ec\u0002\u00ed\u0007\u00ed\u0002\u00ee\u0007\u00ee\u0002\u00ef\u0007"+
		"\u00ef\u0002\u00f0\u0007\u00f0\u0002\u00f1\u0007\u00f1\u0002\u00f2\u0007"+
		"\u00f2\u0002\u00f3\u0007\u00f3\u0002\u00f4\u0007\u00f4\u0002\u00f5\u0007"+
		"\u00f5\u0002\u00f6\u0007\u00f6\u0002\u00f7\u0007\u00f7\u0002\u00f8\u0007"+
		"\u00f8\u0002\u00f9\u0007\u00f9\u0002\u00fa\u0007\u00fa\u0002\u00fb\u0007"+
		"\u00fb\u0002\u00fc\u0007\u00fc\u0002\u00fd\u0007\u00fd\u0002\u00fe\u0007"+
		"\u00fe\u0002\u00ff\u0007\u00ff\u0002\u0100\u0007\u0100\u0002\u0101\u0007"+
		"\u0101\u0002\u0102\u0007\u0102\u0002\u0103\u0007\u0103\u0002\u0104\u0007"+
		"\u0104\u0002\u0105\u0007\u0105\u0002\u0106\u0007\u0106\u0002\u0107\u0007"+
		"\u0107\u0002\u0108\u0007\u0108\u0002\u0109\u0007\u0109\u0002\u010a\u0007"+
		"\u010a\u0002\u010b\u0007\u010b\u0002\u010c\u0007\u010c\u0002\u010d\u0007"+
		"\u010d\u0002\u010e\u0007\u010e\u0002\u010f\u0007\u010f\u0002\u0110\u0007"+
		"\u0110\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u0226\b\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u0237\b\u0007\u0001\u0007\u0003"+
		"\u0007\u023a\b\u0007\u0001\b\u0001\b\u0001\b\u0005\b\u023f\b\b\n\b\f\b"+
		"\u0242\t\b\u0001\b\u0003\b\u0245\b\b\u0001\t\u0001\t\u0003\t\u0249\b\t"+
		"\u0001\n\u0001\n\u0003\n\u024d\b\n\u0001\u000b\u0005\u000b\u0250\b\u000b"+
		"\n\u000b\f\u000b\u0253\t\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u0257"+
		"\b\u000b\u0001\u000b\u0003\u000b\u025a\b\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0005\u000b\u025f\b\u000b\n\u000b\f\u000b\u0262\t\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u0268\b\u000b\n"+
		"\u000b\f\u000b\u026b\t\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e"+
		"\u0277\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0005\u000f\u027f\b\u000f\n\u000f\f\u000f\u0282\t\u000f\u0003"+
		"\u000f\u0284\b\u000f\u0003\u000f\u0286\b\u000f\u0001\u000f\u0001\u000f"+
		"\u0003\u000f\u028a\b\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0003\u0010\u0292\b\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0011\u0001\u0011\u0003\u0011\u0298\b\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0012\u0005\u0012\u029f\b\u0012\n\u0012"+
		"\f\u0012\u02a2\t\u0012\u0001\u0013\u0005\u0013\u02a5\b\u0013\n\u0013\f"+
		"\u0013\u02a8\t\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0005\u0015\u02b3"+
		"\b\u0015\n\u0015\f\u0015\u02b6\t\u0015\u0001\u0015\u0003\u0015\u02b9\b"+
		"\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u02bf"+
		"\b\u0016\u0001\u0017\u0003\u0017\u02c2\b\u0017\u0001\u0018\u0001\u0018"+
		"\u0001\u0019\u0001\u0019\u0003\u0019\u02c8\b\u0019\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u02d5\b\u001c\u0001\u001c"+
		"\u0003\u001c\u02d8\b\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001d\u0001\u001d\u0003\u001d\u02e0\b\u001d\u0001\u001d\u0001\u001d"+
		"\u0005\u001d\u02e4\b\u001d\n\u001d\f\u001d\u02e7\t\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e\u02ee\b\u001e\u0001"+
		"\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0003\u001f\u02f5"+
		"\b\u001f\u0001 \u0001 \u0001 \u0003 \u02fa\b \u0001!\u0001!\u0001!\u0001"+
		"!\u0001!\u0003!\u0301\b!\u0001!\u0003!\u0304\b!\u0001\"\u0001\"\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0003\"\u030c\b\"\u0003\"\u030e\b\"\u0001\""+
		"\u0004\"\u0311\b\"\u000b\"\f\"\u0312\u0001#\u0001#\u0001#\u0001#\u0001"+
		"$\u0001$\u0003$\u031b\b$\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001"+
		"%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001"+
		"%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001"+
		"%\u0001%\u0001%\u0003%\u033a\b%\u0001&\u0001&\u0001&\u0001&\u0001&\u0001"+
		"&\u0001&\u0001&\u0001&\u0001&\u0003&\u0346\b&\u0001\'\u0001\'\u0001\'"+
		"\u0001\'\u0001\'\u0001(\u0003(\u034e\b(\u0001(\u0005(\u0351\b(\n(\f(\u0354"+
		"\t(\u0001)\u0003)\u0357\b)\u0001)\u0001)\u0003)\u035b\b)\u0001)\u0001"+
		")\u0004)\u035f\b)\u000b)\f)\u0360\u0001)\u0005)\u0364\b)\n)\f)\u0367\t"+
		")\u0001*\u0001*\u0001*\u0001*\u0005*\u036d\b*\n*\f*\u0370\t*\u0001+\u0001"+
		"+\u0001+\u0001,\u0001,\u0001,\u0001,\u0003,\u0379\b,\u0001-\u0001-\u0001"+
		"-\u0001-\u0001-\u0005-\u0380\b-\n-\f-\u0383\t-\u0001.\u0001.\u0001.\u0001"+
		".\u0005.\u0389\b.\n.\f.\u038c\t.\u0001/\u0001/\u0001/\u0001/\u0005/\u0392"+
		"\b/\n/\f/\u0395\t/\u00010\u00010\u00010\u00010\u00050\u039b\b0\n0\f0\u039e"+
		"\t0\u00011\u00011\u00011\u00051\u03a3\b1\n1\f1\u03a6\t1\u00011\u00031"+
		"\u03a9\b1\u00012\u00012\u00012\u00012\u00012\u00032\u03b0\b2\u00013\u0001"+
		"3\u00033\u03b4\b3\u00013\u00013\u00013\u00013\u00013\u00013\u00014\u0001"+
		"4\u00015\u00015\u00035\u03c0\b5\u00016\u00016\u00036\u03c4\b6\u00017\u0001"+
		"7\u00037\u03c8\b7\u00017\u00017\u00017\u00037\u03cd\b7\u00017\u00017\u0001"+
		"7\u00037\u03d2\b7\u00017\u00017\u00018\u00018\u00038\u03d8\b8\u00019\u0001"+
		"9\u00039\u03dc\b9\u00019\u00019\u00019\u00039\u03e1\b9\u00019\u00019\u0001"+
		"9\u00039\u03e6\b9\u00019\u00019\u0001:\u0001:\u0003:\u03ec\b:\u0001;\u0001"+
		";\u0003;\u03f0\b;\u0001<\u0001<\u0003<\u03f4\b<\u0001=\u0001=\u0003=\u03f8"+
		"\b=\u0001>\u0001>\u0003>\u03fc\b>\u0001?\u0001?\u0001?\u0001?\u0001@\u0001"+
		"@\u0001@\u0001A\u0001A\u0001A\u0001A\u0001A\u0001B\u0003B\u040b\bB\u0001"+
		"B\u0001B\u0003B\u040f\bB\u0001B\u0001B\u0003B\u0413\bB\u0001B\u0005B\u0416"+
		"\bB\nB\fB\u0419\tB\u0001C\u0001C\u0001C\u0001C\u0005C\u041f\bC\nC\fC\u0422"+
		"\tC\u0001D\u0001D\u0003D\u0426\bD\u0001D\u0001D\u0001D\u0001D\u0001D\u0001"+
		"D\u0001E\u0001E\u0001F\u0001F\u0001F\u0003F\u0433\bF\u0001F\u0003F\u0436"+
		"\bF\u0001F\u0001F\u0003F\u043a\bF\u0001F\u0001F\u0003F\u043e\bF\u0001"+
		"F\u0003F\u0441\bF\u0001F\u0001F\u0001G\u0003G\u0446\bG\u0001G\u0001G\u0001"+
		"H\u0003H\u044b\bH\u0001H\u0003H\u044e\bH\u0001H\u0003H\u0451\bH\u0001"+
		"H\u0001H\u0003H\u0455\bH\u0001H\u0001H\u0003H\u0459\bH\u0001I\u0001I\u0003"+
		"I\u045d\bI\u0001I\u0003I\u0460\bI\u0001I\u0005I\u0463\bI\nI\fI\u0466\t"+
		"I\u0001J\u0001J\u0001K\u0001K\u0001K\u0001L\u0001L\u0001M\u0003M\u0470"+
		"\bM\u0001M\u0001M\u0001M\u0003M\u0475\bM\u0001M\u0001M\u0003M\u0479\b"+
		"M\u0001M\u0005M\u047c\bM\nM\fM\u047f\tM\u0001N\u0001N\u0001N\u0001N\u0003"+
		"N\u0485\bN\u0001N\u0003N\u0488\bN\u0001O\u0001O\u0001O\u0001O\u0003O\u048e"+
		"\bO\u0001P\u0001P\u0001P\u0003P\u0493\bP\u0001Q\u0001Q\u0001Q\u0001Q\u0001"+
		"R\u0001R\u0001R\u0001R\u0001R\u0005R\u049e\bR\nR\fR\u04a1\tR\u0001S\u0004"+
		"S\u04a4\bS\u000bS\fS\u04a5\u0001S\u0003S\u04a9\bS\u0001S\u0005S\u04ac"+
		"\bS\nS\fS\u04af\tS\u0001S\u0001S\u0005S\u04b3\bS\nS\fS\u04b6\tS\u0003"+
		"S\u04b8\bS\u0001T\u0001T\u0003T\u04bc\bT\u0001T\u0001T\u0003T\u04c0\b"+
		"T\u0001T\u0001T\u0003T\u04c4\bT\u0003T\u04c6\bT\u0003T\u04c8\bT\u0001"+
		"U\u0001U\u0001U\u0001U\u0001U\u0003U\u04cf\bU\u0001V\u0001V\u0001V\u0005"+
		"V\u04d4\bV\nV\fV\u04d7\tV\u0001W\u0001W\u0001W\u0001X\u0001X\u0001X\u0005"+
		"X\u04df\bX\nX\fX\u04e2\tX\u0001Y\u0001Y\u0001Y\u0001Z\u0001Z\u0001Z\u0001"+
		"[\u0001[\u0001[\u0001\\\u0001\\\u0001\\\u0005\\\u04f0\b\\\n\\\f\\\u04f3"+
		"\t\\\u0001]\u0001]\u0001]\u0001^\u0001^\u0003^\u04fa\b^\u0001^\u0001^"+
		"\u0001^\u0001^\u0001^\u0001^\u0001_\u0001_\u0001`\u0001`\u0003`\u0506"+
		"\b`\u0001`\u0001`\u0001`\u0001`\u0001`\u0001`\u0001a\u0001a\u0001b\u0001"+
		"b\u0001c\u0001c\u0001d\u0001d\u0003d\u0516\bd\u0001d\u0001d\u0001d\u0001"+
		"d\u0001d\u0001d\u0001e\u0001e\u0001f\u0001f\u0001g\u0001g\u0001g\u0004"+
		"g\u0525\bg\u000bg\fg\u0526\u0001h\u0001h\u0001i\u0001i\u0003i\u052d\b"+
		"i\u0003i\u052f\bi\u0001i\u0001i\u0001i\u0003i\u0534\bi\u0001i\u0001i\u0001"+
		"i\u0003i\u0539\bi\u0001i\u0001i\u0001j\u0001j\u0003j\u053f\bj\u0001k\u0001"+
		"k\u0001k\u0001k\u0003k\u0545\bk\u0001k\u0001k\u0001k\u0001k\u0001k\u0001"+
		"l\u0001l\u0001m\u0001m\u0001m\u0001m\u0001m\u0001n\u0001n\u0001n\u0001"+
		"n\u0001n\u0001o\u0001o\u0001o\u0001o\u0001o\u0001p\u0001p\u0001p\u0001"+
		"p\u0001p\u0001q\u0001q\u0001q\u0001q\u0001q\u0001q\u0001r\u0001r\u0001"+
		"r\u0003r\u056b\br\u0001r\u0003r\u056e\br\u0001r\u0003r\u0571\br\u0001"+
		"r\u0001r\u0001s\u0001s\u0003s\u0577\bs\u0001t\u0003t\u057a\bt\u0001t\u0001"+
		"t\u0001t\u0003t\u057f\bt\u0003t\u0581\bt\u0001t\u0001t\u0001t\u0001t\u0001"+
		"u\u0003u\u0588\bu\u0001u\u0001u\u0001u\u0001u\u0001u\u0001u\u0005u\u0590"+
		"\bu\nu\fu\u0593\tu\u0001u\u0001u\u0001v\u0001v\u0001w\u0003w\u059a\bw"+
		"\u0001w\u0001w\u0003w\u059e\bw\u0001w\u0001w\u0001x\u0001x\u0001y\u0001"+
		"y\u0001z\u0001z\u0001z\u0001z\u0001z\u0001{\u0001{\u0001{\u0001{\u0001"+
		"{\u0001{\u0003{\u05b1\b{\u0001{\u0003{\u05b4\b{\u0001{\u0003{\u05b7\b"+
		"{\u0001{\u0001{\u0001{\u0001{\u0003{\u05bd\b{\u0003{\u05bf\b{\u0001|\u0001"+
		"|\u0001|\u0001|\u0001|\u0001}\u0001}\u0001}\u0001}\u0001}\u0001}\u0003"+
		"}\u05cc\b}\u0001}\u0003}\u05cf\b}\u0001}\u0003}\u05d2\b}\u0001}\u0001"+
		"}\u0001}\u0001}\u0003}\u05d8\b}\u0003}\u05da\b}\u0001~\u0001~\u0001~\u0001"+
		"~\u0001~\u0001\u007f\u0001\u007f\u0001\u007f\u0003\u007f\u05e4\b\u007f"+
		"\u0001\u007f\u0003\u007f\u05e7\b\u007f\u0001\u007f\u0001\u007f\u0001\u0080"+
		"\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0081\u0001\u0081"+
		"\u0001\u0081\u0001\u0081\u0001\u0081\u0003\u0081\u05f5\b\u0081\u0001\u0082"+
		"\u0001\u0082\u0005\u0082\u05f9\b\u0082\n\u0082\f\u0082\u05fc\t\u0082\u0001"+
		"\u0082\u0003\u0082\u05ff\b\u0082\u0001\u0083\u0001\u0083\u0001\u0083\u0001"+
		"\u0083\u0001\u0084\u0001\u0084\u0001\u0084\u0001\u0085\u0001\u0085\u0001"+
		"\u0085\u0003\u0085\u060b\b\u0085\u0001\u0085\u0003\u0085\u060e\b\u0085"+
		"\u0001\u0085\u0001\u0085\u0001\u0086\u0001\u0086\u0001\u0086\u0001\u0086"+
		"\u0001\u0086\u0001\u0087\u0001\u0087\u0001\u0087\u0003\u0087\u061a\b\u0087"+
		"\u0001\u0087\u0003\u0087\u061d\b\u0087\u0001\u0087\u0001\u0087\u0001\u0088"+
		"\u0001\u0088\u0001\u0088\u0001\u0088\u0003\u0088\u0625\b\u0088\u0001\u0088"+
		"\u0003\u0088\u0628\b\u0088\u0001\u0088\u0003\u0088\u062b\b\u0088\u0001"+
		"\u0088\u0001\u0088\u0001\u0089\u0001\u0089\u0001\u008a\u0001\u008a\u0001"+
		"\u008b\u0001\u008b\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001"+
		"\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001"+
		"\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001"+
		"\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001"+
		"\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001"+
		"\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0003"+
		"\u008c\u0657\b\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001"+
		"\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001"+
		"\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001\u008c\u0001"+
		"\u008c\u0001\u008c\u0005\u008c\u066b\b\u008c\n\u008c\f\u008c\u066e\t\u008c"+
		"\u0001\u008d\u0001\u008d\u0001\u008e\u0001\u008e\u0001\u008e\u0001\u008e"+
		"\u0001\u008e\u0001\u008f\u0001\u008f\u0001\u0090\u0001\u0090\u0001\u0090"+
		"\u0001\u0090\u0001\u0091\u0001\u0091\u0001\u0092\u0003\u0092\u0680\b\u0092"+
		"\u0001\u0092\u0001\u0092\u0001\u0092\u0001\u0092\u0001\u0092\u0001\u0092"+
		"\u0003\u0092\u0688\b\u0092\u0001\u0092\u0001\u0092\u0001\u0093\u0001\u0093"+
		"\u0001\u0094\u0001\u0094\u0001\u0095\u0001\u0095\u0001\u0095\u0001\u0095"+
		"\u0001\u0095\u0001\u0095\u0001\u0095\u0003\u0095\u0697\b\u0095\u0001\u0095"+
		"\u0001\u0095\u0001\u0096\u0001\u0096\u0001\u0097\u0001\u0097\u0001\u0098"+
		"\u0001\u0098\u0001\u0099\u0001\u0099\u0001\u009a\u0001\u009a\u0001\u009b"+
		"\u0001\u009b\u0001\u009c\u0001\u009c\u0001\u009d\u0001\u009d\u0001\u009e"+
		"\u0001\u009e\u0001\u009f\u0001\u009f\u0001\u00a0\u0001\u00a0\u0001\u00a0"+
		"\u0001\u00a1\u0001\u00a1\u0001\u00a2\u0001\u00a2\u0001\u00a2\u0001\u00a3"+
		"\u0001\u00a3\u0001\u00a4\u0001\u00a4\u0001\u00a5\u0001\u00a5\u0001\u00a6"+
		"\u0001\u00a6\u0001\u00a7\u0001\u00a7\u0001\u00a8\u0001\u00a8\u0001\u00a9"+
		"\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0003\u00a9"+
		"\u06c9\b\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9"+
		"\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9"+
		"\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9"+
		"\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0001\u00a9"+
		"\u0001\u00a9\u0001\u00a9\u0001\u00a9\u0003\u00a9\u06e5\b\u00a9\u0001\u00a9"+
		"\u0001\u00a9\u0005\u00a9\u06e9\b\u00a9\n\u00a9\f\u00a9\u06ec\t\u00a9\u0001"+
		"\u00aa\u0001\u00aa\u0001\u00ab\u0001\u00ab\u0001\u00ac\u0001\u00ac\u0001"+
		"\u00ad\u0001\u00ad\u0001\u00ad\u0001\u00ad\u0001\u00ad\u0001\u00ad\u0001"+
		"\u00ad\u0003\u00ad\u06fb\b\u00ad\u0001\u00ae\u0001\u00ae\u0001\u00af\u0001"+
		"\u00af\u0001\u00b0\u0001\u00b0\u0001\u00b1\u0001\u00b1\u0001\u00b1\u0001"+
		"\u00b1\u0001\u00b1\u0001\u00b2\u0001\u00b2\u0001\u00b2\u0001\u00b2\u0001"+
		"\u00b2\u0001\u00b2\u0001\u00b3\u0001\u00b3\u0001\u00b3\u0001\u00b3\u0001"+
		"\u00b4\u0001\u00b4\u0003\u00b4\u0714\b\u00b4\u0001\u00b4\u0003\u00b4\u0717"+
		"\b\u00b4\u0001\u00b5\u0001\u00b5\u0001\u00b5\u0001\u00b5\u0001\u00b6\u0001"+
		"\u00b6\u0001\u00b7\u0001\u00b7\u0001\u00b7\u0001\u00b7\u0001\u00b8\u0001"+
		"\u00b8\u0001\u00b8\u0001\u00b8\u0001\u00b9\u0001\u00b9\u0001\u00b9\u0001"+
		"\u00b9\u0001\u00ba\u0001\u00ba\u0001\u00ba\u0001\u00ba\u0001\u00ba\u0001"+
		"\u00ba\u0003\u00ba\u0731\b\u00ba\u0001\u00ba\u0001\u00ba\u0001\u00bb\u0001"+
		"\u00bb\u0001\u00bc\u0001\u00bc\u0001\u00bd\u0001\u00bd\u0001\u00be\u0001"+
		"\u00be\u0001\u00bf\u0001\u00bf\u0001\u00c0\u0001\u00c0\u0001\u00c1\u0001"+
		"\u00c1\u0001\u00c2\u0001\u00c2\u0001\u00c3\u0001\u00c3\u0001\u00c4\u0001"+
		"\u00c4\u0003\u00c4\u0749\b\u00c4\u0001\u00c5\u0001\u00c5\u0001\u00c6\u0001"+
		"\u00c6\u0001\u00c7\u0001\u00c7\u0001\u00c7\u0001\u00c7\u0001\u00c7\u0001"+
		"\u00c7\u0001\u00c7\u0003\u00c7\u0756\b\u00c7\u0001\u00c8\u0001\u00c8\u0001"+
		"\u00c8\u0003\u00c8\u075b\b\u00c8\u0001\u00c9\u0001\u00c9\u0001\u00c9\u0001"+
		"\u00ca\u0001\u00ca\u0001\u00cb\u0001\u00cb\u0001\u00cc\u0001\u00cc\u0001"+
		"\u00cc\u0001\u00cc\u0001\u00cd\u0001\u00cd\u0001\u00ce\u0001\u00ce\u0001"+
		"\u00ce\u0001\u00ce\u0001\u00cf\u0001\u00cf\u0001\u00cf\u0001\u00cf\u0001"+
		"\u00d0\u0001\u00d0\u0001\u00d1\u0001\u00d1\u0001\u00d2\u0001\u00d2\u0003"+
		"\u00d2\u0778\b\u00d2\u0001\u00d3\u0001\u00d3\u0001\u00d4\u0001\u00d4\u0001"+
		"\u00d5\u0001\u00d5\u0001\u00d5\u0003\u00d5\u0781\b\u00d5\u0001\u00d5\u0001"+
		"\u00d5\u0001\u00d6\u0001\u00d6\u0001\u00d6\u0005\u00d6\u0788\b\u00d6\n"+
		"\u00d6\f\u00d6\u078b\t\u00d6\u0001\u00d7\u0001\u00d7\u0001\u00d7\u0005"+
		"\u00d7\u0790\b\u00d7\n\u00d7\f\u00d7\u0793\t\u00d7\u0001\u00d8\u0001\u00d8"+
		"\u0001\u00d9\u0001\u00d9\u0001\u00d9\u0001\u00d9\u0001\u00da\u0001\u00da"+
		"\u0001\u00db\u0001\u00db\u0001\u00dc\u0001\u00dc\u0001\u00dd\u0001\u00dd"+
		"\u0001\u00dd\u0001\u00dd\u0001\u00de\u0001\u00de\u0001\u00de\u0001\u00de"+
		"\u0001\u00de\u0003\u00de\u07aa\b\u00de\u0001\u00df\u0001\u00df\u0001\u00e0"+
		"\u0001\u00e0\u0001\u00e1\u0001\u00e1\u0001\u00e2\u0001\u00e2\u0001\u00e3"+
		"\u0001\u00e3\u0001\u00e4\u0003\u00e4\u07b7\b\u00e4\u0001\u00e4\u0001\u00e4"+
		"\u0001\u00e4\u0003\u00e4\u07bc\b\u00e4\u0001\u00e5\u0001\u00e5\u0001\u00e6"+
		"\u0001\u00e6\u0001\u00e6\u0001\u00e6\u0001\u00e6\u0001\u00e7\u0001\u00e7"+
		"\u0001\u00e7\u0001\u00e7\u0001\u00e7\u0001\u00e8\u0001\u00e8\u0001\u00e8"+
		"\u0001\u00e8\u0001\u00e8\u0001\u00e8\u0001\u00e9\u0001\u00e9\u0003\u00e9"+
		"\u07d2\b\u00e9\u0001\u00e9\u0001\u00e9\u0003\u00e9\u07d6\b\u00e9\u0001"+
		"\u00e9\u0001\u00e9\u0001\u00e9\u0001\u00e9\u0001\u00e9\u0003\u00e9\u07dd"+
		"\b\u00e9\u0001\u00e9\u0003\u00e9\u07e0\b\u00e9\u0001\u00e9\u0001\u00e9"+
		"\u0001\u00e9\u0001\u00e9\u0003\u00e9\u07e6\b\u00e9\u0001\u00ea\u0001\u00ea"+
		"\u0001\u00eb\u0001\u00eb\u0001\u00eb\u0003\u00eb\u07ed\b\u00eb\u0001\u00eb"+
		"\u0001\u00eb\u0001\u00eb\u0001\u00eb\u0001\u00eb\u0003\u00eb\u07f4\b\u00eb"+
		"\u0001\u00eb\u0001\u00eb\u0003\u00eb\u07f8\b\u00eb\u0003\u00eb\u07fa\b"+
		"\u00eb\u0003\u00eb\u07fc\b\u00eb\u0001\u00ec\u0004\u00ec\u07ff\b\u00ec"+
		"\u000b\u00ec\f\u00ec\u0800\u0001\u00ec\u0003\u00ec\u0804\b\u00ec\u0001"+
		"\u00ec\u0005\u00ec\u0807\b\u00ec\n\u00ec\f\u00ec\u080a\t\u00ec\u0001\u00ec"+
		"\u0001\u00ec\u0004\u00ec\u080e\b\u00ec\u000b\u00ec\f\u00ec\u080f\u0003"+
		"\u00ec\u0812\b\u00ec\u0001\u00ed\u0001\u00ed\u0001\u00ee\u0001\u00ee\u0001"+
		"\u00ee\u0003\u00ee\u0819\b\u00ee\u0001\u00ee\u0001\u00ee\u0001\u00ef\u0001"+
		"\u00ef\u0001\u00f0\u0001\u00f0\u0001\u00f1\u0001\u00f1\u0001\u00f2\u0001"+
		"\u00f2\u0001\u00f3\u0001\u00f3\u0001\u00f3\u0001\u00f3\u0001\u00f3\u0003"+
		"\u00f3\u082a\b\u00f3\u0003\u00f3\u082c\b\u00f3\u0001\u00f3\u0001\u00f3"+
		"\u0001\u00f4\u0001\u00f4\u0003\u00f4\u0832\b\u00f4\u0001\u00f5\u0001\u00f5"+
		"\u0001\u00f5\u0001\u00f5\u0001\u00f5\u0001\u00f6\u0001\u00f6\u0001\u00f6"+
		"\u0001\u00f6\u0001\u00f6\u0001\u00f7\u0001\u00f7\u0001\u00f8\u0001\u00f8"+
		"\u0001\u00f9\u0001\u00f9\u0001\u00f9\u0001\u00f9\u0003\u00f9\u0846\b\u00f9"+
		"\u0001\u00f9\u0001\u00f9\u0001\u00f9\u0001\u00fa\u0001\u00fa\u0003\u00fa"+
		"\u084d\b\u00fa\u0001\u00fb\u0001\u00fb\u0001\u00fb\u0001\u00fb\u0001\u00fb"+
		"\u0001\u00fc\u0001\u00fc\u0001\u00fc\u0001\u00fd\u0001\u00fd\u0001\u00fd"+
		"\u0001\u00fe\u0001\u00fe\u0001\u00ff\u0005\u00ff\u085d\b\u00ff\n\u00ff"+
		"\f\u00ff\u0860\t\u00ff\u0001\u00ff\u0001\u00ff\u0001\u00ff\u0001\u00ff"+
		"\u0001\u00ff\u0001\u00ff\u0005\u00ff\u0868\b\u00ff\n\u00ff\f\u00ff\u086b"+
		"\t\u00ff\u0003\u00ff\u086d\b\u00ff\u0001\u00ff\u0001\u00ff\u0001\u0100"+
		"\u0001\u0100\u0001\u0100\u0001\u0100\u0003\u0100\u0875\b\u0100\u0003\u0100"+
		"\u0877\b\u0100\u0001\u0100\u0001\u0100\u0001\u0101\u0001\u0101\u0001\u0101"+
		"\u0005\u0101\u087e\b\u0101\n\u0101\f\u0101\u0881\t\u0101\u0001\u0101\u0003"+
		"\u0101\u0884\b\u0101\u0001\u0102\u0001\u0102\u0001\u0102\u0001\u0102\u0003"+
		"\u0102\u088a\b\u0102\u0001\u0103\u0001\u0103\u0001\u0104\u0003\u0104\u088f"+
		"\b\u0104\u0001\u0104\u0003\u0104\u0892\b\u0104\u0001\u0104\u0001\u0104"+
		"\u0003\u0104\u0896\b\u0104\u0001\u0104\u0003\u0104\u0899\b\u0104\u0001"+
		"\u0104\u0001\u0104\u0001\u0105\u0005\u0105\u089e\b\u0105\n\u0105\f\u0105"+
		"\u08a1\t\u0105\u0001\u0105\u0001\u0105\u0001\u0105\u0001\u0106\u0001\u0106"+
		"\u0001\u0106\u0005\u0106\u08a9\b\u0106\n\u0106\f\u0106\u08ac\t\u0106\u0001"+
		"\u0106\u0001\u0106\u0001\u0106\u0001\u0107\u0001\u0107\u0001\u0107\u0001"+
		"\u0108\u0001\u0108\u0001\u0108\u0001\u0108\u0005\u0108\u08b8\b\u0108\n"+
		"\u0108\f\u0108\u08bb\t\u0108\u0001\u0108\u0003\u0108\u08be\b\u0108\u0001"+
		"\u0109\u0001\u0109\u0001\u0109\u0001\u0109\u0001\u0109\u0001\u010a\u0001"+
		"\u010a\u0001\u010a\u0001\u010a\u0001\u010a\u0001\u010b\u0001\u010b\u0001"+
		"\u010b\u0005\u010b\u08cd\b\u010b\n\u010b\f\u010b\u08d0\t\u010b\u0001\u010b"+
		"\u0003\u010b\u08d3\b\u010b\u0001\u010c\u0001\u010c\u0001\u010c\u0003\u010c"+
		"\u08d8\b\u010c\u0001\u010d\u0001\u010d\u0003\u010d\u08dc\b\u010d\u0001"+
		"\u010d\u0001\u010d\u0001\u010d\u0001\u010e\u0001\u010e\u0001\u010e\u0001"+
		"\u010e\u0001\u010e\u0001\u010f\u0001\u010f\u0001\u010f\u0001\u010f\u0001"+
		"\u010f\u0001\u0110\u0001\u0110\u0001\u0110\u0001\u0110\u0001\u0110\u0001"+
		"\u0110\u0000\u0002\u0118\u0152\u0111\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF"+
		"HJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c"+
		"\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4"+
		"\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc"+
		"\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4"+
		"\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec"+
		"\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104"+
		"\u0106\u0108\u010a\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c"+
		"\u011e\u0120\u0122\u0124\u0126\u0128\u012a\u012c\u012e\u0130\u0132\u0134"+
		"\u0136\u0138\u013a\u013c\u013e\u0140\u0142\u0144\u0146\u0148\u014a\u014c"+
		"\u014e\u0150\u0152\u0154\u0156\u0158\u015a\u015c\u015e\u0160\u0162\u0164"+
		"\u0166\u0168\u016a\u016c\u016e\u0170\u0172\u0174\u0176\u0178\u017a\u017c"+
		"\u017e\u0180\u0182\u0184\u0186\u0188\u018a\u018c\u018e\u0190\u0192\u0194"+
		"\u0196\u0198\u019a\u019c\u019e\u01a0\u01a2\u01a4\u01a6\u01a8\u01aa\u01ac"+
		"\u01ae\u01b0\u01b2\u01b4\u01b6\u01b8\u01ba\u01bc\u01be\u01c0\u01c2\u01c4"+
		"\u01c6\u01c8\u01ca\u01cc\u01ce\u01d0\u01d2\u01d4\u01d6\u01d8\u01da\u01dc"+
		"\u01de\u01e0\u01e2\u01e4\u01e6\u01e8\u01ea\u01ec\u01ee\u01f0\u01f2\u01f4"+
		"\u01f6\u01f8\u01fa\u01fc\u01fe\u0200\u0202\u0204\u0206\u0208\u020a\u020c"+
		"\u020e\u0210\u0212\u0214\u0216\u0218\u021a\u021c\u021e\u0220\u0000\u000f"+
		"\u0002\u0000XXww\u0002\u0000^^ww\u0002\u0000RRll\u0002\u0000\u0016\u0016"+
		"zz\u0002\u0000PPmm\u0002\u0000\u0012\u0012\u0085\u0085\u0001\u0000MO\u0002"+
		"\u000012II\u0004\u0000\u0005\u0005//GG{{\u0006\u0000kkntyy\u0088\u008f"+
		"\u0093\u0093\u0095\u0095\u0003\u0000DD\u0085\u0085\u008d\u008e\u0003\u0000"+
		"--88\u0086\u0086\u0002\u0000##cc\u0001\u0000\u009c\u009d\u0002\u0000?"+
		"?\u0086\u0086\u0919\u0000\u0225\u0001\u0000\u0000\u0000\u0002\u0227\u0001"+
		"\u0000\u0000\u0000\u0004\u0229\u0001\u0000\u0000\u0000\u0006\u022b\u0001"+
		"\u0000\u0000\u0000\b\u022d\u0001\u0000\u0000\u0000\n\u022f\u0001\u0000"+
		"\u0000\u0000\f\u0231\u0001\u0000\u0000\u0000\u000e\u0236\u0001\u0000\u0000"+
		"\u0000\u0010\u0244\u0001\u0000\u0000\u0000\u0012\u0248\u0001\u0000\u0000"+
		"\u0000\u0014\u024c\u0001\u0000\u0000\u0000\u0016\u0251\u0001\u0000\u0000"+
		"\u0000\u0018\u026e\u0001\u0000\u0000\u0000\u001a\u0270\u0001\u0000\u0000"+
		"\u0000\u001c\u0276\u0001\u0000\u0000\u0000\u001e\u0285\u0001\u0000\u0000"+
		"\u0000 \u028d\u0001\u0000\u0000\u0000\"\u0297\u0001\u0000\u0000\u0000"+
		"$\u02a0\u0001\u0000\u0000\u0000&\u02a6\u0001\u0000\u0000\u0000(\u02ac"+
		"\u0001\u0000\u0000\u0000*\u02b8\u0001\u0000\u0000\u0000,\u02be\u0001\u0000"+
		"\u0000\u0000.\u02c1\u0001\u0000\u0000\u00000\u02c3\u0001\u0000\u0000\u0000"+
		"2\u02c7\u0001\u0000\u0000\u00004\u02c9\u0001\u0000\u0000\u00006\u02cc"+
		"\u0001\u0000\u0000\u00008\u02cf\u0001\u0000\u0000\u0000:\u02df\u0001\u0000"+
		"\u0000\u0000<\u02ea\u0001\u0000\u0000\u0000>\u02f4\u0001\u0000\u0000\u0000"+
		"@\u02f6\u0001\u0000\u0000\u0000B\u0303\u0001\u0000\u0000\u0000D\u030d"+
		"\u0001\u0000\u0000\u0000F\u0314\u0001\u0000\u0000\u0000H\u031a\u0001\u0000"+
		"\u0000\u0000J\u0339\u0001\u0000\u0000\u0000L\u0345\u0001\u0000\u0000\u0000"+
		"N\u0347\u0001\u0000\u0000\u0000P\u034d\u0001\u0000\u0000\u0000R\u0356"+
		"\u0001\u0000\u0000\u0000T\u0368\u0001\u0000\u0000\u0000V\u0371\u0001\u0000"+
		"\u0000\u0000X\u0378\u0001\u0000\u0000\u0000Z\u037a\u0001\u0000\u0000\u0000"+
		"\\\u0384\u0001\u0000\u0000\u0000^\u038d\u0001\u0000\u0000\u0000`\u0396"+
		"\u0001\u0000\u0000\u0000b\u03a8\u0001\u0000\u0000\u0000d\u03af\u0001\u0000"+
		"\u0000\u0000f\u03b3\u0001\u0000\u0000\u0000h\u03bb\u0001\u0000\u0000\u0000"+
		"j\u03bf\u0001\u0000\u0000\u0000l\u03c3\u0001\u0000\u0000\u0000n\u03c7"+
		"\u0001\u0000\u0000\u0000p\u03d7\u0001\u0000\u0000\u0000r\u03db\u0001\u0000"+
		"\u0000\u0000t\u03eb\u0001\u0000\u0000\u0000v\u03ef\u0001\u0000\u0000\u0000"+
		"x\u03f3\u0001\u0000\u0000\u0000z\u03f7\u0001\u0000\u0000\u0000|\u03fb"+
		"\u0001\u0000\u0000\u0000~\u03fd\u0001\u0000\u0000\u0000\u0080\u0401\u0001"+
		"\u0000\u0000\u0000\u0082\u0404\u0001\u0000\u0000\u0000\u0084\u040a\u0001"+
		"\u0000\u0000\u0000\u0086\u041a\u0001\u0000\u0000\u0000\u0088\u0425\u0001"+
		"\u0000\u0000\u0000\u008a\u042d\u0001\u0000\u0000\u0000\u008c\u043d\u0001"+
		"\u0000\u0000\u0000\u008e\u0445\u0001\u0000\u0000\u0000\u0090\u044a\u0001"+
		"\u0000\u0000\u0000\u0092\u045f\u0001\u0000\u0000\u0000\u0094\u0467\u0001"+
		"\u0000\u0000\u0000\u0096\u0469\u0001\u0000\u0000\u0000\u0098\u046c\u0001"+
		"\u0000\u0000\u0000\u009a\u046f\u0001\u0000\u0000\u0000\u009c\u0487\u0001"+
		"\u0000\u0000\u0000\u009e\u048d\u0001\u0000\u0000\u0000\u00a0\u048f\u0001"+
		"\u0000\u0000\u0000\u00a2\u0494\u0001\u0000\u0000\u0000\u00a4\u0498\u0001"+
		"\u0000\u0000\u0000\u00a6\u04b7\u0001\u0000\u0000\u0000\u00a8\u04c7\u0001"+
		"\u0000\u0000\u0000\u00aa\u04ce\u0001\u0000\u0000\u0000\u00ac\u04d0\u0001"+
		"\u0000\u0000\u0000\u00ae\u04d8\u0001\u0000\u0000\u0000\u00b0\u04db\u0001"+
		"\u0000\u0000\u0000\u00b2\u04e3\u0001\u0000\u0000\u0000\u00b4\u04e6\u0001"+
		"\u0000\u0000\u0000\u00b6\u04e9\u0001\u0000\u0000\u0000\u00b8\u04ec\u0001"+
		"\u0000\u0000\u0000\u00ba\u04f4\u0001\u0000\u0000\u0000\u00bc\u04f9\u0001"+
		"\u0000\u0000\u0000\u00be\u0501\u0001\u0000\u0000\u0000\u00c0\u0505\u0001"+
		"\u0000\u0000\u0000\u00c2\u050d\u0001\u0000\u0000\u0000\u00c4\u050f\u0001"+
		"\u0000\u0000\u0000\u00c6\u0511\u0001\u0000\u0000\u0000\u00c8\u0515\u0001"+
		"\u0000\u0000\u0000\u00ca\u051d\u0001\u0000\u0000\u0000\u00cc\u051f\u0001"+
		"\u0000\u0000\u0000\u00ce\u0521\u0001\u0000\u0000\u0000\u00d0\u0528\u0001"+
		"\u0000\u0000\u0000\u00d2\u052e\u0001\u0000\u0000\u0000\u00d4\u053e\u0001"+
		"\u0000\u0000\u0000\u00d6\u0540\u0001\u0000\u0000\u0000\u00d8\u054b\u0001"+
		"\u0000\u0000\u0000\u00da\u054d\u0001\u0000\u0000\u0000\u00dc\u0552\u0001"+
		"\u0000\u0000\u0000\u00de\u0557\u0001\u0000\u0000\u0000\u00e0\u055c\u0001"+
		"\u0000\u0000\u0000\u00e2\u0561\u0001\u0000\u0000\u0000\u00e4\u0567\u0001"+
		"\u0000\u0000\u0000\u00e6\u0576\u0001\u0000\u0000\u0000\u00e8\u0580\u0001"+
		"\u0000\u0000\u0000\u00ea\u0587\u0001\u0000\u0000\u0000\u00ec\u0596\u0001"+
		"\u0000\u0000\u0000\u00ee\u0599\u0001\u0000\u0000\u0000\u00f0\u05a1\u0001"+
		"\u0000\u0000\u0000\u00f2\u05a3\u0001\u0000\u0000\u0000\u00f4\u05a5\u0001"+
		"\u0000\u0000\u0000\u00f6\u05be\u0001\u0000\u0000\u0000\u00f8\u05c0\u0001"+
		"\u0000\u0000\u0000\u00fa\u05d9\u0001\u0000\u0000\u0000\u00fc\u05db\u0001"+
		"\u0000\u0000\u0000\u00fe\u05e0\u0001\u0000\u0000\u0000\u0100\u05ea\u0001"+
		"\u0000\u0000\u0000\u0102\u05f4\u0001\u0000\u0000\u0000\u0104\u05fa\u0001"+
		"\u0000\u0000\u0000\u0106\u0600\u0001\u0000\u0000\u0000\u0108\u0604\u0001"+
		"\u0000\u0000\u0000\u010a\u0607\u0001\u0000\u0000\u0000\u010c\u0611\u0001"+
		"\u0000\u0000\u0000\u010e\u0616\u0001\u0000\u0000\u0000\u0110\u0620\u0001"+
		"\u0000\u0000\u0000\u0112\u062e\u0001\u0000\u0000\u0000\u0114\u0630\u0001"+
		"\u0000\u0000\u0000\u0116\u0632\u0001\u0000\u0000\u0000\u0118\u0656\u0001"+
		"\u0000\u0000\u0000\u011a\u066f\u0001\u0000\u0000\u0000\u011c\u0671\u0001"+
		"\u0000\u0000\u0000\u011e\u0676\u0001\u0000\u0000\u0000\u0120\u0678\u0001"+
		"\u0000\u0000\u0000\u0122\u067c\u0001\u0000\u0000\u0000\u0124\u067f\u0001"+
		"\u0000\u0000\u0000\u0126\u068b\u0001\u0000\u0000\u0000\u0128\u068d\u0001"+
		"\u0000\u0000\u0000\u012a\u068f\u0001\u0000\u0000\u0000\u012c\u069a\u0001"+
		"\u0000\u0000\u0000\u012e\u069c\u0001\u0000\u0000\u0000\u0130\u069e\u0001"+
		"\u0000\u0000\u0000\u0132\u06a0\u0001\u0000\u0000\u0000\u0134\u06a2\u0001"+
		"\u0000\u0000\u0000\u0136\u06a4\u0001\u0000\u0000\u0000\u0138\u06a6\u0001"+
		"\u0000\u0000\u0000\u013a\u06a8\u0001\u0000\u0000\u0000\u013c\u06aa\u0001"+
		"\u0000\u0000\u0000\u013e\u06ac\u0001\u0000\u0000\u0000\u0140\u06ae\u0001"+
		"\u0000\u0000\u0000\u0142\u06b1\u0001\u0000\u0000\u0000\u0144\u06b3\u0001"+
		"\u0000\u0000\u0000\u0146\u06b6\u0001\u0000\u0000\u0000\u0148\u06b8\u0001"+
		"\u0000\u0000\u0000\u014a\u06ba\u0001\u0000\u0000\u0000\u014c\u06bc\u0001"+
		"\u0000\u0000\u0000\u014e\u06be\u0001\u0000\u0000\u0000\u0150\u06c0\u0001"+
		"\u0000\u0000\u0000\u0152\u06c8\u0001\u0000\u0000\u0000\u0154\u06ed\u0001"+
		"\u0000\u0000\u0000\u0156\u06ef\u0001\u0000\u0000\u0000\u0158\u06f1\u0001"+
		"\u0000\u0000\u0000\u015a\u06fa\u0001\u0000\u0000\u0000\u015c\u06fc\u0001"+
		"\u0000\u0000\u0000\u015e\u06fe\u0001\u0000\u0000\u0000\u0160\u0700\u0001"+
		"\u0000\u0000\u0000\u0162\u0702\u0001\u0000\u0000\u0000\u0164\u0707\u0001"+
		"\u0000\u0000\u0000\u0166\u070d\u0001\u0000\u0000\u0000\u0168\u0716\u0001"+
		"\u0000\u0000\u0000\u016a\u0718\u0001\u0000\u0000\u0000\u016c\u071c\u0001"+
		"\u0000\u0000\u0000\u016e\u071e\u0001\u0000\u0000\u0000\u0170\u0722\u0001"+
		"\u0000\u0000\u0000\u0172\u0726\u0001\u0000\u0000\u0000\u0174\u072a\u0001"+
		"\u0000\u0000\u0000\u0176\u0734\u0001\u0000\u0000\u0000\u0178\u0736\u0001"+
		"\u0000\u0000\u0000\u017a\u0738\u0001\u0000\u0000\u0000\u017c\u073a\u0001"+
		"\u0000\u0000\u0000\u017e\u073c\u0001\u0000\u0000\u0000\u0180\u073e\u0001"+
		"\u0000\u0000\u0000\u0182\u0740\u0001\u0000\u0000\u0000\u0184\u0742\u0001"+
		"\u0000\u0000\u0000\u0186\u0744\u0001\u0000\u0000\u0000\u0188\u0748\u0001"+
		"\u0000\u0000\u0000\u018a\u074a\u0001\u0000\u0000\u0000\u018c\u074c\u0001"+
		"\u0000\u0000\u0000\u018e\u0755\u0001\u0000\u0000\u0000\u0190\u075a\u0001"+
		"\u0000\u0000\u0000\u0192\u075c\u0001\u0000\u0000\u0000\u0194\u075f\u0001"+
		"\u0000\u0000\u0000\u0196\u0761\u0001\u0000\u0000\u0000\u0198\u0763\u0001"+
		"\u0000\u0000\u0000\u019a\u0767\u0001\u0000\u0000\u0000\u019c\u0769\u0001"+
		"\u0000\u0000\u0000\u019e\u076d\u0001\u0000\u0000\u0000\u01a0\u0771\u0001"+
		"\u0000\u0000\u0000\u01a2\u0773\u0001\u0000\u0000\u0000\u01a4\u0777\u0001"+
		"\u0000\u0000\u0000\u01a6\u0779\u0001\u0000\u0000\u0000\u01a8\u077b\u0001"+
		"\u0000\u0000\u0000\u01aa\u077d\u0001\u0000\u0000\u0000\u01ac\u0784\u0001"+
		"\u0000\u0000\u0000\u01ae\u078c\u0001\u0000\u0000\u0000\u01b0\u0794\u0001"+
		"\u0000\u0000\u0000\u01b2\u0796\u0001\u0000\u0000\u0000\u01b4\u079a\u0001"+
		"\u0000\u0000\u0000\u01b6\u079c\u0001\u0000\u0000\u0000\u01b8\u079e\u0001"+
		"\u0000\u0000\u0000\u01ba\u07a0\u0001\u0000\u0000\u0000\u01bc\u07a9\u0001"+
		"\u0000\u0000\u0000\u01be\u07ab\u0001\u0000\u0000\u0000\u01c0\u07ad\u0001"+
		"\u0000\u0000\u0000\u01c2\u07af\u0001\u0000\u0000\u0000\u01c4\u07b1\u0001"+
		"\u0000\u0000\u0000\u01c6\u07b3\u0001\u0000\u0000\u0000\u01c8\u07bb\u0001"+
		"\u0000\u0000\u0000\u01ca\u07bd\u0001\u0000\u0000\u0000\u01cc\u07bf\u0001"+
		"\u0000\u0000\u0000\u01ce\u07c4\u0001\u0000\u0000\u0000\u01d0\u07c9\u0001"+
		"\u0000\u0000\u0000\u01d2\u07e5\u0001\u0000\u0000\u0000\u01d4\u07e7\u0001"+
		"\u0000\u0000\u0000\u01d6\u07fb\u0001\u0000\u0000\u0000\u01d8\u0811\u0001"+
		"\u0000\u0000\u0000\u01da\u0813\u0001\u0000\u0000\u0000\u01dc\u0818\u0001"+
		"\u0000\u0000\u0000\u01de\u081c\u0001\u0000\u0000\u0000\u01e0\u081e\u0001"+
		"\u0000\u0000\u0000\u01e2\u0820\u0001\u0000\u0000\u0000\u01e4\u0822\u0001"+
		"\u0000\u0000\u0000\u01e6\u082b\u0001\u0000\u0000\u0000\u01e8\u0831\u0001"+
		"\u0000\u0000\u0000\u01ea\u0833\u0001\u0000\u0000\u0000\u01ec\u0838\u0001"+
		"\u0000\u0000\u0000\u01ee\u083d\u0001\u0000\u0000\u0000\u01f0\u083f\u0001"+
		"\u0000\u0000\u0000\u01f2\u0841\u0001\u0000\u0000\u0000\u01f4\u084c\u0001"+
		"\u0000\u0000\u0000\u01f6\u084e\u0001\u0000\u0000\u0000\u01f8\u0853\u0001"+
		"\u0000\u0000\u0000\u01fa\u0856\u0001\u0000\u0000\u0000\u01fc\u0859\u0001"+
		"\u0000\u0000\u0000\u01fe\u085e\u0001\u0000\u0000\u0000\u0200\u0876\u0001"+
		"\u0000\u0000\u0000\u0202\u0883\u0001\u0000\u0000\u0000\u0204\u0889\u0001"+
		"\u0000\u0000\u0000\u0206\u088b\u0001\u0000\u0000\u0000\u0208\u088e\u0001"+
		"\u0000\u0000\u0000\u020a\u089f\u0001\u0000\u0000\u0000\u020c\u08a5\u0001"+
		"\u0000\u0000\u0000\u020e\u08b0\u0001\u0000\u0000\u0000\u0210\u08bd\u0001"+
		"\u0000\u0000\u0000\u0212\u08bf\u0001\u0000\u0000\u0000\u0214\u08c4\u0001"+
		"\u0000\u0000\u0000\u0216\u08d2\u0001\u0000\u0000\u0000\u0218\u08d7\u0001"+
		"\u0000\u0000\u0000\u021a\u08d9\u0001\u0000\u0000\u0000\u021c\u08e0\u0001"+
		"\u0000\u0000\u0000\u021e\u08e5\u0001\u0000\u0000\u0000\u0220\u08ea\u0001"+
		"\u0000\u0000\u0000\u0222\u0226\u0005\u0092\u0000\u0000\u0223\u0224\u0005"+
		"e\u0000\u0000\u0224\u0226\u0005\u000b\u0000\u0000\u0225\u0222\u0001\u0000"+
		"\u0000\u0000\u0225\u0223\u0001\u0000\u0000\u0000\u0226\u0001\u0001\u0000"+
		"\u0000\u0000\u0227\u0228\u0007\u0000\u0000\u0000\u0228\u0003\u0001\u0000"+
		"\u0000\u0000\u0229\u022a\u0007\u0001\u0000\u0000\u022a\u0005\u0001\u0000"+
		"\u0000\u0000\u022b\u022c\u0007\u0002\u0000\u0000\u022c\u0007\u0001\u0000"+
		"\u0000\u0000\u022d\u022e\u0007\u0003\u0000\u0000\u022e\t\u0001\u0000\u0000"+
		"\u0000\u022f\u0230\u0007\u0004\u0000\u0000\u0230\u000b\u0001\u0000\u0000"+
		"\u0000\u0231\u0232\u0007\u0005\u0000\u0000\u0232\r\u0001\u0000\u0000\u0000"+
		"\u0233\u0234\u0005\u0093\u0000\u0000\u0234\u0235\u0005\u009f\u0000\u0000"+
		"\u0235\u0237\u0005\u0095\u0000\u0000\u0236\u0233\u0001\u0000\u0000\u0000"+
		"\u0236\u0237\u0001\u0000\u0000\u0000\u0237\u0239\u0001\u0000\u0000\u0000"+
		"\u0238\u023a\u0005\u009f\u0000\u0000\u0239\u0238\u0001\u0000\u0000\u0000"+
		"\u0239\u023a\u0001\u0000\u0000\u0000\u023a\u000f\u0001\u0000\u0000\u0000"+
		"\u023b\u0245\u0005\u0083\u0000\u0000\u023c\u0240\u0005\u007f\u0000\u0000"+
		"\u023d\u023f\u0003\u0012\t\u0000\u023e\u023d\u0001\u0000\u0000\u0000\u023f"+
		"\u0242\u0001\u0000\u0000\u0000\u0240\u023e\u0001\u0000\u0000\u0000\u0240"+
		"\u0241\u0001\u0000\u0000\u0000\u0241\u0243\u0001\u0000\u0000\u0000\u0242"+
		"\u0240\u0001\u0000\u0000\u0000\u0243\u0245\u0005\u0080\u0000\u0000\u0244"+
		"\u023b\u0001\u0000\u0000\u0000\u0244\u023c\u0001\u0000\u0000\u0000\u0245"+
		"\u0011\u0001\u0000\u0000\u0000\u0246\u0249\u0003\u0014\n\u0000\u0247\u0249"+
		"\u0003\u001a\r\u0000\u0248\u0246\u0001\u0000\u0000\u0000\u0248\u0247\u0001"+
		"\u0000\u0000\u0000\u0249\u0013\u0001\u0000\u0000\u0000\u024a\u024d\u0003"+
		"J%\u0000\u024b\u024d\u0003L&\u0000\u024c\u024a\u0001\u0000\u0000\u0000"+
		"\u024c\u024b\u0001\u0000\u0000\u0000\u024d\u0015\u0001\u0000\u0000\u0000"+
		"\u024e\u0250\u0003\u01f8\u00fc\u0000\u024f\u024e\u0001\u0000\u0000\u0000"+
		"\u0250\u0253\u0001\u0000\u0000\u0000\u0251\u024f\u0001\u0000\u0000\u0000"+
		"\u0251\u0252\u0001\u0000\u0000\u0000\u0252\u0254\u0001\u0000\u0000\u0000"+
		"\u0253\u0251\u0001\u0000\u0000\u0000\u0254\u0259\u0005\u0019\u0000\u0000"+
		"\u0255\u0257\u0003\u000e\u0007\u0000\u0256\u0255\u0001\u0000\u0000\u0000"+
		"\u0256\u0257\u0001\u0000\u0000\u0000\u0257\u0258\u0001\u0000\u0000\u0000"+
		"\u0258\u025a\u0005+\u0000\u0000\u0259\u0256\u0001\u0000\u0000\u0000\u0259"+
		"\u025a\u0001\u0000\u0000\u0000\u025a\u025b\u0001\u0000\u0000\u0000\u025b"+
		"\u0260\u0003:\u001d\u0000\u025c\u025d\u0005\u0084\u0000\u0000\u025d\u025f"+
		"\u0003:\u001d\u0000\u025e\u025c\u0001\u0000\u0000\u0000\u025f\u0262\u0001"+
		"\u0000\u0000\u0000\u0260\u025e\u0001\u0000\u0000\u0000\u0260\u0261\u0001"+
		"\u0000\u0000\u0000\u0261\u0263\u0001\u0000\u0000\u0000\u0262\u0260\u0001"+
		"\u0000\u0000\u0000\u0263\u0264\u0005b\u0000\u0000\u0264\u0269\u0003:\u001d"+
		"\u0000\u0265\u0266\u0005\u0084\u0000\u0000\u0266\u0268\u0003:\u001d\u0000"+
		"\u0267\u0265\u0001\u0000\u0000\u0000\u0268\u026b\u0001\u0000\u0000\u0000"+
		"\u0269\u0267\u0001\u0000\u0000\u0000\u0269\u026a\u0001\u0000\u0000\u0000"+
		"\u026a\u026c\u0001\u0000\u0000\u0000\u026b\u0269\u0001\u0000\u0000\u0000"+
		"\u026c\u026d\u0003\u0010\b\u0000\u026d\u0017\u0001\u0000\u0000\u0000\u026e"+
		"\u026f\u0003:\u001d\u0000\u026f\u0019\u0001\u0000\u0000\u0000\u0270\u0271"+
		"\u0003\u001c\u000e\u0000\u0271\u001b\u0001\u0000\u0000\u0000\u0272\u0277"+
		"\u0003\u001e\u000f\u0000\u0273\u0277\u0003 \u0010\u0000\u0274\u0277\u0003"+
		"\"\u0011\u0000\u0275\u0277\u0003\u01fe\u00ff\u0000\u0276\u0272\u0001\u0000"+
		"\u0000\u0000\u0276\u0273\u0001\u0000\u0000\u0000\u0276\u0274\u0001\u0000"+
		"\u0000\u0000\u0276\u0275\u0001\u0000\u0000\u0000\u0277\u001d\u0001\u0000"+
		"\u0000\u0000\u0278\u0279\u0005\u000f\u0000\u0000\u0279\u0283\u0003\u000e"+
		"\u0007\u0000\u027a\u027b\u0005\u0001\u0000\u0000\u027b\u0280\u0003\u0018"+
		"\f\u0000\u027c\u027d\u0005\u0084\u0000\u0000\u027d\u027f\u0003\u0018\f"+
		"\u0000\u027e\u027c\u0001\u0000\u0000\u0000\u027f\u0282\u0001\u0000\u0000"+
		"\u0000\u0280\u027e\u0001\u0000\u0000\u0000\u0280\u0281\u0001\u0000\u0000"+
		"\u0000\u0281\u0284\u0001\u0000\u0000\u0000\u0282\u0280\u0001\u0000\u0000"+
		"\u0000\u0283\u027a\u0001\u0000\u0000\u0000\u0283\u0284\u0001\u0000\u0000"+
		"\u0000\u0284\u0286\u0001\u0000\u0000\u0000\u0285\u0278\u0001\u0000\u0000"+
		"\u0000\u0285\u0286\u0001\u0000\u0000\u0000\u0286\u0289\u0001\u0000\u0000"+
		"\u0000\u0287\u0288\u0005;\u0000\u0000\u0288\u028a\u0005\u009e\u0000\u0000"+
		"\u0289\u0287\u0001\u0000\u0000\u0000\u0289\u028a\u0001\u0000\u0000\u0000"+
		"\u028a\u028b\u0001\u0000\u0000\u0000\u028b\u028c\u0005\u009a\u0000\u0000"+
		"\u028c\u001f\u0001\u0000\u0000\u0000\u028d\u028e\u0005\u001e\u0000\u0000"+
		"\u028e\u0291\u0003\u000e\u0007\u0000\u028f\u0290\u0005;\u0000\u0000\u0290"+
		"\u0292\u0005\u009e\u0000\u0000\u0291\u028f\u0001\u0000\u0000\u0000\u0291"+
		"\u0292\u0001\u0000\u0000\u0000\u0292\u0293\u0001\u0000\u0000\u0000\u0293"+
		"\u0294\u0005\u009a\u0000\u0000\u0294!\u0001\u0000\u0000\u0000\u0295\u0296"+
		"\u0005U\u0000\u0000\u0296\u0298\u0003\u000e\u0007\u0000\u0297\u0295\u0001"+
		"\u0000\u0000\u0000\u0297\u0298\u0001\u0000\u0000\u0000\u0298\u0299\u0001"+
		"\u0000\u0000\u0000\u0299\u029a\u00059\u0000\u0000\u029a\u029b\u0005\u009e"+
		"\u0000\u0000\u029b\u029c\u0005\u009a\u0000\u0000\u029c#\u0001\u0000\u0000"+
		"\u0000\u029d\u029f\u0003,\u0016\u0000\u029e\u029d\u0001\u0000\u0000\u0000"+
		"\u029f\u02a2\u0001\u0000\u0000\u0000\u02a0\u029e\u0001\u0000\u0000\u0000"+
		"\u02a0\u02a1\u0001\u0000\u0000\u0000\u02a1%\u0001\u0000\u0000\u0000\u02a2"+
		"\u02a0\u0001\u0000\u0000\u0000\u02a3\u02a5\u0003\u01fa\u00fd\u0000\u02a4"+
		"\u02a3\u0001\u0000\u0000\u0000\u02a5\u02a8\u0001\u0000\u0000\u0000\u02a6"+
		"\u02a4\u0001\u0000\u0000\u0000\u02a6\u02a7\u0001\u0000\u0000\u0000\u02a7"+
		"\u02a9\u0001\u0000\u0000\u0000\u02a8\u02a6\u0001\u0000\u0000\u0000\u02a9"+
		"\u02aa\u0003(\u0014\u0000\u02aa\u02ab\u0003*\u0015\u0000\u02ab\'\u0001"+
		"\u0000\u0000\u0000\u02ac\u02ad\u0005A\u0000\u0000\u02ad\u02ae\u0003\u000e"+
		"\u0007\u0000\u02ae)\u0001\u0000\u0000\u0000\u02af\u02b9\u0005\u0083\u0000"+
		"\u0000\u02b0\u02b4\u0005\u007f\u0000\u0000\u02b1\u02b3\u0003,\u0016\u0000"+
		"\u02b2\u02b1\u0001\u0000\u0000\u0000\u02b3\u02b6\u0001\u0000\u0000\u0000"+
		"\u02b4\u02b2\u0001\u0000\u0000\u0000\u02b4\u02b5\u0001\u0000\u0000\u0000"+
		"\u02b5\u02b7\u0001\u0000\u0000\u0000\u02b6\u02b4\u0001\u0000\u0000\u0000"+
		"\u02b7\u02b9\u0005\u0080\u0000\u0000\u02b8\u02af\u0001\u0000\u0000\u0000"+
		"\u02b8\u02b0\u0001\u0000\u0000\u0000\u02b9+\u0001\u0000\u0000\u0000\u02ba"+
		"\u02bf\u00032\u0019\u0000\u02bb\u02bf\u00038\u001c\u0000\u02bc\u02bf\u0003"+
		"<\u001e\u0000\u02bd\u02bf\u0005\u009a\u0000\u0000\u02be\u02ba\u0001\u0000"+
		"\u0000\u0000\u02be\u02bb\u0001\u0000\u0000\u0000\u02be\u02bc\u0001\u0000"+
		"\u0000\u0000\u02be\u02bd\u0001\u0000\u0000\u0000\u02bf-\u0001\u0000\u0000"+
		"\u0000\u02c0\u02c2\u00030\u0018\u0000\u02c1\u02c0\u0001\u0000\u0000\u0000"+
		"\u02c1\u02c2\u0001\u0000\u0000\u0000\u02c2/\u0001\u0000\u0000\u0000\u02c3"+
		"\u02c4\u0007\u0006\u0000\u0000\u02c41\u0001\u0000\u0000\u0000\u02c5\u02c8"+
		"\u00034\u001a\u0000\u02c6\u02c8\u00036\u001b\u0000\u02c7\u02c5\u0001\u0000"+
		"\u0000\u0000\u02c7\u02c6\u0001\u0000\u0000\u0000\u02c83\u0001\u0000\u0000"+
		"\u0000\u02c9\u02ca\u0003.\u0017\u0000\u02ca\u02cb\u0003H$\u0000\u02cb"+
		"5\u0001\u0000\u0000\u0000\u02cc\u02cd\u0003.\u0017\u0000\u02cd\u02ce\u0003"+
		"L&\u0000\u02ce7\u0001\u0000\u0000\u0000\u02cf\u02d0\u0003.\u0017\u0000"+
		"\u02d0\u02d4\u0005\u0003\u0000\u0000\u02d1\u02d2\u0005\u0093\u0000\u0000"+
		"\u02d2\u02d3\u0005\u009f\u0000\u0000\u02d3\u02d5\u0005\u0095\u0000\u0000"+
		"\u02d4\u02d1\u0001\u0000\u0000\u0000\u02d4\u02d5\u0001\u0000\u0000\u0000"+
		"\u02d5\u02d7\u0001\u0000\u0000\u0000\u02d6\u02d8\u0005\u009f\u0000\u0000"+
		"\u02d7\u02d6\u0001\u0000\u0000\u0000\u02d7\u02d8\u0001\u0000\u0000\u0000"+
		"\u02d8\u02d9\u0001\u0000\u0000\u0000\u02d9\u02da\u0005*\u0000\u0000\u02da"+
		"\u02db\u0003:\u001d\u0000\u02db\u02dc\u0003\u0010\b\u0000\u02dc9\u0001"+
		"\u0000\u0000\u0000\u02dd\u02de\u0005\u0090\u0000\u0000\u02de\u02e0\u0005"+
		"v\u0000\u0000\u02df\u02dd\u0001\u0000\u0000\u0000\u02df\u02e0\u0001\u0000"+
		"\u0000\u0000\u02e0\u02e5\u0001\u0000\u0000\u0000\u02e1\u02e2\u0005\u009f"+
		"\u0000\u0000\u02e2\u02e4\u0005v\u0000\u0000\u02e3\u02e1\u0001\u0000\u0000"+
		"\u0000\u02e4\u02e7\u0001\u0000\u0000\u0000\u02e5\u02e3\u0001\u0000\u0000"+
		"\u0000\u02e5\u02e6\u0001\u0000\u0000\u0000\u02e6\u02e8\u0001\u0000\u0000"+
		"\u0000\u02e7\u02e5\u0001\u0000\u0000\u0000\u02e8\u02e9\u0005\u009f\u0000"+
		"\u0000\u02e9;\u0001\u0000\u0000\u0000\u02ea\u02eb\u00030\u0018\u0000\u02eb"+
		"\u02ed\u00050\u0000\u0000\u02ec\u02ee\u0005\u0004\u0000\u0000\u02ed\u02ec"+
		"\u0001\u0000\u0000\u0000\u02ed\u02ee\u0001\u0000\u0000\u0000\u02ee\u02ef"+
		"\u0001\u0000\u0000\u0000\u02ef\u02f0\u0003>\u001f\u0000\u02f0\u02f1\u0003"+
		"\u0010\b\u0000\u02f1=\u0001\u0000\u0000\u0000\u02f2\u02f5\u0003@ \u0000"+
		"\u02f3\u02f5\u0003B!\u0000\u02f4\u02f2\u0001\u0000\u0000\u0000\u02f4\u02f3"+
		"\u0001\u0000\u0000\u0000\u02f5?\u0001\u0000\u0000\u0000\u02f6\u02f9\u0003"+
		":\u001d\u0000\u02f7\u02f8\u0005v\u0000\u0000\u02f8\u02fa\u0005p\u0000"+
		"\u0000\u02f9\u02f7\u0001\u0000\u0000\u0000\u02f9\u02fa\u0001\u0000\u0000"+
		"\u0000\u02faA\u0001\u0000\u0000\u0000\u02fb\u02fc\u0003:\u001d\u0000\u02fc"+
		"\u02fd\u0005v\u0000\u0000\u02fd\u0300\u0005\u008c\u0000\u0000\u02fe\u02ff"+
		"\u0005v\u0000\u0000\u02ff\u0301\u0005p\u0000\u0000\u0300\u02fe\u0001\u0000"+
		"\u0000\u0000\u0300\u0301\u0001\u0000\u0000\u0000\u0301\u0304\u0001\u0000"+
		"\u0000\u0000\u0302\u0304\u0003D\"\u0000\u0303\u02fb\u0001\u0000\u0000"+
		"\u0000\u0303\u0302\u0001\u0000\u0000\u0000\u0304C\u0001\u0000\u0000\u0000"+
		"\u0305\u030e\u0003@ \u0000\u0306\u0307\u0003:\u001d\u0000\u0307\u0308"+
		"\u0005v\u0000\u0000\u0308\u030b\u0005\u008c\u0000\u0000\u0309\u030a\u0005"+
		"v\u0000\u0000\u030a\u030c\u0005p\u0000\u0000\u030b\u0309\u0001\u0000\u0000"+
		"\u0000\u030b\u030c\u0001\u0000\u0000\u0000\u030c\u030e\u0001\u0000\u0000"+
		"\u0000\u030d\u0305\u0001\u0000\u0000\u0000\u030d\u0306\u0001\u0000\u0000"+
		"\u0000\u030e\u0310\u0001\u0000\u0000\u0000\u030f\u0311\u0003F#\u0000\u0310"+
		"\u030f\u0001\u0000\u0000\u0000\u0311\u0312\u0001\u0000\u0000\u0000\u0312"+
		"\u0310\u0001\u0000\u0000\u0000\u0312\u0313\u0001\u0000\u0000\u0000\u0313"+
		"E\u0001\u0000\u0000\u0000\u0314\u0315\u0005\u0081\u0000\u0000\u0315\u0316"+
		"\u0003\u0118\u008c\u0000\u0316\u0317\u0005\u0082\u0000\u0000\u0317G\u0001"+
		"\u0000\u0000\u0000\u0318\u031b\u0003\u001c\u000e\u0000\u0319\u031b\u0003"+
		"J%\u0000\u031a\u0318\u0001\u0000\u0000\u0000\u031a\u0319\u0001\u0000\u0000"+
		"\u0000\u031bI\u0001\u0000\u0000\u0000\u031c\u033a\u0003\u0016\u000b\u0000"+
		"\u031d\u033a\u0003&\u0013\u0000\u031e\u033a\u0003N\'\u0000\u031f\u033a"+
		"\u0003\u0082A\u0000\u0320\u033a\u0003\u00dam\u0000\u0321\u033a\u0003\u00dc"+
		"n\u0000\u0322\u033a\u0003\u00deo\u0000\u0323\u033a\u0003\u01f6\u00fb\u0000"+
		"\u0324\u033a\u0003\u00e0p\u0000\u0325\u033a\u0003\u00e2q\u0000\u0326\u033a"+
		"\u0003\u01cc\u00e6\u0000\u0327\u033a\u0003\u00fc~\u0000\u0328\u033a\u0003"+
		"\u0100\u0080\u0000\u0329\u033a\u0003\u010c\u0086\u0000\u032a\u033a\u0003"+
		"\u01e8\u00f4\u0000\u032b\u033a\u0003\u020a\u0105\u0000\u032c\u033a\u0003"+
		"\u020c\u0106\u0000\u032d\u033a\u0003\u021e\u010f\u0000\u032e\u033a\u0003"+
		"f3\u0000\u032f\u033a\u0003n7\u0000\u0330\u033a\u0003\u0088D\u0000\u0331"+
		"\u033a\u0003r9\u0000\u0332\u033a\u0003\u00d2i\u0000\u0333\u033a\u0003"+
		"\u00bc^\u0000\u0334\u033a\u0003\u00c0`\u0000\u0335\u033a\u0003\u00c8d"+
		"\u0000\u0336\u033a\u0003\u00d6k\u0000\u0337\u033a\u0003\u0214\u010a\u0000"+
		"\u0338\u033a\u0003\u0220\u0110\u0000\u0339\u031c\u0001\u0000\u0000\u0000"+
		"\u0339\u031d\u0001\u0000\u0000\u0000\u0339\u031e\u0001\u0000\u0000\u0000"+
		"\u0339\u031f\u0001\u0000\u0000\u0000\u0339\u0320\u0001\u0000\u0000\u0000"+
		"\u0339\u0321\u0001\u0000\u0000\u0000\u0339\u0322\u0001\u0000\u0000\u0000"+
		"\u0339\u0323\u0001\u0000\u0000\u0000\u0339\u0324\u0001\u0000\u0000\u0000"+
		"\u0339\u0325\u0001\u0000\u0000\u0000\u0339\u0326\u0001\u0000\u0000\u0000"+
		"\u0339\u0327\u0001\u0000\u0000\u0000\u0339\u0328\u0001\u0000\u0000\u0000"+
		"\u0339\u0329\u0001\u0000\u0000\u0000\u0339\u032a\u0001\u0000\u0000\u0000"+
		"\u0339\u032b\u0001\u0000\u0000\u0000\u0339\u032c\u0001\u0000\u0000\u0000"+
		"\u0339\u032d\u0001\u0000\u0000\u0000\u0339\u032e\u0001\u0000\u0000\u0000"+
		"\u0339\u032f\u0001\u0000\u0000\u0000\u0339\u0330\u0001\u0000\u0000\u0000"+
		"\u0339\u0331\u0001\u0000\u0000\u0000\u0339\u0332\u0001\u0000\u0000\u0000"+
		"\u0339\u0333\u0001\u0000\u0000\u0000\u0339\u0334\u0001\u0000\u0000\u0000"+
		"\u0339\u0335\u0001\u0000\u0000\u0000\u0339\u0336\u0001\u0000\u0000\u0000"+
		"\u0339\u0337\u0001\u0000\u0000\u0000\u0339\u0338\u0001\u0000\u0000\u0000"+
		"\u033aK\u0001\u0000\u0000\u0000\u033b\u0346\u0003\u008cF\u0000\u033c\u0346"+
		"\u0003\u00fe\u007f\u0000\u033d\u0346\u0003\u010a\u0085\u0000\u033e\u0346"+
		"\u0003\u010e\u0087\u0000\u033f\u0346\u0003\u0110\u0088\u0000\u0340\u0346"+
		"\u0003\u00e4r\u0000\u0341\u0346\u0003\u00f4z\u0000\u0342\u0346\u0003\u00f8"+
		"|\u0000\u0343\u0346\u0003\u01ce\u00e7\u0000\u0344\u0346\u0003\u01d0\u00e8"+
		"\u0000\u0345\u033b\u0001\u0000\u0000\u0000\u0345\u033c\u0001\u0000\u0000"+
		"\u0000\u0345\u033d\u0001\u0000\u0000\u0000\u0345\u033e\u0001\u0000\u0000"+
		"\u0000\u0345\u033f\u0001\u0000\u0000\u0000\u0345\u0340\u0001\u0000\u0000"+
		"\u0000\u0345\u0341\u0001\u0000\u0000\u0000\u0345\u0342\u0001\u0000\u0000"+
		"\u0000\u0345\u0343\u0001\u0000\u0000\u0000\u0345\u0344\u0001\u0000\u0000"+
		"\u0000\u0346M\u0001\u0000\u0000\u0000\u0347\u0348\u0003P(\u0000\u0348"+
		"\u0349\u0005d\u0000\u0000\u0349\u034a\u0003R)\u0000\u034a\u034b\u0003"+
		"b1\u0000\u034bO\u0001\u0000\u0000\u0000\u034c\u034e\u0005\u0002\u0000"+
		"\u0000\u034d\u034c\u0001\u0000\u0000\u0000\u034d\u034e\u0001\u0000\u0000"+
		"\u0000\u034e\u0352\u0001\u0000\u0000\u0000\u034f\u0351\u0003\u01fa\u00fd"+
		"\u0000\u0350\u034f\u0001\u0000\u0000\u0000\u0351\u0354\u0001\u0000\u0000"+
		"\u0000\u0352\u0350\u0001\u0000\u0000\u0000\u0352\u0353\u0001\u0000\u0000"+
		"\u0000\u0353Q\u0001\u0000\u0000\u0000\u0354\u0352\u0001\u0000\u0000\u0000"+
		"\u0355\u0357\u0005\u0004\u0000\u0000\u0356\u0355\u0001\u0000\u0000\u0000"+
		"\u0356\u0357\u0001\u0000\u0000\u0000\u0357\u0358\u0001\u0000\u0000\u0000"+
		"\u0358\u035a\u0003\u000e\u0007\u0000\u0359\u035b\u0003\u01ee\u00f7\u0000"+
		"\u035a\u0359\u0001\u0000\u0000\u0000\u035a\u035b\u0001\u0000\u0000\u0000"+
		"\u035b\u035e\u0001\u0000\u0000\u0000\u035c\u035f\u0003T*\u0000\u035d\u035f"+
		"\u0003V+\u0000\u035e\u035c\u0001\u0000\u0000\u0000\u035e\u035d\u0001\u0000"+
		"\u0000\u0000\u035f\u0360\u0001\u0000\u0000\u0000\u0360\u035e\u0001\u0000"+
		"\u0000\u0000\u0360\u0361\u0001\u0000\u0000\u0000\u0361\u0365\u0001\u0000"+
		"\u0000\u0000\u0362\u0364\u0003X,\u0000\u0363\u0362\u0001\u0000\u0000\u0000"+
		"\u0364\u0367\u0001\u0000\u0000\u0000\u0365\u0363\u0001\u0000\u0000\u0000"+
		"\u0365\u0366\u0001\u0000\u0000\u0000\u0366S\u0001\u0000\u0000\u0000\u0367"+
		"\u0365\u0001\u0000\u0000\u0000\u0368\u0369\u0003\u0002\u0001\u0000\u0369"+
		"\u036e\u0003h4\u0000\u036a\u036b\u0005\u0084\u0000\u0000\u036b\u036d\u0003"+
		"h4\u0000\u036c\u036a\u0001\u0000\u0000\u0000\u036d\u0370\u0001\u0000\u0000"+
		"\u0000\u036e\u036c\u0001\u0000\u0000\u0000\u036e\u036f\u0001\u0000\u0000"+
		"\u0000\u036fU\u0001\u0000\u0000\u0000\u0370\u036e\u0001\u0000\u0000\u0000"+
		"\u0371\u0372\u0003\f\u0006\u0000\u0372\u0373\u0003p8\u0000\u0373W\u0001"+
		"\u0000\u0000\u0000\u0374\u0379\u0003Z-\u0000\u0375\u0379\u0003\\.\u0000"+
		"\u0376\u0379\u0003^/\u0000\u0377\u0379\u0003`0\u0000\u0378\u0374\u0001"+
		"\u0000\u0000\u0000\u0378\u0375\u0001\u0000\u0000\u0000\u0378\u0376\u0001"+
		"\u0000\u0000\u0000\u0378\u0377\u0001\u0000\u0000\u0000\u0379Y\u0001\u0000"+
		"\u0000\u0000\u037a\u037b\u0005\u001d\u0000\u0000\u037b\u037c\u0005+\u0000"+
		"\u0000\u037c\u0381\u0003t:\u0000\u037d\u037e\u0005\u0084\u0000\u0000\u037e"+
		"\u0380\u0003t:\u0000\u037f\u037d\u0001\u0000\u0000\u0000\u0380\u0383\u0001"+
		"\u0000\u0000\u0000\u0381\u037f\u0001\u0000\u0000\u0000\u0381\u0382\u0001"+
		"\u0000\u0000\u0000\u0382[\u0001\u0000\u0000\u0000\u0383\u0381\u0001\u0000"+
		"\u0000\u0000\u0384\u0385\u0005g\u0000\u0000\u0385\u038a\u0003v;\u0000"+
		"\u0386\u0387\u0005\u0084\u0000\u0000\u0387\u0389\u0003v;\u0000\u0388\u0386"+
		"\u0001\u0000\u0000\u0000\u0389\u038c\u0001\u0000\u0000\u0000\u038a\u0388"+
		"\u0001\u0000\u0000\u0000\u038a\u038b\u0001\u0000\u0000\u0000\u038b]\u0001"+
		"\u0000\u0000\u0000\u038c\u038a\u0001\u0000\u0000\u0000\u038d\u038e\u0005"+
		"4\u0000\u0000\u038e\u0393\u0003x<\u0000\u038f\u0390\u0005\u0084\u0000"+
		"\u0000\u0390\u0392\u0003x<\u0000\u0391\u038f\u0001\u0000\u0000\u0000\u0392"+
		"\u0395\u0001\u0000\u0000\u0000\u0393\u0391\u0001\u0000\u0000\u0000\u0393"+
		"\u0394\u0001\u0000\u0000\u0000\u0394_\u0001\u0000\u0000\u0000\u0395\u0393"+
		"\u0001\u0000\u0000\u0000\u0396\u0397\u0005\u001b\u0000\u0000\u0397\u039c"+
		"\u0003z=\u0000\u0398\u0399\u0005\u0084\u0000\u0000\u0399\u039b\u0003z"+
		"=\u0000\u039a\u0398\u0001\u0000\u0000\u0000\u039b\u039e\u0001\u0000\u0000"+
		"\u0000\u039c\u039a\u0001\u0000\u0000\u0000\u039c\u039d\u0001\u0000\u0000"+
		"\u0000\u039da\u0001\u0000\u0000\u0000\u039e\u039c\u0001\u0000\u0000\u0000"+
		"\u039f\u03a9\u0005\u0083\u0000\u0000\u03a0\u03a4\u0005\u007f\u0000\u0000"+
		"\u03a1\u03a3\u0003d2\u0000\u03a2\u03a1\u0001\u0000\u0000\u0000\u03a3\u03a6"+
		"\u0001\u0000\u0000\u0000\u03a4\u03a2\u0001\u0000\u0000\u0000\u03a4\u03a5"+
		"\u0001\u0000\u0000\u0000\u03a5\u03a7\u0001\u0000\u0000\u0000\u03a6\u03a4"+
		"\u0001\u0000\u0000\u0000\u03a7\u03a9\u0005\u0080\u0000\u0000\u03a8\u039f"+
		"\u0001\u0000\u0000\u0000\u03a8\u03a0\u0001\u0000\u0000\u0000\u03a9c\u0001"+
		"\u0000\u0000\u0000\u03aa\u03b0\u00034\u001a\u0000\u03ab\u03b0\u0003|>"+
		"\u0000\u03ac\u03b0\u00038\u001c\u0000\u03ad\u03b0\u0003<\u001e\u0000\u03ae"+
		"\u03b0\u0005\u009a\u0000\u0000\u03af\u03aa\u0001\u0000\u0000\u0000\u03af"+
		"\u03ab\u0001\u0000\u0000\u0000\u03af\u03ac\u0001\u0000\u0000\u0000\u03af"+
		"\u03ad\u0001\u0000\u0000\u0000\u03af\u03ae\u0001\u0000\u0000\u0000\u03b0"+
		"e\u0001\u0000\u0000\u0000\u03b1\u03b2\u0005W\u0000\u0000\u03b2\u03b4\u0003"+
		"\u000e\u0007\u0000\u03b3\u03b1\u0001\u0000\u0000\u0000\u03b3\u03b4\u0001"+
		"\u0000\u0000\u0000\u03b4\u03b5\u0001\u0000\u0000\u0000\u03b5\u03b6\u0005"+
		"_\u0000\u0000\u03b6\u03b7\u0003j5\u0000\u03b7\u03b8\u0003\u0002\u0001"+
		"\u0000\u03b8\u03b9\u0003l6\u0000\u03b9\u03ba\u0003\u0010\b\u0000\u03ba"+
		"g\u0001\u0000\u0000\u0000\u03bb\u03bc\u0003l6\u0000\u03bci\u0001\u0000"+
		"\u0000\u0000\u03bd\u03c0\u0003:\u001d\u0000\u03be\u03c0\u0003\u00ccf\u0000"+
		"\u03bf\u03bd\u0001\u0000\u0000\u0000\u03bf\u03be\u0001\u0000\u0000\u0000"+
		"\u03c0k\u0001\u0000\u0000\u0000\u03c1\u03c4\u0003:\u001d\u0000\u03c2\u03c4"+
		"\u0003\u00ccf\u0000\u03c3\u03c1\u0001\u0000\u0000\u0000\u03c3\u03c2\u0001"+
		"\u0000\u0000\u0000\u03c4m\u0001\u0000\u0000\u0000\u03c5\u03c6\u0005\u0013"+
		"\u0000\u0000\u03c6\u03c8\u0003\u000e\u0007\u0000\u03c7\u03c5\u0001\u0000"+
		"\u0000\u0000\u03c7\u03c8\u0001\u0000\u0000\u0000\u03c8\u03c9\u0001\u0000"+
		"\u0000\u0000\u03c9\u03cc\u0005\u0011\u0000\u0000\u03ca\u03cd\u0003:\u001d"+
		"\u0000\u03cb\u03cd\u0003\u00ceg\u0000\u03cc\u03ca\u0001\u0000\u0000\u0000"+
		"\u03cc\u03cb\u0001\u0000\u0000\u0000\u03cd\u03ce\u0001\u0000\u0000\u0000"+
		"\u03ce\u03d1\u0003\f\u0006\u0000\u03cf\u03d2\u0003:\u001d\u0000\u03d0"+
		"\u03d2\u0003\u00ceg\u0000\u03d1\u03cf\u0001\u0000\u0000\u0000\u03d1\u03d0"+
		"\u0001\u0000\u0000\u0000\u03d2\u03d3\u0001\u0000\u0000\u0000\u03d3\u03d4"+
		"\u0003\u0010\b\u0000\u03d4o\u0001\u0000\u0000\u0000\u03d5\u03d8\u0003"+
		":\u001d\u0000\u03d6\u03d8\u0003\u00ceg\u0000\u03d7\u03d5\u0001\u0000\u0000"+
		"\u0000\u03d7\u03d6\u0001\u0000\u0000\u0000\u03d8q\u0001\u0000\u0000\u0000"+
		"\u03d9\u03da\u0005\u001c\u0000\u0000\u03da\u03dc\u0003\u000e\u0007\u0000"+
		"\u03db\u03d9\u0001\u0000\u0000\u0000\u03db\u03dc\u0001\u0000\u0000\u0000"+
		"\u03dc\u03dd\u0001\u0000\u0000\u0000\u03dd\u03e0\u0005\u001d\u0000\u0000"+
		"\u03de\u03e1\u0003:\u001d\u0000\u03df\u03e1\u0003\u00ceg\u0000\u03e0\u03de"+
		"\u0001\u0000\u0000\u0000\u03e0\u03df\u0001\u0000\u0000\u0000\u03e1\u03e2"+
		"\u0001\u0000\u0000\u0000\u03e2\u03e5\u0005+\u0000\u0000\u03e3\u03e6\u0003"+
		":\u001d\u0000\u03e4\u03e6\u0003\u00ceg\u0000\u03e5\u03e3\u0001\u0000\u0000"+
		"\u0000\u03e5\u03e4\u0001\u0000\u0000\u0000\u03e6\u03e7\u0001\u0000\u0000"+
		"\u0000\u03e7\u03e8\u0003\u0010\b\u0000\u03e8s\u0001\u0000\u0000\u0000"+
		"\u03e9\u03ec\u0003:\u001d\u0000\u03ea\u03ec\u0003\u00ceg\u0000\u03eb\u03e9"+
		"\u0001\u0000\u0000\u0000\u03eb\u03ea\u0001\u0000\u0000\u0000\u03ecu\u0001"+
		"\u0000\u0000\u0000\u03ed\u03f0\u0003:\u001d\u0000\u03ee\u03f0\u0003\u00cc"+
		"f\u0000\u03ef\u03ed\u0001\u0000\u0000\u0000\u03ef\u03ee\u0001\u0000\u0000"+
		"\u0000\u03f0w\u0001\u0000\u0000\u0000\u03f1\u03f4\u0003:\u001d\u0000\u03f2"+
		"\u03f4\u0003\u00ccf\u0000\u03f3\u03f1\u0001\u0000\u0000\u0000\u03f3\u03f2"+
		"\u0001\u0000\u0000\u0000\u03f4y\u0001\u0000\u0000\u0000\u03f5\u03f8\u0003"+
		":\u001d\u0000\u03f6\u03f8\u0003\u00ccf\u0000\u03f7\u03f5\u0001\u0000\u0000"+
		"\u0000\u03f7\u03f6\u0001\u0000\u0000\u0000\u03f8{\u0001\u0000\u0000\u0000"+
		"\u03f9\u03fc\u0003~?\u0000\u03fa\u03fc\u0003\u0080@\u0000\u03fb\u03f9"+
		"\u0001\u0000\u0000\u0000\u03fb\u03fa\u0001\u0000\u0000\u0000\u03fc}\u0001"+
		"\u0000\u0000\u0000\u03fd\u03fe\u0003.\u0017\u0000\u03fe\u03ff\u0005<\u0000"+
		"\u0000\u03ff\u0400\u0003L&\u0000\u0400\u007f\u0001\u0000\u0000\u0000\u0401"+
		"\u0402\u0003.\u0017\u0000\u0402\u0403\u0003L&\u0000\u0403\u0081\u0001"+
		"\u0000\u0000\u0000\u0404\u0405\u0003P(\u0000\u0405\u0406\u0005\u000e\u0000"+
		"\u0000\u0406\u0407\u0003\u0084B\u0000\u0407\u0408\u0003b1\u0000\u0408"+
		"\u0083\u0001\u0000\u0000\u0000\u0409\u040b\u0005\u0004\u0000\u0000\u040a"+
		"\u0409\u0001\u0000\u0000\u0000\u040a\u040b\u0001\u0000\u0000\u0000\u040b"+
		"\u040c\u0001\u0000\u0000\u0000\u040c\u040e\u0003\u000e\u0007\u0000\u040d"+
		"\u040f\u0003\u01ee\u00f7\u0000\u040e\u040d\u0001\u0000\u0000\u0000\u040e"+
		"\u040f\u0001\u0000\u0000\u0000\u040f\u0412\u0001\u0000\u0000\u0000\u0410"+
		"\u0413\u0003\u0086C\u0000\u0411\u0413\u0003V+\u0000\u0412\u0410\u0001"+
		"\u0000\u0000\u0000\u0412\u0411\u0001\u0000\u0000\u0000\u0412\u0413\u0001"+
		"\u0000\u0000\u0000\u0413\u0417\u0001\u0000\u0000\u0000\u0414\u0416\u0003"+
		"X,\u0000\u0415\u0414\u0001\u0000\u0000\u0000\u0416\u0419\u0001\u0000\u0000"+
		"\u0000\u0417\u0415\u0001\u0000\u0000\u0000\u0417\u0418\u0001\u0000\u0000"+
		"\u0000\u0418\u0085\u0001\u0000\u0000\u0000\u0419\u0417\u0001\u0000\u0000"+
		"\u0000\u041a\u041b\u0003\u0002\u0001\u0000\u041b\u0420\u0003\u008aE\u0000"+
		"\u041c\u041d\u0005\u0084\u0000\u0000\u041d\u041f\u0003\u008aE\u0000\u041e"+
		"\u041c\u0001\u0000\u0000\u0000\u041f\u0422\u0001\u0000\u0000\u0000\u0420"+
		"\u041e\u0001\u0000\u0000\u0000\u0420\u0421\u0001\u0000\u0000\u0000\u0421"+
		"\u0087\u0001\u0000\u0000\u0000\u0422\u0420\u0001\u0000\u0000\u0000\u0423"+
		"\u0424\u0005W\u0000\u0000\u0424\u0426\u0003\u000e\u0007\u0000\u0425\u0423"+
		"\u0001\u0000\u0000\u0000\u0425\u0426\u0001\u0000\u0000\u0000\u0426\u0427"+
		"\u0001\u0000\u0000\u0000\u0427\u0428\u0005\\\u0000\u0000\u0428\u0429\u0003"+
		":\u001d\u0000\u0429\u042a\u0003\u0002\u0001\u0000\u042a\u042b\u0003:\u001d"+
		"\u0000\u042b\u042c\u0003\u0010\b\u0000\u042c\u0089\u0001\u0000\u0000\u0000"+
		"\u042d\u042e\u0003:\u001d\u0000\u042e\u008b\u0001\u0000\u0000\u0000\u042f"+
		"\u0432\u0003\u0092I\u0000\u0430\u0433\u0005$\u0000\u0000\u0431\u0433\u0003"+
		"\u01fa\u00fd\u0000\u0432\u0430\u0001\u0000\u0000\u0000\u0432\u0431\u0001"+
		"\u0000\u0000\u0000\u0433\u0435\u0001\u0000\u0000\u0000\u0434\u0436\u0003"+
		"\u009aM\u0000\u0435\u0434\u0001\u0000\u0000\u0000\u0435\u0436\u0001\u0000"+
		"\u0000\u0000\u0436\u043e\u0001\u0000\u0000\u0000\u0437\u043a\u0003\u008e"+
		"G\u0000\u0438\u043a\u0003\u0090H\u0000\u0439\u0437\u0001\u0000\u0000\u0000"+
		"\u0439\u0438\u0001\u0000\u0000\u0000\u043a\u043b\u0001\u0000\u0000\u0000"+
		"\u043b\u043c\u0003\u009aM\u0000\u043c\u043e\u0001\u0000\u0000\u0000\u043d"+
		"\u042f\u0001\u0000\u0000\u0000\u043d\u0439\u0001\u0000\u0000\u0000\u043e"+
		"\u0440\u0001\u0000\u0000\u0000\u043f\u0441\u0003\u01e4\u00f2\u0000\u0440"+
		"\u043f\u0001\u0000\u0000\u0000\u0440\u0441\u0001\u0000\u0000\u0000\u0441"+
		"\u0442\u0001\u0000\u0000\u0000\u0442\u0443\u0003b1\u0000\u0443\u008d\u0001"+
		"\u0000\u0000\u0000\u0444\u0446\u0005\u0015\u0000\u0000\u0445\u0444\u0001"+
		"\u0000\u0000\u0000\u0445\u0446\u0001\u0000\u0000\u0000\u0446\u0447\u0001"+
		"\u0000\u0000\u0000\u0447\u0448\u0005 \u0000\u0000\u0448\u008f\u0001\u0000"+
		"\u0000\u0000\u0449\u044b\u0003\u0098L\u0000\u044a\u0449\u0001\u0000\u0000"+
		"\u0000\u044a\u044b\u0001\u0000\u0000\u0000\u044b\u044d\u0001\u0000\u0000"+
		"\u0000\u044c\u044e\u0005\u001a\u0000\u0000\u044d\u044c\u0001\u0000\u0000"+
		"\u0000\u044d\u044e\u0001\u0000\u0000\u0000\u044e\u0450\u0001\u0000\u0000"+
		"\u0000\u044f\u0451\u0005\u0002\u0000\u0000\u0450\u044f\u0001\u0000\u0000"+
		"\u0000\u0450\u0451\u0001\u0000\u0000\u0000\u0451\u0454\u0001\u0000\u0000"+
		"\u0000\u0452\u0455\u0005\u0010\u0000\u0000\u0453\u0455\u0005K\u0000\u0000"+
		"\u0454\u0452\u0001\u0000\u0000\u0000\u0454\u0453\u0001\u0000\u0000\u0000"+
		"\u0454\u0455\u0001\u0000\u0000\u0000\u0455\u0458\u0001\u0000\u0000\u0000"+
		"\u0456\u0459\u0005h\u0000\u0000\u0457\u0459\u0005\u0015\u0000\u0000\u0458"+
		"\u0456\u0001\u0000\u0000\u0000\u0458\u0457\u0001\u0000\u0000\u0000\u0458"+
		"\u0459\u0001\u0000\u0000\u0000\u0459\u0091\u0001\u0000\u0000\u0000\u045a"+
		"\u045c\u0003\u008eG\u0000\u045b\u045d\u0003\u0094J\u0000\u045c\u045b\u0001"+
		"\u0000\u0000\u0000\u045c\u045d\u0001\u0000\u0000\u0000\u045d\u0460\u0001"+
		"\u0000\u0000\u0000\u045e\u0460\u0003\u0090H\u0000\u045f\u045a\u0001\u0000"+
		"\u0000\u0000\u045f\u045e\u0001\u0000\u0000\u0000\u0460\u0464\u0001\u0000"+
		"\u0000\u0000\u0461\u0463\u0003\u01fa\u00fd\u0000\u0462\u0461\u0001\u0000"+
		"\u0000\u0000\u0463\u0466\u0001\u0000\u0000\u0000\u0464\u0462\u0001\u0000"+
		"\u0000\u0000\u0464\u0465\u0001\u0000\u0000\u0000\u0465\u0093\u0001\u0000"+
		"\u0000\u0000\u0466\u0464\u0001\u0000\u0000\u0000\u0467\u0468\u0003\u0096"+
		"K\u0000\u0468\u0095\u0001\u0000\u0000\u0000\u0469\u046a\u0003\u0090H\u0000"+
		"\u046a\u046b\u0003\u009aM\u0000\u046b\u0097\u0001\u0000\u0000\u0000\u046c"+
		"\u046d\u0007\u0007\u0000\u0000\u046d\u0099\u0001\u0000\u0000\u0000\u046e"+
		"\u0470\u0005\u0004\u0000\u0000\u046f\u046e\u0001\u0000\u0000\u0000\u046f"+
		"\u0470\u0001\u0000\u0000\u0000\u0470\u0478\u0001\u0000\u0000\u0000\u0471"+
		"\u0474\u0003\u009cN\u0000\u0472\u0475\u0003\u00a6S\u0000\u0473\u0475\u0003"+
		"V+\u0000\u0474\u0472\u0001\u0000\u0000\u0000\u0474\u0473\u0001\u0000\u0000"+
		"\u0000\u0474\u0475\u0001\u0000\u0000\u0000\u0475\u0479\u0001\u0000\u0000"+
		"\u0000\u0476\u0479\u0003\u00a6S\u0000\u0477\u0479\u0003V+\u0000\u0478"+
		"\u0471\u0001\u0000\u0000\u0000\u0478\u0476\u0001\u0000\u0000\u0000\u0478"+
		"\u0477\u0001\u0000\u0000\u0000\u0479\u047d\u0001\u0000\u0000\u0000\u047a"+
		"\u047c\u0003\u009eO\u0000\u047b\u047a\u0001\u0000\u0000\u0000\u047c\u047f"+
		"\u0001\u0000\u0000\u0000\u047d\u047b\u0001\u0000\u0000\u0000\u047d\u047e"+
		"\u0001\u0000\u0000\u0000\u047e\u009b\u0001\u0000\u0000\u0000\u047f\u047d"+
		"\u0001\u0000\u0000\u0000\u0480\u0481\u0005\u0093\u0000\u0000\u0481\u0482"+
		"\u0005\u009f\u0000\u0000\u0482\u0484\u0005\u0095\u0000\u0000\u0483\u0485"+
		"\u0005\u009f\u0000\u0000\u0484\u0483\u0001\u0000\u0000\u0000\u0484\u0485"+
		"\u0001\u0000\u0000\u0000\u0485\u0488\u0001\u0000\u0000\u0000\u0486\u0488"+
		"\u0005\u009f\u0000\u0000\u0487\u0480\u0001\u0000\u0000\u0000\u0487\u0486"+
		"\u0001\u0000\u0000\u0000\u0488\u009d\u0001\u0000\u0000\u0000\u0489\u048e"+
		"\u0003X,\u0000\u048a\u048e\u0003\u00a0P\u0000\u048b\u048e\u0003\u00a2"+
		"Q\u0000\u048c\u048e\u0003\u00a4R\u0000\u048d\u0489\u0001\u0000\u0000\u0000"+
		"\u048d\u048a\u0001\u0000\u0000\u0000\u048d\u048b\u0001\u0000\u0000\u0000"+
		"\u048d\u048c\u0001\u0000\u0000\u0000\u048e\u009f\u0001\u0000\u0000\u0000"+
		"\u048f\u0492\u0005\f\u0000\u0000\u0490\u0493\u0003\u00d0h\u0000\u0491"+
		"\u0493\u0003\u00ceg\u0000\u0492\u0490\u0001\u0000\u0000\u0000\u0492\u0491"+
		"\u0001\u0000\u0000\u0000\u0493\u00a1\u0001\u0000\u0000\u0000\u0494\u0495"+
		"\u00056\u0000\u0000\u0495\u0496\u0005F\u0000\u0000\u0496\u0497\u0003\u00d4"+
		"j\u0000\u0497\u00a3\u0001\u0000\u0000\u0000\u0498\u0499\u0005%\u0000\u0000"+
		"\u0499\u049a\u0005\u000b\u0000\u0000\u049a\u049f\u0003\u00d8l\u0000\u049b"+
		"\u049c\u0005\u0084\u0000\u0000\u049c\u049e\u0003\u00d8l\u0000\u049d\u049b"+
		"\u0001\u0000\u0000\u0000\u049e\u04a1\u0001\u0000\u0000\u0000\u049f\u049d"+
		"\u0001\u0000\u0000\u0000\u049f\u04a0\u0001\u0000\u0000\u0000\u04a0\u00a5"+
		"\u0001\u0000\u0000\u0000\u04a1\u049f\u0001\u0000\u0000\u0000\u04a2\u04a4"+
		"\u0003\u00aaU\u0000\u04a3\u04a2\u0001\u0000\u0000\u0000\u04a4\u04a5\u0001"+
		"\u0000\u0000\u0000\u04a5\u04a3\u0001\u0000\u0000\u0000\u04a5\u04a6\u0001"+
		"\u0000\u0000\u0000\u04a6\u04a8\u0001\u0000\u0000\u0000\u04a7\u04a9\u0003"+
		"\u00a8T\u0000\u04a8\u04a7\u0001\u0000\u0000\u0000\u04a8\u04a9\u0001\u0000"+
		"\u0000\u0000\u04a9\u04ad\u0001\u0000\u0000\u0000\u04aa\u04ac\u0003\u00aa"+
		"U\u0000\u04ab\u04aa\u0001\u0000\u0000\u0000\u04ac\u04af\u0001\u0000\u0000"+
		"\u0000\u04ad\u04ab\u0001\u0000\u0000\u0000\u04ad\u04ae\u0001\u0000\u0000"+
		"\u0000\u04ae\u04b8\u0001\u0000\u0000\u0000\u04af\u04ad\u0001\u0000\u0000"+
		"\u0000\u04b0\u04b4\u0003\u00a8T\u0000\u04b1\u04b3\u0003\u00aaU\u0000\u04b2"+
		"\u04b1\u0001\u0000\u0000\u0000\u04b3\u04b6\u0001\u0000\u0000\u0000\u04b4"+
		"\u04b2\u0001\u0000\u0000\u0000\u04b4\u04b5\u0001\u0000\u0000\u0000\u04b5"+
		"\u04b8\u0001\u0000\u0000\u0000\u04b6\u04b4\u0001\u0000\u0000\u0000\u04b7"+
		"\u04a3\u0001\u0000\u0000\u0000\u04b7\u04b0\u0001\u0000\u0000\u0000\u04b8"+
		"\u00a7\u0001\u0000\u0000\u0000\u04b9\u04c8\u0003\u01ee\u00f7\u0000\u04ba"+
		"\u04bc\u0003\u01ee\u00f7\u0000\u04bb\u04ba\u0001\u0000\u0000\u0000\u04bb"+
		"\u04bc\u0001\u0000\u0000\u0000\u04bc\u04c5\u0001\u0000\u0000\u0000\u04bd"+
		"\u04bf\u0005H\u0000\u0000\u04be\u04c0\u0005C\u0000\u0000\u04bf\u04be\u0001"+
		"\u0000\u0000\u0000\u04bf\u04c0\u0001\u0000\u0000\u0000\u04c0\u04c6\u0001"+
		"\u0000\u0000\u0000\u04c1\u04c3\u0005C\u0000\u0000\u04c2\u04c4\u0005H\u0000"+
		"\u0000\u04c3\u04c2\u0001\u0000\u0000\u0000\u04c3\u04c4\u0001\u0000\u0000"+
		"\u0000\u04c4\u04c6\u0001\u0000\u0000\u0000\u04c5\u04bd\u0001\u0000\u0000"+
		"\u0000\u04c5\u04c1\u0001\u0000\u0000\u0000\u04c6\u04c8\u0001\u0000\u0000"+
		"\u0000\u04c7\u04b9\u0001\u0000\u0000\u0000\u04c7\u04bb\u0001\u0000\u0000"+
		"\u0000\u04c8\u00a9\u0001\u0000\u0000\u0000\u04c9\u04cf\u0003\u00acV\u0000"+
		"\u04ca\u04cf\u0003\u00b0X\u0000\u04cb\u04cf\u0003\u00b4Z\u0000\u04cc\u04cf"+
		"\u0003\u00b6[\u0000\u04cd\u04cf\u0003\u00b8\\\u0000\u04ce\u04c9\u0001"+
		"\u0000\u0000\u0000\u04ce\u04ca\u0001\u0000\u0000\u0000\u04ce\u04cb\u0001"+
		"\u0000\u0000\u0000\u04ce\u04cc\u0001\u0000\u0000\u0000\u04ce\u04cd\u0001"+
		"\u0000\u0000\u0000\u04cf\u00ab\u0001\u0000\u0000\u0000\u04d0\u04d5\u0003"+
		"\u00aeW\u0000\u04d1\u04d2\u0005\u0084\u0000\u0000\u04d2\u04d4\u0003\u00be"+
		"_\u0000\u04d3\u04d1\u0001\u0000\u0000\u0000\u04d4\u04d7\u0001\u0000\u0000"+
		"\u0000\u04d5\u04d3\u0001\u0000\u0000\u0000\u04d5\u04d6\u0001\u0000\u0000"+
		"\u0000\u04d6\u00ad\u0001\u0000\u0000\u0000\u04d7\u04d5\u0001\u0000\u0000"+
		"\u0000\u04d8\u04d9\u0003\u0000\u0000\u0000\u04d9\u04da\u0003\u00be_\u0000"+
		"\u04da\u00af\u0001\u0000\u0000\u0000\u04db\u04e0\u0003\u00b2Y\u0000\u04dc"+
		"\u04dd\u0005\u0084\u0000\u0000\u04dd\u04df\u0003\u00c2a\u0000\u04de\u04dc"+
		"\u0001\u0000\u0000\u0000\u04df\u04e2\u0001\u0000\u0000\u0000\u04e0\u04de"+
		"\u0001\u0000\u0000\u0000\u04e0\u04e1\u0001\u0000\u0000\u0000\u04e1\u00b1"+
		"\u0001\u0000\u0000\u0000\u04e2\u04e0\u0001\u0000\u0000\u0000\u04e3\u04e4"+
		"\u0003\u0004\u0002\u0000\u04e4\u04e5\u0003\u00c2a\u0000\u04e5\u00b3\u0001"+
		"\u0000\u0000\u0000\u04e6\u04e7\u0003\u0006\u0003\u0000\u04e7\u04e8\u0003"+
		"\u00c4b\u0000\u04e8\u00b5\u0001\u0000\u0000\u0000\u04e9\u04ea\u0003\b"+
		"\u0004\u0000\u04ea\u04eb\u0003\u00c6c\u0000\u04eb\u00b7\u0001\u0000\u0000"+
		"\u0000\u04ec\u04f1\u0003\u00ba]\u0000\u04ed\u04ee\u0005\u0084\u0000\u0000"+
		"\u04ee\u04f0\u0003\u00cae\u0000\u04ef\u04ed\u0001\u0000\u0000\u0000\u04f0"+
		"\u04f3\u0001\u0000\u0000\u0000\u04f1\u04ef\u0001\u0000\u0000\u0000\u04f1"+
		"\u04f2\u0001\u0000\u0000\u0000\u04f2\u00b9\u0001\u0000\u0000\u0000\u04f3"+
		"\u04f1\u0001\u0000\u0000\u0000\u04f4\u04f5\u0003\n\u0005\u0000\u04f5\u04f6"+
		"\u0003\u00cae\u0000\u04f6\u00bb\u0001\u0000\u0000\u0000\u04f7\u04f8\u0005"+
		"W\u0000\u0000\u04f8\u04fa\u0003\u000e\u0007\u0000\u04f9\u04f7\u0001\u0000"+
		"\u0000\u0000\u04f9\u04fa\u0001\u0000\u0000\u0000\u04fa\u04fb\u0001\u0000"+
		"\u0000\u0000\u04fb\u04fc\u0005f\u0000\u0000\u04fc\u04fd\u0003:\u001d\u0000"+
		"\u04fd\u04fe\u0003\u00aeW\u0000\u04fe\u04ff\u0003l6\u0000\u04ff\u0500"+
		"\u0003\u0010\b\u0000\u0500\u00bd\u0001\u0000\u0000\u0000\u0501\u0502\u0003"+
		"l6\u0000\u0502\u00bf\u0001\u0000\u0000\u0000\u0503\u0504\u0005W\u0000"+
		"\u0000\u0504\u0506\u0003\u000e\u0007\u0000\u0505\u0503\u0001\u0000\u0000"+
		"\u0000\u0505\u0506\u0001\u0000\u0000\u0000\u0506\u0507\u0001\u0000\u0000"+
		"\u0000\u0507\u0508\u0005]\u0000\u0000\u0508\u0509\u0003j5\u0000\u0509"+
		"\u050a\u0003\u0004\u0002\u0000\u050a\u050b\u0003l6\u0000\u050b\u050c\u0003"+
		"\u0010\b\u0000\u050c\u00c1\u0001\u0000\u0000\u0000\u050d\u050e\u0003l"+
		"6\u0000\u050e\u00c3\u0001\u0000\u0000\u0000\u050f\u0510\u0003l6\u0000"+
		"\u0510\u00c5\u0001\u0000\u0000\u0000\u0511\u0512\u0003l6\u0000\u0512\u00c7"+
		"\u0001\u0000\u0000\u0000\u0513\u0514\u0005W\u0000\u0000\u0514\u0516\u0003"+
		"\u000e\u0007\u0000\u0515\u0513\u0001\u0000\u0000\u0000\u0515\u0516\u0001"+
		"\u0000\u0000\u0000\u0516\u0517\u0001\u0000\u0000\u0000\u0517\u0518\u0005"+
		"Q\u0000\u0000\u0518\u0519\u0003j5\u0000\u0519\u051a\u0003\n\u0005\u0000"+
		"\u051a\u051b\u0003l6\u0000\u051b\u051c\u0003\u0010\b\u0000\u051c\u00c9"+
		"\u0001\u0000\u0000\u0000\u051d\u051e\u0003l6\u0000\u051e\u00cb\u0001\u0000"+
		"\u0000\u0000\u051f\u0520\u0003\u00ceg\u0000\u0520\u00cd\u0001\u0000\u0000"+
		"\u0000\u0521\u0524\u0003\u00d0h\u0000\u0522\u0523\u0005\u0091\u0000\u0000"+
		"\u0523\u0525\u0003\u00d0h\u0000\u0524\u0522\u0001\u0000\u0000\u0000\u0525"+
		"\u0526\u0001\u0000\u0000\u0000\u0526\u0524\u0001\u0000\u0000\u0000\u0526"+
		"\u0527\u0001\u0000\u0000\u0000\u0527\u00cf\u0001\u0000\u0000\u0000\u0528"+
		"\u0529\u0003:\u001d\u0000\u0529\u00d1\u0001\u0000\u0000\u0000\u052a\u052c"+
		"\u00057\u0000\u0000\u052b\u052d\u0003\u000e\u0007\u0000\u052c\u052b\u0001"+
		"\u0000\u0000\u0000\u052c\u052d\u0001\u0000\u0000\u0000\u052d\u052f\u0001"+
		"\u0000\u0000\u0000\u052e\u052a\u0001\u0000\u0000\u0000\u052e\u052f\u0001"+
		"\u0000\u0000\u0000\u052f\u0530\u0001\u0000\u0000\u0000\u0530\u0533\u0005"+
		"6\u0000\u0000\u0531\u0534\u0003:\u001d\u0000\u0532\u0534\u0003\u00ccf"+
		"\u0000\u0533\u0531\u0001\u0000\u0000\u0000\u0533\u0532\u0001\u0000\u0000"+
		"\u0000\u0534\u0535\u0001\u0000\u0000\u0000\u0535\u0538\u0005F\u0000\u0000"+
		"\u0536\u0539\u0003:\u001d\u0000\u0537\u0539\u0003\u00ccf\u0000\u0538\u0536"+
		"\u0001\u0000\u0000\u0000\u0538\u0537\u0001\u0000\u0000\u0000\u0539\u053a"+
		"\u0001\u0000\u0000\u0000\u053a\u053b\u0003\u0010\b\u0000\u053b\u00d3\u0001"+
		"\u0000\u0000\u0000\u053c\u053f\u0003:\u001d\u0000\u053d\u053f\u0003\u00cc"+
		"f\u0000\u053e\u053c\u0001\u0000\u0000\u0000\u053e\u053d\u0001\u0000\u0000"+
		"\u0000\u053f\u00d5\u0001\u0000\u0000\u0000\u0540\u0544\u0005&\u0000\u0000"+
		"\u0541\u0542\u0003\u000e\u0007\u0000\u0542\u0543\u0005F\u0000\u0000\u0543"+
		"\u0545\u0001\u0000\u0000\u0000\u0544\u0541\u0001\u0000\u0000\u0000\u0544"+
		"\u0545\u0001\u0000\u0000\u0000\u0545\u0546\u0001\u0000\u0000\u0000\u0546"+
		"\u0547\u0003:\u001d\u0000\u0547\u0548\u0005\u000b\u0000\u0000\u0548\u0549"+
		"\u0003:\u001d\u0000\u0549\u054a\u0003\u0010\b\u0000\u054a\u00d7\u0001"+
		"\u0000\u0000\u0000\u054b\u054c\u0003:\u001d\u0000\u054c\u00d9\u0001\u0000"+
		"\u0000\u0000\u054d\u054e\u0003P(\u0000\u054e\u054f\u0005\u0017\u0000\u0000"+
		"\u054f\u0550\u0003\u0084B\u0000\u0550\u0551\u0003b1\u0000\u0551\u00db"+
		"\u0001\u0000\u0000\u0000\u0552\u0553\u0003P(\u0000\u0553\u0554\u0005\r"+
		"\u0000\u0000\u0554\u0555\u0003\u0084B\u0000\u0555\u0556\u0003b1\u0000"+
		"\u0556\u00dd\u0001\u0000\u0000\u0000\u0557\u0558\u0003P(\u0000\u0558\u0559"+
		"\u0005[\u0000\u0000\u0559\u055a\u0003\u0084B\u0000\u055a\u055b\u0003b"+
		"1\u0000\u055b\u00df\u0001\u0000\u0000\u0000\u055c\u055d\u0003P(\u0000"+
		"\u055d\u055e\u0005\u0007\u0000\u0000\u055e\u055f\u0003\u0084B\u0000\u055f"+
		"\u0560\u0003b1\u0000\u0560\u00e1\u0001\u0000\u0000\u0000\u0561\u0562\u0003"+
		"P(\u0000\u0562\u0563\u0005\u0007\u0000\u0000\u0563\u0564\u0005[\u0000"+
		"\u0000\u0564\u0565\u0003\u0084B\u0000\u0565\u0566\u0003b1\u0000\u0566"+
		"\u00e3\u0001\u0000\u0000\u0000\u0567\u0568\u0003\u0092I\u0000\u0568\u0570"+
		"\u0005\u0014\u0000\u0000\u0569\u056b\u0003\u009aM\u0000\u056a\u0569\u0001"+
		"\u0000\u0000\u0000\u056a\u056b\u0001\u0000\u0000\u0000\u056b\u056d\u0001"+
		"\u0000\u0000\u0000\u056c\u056e\u0003\u01e4\u00f2\u0000\u056d\u056c\u0001"+
		"\u0000\u0000\u0000\u056d\u056e\u0001\u0000\u0000\u0000\u056e\u0571\u0001"+
		"\u0000\u0000\u0000\u056f\u0571\u0003\u00e6s\u0000\u0570\u056a\u0001\u0000"+
		"\u0000\u0000\u0570\u056f\u0001\u0000\u0000\u0000\u0571\u0572\u0001\u0000"+
		"\u0000\u0000\u0572\u0573\u0003b1\u0000\u0573\u00e5\u0001\u0000\u0000\u0000"+
		"\u0574\u0577\u0003\u00e8t\u0000\u0575\u0577\u0003\u00eau\u0000\u0576\u0574"+
		"\u0001\u0000\u0000\u0000\u0576\u0575\u0001\u0000\u0000\u0000\u0577\u00e7"+
		"\u0001\u0000\u0000\u0000\u0578\u057a\u0003\u009aM\u0000\u0579\u0578\u0001"+
		"\u0000\u0000\u0000\u0579\u057a\u0001\u0000\u0000\u0000\u057a\u057b\u0001"+
		"\u0000\u0000\u0000\u057b\u0581\u0005+\u0000\u0000\u057c\u057e\u0005\u0004"+
		"\u0000\u0000\u057d\u057f\u0005+\u0000\u0000\u057e\u057d\u0001\u0000\u0000"+
		"\u0000\u057e\u057f\u0001\u0000\u0000\u0000\u057f\u0581\u0001\u0000\u0000"+
		"\u0000\u0580\u0579\u0001\u0000\u0000\u0000\u0580\u057c\u0001\u0000\u0000"+
		"\u0000\u0580\u0581\u0001\u0000\u0000\u0000\u0581\u0582\u0001\u0000\u0000"+
		"\u0000\u0582\u0583\u0003\u00ecv\u0000\u0583\u0584\u0005b\u0000\u0000\u0584"+
		"\u0585\u0003\u00ecv\u0000\u0585\u00e9\u0001\u0000\u0000\u0000\u0586\u0588"+
		"\u0003\u009aM\u0000\u0587\u0586\u0001\u0000\u0000\u0000\u0587\u0588\u0001"+
		"\u0000\u0000\u0000\u0588\u0589\u0001\u0000\u0000\u0000\u0589\u058a\u0005"+
		"}\u0000\u0000\u058a\u058b\u0003\u00ecv\u0000\u058b\u058c\u0005\u0084\u0000"+
		"\u0000\u058c\u0591\u0003\u00ecv\u0000\u058d\u058e\u0005\u0084\u0000\u0000"+
		"\u058e\u0590\u0003\u00ecv\u0000\u058f\u058d\u0001\u0000\u0000\u0000\u0590"+
		"\u0593\u0001\u0000\u0000\u0000\u0591\u058f\u0001\u0000\u0000\u0000\u0591"+
		"\u0592\u0001\u0000\u0000\u0000\u0592\u0594\u0001\u0000\u0000\u0000\u0593"+
		"\u0591\u0001\u0000\u0000\u0000\u0594\u0595\u0005~\u0000\u0000\u0595\u00eb"+
		"\u0001\u0000\u0000\u0000\u0596\u0597\u0003\u00eew\u0000\u0597\u00ed\u0001"+
		"\u0000\u0000\u0000\u0598\u059a\u0003\u00f0x\u0000\u0599\u0598\u0001\u0000"+
		"\u0000\u0000\u0599\u059a\u0001\u0000\u0000\u0000\u059a\u059d\u0001\u0000"+
		"\u0000\u0000\u059b\u059c\u0005\u009f\u0000\u0000\u059c\u059e\u0003\u0006"+
		"\u0003\u0000\u059d\u059b\u0001\u0000\u0000\u0000\u059d\u059e\u0001\u0000"+
		"\u0000\u0000\u059e\u059f\u0001\u0000\u0000\u0000\u059f\u05a0\u0003\u00c4"+
		"b\u0000\u05a0\u00ef\u0001\u0000\u0000\u0000\u05a1\u05a2\u0003\u00f2y\u0000"+
		"\u05a2\u00f1\u0001\u0000\u0000\u0000\u05a3\u05a4\u0003\u01ee\u00f7\u0000"+
		"\u05a4\u00f3\u0001\u0000\u0000\u0000\u05a5\u05a6\u0003\u0092I\u0000\u05a6"+
		"\u05a7\u0005\t\u0000\u0000\u05a7\u05a8\u0003\u00f6{\u0000\u05a8\u05a9"+
		"\u0003b1\u0000\u05a9\u00f5\u0001\u0000\u0000\u0000\u05aa\u05b0\u0003\u009a"+
		"M\u0000\u05ab\u05ac\u0005F\u0000\u0000\u05ac\u05ad\u0003\u00ecv\u0000"+
		"\u05ad\u05ae\u0005\u0094\u0000\u0000\u05ae\u05af\u0003\u00ecv\u0000\u05af"+
		"\u05b1\u0001\u0000\u0000\u0000\u05b0\u05ab\u0001\u0000\u0000\u0000\u05b0"+
		"\u05b1\u0001\u0000\u0000\u0000\u05b1\u05bf\u0001\u0000\u0000\u0000\u05b2"+
		"\u05b4\u0005\u0004\u0000\u0000\u05b3\u05b2\u0001\u0000\u0000\u0000\u05b3"+
		"\u05b4\u0001\u0000\u0000\u0000\u05b4\u05bc\u0001\u0000\u0000\u0000\u05b5"+
		"\u05b7\u0005F\u0000\u0000\u05b6\u05b5\u0001\u0000\u0000\u0000\u05b6\u05b7"+
		"\u0001\u0000\u0000\u0000\u05b7\u05b8\u0001\u0000\u0000\u0000\u05b8\u05b9"+
		"\u0003\u00ecv\u0000\u05b9\u05ba\u0005\u0094\u0000\u0000\u05ba\u05bb\u0003"+
		"\u00ecv\u0000\u05bb\u05bd\u0001\u0000\u0000\u0000\u05bc\u05b6\u0001\u0000"+
		"\u0000\u0000\u05bc\u05bd\u0001\u0000\u0000\u0000\u05bd\u05bf\u0001\u0000"+
		"\u0000\u0000\u05be\u05aa\u0001\u0000\u0000\u0000\u05be\u05b3\u0001\u0000"+
		"\u0000\u0000\u05bf\u00f7\u0001\u0000\u0000\u0000\u05c0\u05c1\u0003\u0092"+
		"I\u0000\u05c1\u05c2\u0005`\u0000\u0000\u05c2\u05c3\u0003\u00fa}\u0000"+
		"\u05c3\u05c4\u0003b1\u0000\u05c4\u00f9\u0001\u0000\u0000\u0000\u05c5\u05cb"+
		"\u0003\u009aM\u0000\u05c6\u05c7\u0005(\u0000\u0000\u05c7\u05c8\u0003\u00ec"+
		"v\u0000\u05c8\u05c9\u0005a\u0000\u0000\u05c9\u05ca\u0003\u00ecv\u0000"+
		"\u05ca\u05cc\u0001\u0000\u0000\u0000\u05cb\u05c6\u0001\u0000\u0000\u0000"+
		"\u05cb\u05cc\u0001\u0000\u0000\u0000\u05cc\u05da\u0001\u0000\u0000\u0000"+
		"\u05cd\u05cf\u0005\u0004\u0000\u0000\u05ce\u05cd\u0001\u0000\u0000\u0000"+
		"\u05ce\u05cf\u0001\u0000\u0000\u0000\u05cf\u05d7\u0001\u0000\u0000\u0000"+
		"\u05d0\u05d2\u0005(\u0000\u0000\u05d1\u05d0\u0001\u0000\u0000\u0000\u05d1"+
		"\u05d2\u0001\u0000\u0000\u0000\u05d2\u05d3\u0001\u0000\u0000\u0000\u05d3"+
		"\u05d4\u0003\u00ecv\u0000\u05d4\u05d5\u0005a\u0000\u0000\u05d5\u05d6\u0003"+
		"\u00ecv\u0000\u05d6\u05d8\u0001\u0000\u0000\u0000\u05d7\u05d1\u0001\u0000"+
		"\u0000\u0000\u05d7\u05d8\u0001\u0000\u0000\u0000\u05d8\u05da\u0001\u0000"+
		"\u0000\u0000\u05d9\u05c5\u0001\u0000\u0000\u0000\u05d9\u05ce\u0001\u0000"+
		"\u0000\u0000\u05da\u00fb\u0001\u0000\u0000\u0000\u05db\u05dc\u0003P(\u0000"+
		"\u05dc\u05dd\u0005\b\u0000\u0000\u05dd\u05de\u0003\u0084B\u0000\u05de"+
		"\u05df\u0003b1\u0000\u05df\u00fd\u0001\u0000\u0000\u0000\u05e0\u05e1\u0003"+
		"\u0092I\u0000\u05e1\u05e3\u0005Z\u0000\u0000\u05e2\u05e4\u0003\u009aM"+
		"\u0000\u05e3\u05e2\u0001\u0000\u0000\u0000\u05e3\u05e4\u0001\u0000\u0000"+
		"\u0000\u05e4\u05e6\u0001\u0000\u0000\u0000\u05e5\u05e7\u0003\u01e4\u00f2"+
		"\u0000\u05e6\u05e5\u0001\u0000\u0000\u0000\u05e6\u05e7\u0001\u0000\u0000"+
		"\u0000\u05e7\u05e8\u0001\u0000\u0000\u0000\u05e8\u05e9\u0003b1\u0000\u05e9"+
		"\u00ff\u0001\u0000\u0000\u0000\u05ea\u05eb\u0003P(\u0000\u05eb\u05ec\u0005"+
		",\u0000\u0000\u05ec\u05ed\u0003\u0084B\u0000\u05ed\u05ee\u0003\u0102\u0081"+
		"\u0000\u05ee\u0101\u0001\u0000\u0000\u0000\u05ef\u05f5\u0005\u0083\u0000"+
		"\u0000\u05f0\u05f1\u0005\u007f\u0000\u0000\u05f1\u05f2\u0003\u0104\u0082"+
		"\u0000\u05f2\u05f3\u0005\u0080\u0000\u0000\u05f3\u05f5\u0001\u0000\u0000"+
		"\u0000\u05f4\u05ef\u0001\u0000\u0000\u0000\u05f4\u05f0\u0001\u0000\u0000"+
		"\u0000\u05f5\u0103\u0001\u0000\u0000\u0000\u05f6\u05f9\u0003d2\u0000\u05f7"+
		"\u05f9\u0003\u0106\u0083\u0000\u05f8\u05f6\u0001\u0000\u0000\u0000\u05f8"+
		"\u05f7\u0001\u0000\u0000\u0000\u05f9\u05fc\u0001\u0000\u0000\u0000\u05fa"+
		"\u05f8\u0001\u0000\u0000\u0000\u05fa\u05fb\u0001\u0000\u0000\u0000\u05fb"+
		"\u05fe\u0001\u0000\u0000\u0000\u05fc\u05fa\u0001\u0000\u0000\u0000\u05fd"+
		"\u05ff\u0003\u0108\u0084\u0000\u05fe\u05fd\u0001\u0000\u0000\u0000\u05fe"+
		"\u05ff\u0001\u0000\u0000\u0000\u05ff\u0105\u0001\u0000\u0000\u0000\u0600"+
		"\u0601\u0003.\u0017\u0000\u0601\u0602\u0005V\u0000\u0000\u0602\u0603\u0003"+
		"L&\u0000\u0603\u0107\u0001\u0000\u0000\u0000\u0604\u0605\u0003.\u0017"+
		"\u0000\u0605\u0606\u0003\u0118\u008c\u0000\u0606\u0109\u0001\u0000\u0000"+
		"\u0000\u0607\u0608\u0003\u0092I\u0000\u0608\u060a\u0005\"\u0000\u0000"+
		"\u0609\u060b\u0003\u009aM\u0000\u060a\u0609\u0001\u0000\u0000\u0000\u060a"+
		"\u060b\u0001\u0000\u0000\u0000\u060b\u060d\u0001\u0000\u0000\u0000\u060c"+
		"\u060e\u0003\u01e4\u00f2\u0000\u060d\u060c\u0001\u0000\u0000\u0000\u060d"+
		"\u060e\u0001\u0000\u0000\u0000\u060e\u060f\u0001\u0000\u0000\u0000\u060f"+
		"\u0610\u0003\u0102\u0081\u0000\u0610\u010b\u0001\u0000\u0000\u0000\u0611"+
		"\u0612\u0003P(\u0000\u0612\u0613\u0005L\u0000\u0000\u0613\u0614\u0003"+
		"\u0084B\u0000\u0614\u0615\u0003\u0102\u0081\u0000\u0615\u010d\u0001\u0000"+
		"\u0000\u0000\u0616\u0617\u0003\u0092I\u0000\u0617\u0619\u0005\n\u0000"+
		"\u0000\u0618\u061a\u0003\u009aM\u0000\u0619\u0618\u0001\u0000\u0000\u0000"+
		"\u0619\u061a\u0001\u0000\u0000\u0000\u061a\u061c\u0001\u0000\u0000\u0000"+
		"\u061b\u061d\u0003\u01e4\u00f2\u0000\u061c\u061b\u0001\u0000\u0000\u0000"+
		"\u061c\u061d\u0001\u0000\u0000\u0000\u061d\u061e\u0001\u0000\u0000\u0000"+
		"\u061e\u061f\u0003\u0102\u0081\u0000\u061f\u010f\u0001\u0000\u0000\u0000"+
		"\u0620\u0621\u0003\u0092I\u0000\u0621\u0624\u00055\u0000\u0000\u0622\u0625"+
		"\u0005c\u0000\u0000\u0623\u0625\u0005#\u0000\u0000\u0624\u0622\u0001\u0000"+
		"\u0000\u0000\u0624\u0623\u0001\u0000\u0000\u0000\u0624\u0625\u0001\u0000"+
		"\u0000\u0000\u0625\u0627\u0001\u0000\u0000\u0000\u0626\u0628\u0003\u009a"+
		"M\u0000\u0627\u0626\u0001\u0000\u0000\u0000\u0627\u0628\u0001\u0000\u0000"+
		"\u0000\u0628\u062a\u0001\u0000\u0000\u0000\u0629\u062b\u0003\u01e4\u00f2"+
		"\u0000\u062a\u0629\u0001\u0000\u0000\u0000\u062a\u062b\u0001\u0000\u0000"+
		"\u0000\u062b\u062c\u0001\u0000\u0000\u0000\u062c\u062d\u0003\u0102\u0081"+
		"\u0000\u062d\u0111\u0001\u0000\u0000\u0000\u062e\u062f\u0003\u0114\u008a"+
		"\u0000\u062f\u0113\u0001\u0000\u0000\u0000\u0630\u0631\u0003\u0116\u008b"+
		"\u0000\u0631\u0115\u0001\u0000\u0000\u0000\u0632\u0633\u0003\u0118\u008c"+
		"\u0000\u0633\u0117\u0001\u0000\u0000\u0000\u0634\u0635\u0006\u008c\uffff"+
		"\uffff\u0000\u0635\u0636\u0005.\u0000\u0000\u0636\u0637\u0003\u0118\u008c"+
		"\u0000\u0637\u0638\u0005\u0096\u0000\u0000\u0638\u0639\u0003\u0118\u008c"+
		"\u0000\u0639\u063a\u0005\u001f\u0000\u0000\u063a\u063b\u0003\u0118\u008c"+
		"\u0000\u063b\u063c\u0003\u014e\u00a7\u0000\u063c\u0657\u0001\u0000\u0000"+
		"\u0000\u063d\u063e\u0003\u0122\u0091\u0000\u063e\u063f\u0003\u0118\u008c"+
		"\u0000\u063f\u0640\u0003\u014e\u00a7\u0000\u0640\u0657\u0001\u0000\u0000"+
		"\u0000\u0641\u0642\u0003\u0126\u0093\u0000\u0642\u0643\u0003\u0146\u00a3"+
		"\u0000\u0643\u0644\u0003\u014e\u00a7\u0000\u0644\u0657\u0001\u0000\u0000"+
		"\u0000\u0645\u0646\u0003\u0128\u0094\u0000\u0646\u0647\u0003\u0148\u00a4"+
		"\u0000\u0647\u0648\u0003\u014e\u00a7\u0000\u0648\u0657\u0001\u0000\u0000"+
		"\u0000\u0649\u064a\u0003\u0198\u00cc\u0000\u064a\u064b\u0003\u0140\u00a0"+
		"\u0000\u064b\u064c\u0003\u0146\u00a3\u0000\u064c\u064d\u0003\u014e\u00a7"+
		"\u0000\u064d\u0657\u0001\u0000\u0000\u0000\u064e\u064f\u0003\u0198\u00cc"+
		"\u0000\u064f\u0650\u0003\u0142\u00a1\u0000\u0650\u0651\u0003\u0148\u00a4"+
		"\u0000\u0651\u0652\u0003\u014e\u00a7\u0000\u0652\u0657\u0001\u0000\u0000"+
		"\u0000\u0653\u0654\u0005\u0004\u0000\u0000\u0654\u0657\u0003\u0146\u00a3"+
		"\u0000\u0655\u0657\u0003\u0152\u00a9\u0000\u0656\u0634\u0001\u0000\u0000"+
		"\u0000\u0656\u063d\u0001\u0000\u0000\u0000\u0656\u0641\u0001\u0000\u0000"+
		"\u0000\u0656\u0645\u0001\u0000\u0000\u0000\u0656\u0649\u0001\u0000\u0000"+
		"\u0000\u0656\u064e\u0001\u0000\u0000\u0000\u0656\u0653\u0001\u0000\u0000"+
		"\u0000\u0656\u0655\u0001\u0000\u0000\u0000\u0657\u066c\u0001\u0000\u0000"+
		"\u0000\u0658\u0659\n\u000b\u0000\u0000\u0659\u065a\u0003\u011a\u008d\u0000"+
		"\u065a\u065b\u0003\u0118\u008c\f\u065b\u066b\u0001\u0000\u0000\u0000\u065c"+
		"\u065d\n\n\u0000\u0000\u065d\u065e\u0003\u011e\u008f\u0000\u065e\u065f"+
		"\u0003\u0118\u008c\u000b\u065f\u066b\u0001\u0000\u0000\u0000\u0660\u0661"+
		"\n\u0007\u0000\u0000\u0661\u0662\u0003\u0126\u0093\u0000\u0662\u0663\u0003"+
		"\u0146\u00a3\u0000\u0663\u0664\u0003\u014e\u00a7\u0000\u0664\u066b\u0001"+
		"\u0000\u0000\u0000\u0665\u0666\n\u0005\u0000\u0000\u0666\u0667\u0003\u0128"+
		"\u0094\u0000\u0667\u0668\u0003\u0148\u00a4\u0000\u0668\u0669\u0003\u014e"+
		"\u00a7\u0000\u0669\u066b\u0001\u0000\u0000\u0000\u066a\u0658\u0001\u0000"+
		"\u0000\u0000\u066a\u065c\u0001\u0000\u0000\u0000\u066a\u0660\u0001\u0000"+
		"\u0000\u0000\u066a\u0665\u0001\u0000\u0000\u0000\u066b\u066e\u0001\u0000"+
		"\u0000\u0000\u066c\u066a\u0001\u0000\u0000\u0000\u066c\u066d\u0001\u0000"+
		"\u0000\u0000\u066d\u0119\u0001\u0000\u0000\u0000\u066e\u066c\u0001\u0000"+
		"\u0000\u0000\u066f\u0670\u0007\b\u0000\u0000\u0670\u011b\u0001\u0000\u0000"+
		"\u0000\u0671\u0672\u0003\u012c\u0096\u0000\u0672\u0673\u0003\u011e\u008f"+
		"\u0000\u0673\u0674\u0003\u012c\u0096\u0000\u0674\u0675\u0003\u014e\u00a7"+
		"\u0000\u0675\u011d\u0001\u0000\u0000\u0000\u0676\u0677\u0007\t\u0000\u0000"+
		"\u0677\u011f\u0001\u0000\u0000\u0000\u0678\u0679\u0003\u0122\u0091\u0000"+
		"\u0679\u067a\u0003\u012c\u0096\u0000\u067a\u067b\u0003\u014e\u00a7\u0000"+
		"\u067b\u0121\u0001\u0000\u0000\u0000\u067c\u067d\u0007\n\u0000\u0000\u067d"+
		"\u0123\u0001\u0000\u0000\u0000\u067e\u0680\u0003\u012c\u0096\u0000\u067f"+
		"\u067e\u0001\u0000\u0000\u0000\u067f\u0680\u0001\u0000\u0000\u0000\u0680"+
		"\u0687\u0001\u0000\u0000\u0000\u0681\u0682\u0003\u0126\u0093\u0000\u0682"+
		"\u0683\u0003\u0146\u00a3\u0000\u0683\u0688\u0001\u0000\u0000\u0000\u0684"+
		"\u0685\u0003\u0128\u0094\u0000\u0685\u0686\u0003\u0148\u00a4\u0000\u0686"+
		"\u0688\u0001\u0000\u0000\u0000\u0687\u0681\u0001\u0000\u0000\u0000\u0687"+
		"\u0684\u0001\u0000\u0000\u0000\u0688\u0689\u0001\u0000\u0000\u0000\u0689"+
		"\u068a\u0003\u014e\u00a7\u0000\u068a\u0125\u0001\u0000\u0000\u0000\u068b"+
		"\u068c\u0007\u000b\u0000\u0000\u068c\u0127\u0001\u0000\u0000\u0000\u068d"+
		"\u068e\u0005\u0006\u0000\u0000\u068e\u0129\u0001\u0000\u0000\u0000\u068f"+
		"\u0696\u0003\u0138\u009c\u0000\u0690\u0691\u0003\u0140\u00a0\u0000\u0691"+
		"\u0692\u0003\u0146\u00a3\u0000\u0692\u0697\u0001\u0000\u0000\u0000\u0693"+
		"\u0694\u0003\u0142\u00a1\u0000\u0694\u0695\u0003\u0148\u00a4\u0000\u0695"+
		"\u0697\u0001\u0000\u0000\u0000\u0696\u0690\u0001\u0000\u0000\u0000\u0696"+
		"\u0693\u0001\u0000\u0000\u0000\u0697\u0698\u0001\u0000\u0000\u0000\u0698"+
		"\u0699\u0003\u014e\u00a7\u0000\u0699\u012b\u0001\u0000\u0000\u0000\u069a"+
		"\u069b\u0003\u012e\u0097\u0000\u069b\u012d\u0001\u0000\u0000\u0000\u069c"+
		"\u069d\u0003\u0130\u0098\u0000\u069d\u012f\u0001\u0000\u0000\u0000\u069e"+
		"\u069f\u0003\u0118\u008c\u0000\u069f\u0131\u0001\u0000\u0000\u0000\u06a0"+
		"\u06a1\u0003\u0134\u009a\u0000\u06a1\u0133\u0001\u0000\u0000\u0000\u06a2"+
		"\u06a3\u0003\u0136\u009b\u0000\u06a3\u0135\u0001\u0000\u0000\u0000\u06a4"+
		"\u06a5\u0003\u0114\u008a\u0000\u06a5\u0137\u0001\u0000\u0000\u0000\u06a6"+
		"\u06a7\u0003\u013a\u009d\u0000\u06a7\u0139\u0001\u0000\u0000\u0000\u06a8"+
		"\u06a9\u0003\u013c\u009e\u0000\u06a9\u013b\u0001\u0000\u0000\u0000\u06aa"+
		"\u06ab\u0003\u013e\u009f\u0000\u06ab\u013d\u0001\u0000\u0000\u0000\u06ac"+
		"\u06ad\u0003\u019a\u00cd\u0000\u06ad\u013f\u0001\u0000\u0000\u0000\u06ae"+
		"\u06af\u0005\u0086\u0000\u0000\u06af\u06b0\u0005\u0086\u0000\u0000\u06b0"+
		"\u0141\u0001\u0000\u0000\u0000\u06b1\u06b2\u0005=\u0000\u0000\u06b2\u0143"+
		"\u0001\u0000\u0000\u0000\u06b3\u06b4\u0005\u0004\u0000\u0000\u06b4\u06b5"+
		"\u0003\u0146\u00a3\u0000\u06b5\u0145\u0001\u0000\u0000\u0000\u06b6\u06b7"+
		"\u0003\u014a\u00a5\u0000\u06b7\u0147\u0001\u0000\u0000\u0000\u06b8\u06b9"+
		"\u0003\u014a\u00a5\u0000\u06b9\u0149\u0001\u0000\u0000\u0000\u06ba\u06bb"+
		"\u0003\u014c\u00a6\u0000\u06bb\u014b\u0001\u0000\u0000\u0000\u06bc\u06bd"+
		"\u0003:\u001d\u0000\u06bd\u014d\u0001\u0000\u0000\u0000\u06be\u06bf\u0003"+
		"\u0150\u00a8\u0000\u06bf\u014f\u0001\u0000\u0000\u0000\u06c0\u06c1\u0001"+
		"\u0000\u0000\u0000\u06c1\u0151\u0001\u0000\u0000\u0000\u06c2\u06c3\u0006"+
		"\u00a9\uffff\uffff\u0000\u06c3\u06c4\u0005}\u0000\u0000\u06c4\u06c5\u0003"+
		"\u0168\u00b4\u0000\u06c5\u06c6\u0005~\u0000\u0000\u06c6\u06c9\u0001\u0000"+
		"\u0000\u0000\u06c7\u06c9\u0003\u018e\u00c7\u0000\u06c8\u06c2\u0001\u0000"+
		"\u0000\u0000\u06c8\u06c7\u0001\u0000\u0000\u0000\u06c9\u06ea\u0001\u0000"+
		"\u0000\u0000\u06ca\u06cb\n\b\u0000\u0000\u06cb\u06cc\u0005\u0081\u0000"+
		"\u0000\u06cc\u06cd\u0003\u016c\u00b6\u0000\u06cd\u06ce\u0005\u0082\u0000"+
		"\u0000\u06ce\u06e9\u0001\u0000\u0000\u0000\u06cf\u06d0\n\u0007\u0000\u0000"+
		"\u06d0\u06d1\u0005\u0087\u0000\u0000\u06d1\u06d2\u0005}\u0000\u0000\u06d2"+
		"\u06d3\u0003\u016c\u00b6\u0000\u06d3\u06d4\u0005~\u0000\u0000\u06d4\u06e9"+
		"\u0001\u0000\u0000\u0000\u06d5\u06d6\n\u0006\u0000\u0000\u06d6\u06d7\u0005"+
		"\u0091\u0000\u0000\u06d7\u06e9\u0003\u0188\u00c4\u0000\u06d8\u06d9\n\u0005"+
		"\u0000\u0000\u06d9\u06da\u0005\u0091\u0000\u0000\u06da\u06e9\u0003\u0176"+
		"\u00bb\u0000\u06db\u06dc\n\u0004\u0000\u0000\u06dc\u06dd\u0005|\u0000"+
		"\u0000\u06dd\u06e9\u0003\u0176\u00bb\u0000\u06de\u06df\n\u0003\u0000\u0000"+
		"\u06df\u06e0\u0005x\u0000\u0000\u06e0\u06e4\u0003\u018a\u00c5\u0000\u06e1"+
		"\u06e5\u0003\u0176\u00bb\u0000\u06e2\u06e5\u0003\u017c\u00be\u0000\u06e3"+
		"\u06e5\u0003\u01aa\u00d5\u0000\u06e4\u06e1\u0001\u0000\u0000\u0000\u06e4"+
		"\u06e2\u0001\u0000\u0000\u0000\u06e4\u06e3\u0001\u0000\u0000\u0000\u06e5"+
		"\u06e6\u0001\u0000\u0000\u0000\u06e6\u06e7\u0003\u014e\u00a7\u0000\u06e7"+
		"\u06e9\u0001\u0000\u0000\u0000\u06e8\u06ca\u0001\u0000\u0000\u0000\u06e8"+
		"\u06cf\u0001\u0000\u0000\u0000\u06e8\u06d5\u0001\u0000\u0000\u0000\u06e8"+
		"\u06d8\u0001\u0000\u0000\u0000\u06e8\u06db\u0001\u0000\u0000\u0000\u06e8"+
		"\u06de\u0001\u0000\u0000\u0000\u06e9\u06ec\u0001\u0000\u0000\u0000\u06ea"+
		"\u06e8\u0001\u0000\u0000\u0000\u06ea\u06eb\u0001\u0000\u0000\u0000\u06eb"+
		"\u0153\u0001\u0000\u0000\u0000\u06ec\u06ea\u0001\u0000\u0000\u0000\u06ed"+
		"\u06ee\u0003\u0152\u00a9\u0000\u06ee\u0155\u0001\u0000\u0000\u0000\u06ef"+
		"\u06f0\u0003\u0154\u00aa\u0000\u06f0\u0157\u0001\u0000\u0000\u0000\u06f1"+
		"\u06f2\u0003\u0156\u00ab\u0000\u06f2\u0159\u0001\u0000\u0000\u0000\u06f3"+
		"\u06fb\u0003\u0162\u00b1\u0000\u06f4\u06fb\u0003\u0164\u00b2\u0000\u06f5"+
		"\u06fb\u0003\u0166\u00b3\u0000\u06f6\u06fb\u0003\u0172\u00b9\u0000\u06f7"+
		"\u06fb\u0003\u0170\u00b8\u0000\u06f8\u06fb\u0003\u0174\u00ba\u0000\u06f9"+
		"\u06fb\u0003\u018e\u00c7\u0000\u06fa\u06f3\u0001\u0000\u0000\u0000\u06fa"+
		"\u06f4\u0001\u0000\u0000\u0000\u06fa\u06f5\u0001\u0000\u0000\u0000\u06fa"+
		"\u06f6\u0001\u0000\u0000\u0000\u06fa\u06f7\u0001\u0000\u0000\u0000\u06fa"+
		"\u06f8\u0001\u0000\u0000\u0000\u06fa\u06f9\u0001\u0000\u0000\u0000\u06fb"+
		"\u015b\u0001\u0000\u0000\u0000\u06fc\u06fd\u0003\u015a\u00ad\u0000\u06fd"+
		"\u015d\u0001\u0000\u0000\u0000\u06fe\u06ff\u0003\u015c\u00ae\u0000\u06ff"+
		"\u015f\u0001\u0000\u0000\u0000\u0700\u0701\u0003\u0156\u00ab\u0000\u0701"+
		"\u0161\u0001\u0000\u0000\u0000\u0702\u0703\u0003\u0158\u00ac\u0000\u0703"+
		"\u0704\u0005\u0081\u0000\u0000\u0704\u0705\u0003\u016c\u00b6\u0000\u0705"+
		"\u0706\u0005\u0082\u0000\u0000\u0706\u0163\u0001\u0000\u0000\u0000\u0707"+
		"\u0708\u0003\u0158\u00ac\u0000\u0708\u0709\u0005\u0087\u0000\u0000\u0709"+
		"\u070a\u0005}\u0000\u0000\u070a\u070b\u0003\u016c\u00b6\u0000\u070b\u070c"+
		"\u0005~\u0000\u0000\u070c\u0165\u0001\u0000\u0000\u0000\u070d\u070e\u0005"+
		"}\u0000\u0000\u070e\u070f\u0003\u0168\u00b4\u0000\u070f\u0710\u0005~\u0000"+
		"\u0000\u0710\u0167\u0001\u0000\u0000\u0000\u0711\u0713\u0003\u0118\u008c"+
		"\u0000\u0712\u0714\u0005\u0084\u0000\u0000\u0713\u0712\u0001\u0000\u0000"+
		"\u0000\u0713\u0714\u0001\u0000\u0000\u0000\u0714\u0717\u0001\u0000\u0000"+
		"\u0000\u0715\u0717\u0003\u016a\u00b5\u0000\u0716\u0711\u0001\u0000\u0000"+
		"\u0000\u0716\u0715\u0001\u0000\u0000\u0000\u0717\u0169\u0001\u0000\u0000"+
		"\u0000\u0718\u0719\u0003\u0116\u008b\u0000\u0719\u071a\u0005\u0084\u0000"+
		"\u0000\u071a\u071b\u0003\u016c\u00b6\u0000\u071b\u016b\u0001\u0000\u0000"+
		"\u0000\u071c\u071d\u0003\u0168\u00b4\u0000\u071d\u016d\u0001\u0000\u0000"+
		"\u0000\u071e\u071f\u0003\u0160\u00b0\u0000\u071f\u0720\u0005\u0091\u0000"+
		"\u0000\u0720\u0721\u0003\u0188\u00c4\u0000\u0721\u016f\u0001\u0000\u0000"+
		"\u0000\u0722\u0723\u0003\u0158\u00ac\u0000\u0723\u0724\u0005\u0091\u0000"+
		"\u0000\u0724\u0725\u0003\u0176\u00bb\u0000\u0725\u0171\u0001\u0000\u0000"+
		"\u0000\u0726\u0727\u0003\u0158\u00ac\u0000\u0727\u0728\u0005|\u0000\u0000"+
		"\u0728\u0729\u0003\u0176\u00bb\u0000\u0729\u0173\u0001\u0000\u0000\u0000"+
		"\u072a\u072b\u0003\u0158\u00ac\u0000\u072b\u072c\u0005x\u0000\u0000\u072c"+
		"\u0730\u0003\u018a\u00c5\u0000\u072d\u0731\u0003\u0176\u00bb\u0000\u072e"+
		"\u0731\u0003\u017c\u00be\u0000\u072f\u0731\u0003\u01aa\u00d5\u0000\u0730"+
		"\u072d\u0001\u0000\u0000\u0000\u0730\u072e\u0001\u0000\u0000\u0000\u0730"+
		"\u072f\u0001\u0000\u0000\u0000\u0731\u0732\u0001\u0000\u0000\u0000\u0732"+
		"\u0733\u0003\u014e\u00a7\u0000\u0733\u0175\u0001\u0000\u0000\u0000\u0734"+
		"\u0735\u0003\u0178\u00bc\u0000\u0735\u0177\u0001\u0000\u0000\u0000\u0736"+
		"\u0737\u0003\u017a\u00bd\u0000\u0737\u0179\u0001\u0000\u0000\u0000\u0738"+
		"\u0739\u0003\u01b6\u00db\u0000\u0739\u017b\u0001\u0000\u0000\u0000\u073a"+
		"\u073b\u0003\u017e\u00bf\u0000\u073b\u017d\u0001\u0000\u0000\u0000\u073c"+
		"\u073d\u0003\u0180\u00c0\u0000\u073d\u017f\u0001\u0000\u0000\u0000\u073e"+
		"\u073f\u0003\u0182\u00c1\u0000\u073f\u0181\u0001\u0000\u0000\u0000\u0740"+
		"\u0741\u0003\u0184\u00c2\u0000\u0741\u0183\u0001\u0000\u0000\u0000\u0742"+
		"\u0743\u0003\u0186\u00c3\u0000\u0743\u0185\u0001\u0000\u0000\u0000\u0744"+
		"\u0745\u0003\u014c\u00a6\u0000\u0745\u0187\u0001\u0000\u0000\u0000\u0746"+
		"\u0749\u0003\u0194\u00ca\u0000\u0747\u0749\u0003\u01a8\u00d4\u0000\u0748"+
		"\u0746\u0001\u0000\u0000\u0000\u0748\u0747\u0001\u0000\u0000\u0000\u0749"+
		"\u0189\u0001\u0000\u0000\u0000\u074a\u074b\u0003\u018c\u00c6\u0000\u074b"+
		"\u018b\u0001\u0000\u0000\u0000\u074c\u074d\u0003\u00be_\u0000\u074d\u018d"+
		"\u0001\u0000\u0000\u0000\u074e\u0756\u0003\u0190\u00c8\u0000\u074f\u0756"+
		"\u0003\u01bc\u00de\u0000\u0750\u0756\u0003\u0192\u00c9\u0000\u0751\u0756"+
		"\u0003\u0198\u00cc\u0000\u0752\u0756\u0003\u019c\u00ce\u0000\u0753\u0756"+
		"\u0003\u019e\u00cf\u0000\u0754\u0756\u0003\u01b6\u00db\u0000\u0755\u074e"+
		"\u0001\u0000\u0000\u0000\u0755\u074f\u0001\u0000\u0000\u0000\u0755\u0750"+
		"\u0001\u0000\u0000\u0000\u0755\u0751\u0001\u0000\u0000\u0000\u0755\u0752"+
		"\u0001\u0000\u0000\u0000\u0755\u0753\u0001\u0000\u0000\u0000\u0755\u0754"+
		"\u0001\u0000\u0000\u0000\u0756\u018f\u0001\u0000\u0000\u0000\u0757\u075b"+
		"\u0005E\u0000\u0000\u0758\u0759\u0005}\u0000\u0000\u0759\u075b\u0005~"+
		"\u0000\u0000\u075a\u0757\u0001\u0000\u0000\u0000\u075a\u0758\u0001\u0000"+
		"\u0000\u0000\u075b\u0191\u0001\u0000\u0000\u0000\u075c\u075d\u0003\u0194"+
		"\u00ca\u0000\u075d\u075e\u0003\u014e\u00a7\u0000\u075e\u0193\u0001\u0000"+
		"\u0000\u0000\u075f\u0760\u0003\u0196\u00cb\u0000\u0760\u0195\u0001\u0000"+
		"\u0000\u0000\u0761\u0762\u0003:\u001d\u0000\u0762\u0197\u0001\u0000\u0000"+
		"\u0000\u0763\u0764\u0003\u019a\u00cd\u0000\u0764\u0765\u0005\u0091\u0000"+
		"\u0000\u0765\u0766\u0005?\u0000\u0000\u0766\u0199\u0001\u0000\u0000\u0000"+
		"\u0767\u0768\u0003:\u001d\u0000\u0768\u019b\u0001\u0000\u0000\u0000\u0769"+
		"\u076a\u0003\u01a4\u00d2\u0000\u076a\u076b\u0003\u01aa\u00d5\u0000\u076b"+
		"\u076c\u0003\u014e\u00a7\u0000\u076c\u019d\u0001\u0000\u0000\u0000\u076d"+
		"\u076e\u0005B\u0000\u0000\u076e\u076f\u0003\u01a4\u00d2\u0000\u076f\u0770"+
		"\u0003\u01a0\u00d0\u0000\u0770\u019f\u0001\u0000\u0000\u0000\u0771\u0772"+
		"\u0003\u01a2\u00d1\u0000\u0772\u01a1\u0001\u0000\u0000\u0000\u0773\u0774"+
		"\u0003\u01aa\u00d5\u0000\u0774\u01a3\u0001\u0000\u0000\u0000\u0775\u0778"+
		"\u0003\u01a6\u00d3\u0000\u0776\u0778\u0003\u01a8\u00d4\u0000\u0777\u0775"+
		"\u0001\u0000\u0000\u0000\u0777\u0776\u0001\u0000\u0000\u0000\u0778\u01a5"+
		"\u0001\u0000\u0000\u0000\u0779\u077a\u0003:\u001d\u0000\u077a\u01a7\u0001"+
		"\u0000\u0000\u0000\u077b\u077c\u0003\u00ccf\u0000\u077c\u01a9\u0001\u0000"+
		"\u0000\u0000\u077d\u0780\u0005}\u0000\u0000\u077e\u0781\u0003\u01ac\u00d6"+
		"\u0000\u077f\u0781\u0003\u01ae\u00d7\u0000\u0780\u077e\u0001\u0000\u0000"+
		"\u0000\u0780\u077f\u0001\u0000\u0000\u0000\u0780\u0781\u0001\u0000\u0000"+
		"\u0000\u0781\u0782\u0001\u0000\u0000\u0000\u0782\u0783\u0005~\u0000\u0000"+
		"\u0783\u01ab\u0001\u0000\u0000\u0000\u0784\u0789\u0003\u012c\u0096\u0000"+
		"\u0785\u0786\u0005\u0084\u0000\u0000\u0786\u0788\u0003\u012c\u0096\u0000"+
		"\u0787\u0785\u0001\u0000\u0000\u0000\u0788\u078b\u0001\u0000\u0000\u0000"+
		"\u0789\u0787\u0001\u0000\u0000\u0000\u0789\u078a\u0001\u0000\u0000\u0000"+
		"\u078a\u01ad\u0001\u0000\u0000\u0000\u078b\u0789\u0001\u0000\u0000\u0000"+
		"\u078c\u0791\u0003\u01b0\u00d8\u0000\u078d\u078e\u0005\u0084\u0000\u0000"+
		"\u078e\u0790\u0003\u01b0\u00d8\u0000\u078f\u078d\u0001\u0000\u0000\u0000"+
		"\u0790\u0793\u0001\u0000\u0000\u0000\u0791\u078f\u0001\u0000\u0000\u0000"+
		"\u0791\u0792\u0001\u0000\u0000\u0000\u0792\u01af\u0001\u0000\u0000\u0000"+
		"\u0793\u0791\u0001\u0000\u0000\u0000\u0794\u0795\u0003\u01b2\u00d9\u0000"+
		"\u0795\u01b1\u0001\u0000\u0000\u0000\u0796\u0797\u0003\u01b4\u00da\u0000"+
		"\u0797\u0798\u0005\u0094\u0000\u0000\u0798\u0799\u0003\u0130\u0098\u0000"+
		"\u0799\u01b3\u0001\u0000\u0000\u0000\u079a\u079b\u0003:\u001d\u0000\u079b"+
		"\u01b5\u0001\u0000\u0000\u0000\u079c\u079d\u0003\u01b8\u00dc\u0000\u079d"+
		"\u01b7\u0001\u0000\u0000\u0000\u079e\u079f\u0003\u01ba\u00dd\u0000\u079f"+
		"\u01b9\u0001\u0000\u0000\u0000\u07a0\u07a1\u0005\u007f\u0000\u0000\u07a1"+
		"\u07a2\u0003\u0104\u0082\u0000\u07a2\u07a3\u0005\u0080\u0000\u0000\u07a3"+
		"\u01bb\u0001\u0000\u0000\u0000\u07a4\u07aa\u0003\u01be\u00df\u0000\u07a5"+
		"\u07aa\u0003\u01c2\u00e1\u0000\u07a6\u07aa\u0003\u01c4\u00e2\u0000\u07a7"+
		"\u07aa\u0003\u01c6\u00e3\u0000\u07a8\u07aa\u0003\u01ca\u00e5\u0000\u07a9"+
		"\u07a4\u0001\u0000\u0000\u0000\u07a9\u07a5\u0001\u0000\u0000\u0000\u07a9"+
		"\u07a6\u0001\u0000\u0000\u0000\u07a9\u07a7\u0001\u0000\u0000\u0000\u07a9"+
		"\u07a8\u0001\u0000\u0000\u0000\u07aa\u01bd\u0001\u0000\u0000\u0000\u07ab"+
		"\u07ac\u0003\u01c0\u00e0\u0000\u07ac\u01bf\u0001\u0000\u0000\u0000\u07ad"+
		"\u07ae\u0007\f\u0000\u0000\u07ae\u01c1\u0001\u0000\u0000\u0000\u07af\u07b0"+
		"\u0005\u009e\u0000\u0000\u07b0\u01c3\u0001\u0000\u0000\u0000\u07b1\u07b2"+
		"\u0005\u009c\u0000\u0000\u07b2\u01c5\u0001\u0000\u0000\u0000\u07b3\u07b4"+
		"\u0003\u01c8\u00e4\u0000\u07b4\u01c7\u0001\u0000\u0000\u0000\u07b5\u07b7"+
		"\u0005\u009c\u0000\u0000\u07b6\u07b5\u0001\u0000\u0000\u0000\u07b6\u07b7"+
		"\u0001\u0000\u0000\u0000\u07b7\u07b8\u0001\u0000\u0000\u0000\u07b8\u07b9"+
		"\u0005\u0091\u0000\u0000\u07b9\u07bc\u0007\r\u0000\u0000\u07ba\u07bc\u0005"+
		"\u009d\u0000\u0000\u07bb\u07b6\u0001\u0000\u0000\u0000\u07bb\u07ba\u0001"+
		"\u0000\u0000\u0000\u07bc\u01c9\u0001\u0000\u0000\u0000\u07bd\u07be\u0005"+
		"\u008c\u0000\u0000\u07be\u01cb\u0001\u0000\u0000\u0000\u07bf\u07c0\u0003"+
		"P(\u0000\u07c0\u07c1\u00053\u0000\u0000\u07c1\u07c2\u0003\u0084B\u0000"+
		"\u07c2\u07c3\u0003b1\u0000\u07c3\u01cd\u0001\u0000\u0000\u0000\u07c4\u07c5"+
		"\u0003\u0092I\u0000\u07c5\u07c6\u0005)\u0000\u0000\u07c6\u07c7\u0003\u01d2"+
		"\u00e9\u0000\u07c7\u07c8\u0003b1\u0000\u07c8\u01cf\u0001\u0000\u0000\u0000"+
		"\u07c9\u07ca\u0003\u0092I\u0000\u07ca\u07cb\u0005`\u0000\u0000\u07cb\u07cc"+
		"\u0005)\u0000\u0000\u07cc\u07cd\u0003\u01d2\u00e9\u0000\u07cd\u07ce\u0003"+
		"b1\u0000\u07ce\u01d1\u0001\u0000\u0000\u0000\u07cf\u07d1\u0003\u009aM"+
		"\u0000\u07d0\u07d2\u0003\u01e4\u00f2\u0000\u07d1\u07d0\u0001\u0000\u0000"+
		"\u0000\u07d1\u07d2\u0001\u0000\u0000\u0000\u07d2\u07d5\u0001\u0000\u0000"+
		"\u0000\u07d3\u07d4\u0005F\u0000\u0000\u07d4\u07d6\u0003\u01d4\u00ea\u0000"+
		"\u07d5\u07d3\u0001\u0000\u0000\u0000\u07d5\u07d6\u0001\u0000\u0000\u0000"+
		"\u07d6\u07dc\u0001\u0000\u0000\u0000\u07d7\u07d8\u0005+\u0000\u0000\u07d8"+
		"\u07d9\u0003\u01da\u00ed\u0000\u07d9\u07da\u0005b\u0000\u0000\u07da\u07db"+
		"\u0003\u01da\u00ed\u0000\u07db\u07dd\u0001\u0000\u0000\u0000\u07dc\u07d7"+
		"\u0001\u0000\u0000\u0000\u07dc\u07dd\u0001\u0000\u0000\u0000\u07dd\u07e6"+
		"\u0001\u0000\u0000\u0000\u07de\u07e0\u0005\u0004\u0000\u0000\u07df\u07de"+
		"\u0001\u0000\u0000\u0000\u07df\u07e0\u0001\u0000\u0000\u0000\u07e0\u07e1"+
		"\u0001\u0000\u0000\u0000\u07e1\u07e2\u0003\u01da\u00ed\u0000\u07e2\u07e3"+
		"\u0005b\u0000\u0000\u07e3\u07e4\u0003\u01da\u00ed\u0000\u07e4\u07e6\u0001"+
		"\u0000\u0000\u0000\u07e5\u07cf\u0001\u0000\u0000\u0000\u07e5\u07df\u0001"+
		"\u0000\u0000\u0000\u07e6\u01d3\u0001\u0000\u0000\u0000\u07e7\u07e8\u0003"+
		"\u01d6\u00eb\u0000\u07e8\u01d5\u0001\u0000\u0000\u0000\u07e9\u07ea\u0003"+
		"\u000e\u0007\u0000\u07ea\u07ec\u0003\u01d8\u00ec\u0000\u07eb\u07ed\u0003"+
		"\u01e4\u00f2\u0000\u07ec\u07eb\u0001\u0000\u0000\u0000\u07ec\u07ed\u0001"+
		"\u0000\u0000\u0000\u07ed\u07fc\u0001\u0000\u0000\u0000\u07ee\u07ef\u0003"+
		"\u000e\u0007\u0000\u07ef\u07f0\u0003\u01e4\u00f2\u0000\u07f0\u07fc\u0001"+
		"\u0000\u0000\u0000\u07f1\u07f3\u0003\u00be_\u0000\u07f2\u07f4\u0003\u01ee"+
		"\u00f7\u0000\u07f3\u07f2\u0001\u0000\u0000\u0000\u07f3\u07f4\u0001\u0000"+
		"\u0000\u0000\u07f4\u07fa\u0001\u0000\u0000\u0000\u07f5\u07f7\u0003\u01ee"+
		"\u00f7\u0000\u07f6\u07f8\u0003\u00be_\u0000\u07f7\u07f6\u0001\u0000\u0000"+
		"\u0000\u07f7\u07f8\u0001\u0000\u0000\u0000\u07f8\u07fa\u0001\u0000\u0000"+
		"\u0000\u07f9\u07f1\u0001\u0000\u0000\u0000\u07f9\u07f5\u0001\u0000\u0000"+
		"\u0000\u07fa\u07fc\u0001\u0000\u0000\u0000\u07fb\u07e9\u0001\u0000\u0000"+
		"\u0000\u07fb\u07ee\u0001\u0000\u0000\u0000\u07fb\u07f9\u0001\u0000\u0000"+
		"\u0000\u07fc\u01d7\u0001\u0000\u0000\u0000\u07fd\u07ff\u0003\u00aaU\u0000"+
		"\u07fe\u07fd\u0001\u0000\u0000\u0000\u07ff\u0800\u0001\u0000\u0000\u0000"+
		"\u0800\u07fe\u0001\u0000\u0000\u0000\u0800\u0801\u0001\u0000\u0000\u0000"+
		"\u0801\u0803\u0001\u0000\u0000\u0000\u0802\u0804\u0003\u00a8T\u0000\u0803"+
		"\u0802\u0001\u0000\u0000\u0000\u0803\u0804\u0001\u0000\u0000\u0000\u0804"+
		"\u0808\u0001\u0000\u0000\u0000\u0805\u0807\u0003\u00aaU\u0000\u0806\u0805"+
		"\u0001\u0000\u0000\u0000\u0807\u080a\u0001\u0000\u0000\u0000\u0808\u0806"+
		"\u0001\u0000\u0000\u0000\u0808\u0809\u0001\u0000\u0000\u0000\u0809\u0812"+
		"\u0001\u0000\u0000\u0000\u080a\u0808\u0001\u0000\u0000\u0000\u080b\u080d"+
		"\u0003\u00a8T\u0000\u080c\u080e\u0003\u00aaU\u0000\u080d\u080c\u0001\u0000"+
		"\u0000\u0000\u080e\u080f\u0001\u0000\u0000\u0000\u080f\u080d\u0001\u0000"+
		"\u0000\u0000\u080f\u0810\u0001\u0000\u0000\u0000\u0810\u0812\u0001\u0000"+
		"\u0000\u0000\u0811\u07fe\u0001\u0000\u0000\u0000\u0811\u080b\u0001\u0000"+
		"\u0000\u0000\u0812\u01d9\u0001\u0000\u0000\u0000\u0813\u0814\u0003\u01dc"+
		"\u00ee\u0000\u0814\u01db\u0001\u0000\u0000\u0000\u0815\u0816\u0003\u00c4"+
		"b\u0000\u0816\u0817\u0005\u0091\u0000\u0000\u0817\u0819\u0001\u0000\u0000"+
		"\u0000\u0818\u0815\u0001\u0000\u0000\u0000\u0818\u0819\u0001\u0000\u0000"+
		"\u0000\u0819\u081a\u0001\u0000\u0000\u0000\u081a\u081b\u0003\u01de\u00ef"+
		"\u0000\u081b\u01dd\u0001\u0000\u0000\u0000\u081c\u081d\u0003\u01e0\u00f0"+
		"\u0000\u081d\u01df\u0001\u0000\u0000\u0000\u081e\u081f\u0003\u01e2\u00f1"+
		"\u0000\u081f\u01e1\u0001\u0000\u0000\u0000\u0820\u0821\u0003:\u001d\u0000"+
		"\u0821\u01e3\u0001\u0000\u0000\u0000\u0822\u0823\u0003\u01e6\u00f3\u0000"+
		"\u0823\u01e5\u0001\u0000\u0000\u0000\u0824\u082c\u0005\u0094\u0000\u0000"+
		"\u0825\u082c\u0005u\u0000\u0000\u0826\u0829\u0005\u0018\u0000\u0000\u0827"+
		"\u082a\u0005\u0094\u0000\u0000\u0828\u082a\u0005u\u0000\u0000\u0829\u0827"+
		"\u0001\u0000\u0000\u0000\u0829\u0828\u0001\u0000\u0000\u0000\u0829\u082a"+
		"\u0001\u0000\u0000\u0000\u082a\u082c\u0001\u0000\u0000\u0000\u082b\u0824"+
		"\u0001\u0000\u0000\u0000\u082b\u0825\u0001\u0000\u0000\u0000\u082b\u0826"+
		"\u0001\u0000\u0000\u0000\u082c\u082d\u0001\u0000\u0000\u0000\u082d\u082e"+
		"\u0003\u0118\u008c\u0000\u082e\u01e7\u0001\u0000\u0000\u0000\u082f\u0832"+
		"\u0003\u01ea\u00f5\u0000\u0830\u0832\u0003\u01ec\u00f6\u0000\u0831\u082f"+
		"\u0001\u0000\u0000\u0000\u0831\u0830\u0001\u0000\u0000\u0000\u0832\u01e9"+
		"\u0001\u0000\u0000\u0000\u0833\u0834\u0005@\u0000\u0000\u0834\u0835\u0003"+
		"\u000e\u0007\u0000\u0835\u0836\u0003\u00b2Y\u0000\u0836\u0837\u0003b1"+
		"\u0000\u0837\u01eb\u0001\u0000\u0000\u0000\u0838\u0839\u0005@\u0000\u0000"+
		"\u0839\u083a\u0003\u000e\u0007\u0000\u083a\u083b\u0003\u01f2\u00f9\u0000"+
		"\u083b\u083c\u0003b1\u0000\u083c\u01ed\u0001\u0000\u0000\u0000\u083d\u083e"+
		"\u0003\u01f0\u00f8\u0000\u083e\u01ef\u0001\u0000\u0000\u0000\u083f\u0840"+
		"\u0003\u01f2\u00f9\u0000\u0840\u01f1\u0001\u0000\u0000\u0000\u0841\u0845"+
		"\u0005\u0081\u0000\u0000\u0842\u0843\u0003\u01f4\u00fa\u0000\u0843\u0844"+
		"\u0005y\u0000\u0000\u0844\u0846\u0001\u0000\u0000\u0000\u0845\u0842\u0001"+
		"\u0000\u0000\u0000\u0845\u0846\u0001\u0000\u0000\u0000\u0846\u0847\u0001"+
		"\u0000\u0000\u0000\u0847\u0848\u0003\u01f4\u00fa\u0000\u0848\u0849\u0005"+
		"\u0082\u0000\u0000\u0849\u01f3\u0001\u0000\u0000\u0000\u084a\u084d\u0003"+
		"\u01bc\u00de\u0000\u084b\u084d\u0003\u0192\u00c9\u0000\u084c\u084a\u0001"+
		"\u0000\u0000\u0000\u084c\u084b\u0001\u0000\u0000\u0000\u084d\u01f5\u0001"+
		"\u0000\u0000\u0000\u084e\u084f\u0003P(\u0000\u084f\u0850\u0005>\u0000"+
		"\u0000\u0850\u0851\u0003\u0084B\u0000\u0851\u0852\u0003b1\u0000\u0852"+
		"\u01f7\u0001\u0000\u0000\u0000\u0853\u0854\u0005\u0087\u0000\u0000\u0854"+
		"\u0855\u0003\u01fc\u00fe\u0000\u0855\u01f9\u0001\u0000\u0000\u0000\u0856"+
		"\u0857\u0005\u0087\u0000\u0000\u0857\u0858\u0003\u01fc\u00fe\u0000\u0858"+
		"\u01fb\u0001\u0000\u0000\u0000\u0859\u085a\u0003\u00be_\u0000\u085a\u01fd"+
		"\u0001\u0000\u0000\u0000\u085b\u085d\u0003\u01fa\u00fd\u0000\u085c\u085b"+
		"\u0001\u0000\u0000\u0000\u085d\u0860\u0001\u0000\u0000\u0000\u085e\u085c"+
		"\u0001\u0000\u0000\u0000\u085e\u085f\u0001\u0000\u0000\u0000\u085f\u0861"+
		"\u0001\u0000\u0000\u0000\u0860\u085e\u0001\u0000\u0000\u0000\u0861\u0862"+
		"\u0007\u000e\u0000\u0000\u0862\u086c\u0003\u0200\u0100\u0000\u0863\u0864"+
		"\u0005\u0001\u0000\u0000\u0864\u0869\u0003\u0018\f\u0000\u0865\u0866\u0005"+
		"\u0084\u0000\u0000\u0866\u0868\u0003\u0018\f\u0000\u0867\u0865\u0001\u0000"+
		"\u0000\u0000\u0868\u086b\u0001\u0000\u0000\u0000\u0869\u0867\u0001\u0000"+
		"\u0000\u0000\u0869\u086a\u0001\u0000\u0000\u0000\u086a\u086d\u0001\u0000"+
		"\u0000\u0000\u086b\u0869\u0001\u0000\u0000\u0000\u086c\u0863\u0001\u0000"+
		"\u0000\u0000\u086c\u086d\u0001\u0000\u0000\u0000\u086d\u086e\u0001\u0000"+
		"\u0000\u0000\u086e\u086f\u0003\u0202\u0101\u0000\u086f\u01ff\u0001\u0000"+
		"\u0000\u0000\u0870\u0874\u0003\u000e\u0007\u0000\u0871\u0875\u0005\u0092"+
		"\u0000\u0000\u0872\u0873\u0005e\u0000\u0000\u0873\u0875\u0005\u000b\u0000"+
		"\u0000\u0874\u0871\u0001\u0000\u0000\u0000\u0874\u0872\u0001\u0000\u0000"+
		"\u0000\u0875\u0877\u0001\u0000\u0000\u0000\u0876\u0870\u0001\u0000\u0000"+
		"\u0000\u0876\u0877\u0001\u0000\u0000\u0000\u0877\u0878\u0001\u0000\u0000"+
		"\u0000\u0878\u0879\u0003\u00be_\u0000\u0879\u0201\u0001\u0000\u0000\u0000"+
		"\u087a\u0884\u0005\u0083\u0000\u0000\u087b\u087f\u0005\u007f\u0000\u0000"+
		"\u087c\u087e\u0003\u0204\u0102\u0000\u087d\u087c\u0001\u0000\u0000\u0000"+
		"\u087e\u0881\u0001\u0000\u0000\u0000\u087f\u087d\u0001\u0000\u0000\u0000"+
		"\u087f\u0880\u0001\u0000\u0000\u0000\u0880\u0882\u0001\u0000\u0000\u0000"+
		"\u0881\u087f\u0001\u0000\u0000\u0000\u0882\u0884\u0005\u0080\u0000\u0000"+
		"\u0883\u087a\u0001\u0000\u0000\u0000\u0883\u087b\u0001\u0000\u0000\u0000"+
		"\u0884\u0203\u0001\u0000\u0000\u0000\u0885\u088a\u00034\u001a\u0000\u0886"+
		"\u088a\u0003\u0206\u0103\u0000\u0887\u088a\u00038\u001c\u0000\u0888\u088a"+
		"\u0003<\u001e\u0000\u0889\u0885\u0001\u0000\u0000\u0000\u0889\u0886\u0001"+
		"\u0000\u0000\u0000\u0889\u0887\u0001\u0000\u0000\u0000\u0889\u0888\u0001"+
		"\u0000\u0000\u0000\u088a\u0205\u0001\u0000\u0000\u0000\u088b\u088c\u0003"+
		"\u0208\u0104\u0000\u088c\u0207\u0001\u0000\u0000\u0000\u088d\u088f\u0005"+
		"$\u0000\u0000\u088e\u088d\u0001\u0000\u0000\u0000\u088e\u088f\u0001\u0000"+
		"\u0000\u0000\u088f\u0891\u0001\u0000\u0000\u0000\u0890\u0892\u0007\u0004"+
		"\u0000\u0000\u0891\u0890\u0001\u0000\u0000\u0000\u0891\u0892\u0001\u0000"+
		"\u0000\u0000\u0892\u0893\u0001\u0000\u0000\u0000\u0893\u0895\u0003\u00ca"+
		"e\u0000\u0894\u0896\u0003\u00a6S\u0000\u0895\u0894\u0001\u0000\u0000\u0000"+
		"\u0895\u0896\u0001\u0000\u0000\u0000\u0896\u0898\u0001\u0000\u0000\u0000"+
		"\u0897\u0899\u0003\u01e4\u00f2\u0000\u0898\u0897\u0001\u0000\u0000\u0000"+
		"\u0898\u0899\u0001\u0000\u0000\u0000\u0899\u089a\u0001\u0000\u0000\u0000"+
		"\u089a\u089b\u0003\u0202\u0101\u0000\u089b\u0209\u0001\u0000\u0000\u0000"+
		"\u089c\u089e\u0003\u01fa\u00fd\u0000\u089d\u089c\u0001\u0000\u0000\u0000"+
		"\u089e\u08a1\u0001\u0000\u0000\u0000\u089f\u089d\u0001\u0000\u0000\u0000"+
		"\u089f\u08a0\u0001\u0000\u0000\u0000\u08a0\u08a2\u0001\u0000\u0000\u0000"+
		"\u08a1\u089f\u0001\u0000\u0000\u0000\u08a2\u08a3\u0003\u020e\u0107\u0000"+
		"\u08a3\u08a4\u0003\u0210\u0108\u0000\u08a4\u020b\u0001\u0000\u0000\u0000"+
		"\u08a5\u08a6\u0005Y\u0000\u0000\u08a6\u08aa\u0005:\u0000\u0000\u08a7\u08a9"+
		"\u0003\u01fa\u00fd\u0000\u08a8\u08a7\u0001\u0000\u0000\u0000\u08a9\u08ac"+
		"\u0001\u0000\u0000\u0000\u08aa\u08a8\u0001\u0000\u0000\u0000\u08aa\u08ab"+
		"\u0001\u0000\u0000\u0000\u08ab\u08ad\u0001\u0000\u0000\u0000\u08ac\u08aa"+
		"\u0001\u0000\u0000\u0000\u08ad\u08ae\u0003\u020e\u0107\u0000\u08ae\u08af"+
		"\u0003\u0210\u0108\u0000\u08af\u020d\u0001\u0000\u0000\u0000\u08b0\u08b1"+
		"\u0005J\u0000\u0000\u08b1\u08b2\u0003\u000e\u0007\u0000\u08b2\u020f\u0001"+
		"\u0000\u0000\u0000\u08b3\u08be\u0005\u0083\u0000\u0000\u08b4\u08b9\u0005"+
		"\u007f\u0000\u0000\u08b5\u08b8\u0003,\u0016\u0000\u08b6\u08b8\u0003\u0212"+
		"\u0109\u0000\u08b7\u08b5\u0001\u0000\u0000\u0000\u08b7\u08b6\u0001\u0000"+
		"\u0000\u0000\u08b8\u08bb\u0001\u0000\u0000\u0000\u08b9\u08b7\u0001\u0000"+
		"\u0000\u0000\u08b9\u08ba\u0001\u0000\u0000\u0000\u08ba\u08bc\u0001\u0000"+
		"\u0000\u0000\u08bb\u08b9\u0001\u0000\u0000\u0000\u08bc\u08be\u0005\u0080"+
		"\u0000\u0000\u08bd\u08b3\u0001\u0000\u0000\u0000\u08bd\u08b4\u0001\u0000"+
		"\u0000\u0000\u08be\u0211\u0001\u0000\u0000\u0000\u08bf\u08c0\u0003.\u0017"+
		"\u0000\u08c0\u08c1\u0005\'\u0000\u0000\u08c1\u08c2\u0003\u0118\u008c\u0000"+
		"\u08c2\u08c3\u0005\u0083\u0000\u0000\u08c3\u0213\u0001\u0000\u0000\u0000"+
		"\u08c4\u08c5\u0003P(\u0000\u08c5\u08c6\u0005i\u0000\u0000\u08c6\u08c7"+
		"\u0003\u0084B\u0000\u08c7\u08c8\u0003\u0216\u010b\u0000\u08c8\u0215\u0001"+
		"\u0000\u0000\u0000\u08c9\u08d3\u0005\u0083\u0000\u0000\u08ca\u08ce\u0005"+
		"\u007f\u0000\u0000\u08cb\u08cd\u0003\u0218\u010c\u0000\u08cc\u08cb\u0001"+
		"\u0000\u0000\u0000\u08cd\u08d0\u0001\u0000\u0000\u0000\u08ce\u08cc\u0001"+
		"\u0000\u0000\u0000\u08ce\u08cf\u0001\u0000\u0000\u0000\u08cf\u08d1\u0001"+
		"\u0000\u0000\u0000\u08d0\u08ce\u0001\u0000\u0000\u0000\u08d1\u08d3\u0005"+
		"\u0080\u0000\u0000\u08d2\u08c9\u0001\u0000\u0000\u0000\u08d2\u08ca\u0001"+
		"\u0000\u0000\u0000\u08d3\u0217\u0001\u0000\u0000\u0000\u08d4\u08d8\u0003"+
		"d2\u0000\u08d5\u08d8\u0003\u021a\u010d\u0000\u08d6\u08d8\u0003\u021c\u010e"+
		"\u0000\u08d7\u08d4\u0001\u0000\u0000\u0000\u08d7\u08d5\u0001\u0000\u0000"+
		"\u0000\u08d7\u08d6\u0001\u0000\u0000\u0000\u08d8\u0219\u0001\u0000\u0000"+
		"\u0000\u08d9\u08db\u0005!\u0000\u0000\u08da\u08dc\u0005\u0004\u0000\u0000"+
		"\u08db\u08da\u0001\u0000\u0000\u0000\u08db\u08dc\u0001\u0000\u0000\u0000"+
		"\u08dc\u08dd\u0001\u0000\u0000\u0000\u08dd\u08de\u0003>\u001f\u0000\u08de"+
		"\u08df\u0003\u0010\b\u0000\u08df\u021b\u0001\u0000\u0000\u0000\u08e0\u08e1"+
		"\u0003.\u0017\u0000\u08e1\u08e2\u0005S\u0000\u0000\u08e2\u08e3\u0003:"+
		"\u001d\u0000\u08e3\u08e4\u0003b1\u0000\u08e4\u021d\u0001\u0000\u0000\u0000"+
		"\u08e5\u08e6\u0003P(\u0000\u08e6\u08e7\u0005T\u0000\u0000\u08e7\u08e8"+
		"\u0003\u0084B\u0000\u08e8\u08e9\u0003b1\u0000\u08e9\u021f\u0001\u0000"+
		"\u0000\u0000\u08ea\u08eb\u0003P(\u0000\u08eb\u08ec\u0005j\u0000\u0000"+
		"\u08ec\u08ed\u0003\u0084B\u0000\u08ed\u08ee\u0003\u0102\u0081\u0000\u08ee"+
		"\u0221\u0001\u0000\u0000\u0000\u00e1\u0225\u0236\u0239\u0240\u0244\u0248"+
		"\u024c\u0251\u0256\u0259\u0260\u0269\u0276\u0280\u0283\u0285\u0289\u0291"+
		"\u0297\u02a0\u02a6\u02b4\u02b8\u02be\u02c1\u02c7\u02d4\u02d7\u02df\u02e5"+
		"\u02ed\u02f4\u02f9\u0300\u0303\u030b\u030d\u0312\u031a\u0339\u0345\u034d"+
		"\u0352\u0356\u035a\u035e\u0360\u0365\u036e\u0378\u0381\u038a\u0393\u039c"+
		"\u03a4\u03a8\u03af\u03b3\u03bf\u03c3\u03c7\u03cc\u03d1\u03d7\u03db\u03e0"+
		"\u03e5\u03eb\u03ef\u03f3\u03f7\u03fb\u040a\u040e\u0412\u0417\u0420\u0425"+
		"\u0432\u0435\u0439\u043d\u0440\u0445\u044a\u044d\u0450\u0454\u0458\u045c"+
		"\u045f\u0464\u046f\u0474\u0478\u047d\u0484\u0487\u048d\u0492\u049f\u04a5"+
		"\u04a8\u04ad\u04b4\u04b7\u04bb\u04bf\u04c3\u04c5\u04c7\u04ce\u04d5\u04e0"+
		"\u04f1\u04f9\u0505\u0515\u0526\u052c\u052e\u0533\u0538\u053e\u0544\u056a"+
		"\u056d\u0570\u0576\u0579\u057e\u0580\u0587\u0591\u0599\u059d\u05b0\u05b3"+
		"\u05b6\u05bc\u05be\u05cb\u05ce\u05d1\u05d7\u05d9\u05e3\u05e6\u05f4\u05f8"+
		"\u05fa\u05fe\u060a\u060d\u0619\u061c\u0624\u0627\u062a\u0656\u066a\u066c"+
		"\u067f\u0687\u0696\u06c8\u06e4\u06e8\u06ea\u06fa\u0713\u0716\u0730\u0748"+
		"\u0755\u075a\u0777\u0780\u0789\u0791\u07a9\u07b6\u07bb\u07d1\u07d5\u07dc"+
		"\u07df\u07e5\u07ec\u07f3\u07f7\u07f9\u07fb\u0800\u0803\u0808\u080f\u0811"+
		"\u0818\u0829\u082b\u0831\u0845\u084c\u085e\u0869\u086c\u0874\u0876\u087f"+
		"\u0883\u0889\u088e\u0891\u0895\u0898\u089f\u08aa\u08b7\u08b9\u08bd\u08ce"+
		"\u08d2\u08d7\u08db";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}