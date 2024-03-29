package com.jason.fileoperate.log;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is about The type Tup log util.
 * Record tup log,Written in the SD card file.
 * LogUtil日志类
 */
public final class LogTackle {
    private static String logPath = Environment.getExternalStorageDirectory() + "/" +"log";
    public static final String LOG_FILE_NAME = "OpenIm.log";
    private static final String TAG = "OPENIM";

    private static int logLevel = 3;

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor(new LogThreadFactory("log"));
    private static final int LOG_FILE_SIZE = 5242880;
    private static boolean isOpenLog = true;
    private static double logFileSize = 1024.00 * 1024.00 * 5;

    private LogTackle() {
    }

    public static String getLogPath() {
        return logPath;
    }

    // LogTackle.setLogPath(context.getExternalFilesDir("").getAbsolutePath());
    public static void setLogPath(String path) {
        logPath = path;
    }

    public static void setLogSwitch(boolean isOpen) {
        isOpenLog = isOpen;
    }

    public static void setLogLevel(int level) {
        logLevel = level;
    }



    public static void d(String tag, String msg) {
        if (isOpenLog && logLevel >= 3) {
            String var2 = replaceSpecialchar(tag);
            String var3 = replaceSpecialchar(msg);
            Log.d("LogTackle", var3 + ":" + var2);
            writeLog("[DEBUG][" + var2 + "]");
        }

    }

    public static void i(String tag, String msg) {
        if (isOpenLog && logLevel >= 2) {
            String var2 = replaceSpecialchar(msg);
            String var3 = replaceSpecialchar(tag);
            Log.i("LogTackle", var3 + ":" + var2);
            writeLog("[INFO][" + var2 + "]");
        }

    }

    public static void w(String tag, String msg) {
        if (isOpenLog && logLevel >= 1) {
            String var2 = replaceSpecialchar(msg);
            String var3 = replaceSpecialchar(tag);
            Log.w("LogTackle", var3 + ":" + var2);
            writeLog("[WARNING][" + var2 + "]");
        }

    }

    public static void e(String tag, String msg) {
        if (isOpenLog && logLevel >= 0) {
            String var2 = replaceSpecialchar(msg);
            String var3 = replaceSpecialchar(tag);
            Log.e("LogTackle", var3 + ":" + var2);
            writeLog("[ERROR][" + var2 + "]");
        }

    }

    private static String replaceSpecialchar(String ss) {
        String pS;
        if (!TextUtils.isEmpty(ss)) {
            Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
            Matcher matcher = pattern.matcher(ss);
            pS = matcher.replaceAll("");
        } else {
            pS = ss;
        }
        return pS;
    }

    public static final String CHARSET_UTF_8 = "UTF-8";
    private static void writeLog(final String logText) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        EXECUTOR_SERVICE.execute(new Runnable() {
            public void run() {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }

                String nowTimeStr = String.format("[%s]", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)).format(new Date()));
                String toLogStr = nowTimeStr + " " + logText;
                toLogStr += "\r\n";

                FileOutputStream fileOutputStream = null;
                String logFile = logPath;
                String filename = LOG_FILE_NAME;
                try {
                    File fileOld = new File(logFile + "/" + filename);
                    if ((float) ((fileOld.length() + logText.length()) / 1024.00) > logFileSize) {
                        File bakFile = new File(fileOld.getPath() + ".bak");
                        if (bakFile.exists()) {
                            if (bakFile.delete()) {
                                Log.d("Write Log", "delete " + bakFile.getName());
                            }
                        }
                        if (fileOld.renameTo(bakFile)) {
                            Log.d("Write Log", fileOld.getName() + " rename to " + bakFile.getName());
                        }
                    }

                    File file = new File(logFile);
                    if (!file.exists()) {
                        if (file.mkdir()) {
                            Log.d("Write Log", "create " + file.getName());
                        }
                    }

                    File filepath = new File(logFile + "/" + filename);
                    if (!filepath.exists()) {
                        if (filepath.createNewFile()) {
                            Log.d("Write Log", "create " + filepath.getName());
                        }
                    }

                    fileOutputStream = new FileOutputStream(filepath, true);

                    byte[] buffer = toLogStr.getBytes(CHARSET_UTF_8);

                    fileOutputStream.write(buffer);
                } catch (FileNotFoundException e) {
                    Log.e(TAG, e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            //LogUtil.e(TAG, e.getMessage());
                            Log.e(TAG, (e.getMessage()));
                        }
                    }
                }
            }
        });
    }

    private static final class LogThreadFactory extends AtomicLong implements ThreadFactory {
        private static final long serialVersionUID = 1927816293512124184L;
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;

        LogThreadFactory(String var1) {
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = var1 + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-open-sdk";
        }

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.group, runnable, this.namePrefix + this.getAndIncrement(), 0L) {
                public void run() {
                    super.run();
                }
            };
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }

            thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                public void uncaughtException(Thread thread1, Throwable throwable) {
                    LogTackle.e("LogThreadFactory", "newThread threw uncaught throwable thread:" + thread1.getName());
                    LogTackle.e("LogThreadFactory", "newThread threw uncaught throwable:" + throwable.getMessage());
                }
            });
            return thread;
        }
    }
}
