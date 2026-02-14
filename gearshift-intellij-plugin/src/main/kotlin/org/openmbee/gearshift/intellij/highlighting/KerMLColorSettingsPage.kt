package org.openmbee.gearshift.intellij.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import org.openmbee.gearshift.intellij.KerMLIcons
import javax.swing.Icon

class KerMLColorSettingsPage : ColorSettingsPage {

    override fun getIcon(): Icon = KerMLIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = KerMLSyntaxHighlighter()

    override fun getDemoText(): String = """
        // Single-line comment
        /* Block comment */

        package SpacecraftDesign {
            doc /* Documentation for the package */

            abstract class Vehicle {
                feature mass : Real;
                feature name : String;
            }

            class Spacecraft :> Vehicle {
                feature propulsion : PropulsionSystem;
                feature payload : Real default 1000;

                inv { mass > 0 and mass < 100000 }
            }

            class PropulsionSystem {
                feature thrust : Real;
                feature specificImpulse : Real;
            }

            import ISQ::*;

            binding connector docking of Spacecraft;

            succession first launch then orbit;
        }
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "KerML"

    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Declaration keyword", KerMLSyntaxHighlighter.DECLARATION_KEYWORD),
            AttributesDescriptor("Modifier keyword", KerMLSyntaxHighlighter.MODIFIER_KEYWORD),
            AttributesDescriptor("Relationship keyword", KerMLSyntaxHighlighter.RELATIONSHIP_KEYWORD),
            AttributesDescriptor("Other keyword", KerMLSyntaxHighlighter.KEYWORD),
            AttributesDescriptor("Identifier", KerMLSyntaxHighlighter.IDENTIFIER),
            AttributesDescriptor("String", KerMLSyntaxHighlighter.STRING),
            AttributesDescriptor("Number", KerMLSyntaxHighlighter.NUMBER),
            AttributesDescriptor("Line comment", KerMLSyntaxHighlighter.LINE_COMMENT),
            AttributesDescriptor("Block comment", KerMLSyntaxHighlighter.BLOCK_COMMENT),
            AttributesDescriptor("Doc comment", KerMLSyntaxHighlighter.DOC_COMMENT),
            AttributesDescriptor("Operator", KerMLSyntaxHighlighter.OPERATOR),
            AttributesDescriptor("Braces", KerMLSyntaxHighlighter.BRACES),
            AttributesDescriptor("Brackets", KerMLSyntaxHighlighter.BRACKETS),
            AttributesDescriptor("Parentheses", KerMLSyntaxHighlighter.PARENTHESES),
            AttributesDescriptor("Semicolon", KerMLSyntaxHighlighter.SEMICOLON),
            AttributesDescriptor("Comma", KerMLSyntaxHighlighter.COMMA),
            AttributesDescriptor("Dot", KerMLSyntaxHighlighter.DOT),
            AttributesDescriptor("Bad character", KerMLSyntaxHighlighter.BAD_CHARACTER)
        )
    }
}
