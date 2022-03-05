package com.codemave.mobilecomputing.ui.editReminder

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codemave.mobilecomputing.data.entity.Reminder
import com.codemave.mobilecomputing.ui.reminder.IconListDropdown
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch
import com.codemave.mobilecomputing.util.viewModelProviderFactoryOf


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun EditReminder(
    onBackPress: () -> Unit,
    id : Long
) {

    val viewModel : EditReminderViewModel = viewModel(
        key = "reminder_id_" + id.toString(),
        factory = viewModelProviderFactoryOf { EditReminderViewModel(id) }
    )
    val coroutineScope = rememberCoroutineScope()

    var rem : Reminder?
    var message = rememberSaveable { mutableStateOf("") }

    val location_x = rememberSaveable { mutableStateOf("") }
    val location_y = rememberSaveable { mutableStateOf("") }

    val day = rememberSaveable { mutableStateOf("") }
    val month = rememberSaveable { mutableStateOf("") }
    val year = rememberSaveable { mutableStateOf("") }
    val hour = rememberSaveable { mutableStateOf("") }
    val minute = rememberSaveable { mutableStateOf("") }

    val creation_time = rememberSaveable { mutableStateOf("") }
    val creator_id = rememberSaveable { mutableStateOf("") }
    val reminder_seen = rememberSaveable { mutableStateOf("") }

    var reminder_time : String =""

    var selectedIcon = rememberSaveable { mutableStateOf("") }

    var notification : Boolean = false
    var multipleNotification : Boolean = false

    coroutineScope.launch {
        rem = viewModel.getReminder(id)
        message.value = rem?.reminderMessage.toString()
        location_x.value = rem?.location_x.toString()
        location_y.value = rem?.location_y.toString()
        reminder_time = rem?.reminder_time.toString()
        creation_time.value = rem?.creation_time.toString()
        creator_id.value = rem?.creator_id.toString()
        reminder_seen.value = rem?.reminder_seen.toString()
        selectedIcon.value = rem?.reminder_icon.toString()

        // warning :
        // the end index in subSequence is the index of the character that will not be in the selection
        day.value = reminder_time.subSequence(0,2).toString()
        month.value = reminder_time.subSequence(2,4).toString()
        year.value = reminder_time.subSequence(4,6).toString()
        hour.value = reminder_time.subSequence(6,8).toString()
        minute.value = reminder_time.subSequence(8,10).toString()

        notification = rem?.notification.toString().toBoolean()
        multipleNotification = rem?.multiple_notification.toString().toBoolean()
    }


    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick = onBackPress
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(text = "Edit Reminder")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = message.value,
                    onValueChange = { message.value = it },
                    label = { Text(text = "Reminder message")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row{
                    OutlinedTextField(
                        value = day.value,
                        onValueChange = { day.value = it },
                        label = { Text(text = "Reminder day : dd")},
                        modifier = Modifier.size(110.dp,60.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = month.value,
                        onValueChange = { month.value = it },
                        label = { Text(text = "Reminder month : mm")},
                        modifier = Modifier.size(110.dp,60.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = year.value,
                        onValueChange = { year.value = it },
                        label = { Text(text = "Reminder year : yy")},
                        modifier = Modifier.size(110.dp,60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row{
                    OutlinedTextField(
                        value = hour.value,
                        onValueChange = { hour.value = it },
                        label = { Text(text = "Reminder hour : hh")},
                        modifier = Modifier.size(120.dp,60.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = minute.value,
                        onValueChange = { minute.value = it },
                        label = { Text(text = "Reminder minute : mm")},
                        modifier = Modifier.size(120.dp,60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                IconListDropdown(
                    selectedIcon = selectedIcon
                )
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    enabled = true,
                    onClick = {
                        if(
                            day.value.length == 2
                            && month.value.length == 2
                            && year.value.length == 2
                            && hour.value.length == 2
                            && minute.value.length == 2
                        ) {
                            coroutineScope.launch {
                                viewModel.updateReminder(
                                    com.codemave.mobilecomputing.data.entity.Reminder(
                                        reminderId = id,
                                        reminderMessage = message.value,
                                        location_x = location_x.value,
                                        location_y = location_y.value,
                                        //reminder time format ddmmyyhhmm
                                        reminder_time = day.value + month.value + year.value + hour.value + minute.value,
                                        creation_time = creation_time.value,
                                        creator_id = creator_id.value,
                                        reminder_seen = reminder_seen.value.toBoolean(),
                                        reminder_icon = selectedIcon.value,
                                        notification = notification,
                                        multiple_notification = multipleNotification
                                    )
                                )
                            }
                            onBackPress()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text("Save changes")
                }

                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    enabled = true,
                    onClick = {

                        coroutineScope.launch {
                            viewModel.deleteReminderWithId(reminderId = id)
                        }
                        onBackPress()

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text("Delete reminder")
                }
            }
        }
    }
}

