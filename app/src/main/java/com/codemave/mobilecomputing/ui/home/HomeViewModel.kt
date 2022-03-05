package com.codemave.mobilecomputing.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.mobilecomputing.Graph
import com.codemave.mobilecomputing.data.entity.Reminder
import com.codemave.mobilecomputing.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState())

    val state: StateFlow<HomeViewState>
        get() = _state

    init {
        viewModelScope.launch {
            reminderRepository.reminders().collect { reminders ->
                _state.value = HomeViewState(reminders)
            }
        }
    }
}

data class HomeViewState(
    var reminders: List<Reminder> = emptyList()
)