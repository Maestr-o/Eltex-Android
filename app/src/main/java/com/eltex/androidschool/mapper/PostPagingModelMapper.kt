package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.NoteStatus
import com.eltex.androidschool.model.PagingModel
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.PostUiState
import com.eltex.androidschool.reducer.PostReducer

class PostPagingModelMapper {

    fun map(postUiState: PostUiState): List<PagingModel<PostUiModel>> {
        val posts: List<PagingModel.Data<PostUiModel>> = postUiState.posts.map {
            PagingModel.Data(it)
        }

        return when (val status = postUiState.status) {
            is NoteStatus.NextPageError -> posts + PagingModel.Error(status.reason)
            is NoteStatus.NextPageLoading -> posts + List(PostReducer.PAGE_SIZE) { PagingModel.Progress }
            NoteStatus.EmptyLoading -> List(PostReducer.INITIAL_LOAD_SIZE) { PagingModel.Progress }
            else -> posts
        }
    }
}