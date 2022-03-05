package com.codemave.mobilecomputing.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codemave.mobilecomputing.R
import com.codemave.mobilecomputing.data.entity.Reminder
import com.google.accompanist.insets.systemBarsPadding


@Composable
fun Home(
    navController: NavController,
    username : String
) {

    var viewModel: HomeViewModel = viewModel()

        Surface(modifier = Modifier.fillMaxSize()) {

            Scaffold(
                modifier = Modifier.padding(bottom = 24.dp),
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate(route = "reminder") },
                        contentColor = Color.Blue,
                        modifier = Modifier.padding(all = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .systemBarsPadding()
                        .fillMaxWidth()
                ) {
                    val appBarColor = MaterialTheme.colors.secondary.copy(alpha = 0.87f)

                    HomeAppBar(
                        backgroundColor = appBarColor,
                        navController = navController,
                        username = username
                    )

                    val viewState by viewModel.state.collectAsState()
                    Column(modifier = Modifier.systemBarsPadding().fillMaxWidth()) {
                        ReminderList(
                            list = viewState.reminders,
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }

}


@Composable
private fun HomeAppBar(
    backgroundColor: Color,
    navController: NavController,
    username : String
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor,
        actions = {
            IconButton( onClick = {navController.navigate("myProfile/${username}")} ) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.account))
            }
            IconButton( onClick = { navController.navigate(route = "login") } ) {
                Icon(imageVector = Icons.Filled.Logout, contentDescription = stringResource(R.string.account))
            }
        }
    )
}


@Composable
private fun ReminderList(
    list: List<Reminder>,
    viewModel: HomeViewModel,
    navController: NavController,
) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            if(item.reminder_seen){
                ReminderListItem(
                    reminder = item,
                    viewModel = viewModel,
                    onClick = {},
                    modifier = Modifier.fillParentMaxWidth(),
                    navController = navController,
                )
            }
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    viewModel: HomeViewModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    ConstraintLayout(modifier = modifier.clickable { onClick() }) {
        val (divider,icon, reminderTitle, editIcon, date) = createRefs()
        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        //icon
        when (reminder.reminder_icon) {

            "Sports" -> Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .padding(6.dp)
                    .constrainAs(icon) {
                        top.linkTo(parent.top, 10.dp)
                        bottom.linkTo(parent.bottom, 10.dp)
                        start.linkTo(parent.start, 10.dp)
                    }
            )

            "Work" -> Icon(
                imageVector = Icons.Default.Work,
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .padding(6.dp)
                    .constrainAs(icon) {
                        top.linkTo(parent.top, 10.dp)
                        bottom.linkTo(parent.bottom, 10.dp)
                        start.linkTo(parent.start, 10.dp)
                    }
            )


            "Restaurant" -> Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .padding(6.dp)
                    .constrainAs(icon) {
                        top.linkTo(parent.top, 10.dp)
                        bottom.linkTo(parent.bottom, 10.dp)
                        start.linkTo(parent.start, 10.dp)
                    }
            )
        }

        // title
        Text(
            text = reminder.reminderMessage,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(reminderTitle) {
                linkTo(
                    start = icon.end,
                    end = editIcon.start,
                    startMargin = 10.dp,
                    endMargin = 16.dp,
                    bias = 0f // float this towards the start. this was is the fix we needed
                )
                top.linkTo(parent.top, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )

        // date
        Text(
            text = reminderTimeToDateFormat(reminder.reminder_time),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(date) {
                linkTo(
                    start = icon.end,
                    end = editIcon.start,
                    startMargin = 10.dp,
                    endMargin = 16.dp,
                    bias = 0f // float this towards the start. this was is the fix we needed
                )
                top.linkTo(reminderTitle.bottom, margin = 6.dp)
                bottom.linkTo(parent.bottom, 10.dp)
                width = Dimension.preferredWrapContent
            }
        )



        // editIcon
        IconButton(
            onClick = {
                navController.navigate(route = "edit_reminder/${reminder.reminderId}")
            },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(editIcon) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.ModeEdit,
                contentDescription = stringResource(R.string.check_mark)
            )
        }
    }
}


fun reminderTimeToDateFormat(reminderTime : String) : String {
    var d : String = ""
    d += reminderTime.subSequence(0,2).toString()
    d += "/"
    d += reminderTime.subSequence(2,4).toString()
    d += "/"
    d += reminderTime.subSequence(4,6).toString()
    d += " "
    d += reminderTime.subSequence(6,8).toString()
    d += ":"
    d += reminderTime.subSequence(8,10).toString()
    return d
}