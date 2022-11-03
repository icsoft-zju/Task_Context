package edu.zju.icsoft.taskcontext.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;

import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class PredictCodeWindow{

	private Composite composite;
	private Table table;

	public Table getTable() {
		return table;
	}

	/**
	 * Launch the application.
	 * @param args
	 */

	public PredictCodeWindow(Composite parent) {
		this.composite = parent;
		this.table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		String[] tableHeader = {"Element", "Stereotype", "Confidence"};  
		for (int i = 0; i < tableHeader.length; i++)  
		{  
		    TableColumn tableColumn = new TableColumn(table, SWT.NONE);  
		    tableColumn.setText(tableHeader[i]);  
		    if(i == 0) {
		    	tableColumn.setWidth(0);
		    	tableColumn.setResizable(false);
		    }
		}
		for (int i = 0; i < 3; i++)
		{  
		    table.getColumn(i).pack();  
		}
		open();
	}

	/**
	 * Open the window.
	 */
	public void open() {
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}
}
