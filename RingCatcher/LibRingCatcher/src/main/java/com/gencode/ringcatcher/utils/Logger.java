package com.gencode.ringcatcher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;


public final class Logger {
	private static boolean DEBUG = true;
	private static boolean mbSaveLog = false;
	
	
	private static String mSDCardPath = null;
	private static boolean mbIsExistLogFolder = false;
	private static String mStrLogPath = null;
	private static String TAG = "[Logger]";
	
	
	public static String formattedDate(Date date, String toFormatString) {
		if (date == null || toFormatString == null ) {
			return "";
		}
		
		SimpleDateFormat toFormat = new SimpleDateFormat(toFormatString);
		return toFormat.format(date);
	}
	
	public static String getCurrentDateWithSec(){
		String ret = null;
	    String fromFormatStringWithSecond = "yyyyMMddHHmmss";
		Calendar currentDate = Calendar.getInstance();
		ret = formattedDate(currentDate.getTime(), fromFormatStringWithSecond);
		
		return ret;
	}
	
	public static boolean makeLogFolder()
	{
		if ( mbIsExistLogFolder == true ) return true;
		
		boolean bRet=true;
		mSDCardPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
		mStrLogPath = mSDCardPath + "/AdPhoneLog/log.txt";
		
		File dir = new File( mSDCardPath + "/AdPhoneLog" );											//Log.e(TAG, "[makeLogFolder] path = " + mSDCardPath + "/AdPhoneLog" );
		
		if ( !dir.exists() ) 
        {   try 
            {   dir.mkdirs();
            	mbIsExistLogFolder = true;
            } 
            catch (Exception e) 
            {	/*																	                Log.e( TAG ,  " makeOutputFile unable to create directory " + dir + ": " + e);
                Toast t = Toast.makeText(
                		getApplicationContext(), 
                		getApplicationContext().getString(R.string.rec_err1_mkdir) + " " + dir + " " +
                		getApplicationContext().getString(R.string.rec_err2_mkdir)  + e, Toast.LENGTH_LONG);
                t.show(); */
                return false;
            }
        } 
        else 
        {   if ( !dir.canWrite() ) 
            {   /*             																		 Log.e( TAG ,  " makeOutputFile does not have write permission for directory: " + dir);
                Toast t = Toast.makeText(
                		getApplicationContext(), 
                		getApplicationContext().getString(R.string.rec_err1_mkdir_permission) + " " + dir + " " +
                        getApplicationContext().getString(R.string.rec_err2_mkdir_permission) , Toast.LENGTH_LONG);
                t.show(); */
                return false;
            }
        }
		return bRet;
	}
	
	
	/**		http://arabiannight.tistory.com/47
     * 파일 복사
     * @param file
     * @param save_file
     * @return
     * 			-1 : 복사할 파일이 존재
     * 			 1 : 복사 성공
     * 			 0 : 복사 실패....기존 파일이 존재하지 않는 경우..
     */
    private static int copyFile(java.io.File file , String save_file){
        int result;
        File f=null;
        
        f = new File ( save_file );
        
        if ( f.exists() == true )
        	return -1;
        
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream newfos = new FileOutputStream(save_file);
                int readcount=0;
                byte[] buffer = new byte[1024];
                while((readcount = fis.read(buffer,0,1024))!= -1){
                    newfos.write(buffer,0,readcount);
                }
                newfos.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = 1;
        }else{
            result = 0;
        }
        return result;
    }

	public static void writeLog( String tag, String msg )
	{
		if ( makeLogFolder() == false ) return;
		
		File f=null;
		FileOutputStream fileOutput = null;
		boolean bRet;
		
		try
	    {	f = new File( mStrLogPath );
	         
	    	if ( f.exists() == false ) 
	    	{
	    		bRet = f.createNewFile();
	    		
	    		if ( bRet == false )
	    		{
	    			Log.e( TAG , "[onCreate]Fail creating log : " +  mStrLogPath );
	    			return;
	    		}
	    	}
	    	else // file 이 존재하는 경우 
	    	{
	    		long nMaxSize = 1024 * 100; // 100kbytes
	    		
	    		if ( f.length() >=  nMaxSize )
	    		{
	    			int i,dix=10;
	    			int nRet=-99;
	    			int nCopySuccess=0;
	    			for ( i=1;  i <= dix; ++i )
	    			{
	    				String strTmpPath = mStrLogPath + "." + String.valueOf(i);
	    				nRet = copyFile( f , strTmpPath );
	    				
	    				if ( nRet == 1 ) // 복사성공
	    				{																			Log.i("Logger", "[writeLog] copy file = " + strTmpPath );
	    					nCopySuccess=1;
	    					break;
	    				}
	    				
	    				
	    			}
	    			
	    			if ( nCopySuccess == 0 ) // 복사할 파일이 너무 많은 경우....오래된 것부터 삭제함...
	    			{
	    				String strTmpPath = mStrLogPath + ".1";
	    				File fd = new File( strTmpPath );
	    				
	    				if ( fd.exists() == true )
	    				{
	    					fd.delete();															Log.i("Logger", "[writeLog] delete file2 = " + strTmpPath );
	    				}
	    				int j,djx=dix;
	    				
	    				for ( j=1 ; j < djx ; ++j )
	    				{
	    					String strRenameTmpPathSrc = mStrLogPath + "." + String.valueOf(j+1);
	    					String strRenameTmpPathDst = mStrLogPath + "." + String.valueOf(j);
	    					File fs = new File( strRenameTmpPathSrc );
	    					File fs_n = new File( strRenameTmpPathDst );
	    					
	    					if ( fs.renameTo( fs_n ) == true )
	    					{
	    						Log.i("Logger", "[writeLog] reame file = " + strRenameTmpPathSrc + " to file2 = "  + strRenameTmpPathDst );
	    					}
	    				}
	    				
	    				strTmpPath = mStrLogPath + "." + String.valueOf(dix);
	    				
	    				nRet = copyFile( f , strTmpPath );
	    				
	    				if ( nRet == 1 ) // 복사성공
	    				{
	    					Log.i("Logger", "[writeLog] after deleting file = " + strTmpPath  + " success coping file !!!");
	    				}
	    			}
	    			
	    			f.delete();																			Log.i("Logger", "[writeLog] delete file = " + mStrLogPath );
	    		}	
	    	}
	    	
	    	String strtmp = "[" + getCurrentDateWithSec()  + "] "  + tag + " " + msg + "\n";
			fileOutput = new FileOutputStream( f , true );
	        fileOutput.write( strtmp.getBytes() );
	        fileOutput.close();
	    }
	    catch(Exception e)
	    {
	         e.printStackTrace();
	    }
	}
	
	
	public static void v(String tag, String msg) {
		if (DEBUG == false) {
			return ;
		}
		
		if ( mbSaveLog == true )
			writeLog( tag , msg );
		
		Log.v(tag, msg);
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (DEBUG == false) {
			return;
		}
		Log.v(tag, msg, tr);		
	}

	public static void e(String tag, String msg) {
		if (DEBUG == false) {
			return;
		}
		
		if ( mbSaveLog == true )
			writeLog( tag , msg );
		
		Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (DEBUG == false) {
			return;
		}
		Log.e(tag, msg, tr);		
	}	
	
	public static void w(String tag, String msg) {
		if (DEBUG == false) {
			return;
		}
		
		if ( mbSaveLog == true )
			writeLog( tag , msg );
		
		Log.w(tag, msg);
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (DEBUG == false) {
			return;
		}
		Log.w(tag, msg, tr);		
	}	

	public static void i(String tag, String msg) {
		if (DEBUG == false) {
			return;
		}
		
		if ( mbSaveLog == true )
			writeLog( tag , msg );
		
		Log.i(tag, msg);
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (DEBUG == false) {
			return;
		}
		Log.i(tag, msg, tr);
	}	

	public static void d(String tag, String msg) {
		if (DEBUG == false) {
			return;
		}
		
		if ( mbSaveLog == true )
			writeLog( tag , msg );
		
		Log.d(tag, msg);
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (DEBUG == false) {
			return;
		}
		Log.d(tag, msg, tr);		
	}	

}
