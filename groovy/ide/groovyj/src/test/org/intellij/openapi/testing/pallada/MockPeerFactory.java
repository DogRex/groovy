package org.intellij.openapi.testing.pallada;

import java.awt.Color;

import com.intellij.execution.runners.ProcessProxyFactory;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.ui.DialogWrapperPeerFactory;
import com.intellij.openapi.ui.TableViewFactory;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusFactory;
import com.intellij.openapi.vcs.checkin.DifferenceType;
import com.intellij.peer.PeerFactory;
import com.intellij.psi.search.scope.packageSet.PackageSetFactory;

public class MockPeerFactory extends PeerFactory {

    public FileStatusFactory getFileStatusFactory() {
        return new FileStatusFactory() {
            public FileStatus createFileStatus(String id, String description, Color color) {
                return new FileStatus() {
                    public Color getColor() { return null; }

                    public Color getDefaultColor() { return null; }

                    public String getText() { return null; }
                };
            }

            public DifferenceType createDifferenceTypeInserted() {
                return null;
            }

            public DifferenceType createDifferenceTypeDeleted() {
                return null;
            }

            public DifferenceType createDifferenceTypeNotChanged() {
                return null;
            }

            public DifferenceType createDifferenceTypeModified() {
                return null;
            }

            public DifferenceType createDifferenceType(String id,
                                                       FileStatus fileStatus,
                                                       String mainTextColorKey,
                                                       String leftTextColorKey,
                                                       String rightTextColorKey,
                                                       Color background,
                                                       Color activeBgColor) {
                return null;
            }
        };
    }

    public FileTypeFactory getFileTypeFactory() {
        return new MockFileTypeFactory();
    }

    public DialogWrapperPeerFactory getDialogWrapperPeerFactory() {
        return new MockDialogWrapperPeerFactory();
    }

    public ProcessProxyFactory getProcessProxyFactory() {
        return null;
    }

    public TableViewFactory getTableViewFactory() {
        return null;
    }

    public PackageSetFactory getPackageSetFactory() {
        return null;
    }
}
