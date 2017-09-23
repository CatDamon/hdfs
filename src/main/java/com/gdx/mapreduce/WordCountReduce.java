package com.gdx.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * KEYIN, VALUEIN 对应mapper输出的KETOUT,VALUEOUT类型对应
 * KEYOUT, VALUEOUT 是自定义reduce逻辑处理结果的输出数据类型
 * KEYOUT是单词
 * VALUEOUT是总次数
 * */

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
