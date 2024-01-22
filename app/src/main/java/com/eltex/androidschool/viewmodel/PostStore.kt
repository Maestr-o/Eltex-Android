package com.eltex.androidschool.viewmodel

import com.eltex.androidschool.model.PostEffect
import com.eltex.androidschool.model.PostMessage
import com.eltex.androidschool.model.PostUiState
import com.eltex.androidschool.mvi.Store

typealias PostStore = Store<PostUiState, PostMessage, PostEffect>