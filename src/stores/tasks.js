import { defineStore } from 'pinia';

// Helper to check if running inside Tauri (Rust backend)
const isTauri = () => typeof window !== 'undefined' && window.__TAURI_INTERNALS__ !== undefined;

export const useTasksStore = defineStore('tasks', {
  state: () => ({
    tasks: [
      { id: 1, title: 'Запустить сборку APK в GitHub Actions', time: 'Сегодня, 10:00', priority: 'urgent', category: 'работа', completed: true },
      { id: 2, title: 'Стек переведён на Vue 3 + Rust', time: 'Сегодня, 14:00', priority: 'high', category: 'проект', completed: true },
      { id: 3, title: 'Интеграция Tauri 2.0 Core и SQLite', time: 'Сегодня, 18:30', priority: 'high', category: 'работа', completed: false },
      { id: 4, title: 'Вечерняя пробежка и тренировка 5 км', time: 'Сегодня, 20:00', priority: 'medium', category: 'спорт', completed: false },
      { id: 5, title: 'Чтение книги по архитектуре микросервисов', time: 'Завтра, 09:00', priority: 'low', category: 'личное', completed: false }
    ],
    filter: 'all',
    searchQuery: '',
    isKanban: false
  }),

  getters: {
    filteredTasks(state) {
      const q = state.searchQuery.toLowerCase();
      return state.tasks.filter(t => {
        const matchesQuery = !q || t.title.toLowerCase().includes(q) || t.category.toLowerCase().includes(q);
        const matchesFilter = 
          state.filter === 'all' ? !t.completed :
          state.filter === 'today' ? !t.completed && t.time.includes('Сегодня') :
          state.filter === 'important' ? !t.completed && (t.priority === 'urgent' || t.priority === 'high') :
          state.filter === 'completed' ? t.completed : true;
        return matchesQuery && matchesFilter;
      });
    },

    completedCount(state) {
      return state.tasks.filter(t => t.completed).length;
    },

    activeCount(state) {
      return state.tasks.filter(t => !t.completed).length;
    },

    completionRate(state) {
      if (state.tasks.length === 0) return 0;
      return Math.round((state.completedCount / state.tasks.length) * 100);
    }
  },

  actions: {
    async init() {
      if (isTauri()) {
        try {
          const { invoke } = await import('@tauri-apps/api/core');
          const rustTasks = await invoke('get_tasks');
          if (rustTasks && rustTasks.length > 0) {
            this.tasks = rustTasks;
          }
        } catch (e) {
          console.warn('Tauri invoke get_tasks failed, fallback to local', e);
        }
      } else {
        const saved = localStorage.getItem('neco_tasks');
        if (saved) {
          try { this.tasks = JSON.parse(saved); } catch (e) {}
        }
      }
    },

    saveLocal() {
      if (!isTauri()) {
        localStorage.setItem('neco_tasks', JSON.stringify(this.tasks));
      }
    },

    async addTask(taskData) {
      const newTask = {
        id: Date.now(),
        title: taskData.title,
        time: taskData.time || 'Сегодня, 18:00',
        priority: taskData.priority || 'medium',
        category: taskData.category || 'личное',
        completed: false
      };

      if (isTauri()) {
        try {
          const { invoke } = await import('@tauri-apps/api/core');
          await invoke('create_task', { task: newTask });
        } catch (e) {
          console.warn('Tauri invoke create_task error:', e);
        }
      }

      this.tasks.unshift(newTask);
      this.saveLocal();
    },

    async toggleTask(id) {
      const task = this.tasks.find(t => t.id === id);
      if (task) {
        task.completed = !task.completed;
        if (isTauri()) {
          try {
            const { invoke } = await import('@tauri-apps/api/core');
            await invoke('toggle_task', { id, completed: task.completed });
          } catch (e) {
            console.warn('Tauri invoke toggle_task error:', e);
          }
        }
        this.saveLocal();
      }
    },

    async deleteTask(id) {
      this.tasks = this.tasks.filter(t => t.id !== id);
      if (isTauri()) {
        try {
          const { invoke } = await import('@tauri-apps/api/core');
          await invoke('delete_task', { id });
        } catch (e) {
          console.warn('Tauri invoke delete_task error:', e);
        }
      }
      this.saveLocal();
    },

    setFilter(filter) {
      this.filter = filter;
    },

    toggleKanban() {
      this.isKanban = !this.isKanban;
    }
  }
});
