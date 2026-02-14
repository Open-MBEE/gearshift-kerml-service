package org.openmbee.gearshift.intellij.navigation

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import org.openmbee.gearshift.intellij.parser.KerMLLexerAdapter
import org.openmbee.gearshift.intellij.parser.KerMLTokenTypes
import org.openmbee.gearshift.intellij.psi.KerMLDeclaration

class KerMLFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            KerMLLexerAdapter(),
            KerMLTokenTypes.IDENTIFIERS,
            KerMLTokenTypes.COMMENTS,
            KerMLTokenTypes.STRING_LITERALS
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is KerMLDeclaration && psiElement.declaredName != null
    }

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String {
        return when (element) {
            is KerMLDeclaration -> element.declarationKeyword ?: "element"
            else -> "element"
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return when (element) {
            is KerMLDeclaration -> element.declaredName ?: ""
            else -> ""
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return when (element) {
            is KerMLDeclaration -> {
                val keyword = element.declarationKeyword ?: "element"
                val name = element.declaredName ?: "<anonymous>"
                "$keyword $name"
            }
            else -> element.text?.take(50) ?: ""
        }
    }
}
