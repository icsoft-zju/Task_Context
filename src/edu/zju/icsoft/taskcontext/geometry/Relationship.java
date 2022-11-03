package edu.zju.icsoft.taskcontext.geometry;

import org.eclipse.jdt.core.IMember;

public class Relationship{
	private IMember node1;
	private IMember node2;
	private String relate;
	public Relationship(IMember node1,IMember node2,String relate) {
		this.node1 = node1;
		this.node2 = node2;
		this.relate = relate;
	}
	public IMember getNode1() {
		return node1;
	}
	public IMember getNode2() {
		return node2;
	}
	public String getRelate() {
		return relate;
	}
}
