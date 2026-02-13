library package DocGen {
	doc
	/*
	 * This package defines the DocGen pipeline library for document generation.
	 * Pipelines are modeled using KerML behavioral semantics: Behavior definitions,
	 * Step usages, Succession ordering, and Feature bindings for data flow.
	 *
	 * Replaces the legacy DocGen UML profile (80+ stereotypes) with native KerML
	 * constructs. See docs/roadmap/docgen-pipeline.md for the full design.
	 */

	private import Base::Anything;
	private import ScalarValues::*;

	// ═══════════════════════════════════════
	// Enum-like Types
	// ═══════════════════════════════════════

	struct DirectionKind {
		doc
		/*
		 * Direction for relationship traversal in CollectByRelationship.
		 */

		feature forward : DirectionKind;
		feature backward : DirectionKind;
		feature both : DirectionKind;
	}

	struct SourcePropertyKind {
		doc
		/*
		 * Which element property to use for presentation content.
		 */

		feature documentation : SourcePropertyKind;
		feature name : SourcePropertyKind;
		feature value : SourcePropertyKind;
	}

	// ═══════════════════════════════════════
	// Base Behaviors
	// ═══════════════════════════════════════

	abstract behavior QueryOp {
		doc
		/*
		 * Base behavior for all query pipeline operations.
		 * Takes an element set in, produces a (possibly transformed) element set out.
		 */

		in elements : Anything[0..*];
		out result : Anything[0..*];
	}

	abstract behavior PresentOp {
		doc
		/*
		 * Base behavior for all presentation operations.
		 * Takes an element set in and produces rendered document content.
		 */

		in elements : Anything[0..*];
		feature title : String[0..1];
		feature expression : String[0..1];
	}

	abstract behavior DocGenPipeline {
		doc
		/*
		 * A composable pipeline of QueryOp and PresentOp steps.
		 * The primary unit of reuse for document generation.
		 */

		in context : Anything[0..*];
	}

	// ═══════════════════════════════════════
	// Collect Behaviors
	// ═══════════════════════════════════════

	behavior CollectByExpression specializes QueryOp {
		doc
		/*
		 * Evaluate a custom OCL expression to collect related elements.
		 */

		feature expression : String;
	}

	behavior CollectByRelationship specializes QueryOp {
		doc
		/*
		 * Follow Relationships filtered by KerML metaclass.
		 */

		feature depth : Integer default 1;
		feature direction : DirectionKind default DirectionKind::forward;
		feature relationshipMetaclass : String;
	}

	behavior CollectFeatures specializes QueryOp {
		doc
		/*
		 * Collect Type.feature / Type.ownedFeature.
		 * When includeInherited=true, walks the Specialization chain.
		 */

		feature includeInherited : Boolean default false;
	}

	behavior CollectOwned specializes QueryOp {
		doc
		/*
		 * Walk Namespace.ownedMembership to collect owned children.
		 * depth=1 direct children only, depth=-1 unlimited recursion.
		 */

		feature depth : Integer default -1;
	}

	behavior CollectOwners specializes QueryOp {
		doc
		/*
		 * Navigate up Element.owner chain.
		 */

		feature depth : Integer default 1;
	}

	behavior CollectTypes specializes QueryOp {
		doc
		/*
		 * Follow FeatureTyping.type to collect the types of input features.
		 */
	}

	// ═══════════════════════════════════════
	// Filter Behaviors
	// ═══════════════════════════════════════

	behavior FilterByExpression specializes QueryOp {
		doc
		/*
		 * Keep or exclude elements matching an OCL predicate expression.
		 */

		feature expression : String;
	}

	behavior FilterByMetaclass specializes QueryOp {
		doc
		/*
		 * Keep or exclude elements by KerML metaclass.
		 */

		feature include : Boolean default true;
		feature metaclass : String;
	}

	behavior FilterByName specializes QueryOp {
		doc
		/*
		 * Keep or exclude elements matching a name pattern.
		 */

		feature include : Boolean default true;
		feature pattern : String;
		feature regex : Boolean default false;
	}

	// ═══════════════════════════════════════
	// Sort Behaviors
	// ═══════════════════════════════════════

	behavior SortByExpression specializes QueryOp {
		doc
		/*
		 * Sort by a custom OCL expression result.
		 */

		feature expression : String;
		feature reversed : Boolean default false;
	}

	behavior SortByFeature specializes QueryOp {
		doc
		/*
		 * Sort by a named Feature property value.
		 */

		feature featureName : String;
		feature reversed : Boolean default false;
	}

	behavior SortByName specializes QueryOp {
		doc
		/*
		 * Sort by element name.
		 */

		feature reversed : Boolean default false;
	}

	// ═══════════════════════════════════════
	// Combine Behaviors
	// ═══════════════════════════════════════

	behavior Intersection specializes QueryOp {
		doc
		/*
		 * Intersect two element sets.
		 */

		in branchA : Anything[0..*];
		in branchB : Anything[0..*];
	}

	behavior RemoveDuplicates specializes QueryOp {
		doc
		/*
		 * Deduplicate by element identity.
		 */
	}

	behavior Union specializes QueryOp {
		doc
		/*
		 * Merge two element sets.
		 */

		in branchA : Anything[0..*];
		in branchB : Anything[0..*];
	}

	// ═══════════════════════════════════════
	// Support Types
	// ═══════════════════════════════════════

	struct ColumnSpec {
		doc
		/*
		 * Column specification for PresentTable.
		 */

		feature header : String;
		feature property : String;
		feature width : String[0..1];
	}

	// ═══════════════════════════════════════
	// Presentation Behaviors
	// ═══════════════════════════════════════

	behavior PresentImage specializes PresentOp {
		doc
		/*
		 * Embed an element's diagram or image artifact.
		 */

		feature artifactType : String default "svg";
		feature showCaption : Boolean default true;
	}

	behavior PresentList specializes PresentOp {
		doc
		/*
		 * Generate a list from elements.
		 */

		feature ordered : Boolean default false;
		feature sourceProperty : SourcePropertyKind default SourcePropertyKind::name;
	}

	behavior PresentParagraph specializes PresentOp {
		doc
		/*
		 * Generate paragraph content from element properties or OCL expressions.
		 */

		feature iterate : Boolean default true;
		feature sourceProperty : SourcePropertyKind default SourcePropertyKind::documentation;
	}

	behavior PresentTable specializes PresentOp {
		doc
		/*
		 * Generate a table from elements x properties.
		 * Each input element becomes a row.
		 */

		feature columns : ColumnSpec[1..*];
		feature hideHeaders : Boolean default false;
		feature showIfEmpty : Boolean default false;
	}

	// ═══════════════════════════════════════
	// Document Structure
	// ═══════════════════════════════════════

	behavior SectionDef specializes DocGenPipeline {
		doc
		/*
		 * A section is a named container with a query pipeline.
		 * Sections can be nested to arbitrary depth via step composition.
		 */

		feature isAppendix : Boolean default false;
		feature title : String;
	}
}
