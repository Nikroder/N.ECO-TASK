import { defineStore } from 'pinia';

export const usePomodoroStore = defineStore('pomodoro', {
  state: () => ({
    mode: 'work', // 'work' | 'short' | 'long'
    timeLeft: 25 * 60,
    totalTime: 25 * 60,
    isRunning: false,
    intervalId: null,
    sessionsCompleted: 4,
    todayFocusMinutes: 125,
    ambientSound: false,
    audioContext: null
  }),

  getters: {
    formattedTime(state) {
      const m = String(Math.floor(state.timeLeft / 60)).padStart(2, '0');
      const s = String(state.timeLeft % 60).padStart(2, '0');
      return `${m}:${s}`;
    },

    progress(state) {
      if (state.totalTime === 0) return 0;
      return ((state.totalTime - state.timeLeft) / state.totalTime) * 100;
    }
  },

  actions: {
    setMode(mode) {
      this.pause();
      this.mode = mode;
      const minutes = mode === 'work' ? 25 : mode === 'short' ? 5 : 15;
      this.totalTime = minutes * 60;
      this.timeLeft = minutes * 60;
    },

    start() {
      if (this.isRunning) return;
      this.isRunning = true;
      this.intervalId = setInterval(() => {
        if (this.timeLeft > 0) {
          this.timeLeft--;
        } else {
          this.onComplete();
        }
      }, 1000);
    },

    pause() {
      this.isRunning = false;
      if (this.intervalId) {
        clearInterval(this.intervalId);
        this.intervalId = null;
      }
    },

    toggle() {
      if (this.isRunning) this.pause();
      else this.start();
    },

    reset() {
      this.setMode(this.mode);
    },

    onComplete() {
      this.pause();
      if (this.mode === 'work') {
        this.sessionsCompleted++;
        this.todayFocusMinutes += 25;
        this.setMode('short');
      } else {
        this.setMode('work');
      }
    },

    toggleAmbient() {
      this.ambientSound = !this.ambientSound;
    }
  }
});
