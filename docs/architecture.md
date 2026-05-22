# Архитектура проекта

Документ фиксирует целевую архитектуру Android-приложения по ТЗ (этап проектирования, без реализации).

## 1. Контекст и ограничения ТЗ

- Реализуемые экраны: `Вход`, `Главная`, `Избранное`, `Аккаунт (заглушка)`.
- Не реализуем: `Онбординг`, `Регистрация`.
- Обязательные требования стека: `Kotlin`, `Retrofit`, `MVVM`, `XML`, `Clean Architecture`, `многомодульность`.
- Поведенческие требования:
  - валидация email (маска и запрет кириллицы);
  - нижнее меню после успешного входа;
  - загрузка курсов из API;
  - сортировка по `publishDate` (убывание);
  - избранное с локальным сохранением и отображением в разделе `Избранное`.

## 2. Цели архитектуры

- Изоляция бизнес-логики от Android/UI.
- Независимая разработка фич (`auth`, `home`, `favorites`, `account`).
- Явный поток данных `API/DB -> Domain -> UI`.
- Простая расширяемость (новые экраны, новый источник данных).
- Тестируемость use-case и repository без UI.

## 3. Модульная структура (верхний уровень)

- `:app` — точка сборки APK, `Application`, host-activity, корневая навигация.
- `:core:common` — общие типы, утилиты, константы, результаты (`Result/Error`).
- `:core:ui` — общие UI-компоненты XML/ViewBinding, темы, ресурсы общего назначения.
- `:core:network` — Retrofit, OkHttp, DTO-интерфейсы API, маппинг сетевых ошибок.
- `:core:database` — Room (entities, dao, database, migrations).
- `:core:navigation` — контракты маршрутов и навигационные команды.
- `:core:model` — domain/data модели без Android-зависимостей.

- `:feature:auth:api` / `:feature:auth:impl`
- `:feature:home:api` / `:feature:home:impl`
- `:feature:favorites:api` / `:feature:favorites:impl`
- `:feature:account:api` / `:feature:account:impl`

- `:data:courses` — репозиторий курсов, remote+local datasource, мапперы.
- `:domain:courses` — use-case для курсов/избранного/сортировки.
- `:domain:auth` — use-case валидации входа и состояния авторизации.

## 4. Responsibility модулей

- `:app`
  - собирает graph зависимостей DI;
  - предоставляет контейнер навигации (bottom navigation + host fragment/activity);
  - не содержит бизнес-правил.

- `:core:*`
  - переиспользуемые технические компоненты;
  - отсутствие feature-специфичной логики.

- `:domain:*`
  - бизнес-правила (валидация, сортировка, политика избранного);
  - интерфейсы репозиториев и use-case;
  - Kotlin-only, без Android.

- `:data:*`
  - реализация репозиториев;
  - оркестрация remote/local источников;
  - маппинг DTO/Entity <-> Domain Model.

- `:feature:*:impl`
  - ViewModel + UI state/effects/events;
  - Fragment/Adapter/AdapterDelegates;
  - вызов use-case, отображение состояния.

- `:feature:*:api`
  - контракт точки входа фичи для навигации и подключения в `:app`.

## 5. Структура feature-модулей

Единая структура для `feature/*/impl`:

- `presentation/`
  - `ui/` (Fragment, adapters, delegates)
  - `state/` (`UiState`, `UiEvent`, `UiEffect`)
  - `vm/` (ViewModel)
- `di/` (модуль DI фичи)
- `mapper/` (domain -> ui model)

Пример для `home`:

- `HomeFragment` отображает список курсов.
- `HomeViewModel` получает `GetCoursesUseCase`, `SortCoursesByPublishDateUseCase`, `ToggleFavoriteUseCase`.
- `CourseCardDelegate` отрисовывает карточки и ограничивает описание 2 строками.

## 6. Data Flow (API -> UI)

1. `HomeViewModel` отправляет событие `LoadCourses`.
2. `GetCoursesUseCase` вызывает `CoursesRepository`.
3. `CoursesRepository`:
   - читает данные remote через Retrofit;
   - объединяет с локальным состоянием избранного из Room;
   - возвращает `List<Course>` в domain-форме.
4. ViewModel маппит в UI-модель и публикует `HomeUiState.Content`.
5. UI отображает список.

Для toggling избранного:

1. UI событие `FavoriteClicked(courseId)`.
2. `ToggleFavoriteUseCase` обновляет локальную таблицу избранного.
3. Repository эмитит обновленный список.
4. `home` и `favorites` получают консистентное состояние.

## 7. Navigation Flow

- Старт: `Auth` экран.
- Успешный вход -> root-контейнер с bottom navigation.
- Табы:
  - `Home` (по умолчанию),
  - `Favorites`,
  - `Account` (заглушка).
- Навигация между табами сохраняет state каждого tab-host (без повторной инициализации).
- Кнопки `VK`/`OK` на `Auth` вызывают external intent в браузер.

## 8. State management strategy

- MVI-подобная схема внутри MVVM:
  - `UiEvent` — входные действия пользователя/жизненного цикла;
  - `UiState` — долгоживущее состояние экрана (`Loading/Content/Empty/Error`);
  - `UiEffect` — одноразовые эффекты (toast, open browser, navigate).
- В ViewModel:
  - `StateFlow<UiState>`;
  - `SharedFlow<UiEffect>`;
  - reducer-функции для предсказуемых переходов состояния.

## 9. Error handling strategy

- Единая модель ошибок в `:core:common`:
  - `NetworkError`, `ServerError`, `ValidationError`, `UnknownError`.
- На data-слое: маппинг исключений Retrofit/IO -> `AppError`.
- На domain-слое: `Either/Result` без бросания исключений наружу.
- На UI-слое:
  - recoverable ошибки -> `UiState.Error` + retry action;
  - валидационные ошибки входа -> inline validation;
  - external-link ошибки -> `UiEffect.ShowMessage`.

## 10. Room strategy (избранное)

- Room используется для локального состояния избранного и офлайн-устойчивости этого состояния.
- Минимальная таблица:
  - `favorite_courses(course_id PRIMARY KEY, updated_at)`.
- Почему отдельная таблица, а не полное кэширование курсов:
  - ТЗ требует локально хранить факт избранности;
  - упрощает синхронизацию с API-данными.
- Правило мерджа:
  - `effectiveHasLike = api.hasLike OR localFavoriteExists`.
- Для удаления из избранного:
  - при явном снятии лайка удаляем запись из local table;
  - в UI сразу отражается обновление через Flow из DAO.

## 11. Network layer strategy

- `Retrofit + OkHttp + kotlinx.serialization/moshi` (один выбранный сериализатор на весь проект).
- `CoursesApi` возвращает DTO-ответ с массивом `courses`.
- DataSource:
  - `CoursesRemoteDataSource` только сетевые вызовы;
  - retry/backoff не обязателен для тестового, но добавляется как расширение.
- Сортировка по `publishDate` выполняется на domain-слое (не в UI и не в API).

## 12. Naming conventions

- Модули: `:layer:scope` (`:core:network`, `:feature:home:impl`).
- Domain:
  - use-case: `VerbNounUseCase` (`GetCoursesUseCase`, `ToggleFavoriteUseCase`),
  - repository interface: `NounRepository`.
- Data:
  - DTO: `*Dto`, entity: `*Entity`, mapper: `*Mapper`.
- Presentation:
  - ViewModel: `*ViewModel`,
  - state/event/effect: `ScreenUiState`, `ScreenUiEvent`, `ScreenUiEffect`.
- Навигация:
  - route id: `route_*`, command: `NavigateTo*`.

## 13. Как будут работать ключевые функции

- Избранное:
  - источник истины для списка курсов: merged поток `API + local favorites`;
  - раздел `Избранное` фильтрует merged-список по `hasLike=true`.

- Сортировка:
  - по нажатию кнопки сортировки вызывается use-case сортировки по `publishDate` DESC;
  - сортировка применяется к текущему списку в `HomeUiState.Content`.

- Навигация:
  - авторизация успешна -> переключение root-графа на основной с bottom nav;
  - табы переключаются через `core:navigation` контракты без прямых зависимостей между фичами.
