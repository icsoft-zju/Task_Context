package edu.zju.icsoft.taskcontext.util.graph;

import java.util.ArrayList;


public class FreGraph {
	private ArrayList<Vertex>vertexs;	
	private ArrayList<Edge>edges;
	private int freq;
	public ArrayList<Vertex> getVertexs() {
		return vertexs;
	}
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	public int getFreq() {
		return freq;
	}
	public void setFreq(int freq) {
		this.freq = freq;
	}
	public FreGraph() {
		vertexs = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	}
	public void addV(Vertex v) {
		this.vertexs.add(v);
	}
	public void addE(Edge e) {
		this.edges.add(e);
	}

}
