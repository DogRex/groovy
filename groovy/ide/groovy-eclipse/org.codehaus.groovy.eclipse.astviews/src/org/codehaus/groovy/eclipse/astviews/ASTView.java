package org.codehaus.groovy.eclipse.astviews;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.codehaus.groovy.eclipse.editor.GroovyEditor;
import org.codehaus.groovy.eclipse.editor.actions.EditorPartFacade;
import org.codehaus.groovy.eclipse.model.GroovyBuildListener;
import org.codehaus.groovy.eclipse.model.GroovyModel;
import org.codehaus.groovy.eclipse.model.GroovyProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

/**
 * A view into the Groovy AST. Anyone who needs to manipulate the AST will find
 * this useful for exploring various nodes.
 */
public class ASTView extends ViewPart { // implements ISelectionListener {
	private TreeViewer viewer;

	private DrillDownAdapter drillDownAdapter;

//	private Action action1;
//
//	private Action action2;

	private Action doubleClickAction;

	private GroovyEditor editor;

	private IPartListener partListener;

	private GroovyBuildListener groovyBuildListener;

	class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
		ITreeNode root;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object inputElement) {
			IFile file = (IFile) inputElement;

			root = TreeNodeFactory.createTreeNode(null, GroovyModel.getModel().getModuleNodes(file), "Module Nodes");

			Object[] children = root.getChildren();
			return children;
		}

		public Object getParent(Object child) {
			Object parent = ((ITreeNode) child).getParent();
			return parent;
		}

		public Object[] getChildren(Object parent) {
			ITreeNode[] children = ((ITreeNode) parent).getChildren();
			return children;
		}

		public boolean hasChildren(Object parent) {
			boolean has = !((ITreeNode) parent).isLeaf();
			return has;
		}
	}

	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return ((ITreeNode) obj).getDisplayName();
		}

		public Image getImage(Object obj) {
			return null;
		}
	}

	/**
	 * The constructor.
	 */
	public ASTView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(null);
		viewer.setInput(null);
		makeActions();
//		hookContextMenu();
		hookDoubleClickAction();
		hookGroovy();
//		contributeToActionBars();
	}

	public void dispose() {
		unhookGroovy();
		super.dispose();
	}

	private void hookGroovy() {
		partListener = new IPartListener() {
			public void partActivated(IWorkbenchPart part) {
			}

			public void partBroughtToTop(IWorkbenchPart part) {
				if (part instanceof GroovyEditor && editor != part) {
					editor = (GroovyEditor) part;
					viewer.setInput(editor.getEditorInput().getAdapter(IFile.class));
					return;
				}
				editor = null;
				// This is a guard - the content provider should not be null,
				// but sometimes this happens when the
				// part is disposed of for various reasons (unhandled exceptions
				// AFAIK). Without this guard,
				// error message popups continue until Eclipse if forcefully
				// killed.
				if (viewer.getContentProvider() != null) {
					viewer.setInput(null);
				}
			}

			public void partClosed(IWorkbenchPart part) {
			}

			public void partDeactivated(IWorkbenchPart part) {
			}

			public void partOpened(IWorkbenchPart part) {
			}
		};
		getSite().getPage().addPartListener(partListener);

		// Warm the listener up.
		if (getSite().getPage().getActiveEditor() instanceof GroovyEditor) {
			partListener.partBroughtToTop(getSite().getPage().getActiveEditor());
		}

		// Listen to when the build changes.
		groovyBuildListener = new GroovyBuildListener() {
			public void fileBuilt(final IFile file) {
				// HACK ALERT:
				// fileBuilt is notified regardless of errors. However nothing
				// has been build, ie. there are no module nodes for this
				// file. The only way to check for errors is to get the error
				// markers.
				// These are set as a workspace operation, so this method
				// will also have to do so, else it will check for markers
				// before they have had a chance to be set.
				// Note that for some reason markers are not always set, so this
				// is not reliable.
				try {
					ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
						public void run(IProgressMonitor monitor) throws CoreException {
							// The editor is currently not a GroovyEditor, so
							// there is not
							// ASTView to refresh.
							if (editor == null) {
								return;
							}

							// Nothing to do, this is not a file that is being
							// editied.
							IFile editorFile = (IFile) editor.getEditorInput().getAdapter(IFile.class);
							if (!file.equals(editorFile)) {
								return;
							}

							// HACK: Check for errors.
							IMarker[] markers = editorFile.findMarkers(GroovyProject.GROOVY_ERROR_MARKER, false,
									IResource.DEPTH_INFINITE);
							if (markers.length != 0) {
								viewer.setInput(null);
								return;
							}

							viewer.setInput(file);
						}
					}, null);
				} catch (CoreException e) {
					GroovyPlugin.getPlugin().logException(e.getMessage(), e);
				}
			}
		};

		GroovyModel.getModel().addBuildListener(groovyBuildListener);
	}

	private void unhookGroovy() {
		GroovyModel.getModel().removeBuildListener(groovyBuildListener);
		getSite().getPage().removePartListener(partListener);
	}

//	private void hookContextMenu() {
//		MenuManager menuMgr = new MenuManager("#PopupMenu");
//		menuMgr.setRemoveAllWhenShown(true);
//		menuMgr.addMenuListener(new IMenuListener() {
//			public void menuAboutToShow(IMenuManager manager) {
//				ASTView.this.fillContextMenu(manager);
//			}
//		});
//		Menu menu = menuMgr.createContextMenu(viewer.getControl());
//		viewer.getControl().setMenu(menu);
//		getSite().registerContextMenu(menuMgr, viewer);
//	}
//
//	private void contributeToActionBars() {
//		IActionBars bars = getViewSite().getActionBars();
//		fillLocalPullDown(bars.getMenuManager());
//		fillLocalToolBar(bars.getToolBarManager());
//	}
//
//	private void fillLocalPullDown(IMenuManager manager) {
//		manager.add(action1);
//		manager.add(new Separator());
//		manager.add(action2);
//	}
//
//	private void fillContextMenu(IMenuManager manager) {
//		manager.add(action1);
//		manager.add(action2);
//		manager.add(new Separator());
//		drillDownAdapter.addNavigationActions(manager);
//		// Other plug-ins can contribute there actions here
//		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
//	}
//
//	private void fillLocalToolBar(IToolBarManager manager) {
//		manager.add(action1);
//		manager.add(action2);
//		manager.add(new Separator());
//		drillDownAdapter.addNavigationActions(manager);
//	}

	private void makeActions() {
//		action1 = new Action() {
//			public void run() {
//				showMessage("Action 1 executed");
//			}
//		};
//		action1.setText("Action 1");
//		action1.setToolTipText("Action 1 tooltip");
//		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
//				ISharedImages.IMG_OBJS_INFO_TSK));
//
//		action2 = new Action() {
//			public void run() {
//				showMessage("Action 2 executed");
//			}
//		};
//		action2.setText("Action 2");
//		action2.setToolTipText("Action 2 tooltip");
//		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
//				ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				if (obj == null) {
					return;
				}
				if (((ITreeNode) obj).getValue() instanceof ASTNode) {
					Object value = ((ITreeNode) obj).getValue();
					if (!(value instanceof ASTNode)) {
						return;
					}

					ASTNode node = (ASTNode) value;
					if (node.getLineNumber() != -1) {
						EditorPartFacade facade = new EditorPartFacade(editor);
						try {
							int offset0 = facade.getOffset(node.getLineNumber() - 1, node.getColumnNumber() - 1);
							int offset1 = facade
									.getOffset(node.getLastLineNumber() - 1, node.getLastColumnNumber() - 1);
							// editor.setHighlightRange(offset0, offset1 -
							// offset0, true);
							facade.select(offset0, offset1 - offset0);
						} catch (BadLocationException e) {
						}
					}
				}
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Groovy AST View", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}