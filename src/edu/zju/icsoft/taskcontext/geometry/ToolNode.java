package edu.zju.icsoft.taskcontext.geometry;

import org.eclipse.jdt.core.IMember;

public class ToolNode implements Comparable{
	private IMember member;
	private boolean visit;
	private int id,matchId,match_time,remain_step,ind,outd;
	private double confidence;
	private String stereotype = null;
	
	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}

	public ToolNode(IMember member) {
		this.visit = false;
		this.member = member;
	}

	

	public int getInd() {
		return ind;
	}

	public void setInd(int ind) {
		this.ind = ind;
	}

	public int getOutd() {
		return outd;
	}

	public void setOutd(int outd) {
		this.outd = outd;
	}

	public IMember getMember() {
		return member;
	}

	public void setRemain_step(int step) {
		this.remain_step = step;
	}

	public boolean isVisit() {
		return visit;
	}

	public String getStereotype() {
		return stereotype;
	}
	
	public void setVisit(boolean visit) {
		this.visit = visit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public int getMatch_time() {
		return match_time;
	}

	public void setMatch_time(int match_time) {
		this.match_time = match_time;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public int getRemain_step() {
		return remain_step;
	}

	public void setMember(IMember member) {
		this.member = member;
	}
	
	@Override
	public int compareTo(Object o) {
		ToolNode s = (ToolNode)o;
		return this.stereotype.compareTo(s.stereotype);
	}


}
