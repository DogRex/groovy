package org.intellij.openapi.testing.pallada;

import javax.swing.Icon;

import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.fileTypes.FileType;

public class MockFileTypeFactory implements FileTypeFactory {

    public FileType createFileType(String name, String description, boolean isBinary, String charset) {
        return FileType.UNKNOWN;
    }

    public FileType createReadOnlyFileType(String name, String description, String extensions, Icon icon, boolean isBinary) {
        return FileType.UNKNOWN;
    }

    public FileType createFileType(String name, String description, String defaultExtension, String[] extensions,
                                   Icon icon, boolean isBinary, String charset) {
        return FileType.UNKNOWN;
    }
}
