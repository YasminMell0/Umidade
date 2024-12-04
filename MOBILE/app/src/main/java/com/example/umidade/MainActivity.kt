package com.example.umidade

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.umidade.ui.theme.UmidadeTheme
import com.google.firebase.firestore.FirebaseFirestore

data class SensorData(
    val dateTime: String = "",
    val temperature: Double = 0.0,
    val humidity: Double = 0.0
)

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UmidadeTheme {
                var sensorDataList by remember { mutableStateOf(listOf<SensorData>()) }

                // Fetch data from Firestore
                LaunchedEffect(Unit) {
                    val db = FirebaseFirestore.getInstance()
                    db.collection("sensorData")
                        .get()
                        .addOnSuccessListener { result ->
                            val data = result.map { doc ->
                                doc.toObject(SensorData::class.java)
                            }
                            sensorDataList = data
                        }
                        .addOnFailureListener {
                            sensorDataList = emptyList() // Handle errors as needed
                        }
                }

                // Display the data in a list
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(sensorDataList) { data ->
                            SensorDataItem(data)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SensorDataItem(data: SensorData) {
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "DateTime: ${data.dateTime}")
                Text(text = "Temperature: ${data.temperature} °C")
                Text(text = "Humidity: ${data.humidity} %")
            }
        }
    }
}