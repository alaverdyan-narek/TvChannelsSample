package com.tvchannels.sample.domain.extensions

inline fun Boolean.onFalse(crossinline  action:()->Unit):Boolean = apply {
    if(!this) action()
}
inline fun Boolean.onTrue(crossinline  action:()->Unit):Boolean = apply {
    if(this) action()
}
fun Boolean?.orFalse() = this ?: false