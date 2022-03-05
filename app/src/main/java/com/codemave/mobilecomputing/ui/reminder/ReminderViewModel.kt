package com.codemave.mobilecomputing.ui.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.codemave.mobilecomputing.Graph
import com.codemave.mobilecomputing.Graph.reminderRepository
import com.codemave.mobilecomputing.R
import com.codemave.mobilecomputing.data.entity.Reminder
import com.codemave.mobilecomputing.data.repository.ReminderRepository
import com.codemave.mobilecomputing.ui.home.reminderTimeToDateFormat
import com.codemave.mobilecomputing.util.NotificationWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit


class ReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository
): ViewModel() {
    private val _state = MutableStateFlow(ReminderViewState())

    val state: StateFlow<ReminderViewState>
        get() = _state

    suspend fun saveReminder(reminder: Reminder, day : Long, hour : Long, minute : Long, second : Long) {

        var id : Long = reminderRepository.addReminder(reminder)

            setOneTimeNotificationWithTime(reminder, id)
            if(reminder.notification && reminder.multiple_notification) {
                //calls function to set a notification before the reminder time
                setNotificationBeforeTime(reminder, id, day, hour, minute, second)
            }

    }

    fun reminder(id : Long): Reminder? {
        return reminderRepository.reminder(id)
    }

    init {
        createNotificationChannel(context = Graph.appContext)
        viewModelScope.launch {
            reminderRepository.reminders().collect { reminders ->
                _state.value = ReminderViewState(reminders)
            }
        }
    }
}


fun updateReminderSeen(reminderId: Long, seen : Boolean) {
    return reminderRepository.updateReminderSeen(reminderId, seen)
}


private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "NotificationChannelName"
        val descriptionText = "NotificationChannelDescriptionText"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
            description = descriptionText
        }
        // register the channel with the system
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}


//fonction to create a WorkManager Job
fun setOneTimeNotificationWithTime(reminder : Reminder, id : Long) {
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = androidx.work.Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    var delay : Long = timeBeforeReminder (reminder.reminder_time)

    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    //Monitoring for state of work
    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                if(reminder.notification){
                    createReminderNotification(reminder, id)
                }
                updateReminderSeen(id, true)
            }
        }
}


//function to create the notification
// WARNING notificationId must be different for each notification (of the same WorkManager ?)
private fun createReminderNotification(reminder: Reminder, id : Long) {
    val notificationId : Int = id.toInt() //System.currentTimeMillis().toInt()
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("Reminder !")
        .setContentText("${reminder.reminderMessage}   ${reminderTimeToDateFormat(reminder.reminder_time)}")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(Graph.appContext)) {
        //notificationId is unique for each notification that you define
        notify(notificationId, builder.build())
    }
}


//function that calculates the time remaining before the reminder
fun timeBeforeReminder (reminder_time : String) : Long {
    var remainingTime : Long = 0

    var day : Int = 0
    var month : Int = 0
    var year : Int = 0
    var hour : Int = 0
    var minute : Int = 0

    // warning :
    // the end index in subSequence is the index of the character that will not be in the selection
    day = reminder_time.subSequence(0,2).toString().toInt()
    month = reminder_time.subSequence(2,4).toString().toInt()
    year = reminder_time.subSequence(4,6).toString().toInt()
    hour = reminder_time.subSequence(6,8).toString().toInt()
    minute = reminder_time.subSequence(8,10).toString().toInt()

    year += 2000
    year -= 1970 //the calendar make +1970 on the year

    month -= 1 //calendar months start at 0
    day -= 1 //the calendar makes +1 on the day

    var cal : Calendar = Calendar.getInstance();
    cal.clear()
    cal.add(Calendar.YEAR, year); //yyyy
    cal.add(Calendar.MONTH, month); //mm
    cal.add(Calendar.DAY_OF_MONTH, day);//dd
    cal.add(Calendar.HOUR, hour); //hh
    cal.add(Calendar.MINUTE, minute); //mm

    remainingTime = cal.timeInMillis - System.currentTimeMillis()
    if(remainingTime >= 0){
        return remainingTime
    }
    return 0
}


//for multiple notification (a notification ten second before the reminder time)
fun setNotificationTenSecondsBefore(reminder : Reminder, id : Long) {
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = androidx.work.Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    var delay : Long = 10000

    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    //Monitoring for state of work
    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                createReminderNotificationTenSecondsBefore(reminder, id)
            }
        }
}

//for multiple notification (a notification ten second before the reminder time)
//function to create the notification
//WARNING notificationId must be different for each notification (of the same WorkManager ?)
private fun createReminderNotificationTenSecondsBefore(reminder: Reminder, id : Long) {
    val notificationId : Int = id.toInt() //System.currentTimeMillis().toInt()
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("Reminder ! In ten seconds :")
        .setContentText("${reminder.reminderMessage}   ${reminderTimeToDateFormat(reminder.reminder_time)}")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(Graph.appContext)) {
        //notificationId is unique for each notification that you define
        notify(notificationId, builder.build())
    }
}

//for multiple notification
fun setNotificationBeforeTime(reminder : Reminder, id : Long, day : Long, hour : Long, minute : Long, second : Long) {
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = androidx.work.Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    var delay : Long = timeBeforeReminder (reminder.reminder_time) - notificationTimeBeforeReminderTime(day, hour, minute, second)

    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    //Monitoring for state of work
    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                createReminderNotificationBeforeTime(reminder, id, day, hour, minute, second)
            }
        }
}

//for multiple notification
//function to create the notification
//WARNING notificationId must be different for each notification (of the same WorkManager ?)
private fun createReminderNotificationBeforeTime(reminder: Reminder, id : Long, day : Long, hour : Long, minute : Long, second : Long) {
    val notificationId : Int = id.toInt() //System.currentTimeMillis().toInt()
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("Reminder : In $day day $hour hour $minute min and $second sec :")
        .setContentText("${reminder.reminderMessage}   ${reminderTimeToDateFormat(reminder.reminder_time)}")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(Graph.appContext)) {
        //notificationId is unique for each notification that you define
        notify(notificationId, builder.build())
    }
}

fun notificationTimeBeforeReminderTime(day : Long, hour : Long, minute : Long, second : Long) : Long{
    return day*86400000+hour*3600000+minute*60000+second*1000
}


data class ReminderViewState(
    val reminders: List<Reminder> = emptyList()
)


val iconList = listOf("Sports","Work","Restaurant")

