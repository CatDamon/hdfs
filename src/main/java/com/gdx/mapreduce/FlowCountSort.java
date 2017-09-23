package com.gdx.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.gdx.mapreduce.FlowCount.FlowCountMapper;
import com.gdx.mapreduce.FlowCount.FlowCountReducer;

public class FlowCountSort {

	static class FlowCountSortManager extends Mapper<LongWritable, Text, FlowBean, Text>{
		private FlowBean fb = new FlowBean();
		Text v = new Text();
		
		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, FlowBean, Text>.Context context)
				throws IOException, InterruptedException {
			
			//拿到的是上一个统计程序的输出结果，已经是各个手机号的总流量信息
			String line = value.toString();
			
			String[] fields = line.split("\t");
			
			String phoneNbr = fields[0];
			
			long upFlow = Long.parseLong(fields[1]);
			long dFlow = Long.parseLong(fields[2]);
			
			fb.set(upFlow, dFlow);
			v.set(phoneNbr);
			
			context.write(fb, v);
			
			
			
		}
	}
	
	static class FlowCountSortReducer extends Reducer<FlowBean, Text, Text, FlowBean>{
		
		//<bean(),phonenbr>
		@Override
		protected void reduce(FlowBean bean, Iterable<Text> values,
				Reducer<FlowBean, Text, Text, FlowBean>.Context context) throws IOException, InterruptedException {
			context.write(values.iterator().next(), bean);
		}
	}
	
	public static void main(String[] args) throws Exception, IOException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
		/*job.setJar(args[0]);*/
		//指定本程序的jar包所在的本地路径
		job.setJarByClass(FlowCountSort.class);
		
		
		//指定本业务job要使用的mapper业务类
		job.setMapperClass(FlowCountSortManager.class);
		job.setReducerClass(FlowCountSortReducer.class);
		//指定我们自定的数据分区器
		//job.setPartitionerClass(MyPartition.class);
		//同时指定相应数量的reducetask
		//job.setNumReduceTasks(5);
		
		//指定Mapper输出数据的kv类型
		job.setMapOutputKeyClass(FlowBean.class);
		job.setMapOutputValueClass(Text.class);
		
		//指定最终输出的数的kv类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);
		
		//指定job的输入原始文件所在目录
		//FileInputFormat.setInputPaths(job, new Path("/flowcount/input"));
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		//指定job的输出结果所在目录
		//FileOutputFormat.setOutputPath(job, new Path("/flowcount/output"));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		//将job中配置的相关参数，以及job所用的java类所在的jar，提交给yarn去运行
		/*job.submit();*/
		//true代表集群的运行信息返回
		boolean b = job.waitForCompletion(true);
		System.exit(b?0:1);
	}
}
