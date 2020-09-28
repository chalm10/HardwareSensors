package com.example.hardwaresensors

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt
import kotlin.random.Random

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    lateinit var proxySensor: Sensor
    lateinit var sensorEventListener: SensorEventListener
    lateinit var sensorManager: SensorManager
    lateinit var accelSensor: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val colors = arrayListOf(Color.RED,Color.BLACK,Color.BLUE,Color.CYAN,Color.DKGRAY,Color.MAGENTA)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        val sm = getSystemService<SensorManager>()
//        val sensorsList = sm.getSensorList(Sensor.TYPE_ALL)
//
//        sensorsList.forEach {
//            Log.d(TAG , """
//                ${it.name}
//                ${it.stringType}
//                ${it.vendor}
//            """.trimIndent())
//
//        }
        proxySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorEventListener = object : SensorEventListener{
            override fun onSensorChanged(event: SensorEvent?) {

                if (event!!.sensor.type == Sensor.TYPE_PROXIMITY){
                    Log.d(TAG , "onSensorChanged : ${event.values[0]}")
                    if (event.values[0]>0){
                        proximitySensorFl.setBackgroundColor(colors[Random.nextInt(6)])
                    }
                }

                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER){

                    val bgColor = Color.rgb(
                        accel2color(event.values[0]),
                        accel2color(event.values[1]),
                        accel2color(event.values[2])
                    )
                    accelerometerSensorFl.setBackgroundColor(bgColor)
//
//                    Log.d(TAG , """
//                    ax = ${event.values[0]}
//                    ay = ${event.values[1]}
//                    az = ${event.values[2]}
//                """.trimIndent())
                }

            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        }



    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            sensorEventListener , proxySensor ,1000*1000
        )
        sensorManager.registerListener(
            sensorEventListener , accelSensor ,1000*2000
        )

    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }

    private fun accel2color(event: Float): Int {
        return (((event+15)/27)*255).roundToInt()
    }


}