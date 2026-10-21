use crate::db::{self, TaskItem};
use crate::nlp::{self, ParsedTask};
use rusqlite::Connection;
use std::sync::Mutex;
use tauri::State;

pub struct AppState {
    pub db: Mutex<Connection>,
}

#[tauri::command]
pub fn get_tasks(state: State<AppState>) -> Result<Vec<TaskItem>, String> {
    let conn = state.db.lock().map_err(|e| e.to_string())?;
    db::get_tasks(&conn).map_err(|e| e.to_string())
}

#[tauri::command]
pub fn create_task(state: State<AppState>, task: TaskItem) -> Result<i64, String> {
    let conn = state.db.lock().map_err(|e| e.to_string())?;
    db::insert_task(&conn, &task).map_err(|e| e.to_string())
}

#[tauri::command]
pub fn toggle_task(state: State<AppState>, id: i64, completed: bool) -> Result<(), String> {
    let conn = state.db.lock().map_err(|e| e.to_string())?;
    db::toggle_task(&conn, id, completed).map_err(|e| e.to_string())
}

#[tauri::command]
pub fn delete_task(state: State<AppState>, id: i64) -> Result<(), String> {
    let conn = state.db.lock().map_err(|e| e.to_string())?;
    db::delete_task(&conn, id).map_err(|e| e.to_string())
}

#[tauri::command]
pub fn parse_task_nlp(input: String) -> ParsedTask {
    nlp::parse_nlp_input(&input)
}
