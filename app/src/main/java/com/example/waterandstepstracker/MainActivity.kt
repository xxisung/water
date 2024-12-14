package com.example.waterandstepstracker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.waterandstepstracker.ui.theme.WaterAndStepsTrackerTheme
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaterAndStepsTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 메인 화면 호출
                    WaterAndStepsScreen(this)
                }
            }
        }
    }
}

@Composable
fun WaterAndStepsScreen(context: Context) {
    // SharedPreferences 선언
    val sharedPreferences = context.getSharedPreferences("tracker_data", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // 저장된 데이터 불러오기
    var waterCount by remember { mutableStateOf(sharedPreferences.getInt("waterCount", 0)) }
    var stepCount by remember { mutableStateOf(sharedPreferences.getInt("stepCount", 0)) }

    // 현재 주와 전주 데이터 비교를 위한 필드
    val currentWeek = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())

    val lastWeekWater = sharedPreferences.getInt("lastWeekWater", 0)
    val lastWeekSteps = sharedPreferences.getInt("lastWeekSteps", 0)
    val savedWeek = sharedPreferences.getInt("savedWeek", currentWeek)

    // 주간 데이터 저장 및 비교
    if (currentWeek != savedWeek) {
        // 새 주 시작 시 데이터 리셋 및 지난 주 저장
        editor.putInt("lastWeekWater", waterCount)
        editor.putInt("lastWeekSteps", stepCount)
        editor.putInt("waterCount", 0)
        editor.putInt("stepCount", 0)
        editor.putInt("savedWeek", currentWeek)
        editor.apply()

        waterCount = 0
        stepCount = 0
    }

    // UI 화면 구성
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 물 마시기 버튼
        Button(onClick = {
            waterCount++
            editor.putInt("waterCount", waterCount).apply()
        }) {
            Text(text = "물 마시기 추가")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "오늘 마신 물: $waterCount 컵",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 계단 오르기 버튼
        Button(onClick = {
            stepCount++
            editor.putInt("stepCount", stepCount).apply()
        }) {
            Text(text = "계단 오르기 추가")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "오늘 오른 층수: $stepCount 층",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 주간 리포트 메시지
        Text(
            text = "지난주와 비교: ",
            fontSize = 18.sp
        )
        Text(
            text = "지난주 물 마신 수: $lastWeekWater 컵\n이번주 물 마신 수: $waterCount 컵",
            fontSize = 16.sp
        )
        Text(
            text = "지난주 오른 층수: $lastWeekSteps 층\n이번주 오른 층수: $stepCount 층",
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWaterAndStepsScreen() {
    WaterAndStepsTrackerTheme {
        WaterAndStepsScreen(context = androidx.compose.ui.platform.LocalContext.current)
    }
}
