package edu.zju.icsoft.taskcontext.util.vf3;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.TimeZone;

import edu.zju.icsoft.taskcontext.util.graph.Edge;
import edu.zju.icsoft.taskcontext.util.graph.ExpandGraph;
import edu.zju.icsoft.taskcontext.util.graph.FreGraph;
import edu.zju.icsoft.taskcontext.util.graph.Vertex;



public class PatternMatching {
	private ArrayList<FreGraph> graphs = null;
	private ExpandGraph graph2 = null;
	private long nowTime;
	private HashMap<String, Integer>  stereotypeCount = new HashMap<>();
	private ArrayList<ArrayList<Correspond>> solutions = new ArrayList<ArrayList<Correspond> >();
	private ArrayList<Integer>matchedGraphFre;
	private boolean t = false;
	
	public PatternMatching(ArrayList<FreGraph> graphs, ExpandGraph graph2, int step, long nowTime) {
		this.graphs = graphs;
		this.graph2 = graph2;
		this.nowTime = nowTime;
		initial();
		for(FreGraph g: this.graphs) {
			if(g.getVertexs().size()<=step)continue;
			generateNodeSequence(g);
			HashMap<Vertex, Vertex>parent = preprocessPatternGraph(g);
			findCorrespond(g, parent);
			match(new ArrayList<Correspond>(), g, parent, solutions);
		}
	}

	public ArrayList<ArrayList<Correspond>> getSolutions(){
		return this.solutions;
	}
	
	private void initial() {
		matchedGraphFre = new ArrayList<>();
		for(ToolNode node : this.graph2.getVertexs()) {
			node.setInd(0);
			node.setOutd(0);
			if(stereotypeCount.get(node.getStereotype()) != null) {
				stereotypeCount.replace(node.getStereotype(), stereotypeCount.get(node.getStereotype())+1);
			}
			else stereotypeCount.put(node.getStereotype(), 1);
		}
		for(ToolRelation relation : this.graph2.getEdges()) {
			relation.getNode1().setOutd(relation.getNode1().getOutd()+1);
			relation.getNode2().setInd(relation.getNode2().getInd()+1);
		}
	}
	
	private void computeProbabilities(FreGraph graph1) {

		for(Vertex v : graph1.getVertexs()) {
			v.setInd(0);
			v.setOutd(0);
		}
		for(Edge e : graph1.getEdges()){
			e.getStartV().setOutd(e.getStartV().getOutd()+1);
			e.getEndV().setInd(e.getEndV().getInd()+1);
		}
		for(Vertex v : graph1.getVertexs()) {
			double pl = 0.0 , pdi = 0.0 , pdo = 0.0;
			int incount = 0 , outcount = 0;
			pl = stereotypeCount.get(v.getStereoType()) == null ? 0.0 : 
				((double)stereotypeCount.get(v.getStereoType()))/graph2.getVertexs().size();
			for(ToolNode node : this.graph2.getVertexs()) {
				if(node.getInd() >= v.getInd()) {
					incount++;
				}
				if(node.getOutd() >= v.getOutd()) {
					outcount++;
				}
			}
			pdi = (double)incount/graph2.getVertexs().size();
			pdo = (double)outcount/graph2.getVertexs().size();
			v.setPf(pl*pdi*pdo);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void generateNodeSequence(FreGraph graph1) {
		Collections.sort(graph1.getVertexs());
		
		for(int i = 1 ; i < graph1.getVertexs().size() ; i++) {
			int maxre = 0 , maxid = i , relate = 0;
			for(int j = i ; j < graph1.getVertexs().size() ; j++) {
				for(int k = 0 ; k < i ; k++) {
					for(int r = 0 ; r < graph1.getEdges().size() ; r++) {
						if((graph1.getEdges().get(r).getStartV().getId() == graph1.getVertexs().get(j).getId()) 
								&& ((graph1.getEdges().get(r).getEndV().getId() == graph1.getVertexs().get(k).getId()))) {
							relate++;
						}
						if((graph1.getEdges().get(r).getStartV().getId() == graph1.getVertexs().get(k).getId()) 
								&& ((graph1.getEdges().get(r).getEndV().getId() == graph1.getVertexs().get(j).getId()))) {
							relate++;
						}
					}
				}
				if(relate > maxre) {
					maxre = relate;
					maxid = j;
				}
			}
			Collections.swap(graph1.getVertexs(), i, maxid);
		}
	}
	
	
	private HashMap<Vertex, Vertex> preprocessPatternGraph(FreGraph graph1) {
		HashMap<Vertex, Vertex>parent = new HashMap<>();
		for(int i = 1 ; i < graph1.getVertexs().size() ; i++) {
			boolean t = false;
			for(int j = 0 ; j < i ; j++) {
				if(t)break;
				for(Edge e : graph1.getEdges()) {
					if((e.getStartV().getId() == graph1.getVertexs().get(i).getId()) && 
							(e.getEndV().getId() == graph1.getVertexs().get(j).getId())) {
						t = true;
						parent.put(graph1.getVertexs().get(i), graph1.getVertexs().get(j));
						break;
					}
					if((e.getStartV().getId() == graph1.getVertexs().get(j).getId()) && 
							(e.getEndV().getId() == graph1.getVertexs().get(i).getId())) {
						t = true;
						parent.put(graph1.getVertexs().get(i), graph1.getVertexs().get(j));
						break;
					}
				}
			}
		}
		return parent;
	}
	
	private void match(ArrayList<Correspond>sc, FreGraph graph1, HashMap<Vertex, Vertex>parent, 
			ArrayList<ArrayList<Correspond> > solutions ) {
		if(t)return;
		long time = getTime();
		if(time - nowTime > 5000) {
			t=true;
			solutions.clear();
			return;
		}
		if(sc.size() == graph1.getVertexs().size()) {
			solutions.add((ArrayList<Correspond>) sc.clone());
			matchedGraphFre.add(graph1.getFreq());
			return;
		}
		Correspond s = getCandidate(sc,new Correspond(null,null), parent, graph1);
		while(s != null && s.getT() != null) {
			if(isFeasible(sc,s, graph1)) {
				sc.add(s);
				match(sc, graph1, parent, solutions);
				sc.remove(sc.size()-1);
			}
			s = getCandidate(sc, s, parent, graph1);
		}
	}


	public ArrayList<Integer> getMatchedGraphFre() {
		return matchedGraphFre;
	}

	private Correspond getCandidate(ArrayList<Correspond> sc, Correspond last, HashMap<Vertex, Vertex> parent,
			FreGraph graph1) {
		Vertex un = null;
		ToolNode vn = null;
		if(last.getV() == null) {
			if(sc.size() >= graph1.getVertexs().size()) {
				return null;
			}
			un = graph1.getVertexs().get(sc.size());
		}
		
		else un = last.getV();
		if(parent.get(un) == null) {
			vn = getNextNode(last.getT(),getR(sc,un.getStereoType()));
			return new Correspond(un,vn);
		}
		else {
			ToolNode vparent = parentMatch(sc,parent.get(un));
			if(isPre(un, parent.get(un), graph1)) {
				vn = getNextNode(last.getT(), getRp(sc,un,vparent));
				return new Correspond(un,vn);
			}
			if(isSuc(un, parent.get(un),graph1)) {
				vn = getNextNode(last.getT(), getRs(sc,un,vparent));
				return new Correspond(un,vn);
			}
		}
		return null;
	}

	private boolean isSuc(Vertex un, Vertex vertical, FreGraph graph1) {
		for(Edge e : graph1.getEdges()) {
			if(e.getStartV() == vertical && e.getEndV() == un) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<ToolNode>  getRs(ArrayList<Correspond> sc, Vertex un, ToolNode vparent) {
		ArrayList<ToolNode>r = new ArrayList<>();
		
		for(ToolRelation relate: graph2.getEdges()) {
			if(relate.getNode1() == vparent && relate.getNode2().getStereotype().equals(un.getStereoType())) {
				ToolNode n1 = relate.getNode2();
				boolean t = true;
				for(Correspond c : sc) {
					if(c.getT() == n1) t = false;
				}
				if(t) {
					r.add(n1);
				}
			}
		}
		return r;
	}

	private ArrayList<ToolNode> getRp(ArrayList<Correspond> sc, Vertex un, ToolNode vparent) {
		ArrayList<ToolNode>r = new ArrayList<>();
		
		for(ToolRelation relate: graph2.getEdges()) {
			if(relate.getNode2() == vparent && relate.getNode1().getStereotype().equals(un.getStereoType())) {
				ToolNode n1 = relate.getNode1();
				boolean t = true;
				for(Correspond c : sc) {
					if(c.getT() == n1) t = false;
				}
				if(t) {
					r.add(n1);
				}
			}
		}
		return r;
	}

	private boolean isPre(Vertex un, Vertex vertical, FreGraph graph1) {
		for(Edge e : graph1.getEdges()) {
			if(e.getStartV() == un && e.getEndV() == vertical) {
				return true;
			}
		}
		return false;
	}

	private ToolNode parentMatch(ArrayList<Correspond> sc, Vertex vertical) {
		for(Correspond c : sc) {
			if(c.getV() == vertical) {
				return c.getT();
			}
		}
		return null;
	}

	
	private ToolNode getNextNode(ToolNode t, ArrayList<ToolNode> r) {
		if(t == null && r.size()>0) {
			return r.get(0);
		}
		else if(t == null && r.size() == 0) {
			return null;
		}
		for(int i = 0 ; i < r.size()-1 ; i++) {
			if(r.get(i) == t) {
				return r.get(i+1);
			}
		}
		return null;
	}

	private ArrayList<ToolNode> getR(ArrayList<Correspond> sc, String stereoType) {
		ArrayList<ToolNode>r = new ArrayList<>();
		
		for(ToolNode n : graph2.getVertexs()) {
			
			if(n.getStereotype().equals(stereoType)) {
				r.add(n);
			}
		}
		return r;
	}

	private void findCorrespond(FreGraph graph1, HashMap<Vertex, Vertex> parent) {
		for(Vertex v : graph1.getVertexs()) {
			v.setCandidates(new ArrayList<ToolNode>());
			if(parent.get(v) != null) {
				Vertex pa = parent.get(v);
				int toward = 0;
				for(Edge e : graph1.getEdges()) {
					if(e.getStartV() == v && e.getEndV() == pa) {
						toward = 1;
						break;
					}
				}
				for(ToolNode node : graph2.getVertexs()) {
					if(node.getStereotype().equals(v.getStereoType())) {
						for(ToolRelation relate : graph2.getEdges()) {
							if(toward == 1 && relate.getNode1() == node && relate.getNode2().getStereotype().equals(pa.getStereoType())) {
								v.addCandidates(node);
								break;
							}
							if(toward == 0 && relate.getNode2() == node && relate.getNode1().getStereotype().equals(pa.getStereoType())) {
								v.addCandidates(node);
								break;
							}
						}
					}
				}
				
				
				
			}
			else {
				for(ToolNode node : graph2.getVertexs()) {
					if(node.getStereotype().equals(v.getStereoType())) {
						v.addCandidates(node);
					}
				}
			}
			
		}
	}
	
	private boolean isFeasible(ArrayList<Correspond> sc, Correspond s, FreGraph graph1) {
		for(Correspond c : sc) {
			Vertex u = c.getV();
			ToolNode v = c.getT();
			int tu = 0;
			for(Edge e : graph1.getEdges()) {
				if(e.getStartV() == u && e.getEndV() == s.getV()) {
					tu = 1;
					break;
				}
				if(e.getStartV() == s.getV() && e.getEndV() == u) {
					tu = 2;
					break;
				}
			}

			if(tu == 1) {
				boolean f = false;
				for(ToolRelation relate : graph2.getEdges()) {
					if(relate.getNode1() == v && relate.getNode2() == s.getT()) {
						f = true;
						break;
					}
				}
				if(!f)return false; 
			}
			else if(tu == 2) {
				boolean f = false;
				for(ToolRelation relate : graph2.getEdges()) {
					if(relate.getNode2() == v && relate.getNode1() == s.getT()) {
						f = true;
						break;
					}
				}
				if(!f)return false; 
			}
		}
		
		return true;
	}
	public boolean getT() {
		return t;
	}
	private long getTime() {
		Calendar currenDdate = Calendar.getInstance(TimeZone.getDefault());
		return currenDdate.getTimeInMillis();
	}
	
}

























