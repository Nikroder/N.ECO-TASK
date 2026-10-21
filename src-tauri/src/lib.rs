pub mod commands;
pub mod db;
pub mod nlp;

use commands::AppState;
use rusqlite::Connection;
use std::sync::Mutex;

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    let conn = Connection::open("neco_task.db").expect("Failed to open SQLite database");
    db::init_db(&conn).expect("Failed to initialize database tables");

    let state = AppState {
        db: Mutex::new(conn),
    };

    tauri::Builder::default()
        .manage(state)
        .plugin(tauri_plugin_shell::init())
        .invoke_handler(tauri::generate_handler![
            commands::get_tasks,
            commands::create_task,
            commands::toggle_task,
            commands::delete_task,
            commands::parse_task_nlp
        ])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
