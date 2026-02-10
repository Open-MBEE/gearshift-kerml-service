grammar KerMLViews;

// ===== Views Extension Grammar =====
// Delegate grammar imported by KerML.g4 to add concrete syntax for
// View, Rendering, Viewpoint, Expose, and ViewRenderingMember.
//
// These are Gearshift-specific extensions — collapsed from SysML into KerML.
// Syntax follows existing KerML patterns (no def/usage split).

// === View ===

// View = TypePrefix 'view' ClassifierDeclaration ViewBody
view
    : typePrefix VIEW
      classifierDeclaration viewBody
    ;

viewBody
    : SEMICOLON
    | LBRACE viewBodyElement* RBRACE
    ;

viewBodyElement
    : typeBodyElement
    | expose
    | viewRenderingMember
    ;

// === Expose ===

// Expose = 'expose' ('all')? ImportDeclaration RelationshipBody
expose
    : EXPOSE ( isImportAll=ALL )?
      importDeclaration relationshipBody
    ;

// === View Rendering Member ===

// ViewRenderingMember = MemberPrefix 'render' QualifiedName TypeBody
viewRenderingMember
    : memberPrefix RENDER
      qualifiedName typeBody
    ;

// === Rendering ===

// Rendering = TypePrefix 'rendering' ClassifierDeclaration TypeBody
rendering
    : typePrefix RENDERING
      classifierDeclaration typeBody
    ;

// === Viewpoint ===

// Viewpoint = TypePrefix 'viewpoint' ClassifierDeclaration FunctionBody
viewpoint
    : typePrefix VIEWPOINT
      classifierDeclaration functionBody
    ;

// === Lexer Tokens ===

EXPOSE : 'expose' ;
RENDER : 'render' ;
RENDERING : 'rendering' ;
VIEW : 'view' ;
VIEWPOINT : 'viewpoint' ;
