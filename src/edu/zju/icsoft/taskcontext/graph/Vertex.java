package edu.zju.icsoft.taskcontext.graph;

import java.util.ArrayList;

import edu.zju.icsoft.taskcontext.geometry.ToolNode;

@SuppressWarnings("rawtypes")
public class Vertex implements Comparable{
	private int id;
	private int ind,outd;
	private double pf = 0;
	private ArrayList<ToolNode>candidates;
	

	public ArrayList<ToolNode> getCandidates() {
		return candidates;
	}

	public void setCandidates(ArrayList<ToolNode> candidates) {
		this.candidates = candidates;
	}

	public void addCandidates(ToolNode candidate) {
		this.candidates.add(candidate);
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

	public double getPf() {
		return pf;
	}

	public void setPf(double pf) {
		this.pf = pf;
	}

	private String stereoType;
	
	public Vertex(int id,String stereoType) {
		this.id = id;
		this.stereoType = stereoType;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getStereoType() {
		return stereoType;
	}
	
	@Override
	public int compareTo(Object o) {
		Vertex s = (Vertex)o;
		if(this.pf != s.getPf()) {
			return this.pf - s.getPf() > 0 ? 1 : -1;
		}
		return this.getInd()+this.getOutd() - s.getInd()- s.getOutd() > 0 ? 1 : -1;
	}
	
}
