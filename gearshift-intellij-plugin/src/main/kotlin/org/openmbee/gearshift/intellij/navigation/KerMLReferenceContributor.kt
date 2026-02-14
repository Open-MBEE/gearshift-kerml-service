package org.openmbee.gearshift.intellij.navigation

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.openmbee.gearshift.intellij.psi.KerMLNameRef

class KerMLReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(KerMLNameRef::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val nameRef = element as? KerMLNameRef ?: return PsiReference.EMPTY_ARRAY
                    val text = nameRef.referenceName ?: return PsiReference.EMPTY_ARRAY
                    val range = TextRange(0, text.length)
                    return arrayOf(KerMLReference(nameRef, range))
                }
            }
        )
    }
}
