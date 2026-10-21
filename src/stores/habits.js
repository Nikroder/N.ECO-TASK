import { defineStore } from 'pinia';

const isTauri = () => typeof window !== 'undefined' && window.__TAURI_INTERNALS__ !== undefined;

export const useHabitsStore = defineStore('habits', {
  state: () => ({
    habits: [
      { id: 1, title: 'Выпить 2л чистой воды', streak: 14, completed: true, color: 'emerald' },
      { id: 2, title: 'Утренняя зарядка 15 мин', streak: 7, completed: true, color: 'purple' },
      { id: 3, title: 'Чтение профессиональной литературы', streak: 21, completed: false, color: 'blue' },
      { id: 4, title: 'Медитация и дыхательные практики', streak: 4, completed: false, color: 'amber' }
    ]
  }),

  actions: {
    async init() {
      if (isTauri()) {
        try {
          const { invoke } = await import('@tauri-apps/api/core');
          const rustHabits = await invoke('get_habits');
          if (rustHabits && rustHabits.length > 0) this.habits = rustHabits;
        } catch (e) {}
      } else {
        const saved = localStorage.getItem('neco_habits');
        if (saved) {
          try { this.habits = JSON.parse(saved); } catch (e) {}
        }
      }
    },

    saveLocal() {
      if (!isTauri()) {
        localStorage.setItem('neco_habits', JSON.stringify(this.habits));
      }
    },

    async toggleHabit(id) {
      const h = this.habits.find(x => x.id === id);
      if (h) {
        h.completed = !h.completed;
        if (h.completed) h.streak += 1;
        else h.streak = Math.max(0, h.streak - 1);

        if (isTauri()) {
          try {
            const { invoke } = await import('@tauri-apps/api/core');
            await invoke('toggle_habit', { id, completed: h.completed });
          } catch (e) {}
        }
        this.saveLocal();
      }
    },

    addHabit(title, color = 'purple') {
      const newHabit = {
        id: Date.now(),
        title,
        streak: 0,
        completed: false,
        color
      };
      this.habits.push(newHabit);
      this.saveLocal();
    }
  }
});
