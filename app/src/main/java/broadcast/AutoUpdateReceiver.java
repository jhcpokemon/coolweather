package broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import service.AutoUpdateService;


/**
 * Created by jhcpokemon on 04/24/15.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context,Intent intent){
        Intent intent1 = new Intent(context, AutoUpdateService.class);
        context.startService(intent1);
    }
}
