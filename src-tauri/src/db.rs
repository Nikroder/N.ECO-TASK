use rusqlite::{params, Connection, Result};
use serde::{Deserialize, Serialize};

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct TaskItem {
    pub id: i64,
    pub title: String,
    pub time: String,
    pub priority: String,
    pub category: String,
    pub completed: bool,
}

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct HabitItem {
    pub id: i64,
    pub title: String,
    pub streak: i32,
    pub completed: bool,
    pub color: String,
}

pub fn init_db(conn: &Connection) -> Result<()> {
    conn.execute(
        "CREATE TABLE IF NOT EXISTS tasks (
            id INTEGER PRIMARY KEY,
            title TEXT NOT NULL,
            time TEXT,
            priority TEXT,
            category TEXT,
            completed INTEGER DEFAULT 0
        )",
        [],
    )?;

    conn.execute(
        "CREATE TABLE IF NOT EXISTS habits (
            id INTEGER PRIMARY KEY,
            title TEXT NOT NULL,
            streak INTEGER DEFAULT 0,
            completed INTEGER DEFAULT 0,
            color TEXT
        )",
        [],
    )?;

    // Check if initial tasks exist
    let count: i64 = conn.query_row("SELECT COUNT(*) FROM tasks", [], |r| r.get(0))?;
    if count == 0 {
        conn.execute(
            "INSERT INTO tasks (title, time, priority, category, completed) VALUES
             ('Запустить сборку APK в GitHub Actions', 'Сегодня, 10:00', 'urgent', 'работа', 1),
             ('Стек переведён на Vue 3 + Rust', 'Сегодня, 14:00', 'high', 'проект', 1),
             ('Интеграция Tauri 2.0 Core и SQLite', 'Сегодня, 18:30', 'high', 'работа', 0),
             ('Вечерняя пробежка и тренировка 5 км', 'Сегодня, 20:00', 'medium', 'спорт', 0)",
            [],
        )?;
    }

    Ok(())
}

pub fn get_tasks(conn: &Connection) -> Result<Vec<TaskItem>> {
    let mut stmt = conn.prepare("SELECT id, title, time, priority, category, completed FROM tasks ORDER BY completed ASC, id DESC")?;
    let task_iter = stmt.query_map([], |row| {
        Ok(TaskItem {
            id: row.get(0)?,
            title: row.get(1)?,
            time: row.get(2)?,
            priority: row.get(3)?,
            category: row.get(4)?,
            completed: row.get::<_, i32>(5)? == 1,
        })
    })?;

    let mut tasks = Vec::new();
    for task in task_iter {
        tasks.push(task?);
    }
    Ok(tasks)
}

pub fn insert_task(conn: &Connection, task: &TaskItem) -> Result<i64> {
    conn.execute(
        "INSERT INTO tasks (title, time, priority, category, completed) VALUES (?1, ?2, ?3, ?4, ?5)",
        params![task.title, task.time, task.priority, task.category, if task.completed { 1 } else { 0 }],
    )?;
    Ok(conn.last_insert_rowid())
}

pub fn toggle_task(conn: &Connection, id: i64, completed: bool) -> Result<()> {
    conn.execute(
        "UPDATE tasks SET completed = ?1 WHERE id = ?2",
        params![if completed { 1 } else { 0 }, id],
    )?;
    Ok(())
}

pub fn delete_task(conn: &Connection, id: i64) -> Result<()> {
    conn.execute("DELETE FROM tasks WHERE id = ?1", params![id])?;
    Ok(())
}
