package edu.zju.icsoft.taskcontext.util.graph;

import java.util.ArrayList;

import edu.zju.icsoft.taskcontext.util.vf3.ToolNode;
import edu.zju.icsoft.taskcontext.util.vf3.ToolRelation;


public class ExpandGraph {
	private ArrayList<ToolNode>vertexs = new ArrayList<ToolNode>();
	private ArrayList<ToolRelation>edges = new ArrayList<ToolRelation>();
	
	public ExpandGraph(ArrayList<ToolNode> verticals, ArrayList<ToolRelation> edges) {
		this.vertexs = verticals;
		this.edges = edges;
	}

	public ArrayList<ToolNode> getVertexs() {
		return vertexs;
	}

	public void setVerticals(ArrayList<ToolNode> vertexs) {
		this.vertexs = vertexs;
	}

	public ArrayList<ToolRelation> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<ToolRelation> edges) {
		this.edges = edges;
	}
	
}
