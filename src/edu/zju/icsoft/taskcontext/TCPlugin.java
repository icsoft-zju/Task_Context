package edu.zju.icsoft.taskcontext;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class TCPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.zju.icsoft.codeelementpredict"; //$NON-NLS-1$

	
	public static final String BAD_WORDS_PREFERENCE = "badwords";
	public static final String HIGHLIGHT_PREFERENCE = "highlight";
	public static final String DEFAULT_BAD_WORDS = "bug;bogus;hack;";
	public static final int DEFAULT_HIGHLIGHT = SWT.COLOR_BLUE;
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		store.setDefault(BAD_WORDS_PREFERENCE, DEFAULT_BAD_WORDS);
		Color color= Display.getDefault().getSystemColor(DEFAULT_HIGHLIGHT);
		PreferenceConverter.setDefault(store,  HIGHLIGHT_PREFERENCE, color.getRGB());

	}
	
	
	// The shared instance
	private static TCPlugin plugin;
	
	/**
	 * The constructor
	 */
	public TCPlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static TCPlugin getDefault() {
		return plugin;
	}

}
