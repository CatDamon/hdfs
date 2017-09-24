package com.gdx.sharefriends;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class ShareFriendsStepOne {

	
	static class ShareFriendsStepOneMapper extends Mapper<LongWritable, Text, Text, Text>{
		@Override
		protected void map(LongWritable key, Text value,Context context)
				throws IOException, InterruptedException {
				//A:B,C,D,E,F,
				String line = value.toString();
				String[] person_friends = line.split(":");
				String person = person_friends[0];
				String friends = person_friends[1];
				
				for(String  friend:person_friends[1].split(",")){
					
					//输出<好友，人>
					context.write(new Text(friend), new Text(person));
				}
		}
	}
	
	static class ShareFriendsStepOneReducer extends Reducer<Text, Text, Text, Text>{
		@Override
		protected void reduce(Text friend, Iterable<Text> persons, Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
				StringBuffer sb = new StringBuffer();
				for(Text person:persons){
					sb.append(person).append(",");
				}
				context.write(friend, new Text(sb.toString()));
		}
	}
}
