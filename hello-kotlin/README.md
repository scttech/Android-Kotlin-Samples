# Hello Kotlin

The simplest possible starting point: one screen that shows the text **"Hello, Kotlin!"**
centered on a plain background. No user interaction, no navigation — just enough to see
how a Kotlin/Compose Android app is wired together end to end.

## What it demonstrates

This sample uses [Jetpack Compose](https://developer.android.com/develop/ui/compose),
Android's modern UI toolkit. Instead of XML layout files, the UI is built directly in
Kotlin using functions annotated `@Composable`. Everything lives in one file:
[`MainActivity.kt`](src/main/java/com/scttech/android/kotlin/samples/hellokotlin/MainActivity.kt).

| Concept | Where | What it does |
| --- | --- | --- |
| `ComponentActivity` | class declaration | Base class for an activity that hosts Compose content (instead of the older `AppCompatActivity` + XML approach). |
| `onCreate` | `MainActivity.onCreate` | Lifecycle callback the system calls once when the screen is first created — the standard place to set up the UI. |
| `enableEdgeToEdge()` | `onCreate` | Lets the app's content draw behind the status/navigation bars for a modern, full-screen look. |
| `setContent { }` | `onCreate` | The bridge between the `Activity` world and the Compose world — everything inside this block is Compose UI. |
| `@Composable` function | `HelloKotlin()` | A function that describes a piece of UI. Compose calls it (and re-calls it when data changes) to draw the screen — you never manually update views. |
| `MaterialTheme` | wraps the content | Supplies Material Design 3 colors, typography, and shapes to everything nested inside it, via `MaterialTheme.colorScheme` / `.typography`. |
| `Surface` | wraps `HelloKotlin()` | A themed background container — paints the theme's background color behind its content. |
| `Box` + `Modifier` + `Alignment` | `HelloKotlin()` | `Box` stacks/positions children; `Modifier.fillMaxSize()` makes it fill the screen; `contentAlignment = Alignment.Center` centers whatever is inside it. |
| `Text` | `HelloKotlin()` | Displays a string, styled here with `MaterialTheme.typography.headlineMedium`. |
| `@Preview` | `HelloKotlinPreview()` | Lets Android Studio render a composable in the **Design** tab without building and running the whole app — useful while iterating on UI. |

## Other files worth a look

- [`AndroidManifest.xml`](src/main/AndroidManifest.xml) — declares `MainActivity` as the
  app's launcher activity (the `MAIN`/`LAUNCHER` intent filter is what makes it show up
  as an icon and be the screen that opens first).
- [`res/values/themes.xml`](src/main/res/values/themes.xml) — defines `Theme.KotlinSamples`,
  the XML theme referenced from the manifest. Compose UI is styled separately via
  `MaterialTheme` in Kotlin, but the manifest still needs a base XML theme.

## Code

A breakdown of the interesting pieces of the code

### HelloKotlin

We define the function with `fun`

Using Jetpack Compose we annotate the `HelloKotlin` function with `@Composable` 
designating the function as a building block for the UI.

`Box` is a layout mechanism for the UI.  We've told it with `Modifier.fillMaxSize()` to
take up the whole screen.  Then we align the contents with `contentAlignment`

We then use `Text` to add a string of text within the `Box` layout.

```kotlin
@Composable   
fun HelloKotlin() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Hello, Kotlin!", style = MaterialTheme.typography.headlineMedium)
    }
}
```

## Try it yourself

- Change the string in `Text(text = "Hello, Kotlin!")` and re-run.
- Change its color: `Text(text = "...", color = MaterialTheme.colorScheme.primary)`.
- Add a second composable (e.g. a `Button`) and arrange both inside a `Column` instead of
  a `Box`.
