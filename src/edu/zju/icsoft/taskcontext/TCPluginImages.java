package edu.zju.icsoft.taskcontext;

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.jface.resource.ImageDescriptor;

public class TCPluginImages {
	private static URL fgIconBaseURL= null;
	static {
		fgIconBaseURL= TCPlugin.getDefault().getBundle().getEntry("/icons/c/"); //$NON-NLS-1$
	}

	public static final String CLASS= "class.png";
	public static final String INTERFACE= "interface.png";
	public static final String METHOD= "method.png";
	public static final String FIELD= "field.png";
	
	
	public static final ImageDescriptor IMG_CLASS= create(CLASS);
	public static final ImageDescriptor IMG_INTERFACE= create(INTERFACE);

	public static final ImageDescriptor IMG_METHOD= create(METHOD);
	public static final ImageDescriptor IMG_FIELD= create(FIELD);
	
	private static ImageDescriptor create(String name) {
		try {
			return ImageDescriptor.createFromURL(new URL(fgIconBaseURL, name));
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	
	private TCPluginImages() {
	}
}
