package com.thegenzdev.amnesiopathbrowser.screens


import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.system.exitProcess

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSettingsClick: () -> Unit = {}
){
        val context = LocalContext.current

        var url by remember { mutableStateOf("") }
        var commitedUrl by remember { mutableStateOf("file:///android_asset/index.html") }
        var isFocused by remember { mutableStateOf(false) }

        // get activity to close it
        val activity = context as? Activity

        val focusManager = LocalFocusManager.current

        var showMenu by remember { mutableStateOf(false) }

        // Webview instance
        var webViewRef by remember { mutableStateOf<WebView?>(null) }

    BackHandler {
        if (webViewRef?.canGoBack() == true) {
            webViewRef?.goBack()
        } else {
            (context as? Activity)?.finishAffinity()
            Toast.makeText(context, "AmnesioPath terminated successfully", Toast.LENGTH_SHORT).show()
        }
    }



    Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        activity?.finishAffinity()
                                        Toast.makeText(context,"AmnesioPath terminated successfully", Toast.LENGTH_SHORT).show()
                                    },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text("💀", fontSize = 22.sp)
                            }


                            OutlinedTextField(
                                value = when{
                                    isFocused -> url
                                    commitedUrl == "file:///android_asset/index.html" -> ""
                                    else -> commitedUrl
                                },
                                onValueChange = { url = it },
                                singleLine = true,
                                textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                                placeholder = { Column(verticalArrangement = Arrangement.Center){Text("Search or enter address")}},
                                modifier = Modifier.fillMaxWidth()
                                    .padding(start = 2.dp)
                                    .height(52.dp)
                                    .onFocusChanged{ focusState ->
                                        isFocused = focusState.isFocused
                                        if(isFocused){
                                            url = ""
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Go
                                ),
                                keyboardActions = KeyboardActions(
                                    onGo = {
                                        commitedUrl = when {
                                            url.startsWith("http://") ||
                                                    url.startsWith("https://") ||
                                                    url.startsWith("file:///") -> url
                                            url.contains(".") -> "https://$url"
                                            else -> "https://duckduckgo.com/?q=${Uri.encode(url)}"
                                        }

                                        focusManager.clearFocus()
                                    }
                                )
                            )
                        }

                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    showMenu = true
                                }
                        ) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "More Options")

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false}
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Clear Cache")},
                                    onClick = {
                                        webViewRef?.clearCache(true)
                                        Toast.makeText(context,"Cache Cleared! 🧹", Toast.LENGTH_SHORT).show()
                                        showMenu = false
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text("Toggle Cookies") },
                                    onClick = {
                                        val cookieManager = CookieManager.getInstance()
                                        val current = cookieManager.acceptCookie()
                                        cookieManager.setAcceptCookie(!current)

                                        webViewRef?.let{
                                            cookieManager.setAcceptThirdPartyCookies(it, !current)
                                        }

                                        Toast.makeText(
                                            context,
                                            if(!current) "Cookies Enabled 🍪" else "Cookies Disabled 💀",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        showMenu = false
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text("Settings") },
                                    onClick = {
                                        showMenu = false
                                        onSettingsClick()
                                    }
                                )

                                DropdownMenuItem(text = { Text("Exit") }, onClick = {
                                    activity?.finishAffinity()
                                    Toast.makeText(context, "AmnesioPath terminated successfully", Toast.LENGTH_SHORT).show()
                                    showMenu = false
                                })


                            }
                        }
                    }
                )



            },


        )
    { padding ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            factory = {
                WebView(context).apply {
                    webViewRef = this

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.setSupportZoom(true)
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false

                    // Disable cookies BEFORE loading anything
                    CookieManager.getInstance().setAcceptCookie(false)
                    CookieManager.getInstance().setAcceptThirdPartyCookies(webViewRef, false)

                    // Clear cache/history BEFORE loading
                    clearCache(true)
                    clearHistory()

                    // keeping navigation inside webview
                    webViewClient = object : WebViewClient() {}

                    loadUrl(commitedUrl)
                }
            },
            update = { webView ->
                if (webView.url != commitedUrl && commitedUrl.isNotBlank()) {
                    webView.loadUrl(commitedUrl)
                }
            }
        )


    }
}