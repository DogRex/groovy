package groovy.idesupport.idea.psi;

import com.intellij.extapi.psi.PsiFileBase;
import groovy.idesupport.idea.GroovySupportLoader;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class GroovyFile extends PsiFileBase implements GroovyElement {
  public GroovyFile(Project project, VirtualFile file) {
    super(project, file, GroovySupportLoader.GROOVY.getLanguage());
  }

  public GroovyFile(Project project, String name, CharSequence text) {
    super(project, name, text, GroovySupportLoader.GROOVY.getLanguage());
  }

  public FileType getFileType() {
    return GroovySupportLoader.GROOVY;
  }

  public String toString() {
    return "GroovyFile:" + getName();
  }
}
