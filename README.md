# N.ECO TASK — Ультимативный планировщик задач (Vue 3 + Rust)

[![Build Android APK and Release](https://github.com/Nikroder/N.ECO-TASK/actions/workflows/build-apk.yml/badge.svg)](https://github.com/Nikroder/N.ECO-TASK/actions/workflows/build-apk.yml)
![Stack](https://img.shields.io/badge/Stack-Vue%203%20%2B%20Rust-purple.svg)
![Core](https://img.shields.io/badge/Core-Tauri%202.0%20(Rust)-orange.svg)
![UI](https://img.shields.io/badge/Frontend-Vite%20%2B%20Tailwind-blue.svg)
![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20Web%20%7C%20Desktop-success.svg)

**N.ECO TASK** — персональная «Операционная система продуктивности» (Life OS) нового поколения на стеке **Vue 3 + Rust (Tauri 2.0)**.
Проект объединяет задачи, календарь, трекер привычек, таймер глубокого фокуса (Pomodoro) и интерактивные виджеты рабочего стола Android.

---

## 🌐 Интерактивный предпросмотр в браузере

Попробуйте приложение прямо сейчас без установки:
👉 **[https://nikroder.github.io/N.ECO-TASK/](https://nikroder.github.io/N.ECO-TASK/)**

---

## 📥 Скачать приложение для Android (APK)

* [**Скачать актуальный N.ECO-TASK.apk**](https://github.com/Nikroder/N.ECO-TASK/releases/latest)
* Приложение поддерживает **автообновление** прямо из GitHub Releases: при выходе новой сборки на экране появится кнопка установки обновления в один клик.

---

## 🛠 Технологический стек

### 🟢 Frontend (Vue 3)
* **Фреймворк:** Vue 3 (Composition API, `<script setup>`)
* **Сборщик:** Vite 5 (мгновенный Hot Module Replacement в браузере)
* **Стилизация:** Tailwind CSS (темная тема AMOLED, адаптивность)
* **Управление состоянием:** Pinia (реактивные хранилища задач, привычек и Pomodoro)
* **Иконки:** SVG & Lucide Icons

### 🦀 Core & Backend (Rust)
* **Ядро:** Tauri 2.0 Mobile
* **База данных:** SQLite (`rusqlite`) с мгновенным откликом и надежным хранением
* **NLP-движок:** высокоскоростной парсер естественного языка на Rust (`regex`) для автоматического извлечения дат, времени, тегов и приоритетов
* **Tauri Commands:** прямой вызов Rust-функций из Vue-компонентов

### 📱 Android & Виджеты
* **Android APK:** автоматическая сборка через GitHub Actions CI/CD
* **Glance AppWidgets:** интерактивный виджет «Задачи на сегодня» (4x3) и панель быстрых действий (4x1)
* **Уведомления:** точные будильники (Exact Alarms) и фоновые процессы

---

## 🏗 Архитектура проекта

```
N.ECO-TASK/
 ├── src/                          # 🟢 FRONTEND (Vue 3)
 │    ├── stores/                  # Pinia хранилища (tasks.js, habits.js, pomodoro.js)
 │    ├── views/                   # Экраны приложения:
 │    │    ├── TasksView.vue       # Задачи (список, фильтры, Kanban-доска)
 │    │    ├── CalendarView.vue    # Тайм-блокинг и календарная сетка
 │    │    ├── HabitsView.vue      # Привычки и Heatmap карта дисциплины
 │    │    ├── FocusView.vue       # Круговой таймер Pomodoro и звуки окружения
 │    │    └── AnalyticsView.vue   # Графики продуктивности и автообновления
 │    ├── components/              # Reusable компоненты:
 │    │    ├── TaskItem.vue        # Карточка задачи с чекбоксом и приоритетом
 │    │    ├── AddTaskModal.vue    # Умный NLP-ввод с распознаванием на лету
 │    │    └── GlanceWidgetsView.vue # Симулятор виджетов рабочего стола Android
 │    ├── App.vue                  # Корневой интерфейс со смартфоном и навигацией
 │    └── main.js
 ├── src-tauri/                    # 🦀 CORE (Rust)
 │    ├── src/
 │    │    ├── lib.rs              # Точка входа Tauri
 │    │    ├── db.rs               # Локальная база данных SQLite
 │    │    ├── nlp.rs              # Парсер естественного языка на Rust
 │    │    └── commands.rs         # Tauri IPC команды (get_tasks, create_task, etc.)
 │    ├── Cargo.toml               # Зависимости Rust
 │    └── tauri.conf.json          # Конфигурация Tauri
 ├── docs/                         # 🌐 Онлайн-версия для GitHub Pages
 ├── .github/workflows/            # 🚀 Автоматическая сборка APK на GitHub
 └── package.json                  # Скрипты Vite + Vue 3
```

---

## 💻 Локальный запуск и разработка

### 1. Запуск интерфейса в браузере (быстрый старт):
```powershell
# Установка зависимостей
npm install

# Запуск локального сервера разработки
npm run dev
```
Приложение откроется по адресу `http://localhost:5173`. Любые изменения в коде мгновенно отображаются в браузере.

### 2. Сборка продакшен-бандла для веба:
```powershell
npm run build
```

---

## 📄 Лицензия
Разработано для личного использования и коммерческого масштабирования.
Стек: **Vue 3 + Rust + Android**.
