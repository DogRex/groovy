package org.intellij.openapi.testing.irida;

import com.intellij.execution.runners.ProcessProxyFactory;
import com.intellij.openapi.diff.DiffRequestFactory;
import com.intellij.openapi.fileChooser.FileSystemTreeFactory;
import com.intellij.openapi.ui.DialogWrapperPeerFactory;
import com.intellij.openapi.vcs.FileStatusFactory;
import com.intellij.openapi.vcs.actions.VcsContextFactory;
import com.intellij.peer.PeerFactory;
import com.intellij.psi.search.scope.packageSet.PackageSetFactory;
import com.intellij.ui.UIHelper;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.errorView.ErrorViewFactory;
import com.intellij.ide.structureView.StructureViewFactory;

public class MockPeerFactory extends PeerFactory {

    public FileStatusFactory getFileStatusFactory() {
        return null;
    }

    public DialogWrapperPeerFactory getDialogWrapperPeerFactory() {
        return new MockDialogWrapperPeerFactory();
    }

    public ProcessProxyFactory getProcessProxyFactory() {
        return null;
    }

    public PackageSetFactory getPackageSetFactory() {
        return null;
    }

    public UIHelper getUIHelper() {
        return null;
    }

    public ErrorViewFactory getErrorViewFactory() {
        return null;
    }

    public ContentFactory getContentFactory() {
        return null;
    }

    public FileSystemTreeFactory getFileSystemTreeFactory() {
        return null;
    }

    public DiffRequestFactory getDiffRequestFactory() {
        return null;
    }

    public VcsContextFactory getVcsContextFactory() {
        return null;
    }

    public StructureViewFactory getStructureVeiwFactory() {
        return null; 
    }
}
