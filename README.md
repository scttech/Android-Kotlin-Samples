# Kotlin Samples

A growing collection of small, self-contained Android apps written in Kotlin, used to
demonstrate individual concepts for beginning Android development. Each sample lives in
its own Gradle module so it can be built, run, and read independently of the others.

## Project structure

This is a single Gradle project with multiple Android application modules:

```
KotlinSamples/
├── settings.gradle.kts     # lists every sample module
├── build.gradle.kts        # shared plugin versions (applied per-module)
├── gradle/libs.versions.toml
└── hello-kotlin/            # one module per sample app
    └── src/main/...
```

Every module applies the `com.android.application` plugin, so each one builds to its own
installable APK with its own application ID. That matters for two reasons:

- Android Studio automatically creates a **Run/Debug Configuration** for every app module
  it finds when it syncs the project.
- Distinct application IDs mean students can have several sample apps installed on the
  same device or emulator at once without them overwriting each other.

## Running a sample

1. Clone the repository and open the **root folder** (`KotlinSamples/`) in Android Studio
   — not an individual sample folder.
2. Let Gradle sync finish (first sync will download dependencies).
3. In the toolbar, open the **Run/Debug configurations** dropdown next to the Run ▶ button.
4. Pick the sample you want (e.g. `hello-kotlin`) and press Run.

No manual configuration is required — Android Studio generates one configuration per
module automatically, so this list grows on its own as new samples are added.

## Samples

| Module | Demonstrates |
| --- | --- |
| [`hello-kotlin`](hello-kotlin) | Minimal Android app: a single `Activity` using Jetpack Compose to show "Hello, Kotlin!" |

## Adding a new sample

1. Copy an existing module folder (e.g. `hello-kotlin`) as a starting point, or use
   **File ▸ New ▸ New Module...** in Android Studio.
2. Give the new folder a short, kebab-case name, e.g. `calculator`.
3. Add it to `settings.gradle.kts`:
   ```kotlin
   include(":calculator")
   ```
4. In the new module's `build.gradle.kts`, set a unique `namespace` and `applicationId`
   following the existing convention:
   ```kotlin
   namespace = "com.scttech.android.kotlin.samples.calculator"
   applicationId = "com.scttech.android.kotlin.samples.calculator"
   ```
5. Sync Gradle. The new sample appears in the Run/Debug configurations dropdown and in
   the module tree — no other setup needed.
6. Add a row to the table above.

## Notes

- Shared dependency versions live in `gradle/libs.versions.toml` (the Gradle version
  catalog) and are referenced from every module's `build.gradle.kts` as `libs.xxx`, so
  bumping a library version only needs to happen in one place.
- This project relies on Android Gradle Plugin 9's built-in Kotlin support, so modules do
  **not** apply a separate `org.jetbrains.kotlin.android` plugin.
- `minSdk` is currently set to 36 (Android Studio's default for a brand-new project).
  That's quite high for a teaching repo — it excludes any device or emulator older than
  the newest Android release. Consider lowering it (e.g. to 24 or 26) in each module once
  you know which devices/emulators students will actually use.
