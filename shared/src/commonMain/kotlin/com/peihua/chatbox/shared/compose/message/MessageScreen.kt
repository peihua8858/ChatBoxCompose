package com.peihua.chatbox.shared.compose.message

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.logo
import coil3.compose.AsyncImage
import com.peihua.chatbox.shared.components.stateView.ErrorView
import com.peihua.chatbox.shared.components.stateView.LoadingView
import com.peihua.chatbox.shared.db.ChatBoxMessage
import com.peihua.chatbox.shared.db.UserType
import com.peihua.chatbox.shared.utils.DLog
import com.peihua.chatbox.shared.viewmodel.MessageViewModel
import com.peihua.chatbox.shared.utils.ResultData
import com.peihua.chatbox.shared.utils.dLog
import com.peihua.chatbox.shared.viewmodel.UiAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun <T> Flow<T>.collectAsLazyItems(
    context: CoroutineContext = EmptyCoroutineContext,
): SnapshotStateList<T> {

    val lazyPagingItems = remember { mutableStateListOf<T>() }

    LaunchedEffect(lazyPagingItems) {
        if (context == EmptyCoroutineContext) {
            toCollection(lazyPagingItems)
        } else {
            withContext(context) {
                toCollection(lazyPagingItems)
            }
        }
    }
    return lazyPagingItems
}


@Composable
fun MessageScreen(
    menuId: Long, modifier: Modifier = Modifier,
    viewModel: MessageViewModel = viewModel(MessageViewModel::class),
) {
    val resultData = viewModel.pagingDataFlow.collectAsLazyItems()
    val rState = remember { mutableStateOf(resultData) }
//    if (rState.value.isEmpty()) {
//        LoadingView()
//        DLog { "MessageScreen>>>>>> load messages" }
//        viewModel.userAction(UiAction.LoadMessages(menuId))
//    } else {
        MessageContent(modifier, resultData,{
            viewModel. dLog { "MessageScreen>>>>>> load messages" }
            viewModel.userAction(UiAction.SendMsg(menuId,""))
        }) {
            viewModel.userAction(UiAction.SendMsg(menuId, it))
        }
//    }
//    val resultData = viewModel.listMsgState
//    val data = resultData.value
//    when (data) {
//        is ResultData.Initialize -> viewModel.userAction(UiAction.LoadMessages(menuId))
//        is ResultData.Success -> {
//            val msgItems = resultDa
//            MessageContent(modifier, msgItems) {
//                viewModel.userAction(UiAction.SendMsg(menuId, it))
//            }
//        }
//
//        is ResultData.Starting -> LoadingView()
//        is ResultData.Failure -> ErrorView {
//            viewModel.userAction(UiAction.LoadMessages(menuId))
////            viewModel.requestMessageById(menuId)
//        }
//    }
}

@Composable
fun MessageContent(
    modifier: Modifier = Modifier,
    msgItems: List<ChatBoxMessage>,
    refresh: () -> Unit,
    sendMsg: (String) -> Unit,
) {
    val inputContent = remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    Box(
        modifier = Modifier
    ) {
        Column {
           Button(onClick = {
              refresh()
           }) {
               Text(text = "刷新数据")
           }
            LazyColumn(
                state = listState,
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color(241, 241, 241))
                    .weight(1f)
            ) {
                items(msgItems) {
                    MessageItem(item = it)
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .wrapContentHeight()

            ) {
                OutlinedTextField(
                    value = inputContent.value,
                    onValueChange = {
                        inputContent.value = it
                    },
                    modifier = Modifier.weight(1f)
                )
                IconButton({
                    sendMsg(inputContent.value)
                    inputContent.value = ""
                }) {
                    Icon(Icons.Default.Send, contentDescription = "")
                }

            }
            // 监听消息变化并滚动到底部
            LaunchedEffect(msgItems.size) {
                if (msgItems.isNotEmpty()) {
                    // 滚动到最后一项
                    listState.animateScrollToItem(msgItems.size - 1)
                }
            }
        }

    }
}

@Composable
fun MessageItem(modifier: Modifier = Modifier, item: ChatBoxMessage) {
    ChatBubble(item, item.userType == UserType.USER)
}


@Composable
fun ChatBubble(
    item: ChatBoxMessage,
    isUser: Boolean,
    modifier: Modifier = Modifier,
) {
    // 根据消息类型设置气泡颜色和对齐方式
    val bubbleColor = if (isUser) Color(0xFF007AFF) else Color(0xFFE5E5EA)
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            // 显示 AI 头像
            Avatar(avatar = if (item.icon.isEmpty()) Res.drawable.logo else item.icon)
            Spacer(modifier = Modifier.width(8.dp))
        }

        // 聊天气泡
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = bubbleColor,
            shadowElevation = 2.dp
        ) {
            Text(
                text = item.message.value,
                style = TextStyle(
                    color = if (isUser) Color.White else Color.Black,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .padding(12.dp)
            )
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // 显示用户头像
            Avatar(avatar = if (item.icon.isEmpty()) Res.drawable.logo else item.icon)
        }
    }
}

@Composable
fun Avatar(avatar: Any) {
    if (avatar is DrawableResource) {
        Image(
            painter = painterResource(avatar), // 使用资源 ID 加载头像
            contentDescription = "Avatar",
            modifier = Modifier
                .size(40.dp) // 头像大小
                .clip(CircleShape) // 圆形裁剪
                .background(Color.LightGray), // 默认背景色
            contentScale = ContentScale.Crop // 图片裁剪方式
        )
    } else {
        AsyncImage(
            model = avatar, // 使用资源 ID 加载头像
            contentDescription = "Avatar",
            modifier = Modifier
                .size(40.dp) // 头像大小
                .clip(CircleShape) // 圆形裁剪
                .background(Color.LightGray), // 默认背景色
            contentScale = ContentScale.Crop // 图片裁剪方式
        )
    }
}
