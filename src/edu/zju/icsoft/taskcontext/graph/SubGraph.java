package edu.zju.icsoft.taskcontext.graph;

import java.util.ArrayList;

import edu.zju.icsoft.taskcontext.geometry.ToolNode;
import edu.zju.icsoft.taskcontext.geometry.ToolRelation;

public class SubGraph {
	private ArrayList<ToolNode>verticals = new ArrayList<ToolNode>();
	private ArrayList<ToolRelation>edges = new ArrayList<ToolRelation>();
	private int match_time;
	
	public int getMatch_time() {
		return match_time;
	}

	public void addMatch_time(int time) {
		this.match_time += time;
	}

	public SubGraph() {
		match_time = 0;
	}
	
	public ArrayList<ToolNode> getVerticals() {
		return verticals;
	}

	public ArrayList<ToolRelation> getEdges() {
		return edges;
	}

	public void addV(ToolNode node) {
		this.verticals.add(node);
	}
	public void addE(ToolRelation realate) {
		this.edges.add(realate);
	}
	public void removeLast() {
		verticals.remove(verticals.size()-1);
		edges.remove(edges.size()-1);
	}

}
