package groovy.idesupport.idea;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.application.ApplicationManager;
import org.apache.log4j.Logger;

public class GroovySupportLoader implements ApplicationComponent {        

    private static final Logger LOG = Logger.getLogger(GroovySupportLoader.class);
    public static final LanguageFileType GROOVY = new groovy.idesupport.idea.GroovyFileType();

    public void initComponent() {
        LOG.debug("in initComponent");
        //System.out.println("initComponent");
        ApplicationManager.getApplication().runWriteAction(
                new Runnable() {
            public void run() {
                FileTypeManager.getInstance().registerFileType(GROOVY, new String[]{"groovy", "gy"});
            }
        }
        );
    }

    public void disposeComponent() {
        LOG.debug("in disposeComponent");
    }

    public String getComponentName() {
        LOG.debug("in getComponentName");
        return "groovy support loader";
    }
}
