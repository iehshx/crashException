package com.iehshx.crashexception;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        restartApp3(100);
    }


    /**
     * 通过AlarmManager 重启app
     */
    private void restartApp(){
        new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, MainActivity.class);
                PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
                    AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }.start();
    }

    /**
     * 通过flag 重启app
     */
    private void restartApp2(){
        Log.d(TAG,"通过flag 重启app");
        final Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    /**
     * 通过service 重启app
     * @param Delayed 延迟多少毫秒
     */
    public void restartApp3(long Delayed){
        Log.d(TAG,"通过service 重启app");
        Intent intent1=new Intent(mContext,killSelfService.class);
        intent1.putExtra("PackageName",mContext.getPackageName());
        intent1.putExtra("Delayed",Delayed);
        mContext.startService(intent1);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}