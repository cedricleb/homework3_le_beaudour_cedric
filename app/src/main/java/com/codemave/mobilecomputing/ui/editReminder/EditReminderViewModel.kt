package com.codemave.mobilecomputing.ui.editReminder


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.mobilecomputing.Graph
import com.codemave.mobilecomputing.data.entity.Reminder
import com.codemave.mobilecomputing.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class EditReminderViewModel(
    private val reminderId: Long,
    private val reminderRepository: ReminderRepository = Graph.reminderRepository
): ViewModel() {

    private val _state = MutableStateFlow(EditReminderViewState(null))

    val stateReminder: StateFlow<EditReminderViewState>
        get() = _state

    suspend fun updateReminder(reminder: Reminder) {
        return reminderRepository.updateReminder(reminder)
    }


    suspend fun deleteReminderWithId(reminderId :Long){
        reminderRepository.deleteReminderWithId(reminderId)
    }



    fun getReminder(id : Long): Reminder? {
        return reminderRepository.reminder(id)
    }

    init {
        viewModelScope.launch {
            reminderRepository.reminder(reminderId).apply {
                _state.value = EditReminderViewState(this)
            }
        }
    }
}


data class EditReminderViewState(
    val reminder: Reminder?,
)