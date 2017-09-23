package com.gdx.hdfs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 客户端去操作hdfs时，是有一个用户身份的
 * 默认情况下，hdfs客户端api会从jvm中获取一个参数来作为自己的用户身份：-DHADOOP_USER_NAME=hadoop
 * 
 * 也可以在构造客户端fs对象时，通过参数传递进去
 * @author
 *
 */
public class HdfsClient {
	FileSystem fs = null;
	Configuration conf = null;
	@Before
	public void init() throws Exception{
		
		conf = new Configuration();
//		conf.set("fs.defaultFS", "hdfs://mini1:9000");
		conf.set("dfs.replication", "5");
		
		//拿到一个文件系统操作的客户端实例对象
		fs = FileSystem.get(conf);
		//可以直接传入 uri和用户身份
		fs = FileSystem.get(new URI("hdfs://mini01:9000"),conf,"root");
	}

	
	@Test
	public void testUpload() throws Exception, IOException{
		fs.copyFromLocalFile(new Path("E:/testgdx.txt"), new Path("/testgdx.txt.copy"));
		fs.close();
	}
	
	@Test
	public void testDownload() throws Exception {
		
		fs.copyToLocalFile(new Path("/boot.sh.copy"), new Path("E:/"));
		fs.close();
	}
	
	@Test
	public void testConf(){
		Iterator<Entry<String, String>> iterator = conf.iterator();
		while(iterator.hasNext()){
			Entry<String, String> next = iterator.next();
			System.out.println(next.getKey()+"...."+next.getValue());
		}
	}
	
	@Test
	public void testMkdir() throws Exception{
		boolean mkdirs = fs.mkdirs(new Path("/testmkdir/aa/bb"));
		System.out.println(mkdirs);
	}
	
	@Test
	public void testDelete() throws Exception{
		boolean del = fs.delete(new Path("/testmkdir"),true);
		System.out.println(del);
	}
	
	@Test
	public void testLs() throws Exception{
		RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/test0808.txt"), true);
		while(listFiles.hasNext()){
			LocatedFileStatus next = listFiles.next();
			
			System.out.println("blocksize:"+next.getBlockSize());
			System.out.println("owner:"+next.getOwner());
			System.out.println("Replication:"+next.getReplication());
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration(); 
		FileSystem fs = FileSystem.get(conf);
		fs.copyToLocalFile(new Path("E:/boot.sh"), new Path("/boot.sh.txt"));
		fs.close();
		
	}
}
