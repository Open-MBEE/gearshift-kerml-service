package org.openmbee.gearshift.intellij.editor

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import org.openmbee.gearshift.intellij.parser.KerMLTokenTypes

class KerMLQuoteHandler : SimpleTokenSetQuoteHandler(KerMLTokenTypes.STRING_LITERALS)
