package com.tvchannels.sample.coreui.utils

import android.text.Editable
import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView

typealias RecyclerViewHolder = RecyclerView.ViewHolder

typealias InputAfterWatcher = (Editable?) -> Unit

typealias InputWatcher = (
    text: CharSequence?,
    start: Int,
    before: Int,
    count: Int
) -> Unit


typealias  SwitchWatcher = CompoundButton.OnCheckedChangeListener

typealias  SpanClick = () -> Unit

typealias  InputFocusDetector = View.OnFocusChangeListener



