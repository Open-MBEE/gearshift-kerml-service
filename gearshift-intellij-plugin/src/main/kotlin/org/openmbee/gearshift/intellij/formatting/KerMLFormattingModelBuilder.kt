/*
 * Copyright 2026 Charles Galey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openmbee.gearshift.intellij.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import org.openmbee.gearshift.intellij.annotator.KerMLModelCache
import org.openmbee.gearshift.kerml.KerMLModel
import org.openmbee.gearshift.kerml.generator.KerMLWriter
import org.openmbee.gearshift.settings.GearshiftSettings

/**
 * Formatting model that performs whole-file formatting via KerMLWriter round-trip:
 * parse → KerMLWriter.write() → replace entire document.
 *
 * Uses the per-file [KerMLModel] from the cache when available so that
 * the model root is accessible for writing.
 */
class KerMLFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val file = formattingContext.psiElement.containingFile
        val block = KerMLFormattingBlock(file.node)
        return FormattingModelProvider.createFormattingModelForPsiFile(file, block, formattingContext.codeStyleSettings)
    }

    companion object {
        private val lightSettings = GearshiftSettings(
            processImpliedRelationships = false,
            autoMountLibraries = false,
            autoNameFeatures = false
        )

        /**
         * Format the given KerML text using parse-then-write round-trip.
         * If a [cache] and [filePath] are provided, reuses the cached model
         * for that file.  Otherwise does a standalone parse.
         * Returns null if parsing fails.
         */
        fun formatText(
            text: String,
            cache: KerMLModelCache? = null,
            filePath: String? = null,
            modificationStamp: Long = 0
        ): String? {
            val model = if (cache != null && filePath != null) {
                cache.getModel(filePath, modificationStamp, text)
            } else {
                val standalone = KerMLModel(settings = lightSettings)
                standalone.parseString(text) ?: return null
                standalone
            }
            val writer = KerMLWriter.fromSettings(lightSettings)
            return writer.write(model.root)
        }
    }
}

/**
 * Simple block implementation — the real formatting happens via document-level
 * replacement in [KerMLFormattingModelBuilder.formatText].
 */
class KerMLFormattingBlock(private val node: ASTNode) : Block {
    override fun getTextRange(): TextRange = node.textRange
    override fun getSubBlocks(): List<Block> = emptyList()
    override fun getWrap(): Wrap? = null
    override fun getIndent(): Indent? = Indent.getNoneIndent()
    override fun getAlignment(): Alignment? = null
    override fun getSpacing(child1: Block?, child2: Block): Spacing? = null
    override fun getChildAttributes(newChildIndex: Int): ChildAttributes = ChildAttributes(Indent.getNoneIndent(), null)
    override fun isIncomplete(): Boolean = false
    override fun isLeaf(): Boolean = true
}
