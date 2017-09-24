package com.gdx.sharefriends;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class MyShareFriendsStepOne {

	static class MyShareFriendsStepMapper extends Mapper<LongWritable, Text, Text, Text>{
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
				String line = value.toString();
				
				//A: B,C,D,E,F,G,H
				String[] split = line.split(":");
				String person = split[0]; //某个人
				String friends = split[1]; //好友
				//每次输出 B:A /C:A/ D:A
				for (String friend : friends.split(",")) {
					context.write(new Text(friend), new Text(person));
				}
		
		}
	}
	
	
	static class MyShareFriendsStepReducer extends Reducer<Text, Text, Text, Text>{
		@Override
		protected void reduce(Text friend, Iterable<Text> person, Context context)
				throws IOException, InterruptedException {
			//每次输入 	B:A /C:A/ D:A
			Iterator<Text> it = person.iterator();
			StringBuffer sb = new StringBuffer();
			while(it.hasNext()){
				sb.append(it.next()).append(",");
			}
			context.write(friend, new Text(sb.toString()));
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		System.setProperty("hadoop.home.dir", "D:/hadoop-2.6.1");
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
		job.setJarByClass(MyShareFriendsStepOne.class);
		
		job.setMapperClass(MyShareFriendsStepMapper.class);
		job.setReducerClass(MyShareFriendsStepReducer.class);
		
		job.setOutputKeyClass(Text.class);
		
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.setInputPaths(job, new Path("F:/testhadoop/sharefriend.txt"));
		FileOutputFormat.setOutputPath(job, new Path("F:/testhadoop/sharefrendoutput"));
		
		
		//指定需要缓存一个文件到所有的maptask的运行节点的工作目录
		/**
		 * job.addArchiveToClassPath(archive);   缓存jar包到task运行节点的classpath中
		 *
		job.addFileToClassPath(file);  //缓存普通文件到task运行节点的classpath中
		job.addCacheArchive(uri); 	//缓存压缩包文件到task运行节点的工作目录
		job.addCacheFile(uri);   	//缓存普通文件到task运行节点的个工作目录
		 */
		
		//map端join的逻辑不需要reduce阶段，设置reduce
		
		
		job.waitForCompletion(true);
	}
}
