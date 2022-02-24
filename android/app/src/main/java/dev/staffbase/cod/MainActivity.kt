package dev.staffbase.cod

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.microsoft.windowsazure.messaging.notificationhubs.InstallationTemplate
import com.microsoft.windowsazure.messaging.notificationhubs.NotificationHub
import dev.staffbase.cod.ui.theme.CODHackathonTheme

class MainActivity : ComponentActivity() {

    private val connectionString = "Endpoint=sb://codhack.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=2FryYRvRN2H5KVDEBclbikEtoxjRj01ZqKtTC4lHeUY="
    private val hubName = "cod-hub-01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        NotificationHub.setListener(CustomNotificationListener())
        NotificationHub.start(application, hubName, connectionString)

        NotificationHub.addTag("userAgent:com.example.notification_hubs_test_app_refresh:0.1.0")

        val testTemplate = InstallationTemplate()
        testTemplate.body = "{\"data\":{\"message\":\"$(messageParam)\"}}"
        NotificationHub.setTemplate("testTemplate", testTemplate)

        setContent {
            CODHackathonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channelName"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CODHackathonTheme {
        Greeting("Android")
    }
}