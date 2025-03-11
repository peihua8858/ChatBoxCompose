package com.peihua.chatbox.shared.theme

enum class ThemeMode(val index: Int) {
    /**
     *  根据用户选择的内容使用浅色或深色主题系统设置。
     */
    system(0),

    /**
     * 始终使用 Light 模式，而不管系统首选项如何。
     */
    light(1),

    /**
     * 始终使用深色模式（如果可用），而不管系统首选项如何。
     */
    dark(2),
}