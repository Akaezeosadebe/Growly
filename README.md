# 🌱 Growly - Minimalist Productivity Companion

A beautiful, minimalist Android app designed to help you grow through mindful productivity. Built with Jetpack Compose and featuring a calming pastel color palette.

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Platform](https://img.shields.io/badge/platform-Android-green)
![Language](https://img.shields.io/badge/language-Kotlin-blue)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-orange)

## ✨ Features

### 📝 **Smart Journaling** ✅ *Fully Implemented*
- **Five Curated Categories**: Daily Reflections, Gratitude Journaling, Goal Tracking, Growth, and Custom Prompts
- **Seamless Navigation**: Tap any category to open the writing interface
- **Distraction-Free Writing**: Clean, focused writing environment with real-time word count
- **Auto-Save**: Automatic saving every 2 seconds with visual feedback
- **Category-Based Organization**: Each entry is properly categorized and timestamped

### ✅ **Task Management** ✅ *Fully Implemented*
- **Intuitive Task Creation**: Quick add with title and description via floating action button
- **Real-Time Updates**: Tasks appear instantly and persist during session
- **Priority System**: Visual priority indicators with color coding (Low, Medium, High, Urgent)
- **Completion Tracking**: Tap checkbox to complete/uncomplete with smooth animations
- **Task Deletion**: Swipe or tap delete button to remove tasks

### 🍅 **Focus Timer (Pomodoro)** ✅ *Fully Implemented*
- **25-Minute Work Sessions**: Classic Pomodoro technique with real-time countdown
- **Visual Progress**: Circular progress bar that fills as time progresses
- **Session Controls**: Start, pause, and reset functionality
- **Productivity Tips**: Built-in focus enhancement suggestions and techniques
- **Timer Display**: Large, clear countdown display (25:00 → 00:00)

### 👤 **Personal Dashboard** ✅ *Fully Implemented*
- **Daily Motivation**: Inspiring quotes and affirmations on home screen
- **Quick Access Cards**: Fast navigation to journaling, tasks, and focus timer
- **Clean Interface**: Minimalist design that reduces cognitive load
- **Bottom Navigation**: Thumb-friendly navigation between main sections

### 🔮 **Planned Features** 🚧 *Coming Soon*
- **📊 Habit Tracking**: Daily habit monitoring with streak tracking
- **😊 Mood Tracking**: Daily mood logging with trend visualization
- **☁️ Cloud Sync**: Backup and sync across devices
- **📊 Analytics**: Detailed productivity insights and statistics



## 🎨 Design Philosophy

**Minimalist & Calming**: Every element designed for peace of mind and focused productivity

### **Color Palette**
- 🏠 **Soft Beige Background** (#F5F5DC): Easy on the eyes for extended use
- 💙 **Sky Blue Accents** (#87CEEB): Calming primary actions and highlights
- 🌸 **Blush Pink Elements** (#FFB6C1): Gentle secondary interactions
- 🌿 **Light Mint Indicators** (#98FB98): Success states and progress markers
- 📝 **Readable Text** (#555555): Optimal contrast for comfortable reading

### **Design Principles**
- **Rounded Cards**: Soft, approachable interface elements with gentle shadows
- **Consistent Spacing**: Harmonious 16dp grid system with breathing room
- **Intuitive Navigation**: Bottom navigation for thumb-friendly access
- **Micro-Interactions**: Subtle animations that provide delightful feedback
- **Typography**: Clean, readable fonts optimized for long writing sessions

## 🏗️ Architecture

Growly is built using modern Android development practices:

- **Jetpack Compose**: Modern declarative UI toolkit
- **MVVM Architecture**: Clean separation of concerns
- **Room Database**: Local data persistence
- **Kotlin Coroutines**: Asynchronous programming
- **StateFlow**: Reactive state management
- **Navigation Component**: Type-safe navigation

### 📁 Project Structure

```
app/
├── src/main/java/com/growly/app/
│   ├── data/
│   │   ├── entities/          # Room entities
│   │   ├── dao/              # Data Access Objects
│   │   ├── database/         # Database configuration
│   │   └── converters/       # Type converters
│   ├── ui/
│   │   ├── components/       # Reusable UI components
│   │   ├── screens/          # Screen composables
│   │   ├── viewmodels/       # ViewModels
│   │   ├── navigation/       # Navigation setup
│   │   └── theme/           # Design system
│   ├── MainActivity.kt
│   └── GrowlyApplication.kt
└── src/main/res/             # Resources
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Kotlin 1.9.10 or later

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/growly.git
   cd growly
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Build and Run**
   - Sync the project with Gradle files
   - Run the app on an emulator or physical device

## 🧪 Testing

The app includes comprehensive testing:

- **Unit Tests**: ViewModels and business logic
- **UI Tests**: Screen interactions and navigation
- **Database Tests**: Room database operations

Run tests using:
```bash
./gradlew test           # Unit tests
./gradlew connectedAndroidTest  # Instrumented tests
```

## 📱 Current Implementation Status

✅ **Fully Working Features:**
- Journal navigation and writing interface
- Task creation, completion, and deletion
- Pomodoro timer with real-time countdown
- Beautiful pastel UI with Material Design 3
- Bottom navigation between all screens
- Auto-save functionality for journal entries

🚧 **Coming Soon:**
- Habit tracking and mood logging
- Cloud sync and data export
- Advanced analytics and insights

## 🛠️ Development

### Key Dependencies

- **Jetpack Compose**: UI toolkit
- **Room**: Database
- **Navigation Compose**: Navigation
- **ViewModel**: State management
- **Coroutines**: Asynchronous operations
- **Material 3**: Design components

### Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Inspired by the principles of mindful productivity and digital wellness
- Design influenced by Material Design 3 guidelines
- Built with love for the journaling and productivity community

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/yourusername/growly/issues) page
2. Create a new issue with detailed information
3. Contact support at support@growly.app

---

**Made with 🌱 for mindful productivity**

*Growly helps you grow through intentional, mindful productivity practices.*
