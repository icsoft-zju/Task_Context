package edu.zju.icsoft.taskcontext.util.graph;

import org.eclipse.jdt.core.IMember;

public class TimeNode {
	private IMember member;
	private long time;
	
	public TimeNode(IMember member, long time) {
		this.member = member;
		this.time = time;
	}
	
	public IMember getMember() {
		return member;
	}
	public long getTime() {
		return time;
	}	
}
