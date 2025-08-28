# 1. Android Studio Installation and Configuration

## 📋 Prerequisites
- **Operating System**: Windows 10/11, macOS 10.14+, or Linux
- **RAM**: Minimum 8GB (16GB recommended)
- **Storage**: At least 8GB free space
- **Java**: Android Studio includes OpenJDK 11

## 🚀 Installation Steps

### Step 1: Download Android Studio
1. Visit the official Android Developer website: [developer.android.com/studio](https://developer.android.com/studio)
2. Click "Download Android Studio"
3. Choose the appropriate version for your operating system
4. Accept the terms and conditions

### Step 2: Install Android Studio

#### Windows
```bash
# Run the downloaded .exe file
# Follow the installation wizard
# Recommended settings:
# - Install for all users
# - Custom installation location (avoid spaces in path)
# - Create desktop shortcut
```

#### macOS
```bash
# Mount the downloaded .dmg file
# Drag Android Studio to Applications folder
# First launch will require administrator privileges
```

#### Linux
```bash
# Extract the downloaded .tar.gz file
tar -xzf android-studio-*.tar.gz
# Move to /opt directory
sudo mv android-studio /opt/
# Create desktop shortcut
```

### Step 3: First Launch Setup

#### Welcome Screen
1. **Import Settings**: Choose "Do not import settings" for first-time users
2. **Setup Wizard**: Click "Next" to begin configuration

#### Installation Type
- **Standard**: Recommended for beginners
- **Custom**: Advanced users can customize components

#### UI Theme
- **Light**: Default theme
- **Dark**: Easier on eyes, popular among developers

#### Component Selection
Ensure these components are selected:
- ✅ Android SDK
- ✅ Android Virtual Device
- ✅ Performance (Intel HAXM)

## ⚙️ SDK Manager Configuration

### Accessing SDK Manager
- **Windows/Linux**: File → Settings → Appearance & Behavior → System Settings → Android SDK
- **macOS**: Android Studio → Preferences → Appearance & Behavior → System Settings → Android SDK

### Essential SDK Components

#### SDK Platforms
Install these Android versions:
- **Android 14.0 (API 34)** - Latest stable
- **Android 13.0 (API 33)** - Widely supported
- **Android 12.0 (API 31)** - Good compatibility

#### SDK Tools
- ✅ Android SDK Build-Tools
- ✅ Android SDK Command-line Tools
- ✅ Android Emulator
- ✅ Android SDK Platform-Tools
- ✅ Intel x86 Emulator Accelerator (HAXM)

### SDK Installation Location
**Recommended paths:**
- **Windows**: `C:\Users\[Username]\AppData\Local\Android\Sdk`
- **macOS**: `/Users/[Username]/Library/Android/sdk`
- **Linux**: `/home/[Username]/Android/Sdk`

## 🎨 IDE Configuration

### Appearance Settings
1. **Theme**: Choose between Light and Dark themes
2. **Font Size**: Adjust for comfortable reading
3. **Line Numbers**: Enable for better code navigation

### Editor Settings
1. **Code Style**: Set to Kotlin standards
2. **Auto Import**: Enable automatic imports
3. **Code Completion**: Configure intelligent suggestions

### Performance Optimization
1. **Memory Settings**: Increase heap size for better performance
2. **Gradle Settings**: Configure offline mode for faster builds
3. **Indexing**: Exclude unnecessary folders from indexing

## 🔌 Essential Plugins

### Built-in Plugins (Enable these)
- ✅ Kotlin
- ✅ Android
- ✅ Gradle
- ✅ Git Integration
- ✅ Layout Inspector
- ✅ APK Analyzer

### Recommended Third-party Plugins
1. **Rainbow Brackets**: Color-coded brackets
2. **Key Promoter X**: Keyboard shortcut hints
3. **String Manipulation**: Text processing tools
4. **Material Theme UI**: Enhanced UI themes

## 🖥️ Main IDE Windows

### Project Window
- **Location**: Left side of IDE
- **Purpose**: Navigate project files and folders
- **Views**: Project, Android, Packages, Project Files

### Code Editor
- **Location**: Center of IDE
- **Features**: Syntax highlighting, code completion, error detection
- **Tabs**: Multiple files can be open simultaneously

### Logcat
- **Location**: Bottom of IDE
- **Purpose**: View application logs and debugging information
- **Filters**: Verbose, Debug, Info, Warn, Error

### Gradle Console
- **Location**: Bottom of IDE
- **Purpose**: Monitor build processes and dependency resolution
- **Features**: Real-time build status and error reporting

## 🔧 Environment Variables

### Windows
```batch
# Add to System Environment Variables
ANDROID_HOME=C:\Users\[Username]\AppData\Local\Android\Sdk
ANDROID_SDK_ROOT=C:\Users\[Username]\AppData\Local\Android\Sdk
PATH=%PATH%;%ANDROID_HOME%\platform-tools
PATH=%PATH%;%ANDROID_HOME%\tools
```

### macOS/Linux
```bash
# Add to ~/.bash_profile or ~/.zshrc
export ANDROID_HOME=$HOME/Library/Android/sdk
export ANDROID_SDK_ROOT=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/tools
```

## ✅ Verification Steps

### Test Installation
1. **Create New Project**: File → New → New Project
2. **Select Template**: Choose "Empty Activity"
3. **Configure Project**:
   - Name: TestProject
   - Package name: com.example.testproject
   - Language: Kotlin
   - Minimum SDK: API 24 (Android 7.0)
4. **Build Project**: Build → Make Project
5. **Run Project**: Run → Run 'app'

### Expected Results
- ✅ Project creates successfully
- ✅ Gradle sync completes without errors
- ✅ Build process finishes successfully
- ✅ "Hello World" appears in emulator

## 🚨 Troubleshooting

### Common Issues

#### Gradle Sync Failed
```bash
# Solution 1: Invalidate Caches
File → Invalidate Caches and Restart

# Solution 2: Update Gradle Version
File → Settings → Build, Execution, Deployment → Gradle
```

#### SDK Not Found
```bash
# Solution: Set SDK Path
File → Settings → Appearance & Behavior → System Settings → Android SDK
```

#### Emulator Won't Start
```bash
# Solution 1: Enable Virtualization
# Check BIOS settings for Intel VT-x or AMD-V

# Solution 2: Install HAXM
# SDK Manager → SDK Tools → Intel x86 Emulator Accelerator
```

#### Slow Performance
```bash
# Solution 1: Increase Memory
# Help → Edit Custom VM Options
-Xmx2048m

# Solution 2: Disable Unused Plugins
# File → Settings → Plugins
```

## 📚 Additional Resources

### Official Documentation
- [Android Studio User Guide](https://developer.android.com/studio/intro)
- [SDK Manager Guide](https://developer.android.com/studio/manage/sdk)
- [Performance Optimization](https://developer.android.com/studio/intro/studio-config)

### Community Resources
- [Stack Overflow Android Studio Tag](https://stackoverflow.com/questions/tagged/android-studio)
- [Reddit r/androiddev](https://www.reddit.com/r/androiddev/)
- [Android Developers Blog](https://android-developers.googleblog.com/)

---

**Next**: [Project Structure](./02-project-structure.md)
