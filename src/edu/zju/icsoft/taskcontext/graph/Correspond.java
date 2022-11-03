package edu.zju.icsoft.taskcontext.graph;

import edu.zju.icsoft.taskcontext.geometry.ToolNode;

public class Correspond {
	private Vertex v;
	private ToolNode t;
	
	public Correspond(Vertex v, ToolNode t) {
		this.v = v;
		this.t = t;
	}

	public Vertex getV() {
		return v;
	}

	public void setV(Vertex v) {
		this.v = v;
	}

	public ToolNode getT() {
		return t;
	}

	public void setT(ToolNode t) {
		this.t = t;
	}
	
}
