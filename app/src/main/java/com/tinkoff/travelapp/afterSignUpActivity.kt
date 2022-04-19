package com.tinkoff.travelapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/*
    Не забывайте смотреть на то, что вам студия подсвечивает, т.к.
    иногда это могут быть потенциальные ошибки, но чаще, все-таки
    какие-то стилистические вопросы.
 */
class afterSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_sign_up)

        val buttonLetsGo = findViewById<Button>(R.id.after_sign_up_lets_go)
        buttonLetsGo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Не стоит запрещать пользователю возвращаться назад, т.к. это
    // привычное действие для них. При навигации на следующий экран
    // если вы не предполагаете, что пользователь не может вернуться назад,
    // то перед startActivity(...) лучше делать finish() у текущей.
    override fun onBackPressed() {
        return
    }
}
