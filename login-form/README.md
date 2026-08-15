# Login Form

A single-screen login form with **username** and **password** fields, hardcoded
credentials to validate against, and inline/Snackbar feedback for both the success and
error paths. Builds on [`hello-kotlin`](../hello-kotlin) by adding user input, local
state, form validation, and Material Design 3 UX patterns.

The default login credentials for the app are:

* Username is `admin`
* Password is `password`

## What it demonstrates

This sample uses [Jetpack Compose](https://developer.android.com/develop/ui/compose) to
build an interactive form. Everything lives in one file:
[`MainActivity.kt`](src/main/java/com/scttech/android/kotlin/samples/loginform/MainActivity.kt).

| Concept | Where | What it does |
| --- | --- | --- |
| `rememberSaveable` | `username`, `password`, `passwordVisible` | Holds UI state that survives recomposition **and** configuration changes (e.g. rotation), so the user doesn't lose what they typed. |
| `remember` | `usernameError`, `passwordError` | Holds transient state that only needs to survive recomposition, not configuration changes — validation messages are cheap to recompute. |
| `OutlinedTextField` | `LoginScreen()` | Material 3 text input. Used here for both the username and password fields. |
| `isError` + `supportingText` | both text fields | The Material-recommended way to show a field-level validation error: the field's outline turns red and a message appears directly beneath it. |
| `visualTransformation` | password field | `PasswordVisualTransformation()` masks the input; swapping it for `VisualTransformation.None` is what powers the show/hide toggle. |
| `KeyboardOptions` / `KeyboardType` | both text fields | Configures the on-screen keyboard per field — `KeyboardType.Password` and disabled autocorrect for the password field, `ImeAction.Next` / `ImeAction.Done` to drive the IME action button. |
| `FocusRequester` | `passwordFocusRequester` | Lets pressing **Next** on the username field's keyboard action move focus straight to the password field. |
| `LocalFocusManager` | `submit()` | Dismisses the keyboard (`clearFocus()`) once the form is submitted. |
| `Scaffold` + `SnackbarHost` | `LoginScreen()` | Standard Compose layout for hosting transient, non-blocking feedback — used here for both the "login successful" and "invalid credentials" messages. |
| `rememberCoroutineScope` | `LoginScreen()` | `SnackbarHostState.showSnackbar` is a suspend function; this gives the click handler a scope to launch it from. |
| `stringResource` | throughout | All user-facing text lives in [`strings.xml`](src/main/res/values/strings.xml) rather than being hardcoded in Kotlin, so it can be localized. |

## UX/UI choices worth calling out

- **Validation runs on submit, not on every keystroke** — errors clear as soon as the
  user edits a field again, so the form never nags them before they've finished typing.
- **Each field is validated independently.** Leaving both blank shows both errors at
  once instead of forcing the user through them one at a time.
- **The password error is repurposed for "invalid credentials"**, since a wrong
  password can't be attributed to the username or password field specifically without
  leaking which one was wrong — a small security-conscious UX choice.
- **Feedback uses a `Snackbar`, not a blocking dialog**, so it doesn't interrupt the
  user or require a extra tap to dismiss.
- **The show/hide password toggle** lets users verify what they typed instead of
  guessing, which reduces failed login attempts caused by typos.
- **`ImeAction.Next` / `ImeAction.Done`** let the whole form be completed from the
  keyboard alone, with no need to tap into each field manually.

## Try it yourself

- Change `VALID_USERNAME` / `VALID_PASSWORD` in `MainActivity.kt` and re-run.
- Add a "remember me" `Checkbox` below the password field.
- Replace the hardcoded check in `submit()` with a call to a fake suspend function that
  simulates network latency, and show a loading state on the button while it runs.
