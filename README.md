# Effective Mobile Тестовое задание

**Разработчик:** Гульванюк Кирилл Алексеевич  
**Telegram:** @Warzone01

---

# BUILD-файл 

## **app-debug.apk** - лежит в корне проекта, можно скачать и запустить!

---

## Архитектура

**MVVM** (Model-View-ViewModel) с **Clean Architecture** в расслоении модулей.

```
feature/* (presentation/domain/data)
    ↓
:data (repository, remote, local)
    ↓
:domain (usecase, repository interfaces)
    ↓
:core:common, :core:model
```

- **domain** - чистый Kotlin/JVM, без зависимостей от Android.
- **data** - имплементации репозиториев, удалённые и локальные источники данных.
- *feature/* - UI + ViewModel + MVI-подобный подход (State/Event/Effect/Reducer).

### Presentation-слой
- `Fragment` - View, подписывается на `StateFlow<UiState>`.
- `ViewModel` - `@HiltViewModel`, принимает use case-ы, хранит состояние, обрабатывает события.
- `UiState` - sealed interface (Loading / Content / Error).
- `UiEvent` - sealed классы действий пользователя.
- `UiEffect` - одноразовые side-эффекты (навигация, toast).
- `Reducer` - чистая функция `(State, Event) → State`.

---

## Модули

### `:app`
Точка входа. `MainActivity`, `App` (@HiltAndroidApp), `MainContainerFragment` с BottomNavigationView.

### `:domain`
Бизнес-логика. Use case-ы и интерфейсы репозиториев.

### `:data`
Слой данных. Репозитории, CourseApi (Retrofit), DAO (Room), мапперы DTO/Entity ↔ domain model.

### `:core:common`
Общие утилиты: `AppResult`, `AppError`, `Mapper`, `UseCase`, `UiText`, `CoroutineDispatchers`, `DateFormatter`.

### `:core:model`
Domain-модели: `Course`, `FavoriteCourse`.

### `:core:ui`
Общие UI-ресурсы: `ResourceProvider` для получения строк/д rawable в common-коде.

### `:core:network`
Retrofit + OkHttp, `SafeApiCall`, `NetworkResult`, мапперы ошибок.

### `:core:database`
Room: сущности, DAO, конвертеры. База `effective_mobile.db`.

### `:feature:auth`
Экран логина (email/password + VK/OK). `LoginViewModel`, `ValidateLoginFormUseCase`.

### `:feature:home`
Список курсов (RecyclerView + AdapterDelegates). Сортировка по дате, добавление в избранное.

### `:feature:favorites`
Список избранных курсов с возможностью удаления.

### `:feature:details`
Детальная карточка курса: обложка, цена, описание, кнопка избранного.

### `:feature:account`
Статический экран профиля - без ViewModel, чистая вёрстка.

---

## Используемые библиотеки

| Библиотека | Версия | Назначение |
|---|---|---|
| **Kotlin** | 2.0.21 | Язык |
| **AGP** | 8.7.3 | Плагин |
| **Hilt (Dagger)** | 2.57.1 | DI |
| **Room** | 2.7.2 | Локальная БД |
| **Retrofit** | 2.11.0 | HTTP-клиент |
| **OkHttp** | 4.12.0 | HTTP-клиент, логирование |
| **Kotlinx Serialization** | 1.7.3 | JSON-сериализация |
| **Coroutines** | 1.9.0 | Асинхронность |
| **Navigation** | 2.9.3 | Навигация |
| **Fragment KTX** | 1.8.9 | ViewBinding + фрагменты |
| **Lifecycle** | 2.9.4 | ViewModel, Lifecycle |
| **Material** | 1.12.0 | Material 3 компоненты |
| **ConstraintLayout** | 2.2.1 | Layout |
| **RecyclerView** | 1.3.2 | Списки |

---

## Ключевые решения

### DI - Hilt
`@HiltAndroidApp` → `@AndroidEntryPoint` → `@HiltViewModel`. Модули: `NetworkModule`, `DatabaseModule`, `DataRemoteModule`, `DataLocalModule`, `AuthDataModule`.

### Навигация
Два NavHostFragment:
- **root** (`nav_graph_root.xml`) - auth → main
- **main** (`nav_graph_main.xml`) - 3 таба BottomNavigation (home / favorites / account) + общий `detailsFragment`, доступный по deep link `emtest://details/{courseId}` с home и favorites.

### Сеть
`GET` к Google Drive (mock-данные). Retrofit + kotlinx.serialization конвертер. `SafeApiCall` для обработки ошибок.

### База данных
Room с таблицами `courses` и `favorite_courses`. `fallbackToDestructiveMigration()`. Версия 2.

### UI
- ViewBinding (проект-wide).
- AdapterDelegates для RecyclerView (Course, Loading, Error, Empty).
- NestedScrollView + ConstraintLayout для детальных экранов.
- Material 3 BottomNavigationView с кастомным `itemActiveIndicatorStyle`.
- Все цвета/размеры/стили - в ресурсах (без хардкода).
- Векторные drawable-иконки для табов, кнопок, чипсов.

### Мапперы
`Mapper<I, O>` / `BiMapper<I1, I2, O>` из `:core:common`.  
DTO → domain → UI model. Дата форматируется через общий `DateFormatter`.

### Сборка
- compileSdk / targetSdk 36, minSdk 26.
- Java 17, JVM target 17.
- 13 модулей.
- Room schema экспортируется в `core/database/schemas/`.

---

## Требования к окружению

- Android Studio Ladybug / Koala
- JDK 17
- Gradle 8.x (wrapper приложен)

## Сборка и запуск

```bash
./gradlew assembleDebug
```

Для установки на устройство или эмулятор:

```bash
./gradlew installDebug
```
