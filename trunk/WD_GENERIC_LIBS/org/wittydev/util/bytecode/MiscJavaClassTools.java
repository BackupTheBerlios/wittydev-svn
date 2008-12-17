package org.wittydev.util.bytecode;

import java.awt.print.Printable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Enumeration;
import java.util.zip.Checksum;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * This class contains some generic utilities to work with java classes. 
 * 
 * @author Charles Khayat
 *
 */
public class MiscJavaClassTools {
	
	public static final short CLASS_MINOR_VERSION_JDK_6_0 = 50;
	public static final short CLASS_MINOR_VERSION_JDK_5_0 = 49;
	public static final short CLASS_MINOR_VERSION_JDK_1_4 = 48;
	public static final short CLASS_MINOR_VERSION_JDK_1_3 = 47;
	public static final short CLASS_MINOR_VERSION_JDK_1_2 = 46;
	public static final Object[][] CLASS_MINOR_VERSIONS ={
														{"JDK_6.0", CLASS_MINOR_VERSION_JDK_6_0},
														{"JDK_5.0", CLASS_MINOR_VERSION_JDK_5_0},
														{"JDK_1.4", CLASS_MINOR_VERSION_JDK_1_4},
														{"JDK_1.3", CLASS_MINOR_VERSION_JDK_1_3},
														{"JDK_1.2", CLASS_MINOR_VERSION_JDK_1_2}
													}; 
	
	public static void main(String[] args) {
		if (args==null || args.length<3 || args[0]==null || args[1] == null || args[2] == null) {
			printUsage();
			return;
		}
		
		File file=null;
		String lastSwitch=null;
		boolean modifyClazzVersion=false;
		short targetClazzVersion = -1;
		boolean verbose=true;
		boolean tryOnly=false;
		boolean ignoreLowerVersions =true;
		
		for ( int i=0; i<args.length; i++ ){
			
			if (i==args.length-1){
				file = new File (args[i]);
			}else if (i%2==0){
				lastSwitch=args[i];
			}else{
				if ( lastSwitch.equals("-v") ){
					modifyClazzVersion = true;
					for ( int j=0; j<CLASS_MINOR_VERSIONS.length; j++){
						if (args[1].equals(CLASS_MINOR_VERSIONS[j][0])){
							targetClazzVersion = (Short)CLASS_MINOR_VERSIONS[j][1];
							break;
						}
					}
					if (targetClazzVersion<0) {
						printUsage();
						return;
					}
				}else if ( lastSwitch.equals("-verbose") ){
					if (args[i].equals(1)||args[i].equals("true") ) 
						verbose=true; 
					else 
						verbose=false;
				}else if ( lastSwitch.equals("-tryOnly") ){
					if (args[i].equals(1)||args[i].equals("true") ) 
						tryOnly=true; 
					else 
						tryOnly=false;
				}else if ( lastSwitch.equals("-ignoreLowerVersions") ){
					if (args[i].equals(1)||args[i].equals("true") ) 
						ignoreLowerVersions=true; 
					else 
						ignoreLowerVersions=false;
				}				
			}
		}
		if (modifyClazzVersion && file!=null){
			modifyClazzMinorVersion(file, targetClazzVersion, true, tryOnly, verbose);
		}else{
			System.out.println("Invalid argument ["+args[0]+"]");
			printUsage();
		}
			
		
	}
	
	static void printUsage(){
		System.out.println("Usage: java org.wittydev.util.bytecode.MiscClassModifier [-v target] [-verbose true/false] [-tryOnly true/false] [-ignoreLowerVersions true/false] <class or jar filePath>");
		System.out.println("-v: 		Use this switch to modify the class version of a single class file or of all the ");
		System.out.println("		classes in a given jar file.");
		System.out.println("		'target' should have one of the following values: JDK_6.0, JDK_5.0, JDK_1.4, JDK_1.3, JDK_1.2");
		System.out.println("		If this switch is given the last argument should point to a class, jar or zip file name.");
		System.out.println("-verbose: 	If set to \"true\", force a more verbose log  ");
		System.out.println("-tryOnly: 	If set to \"true\", the requested job won't really happen. Only logs will appear.");
		System.out.println("-ignoreLowerVersions: 	Should be used  with the \"-v\" switch." );
		System.out.println("		If set to \"true\", only files with class' version  higher then the target ");
		System.out.println("		will be modified .");
		
		System.out.println("\nExample:");
		System.out.println( "java org.wittydev.util.bytecode.MiscClassModifier -v JDK_6.0 -tryOnly true c:\\tmp\\myJar.jar");
		
	}
	
	/** 
	 * 
	 * Ref http://en.wikipedia.org/wiki/Class_(file_format)
	 * 
	 * @param file	The file must be a ".class", ".jar" or ".zip" file
	 * 				If a ".class" is provided, the file will be modified with the target version.
	 * 				If a ".jar" or ".zip" archive is provided, all the classes it contains will be modified  with the target version,
	 * 				and a new archive will be generated on the same path: "fileName_version_<target version>.<jar/zip>" 
	 * 			 
	 * @param targetMinorVersion	The target minor version to set. Allowed value are: 
	 *  							CLASS_MINOR_VERSION_JDK_6_0 = 50;
	 *  							CLASS_MINOR_VERSION_JDK_5_0 = 49;
	 *  							CLASS_MINOR_VERSION_JDK_1_4 = 48;
	 *  							CLASS_MINOR_VERSION_JDK_1_3 = 47;
	 *  							CLASS_MINOR_VERSION_JDK_1_2 = 46;
	 *  
	 * @param ignoreLowerVersions	If set to true, only classes with versions higher then the "targetMinorVersion" will be changed
	 * @param tryOnly				If set to true, no class will be modified. Only the log will be outputed
	 * @param verbose				If set to true, a verbose log will be outputed
	 * @return						TRUE if everything was, FALSE otherwise
	 *    
	 */
	public static boolean modifyClazzMinorVersion(
									File file, short targetMinorVersion, 
									boolean ignoreLowerVersions,
									boolean tryOnly,
									boolean verbose 
									) {
		if (file==null|| (file.getName().trim().length()<=4)) {
				System.out.println("Invalid file argument d ["+file.getName()+"]");
				return false;
		}
		
		String fileName=file.getName().toLowerCase().trim();
		int pos=fileName.indexOf(".");
		
		String extension = null;
		extension=fileName.substring(pos);
		if (".class".equals(extension)){
			
			RandomAccessFile raf=null;
			try {
				raf = new RandomAccessFile(file, "rw");
				raf.seek(6);
				/*System.out.println(raf.readByte());
				System.out.println(raf.readByte());
				raf.seek(6);*/
				short currentMinorVersion=raf.readShort();
				if (currentMinorVersion==targetMinorVersion || (ignoreLowerVersions && currentMinorVersion<=targetMinorVersion)){
					System.out.println("No need to modify class: ["+file.getName()+"] same version as target. ["+currentMinorVersion+"] "); 
				}else{
					raf.seek(6);
					if ( !tryOnly)
						raf.writeShort(targetMinorVersion);
					System.out.println("Modified class' version: ["+file.getName()+"]. From ["+currentMinorVersion+"] to ["+targetMinorVersion+"]");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}finally{
				try {
					if ( raf!=null)raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
 		}else if (".jar".equals(extension) || ".zip".equals(extension)){
 			ZipFile zip=null;
 			ZipOutputStream jos=null;
 			System.out.println("Handling archive ["+file+"]"); 
 			try {
				zip=new ZipFile(file);
				String targetArchive=file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-4)+"_version_"+targetMinorVersion+extension;
				System.out.println("Target archive initialization: ["+targetArchive+"]"); 
				FileOutputStream fos=new FileOutputStream(targetArchive);
				
				if (!tryOnly)
					jos=new ZipOutputStream(new BufferedOutputStream(fos));
				
				for (Enumeration e = zip.entries(); e.hasMoreElements();){
					ZipEntry je=(ZipEntry)e.nextElement();
					ZipEntry je_out=new ZipEntry(je.getName());
					
					
					
					
					BufferedInputStream bis=new BufferedInputStream(zip.getInputStream(je));
					
					if (!tryOnly)jos.putNextEntry(je_out);
					byte[] bs=new byte[8192];
					int len;
					if (!je.isDirectory() && je.getName().toLowerCase().endsWith(".class")){
						bis.read(bs, 0, 6);
						if (!tryOnly)jos.write(bs, 0, 6);
						//(short)((b1 << 8) | b2)
						bis.read(bs, 0, 2);
						int currentMinorVersion= (bs[0]<<8)|bs[1];
						if (currentMinorVersion==targetMinorVersion || (ignoreLowerVersions && currentMinorVersion<=targetMinorVersion)){
							System.out.println("No need to modify class: ["+je.getName()+"] same version as target. ["+currentMinorVersion+"] ");
						}else
							System.out.println("Modified class' version: ["+je.getName()+"]. From ["+currentMinorVersion+"] to ["+targetMinorVersion+"]");
						
						bs[0]=(byte)(targetMinorVersion/256);
						bs[1]=(byte)(targetMinorVersion%256);
						//System.out.println(b0);
						//System.out.println(b1);
						if (!tryOnly)jos.write(bs, 0, 2);
						
					}

					if (!tryOnly){
						while((len=bis.read(bs)) > 0){
							jos.write(bs, 0, len);
						}
					}
					if (!tryOnly)jos.closeEntry();
					
					bis.close();
				}
				System.out.println("Target archive closed: ["+targetArchive+"]");
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if (zip!=null)zip.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					if (jos!=null)jos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			
		}else{
			System.out.println("Invalid file argument ["+file+"][extension: "+extension+"]");
			return false;
		}
		if (tryOnly){
			System.out.println("====================================================================");
			System.out.println("Warning: This was only a simulation, no file was created or modified");
		}
		 return true;
		
	}
	/*public static void main(String[] args) {
		//File file =new File("c:\\tmp\\OrderedMap.class");
		//File file =new File("c:\\tmp\\jboss-xml-binding.jar");
		File file =new File("c:\\tmp\\jbossall-client.jar");
		modifyClazzMinorVersion(file, (short)48, true, true);
	}*/
	
	
}
