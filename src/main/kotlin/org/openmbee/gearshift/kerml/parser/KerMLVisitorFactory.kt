package org.openmbee.gearshift.kerml.parser

/**
 * Factory for creating KerML element visitors.
 * Provides a central registry of all visitor types organized by element category.
 */
object KerMLVisitorFactory {

    /**
     * NonFeatureElement visitors.
     */
    object NonFeatureElements {
        val dependency = DependencyVisitor()
        val namespace = NamespaceVisitor()
        val type = TypeVisitor()
        val classifier = ClassifierVisitor()
        val dataType = DataTypeVisitor()
        val classElement = ClassVisitor()
        val structure = StructureVisitor()
        val metaclass = MetaclassVisitor()
        val association = AssociationVisitor()
        val associationStructure = AssociationStructureVisitor()
        val interaction = InteractionVisitor()
        val behavior = BehaviorVisitor()
        val function = FunctionVisitor()
        val predicate = PredicateVisitor()
        val multiplicity = MultiplicityVisitor()
        val packageElement = PackageVisitor()
        val libraryPackage = LibraryPackageVisitor()
        val specialization = SpecializationVisitor()
        val conjugation = ConjugationVisitor()
        val subclassification = SubclassificationVisitor()
        val disjoining = DisjoiningVisitor()
        val featureInverting = FeatureInvertingVisitor()
        val featureTyping = FeatureTypingVisitor()
        val subsetting = SubsettingVisitor()
        val redefinition = RedefinitionVisitor()
        val typeFeaturing = TypeFeaturingVisitor()
    }

    /**
     * FeatureElement visitors.
     */
    object FeatureElements {
        val feature = FeatureVisitor()
        val step = StepVisitor()
        val expression = ExpressionVisitor()
        val booleanExpression = BooleanExpressionVisitor()
        val invariant = InvariantVisitor()
        val connector = ConnectorVisitor()
        val bindingConnector = BindingConnectorVisitor()
        val succession = SuccessionVisitor()
        val flow = FlowVisitor()
        val successionFlow = SuccessionFlowVisitor()
    }

    /**
     * AnnotatingElement visitors.
     */
    object AnnotatingElements {
        val comment = CommentVisitor()
        val documentation = DocumentationVisitor()
        val textualRepresentation = TextualRepresentationVisitor()
        val metadataFeature = MetadataFeatureVisitor()
        val annotation = AnnotationVisitor()
        val ownedAnnotation = OwnedAnnotationVisitor()
        val prefixMetadataAnnotation = PrefixMetadataAnnotationVisitor()
    }

    /**
     * Get a visitor by element type name.
     *
     * @param elementType The KerML element type name
     * @return The visitor for that element type, or null if not found
     */
    fun getVisitor(elementType: String): KerMLElementVisitor<Any>? {
        return when (elementType.lowercase()) {
            // NonFeatureElements
            "dependency" -> NonFeatureElements.dependency
            "namespace" -> NonFeatureElements.namespace
            "type" -> NonFeatureElements.type
            "classifier" -> NonFeatureElements.classifier
            "datatype" -> NonFeatureElements.dataType
            "class" -> NonFeatureElements.classElement
            "structure" -> NonFeatureElements.structure
            "metaclass" -> NonFeatureElements.metaclass
            "association" -> NonFeatureElements.association
            "associationstructure" -> NonFeatureElements.associationStructure
            "interaction" -> NonFeatureElements.interaction
            "behavior" -> NonFeatureElements.behavior
            "function" -> NonFeatureElements.function
            "predicate" -> NonFeatureElements.predicate
            "multiplicity" -> NonFeatureElements.multiplicity
            "package" -> NonFeatureElements.packageElement
            "librarypackage" -> NonFeatureElements.libraryPackage
            "specialization" -> NonFeatureElements.specialization
            "conjugation" -> NonFeatureElements.conjugation
            "subclassification" -> NonFeatureElements.subclassification
            "disjoining" -> NonFeatureElements.disjoining
            "featureinverting" -> NonFeatureElements.featureInverting
            "featuretyping" -> NonFeatureElements.featureTyping
            "subsetting" -> NonFeatureElements.subsetting
            "redefinition" -> NonFeatureElements.redefinition
            "typefeaturing" -> NonFeatureElements.typeFeaturing

            // FeatureElements
            "feature" -> FeatureElements.feature
            "step" -> FeatureElements.step
            "expression" -> FeatureElements.expression
            "booleanexpression" -> FeatureElements.booleanExpression
            "invariant" -> FeatureElements.invariant
            "connector" -> FeatureElements.connector
            "bindingconnector" -> FeatureElements.bindingConnector
            "succession" -> FeatureElements.succession
            "flow" -> FeatureElements.flow
            "successionflow" -> FeatureElements.successionFlow

            // AnnotatingElements
            "comment" -> AnnotatingElements.comment
            "documentation" -> AnnotatingElements.documentation
            "textualrepresentation" -> AnnotatingElements.textualRepresentation
            "metadatafeature" -> AnnotatingElements.metadataFeature
            "annotation" -> AnnotatingElements.annotation
            "ownedannotation" -> AnnotatingElements.ownedAnnotation
            "prefixmetadataannotation" -> AnnotatingElements.prefixMetadataAnnotation

            else -> null
        }
    }

    /**
     * Get all registered visitor types.
     *
     * @return List of all supported element type names
     */
    fun getAllVisitorTypes(): List<String> {
        return listOf(
            // NonFeatureElements
            "Dependency", "Namespace", "Type", "Classifier", "DataType", "Class", "Structure",
            "Metaclass", "Association", "AssociationStructure", "Interaction", "Behavior",
            "Function", "Predicate", "Multiplicity", "Package", "LibraryPackage",
            "Specialization", "Conjugation", "Subclassification", "Disjoining",
            "FeatureInverting", "FeatureTyping", "Subsetting", "Redefinition", "TypeFeaturing",

            // FeatureElements
            "Feature", "Step", "Expression", "BooleanExpression", "Invariant", "Connector",
            "BindingConnector", "Succession", "Flow", "SuccessionFlow",

            // AnnotatingElements
            "Comment", "Documentation", "TextualRepresentation", "MetadataFeature",
            "Annotation", "OwnedAnnotation", "PrefixMetadataAnnotation"
        )
    }
}
