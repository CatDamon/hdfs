package com.gdx.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class FlowBean implements WritableComparable<FlowBean>{

	private long upFlow;
	private long downFolw;
	private long sum;
	
	public FlowBean() {
	}
	
	public FlowBean(long upFlow, long downFolw) {
		this.upFlow = upFlow;
		this.downFolw = downFolw;
		this.sum = upFlow+downFolw;
	}
	
	public void set(long upFlow, long downFolw) {
		this.upFlow = upFlow;
		this.downFolw = downFolw;
		this.sum = upFlow+downFolw;
	}
	
	
	
	public long getSum() {
		return sum;
	}



	public void setSum(long sum) {
		this.sum = sum;
	}



	public long getUpFlow() {
		return upFlow;
	}
	public void setUpFlow(long upFlow) {
		this.upFlow = upFlow;
	}
	public long getDownFolw() {
		return downFolw;
	}
	public void setDownFolw(long downFolw) {
		this.downFolw = downFolw;
	}
	
	//序列化方法
	public void write(DataOutput out) throws IOException {
		out.writeLong(upFlow);
		out.writeLong(downFolw);
		out.writeLong(sum);
	}
	
	//反序列化方法
	/*
	 * 注意反序列化顺序和序列化顺序完全一致
	 * */
	public void readFields(DataInput in) throws IOException {
		upFlow = in.readLong();
		downFolw = in.readLong();
		sum = in.readLong();
	}
	
	@Override
	public String toString() {
		return upFlow + "\t" + downFolw + "\t" + sum;
	}

	public int compareTo(FlowBean o) {
		return this.sum>o.getSum()?-1:1;
	}
	
	
}
