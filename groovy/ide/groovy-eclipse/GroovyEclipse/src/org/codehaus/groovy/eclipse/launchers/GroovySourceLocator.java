/* $Id$
 * Created on 31.03.2004
 */
package org.codehaus.groovy.eclipse.launchers;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.ui.JavaUISourceLocator;

/**
 * @author <a href="mailto:blib@mail.com">Boris Bliznukov</a> 
 */
public class GroovySourceLocator extends JavaUISourceLocator {

    public static final String ID = "org.codehaus.groovy.eclipse.editor.groovySourceLocator";

    /* (non-Javadoc)
     * @see org.eclipse.debug.core.model.ISourceLocator#getSourceElement(org.eclipse.debug.core.model.IStackFrame)
     */
    public Object getSourceElement(IStackFrame stackFrame) {
        if (stackFrame instanceof IJavaStackFrame) {
            IJavaStackFrame frame = (IJavaStackFrame) stackFrame;
            try {
                String sourceName = frame.getSourceName();
                if (sourceName != null) {
                    IFile result = ResourcesPlugin.getWorkspace().getRoot()
                            .getFileForLocation(new Path(sourceName));
                    if (result != null) return result;
                }
            } catch (DebugException e) {
                GroovyPlugin.getPlugin().logException("failed to get source element",e);
            }
        }
        return super.getSourceElement(stackFrame);
    }
}
