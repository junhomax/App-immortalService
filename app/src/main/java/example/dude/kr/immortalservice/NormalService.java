package example.dude.kr.immortalservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Date;

public class NormalService extends Service {
    private static final String LOG_TAG = "NormalService";
    static Thread myThread = null;


    public NormalService() {
    }

    @Override
    public void onCreate() {
        unregisterRestartAlarm(); // 알람 해제
        Log.d( LOG_TAG, "Service is created");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d( LOG_TAG, "Service is destroyed " + new Date().toString() );
        registerRestartAlarm(); // 알람 등록
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d( LOG_TAG, "Service is started. Normal Thread running ");


        if (myThread != null) {
            Log.d( LOG_TAG, " Kill previous thread ");
            // 쓰레드가 이미 존재한다면 죽임.
            Log.d( LOG_TAG, "Thread Status :: " + myThread.getState());
            if(myThread != null) {
                try {
                    myThread.interrupt();
                }
                catch( Exception e) {
                    Log.d( LOG_TAG, " Thread Stop " + e.getMessage());
                }
            }
            myThread = null;
        }


        if( myThread == null) {
            myThread = new NormalThread();
            myThread.start();
            Log.d( LOG_TAG, " Create New Worker Thread " + myThread);
        }


        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 서비스가 시스템에 의해서 또는 강제적으로 종료되었을 때 호출되어
     * 알람을 등록해서 10초 후에 서비스가 실행되도록 한다.
     */
    private void registerRestartAlarm() {

        Log.d( LOG_TAG , "registerRestartAlarm()");

        Intent intent = new Intent(NormalService.this, ServiceMonitor.class);
        intent.setAction( ServiceMonitor.ACTION_RESTART );
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 2000; // 2초후 알람.

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 3000, sender);
    }


    /**
     * 기존 등록되어있는 알람을 해제한다.
     */
    private void unregisterRestartAlarm() {
        Log.d( LOG_TAG , "unregisterRestartAlarm()");
        Intent intent = new Intent(NormalService.this, ServiceMonitor.class);
        intent.setAction( ServiceMonitor.ACTION_RESTART );
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }




    private class NormalThread extends Thread {
        private int s = 0;

        public NormalThread() {
            Log.d( LOG_TAG, "worker is created");
        }

        @Override
        public void run() {
            Log.d( LOG_TAG, " Service Running ");
            while(true && !this.currentThread().isInterrupted()) {
                Log.d( LOG_TAG, " LL : " + (s++) );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }

            }


            Log.d( LOG_TAG, " Terminated thread :: " + this);
        }
    }





}
