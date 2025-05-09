package com.peihua.chatbox.common

enum class ListTileControlAffinity {
    /**
     * Position the control on the leading edge, and the secondary widget, if
     * any, on the trailing edge.
     */
    leading,

    /**
     * Position the control on the trailing edge, and the secondary widget, if
     * any, on the leading edge.
     */
    trailing,

    /**
     * Position the control relative to the text in the fashion that is typical
     * for the current platform, and place the secondary widget on the opposite
     * side.
     */
    platform,
}