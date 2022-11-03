package edu.zju.icsoft.taskcontext.graph;

public class Edge{
	Vertex startV,endV;
	String relate;
	public Edge(Vertex startV,Vertex endV,String relate) {
		this.startV = startV;
		this.endV = endV;
		this.relate = relate;
	}
	
	public Vertex getStartV() {
		return startV;
	}

	public Vertex getEndV() {
		return endV;
	}

	public String getRelate() {
		return relate;
	}
	
}
