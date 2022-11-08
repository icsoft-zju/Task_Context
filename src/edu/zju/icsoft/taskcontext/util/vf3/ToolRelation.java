package edu.zju.icsoft.taskcontext.util.vf3;

public class ToolRelation{
	private ToolNode node1;
	private ToolNode node2;
	private String relate;
	public ToolRelation(ToolNode node1,ToolNode node2,String relate) {
		this.node1 = node1;
		this.node2 = node2;
		this.relate = relate;
	}
	public ToolNode getNode1() {
		return node1;
	}
	public ToolNode getNode2() {
		return node2;
	}
	public void setNode1(ToolNode node1) {
		this.node1 = node1;
	}
	public void setNode2(ToolNode node2) {
		this.node2 = node2;
	}
	public String getRelate() {
		return relate;
	}
}
