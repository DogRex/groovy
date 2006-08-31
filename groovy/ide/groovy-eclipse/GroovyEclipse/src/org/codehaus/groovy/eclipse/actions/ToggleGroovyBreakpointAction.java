/* $Id$
 * Created on 31.03.2004
 */
package org.codehaus.groovy.eclipse.actions;

import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.GroovyProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.debug.core.IJavaLineBreakpoint;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author <a href="mailto:blib@mail.com">Boris Bliznukov</a>
 */
public class ToggleGroovyBreakpointAction extends Action implements IAction {

    private ITextEditor fEditor;

    private IVerticalRulerInfo fRulerInfo;

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        IBreakpointManager manager = DebugPlugin.getDefault()
                .getBreakpointManager();
        IBreakpoint[] breakpoints = manager.getBreakpoints();
        IResource resource = getResource();
        int lineNumber = fRulerInfo.getLineOfLastMouseButtonActivity() + 1;
        for (int i = 0; i < breakpoints.length; i++) {
            IBreakpoint bp = breakpoints[i];
            if (bp instanceof IJavaLineBreakpoint) {
                IJavaLineBreakpoint breakpoint = (IJavaLineBreakpoint) bp;
                if (breakpoint.getMarker().getResource().equals(resource)) {
                    try {
                        if (breakpoint.getLineNumber() == lineNumber) {
                            // remove
                            breakpoint.delete();
                            return;
                        }
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        createBreakpoint();
    }

    protected void createBreakpoint() {
		GroovyModel model = GroovyModel.getModel();
        IResource resource = getResource();
		IFile file = (IFile) resource;
		GroovyProject groovyProject = model.getProject(file.getProject());
		List classes  = groovyProject.model().getClassesForFile( file );
		if (classes.isEmpty()) {
			groovyProject.compileGroovyFile(file, false); //generateClassFiles
			classes = groovyProject.model().getClassesForFile( file );
			GroovyPlugin.trace("creating modules inside of createBreakPoint(), file:" + file);
		}
		// TODO: This is suspect - get(0) works for most cases but can't work for all cases
		ClassNode classNode = (ClassNode) classes.get(0);
        int lineNumber = fRulerInfo.getLineOfLastMouseButtonActivity() + 1;
        try {
        	JDIDebugModel.createLineBreakpoint(resource,classNode.getName(),lineNumber,-1,-1,0,true,null);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param editor
     * @param rulerInfo
     */
    public ToggleGroovyBreakpointAction(ITextEditor editor,
            IVerticalRulerInfo rulerInfo) {
        super("Toggle Breakpoint");
        fEditor = editor;
        fRulerInfo = rulerInfo;
    }

    protected IResource getResource() {
        IEditorInput input = fEditor.getEditorInput();
        IResource resource = (IResource) input.getAdapter(IFile.class);
        if (resource == null) {
            resource = (IResource) input.getAdapter(IResource.class);
        }
        return resource;
    }
}
