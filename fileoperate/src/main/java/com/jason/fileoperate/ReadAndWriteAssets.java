package com.jason.fileoperate;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;

/**
 * Created by xiyu on 2018/3/31.
 */

public class ReadAndWriteAssets {

    Activity context;

    public ReadAndWriteAssets(Activity context) {
        this.context = context;
    }

    public String readAssets(String fileName) {

        //读assets目录下的文件
        InputStream is = null;
        String line = null;
        try {
            is = context.getResources().getAssets().open(fileName);
            Reader in = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(in);
            while (null != (line = bufferedReader.readLine())) {
                System.out.println("assets file==========" + line);
            }
            bufferedReader.close();
            in.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;

    }


    /**
     * 将assets文件夹下的文件拷贝到/data/data/包名 下
     *
     * @param context
     * @param fileName
     */
    public void copyDbFile(Context context, String fileName, String path) {
        InputStream in = null;
        FileOutputStream out = null;
        Log.e("copyDbFile1", path + "/" + fileName);
        File file = new File(path + "/" + fileName);

        //创建文件夹
        File filePath = new File(path);
        Log.e("copyDbFile2", fileName);
        if (!filePath.exists())
            filePath.mkdirs();

//        if (file.exists())
//            return;

        try {
            in = context.getAssets().open(fileName); // 从assets目录下复制

            out = new FileOutputStream(file);
            int length = -1;
            byte[] buf = new byte[1024];
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    public void writeStringToDisk(String content, String path, String name) {
//        writeTxtToFile(content, path, name);
        Log.e("Write", "WriteData：" + content);
        FileOutputStream out = null;
        BufferedWriter writer = null;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }


        try {
            out = new FileOutputStream(new File(path + "/" + name));
//            out = context.openFileOutput(path+"/"+name, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    public String readDiskTextToString(String pathName) {
//        try {
//            FileInputStream fileInputStream = context.openFileInput(pathName);
//            InputStreamReader isr = new InputStreamReader(fileInputStream);
//            char[] input = new char[fileInputStream.available()];
//            isr.read(input);
//            isr.close();
//            fileInputStream.close();
//            String str = new String(input);
//            return str;
//        } catch (Exception e) {
//            return null;
//
//        }
//        String res="";
//        try{
//            FileInputStream fin = new FileInputStream(pathName);
//
//            int length = fin.available();
//
//            byte [] buffer = new byte[length];
//            fin.read(buffer);
//
////            res = EncodingUtils.getString(buffer, "UTF-8");
//
//            fin.close();
//        }
//
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return res;


        String path = pathName;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        Log.e("Read", "ReadData：" + content);

        return content;


    }


    /**
     * 　*
     * 　* @param myContext
     * 　* @param ASSETS_NAME 要复制的文件名
     * 　* @param savePath 要保存的路径
     * 　* @param saveName 复制后的文件名
     * 　* testCopy(Context context)是一个测试例子。
     *
     */
    public static void copy(Context myContext, String ASSETS_NAME, String savePath, String saveName) {
        String filename = savePath + "/" + saveName;
        File dir = new File(savePath);
        // 如果目录不中存在，创建这个目录
        if (!dir.exists())
            dir.mkdir();
        try {
//            if (!(new File(filename)).exists()) {
            InputStream is = myContext.getResources().getAssets().open(ASSETS_NAME);
            FileOutputStream fos = new FileOutputStream(filename);
            byte[] buffer = new byte[7168];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//        public void testCopy(Context context) {
//        String path=context.getFilesDir().getAbsolutePath();
//        String name="test.txt";
//        CopyFileFromAssets.copy(context, name, path, name);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strContent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + "/" + fileName;
        // 每次写入时，都换行写
//        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                }
            }
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
