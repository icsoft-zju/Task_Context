package edu.zju.icsoft.taskcontext.util.vf3;

import edu.zju.icsoft.taskcontext.util.graph.Vertex;

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
