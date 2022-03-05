package com.codemave.mobilecomputing.ui.reminder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codemave.mobilecomputing.R
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch

@Composable
fun Reminder(
    onBackPress: () -> Unit,
    viewModel: ReminderViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val message = rememberSaveable { mutableStateOf("") }
    val day = rememberSaveable { mutableStateOf("") }
    val month = rememberSaveable { mutableStateOf("") }
    val year = rememberSaveable { mutableStateOf("") }
    val hour = rememberSaveable { mutableStateOf("") }
    val minute = rememberSaveable { mutableStateOf("") }

    var selectedIcon = rememberSaveable { mutableStateOf("") }

    //variables to set if there is no notification, one notification, or multiple notifications
    var notification = rememberSaveable {mutableStateOf(false)}
    var multipleNotification = rememberSaveable {mutableStateOf(false)}

    //variables to set the time of the multiple notification
    val d = rememberSaveable { mutableStateOf("0") }
    val h = rememberSaveable { mutableStateOf("00") }
    val min = rememberSaveable { mutableStateOf("00") }
    val sec = rememberSaveable { mutableStateOf("00") }

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
                Text(text = "Add Reminder")
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
                        label = { Text(text = "Day : dd")},
                        modifier = Modifier.size(110.dp,60.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = month.value,
                        onValueChange = { month.value = it },
                        label = { Text(text = "Month : mm")},
                        modifier = Modifier.size(110.dp,60.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = year.value,
                        onValueChange = { year.value = it },
                        label = { Text(text = "Year : yy")},
                        modifier = Modifier.size(110.dp,60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row{
                    OutlinedTextField(
                        value = hour.value,
                        onValueChange = { hour.value = it },
                        label = { Text(text = "Hour : hh")},
                        modifier = Modifier.size(120.dp,60.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = minute.value,
                        onValueChange = { minute.value = it },
                        label = { Text(text = "Minute : mm")},
                        modifier = Modifier.size(120.dp,60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                IconListDropdown(
                    selectedIcon = selectedIcon
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row{
                    Text(
                        text = "Notification : ",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .heightIn(max = 24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Checkbox(
                        checked = notification.value,
                        onCheckedChange = { notification.value = it }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                if(notification.value){
                    Row{
                        Text(
                            text = "Multiple notification : ",
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .heightIn(max = 24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Checkbox(
                            checked = multipleNotification.value,
                            onCheckedChange = { multipleNotification.value = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    if(multipleNotification.value){
                        Text(
                            text = "How long before the reminder time ?",
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .heightIn(max = 24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Row{
                            OutlinedTextField(
                                value = d.value,
                                onValueChange = { d.value = it },
                                label = { Text(text = "Day")},
                                modifier = Modifier.size(80.dp,60.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            OutlinedTextField(
                                value = h.value,
                                onValueChange = { h.value = it },
                                label = { Text(text = "Hour")},
                                modifier = Modifier.size(80.dp,60.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            OutlinedTextField(
                                value = min.value,
                                onValueChange = { min.value = it },
                                label = { Text(text = "Min")},
                                modifier = Modifier.size(80.dp,60.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            OutlinedTextField(
                                value = sec.value,
                                onValueChange = { sec.value = it },
                                label = { Text(text = "Sec")},
                                modifier = Modifier.size(80.dp,60.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

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
                            var reminderTime : String = day.value + month.value + year.value + hour.value + minute.value
                            var reminderAlreadyOccurred : Boolean = false
                            if(timeBeforeReminder(reminderTime) <= 0){
                                reminderAlreadyOccurred = true
                            }

                            coroutineScope.launch {
                                viewModel.saveReminder(
                                    com.codemave.mobilecomputing.data.entity.Reminder(
                                        reminderMessage = message.value,
                                        location_x = "",
                                        location_y = "",
                                        //reminder time format ddmmyyhhmm
                                        reminder_time = reminderTime,
                                        creation_time = "",
                                        creator_id = "",
                                        reminder_seen = reminderAlreadyOccurred,
                                        reminder_icon = selectedIcon.value,
                                        notification = notification.value,
                                        multiple_notification = multipleNotification.value
                                    ),
                                    d.value.toLong(),
                                    h.value.toLong(),
                                    min.value.toLong(),
                                    sec.value.toLong()
                                )
                            }
                            onBackPress()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text("Save reminder")
                }
            }
        }
    }
}

@Composable
fun IconListDropdown(selectedIcon :  MutableState<String>) {

    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) {
        Icons.Filled.ArrowDropUp // requires androidx.compose.material:material-icons-extended dependency
    } else {
        Icons.Filled.ArrowDropDown
    }

    Column {
        OutlinedTextField(
            value = selectedIcon.value,
            onValueChange = { selectedIcon.value = it},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Icon") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            iconList.forEach { dropDownOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedIcon.value = dropDownOption
                        expanded = false
                    },
                    enabled = true
                ) {
                    Text(text = dropDownOption)
                }

            }
        }
    }
}

