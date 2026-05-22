# Module Graph

## 1. Список модулей

- `:app`
- `:core:common`
- `:core:model`
- `:core:ui`
- `:core:network`
- `:core:database`
- `:core:navigation`
- `:domain:auth`
- `:domain:courses`
- `:data:courses`
- `:feature:auth:api`, `:feature:auth:impl`
- `:feature:home:api`, `:feature:home:impl`
- `:feature:favorites:api`, `:feature:favorites:impl`
- `:feature:account:api`, `:feature:account:impl`

## 2. Граф зависимостей (направление ->)

```text
:app
  -> :core:ui
  -> :core:navigation
  -> :feature:auth:api
  -> :feature:auth:impl
  -> :feature:home:api
  -> :feature:home:impl
  -> :feature:favorites:api
  -> :feature:favorites:impl
  -> :feature:account:api
  -> :feature:account:impl
  -> :data:courses

:feature:auth:impl
  -> :feature:auth:api
  -> :domain:auth
  -> :core:common
  -> :core:ui
  -> :core:navigation

:feature:home:impl
  -> :feature:home:api
  -> :domain:courses
  -> :core:model
  -> :core:common
  -> :core:ui
  -> :core:navigation

:feature:favorites:impl
  -> :feature:favorites:api
  -> :domain:courses
  -> :core:model
  -> :core:common
  -> :core:ui
  -> :core:navigation

:feature:account:impl
  -> :feature:account:api
  -> :core:ui
  -> :core:navigation

:data:courses
  -> :domain:courses
  -> :core:model
  -> :core:common
  -> :core:network
  -> :core:database

:domain:auth
  -> :core:common

:domain:courses
  -> :core:model
  -> :core:common

:core:network
  -> :core:common

:core:database
  -> :core:common
```

## 3. Правила зависимостей

- Feature не зависит напрямую от Data.
- Feature работает только с Domain use-case.
- Domain не зависит от Android/framework.
- Data реализует интерфейсы Domain repository.
- Core-модули не зависят от feature/data/domain (кроме допустимой зависимости на `:core:common`).

## 4. DI graph (логический)

- `:app` собирает DI контейнер.
- `:core:network` предоставляет `OkHttpClient`, `Retrofit`, `CoursesApi`.
- `:core:database` предоставляет `RoomDatabase`, `FavoriteCourseDao`.
- `:data:courses` бинды:
  - `CoursesRepositoryImpl` как `CoursesRepository`.
- `:domain:*` предоставляет use-case.
- `:feature:*:impl` инжектит use-case в ViewModel.

## 5. Module ownership / responsibility

- `:feature:*` — UI и presentation логика экрана.
- `:domain:*` — бизнес-правила и контракты.
- `:data:*` — источники данных и репозитории.
- `:core:*` — инфраструктура и общие компоненты.
- `:app` — интеграция модулей в приложение.
