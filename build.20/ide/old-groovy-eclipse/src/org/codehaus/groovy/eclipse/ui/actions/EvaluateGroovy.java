package org.codehaus.groovy.eclipse.ui.actions;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import org.eclipse.core.internal.resources.File;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Evaluates a file or piece of text as a Groovy script and executes it
 * 
 * @author James Strachan
 * @version $Revision$
 */
public class EvaluateGroovy implements IObjectActionDelegate,  IEditorActionDelegate {

    private File selectedFile;
    private String selectedText;

    /**
     * Constructor for Action1.
     */
    public EvaluateGroovy() {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        //System.out.println("ActivePart: " + targetPart);
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action) {
        String[] args = {
        };
        try {
            // TODO temporary hack to work around a classloader bug
            GroovyShell groovy = new GroovyShell(GroovyShell.class.getClassLoader(), new Binding());
            if (selectedText != null) {
                String fileName = "Dummy.groovy";
                groovy.run(selectedText, fileName, args);
            }
            else if (selectedFile != null) {
                java.io.File file = selectedFile.getFullPath().toFile();
                groovy.run(selectedFile.getContents(), file.toString(), args);
            }
            else {
                Shell shell = new Shell();
                MessageDialog.openInformation(shell, "Groovy Plug-in", "No Groovy code highlighted to be run.");
            }
        }
        catch (Exception e) {
            Shell shell = new Shell();
            MessageDialog.openInformation(shell, "Groovy Plug-in", "Failed to evaluate Groovy script.\n" + e);
            e.printStackTrace();
        }
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        selectedFile = null;
        selectedText = null;
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            Object object = structuredSelection.getFirstElement();
            //System.out.println("Chose: " + object + " with type: " + object.getClass());
            if (object instanceof File) {
                selectedFile = (File) object;
            }
        }
        else if (selection instanceof ITextSelection) {
            ITextSelection textSelection = (ITextSelection) selection;
            selectedText = textSelection.getText();
        }
    }
    

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        System.out.println("Editor: " + targetEditor);
        if (targetEditor != null) {
            System.out.println("class: " + targetEditor.getClass());
        }
    }

}
