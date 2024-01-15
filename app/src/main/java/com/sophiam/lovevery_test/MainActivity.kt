package com.sophiam.lovevery_test

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.sophiam.lovevery_test.feature_messages.data.remote.dto.MessageApi
import com.sophiam.lovevery_test.feature_messages.data.remote.dto.MessageApiResponse
import com.sophiam.lovevery_test.feature_messages.domain.model.MessageModel
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisplayProtoMessages()
        }
    }
}

@Composable
fun DisplayProtoMessages() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
                .padding(16.dp),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            text = "Lovevery Take Home Assignment",
            textAlign = TextAlign.Center
        )
        PostMessage()
        GetAllMessages()
        GetDataByUser()
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    DisplayProtoMessages()
}

@Composable
private fun PostMessage() {
    val postResponse = remember {
        mutableStateOf("see results here")
    }
    var isInputValid by remember { mutableStateOf(false) }

    var userName = remember {
        mutableStateOf(TextFieldValue())
    }

    val subject = remember {
        mutableStateOf(TextFieldValue())
    }
    val message = remember {
        mutableStateOf(TextFieldValue())
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .background(Color.LightGray)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp, 0.dp, 10.dp),
            text = "Post a message to the API",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 16.sp
        )
        TextField(
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
                .fillMaxWidth(),
            value = userName.value,
            onValueChange = { value ->
                userName.value = value
                isInputValid = validateInput(value)
            },
            placeholder = {
                Text(
                    text = "enter user name",
                )
            }
        )
        TextField(
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
                .fillMaxWidth(),
            value = subject.value,
            onValueChange = { value ->
                subject.value = value
                isInputValid = validateInput(value)
            },
            placeholder = { Text(text = "enter subject") }
        )
        TextField(
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
                .fillMaxWidth(),
            value = message.value,
            onValueChange = { value ->
                message.value = value
                isInputValid = validateInput(value)
            },
            placeholder = { Text(text = "enter short message") }
        )
        Button(
            onClick = {
                if (isInputValid) {
                    postMessage(
                        postResponse,
                        userName.value.text,
                        subject.value.text,
                        message.value.text
                    )
                } else {
                    postResponse.value = "Input is not valid, please try again"
                }
            },
            modifier = Modifier
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
        ) {
            Text(text = "post message")
        }
        Text(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 10.dp),
            text = postResponse.value,
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun GetAllMessages() {
    val response = remember {
        mutableStateOf("see results here")
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .background(Color.LightGray)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp, 0.dp, 0.dp),
            text = "Get all messages from the API",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 16.sp
        )
        Button(
            onClick = {
                getAllMessagesFromAPI(response)
            },
            modifier = Modifier
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
        ) {
            Text(text = "fetch messages")
        }
        Text(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 10.dp),
            text = response.value,
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun GetDataByUser() {
    val userResponse = remember {
        mutableStateOf("see results here")
    }

    var isInputValid by remember { mutableStateOf(false) }

    val userName = remember {
        mutableStateOf(TextFieldValue())
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .background(Color.LightGray)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp, 0.dp, 0.dp),
            text = "Get messages by User",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 16.sp
        )
        TextField(
            modifier = Modifier
                .padding(8.dp, 10.dp)
                .fillMaxWidth(),
            value = userName.value,
            onValueChange = { value ->
                userName.value = value
                isInputValid = validateInput(value)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isInputValid) {
                        getMessagesByUserFromAPI(
                            userResponse,
                            userName.value.text
                        )
                    } else {
                        userResponse.value = "Input is not valid, please try again"
                    }
                }
            ),
            placeholder = { Text(text = "enter user name") }
        )
        Button(onClick = {
            getMessagesByUserFromAPI(userResponse, userName.value.text)
        }) {
            Text(text = "get messages by user")
        }
        Text(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 10.dp),
            text = userResponse.value,
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun postMessage(
    postResult: MutableState<String>,
    user: String,
    subject: String,
    message: String
) {
    val endpointUrl = "https://abraxvasbh.execute-api.us-east-2.amazonaws.com/proto/messages/"
    val retrofit = Retrofit.Builder()
        .baseUrl(endpointUrl)
        // as we are sending data in json format so
        // we have to add Gson converter factory
        .addConverterFactory(GsonConverterFactory.create())
        // at last we are building our retrofit builder.
        .build()

    val retrofitMessageApi = retrofit.create(MessageApi::class.java)
    val messageModelBody = MessageModel(user, "add_message", subject, message)
    val call: Call<MessageApiResponse> = retrofitMessageApi.postMessageApi(messageModelBody)

    call.enqueue(object : Callback<MessageApiResponse> {
        override fun onResponse(
            call: Call<MessageApiResponse>,
            response: Response<MessageApiResponse>
        ) {
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse != null) {
                    val responseBody = apiResponse.body
                    val cleanedResponseBody = responseBody.replace("\\", "")
                    val messageModel: MessageModel =
                        Gson().fromJson(cleanedResponseBody, MessageModel::class.java)
                    postResult.value =
                        "Message saved for ${messageModel.user}"
                }
            } else {
                postResult.value = "The API call was not successful"
            }
        }

        override fun onFailure(call: Call<MessageApiResponse>, t: Throwable) {
            if (t is java.net.UnknownHostException) {
                // Unable to resolve host error
            } else {
                // Other errors
            }
        }
    })


}

private fun getAllMessagesFromAPI(
    result: MutableState<String>
) {
    // TODO: pull out constants and refactor to own file if needed
    val endpointUrl = "https://abraxvasbh.execute-api.us-east-2.amazonaws.com/proto/messages/"
    val retrofit = Retrofit.Builder()
        .baseUrl(endpointUrl)
        // as we are sending data in json format so
        // we have to add Gson converter factory
        .addConverterFactory(GsonConverterFactory.create())
        // at last we are building our retrofit builder.
        .build()

    val retrofitMessageApi = retrofit.create(MessageApi::class.java)
    val call: Call<MessageApiResponse> = retrofitMessageApi.getAllMessagesApi()

    call.enqueue(object : Callback<MessageApiResponse> {
        override fun onResponse(
            call: Call<MessageApiResponse>,
            response: Response<MessageApiResponse>
        ) {
            if (response.isSuccessful) {
                result.value = "" // clear the output first
                val apiResponse = response.body()
                if (apiResponse != null) {
                    val responseBody = apiResponse.body
                    if (responseBody != "{}") {
                        val messageModels = parseAllMessageResponse(responseBody)
                        for (messageModel in messageModels) {
                            result.value = "${result.value} \n ${messageModel.message}"
                        }
                    } else {
                        result.value = "No messages were found"
                    }
                }
            } else {
                result.value = "The API call was not successful"
            }
        }

        override fun onFailure(call: Call<MessageApiResponse>, t: Throwable) {
            Log.e("API Call", "Request failed: ${t.message}")
            result.value = "got failure"
        }
    })
}

private fun getMessagesByUserFromAPI(
    userResult: MutableState<String>,
    user: String?
) {
    // TODO: pull out constants and refactor to own file if needed
    val endpointUrl = "https://abraxvasbh.execute-api.us-east-2.amazonaws.com/proto/messages/$user/"
    val retrofit = Retrofit.Builder()
        .baseUrl(endpointUrl)
        // as we are sending data in json format so
        // we have to add Gson converter factory
        .addConverterFactory(GsonConverterFactory.create())
        // at last we are building our retrofit builder.
        .build()

    val retrofitMessageApi = retrofit.create(MessageApi::class.java)
    val call: Call<MessageApiResponse> = retrofitMessageApi.getMessageApiByUser("$user")

    call.enqueue(object : Callback<MessageApiResponse> {
        override fun onResponse(
            call: Call<MessageApiResponse>,
            response: Response<MessageApiResponse>
        ) {
            if (response.isSuccessful) {
                userResult.value = "" // reset the output
                val apiResponse = response.body()
                if (apiResponse != null) {
                    val responseBody = apiResponse.body
                    if (responseBody != "{}") {
                        val messageModels = parseUserMessageResponse(responseBody)
                        for (messageModel in messageModels) {
                            if (messageModels != null) {
                                userResult.value = "${userResult.value} \n ${messageModel.message}"
                            }
                        }
                    } else {
                        userResult.value = "No messages were found"
                    }
                } else {
                    userResult.value = "No messages were found"
                }

            } else {
                userResult.value = "The API call was not successful"
            }
        }

        override fun onFailure(call: Call<MessageApiResponse>, t: Throwable) {
            if (t is java.net.UnknownHostException) {
                // Unable to resolve host error
            } else {
                // Other errors
            }
        }
    })

}

private fun parseAllMessageResponse(jsonStringResponse: String): List<MessageModel> {
    val messageModels = mutableListOf<MessageModel>()
    try {
        val jsonObjectToConvert = JSONObject(jsonStringResponse)
        for (key in jsonObjectToConvert.keys()) {
            val jsonArray = jsonObjectToConvert.getJSONArray(key)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val message = item.getString("message")
                val subject = item.getString("subject")
                val messageModel = MessageModel(key, "add_message", subject, message)
                messageModels.add(messageModel)
            }
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    return messageModels
}

private fun parseUserMessageResponse(jsonStringResponse: String): List<MessageModel> {
    val messageModels = mutableListOf<MessageModel>()
    try {
        val jsonObjectToConvert = JSONObject(jsonStringResponse)
        val user = jsonObjectToConvert.getString("user")
        val userMessages = jsonObjectToConvert.getJSONArray("message")
        for (i in 0 until userMessages.length()) {
            val item = userMessages.getJSONObject(i)
            val subject = item.getString("subject")
            val message = item.getString("message")
            val messageModel = MessageModel(user, "add_message", subject, message)
            messageModels.add(messageModel)
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    return messageModels
}

private fun validateInput(input: TextFieldValue): Boolean {
    // add alphanumeric check or specific character checks here
    return input.text.isNotBlank()
}