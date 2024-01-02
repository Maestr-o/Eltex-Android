package com.eltex.androidschool.repository

import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.utils.Callback

class NetworkEventRepository(
    private val api: EventsApi
) : EventRepository {

    override fun getEvents(callback: Callback<List<Event>>) {
        api.getAll().enqueue(
            object : retrofit2.Callback<List<Event>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Event>>,
                    response: retrofit2.Response<List<Event>>
                ) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Event>>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun likeById(id: Long, callback: Callback<Event>) {
        api.like(id).enqueue(
            object : retrofit2.Callback<Event> {
                override fun onResponse(
                    call: retrofit2.Call<Event>,
                    response: retrofit2.Response<Event>
                ) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun unlikeById(id: Long, callback: Callback<Event>) {
        api.unlike(id).enqueue(
            object : retrofit2.Callback<Event> {
                override fun onResponse(
                    call: retrofit2.Call<Event>,
                    response: retrofit2.Response<Event>
                ) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun participateById(id: Long, callback: Callback<Event>) {
        api.participate(id).enqueue(
            object : retrofit2.Callback<Event> {
                override fun onResponse(
                    call: retrofit2.Call<Event>,
                    response: retrofit2.Response<Event>
                ) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun unparticipateById(id: Long, callback: Callback<Event>) {
        api.unparticipate(id).enqueue(
            object : retrofit2.Callback<Event> {
                override fun onResponse(
                    call: retrofit2.Call<Event>,
                    response: retrofit2.Response<Event>
                ) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun saveEvent(id: Long, content: String, callback: Callback<Event>) {
        api.save(
            Event(
                id = id,
                content = content,
                datetime = "2024-01-03T17:00:00.248Z",
                link = "google.com",
            )
        ).enqueue(
            object : retrofit2.Callback<Event> {
                override fun onResponse(
                    call: retrofit2.Call<Event>,
                    response: retrofit2.Response<Event>
                ) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun deleteById(id: Long, callback: Callback<Unit>) {
        api.delete(id).enqueue(
            object : retrofit2.Callback<Unit> {
                override fun onResponse(
                    call: retrofit2.Call<Unit>,
                    response: retrofit2.Response<Unit>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }
}