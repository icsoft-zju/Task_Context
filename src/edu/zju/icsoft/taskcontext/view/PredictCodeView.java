package edu.zju.icsoft.taskcontext.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.inject.Inject;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import edu.zju.icsoft.taskcontext.TCPluginImages;
import edu.zju.icsoft.taskcontext.geometry.ToolNode;

public class PredictCodeView extends ViewPart {

	private Image cImage = TCPluginImages.IMG_CLASS.createImage();
	private Image iImage = TCPluginImages.IMG_INTERFACE.createImage();
	private Image mImage = TCPluginImages.IMG_METHOD.createImage();
	private Image fImage = TCPluginImages.IMG_FIELD.createImage();
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "edu.zju.icsoft.taskcontext.view.PredictCodeView";
	private ArrayList<ToolNode>predictCode;
	private static PredictCodeWindow window;
	
	public void setPredictCode(ArrayList<ToolNode> predictCode) {
		this.predictCode = predictCode;
	}

	@Inject IWorkbench workbench;
	
	@Override
	public void createPartControl(Composite parent) {
		window = new PredictCodeWindow(parent);
		window.open();
		Table myTable = window.getTable();
		myTable.addListener(SWT.MouseDown,  new Listener() { public void handleEvent(Event event) {
			TableItem item = myTable.getItem( new Point(event.x,event.y));
			if(item == null)return;
			IMember member= (IMember)item.getData();
			IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
		            .findView("edu.zju.icsoft.taskcontext.view.InterestCodeView");
			((InterestCodeView)part).captureElement(member);
			try {
				showMethod(member);
			} catch (PartInitException e) {
				e.printStackTrace();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}});
	}

	

	@Override
	public void setFocus() {
	}

	public void show() {
		Table table = window.getTable();
		table.removeAll();
		sort(predictCode);
		DecimalFormat df = new DecimalFormat("0.0000");
		for(int i = 0;i<predictCode.size();i++) {
			TableItem item = new TableItem(table, SWT.NONE);  
			item.setData(predictCode.get(i).getMember());
			item.setText(new String[] {(predictCode.get(i).getMember() instanceof IMethod ? getMethodName((IMethod)predictCode.get(i).getMember()) :  predictCode.get(i).getMember().getElementName()),predictCode.get(i).getStereotype(),String.valueOf(df.format(predictCode.get(i).getConfidence()))});
			if(predictCode.get(i).getMember() instanceof IField) {
				item.setImage(0,fImage);
			}
			else if(predictCode.get(i).getMember() instanceof IMethod) {
				item.setImage(0,mImage);
			}
			else if(predictCode.get(i).getMember() instanceof IType) {
				try {
					if(((IType)predictCode.get(i).getMember()).isInterface()) {
						item.setImage(0,iImage);
					}
					else item.setImage(0,cImage);
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i = 0; i < 3; i++)
		{  
		    table.getColumn(i).pack();  
		}
	}
	
	private void showMethod(IMember member) throws PartInitException, JavaModelException {
        ICompilationUnit cu = member.getCompilationUnit();
        IEditorPart javaEditor = JavaUI.openInEditor(cu);
        JavaUI.revealInEditor(javaEditor, (IJavaElement)member);
    }
	private void sort(ArrayList<ToolNode> nodes) {
		
		for(int i = 1 ;i< nodes.size();i++) {
			if(nodes.get(i).getConfidence()<=nodes.get(i-1).getConfidence()) {
				continue;
			}
			int insert = i-1;
			while(nodes.get(i).getConfidence()>nodes.get(insert).getConfidence()) {
				insert--;
				if(insert<0)break;
			}
			ToolNode node = nodes.get(i);
			nodes.remove(i);
			nodes.add(insert+1, node);
		}
	}
	private String getMethodName(IMethod method) {
		String identi = method.toString().split(" \\[in ")[0];
		try {
			if(method.isConstructor()) {
				return identi;
			}
			else {
				String name = "";
				String[] total = identi.split(" ");
				ArrayList<String> paras = new ArrayList<String>();
				for(String str:total) {
					if(str.contains("{key=")) {
						continue;
					}
					paras.add(str);
				}
				if(paras.get(0).equals("static")) {
					paras.remove(0);
				}
				name += paras.get(1);
				for(int i=2;i<paras.size();i++) {
					name += " "+paras.get(i);
				}
				name+=":"+paras.get(0);
				return name;
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

}
