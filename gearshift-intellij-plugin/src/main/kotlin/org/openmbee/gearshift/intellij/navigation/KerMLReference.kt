package org.openmbee.gearshift.intellij.navigation

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import org.openmbee.gearshift.intellij.psi.KerMLDeclaration
import org.openmbee.gearshift.intellij.psi.KerMLFile
import org.openmbee.gearshift.intellij.psi.KerMLNameRef

/**
 * PSI reference from a name reference to a declaration with matching name.
 * Resolves within the same file by name matching.
 */
class KerMLReference(element: KerMLNameRef, rangeInElement: TextRange) :
    PsiReferenceBase<KerMLNameRef>(element, rangeInElement, true) {

    override fun resolve(): PsiElement? {
        val refName = element.referenceName ?: return null
        val file = element.containingFile as? KerMLFile ?: return null

        // Don't resolve if this name ref IS the declaration name
        val parentDecl = PsiTreeUtil.getParentOfType(element, KerMLDeclaration::class.java)
        if (parentDecl != null && parentDecl.nameIdentifier == element) {
            return null
        }

        return findDeclarationByName(file, refName)
    }

    override fun getVariants(): Array<Any> = emptyArray()

    companion object {
        fun findDeclarationByName(file: KerMLFile, name: String): KerMLDeclaration? {
            return PsiTreeUtil.findChildrenOfType(file, KerMLDeclaration::class.java)
                .firstOrNull { it.declaredName == name }
        }

        fun findAllDeclarations(file: KerMLFile): List<KerMLDeclaration> {
            return PsiTreeUtil.findChildrenOfType(file, KerMLDeclaration::class.java)
                .filter { it.declaredName != null }
                .toList()
        }
    }
}
