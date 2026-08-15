package com.scttech.android.kotlin.samples.loginform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Hardcoded credentials this sample validates against. A real app would call an
 * authentication API instead of comparing against a constant.
 */
private const val VALID_USERNAME = "admin"
private const val VALID_PASSWORD = "password"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginScreen()
                }
            }
        }
    }
}

/**
 * Result of validating and submitting the login form, used to drive the Snackbar message.
 */
private sealed interface LoginResult {
    data class Success(val username: String) : LoginResult
    data object InvalidCredentials : LoginResult
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val usernameRequiredMessage = stringResource(R.string.error_username_required)
    val passwordRequiredMessage = stringResource(R.string.error_password_required)
    val invalidCredentialsMessage = stringResource(R.string.error_invalid_credentials)
    val successMessage = stringResource(R.string.login_success, username)

    fun submit() {
        val trimmedUsername = username.trim()
        usernameError = if (trimmedUsername.isEmpty()) usernameRequiredMessage else null
        passwordError = if (password.isEmpty()) passwordRequiredMessage else null

        if (usernameError != null || passwordError != null) return

        focusManager.clearFocus()
        val result = if (trimmedUsername == VALID_USERNAME && password == VALID_PASSWORD) {
            LoginResult.Success(trimmedUsername)
        } else {
            LoginResult.InvalidCredentials
        }

        val message = when (result) {
            is LoginResult.Success -> successMessage
            LoginResult.InvalidCredentials -> {
                passwordError = invalidCredentialsMessage
                invalidCredentialsMessage
            }
        }
        coroutineScope.launch { snackbarHostState.showSnackbar(message) }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(snackbarData = data)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    if (usernameError != null) usernameError = null
                },
                label = { Text(stringResource(R.string.username_label)) },
                singleLine = true,
                isError = usernameError != null,
                supportingText = {
                    usernameError?.let { Text(it) }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequester.requestFocus() }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 360.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (passwordError != null) passwordError = null
                },
                label = { Text(stringResource(R.string.password_label)) },
                singleLine = true,
                isError = passwordError != null,
                supportingText = {
                    passwordError?.let { Text(it) }
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(
                            stringResource(
                                if (passwordVisible) R.string.hide_password else R.string.show_password
                            )
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { submit() }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 360.dp)
                    .focusRequester(passwordFocusRequester)
                    .padding(top = 8.dp)
            )

            Button(
                onClick = { submit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 360.dp)
                    .padding(top = 24.dp)
            ) {
                Text(stringResource(R.string.login_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}
