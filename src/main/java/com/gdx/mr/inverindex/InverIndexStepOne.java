package com.gdx.mr.inverindex;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

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
	
	public class WordCountReduce extends Reducer<Text, IntWritable, Text, IntWritable>{

		/**
		 * 入参 key,是一组相同单词kv对的key，例如<h,1><h,1><a,1>
		 * */
		@Override
		protected void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			
			//<a,1> 传第一个key值过来
			int count = 0;
			for (IntWritable value : values) {
				
				count += value.get();
			}
			
			context.write(key, new IntWritable(count));
			
		}
	}
	
	public static void main(String[] args) {
		
	}
}
