use regex::Regex;
use serde::{Deserialize, Serialize};

#[derive(Debug, Serialize, Deserialize)]
pub struct ParsedTask {
    pub clean_title: String,
    pub date: Option<String>,
    pub time: Option<String>,
    pub priority: String,
    pub tags: Vec<String>,
}

pub fn parse_nlp_input(input: &str) -> ParsedTask {
    let mut text = input.trim().to_string();
    let mut date = None;
    let mut time = None;
    let mut priority = "medium".to_string();
    let mut tags = Vec::new();

    // 1. Tags (#tag)
    let tag_re = Regex::new(r"#([a-zA-Zа-яА-Я0-9_]+)").unwrap();
    for cap in tag_re.captures_iter(&text) {
        tags.push(cap[1].to_string());
    }
    text = tag_re.replace_all(&text, "").to_string();

    // 2. Priority
    let urgent_re = Regex::new(r"(?i)!(срочно|urgent|p1)").unwrap();
    let high_re = Regex::new(r"(?i)!(высокий|важно|high|p2)").unwrap();
    let low_re = Regex::new(r"(?i)!(низкий|low|p4)").unwrap();

    if urgent_re.is_match(&text) {
        priority = "urgent".to_string();
        text = urgent_re.replace_all(&text, "").to_string();
    } else if high_re.is_match(&text) {
        priority = "high".to_string();
        text = high_re.replace_all(&text, "").to_string();
    } else if low_re.is_match(&text) {
        priority = "low".to_string();
        text = low_re.replace_all(&text, "").to_string();
    }

    // 3. Date
    let tomorrow_re = Regex::new(r"(?i)\b(завтра|tomorrow)\b").unwrap();
    let today_re = Regex::new(r"(?i)\b(сегодня|today)\b").unwrap();

    if tomorrow_re.is_match(&text) {
        date = Some("Завтра".to_string());
        text = tomorrow_re.replace_all(&text, "").to_string();
    } else if today_re.is_match(&text) {
        date = Some("Сегодня".to_string());
        text = today_re.replace_all(&text, "").to_string();
    }

    // 4. Time
    let time_re = Regex::new(r"(?:в|at)?\s*(\d{1,2}:\d{2})").unwrap();
    if let Some(cap) = time_re.captures(&text) {
        time = Some(cap[1].to_string());
        text = time_re.replace_all(&text, "").to_string();
    }

    let clean_title = text.split_whitespace().collect::<Vec<&str>>().join(" ");

    ParsedTask {
        clean_title: if clean_title.is_empty() { input.to_string() } else { clean_title },
        date,
        time,
        priority,
        tags,
    }
}
