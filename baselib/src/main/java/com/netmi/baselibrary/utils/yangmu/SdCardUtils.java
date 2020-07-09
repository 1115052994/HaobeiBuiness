package com.netmi.baselibrary.utils.yangmu;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SdCardUtils {
	private static final String TAG = "ym";
	/**
	 * 判断sdCard是否能用*/
	public boolean isSafe() {
		return getSdFile().equals(Environment.MEDIA_MOUNTED);
	}
	/**
	 * 获取sdCard内存总大小*/
	public String getTotalSize(Context context) {
		//获取根目录
		File path = getSdFile();
		// 获取指定目录下的内存存储状态
		StatFs stat = new StatFs(path.getPath());
		//获取每个扇区的大小
		long blockSize = stat.getBlockSize();
		//获取扇区的数量
		long totalBlocks = stat.getBlockCount();
		// 总空间 = 扇区的总数 * 扇区的大小
		long totalSize = blockSize * totalBlocks;
		String size = Formatter.formatFileSize(context, totalSize);
		Log.i("ym", "总空间 = " + size);
		return size;
	}
	/**
	 * 获取sdCard可用内存大小*/
	
public String getAvailableSize(Context context){
		
		// 获取SD卡根目录
		File path = getSdFile();
		// 获取指定目录下的内存存储状态
		StatFs stat = new StatFs(path.getPath());
		// 获取单个扇区的大小
		long blockSize = stat.getBlockSize();
		// 获取可以使用的扇区数量
		long availableBlocks = stat.getAvailableBlocks();
		// 可用空间 = 扇区的大小 + 可用的扇区
		long availableSize = blockSize * availableBlocks;
		// 格式化文件大小的格式
		String size = Formatter.formatFileSize(context, availableSize);
		Log.i("sdcard", "总空间 = " + size);		
		return size;
		
	}
	/**
	 * 获取sd卡根目录字符串的路径*/
	
	public String getSdPath () {
		return getSdFile().getAbsolutePath();
	}
	
	/**
	 * 获取sd卡*/
	public String getInnerSDCardPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}
	public File getSdFile(){
		return Environment.getExternalStorageDirectory();
	}
	
	/**
	 *读取指定文件中的数据，将数据读取为byte[]类型 */
	public byte[] getDataFromFile(String filePath) {
		
		BufferedInputStream bis = null;
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		try {
			fis = new FileInputStream(filePath);
			bis = new BufferedInputStream(fis);
			int len = -1;
			byte[] buffer = new byte[1024];
			while ((len = bis.read(buffer))!=-1) {
				baos.write(buffer, 0, len);
			}
			return baos.toByteArray();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (bis!=null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (baos!=null) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 向指定路径中写入指定数据
	 * */
	public void saveFile(String path,byte[] data,String fileName) {

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		BufferedOutputStream bos = null;
		Log.e(TAG, "**********************:  "+(path+File.separator+fileName) );
		try {
			bos = new BufferedOutputStream(new FileOutputStream(path+File.separator+fileName));
			bos.write(data);
			bos.flush();
			Log.i("ym", "**********************: 数据存完");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "**********************:  "+e.getMessage() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "**********************:  "+e.getMessage() );
		}finally{
			if (bos!=null) {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 复制文件*/
	public void copyFile(String fromPath,String saveAsFileName,String toPath) {
		byte[] data = getDataFromFile(fromPath);
		saveFile(toPath, data, saveAsFileName);
	}
	
	/**
	 * 删除文件*/
	public void deleteFile(String filePath) {
		File file = new File(filePath);
		file.delete();
	}
	/**
	 * 剪切文件*/
	public void cutFile(String fromPath,String saveAsFileName,String toPath) {
		copyFile(fromPath, saveAsFileName, toPath);
		deleteFile(fromPath);
	}
	//获取指定文件大小
	public long getFolderSize(File file){
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++)
			{
				if (fileList[i].isDirectory())
				{
					size = size + getFolderSize(fileList[i]);

				}else{
					size = size + fileList[i].length();

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return size/1048576;
		return size;
	}
	
}
