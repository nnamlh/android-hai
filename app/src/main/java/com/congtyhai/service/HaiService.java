package com.congtyhai.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import com.congtyhai.activity.checkin.CheckInHistoryActivity;
import com.congtyhai.activity.R;
import com.congtyhai.model.send.CheckIn;
import com.congtyhai.model.data.DCheckIn;
import com.congtyhai.model.receive.ResultInfo;
import com.congtyhai.util.ApiClient;
import com.congtyhai.util.ApiClientUpload;
import com.congtyhai.util.ApiInterface;
import com.congtyhai.util.HaiSetting;
import com.congtyhai.util.RealmController;
import java.io.File;
import java.util.List;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HaiService extends Service {

    private static final String TAG = HaiService.class.getName();

    private boolean isRunning = false;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        registerReceiver(this.mConnReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Stop service once it finishes its task
                // stopSelf();
            }
        }).start();
        */
        return Service.START_STICKY;
    }

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            boolean isConnected = wifi != null
                    && wifi.isConnectedOrConnecting() || mobile != null
                    && mobile.isConnectedOrConnecting();
            if (!isConnected) {
                isRunning = false;
                Log.i(TAG, "not connect internet");
               // showNotificationMessage("Mất kết nối internet.", "mat ket noi interet", new Intent(getApplicationContext(), MainActivity.class), "");
            } else {

                if (!isRunning) {
                    isRunning = true;

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String token = sharedPref.getString(HaiSetting.KEY_TOKEN, "");

                    List<DCheckIn> arrays = RealmController.getInstance().getCheckIns();

                    for (DCheckIn item : arrays ) {
                        if(item.getStt() == 0) {
                            uploadImage(item.getId(), token);
                        } else if (item.getStt() == 1) {
                            sendInfo(item.getId(), token);
                        }
                    }
                }
            }
        }
    };

    private void uploadImage(final long id, final String tokenString ) {

        File sourceFile = null;
        RequestBody requestFile = null;
        MultipartBody.Part body = null;

        DCheckIn item = RealmController.getInstance().getCheckIn(id);

        if (item.getFilePath() != null) {

            Uri imageUri = Uri.parse(item.getFilePath());

            sourceFile = new File(imageUri.getPath());

            requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), sourceFile);
            body =
                    MultipartBody.Part.createFormData("image", sourceFile.getName(), requestFile);
        }


        RequestBody user =
                RequestBody.create(
                        MediaType.parse("text/plain"), item.getUser());


        RequestBody token =
                RequestBody.create(
                        MediaType.parse("text/plain"), tokenString);

        RequestBody extension =
                RequestBody.create(
                        MediaType.parse("text/plain"), ".jpg");

        ApiInterface apiService =
                ApiClientUpload.getClient().create(ApiInterface.class);

        Call<ResultInfo> call = apiService.uploadImage(body, user, token, extension);

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call,final Response<ResultInfo> response) {
                if (response.body().getId().equals("1")) {
                    Realm realm = RealmController.getInstance().getRealm();


                    final DCheckIn item = RealmController.getInstance().getCheckIn(id);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            item.setStt(1);
                            item.setImage(response.body().getMsg());
                        }
                    });
                    sendInfo(item.getId(), tokenString);
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {

            }
        });

    }


    private void sendInfo(final long id, String tokenString) {

        DCheckIn item = RealmController.getInstance().getCheckIn(id);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        CheckIn info = new CheckIn(item.getUser(), tokenString, item.getComment(), item.getImage(), item.getLatitude(), item.getLongitude(),item.getDate(), item.getAgency());

        Call<ResultInfo> call = apiService.checkIn(info);

        call.enqueue(new Callback<ResultInfo>() {
            @Override
            public void onResponse(Call<ResultInfo> call,
                                   Response<ResultInfo> response) {
                if ("1".equals(response.body().getId())) {
                    Realm realm = RealmController.getInstance().getRealm();
                    final DCheckIn item = RealmController.getInstance().getCheckIn(id);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            item.setStt(2);
                        }
                    });
                    showNotificationMessage("Thông báo cập nhật", "Đã cập nhật xong checkin.", new Intent(getApplicationContext(), CheckInHistoryActivity.class), "");
                }
            }

            @Override
            public void onFailure(Call<ResultInfo> call, Throwable t) {

            }
        });
    }


    public void showNotificationMessage(final String title, final String message, Intent intent, String imageUrl) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;

        // notification icon
        final int icon = R.mipmap.logo;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext());

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getApplicationContext().getPackageName() + "/raw/notification");

        if (!TextUtils.isEmpty(imageUrl)) {
            showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound);
        } else {
            showSmallNotification(mBuilder, icon, title, message, resultPendingIntent, alarmSound);
            playNotificationSound();
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(HaiSetting.NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        unregisterReceiver(this.mConnReceiver);
        Log.i(TAG, "Service onDestroy");
    }
}
