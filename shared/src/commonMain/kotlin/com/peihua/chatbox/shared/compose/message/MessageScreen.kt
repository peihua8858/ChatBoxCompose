package com.peihua.chatbox.shared.compose.message

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.logo
import coil3.compose.AsyncImage
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.peihua.chatbox.shared.components.stateView.LoadingView
import com.peihua.chatbox.shared.data.db.ChatBoxMessage
import com.peihua.chatbox.shared.data.db.UserType
import com.peihua.chatbox.shared.viewmodel.MessageViewModel
import com.peihua.chatbox.shared.viewmodel.UiAction
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes
import kotlinx.coroutines.flow.Flow
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
            collect {
                lazyPagingItems.add(0, it)
            }
        } else {
            withContext(context) {
                collect {
                    lazyPagingItems.add(0, it)
                }
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
    val refresh = {
        viewModel.userAction(UiAction.QueryOrSendMsg(menuId))
    }
    if (resultData.isEmpty()) {
        refresh()
    }
    Box(
        modifier = Modifier
    ) {
        Column {
            MessageList(
                modifier =
                    modifier.weight(1f), resultData
            )
            InputText(modifier = Modifier, viewModel.enInputState.value) {
                viewModel.userAction(UiAction.QueryOrSendMsg(menuId, it))
            }
        }
    }
}

@Composable
fun InputText(modifier: Modifier, isEnabled: Boolean, sendMsg: (String) -> Unit) {
    val inputContent = remember { mutableStateOf("") }
    Box(
        // Use navigationBarsPadding() imePadding() and , to move the input panel above both the
        // navigation bar, and on-screen keyboard (IME)
        modifier = Modifier
            .padding(top = 8.dp)
            .navigationBarsPadding()
            .imePadding(),
    ) {
        Column {
            HorizontalDivider(Modifier.height(0.2.dp))
            Box(
                Modifier
                    .padding(horizontal = 4.dp)
                    .padding(top = 6.dp, bottom = 10.dp)
            ) {
                Row(
                    modifier = modifier
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
                    IconButton(
                        enabled = inputContent.value.isNotEmpty() && isEnabled,
                        onClick = {
                            sendMsg(inputContent.value)
                            inputContent.value = ""
                        }) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "")
                    }

                }
            }
        }
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, resultData: SnapshotStateList<ChatBoxMessage>) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        reverseLayout = true,
        modifier = modifier
            .fillMaxWidth()
            .background(Color(241, 241, 241))
            .padding(horizontal = 16.dp)
    ) {
        items(resultData) { item, index ->
            MessageItem(item = item, isLast = index == resultData.size - 1)
        }
    }
}

@Composable
fun MessageItem(modifier: Modifier = Modifier, item: ChatBoxMessage, isLast: Boolean = false) {
    MessageCard(item, item.userType == UserType.USER, isLast)
}

@Composable
fun MessageCard(message: ChatBoxMessage, isHuman: Boolean = false, isLast: Boolean = false) {
    Column(
        horizontalAlignment = if (isHuman) Alignment.End else Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(top = if (isLast) 120.dp else 0.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(
                    start = if (isHuman) 32.dp else 0.dp,
                    end = if (isHuman) 0.dp else 32.dp,
                )
                .background(
                    if (isHuman) Color.Blue else Color.White,
                    shape = RoundedCornerShape(12.dp)
                ),
        ) {
            if (isHuman) {
                HumanMessageCard(message = message)
            } else {
                BotMessageCard(message = message)
            }
        }
    }
}

@Composable
fun HumanMessageCard(message: ChatBoxMessage) {
    Text(
        text = message.message.value,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Justify,
    )
}


@Composable
fun BotMessageCard(message: ChatBoxMessage) {
    val isDarkTheme = isSystemInDarkTheme()
    val highlightsBuilder = remember(isDarkTheme) {
        Highlights.Builder().theme(SyntaxThemes.atom(darkMode = isDarkTheme))
    }
    Box(modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)) {
        Markdown(
            content = message.message.value.trimIndent(),
            colors = markdownColor(text = Color.Black),
            typography = markdownTypography(),
            imageTransformer = Coil3ImageTransformerImpl,
            components = markdownComponents(
                codeBlock = {
                    MarkdownHighlightedCodeBlock(
                        content = it.content,
                        node = it.node,
                        highlights = highlightsBuilder
                    )
                },
                codeFence = {
                    MarkdownHighlightedCodeFence(
                        content = it.content,
                        node = it.node,
                        highlights = highlightsBuilder
                    )
                },
            )
        )
    }
}

@Composable
fun ChatBubble(
    item: ChatBoxMessage,
    isUser: Boolean,
    modifier: Modifier = Modifier,
) {
    // 根据消息类型设置气泡颜色和对齐方式
    val bubbleColor = if (isUser) Color(0xFF007AFF) else Color(0xFFE5E5EA)
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

inline fun <T> LazyListScope.items(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T, index: Int) -> Unit,
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> key(items[index]) } else null,
    contentType = { index: Int -> contentType(items[index]) }
) {
    itemContent(items[it], it)
}
