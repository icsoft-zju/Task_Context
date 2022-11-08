package edu.zju.icsoft.taskcontext.util.graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.TimeZone;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import edu.zju.icsoft.taskcontext.util.vf3.Correspond;
import edu.zju.icsoft.taskcontext.util.vf3.PatternMatching;
import edu.zju.icsoft.taskcontext.util.vf3.ToolNode;
import edu.zju.icsoft.taskcontext.util.vf3.ToolRelation;




public class Graph {
	
	private int nowId,total_match,maxVertex,minVertex;
	private HashMap<String, String> map1;
	private ArrayList<Relationship> map2;
	
	private ArrayList<IMember>mirrorNodes;
	private ArrayList<ToolNode>toolNodes;
	private ArrayList<ToolNode>temp = new ArrayList<ToolNode>();
	
	private ArrayList<ToolRelation>toolRelates;
	
	private ArrayList<FreGraph>graphs = new ArrayList<FreGraph>();
	private ArrayList<SubGraph>subGraphs = new ArrayList<SubGraph>();
	
	private ArrayList<String> cutNodes = new ArrayList<String>();
	private boolean t = false;
	private long nowTime;
	

	public Graph() {
		mirrorNodes = new ArrayList<IMember>();
		toolNodes = new ArrayList<ToolNode>();
		toolRelates = new ArrayList<ToolRelation>();
		}
	public void setMirrorNodes(ArrayList<IMember> allnodes) {
		
		this.mirrorNodes.clear();
		for(IMember node : allnodes) {
			this.mirrorNodes.add(node);
		}
	}
	
	public void initial() {
		t=false;
		toolNodes.clear();
		toolRelates.clear();
		graphs.clear();
		subGraphs.clear();
		cutNodes.clear();
		nowId = 0;
		total_match = 0;
		maxVertex = 0;
        minVertex = 9999;
        
        
		for(IMember member:mirrorNodes) {
			ToolNode node = new ToolNode(member);
			node.setId(nowId++);
			toolNodes.add(node);
		}
	}
	public void expand_step(int step) {

      	for(ToolNode node : toolNodes) {
			node.setRemain_step(step);
		}

		for(int i = step ; i > 0; i--) {
			temp.clear();
			for(ToolNode node:toolNodes) {
				if(node.getRemain_step() != i)continue;
				if(node.getMember() instanceof IField) {
					expend_field(node,i);
				}
				else if(node.getMember() instanceof IMethod) {
					expand_method(node,i);
				}
				else {
					expend_type(node,i);
				}
			}

			for(ToolNode node:temp) {
				node.setId(-1);
				toolNodes.add(node);
			}
		}
	}
	
	private void expend_field(ToolNode node, int step) {
		//declared by class
		ToolNode parent = new ToolNode((IMember)(node.getMember().getParent()));
		ToolRelation relate = new ToolRelation(parent, node, "declare");
		if(!checkNodeExist(parent, toolNodes) && !checkNodeExist(parent, temp)) {
			parent.setRemain_step(step-1);
			temp.add(parent);
		}
		if(!checkRelateExist(relate, toolRelates)) {
			toolRelates.add(relate);
		}
	}

	private void expand_method(ToolNode node, int step) {
		//declared by class
		ToolNode parent = new ToolNode((IMember)(node.getMember().getParent()));
		ToolRelation relate = new ToolRelation(parent, node, "declare");
		if(!checkNodeExist(parent, toolNodes) && !checkNodeExist(parent, temp)) {
			parent.setRemain_step(step-1);
			temp.add(parent);
		}
		if(!checkRelateExist(relate, toolRelates)) {
			toolRelates.add(relate);
		}
		//call and called methods
		IMember member = (IMember)node.getMember();
		
		for(Relationship ship:map2) {
			if(t)return;
			if(ship.getNode1().getHandleIdentifier().equals(member.getHandleIdentifier())) {
				ToolNode newNode = new ToolNode(ship.getNode2());
				ToolRelation newRelate = new ToolRelation(node, newNode, ship.getRelate());
				if(!checkNodeExist(newNode, toolNodes) && !checkNodeExist(newNode, temp)) {
					newNode.setRemain_step(step-1);
					temp.add(newNode);
				}
				if(!checkRelateExist(newRelate, toolRelates)) {
					toolRelates.add(newRelate);
				}
			}
			else if(ship.getNode2().getHandleIdentifier().equals(member.getHandleIdentifier())) {
				ToolNode newNode = new ToolNode(ship.getNode1());
				ToolRelation newRelate = new ToolRelation(newNode, node, ship.getRelate());
				if(!checkNodeExist(newNode, toolNodes) && !checkNodeExist(newNode, temp)) {
					newNode.setRemain_step(step-1);
					temp.add(newNode);
				}
				if(!checkRelateExist(newRelate, toolRelates)) {
					toolRelates.add(newRelate);
				}
			}
		}
	}
	private void expend_type(ToolNode node, int step) {
		//declared by class
		if(node.getMember().getParent() instanceof IType) {
			ToolNode parent = new ToolNode((IType)(node.getMember().getParent()));
			ToolRelation relate = new ToolRelation(parent, node, "declare");
			if(!checkNodeExist(parent, toolNodes) && !checkNodeExist(parent, temp)) {
				parent.setRemain_step(step-1);
				temp.add(parent);
			}
			if(!checkRelateExist(relate, toolRelates)) {
				toolRelates.add(relate);
			}
		}
		//declare
		try {
			IJavaElement[] children = node.getMember().getChildren();
			for(IJavaElement element : children) {
				if(t)return;
				if((element instanceof IField)||(element instanceof IMethod)||(element instanceof IType)) {
					ToolNode child = new ToolNode((IMember)element);
					ToolRelation relate = new ToolRelation(node, child, "declare");
					if(!checkNodeExist(child, toolNodes) && !checkNodeExist(child, temp)) {
						child.setRemain_step(step-1);
						temp.add(child);
					}
					if(!checkRelateExist(relate, toolRelates)) {
						toolRelates.add(relate);
					}
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		//inherit and implement
		IMember member = (IMember)node.getMember();
		
		for(Relationship ship:map2) {
			if(t)return;
			if(ship.getNode1().getHandleIdentifier().equals(member.getHandleIdentifier())) {
				ToolNode newNode = new ToolNode(ship.getNode2());
				ToolRelation newRelate = new ToolRelation(node, newNode, ship.getRelate());
				if(!checkNodeExist(newNode, toolNodes) && !checkNodeExist(newNode, temp)) {
					newNode.setRemain_step(step-1);
					temp.add(newNode);
				}
				if(!checkRelateExist(newRelate, toolRelates)) {
					toolRelates.add(newRelate);
				}
			}
			else if(ship.getNode2().getHandleIdentifier().equals(member.getHandleIdentifier())) {
				ToolNode newNode = new ToolNode(ship.getNode1());
				ToolRelation newRelate = new ToolRelation(newNode, node, ship.getRelate());
				if(!checkNodeExist(newNode, toolNodes) && !checkNodeExist(newNode, temp)) {
					newNode.setRemain_step(step-1);
					temp.add(newNode);
				}
				if(!checkRelateExist(newRelate, toolRelates)) {
					toolRelates.add(newRelate);
				}
			}
		}
	
		
	}
	
	private boolean checkNodeExist(ToolNode node,ArrayList<ToolNode>check) {
		long time = getTime();
		if(time - nowTime > 5000) {
			t=true;
			return false;
		}
		for(ToolNode cnode : check) {
			if(node.getMember().getHandleIdentifier().equals(cnode.getMember().getHandleIdentifier())) {
				return true;
			}
		}
		return false;
	}
	
	private long getTime() {
		Calendar currenDdate = Calendar.getInstance(TimeZone.getDefault());
		return currenDdate.getTimeInMillis();
	}
	
	private boolean checkRelateExist(ToolRelation relate,ArrayList<ToolRelation> check) {
		for(ToolRelation ship:check) {
			if(relate.getNode1().getMember().getHandleIdentifier().equals(ship.getNode1().getMember().getHandleIdentifier()) && relate.getNode2().getMember().getHandleIdentifier().equals(ship.getNode2().getMember().getHandleIdentifier()) && relate.getRelate().equals(ship.getRelate())) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<ToolNode> getPredictNodes(int step2, long nowTime) {

		readPatterns();
		assignStereoType();
        System.out.println("match");      
        PatternMatching p = new PatternMatching(graphs, new ExpandGraph(toolNodes, toolRelates),step2, nowTime);
        if(p.getT()) {
        	this.t=true;
        	return new ArrayList<ToolNode>();
        }
        ArrayList<SubGraph> matchgraph = convert(p.getSolutions(), p.getMatchedGraphFre());
       
        System.out.println("complete match");
        
        ArrayList<ToolNode>predictNodes = new ArrayList<ToolNode>();
		filter_expandNodes(matchgraph, predictNodes);
		confidence(predictNodes,matchgraph);
		return predictNodes;
	}
	
	
	private ArrayList<SubGraph> convert(ArrayList<ArrayList<Correspond>> solutions, ArrayList<Integer> initFre) {
		ArrayList<SubGraph> subgraphs = new ArrayList<>();
		for(int i = 0 ; i < solutions.size(); i++) {
			boolean f = false;
			SubGraph g = new SubGraph();
			for(Correspond c : solutions.get(i)) {
				
				g.addV(c.getT());
				if(c.getT().getId()>=0) {
					f = true;
				}
			}
			if(f) {
				g.addMatch_time(initFre.get(i));
				subgraphs.add(g);
			}
		}
		return subgraphs;
	}
	private void readPatterns() {
		
		InputStream is = this.getClass().getResourceAsStream("/pattern_mylyn/pattern_mylyn.txt");
		BufferedReader pattern=new BufferedReader(new InputStreamReader(is));    
		String linestr;
		String[] line;
		try {
			while((linestr = pattern.readLine()) != null) {
				if(linestr.startsWith("t")) {
					FreGraph graph = new FreGraph();
					while ((linestr = pattern.readLine()) != null && (linestr.startsWith("v")||linestr.startsWith("e"))) {
						if(linestr.startsWith("v")) {
			            	line = linestr.split(" ");
			            	Vertex v = new Vertex(Integer.parseInt(line[1]), line[2]);
			            	
			            	String stereo = line[2];
			            	if(!cutNodes.contains(stereo)) {
			            		cutNodes.add(stereo);
			            	}
			            	graph.addV(v);
			            }
						else if(linestr.startsWith("e")) {
							line = linestr.split(" ");
							Vertex v1 = null,v2 =null;
							for(Vertex v:graph.getVertexs()) {
								if(v.getId() == Integer.parseInt(line[1])) {
									v1 = v;
								}
								if(v.getId() == Integer.parseInt(line[2])) {
									v2 = v;
								}
							}
							Edge e = new Edge(v1,v2,line[3]);
							graph.addE(e);
						}
					}
					graph.setFreq(Integer.parseInt(linestr));
					if(graph.getVertexs().size()>maxVertex) {
						maxVertex = graph.getVertexs().size();
					}
					if(graph.getVertexs().size()<minVertex) {
						minVertex = graph.getVertexs().size();
					}
					Collections.sort(graph.getVertexs());
					for(int j=0;j<graph.getVertexs().size();j++) {
						graph.getVertexs().get(j).setId(j);
					}
					graphs.add(graph);
				}
			}
			pattern.close();
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void assignStereoType() {
		for(ToolNode node:toolNodes) {
			if(node.getMember() instanceof IType || node.getMember() instanceof IMethod) {
				String stereoname = map1.get(node.getMember().getHandleIdentifier());
				if(stereoname == null) {
					node.setStereotype("NULL");
				}
				else node.setStereotype(stereoname);
			}
			else {
				node.setStereotype("FIELD");
			}
		}

		for(ToolRelation relate:toolRelates) {
			if(relate.getNode1().getStereotype() == null ) {
				for(ToolNode node:toolNodes) {
					if(node.getMember().getHandleIdentifier().equals(relate.getNode1().getMember().getHandleIdentifier())) {
						relate.setNode1(node);
					}
				}
			}
			if(relate.getNode2().getStereotype() == null ) {
				for(ToolNode node:toolNodes) {
					if(node.getMember().getHandleIdentifier().equals(relate.getNode2().getMember().getHandleIdentifier())) {
						relate.setNode2(node);
					}
				}
			}
		}
		for(int i=0;i<toolNodes.size();i++) {
			if(!cutNodes.contains(toolNodes.get(i).getStereotype())) {
				if(toolNodes.get(i).getId()<0) {
					toolNodes.remove(i);
					i--;
				}
			}
		}
		for(int i = 0 ; i < toolRelates.size() ; i++) {
			ToolRelation relate = toolRelates.get(i);
			String str1,str2;
			str1 = relate.getNode1().getStereotype();
			str2 = relate.getNode2().getStereotype();
			if(!(cutNodes.contains(str2)&&cutNodes.contains(str1))) {
				toolRelates.remove(i);
				i--;
			}
		}
	}
	

	private void filter_expandNodes(ArrayList<SubGraph> matchgragh, ArrayList<ToolNode> predictNodes) {
		for(SubGraph g:matchgragh) {
			for(ToolNode node :g.getVerticals()) {
				if(node.getId() >= 0) {
					continue;
				}
				if(predictNodes.contains(node)) {
					node.setMatch_time(node.getMatch_time()+g.getMatch_time());
				}
				else {
					node.setMatch_time(g.getMatch_time());
					predictNodes.add(node);
				}
			}
		}
	}

	private void confidence(ArrayList<ToolNode> predictNodes,ArrayList<SubGraph> matchgragh) {
		for(SubGraph g:matchgragh) {
			total_match+=g.getMatch_time();
		}
		for(ToolNode node : predictNodes) {
			node.setConfidence(1.0*node.getMatch_time()/total_match);
		}
	}
	public void setMap(HashMap<String, String> m1, ArrayList<Relationship> m2) {
		this.map1=m1;
		this.map2=m2;
	}
	public boolean getT() {
		return t;
	}
	public void setT() {
		t = false;
	}
	public void setTime(long nowTime) {
		this.nowTime = nowTime;
	}
}