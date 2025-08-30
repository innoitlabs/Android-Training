# Advanced Android Architecture Patterns

## Course Overview

This comprehensive course teaches advanced architectural patterns and software engineering best practices for Android development using Kotlin. The course covers Clean Architecture, modern Android libraries, and scalable code organization.

## Learning Objectives

By the end of this course, you will be able to:

- ✅ Implement advanced architectural patterns for Android apps
- ✅ Use modern Android libraries (Jetpack, Compose, Coroutines, Hilt)
- ✅ Create scalable and maintainable codebases
- ✅ Apply software engineering best practices for long-term projects
- ✅ Build modular applications with feature modules
- ✅ Implement dependency injection with Hilt
- ✅ Use Jetpack Compose for modern UI development

## Course Structure

### 📚 Documentation
- [Clean Architecture Principles](./docs/01-clean-architecture.md)
- [Repository Pattern & Data Layers](./docs/02-repository-pattern.md)
- [Use Case Pattern & Domain Logic](./docs/03-use-case-pattern.md)
- [Modular App Architecture](./docs/04-modular-architecture.md)
- [Feature Modules & Dynamic Delivery](./docs/05-feature-modules.md)
- [Jetpack Compose Basics](./docs/06-jetpack-compose.md)
- [Advanced Kotlin Features](./docs/07-advanced-kotlin.md)
- [Design Patterns in Android](./docs/08-design-patterns.md)
- [Code Organization & Package Structure](./docs/09-code-organization.md)
- [Documentation & Code Standards](./docs/10-documentation-standards.md)

### 🏗️ Project Examples
- Complete modular Android application in `ArchitecturePatterns/` folder
- Hands-on lab with User Directory App
- Feature modules implementation
- Dependency injection with Hilt

### 🧪 Exercises
- Easy: Implement new use cases
- Intermediate: Add feature modules
- Advanced: Refactor to Compose with MVVM

## Quick Start

1. **Clone the repository**
2. **Open in Android Studio**
3. **Build and run the project**
4. **Follow the documentation in order**
5. **Complete the hands-on exercises**

## Technology Stack

- **Kotlin 2.0.21+**
- **Android Gradle Plugin 8.12.2**
- **Jetpack Compose 1.5+**
- **Hilt 2.48+**
- **Retrofit 2.9+**
- **Coroutines & Flow**
- **Material Design 3**

## Project Structure

```
ArchitecturePatterns/
├── app/                    # Main application module
├── data/                   # Data layer (repositories, API, DB)
├── domain/                 # Domain layer (use cases, models)
├── presentation/           # Presentation layer (UI, ViewModels)
└── di/                     # Dependency injection
```

## Building and Running

```bash
# Navigate to project directory
cd ArchitecturePatterns

# Build the project
./gradlew build

# Run on connected device/emulator
./gradlew installDebug
```

## Contributing

This is a teaching material repository. Feel free to:
- Report issues
- Suggest improvements
- Submit pull requests

## License

This project is for educational purposes.
