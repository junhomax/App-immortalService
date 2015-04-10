package example.dude.kr.immortalservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceMonitor extends BroadcastReceiver {
    public static final String ACTION_RESTART = "ACTION.RESTART.NormalService";
    private static final String LOG_TAG = "ServiceMonitor";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d( LOG_TAG, " OnReceive() ");

        if( intent.getAction().equals( ServiceMonitor.ACTION_RESTART ) ) {
            Log.d( LOG_TAG, " Service Monitor will start the NormalService");
            // 서비스 시작.
            Intent i = new Intent( context, NormalService.class);
            context.startService( i );
        }
    }
}