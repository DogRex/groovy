package org.codehaus.groovy.eclipse.actions;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.model.ItemPointer;
import org.codehaus.groovy.eclipse.model.Location;
import org.codehaus.groovy.eclipse.editor.GroovyEditor;

/*
import org.python.pydev.core.REF;
import org.python.pydev.editor.PyEdit;
*/
/**
 * Opens an editor and selects text in it.
 * 
 * Inspired by org.eclipse.jdt.ui.actions.OpenAction, but simplifies all handling in a single class.
 */
public class GroovyOpenAction extends Action {

    public IEditorPart editor;

    public GroovyOpenAction() {
    }

    public void showInEditor(ITextEditor textEdit, Location start, Location end) {
        try {
            IDocument doc = textEdit.getDocumentProvider().getDocument(textEdit.getEditorInput());
            int s = start.toOffset(doc);
            int e = end == null ? s : end.toOffset(doc);
            TextSelection sel = new TextSelection(s, e - s);
            textEdit.getSelectionProvider().setSelection(sel);
        } catch (BadLocationException e1) {
        	if(textEdit instanceof GroovyEditor){
        		GroovyEditor p = (GroovyEditor) textEdit;
        		GroovyPlugin.trace("Error setting selection:"+start+" - "+end+" - "+ /*p.getEditorFile(),*/ e1.getMessage());
        		
        	}else{
        		GroovyPlugin.trace("Error setting selection:"+start+" - "+end + e1.getMessage());
        	}
        }
    }

    public void run(ItemPointer p) {
        editor = null;
        Object file = p.file;

        if (file instanceof IFile) {
        	IFile f = (IFile) file;
            editor = GroovyPlugin.doOpenEditor(f, true);

        } else if (file instanceof IPath) {
            IPath path = (IPath) file;
            editor = GroovyPlugin.doOpenEditor(path, true);

        }
//        else if (file instanceof File) {
//            String absPath = REF.getFileAbsolutePath((File) file);
//			IPath path = Path.fromOSString(absPath);
//            editor = GroovyPlugin.doOpenEditor(path, true);
//        }

        if (editor instanceof ITextEditor && p.start.line >= 0) {
            showInEditor((ITextEditor) editor, p.start, p.end);
        }
    }
}
