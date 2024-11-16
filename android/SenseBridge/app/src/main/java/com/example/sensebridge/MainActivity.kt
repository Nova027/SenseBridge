package com.example.sensebridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkAnnotation.Clickable
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.sensebridge.ui.theme.SenseBridgeTheme
@OptIn(ExperimentalMaterial3Api::class)

class MainActivity : ComponentActivity()
{
    private external fun stringFromJNI(): String
    private external fun stringFromJNI2(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.loadLibrary("sensebridge_cpp_v1")
        System.loadLibrary("sensebridge_cpp_v2")
        enableEdgeToEdge()
        setContent {
            HomeScreenUI()
        }
    }

    @Composable
    fun HomeScreenUI()
    {
        var showAddSessionDialog = remember { mutableStateOf(false) }
        var sessionCount = remember { mutableStateOf(0) }

        Scaffold(
            topBar = { TopBar("Home") },
            floatingActionButton = { AddSessionButton(showAddSessionDialog) })
        { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
            {
                // Text(text = "$innerPadding ${stringFromJNI()} ${stringFromJNI2()}!", modifier = Modifier.padding(16.dp))
                if (sessionCount.value > 0)
                {
                    LazyColumn(modifier = Modifier.padding(16.dp))
                    {
                        items(sessionCount.value)
                        { index ->
                            Card(shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().height(100.dp))
                            {
                                Text(text = "Session $index", modifier = Modifier.padding(8.dp))
                            }
                            HorizontalDivider()
                        }
                    }
                }
                else
                {
                    Text(text = "Nothing to see here!", modifier = Modifier.padding(16.dp))
                }
            }
        }

        if (showAddSessionDialog.value)
        {
            AddSessionDialog(showAddSessionDialog, sessionCount)
        }
    }

    @Composable
    fun TopBar(text: String = "Top Bar")
    {
        TopAppBar(colors = topAppBarColors(containerColor = Color(0xFF44A6CC)),
            title = {Text(text, color = Color.White)})
    }

    @Composable
    fun AddSessionButton(showAddSessionDialog: MutableState<Boolean>)
    {
        FloatingActionButton(onClick = { showAddSessionDialog.value = true }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }

    @Composable
    fun AddSessionDialog(showAddSessionDialog: MutableState<Boolean>, sessionCount: MutableState<Int>)
    {
        Dialog(onDismissRequest = { showAddSessionDialog.value = false })
        {
            Card(shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(16.dp).fillMaxWidth().height(600.dp))
            {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text("Add Session", color = Color.Black)

                    Text(buildAnnotatedString {
                        val link = Clickable(tag = "OK",
                            linkInteractionListener = LinkInteractionListener {
                                showAddSessionDialog.value = false
                                sessionCount.value++
                            })

                            withLink(link) {
                                append("OK")
                            }
                        }
                    )

                }
            }
        }
    }
}