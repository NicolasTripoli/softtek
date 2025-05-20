package br.com.fiap.softtek.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        color = Color.Red,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewErrorMessage() {
    ErrorMessage(message = "This is an example error message.")
}