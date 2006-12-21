package org.codehaus.groovy.eclipse.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;

/**
 * The GroovyEclipse project does not implement IHyperlinkDetector itself. That
 * is left up to plugins implementing the
 * org.codehaus.groovy.eclipse.hyperlinkDetector extension. This class simply
 * forwards requests to the extension implementation.
 * 
 * @author emp
 */
public class GroovyHyperlinkDetector implements IHyperlinkDetector {
	private static List detectors = new ArrayList();

	static {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg
				.getExtensionPoint("org.codehaus.groovy.eclipse.hyperlinkDetectors");
		IExtension[] extensions = ep.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IExtension extension = extensions[i];
			IConfigurationElement[] configElement = extension
					.getConfigurationElements();
			for (int j = 0; j < configElement.length; j++) {
				try {
					IHyperlinkDetector detector = (IHyperlinkDetector) configElement[j]
							.createExecutableExtension("class");
					registerHyperlinkDetector(detector);
				} catch (CoreException e) {
					// LOG: where to?
					e.printStackTrace();
				}
			}
		}
	}

	public static void registerHyperlinkDetector(IHyperlinkDetector detector) {
		detectors.add(detector);
	}

	public static boolean unregisterHyperlinkDetector(
			IHyperlinkDetector detector) {
		return detectors.remove(detector);
	}

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		List allHyperlinks = new ArrayList();

		for (Iterator iter = detectors.iterator(); iter.hasNext();) {
			IHyperlinkDetector detector = (IHyperlinkDetector) iter.next();
			IHyperlink[] hyperlinks = detector.detectHyperlinks(textViewer,
					region, canShowMultipleHyperlinks);
			if (hyperlinks != null) {
				allHyperlinks.addAll(Arrays.asList(hyperlinks));
			}
		}

		if (allHyperlinks.size() > 0) {
			return (IHyperlink[]) allHyperlinks
					.toArray(new IHyperlink[allHyperlinks.size()]);
		}
		return null;
	}
}
