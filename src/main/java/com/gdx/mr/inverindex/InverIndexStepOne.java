package com.gdx.mr.inverindex;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapreduce.Mapper;

public class InverIndexStepOne {

	static class InverIndexStepOneMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
		Text k = new Text();
		IntWritable v = new IntWritable(1);
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
				String line = value.toString();
				
				String[] words = line.split(" ");
				
				FileSplit inputSplit = (FileSplit) context.getInputSplit();
				String fileName = inputSplit.getPath().getName();
				
				for(String word : words){
					k.set(word + "--" + fileName);
					context.write(k, v);
				}
		
		}
	}
	
}
