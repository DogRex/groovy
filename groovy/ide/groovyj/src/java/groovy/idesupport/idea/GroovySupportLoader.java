package groovy.idesupport.idea;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.application.ApplicationManager;

public class GroovySupportLoader implements ApplicationComponent {
  public static final LanguageFileType GROOVY = new groovy.idesupport.idea.GroovyFileType();

  public void initComponent() {
    ApplicationManager.getApplication().runWriteAction(
      new Runnable() {
        public void run() {
          FileTypeManager.getInstance().registerFileType(GROOVY, new String[] {"groovy", "gy"});
        }
      }
    );
  }

  public void disposeComponent() {
  }

  public String getComponentName() {
    return "groovy support loader";
  }
}
