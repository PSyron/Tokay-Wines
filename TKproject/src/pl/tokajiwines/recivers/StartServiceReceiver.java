
package pl.tokajiwines.recivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pl.tokajiwines.services.NotificationService;
import pl.tokajiwines.utils.Log;

public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("StartServiceReceiver", "execute");
        Intent service = new Intent(context, NotificationService.class);
        context.startService(service);

    }
}
