# 4. Android Virtual Device (AVD) Setup

## 📱 What is an Android Virtual Device?

An Android Virtual Device (AVD) is a software emulation of a physical Android device that runs on your computer. It allows you to test your Android applications without needing a physical device.

### Benefits of Using AVDs
- **Cost-effective**: No need to purchase multiple physical devices
- **Flexibility**: Test on different Android versions and screen sizes
- **Consistency**: Same environment across different development machines
- **Debugging**: Easy to debug and test features
- **Performance**: Can be faster than physical devices for development

## 🚀 Setting Up AVD Manager

### Accessing AVD Manager
1. **From Android Studio**: Tools → AVD Manager
2. **From Welcome Screen**: Configure → AVD Manager
3. **Keyboard Shortcut**: 
   - **Windows/Linux**: `Ctrl + Shift + A` → "AVD Manager"
   - **macOS**: `Cmd + Shift + A` → "AVD Manager"

### AVD Manager Interface
```
AVD Manager
├── Virtual Devices Tab    # List of created AVDs
├── Device Definitions Tab # Available device profiles
└── Actions
    ├── Create Virtual Device
    ├── Edit
    ├── Delete
    └── Launch
```

## 📋 Creating Your First AVD

### Step 1: Choose Device Definition
Select a device profile that matches your target audience:

#### Recommended Device Profiles
- **Pixel 6** (1080 x 2400, 6.4") - Modern flagship
- **Pixel 4** (1080 x 2280, 5.7") - Standard size
- **Pixel 2** (1080 x 1920, 5.0") - Compact size
- **Nexus 7** (1200 x 1920, 7.0") - Tablet

#### Device Categories
- **Phone**: Standard smartphone sizes
- **Tablet**: Larger screen devices
- **TV**: Android TV devices
- **Wear**: Android Wear devices

### Step 2: Select System Image
Choose the Android version and architecture:

#### Recommended System Images
- **API 34** (Android 14.0) - Latest stable
- **API 33** (Android 13.0) - Widely supported
- **API 31** (Android 12.0) - Good compatibility
- **API 30** (Android 11.0) - Older but stable

#### System Image Types
- **Google APIs**: Includes Google Play Services
- **Google Play**: Includes Google Play Store
- **Vanilla Android**: Pure Android without Google services

### Step 3: Configure AVD
Set advanced options for your virtual device:

#### Basic Configuration
```yaml
AVD Name: Pixel_6_API_34
Device: Pixel 6
System Image: API 34 (Android 14.0)
RAM: 2048 MB
VM Heap: 256 MB
Internal Storage: 2048 MB
SD Card: 512 MB
```

#### Advanced Configuration
```yaml
Graphics: Hardware - GLES 2.0
Boot Option: Cold Boot
Multi-Core CPU: 4 cores
Camera Back: VirtualScene
Camera Front: VirtualScene
```

## ⚙️ AVD Configuration Options

### Hardware Profile Settings
```yaml
# Display
Screen Size: 6.4 inches
Resolution: 1080 x 2400
Density: 420 dpi
Orientation: Portrait

# Memory
RAM: 2048 MB
VM Heap: 256 MB
Internal Storage: 2048 MB
SD Card: 512 MB

# CPU/ABI
CPU: x86_64
ABI: x86_64
```

### Advanced Settings
```yaml
# Graphics
Graphics: Hardware - GLES 2.0
GPU Emulation: Enabled
Hardware Accelerated Rendering: Enabled

# Boot Options
Boot Option: Cold Boot
Multi-Core CPU: 4 cores
Snapshot: Disabled

# Camera
Back Camera: VirtualScene
Front Camera: VirtualScene

# Sensors
Accelerometer: Enabled
Gyroscope: Enabled
GPS: Enabled
```

## 🎮 Launching and Using AVDs

### Starting an AVD
1. **From AVD Manager**: Click the play button (▶️)
2. **From Android Studio**: Run → Run 'app'
3. **Command Line**: `emulator -avd Pixel_6_API_34`

### AVD Startup Process
1. **Booting**: Android system boots up
2. **Initialization**: System services start
3. **Home Screen**: Android launcher appears
4. **Ready**: Device is ready for app installation

### First Boot Configuration
- **Language Selection**: Choose your preferred language
- **Wi-Fi Setup**: Configure network connection
- **Google Account**: Sign in to Google services
- **Security Setup**: Set up screen lock (optional)

## 🔧 AVD Performance Optimization

### Hardware Acceleration
Enable hardware acceleration for better performance:

#### Windows
```bash
# Enable Hyper-V
dism.exe /Online /Enable-Feature /FeatureName:Microsoft-Hyper-V /All

# Or enable Intel HAXM
# Download from Intel Developer Zone
```

#### macOS
```bash
# Enable Hypervisor.framework
# Automatically enabled on macOS 10.10+

# Install Intel HAXM
# Download from Intel Developer Zone
```

#### Linux
```bash
# Install KVM
sudo apt-get install qemu-kvm libvirt-daemon-system libvirt-clients bridge-utils

# Add user to kvm group
sudo usermod -aG kvm $USER
```

### Performance Settings
```yaml
# Memory Allocation
RAM: 4096 MB (for better performance)
VM Heap: 512 MB

# Graphics
Graphics: Hardware - GLES 2.0
GPU Emulation: Enabled

# CPU
Multi-Core CPU: 4 cores
```

## 📱 AVD Management

### Managing Multiple AVDs
```yaml
# Development AVDs
Pixel_6_API_34_Dev: API 34, 4GB RAM, Debug enabled
Pixel_4_API_33_Test: API 33, 2GB RAM, Standard config

# Testing AVDs
Nexus_7_API_31_Tablet: API 31, Tablet, Large screen
Pixel_2_API_30_Old: API 30, Older Android version
```

### AVD Snapshots
```yaml
# Create Snapshot
- Boot AVD to desired state
- Tools → AVD Manager → Edit → Snapshot
- Save current state

# Use Snapshot
- Quick boot from saved state
- Faster startup times
- Consistent testing environment
```

## 🛠️ Troubleshooting AVD Issues

### Common Problems

#### AVD Won't Start
```bash
# Solution 1: Check hardware acceleration
# Ensure virtualization is enabled in BIOS

# Solution 2: Increase memory allocation
# Edit AVD → Advanced Settings → Memory

# Solution 3: Use software rendering
# Graphics: Software - GLES 2.0
```

#### Slow Performance
```bash
# Solution 1: Enable hardware acceleration
# Graphics: Hardware - GLES 2.0

# Solution 2: Increase RAM allocation
# RAM: 4096 MB or higher

# Solution 3: Close other applications
# Free up system resources
```

#### App Crashes
```bash
# Solution 1: Clear app data
# Settings → Apps → Your App → Clear Data

# Solution 2: Wipe AVD data
# AVD Manager → Edit → Wipe Data

# Solution 3: Create new AVD
# Different system image or device profile
```

### Performance Monitoring
```bash
# Check AVD performance
adb shell top

# Monitor memory usage
adb shell dumpsys meminfo

# Check CPU usage
adb shell dumpsys cpuinfo
```

## 📊 AVD Best Practices

### Development Workflow
1. **Create Multiple AVDs**: Different Android versions and screen sizes
2. **Use Snapshots**: Save time with quick boot states
3. **Regular Updates**: Keep system images updated
4. **Performance Monitoring**: Monitor AVD performance

### Testing Strategy
```yaml
# Primary Development AVD
Device: Pixel 6
API: 34 (Latest)
Purpose: Main development and testing

# Compatibility Testing AVDs
Device: Pixel 4, API 33
Device: Pixel 2, API 30
Device: Nexus 7, API 31 (Tablet)

# Performance Testing AVD
Device: Pixel 6
API: 34
RAM: 8GB
Purpose: Performance testing
```

### Resource Management
```yaml
# Memory Allocation
Development: 4-8GB RAM
Testing: 2-4GB RAM
CI/CD: 2GB RAM

# Storage Allocation
Internal Storage: 4-8GB
SD Card: 1-2GB
```

## 🔍 AVD vs Physical Device

### AVD Advantages
- **Cost**: Free to create multiple devices
- **Flexibility**: Easy to test different configurations
- **Consistency**: Same environment across machines
- **Debugging**: Better debugging tools

### Physical Device Advantages
- **Performance**: Real device performance
- **Sensors**: Actual hardware sensors
- **User Experience**: Real user interaction
- **Hardware Features**: Camera, GPS, etc.

### When to Use Each
```yaml
# Use AVD for:
- Initial development
- Basic functionality testing
- Different Android versions
- Screen size testing

# Use Physical Device for:
- Performance testing
- Sensor testing
- Camera functionality
- Final testing before release
```

## 📚 Additional Resources

### Official Documentation
- [AVD Manager](https://developer.android.com/studio/run/managing-avds)
- [System Images](https://developer.android.com/studio/run/managing-avds#system-image)
- [Hardware Acceleration](https://developer.android.com/studio/run/emulator-acceleration)

### Performance Optimization
- [Intel HAXM](https://github.com/intel/haxm)
- [KVM on Linux](https://www.linux-kvm.org/page/Main_Page)
- [Hyper-V on Windows](https://docs.microsoft.com/en-us/virtualization/hyper-v-on-windows/)

---

**Next**: [Git Integration and Version Control](./05-git-integration.md)
