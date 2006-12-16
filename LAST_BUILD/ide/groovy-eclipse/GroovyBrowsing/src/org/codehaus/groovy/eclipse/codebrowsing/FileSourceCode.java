package org.codehaus.groovy.eclipse.codebrowsing;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Source code that is represented by a source file.
 * 
 * @author emp
 */
public class FileSourceCode extends AbstractSourceCode {
	private IFile file;

	/**
	 * @param file The file to open.
	 * @param offset The offset in the file that is of interest.
	 * @param length The length of the area of interest.
	 */
	public FileSourceCode(IFile file, IRegion region) {
		super(file, region);
		this.file = file;
	}
	
	public boolean open() {
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			IEditorDescriptor desc = PlatformUI.getWorkbench()
					.getEditorRegistry().getDefaultEditor(((IFile)file).getName());
			try {
				IEditorPart part = page.openEditor(new FileEditorInput(file),
						desc.getId());
				IRegion hregion = getRegionOfInterest();
				ITextEditor editor = (ITextEditor) part;
				editor
						.selectAndReveal(hregion.getOffset(), hregion
								.getLength());
			} catch (PartInitException e) {
				// LOG: need own logging.
				GroovyPlugin.getPlugin().logException("Should not happen.", e);
				e.printStackTrace();
				return false;
			}
		} catch (NullPointerException e) {
			// LOG: Rare case that somehow workbench or active window or
			// page is null.
			return false;
		}
		return true;
	}
}
