package groovy.idesupport.idea;

import com.intellij.openapi.fileTypes.FileTypeSupportCapabilities;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ide.structureView.StructureViewModel;

import javax.swing.*;

public class GroovyFileType extends LanguageFileType {
  public GroovyFileType() {
    super(new groovy.idesupport.idea.GroovyLanguage());
  }

  public String getName() {
    return "Groovy";
  }

  public String getDescription() {
    return "Groovy";
  }

  public String getDefaultExtension() {
    return "groovy";
  }

  public Icon getIcon() {
    return IconLoader.getIcon("/icons/groovy_fileType.png");
  }

  public FileTypeSupportCapabilities getSupportCapabilities() {
    return new FileTypeSupportCapabilities() {
      public boolean hasCompletion() {
        return true;
      }

      public boolean hasValidation() {
        return true;
      }

      public boolean hasFindUsages() {
        return true;
      }

      public boolean hasNavigation() {
        return true;
      }

      public boolean hasRename() {
        return true;
      }
    };
  }

  public StructureViewModel getStructureViewModel(VirtualFile file, Project project) {
    return null;
  }
}
