# KerML Model Interchange

<!-- Page 401 -->

## 10 Model Interchange

### 10.1 Model Interchange Overview

Model interchange is the capability to interchange models between tools using file-based resources (see Clause 2).

The unit of interchange is the **project**, which is defined as follows:

> A project is a set of root namespaces (see 7.2.5.3 and 8.2.3.4.1), including all elements in the ownership trees of those namespaces, and a set of references to **used projects**, such that every cross reference from an element in the project is to another element in that project or to an element in one of the used projects.

The root namespaces in a project may be **serialized** into model interchange files, using any of the formats given in 10.2. A **project interchange file** is then a compressed archive of model interchange files and additional required metadata, as described in 10.3.

KerML is intended to be used as the basis for building other modeling languages. Project-based model interchange as defined in this clause may also be used to interchange models in such languages. Each of the following subclauses includes descriptions of the allowed adaptations for interchanging models in **KerML-based** languages.

### 10.2 Model Interchange Formats

A **model interchange file** contains a textual representation (known as a **serialization**) of a single root namespace (see 7.2.5.3 and 8.2.3.4.1) and all the elements in the ownership tree root in that namespace. A model interchange file shall have one of the following formats:

1. **Textual notation**, using the textual concrete syntax defined in this specification. Note that in certain limited cases, models conformant with the KerML syntax, but prepared by a means other than using the KerML textual concrete syntax, may not be fully serializable into the standard textual notation. In this case, a tool may either not export such model at all using the textual notation, or export the model as closely as possible, informing the user of any changes from the original model. A model interchange file in this format shall have the file extension `.kerml`.

2. **JSON**, using a format according to the JSON serialization mapping defined in 10.4. A model interchange file in this format shall have the file extension `.json`.

3. **XML**, using the XML Metadata Interchange [XMI] format based on the MOF-conformant abstract syntax metamodel for KerML. A model interchange file in this format shall have the file extension `.xmi`.

Every conformant KerML modeling tool shall provide the ability to import and/or export (as appropriate) models in at least one of the first two formats.

For a KerML-based language:

1. **Textual Notation.** If the language has a textual concrete syntax, then this textual notation may be used as a model interchange file format. The language shall define a distinguishing file extension for files of its textual notation.

2. **JSON.** It shall always be possible to use JSON format as a model interchange file format, using the mapping strategy defined in 10.4, as applied to the abstract syntax of the language.

3. **XML.** If the language is defined using a MOF-conformant abstract syntax, then XMI may be used as a model interchange file format.

A KerML-based-language specification may specify further requirements on what interchange formats must be supported by conforming language tools.

### 10.3 Model Interchange Projects

A **project interchange file** contains a single project serialized as a set of model interchange files, archived using the ZIP format [ZIP]. The archive shall contain a model interchange file for each of the root namespaces in the project, each formatted in one of the formats listed in 10.2. In addition, the archive shall contain, at its top level, exactly one file named `.project.json` and exactly one file named `.meta.json`. A KerML project interchange file shall have the file extension `.kpar` (KerML Project Archive).

Other than the use of the file extensions given in 10.2, there are no requirements on the naming of the model interchange files. Nevertheless, they should be named in a way that is compatible across different file systems and that allows for easy reference using International Resource Identifiers (IRIs). The model interchange files may be organized into subdirectories, but this has no impact on the global scope for the project, which is always a flat namespace derived from the root namespaces of the project (see 8.2.3.5). However, each model interchange file shall be identifiable by a unique path in the archive directory structure.

The `.project.json` file shall contain the **InterchangeProject** information shown in Fig. 42, serialized as a single JSON object according to the **Project** schema definition in the **ModelInterchange.json** artifact provided with this specification. Table 12 gives all the properties of the **InterchangeProject** and **InterchangeProjectUsage** elements, consistent with the normative JSON schema. Every element referenced in a model interchange file in a project interchange file shall either also be contained in a model interchange file in that project interchange file, or in one of the projects referenced in the **usage** list for the project interchange file.

The **usage** information for each used project includes an optional **versionConstraint** property. If given, then only versions of the project identified by the **resource** property that meet this constraint may be used. For an interchanged project, the version is as given in its **version** property. It is recommended, but not required, that semantic versioning (see https://semver.org/) be used for the version numbering of interchange projects and semantic versioning ranges (see, e.g., https://docs.npmjs.com/cli/v6/using-npm/semver#ranges) be used for version constraints. Tools that support such version formatting should report any version constraint violations when importing an interchange project, for any used projects with dereferenceable **resource** IRIs.

The `.meta.json` file shall contain further metadata on the project interchange file, serialized as a single JSON object according to the **Meta** schema definition in the **ModelInterchange.json** artifact provided with this specification. Table 13 describes all the fields specified in the normative JSON schema.

A project interchange file for a KerML-based language shall include model interchange files specific to that language (as described in 10.2). Such a project interchange file may use the generic `.kpar` extension, or it may define its own language-specific extension. If it uses the `.kpar` extension, then the metadata for the file shall identify the KerML-based language metamodel (see Table 13). Each project interchange file shall only contain models in a single language, but it shall be able to have used projects both in the same language and in KerML (such as from the Kernel Model Libraries). A KerML-based-language specification may also allow for project interchange files that use projects in other KerML-based languages.