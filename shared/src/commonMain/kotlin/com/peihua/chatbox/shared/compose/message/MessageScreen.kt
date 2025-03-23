package com.peihua.chatbox.shared.compose.message

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peihua.chatbox.shared.components.stateView.ErrorView
import com.peihua.chatbox.shared.components.stateView.LoadingView
import com.peihua.chatbox.shared.db.Message
import com.peihua.chatbox.shared.repository.MessageRepository
import com.peihua.chatbox.shared.utils.ResultData
import com.peihua.chatbox.shared.utils.isError
import com.peihua.chatbox.shared.utils.isInitialize
import com.peihua.chatbox.shared.utils.isStarting
import com.peihua.chatbox.shared.utils.isSuccess

@Composable
fun MessageScreen(
    menuId: Long, modifier: Modifier = Modifier,
    viewModel: MessageRepository = viewModel(MessageRepository::class),
) {
    val resultData = viewModel.listMsgState
    val data = resultData.value
    when (data) {
        is ResultData.Initialize -> viewModel.requestAllMessagesByMenuId(menuId)
        is ResultData.Success -> {
            val msgItems = (resultData.value as ResultData.Success).data
            MessageContent(modifier, msgItems)
        }

        is ResultData.Starting -> LoadingView()
        is ResultData.Failure -> ErrorView {
            viewModel.requestMessageById(menuId)
        }
    }
}

@Composable
fun MessageContent(modifier: Modifier = Modifier, msgItems: List<Message>) {
    Box(
        modifier = Modifier
            .padding(32.dp)
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            items(msgItems) {
                MessageItem(item = it)
            }
        }
    }
}

@Composable
fun MessageItem(modifier: Modifier = Modifier, item: Message) {
    Text(text = item.content)
}
