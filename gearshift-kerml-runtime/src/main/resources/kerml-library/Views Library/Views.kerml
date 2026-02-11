standard library package Views {
	doc
	/*
	 * This package defines the base types for views, viewpoints, and renderings
	 * in the KerML language. Adapted from the SysML v2 Views library (clause 8.3.26)
	 * into KerML collapsed syntax.
	 *
	 * SysML-specific concepts (Requirements, satisfy, require, subject) are omitted.
	 * Part is replaced by Object as the structural base type.
	 */

	private import Objects::Object;
	private import Objects::objects;

	abstract view View :> Object {
		doc
		/*
		 * View is the most general type for views. A View selects and organizes
		 * model elements for presentation via a Rendering. Elements to be included
		 * are specified using Expose (an import into the View).
		 */

		feature self : View redefines Object::self;

		abstract feature subviews : View[0..*] :> views {
			doc
			/*
			 * Other Views that are used in the rendering of this View.
			 */
		}
	}

	abstract rendering Rendering :> Object {
		doc
		/*
		 * Rendering is the most general type for renderings. A Rendering specifies
		 * how the contents of a View are to be presented.
		 */

		feature self : Rendering redefines Object::self;

		abstract feature subrenderings : Rendering[0..*] :> renderings {
			doc
			/*
			 * Other Renderings used to carry out this Rendering.
			 */
		}
	}

	abstract viewpoint Viewpoint {
		doc
		/*
		 * Viewpoint is the most general type for viewpoints. A Viewpoint defines
		 * the concerns and constraints that a View must satisfy for its stakeholders.
		 */
	}

	rendering TextualRendering :> Rendering {
		doc
		/*
		 * A TextualRendering is a Rendering of a View into a textual format.
		 */
	}

	rendering GraphicalRendering :> Rendering {
		doc
		/*
		 * A GraphicalRendering is a Rendering of a View into a graphical format.
		 */
	}

	rendering TabularRendering :> Rendering {
		doc
		/*
		 * A TabularRendering is a Rendering of a View into a tabular format.
		 */
	}

	abstract feature views : View[0..*] nonunique :> objects {
		doc
		/*
		 * views is the base feature of all View usages.
		 */
	}

	abstract feature renderings : Rendering[0..*] nonunique :> objects {
		doc
		/*
		 * renderings is the base feature of all Rendering usages.
		 */
	}

	feature asTextualNotation : TextualRendering[1] :> renderings {
		doc
		/*
		 * asTextualNotation renders a View into textual notation as defined
		 * in the KerML specification.
		 */
	}

	feature asTreeDiagram : GraphicalRendering[1] :> renderings {
		doc
		/*
		 * asTreeDiagram renders a View as a tree diagram.
		 */
	}

	feature asInterconnectionDiagram : GraphicalRendering[1] :> renderings {
		doc
		/*
		 * asInterconnectionDiagram renders a View as an interconnection diagram.
		 */
	}

	feature asElementTable : TabularRendering[1] :> renderings {
		doc
		/*
		 * asElementTable renders a View as a table, with one row for each
		 * exposed Element.
		 */
	}
}
