package com.gdx.hdfs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 用流的方式操作HDFS上得文件
 * 可以实现读取指定偏移量范围的文件
 * */
public class HdfsStreamAccess {

	FileSystem fs = null;
	Configuration conf = null;
	@Before
	public void init() throws Exception{
		
		conf = new Configuration();
		conf.set("dfs.replication", "5");
		
		//拿到一个文件系统操作的客户端实例对象
		fs = FileSystem.get(conf);
		//可以直接传入 uri和用户身份
		fs = FileSystem.get(new URI("hdfs://mini01:9000"),conf,"root");
	}
	
	@Test
	public void testWrite() throws Exception, IOException{
		FSDataOutputStream os = fs.create(new Path("/gdx.love"),true);
		FileInputStream is = new FileInputStream("E:/testgdx.txt");
		
		IOUtils.copy(is, os);
		
	}
	
	@Test
	public void testDownLoad() throws Exception, IOException{
		
		FSDataInputStream open = fs.open(new Path("/gdx.love"));
		
		FileOutputStream outputStream = new FileOutputStream("E:/angelababy.love");
		
		IOUtils.copy(open, outputStream);
	}
	
	
	
	
	
	
	
	
	
	
}
