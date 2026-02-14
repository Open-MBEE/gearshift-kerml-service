package org.openmbee.gearshift.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.openmbee.gearshift.intellij.psi.KerMLFile
import org.openmbee.gearshift.intellij.psi.KerMLPsiUtil

class KerMLParserDefinition : ParserDefinition {

    override fun createLexer(project: Project?): Lexer = KerMLLexerAdapter()

    override fun createParser(project: Project?): PsiParser = KerMLPsiParser()

    override fun getFileNodeType(): IFileElementType = KerMLElementTypes.FILE

    override fun getCommentTokens(): TokenSet = KerMLTokenTypes.COMMENTS

    override fun getStringLiteralElements(): TokenSet = KerMLTokenTypes.STRING_LITERALS

    override fun createElement(node: ASTNode): PsiElement = KerMLPsiUtil.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = KerMLFile(viewProvider)
}
