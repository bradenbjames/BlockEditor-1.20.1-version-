# Building and running BlockEditor (Windows)

This document explains how to build the BlockEditor project on Windows (cmd.exe or PowerShell). It also records the exact commands I used when building the project in this workspace so both you and I can reference them.

Prerequisites
- Java 17+ (Java 21 was used successfully when I built). Ensure `java` is on your PATH or set `JAVA_HOME` to a JDK installation.
- A terminal (cmd.exe recommended for the commands below). PowerShell works too (see notes).

Quick build (recommended)
- The repository contains a bundled Gradle distribution you can use directly. From the project root open `cmd.exe` and run:

```bat
gradle-dist\gradle-8.8\bin\gradle.bat build -x test --no-daemon
```

- This will compile the project and produce the mod JAR in `build/libs/` on success.

Using the Gradle wrapper
- If the wrapper is functional, you can use the wrapper script from the project root (Windows cmd.exe):

```bat
.\gradlew.bat build -x test --no-daemon
```

- In PowerShell run it with `.\
elpath` as well:

```powershell
.\gradlew.bat build -x test --no-daemon
```

Note: In this workspace I fixed `gradlew.bat` (removed stray characters) so it correctly invokes the wrapper JAR, but the local `gradle\wrapper\gradle-wrapper.jar` in this repo was invalid (manifest issue). If you get an error like "no main manifest attribute" or "Could not find the Gradle wrapper jar", follow the troubleshooting steps below.

Troubleshooting
- "Could not find the Gradle wrapper jar at <path>" — make sure `gradle\wrapper\gradle-wrapper.jar` exists. If it doesn't, regenerate the wrapper (see next bullet) or copy a valid wrapper JAR into that path.
- "no main manifest attribute" in the wrapper jar — the wrapper JAR is corrupted or not a proper wrapper. Fix by regenerating the wrapper (see below) or use the bundled Gradle (recommended).
- Java not found / wrong version — either add Java to PATH or set `JAVA_HOME` (cmd example):

```bat
setx JAVA_HOME "C:\Program Files\Java\jdk-21" /M
```

Regenerating the wrapper (optional)
- If you have a globally installed Gradle, you can regenerate the wrapper so `./gradlew` works for everyone. From the project root run (requires Gradle on PATH):

```bat
gradle wrapper --gradle-version 8.8
```

- Alternatively, download a proper `gradle-wrapper.jar` from a trusted Gradle distribution or copy it from another project.

Running available Gradle tasks (inspect first)
- To list available tasks:

```bat
gradle-dist\gradle-8.8\bin\gradle.bat tasks --all
```

- Some Forge/Minecraft mod setups provide tasks like `runClient` or `runServer`. If present, you can run the client with (example):

```bat
gradle-dist\gradle-8.8\bin\gradle.bat runClient
```

(Only run these if the task exists in `gradle tasks --all`.)

What I did and exact commands I ran (for reference)
- I fixed `gradlew.bat` to remove stray characters so it properly invokes the wrapper jar.
- I attempted to run the wrapper and saw the wrapper JAR reported as invalid ("no main manifest attribute").
- I then used the bundled Gradle distribution to build. Exact command I executed:

```bat
cmd /c "gradle-dist\gradle-8.8\bin\gradle.bat" build -x test --no-daemon
```

- Build output summary I observed:
  - Java: 21.0.8
  - BUILD SUCCESSFUL in 8s
  - 7 actionable tasks: 4 executed, 3 up-to-date

Notes and recommendations
- For reliability, use the bundled Gradle distribution (as above) if the wrapper JAR is invalid.
- If you want to restore a fully functioning `./gradlew` wrapper for contributors, regenerate the wrapper with a proper Gradle installation, then commit the generated wrapper files (including `gradle/wrapper/gradle-wrapper.jar`).
- If you'd like, I can:
  - Regenerate and add a valid `gradle/wrapper/gradle-wrapper.jar` to the repo (I can do that here if you want me to create it), or
  - Revert the `gradlew.bat` change if you prefer to keep the original file intact.

If you want me to update this document (for example add instructions to run the mod in a dev environment, or add CI instructions), tell me what you prefer and I will update `BUILD.md` accordingly.
