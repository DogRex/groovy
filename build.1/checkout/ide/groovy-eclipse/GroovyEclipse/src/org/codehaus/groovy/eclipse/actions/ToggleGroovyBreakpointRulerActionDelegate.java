/* $Id$
 * Created on 31.03.2004
 */
package org.codehaus.groovy.eclipse.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.texteditor.AbstractRulerActionDelegate;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author <a href="mailto:blib@mail.com">Boris Bliznukov</a> 
 */
public class ToggleGroovyBreakpointRulerActionDelegate extends
        AbstractRulerActionDelegate {

    /* (non-Javadoc)
     * @see org.eclipse.ui.texteditor.AbstractRulerActionDelegate#createAction(org.eclipse.ui.texteditor.ITextEditor, org.eclipse.jface.text.source.IVerticalRulerInfo)
     */
    protected IAction createAction(ITextEditor editor,
            IVerticalRulerInfo rulerInfo) {
        return new ToggleGroovyBreakpointAction(editor, rulerInfo);
    }

}
