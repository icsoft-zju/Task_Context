package edu.zju.icsoft.taskcontext.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.layout.GridData;

public class InterestCodeWindow{
	
	private Composite composite;
	private Tree tree;
	
	public Tree getTree() {
		return tree;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public InterestCodeWindow(Composite composite) {
		this.composite = composite;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		createContents();
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.right = new FormAttachment(85);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new GridLayout(2, false));
		
		tree = new Tree(composite, SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
	}
}
