package edu.zju.icsoft.taskcontext.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import javax.inject.Inject;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.zju.icsoft.taskcontext.TCPluginImages;
import edu.zju.icsoft.taskcontext.util.analysis.Analyzer;
import edu.zju.icsoft.taskcontext.util.graph.Graph;
import edu.zju.icsoft.taskcontext.util.graph.Relationship;
import edu.zju.icsoft.taskcontext.util.graph.TimeNode;
import edu.zju.icsoft.taskcontext.util.jstereocode.ProjectInformation;
import edu.zju.icsoft.taskcontext.util.jstereocode.StereotypeIdentifier;
import edu.zju.icsoft.taskcontext.util.vf3.ToolNode;





public class InterestCodeView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "edu.zju.icsoft.taskcontext.view.InterestCodeView";
	
	@Inject IWorkbench workbench;
	private static InterestCodeWindow window;
	private ArrayList<Action> stepActions = new ArrayList<Action>();
	private ArrayList<Action> timeActions = new ArrayList<Action>();
	private IJavaProject project = null;
	protected HashMap<String, ProjectInformation> projectsInfo = new HashMap<String, ProjectInformation>();
	
	private ArrayList<TimeNode> timeSlide,temp = new ArrayList<TimeNode>();

	private Image cImage = TCPluginImages.IMG_CLASS.createImage();
	private Image iImage = TCPluginImages.IMG_INTERFACE.createImage();
	private Image mImage = TCPluginImages.IMG_METHOD.createImage();
	private Image fImage = TCPluginImages.IMG_FIELD.createImage();

	private ArrayList<IMember> headnodes = new ArrayList<IMember>();
	private ArrayList<IMember> leafnodes = new ArrayList<IMember>();
	private ArrayList<IMember> allnodes = new ArrayList<IMember>();
	private ArrayList<IMember> querynodes = new ArrayList<IMember>();
	
	private ArrayList<Boolean> visit = new ArrayList<Boolean>();
	private HashMap<String, String> map0 = new HashMap<String, String>();
	private ArrayList<Relationship> map1 = new ArrayList<Relationship>();
	
	private Graph graph = new Graph();
	private long timeslide;
	private int step;
	
	@Override
	public void createPartControl(Composite parent) {
		step = 1;
		timeslide = 5000;
		selectionListener();
		window = new InterestCodeWindow(parent);
		window.open();
		Tree myTree = window.getTree();
		
		initPopup(myTree);
		
		myTree.addMouseListener((MouseListener) new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				TreeItem item = myTree.getItem(new Point(e.x,e.y));
				if(item!=null){
					IMember member = (IMember)item.getData();
					try {
						showMethod(member);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					} catch (JavaModelException e1) {
						e1.printStackTrace();
					}
					captureElement(member);
				}
			}
		});
		
		makeActions();
		contributeToActionBars();
	}

	private class TimeAction extends Action{
		private int time;
		public TimeAction(int time) {
			super("", AS_RADIO_BUTTON);
			this.time = time;
			setText(time+" seconds");
		}
		public void run() {
			timeslide = this.time*1000;
		}
	}
	
	private class StepAction extends Action{
		private int predictionStep;
		public StepAction(int predictionStep) {
			super("",AS_RADIO_BUTTON);
			this.predictionStep = predictionStep;
			setText(predictionStep+"-step");
		}
		public void run() {
			step = this.predictionStep;
		}
	}
	
	private void makeActions() {
		Integer[] times = new Integer[] {5,10,20,30,60}; 
		for(Integer t:times) {
			TimeAction action = new TimeAction(t) ;
			timeActions.add(action);
		}
		
		Integer[] steps = new Integer[] {1,2,3}; 
		for(Integer s:steps) {
			StepAction action = new StepAction(s);
			stepActions.add(action);
		}
	}
	
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
	}
	private void fillLocalPullDown(IMenuManager manager) {
		MenuManager timeSlide = new MenuManager("time sliding");
		MenuManager predictionStep = new MenuManager("prediction steps"); 
		for(Action action: timeActions) {
			timeSlide.add(action);
		}
		for(Action action:stepActions) {
			predictionStep.add(action);
		}
		manager.add(timeSlide);
		manager.add(new Separator());
		manager.add(predictionStep);
	}

	private void initPopup(Tree myTree) {
		Menu menu=new Menu(myTree);
        MenuItem addNode=new MenuItem(menu,SWT.PUSH);
        addNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	if(myTree.getSelection().length == 0) {
            		return;
            	}
            	IMember deleteMember = (IMember) myTree.getSelection()[0].getData();
            	allnodes.remove(myTree.getSelection()[0].getData());
            	for(TimeNode node: timeSlide) {
            		if(node.getMember().getHandleIdentifier().equals(deleteMember.getHandleIdentifier())) {
            			timeSlide.remove(node);
            			break;
            		}
            	}
            	build();
            }
        });
        addNode.setText("Remove");
        
        MenuItem deleteNode=new MenuItem(menu, SWT.PUSH);
        deleteNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	allnodes.clear();
            	build();
            }
        });
        deleteNode.setText("Remove All");
        myTree.setMenu(menu);
	}

	private void selectionListener() {
				
		ISelectionListener selectionListener = new ISelectionListener() {
			@SuppressWarnings("restriction")
			@Override
			public void selectionChanged(IWorkbenchPart arg0, ISelection arg1) {
				if(arg1 instanceof IStructuredSelection) {
					if(((IStructuredSelection) arg1).getFirstElement() instanceof IMember) {
						IMember selected = (IMember) ((IStructuredSelection) arg1).getFirstElement();
						if(selected instanceof IType) {
							try {
								if (!((IType)selected).isAnonymous()) {
									captureElement(selected);
								}
							} catch (JavaModelException e) {
								e.printStackTrace();
							}
						}
						else captureElement(selected);
					}

				}
			}
		};
		ISelectionService service = (ISelectionService) getSite().getService(ISelectionService.class);
		service.addSelectionListener(selectionListener);
	}
	
	public void captureElement(IMember selected) {
		
		IJavaProject pro = ((IMember)selected).getJavaProject();
		if(project == null ||! pro.getHandleIdentifier().equals(project.getHandleIdentifier())) {
			analyzeProject(pro);
		}
		
		TimeNode node = new TimeNode((IMember)selected,getTime());
		refresh(node);
	}

	private void analyzeProject(IJavaProject pro) {
		project = pro;
		map0.clear();
		map1.clear();
		IPackageFragment[] var8;
		int var7;
		try {
			var7 = (var8 = project.getPackageFragments()).length;
			
			for(int var6 = 0; var6 < var7; ++var6) {
		        IPackageFragment packFragment = var8[var6];
		        
		        stereotype(packFragment);
		    }
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}
	private long getTime() {
		Calendar currenDdate = Calendar.getInstance(TimeZone.getDefault());
		return currenDdate.getTimeInMillis();
	}
	
	protected void refresh(TimeNode node) {
		if(!checkExist(node.getMember(), allnodes)) {
			allnodes.add(node.getMember());
			build();
		}
		timeSlide(node);
	}

	@SuppressWarnings("unchecked")
	private void timeSlide(TimeNode node) {
		if(querynodes != null) {
			querynodes.clear();
		}
		temp.clear();
		for(int i = 0 ; i < (timeSlide == null ? 0 : timeSlide.size()) ; i++) {
			if((!timeSlide.get(i).getMember().getHandleIdentifier().equals(node.getMember().getHandleIdentifier())) && (timeSlide.get(i).getTime()+timeslide > node.getTime())) {
				temp.add(timeSlide.get(i));
				querynodes.add(timeSlide.get(i).getMember());
			}
		}
		temp.add(node);
		querynodes.add(node.getMember());
		timeSlide = (ArrayList<TimeNode>) temp.clone();
		fresh();
	}

	private void fresh() {
		long nowTime = getTime();
		graph.setTime(nowTime);
		graph.setMap(map0,map1);
		graph.setMirrorNodes(querynodes);
		graph.initial();
		graph.expand_step(step);
		ArrayList<ToolNode>predictNodes;
		if(graph.getT()) {
			graph.setT();
			timeOut();
			predictNodes=new ArrayList<ToolNode>();
			showPredictionPart(predictNodes);
		}
		else {
			nowTime = getTime();
			predictNodes = graph.getPredictNodes(step,nowTime);
			if(graph.getT()) {
				graph.setT();
				timeOut();
			}
			showPredictionPart(predictNodes);
		}
	}

	private void showPredictionPart(ArrayList<ToolNode> predictNodes) {
		IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
	            .findView("edu.zju.icsoft.taskcontext.view.PredictCodeView");
		((PredictCodeView)part).setPredictCode(predictNodes);
		((PredictCodeView)part).show();
	}

	private void timeOut() {
		Display display = Display.getCurrent();
		Shell shell = new Shell(display);
		MessageBox mBox = new MessageBox(shell);
		mBox.setMessage("Time Out! Please adjust the prediction step.");
		mBox.open();
		graph.setT();
	}
	public void build() {
		headnodes.clear();
		leafnodes.clear();
		for(IMember member:allnodes) {
			
			if(member.getParent() instanceof ICompilationUnit) {
				headnodes.add(member);
			}
			else {
				IMember p=(IMember)member.getParent();
				if(!checkExist(p, allnodes) && member instanceof IType) {
					headnodes.add(member);
				}
				else {
					leafnodes.add(member);
				}
			}
		}
		visit.clear();
		for(int i = 0; i < leafnodes.size(); i++) {
			visit.add(false);
		}
		buildTree();
	}
	
	

	private boolean checkExist(IMember selectedNode,ArrayList<IMember> check) {
		for(IMember node : check) {
			if(node.getHandleIdentifier().equals(selectedNode.getHandleIdentifier())) {
				return true;
			}
		}
		return false;
	}
	

	private void buildTree() {
		Tree myTree = window.getTree();
		myTree.setItemCount(0);
		for(IMember node : headnodes) {
			TreeItem treeItem = new TreeItem(myTree, SWT.NONE);
			treeItem.setData(node);
			treeItem.setText(node.getElementName());
			try {
				if(((IType)node).isInterface()) {
					treeItem.setImage(iImage);
				}
				else {
					treeItem.setImage(cImage);
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			addothernodes(treeItem,(IType)node);
			treeItem.setExpanded(true);
		}
		for(int i=0;i < leafnodes.size();i++) {
			if(visit.get(i))continue;
			TreeItem treeItem = new TreeItem(myTree, SWT.NONE);
			treeItem.setData(leafnodes.get(i));
			if(leafnodes.get(i) instanceof IMethod) {
				treeItem.setText(getMethodName((IMethod)leafnodes.get(i)));
			}
			else {treeItem.setText(leafnodes.get(i).getElementName());}
			if(leafnodes.get(i) instanceof IMethod) {
				treeItem.setImage(mImage);
			}
			else if(leafnodes.get(i) instanceof IField){
				treeItem.setImage(fImage); 
			}
		}
		
	}

	private void addothernodes(TreeItem treeItem, IType node) {
		IJavaElement[] cleaves=null;
		try {
			cleaves = node.getChildren();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < leafnodes.size(); i++) {
			if(visit.get(i))continue;
			for(IJavaElement element:cleaves) {
				
				if(((IMember)element).getHandleIdentifier().equals(((IMember)leafnodes.get(i)).getHandleIdentifier() )) {
					addnodes(treeItem,node,leafnodes.get(i),i);
					
				}
			}
		}
	}

	private void addnodes(TreeItem treeItem,IType item,IMember node,int num) {
		TreeItem newitemItem = new TreeItem(treeItem, SWT.NONE);
		newitemItem.setData(node);
		if(node instanceof IMethod) {
			newitemItem.setText(getMethodName((IMethod)node));
		}
		else {newitemItem.setText(node.getElementName());}
		visit.set(num, true);
		if(node instanceof IMethod) {
			newitemItem.setImage(mImage);
		}
		else if(node instanceof IField){
			
			newitemItem.setImage(fImage); 
		}
		else {
			try {
				if(((IType)node).isInterface()) {
					newitemItem.setImage(iImage);
				}
				else {
					newitemItem.setImage(cImage);
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			addothernodes(newitemItem,(IType)node);
			newitemItem.setExpanded(true);
		}
		
	}


	@Override
	public void setFocus() {
	
	}

	private static String getMethodName(IMethod method) {
		
		String identi = method.toString().split(" \\[in ")[0];
		if(identi.contains("(not open)")) {
			identi = identi.split("(not open)")[0];
			return identi.substring(0, identi.length()-1);
		}
		try {
			if(method.isConstructor()) {
				return identi;
			}
			else {
				String name = "";
				String[] total = identi.split(" ");
				ArrayList<String> paras = new ArrayList<String>();
				for(String str:total) {
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
				
				return name.split("\\{key")[0];
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}
	private void showMethod(IMember member) throws PartInitException, JavaModelException {
        ICompilationUnit cu = member.getCompilationUnit();
        IEditorPart javaEditor = JavaUI.openInEditor(cu);
        JavaUI.revealInEditor(javaEditor, (IJavaElement)member);
    }
	private void stereotype(IPackageFragment packFragment) {
		try {
			ICompilationUnit[] var9;
	        int var8;
			var8 = (var9 = packFragment.getCompilationUnits()).length;
			for(int var7 = 0; var7 < var8; ++var7) {
				
	            ICompilationUnit unit = var9[var7];
	            StereotypeIdentifier identifier = new StereotypeIdentifier();
	            stereotype(unit,identifier);
	        }
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}

	private void stereotype(ICompilationUnit unit, StereotypeIdentifier identifier) {
		ProjectInformation projectInfo = this.getProjectInfo(unit.getJavaProject());
		identifier.setParameters(unit,projectInfo.getMethodsMean(), projectInfo.getMethodsStdDev());
        identifier.identifyStereotypes();
        
        Analyzer analyser = new Analyzer(identifier.getParser(), identifier.getStereotypedElements());
        analyser.update(map0,map1);

	}

	protected ProjectInformation getProjectInfo(IJavaProject project) {
        ProjectInformation projectInformation;
        if (!this.projectsInfo.containsKey(project.getElementName())) {
            projectInformation = new ProjectInformation(project);
            projectInformation.compute();
            this.projectsInfo.put(project.getElementName(), projectInformation);
        } else {
            projectInformation = (ProjectInformation)this.projectsInfo.get(project.getElementName());
        }
        return projectInformation;
    }
	public void setMap(HashMap<String, String> m1, ArrayList<Relationship> m2) {
		map0=m1;
		map1=m2;
	}
}

