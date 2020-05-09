package com.stevecrossin.grocerytracker.utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.stevecrossin.grocerytracker.BuildConfig;
import com.stevecrossin.grocerytracker.R;
import com.stevecrossin.grocerytracker.database.AppDataRepo;
import com.stevecrossin.grocerytracker.entities.Receipt;
import com.stevecrossin.grocerytracker.entities.User;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Reminders {
    private static final String TAG = Reminders.class.getName();
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_SHOPPING_FREQUENCY = "shoppingFrequency";
    private static final String CHANNEL_ID = "grocery_tracker_reminders";
    private static final int NOTIFICATION_ID = 40;
    private static final int SUBSEQUENT_REMINDER_DELAY_DAYS = 7;

    private static Reminders mInstance;

    private Reminders() {
    }

    public static Reminders getInstance() {
        if (mInstance == null) {
            mInstance = new Reminders();
        }
        return mInstance;
    }

    /**
     * Method that creates a new scheduled task if necessary, in the background.
     * First checks isReminderScheduled, if one already is, log this and do nothing else.
     * Then checks if a reminder is not scheduled, and if the user has not uploaded any receipts by checking current uploaded receipts for the user if it is equal to 0
     * If true, it will schedule the reminder to trigger at [current time + their shopping frequency]
     * <p>
     * It will then execute the scheduleRemindersTask.
     */
    @SuppressLint("StaticFieldLeak")
    public void scheduleIfNecessary(final Context context) {
        AsyncTask<Void, Void, Void> scheduleRemindersTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                AppDataRepo repo = new AppDataRepo(context);
                User user = repo.getSignedUser();
                int userId = user.getUserID();

                if (isReminderScheduled(context, userId)) {
                    Log.i(TAG, "Reminder is already scheduled for the user.");
                    return null;
                }

                List<Receipt> receipts = repo.getReceiptsForUser(user.getEmail());
                if (receipts == null || receipts.size() == 0) {
                    schedule(context);
                    return null;
                }

                return null;
            }
        };
        scheduleRemindersTask.execute();
    }

    /**
     * Performs check to see if reminder scheduled for this given user.
     **/
    private boolean isReminderScheduled(Context context, int userId) {
        ListenableFuture<List<WorkInfo>> workInfosFuture =
                WorkManager.getInstance(context).getWorkInfosByTag(String.valueOf(userId));
        try {
            List<WorkInfo> workInfos = workInfosFuture.get();
            if (workInfos == null || workInfos.size() == 0) {
                return false;
            }
            WorkInfo workInfo = workInfos.get(0);
            WorkInfo.State state = workInfo.getState();
            return state != WorkInfo.State.SUCCEEDED && state != WorkInfo.State.FAILED &&
                    state != WorkInfo.State.CANCELLED;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Schedule a reminder for the current user after the shopping frequency of the current user.
     **/
    @SuppressLint("StaticFieldLeak")
    public void schedule(final Context context) {
        AsyncTask<Void, Void, Void> scheduleRemindersTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                reschedule(context, -1, -1);
                return null;
            }
        };
        scheduleRemindersTask.execute();
    }

    /**
     * Schedule a reminder for the given user after the given number of days.
     **/

    @SuppressLint("StaticFieldLeak")
    public void schedule(final Context context, final int days, final int userId) {
        AsyncTask<Void, Void, Void> scheduleRemindersTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                reschedule(context, days, userId);
                return null;
            }
        };
        scheduleRemindersTask.execute();
    }


    /**
     * Cancel any pending reminders and schedule a new one after the given number of days.
     */
    private void reschedule(final Context context, int initialDelayDays, int userId) {
        AppDataRepo repo = new AppDataRepo(context);
        User user;
        if (userId < 0) {
            user = repo.getSignedUser();
        } else {
            user = repo.getUserById(userId);
        }
        if (user == null) {
            Log.e(TAG, "Could not schedule reminder. Invalid user or no user signed in. Supplied userId: " + userId);
            return;
        }
        userId = user.getUserID();
        String shoppingFrequency = convertShopNumberToShopFrequency(context, user.getShopNumber());

        /**
         * If the initial given delay days is not valid, fallback to the user's shopping frequency.
         */
        if (initialDelayDays < 0) {
            initialDelayDays = convertShopNumberToDays(user.getShopNumber());
            if (initialDelayDays < 0) {
                Log.e(TAG, "Incorrect shop number: " + user.getShopNumber());
                return;
            }
        }

        /**
         * Cancels all pending reminders.
         */

        WorkManager.getInstance(context).cancelAllWorkByTag(String.valueOf(userId));

        Data data = new Data.Builder()
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_SHOPPING_FREQUENCY, shoppingFrequency)
                .build();

        OneTimeWorkRequest request = buildReminderWorkRequest(context, data, userId, initialDelayDays, TimeUnit.DAYS);
        WorkManager.getInstance(context).enqueue(request);
    }

    /**
     * Schedules a onetime work request.
     * For debug builds, override delay days with debug delay minutes if available.
     */
    private OneTimeWorkRequest buildReminderWorkRequest(Context context, Data data, int userId, int initialDelay, TimeUnit timeUnit) {
        if (BuildConfig.DEBUG) {
            SharedPreferences preferences = context.getSharedPreferences("reminders_debug", Context.MODE_PRIVATE);
            String initialDelayDebugString = preferences.getString("reminder_minutes", null);
            if (initialDelayDebugString != null && !TextUtils.isEmpty(initialDelayDebugString)) {
                initialDelay = Integer.parseInt(initialDelayDebugString);
                timeUnit = TimeUnit.MINUTES;
            }
        }
        return new OneTimeWorkRequest.Builder(RemindersWorker.class)
                .setInputData(data)
                .addTag(String.valueOf(userId))
                .setInitialDelay(initialDelay, timeUnit)
                .build();
    }

    /**
     * Converts the shopping frequency of the user into a text value for display purpose.
     */
    private String convertShopNumberToShopFrequency(Context context, String shopNumber) {
        switch (shopNumber) {
            case "Weekly or more":
                return context.getString(R.string.week);
            case "Fortnightly":
                return context.getString(R.string.fortnight);
            case "Monthly or less":
                return context.getString(R.string.month);
            default:
                return "";
        }
    }

    /**
     * Converts the shopping frequency string of the user into a numerical value for scheduling purposes.
     */
    private int convertShopNumberToDays(String shopNumber) {
        switch (shopNumber) {
            case "Weekly or more":
                return 7;
            case "Fortnightly":
                return 14;
            case "Monthly or less":
                return 30;
            default:
                return -1;
        }
    }

    /**
     * Remind the user to upload a new receipt using high priority notification - this will ensure it appears on the screen
     * rather than going silently into the notification tray.
     * notificationID is a unique int for this apps notifications that we have defined as 40.
     */

    //
    private void remind(Context context, String shoppingFrequency) {
        createNotificationChannelIfNecessary(context);
        String text = context.getString(R.string.reminder_text) + shoppingFrequency;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.globe)
                .setContentTitle(context.getString(R.string.reminder))
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * Create the NotificationChannel, but only on API 26+ because
     * the NotificationChannel class is new and not in the support library covered in older SDKs
     *
     * @param context - specifically getSystemService - registers the notification channel with the system. After this,
     *                the importance of other notification behaviours cannot be changed
     */
    private void createNotificationChannelIfNecessary(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * This is the ReminderWorker that is triggered when the scheduled time comes.
     * Does a few things here:
     * If the reminder schedule that is fired is for a different user, reschedule it after
     * SUBSEQUENT_REMINDER_DELAY_DAYS days for the user for which it fired for. As a default this value is 7 days after the reminder.
     */
    public static class RemindersWorker extends Worker {

        public RemindersWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
            int userId = getInputData().getInt(KEY_USER_ID, -1);
            if (userId == -1) {
                Log.wtf(TAG, "Work scheduled for an unknown user???");
                return Result.failure();
            }

            AppDataRepo repo = new AppDataRepo(getApplicationContext());
            User user = repo.getSignedUser();
            if (user == null || user.getUserID() != userId) {
                Log.i(TAG, "Reminder request not for current user. Rescheduling.");
                Reminders.getInstance().schedule(getApplicationContext(), SUBSEQUENT_REMINDER_DELAY_DAYS, userId);
                return Result.success();
            }

            String shoppingFrequency = getInputData().getString(KEY_SHOPPING_FREQUENCY);
            if (TextUtils.isEmpty(shoppingFrequency)) {
                Log.wtf(TAG, "Work scheduled with unknown shopping frequency???");
                return Result.failure();
            }

            /**
             * Remind the user and schedule a subsequent reminder for SUBSEQUENT_REMINDER_DELAY_DAYS
             * from now in case the user does not upload a receipt even after the reminder.
             * It will be reset accordingly if the user uploads a receipt in the meantime.
             */

            Reminders.getInstance().remind(getApplicationContext(), shoppingFrequency);
            Reminders.getInstance().schedule(getApplicationContext(), SUBSEQUENT_REMINDER_DELAY_DAYS, userId);
            return Result.success();
        }
    }
}