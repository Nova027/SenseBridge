package com.example.sensebridge

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.sensebridge.ui.theme.Purple80
import com.example.sensebridge.ui.theme.CardPurpleGrey10
@OptIn(ExperimentalMaterial3Api::class)

class HomeScreen {
    @Composable
    fun HomeScreenUI(mainContext: Context)
    {
        var showAddSessionDialog = remember { mutableStateOf(false) }
        var voiceButtonPressed = remember { mutableStateOf(false) }
        var sessionCount = remember { mutableIntStateOf(0) }
        var selectedFeature = remember { mutableStateOf(FeatureInfo("Invalid", 0)) }
        var isFeatureSelected = remember { mutableStateOf(false) }

        // Scaffold to organize the UI with topBar, floatingActionButtons and Content
        Scaffold(topBar = { TopBar("Home") },
            floatingActionButton = { ActionButtons(showAddSessionDialog, voiceButtonPressed) })
        { innerPadding ->
            // Column to hold the content of the home screen, based on scaffold placement
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding))
            {
                // Display a scrollable list of previously created sessions, If they exist
                if (sessionCount.intValue > 0) {
                    LazyColumn(modifier = Modifier.padding(16.dp))
                    {
                        items(sessionCount.intValue) { index ->
                            SessionDataView(index)
                        }
                    }
                }
                // If no sessions created
                else {
                    Text(text = "\n\n\n\n\n\n\n\n\n\n\n\n\n\nNothing to see here!", fontSize = 18.sp,
                        textAlign = TextAlign.Center, modifier = Modifier
                            .size(400.dp)
                            .padding(16.dp))
                }
            }
        }

        // If addSession button is clicked, show dialog to add a new session
        if (showAddSessionDialog.value)
            AddSessionDialog(showAddSessionDialog, selectedFeature, sessionCount, mainContext, isFeatureSelected)

        // If mic button is clicked, .. to do
        if (voiceButtonPressed.value) {
            Toast.makeText(mainContext, "To Do", Toast.LENGTH_SHORT).show()
            voiceButtonPressed.value = false
        }

        // If a new session is added with selected feature, show a toast
        if (isFeatureSelected.value) {
            Toast.makeText(mainContext, "Session Created for ${selectedFeature.value.name}", Toast.LENGTH_SHORT).show()
            isFeatureSelected.value = false
        }
    }

    // TopBar to be used by Home screen Scaffold
    @Composable
    fun TopBar(text: String = "Top Bar")
    {
        TopAppBar(colors = topAppBarColors(containerColor = Purple80),
            title = {Text(text, color = Color(0xFF000055), fontWeight = FontWeight.Bold)})
    }

    // Action Buttons to be used by Home screen Scaffold
    @Composable
    fun ActionButtons(showAddSessionDialog: MutableState<Boolean>, voiceButtonPressed : MutableState<Boolean>)
    {
        Column {
            // Button to provide voice input
            FloatingActionButton(onClick = { voiceButtonPressed.value = true }) {
                Icon(painter = painterResource(R.drawable.mic_24px), contentDescription = "Mic")
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Button to add a new session
            FloatingActionButton(onClick = { showAddSessionDialog.value = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }

    // To display Session Data in a LazyColumn within content under Home screen Scaffold
    @Composable
    fun SessionDataView(index : Int)
    {
        // Card to hold the session data
        Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(CardPurpleGrey10),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp))
        {
            Text(text = "Session $index", modifier = Modifier.padding(8.dp))
        }
        // Horizontal divider to separate from next card
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(modifier = Modifier.padding(1.dp))
        Spacer(modifier = Modifier.height(4.dp))
    }

    // Dialog box as an overlaid card, to add a new session
    @Composable
    fun AddSessionDialog(showAddSessionDialog: MutableState<Boolean>, selectedFeature: MutableState<FeatureInfo>,
                         sessionCount: MutableIntState, mainContext: Context, isFeatureSelected: MutableState<Boolean>)
    {
        var currentFeatureSelected = remember { mutableStateOf(FeatureInfo("Invalid", 0)) }
        Dialog(onDismissRequest = { showAddSessionDialog.value = false })
        {
            Card(shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(400.dp))
            {
                Column(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth())
                {
                    Text("Choose Feature for New Session", color = Color.Black)
                    Spacer(modifier = Modifier.height(32.dp))

                    // Adding multiple rows of radio buttons to select a feature
                    for (feature in featuresAvailable) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(25.dp),
                            verticalAlignment = Alignment.CenterVertically)
                        {
                            RadioButton(onClick = { currentFeatureSelected.value = feature },
                                selected = (feature == currentFeatureSelected.value))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(feature.name)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth())
                    {
                        TextButton(onClick = {
                            if (currentFeatureSelected.value.name != "Invalid")
                            {
                                sessionCount.intValue++
                                showAddSessionDialog.value = false
                                selectedFeature.value = currentFeatureSelected.value
                                isFeatureSelected.value = true
                            }
                            else
                            {
                                Toast.makeText(mainContext, "Please select a feature!", Toast.LENGTH_SHORT).show()
                            }
                        })
                        {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}