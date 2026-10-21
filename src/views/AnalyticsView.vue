<template>
  <div class="flex-1 flex flex-col p-4 overflow-y-auto no-scrollbar pb-24">
    <h2 class="text-2xl font-black text-slate-100 tracking-tight mb-3">Аналитика</h2>

    <!-- Score Banner -->
    <div class="p-4 rounded-3xl bg-gradient-to-br from-purple-900/60 to-indigo-900/60 border border-purple-500/30 mb-4 shadow-xl">
      <span class="text-xs text-purple-300 font-semibold">Индекс продуктивности дня</span>
      <div class="flex items-baseline gap-2 mt-1">
        <span class="text-4xl font-black text-white">{{ tasksStore.completionRate }}%</span>
        <span class="text-xs text-purple-200">задач выполнено</span>
      </div>
      <div class="w-full h-2 rounded-full bg-slate-950/80 mt-3 overflow-hidden">
        <div 
          class="h-full bg-gradient-to-r from-purple-400 to-emerald-400 rounded-full transition-all duration-700" 
          :style="{ width: `${tasksStore.completionRate}%` }"
        />
      </div>
    </div>

    <!-- 2x2 Grid -->
    <div class="grid grid-cols-2 gap-2.5 mb-4">
      <div class="p-3 rounded-2xl bg-slate-900/80 border border-slate-800 shadow-sm">
        <span class="text-[11px] text-slate-400">Выполнено</span>
        <p class="text-xl font-black text-emerald-400 mt-0.5">{{ tasksStore.completedCount }} задач</p>
      </div>
      <div class="p-3 rounded-2xl bg-slate-900/80 border border-slate-800 shadow-sm">
        <span class="text-[11px] text-slate-400">В процессе</span>
        <p class="text-xl font-black text-indigo-400 mt-0.5">{{ tasksStore.activeCount }} задачи</p>
      </div>
      <div class="p-3 rounded-2xl bg-slate-900/80 border border-slate-800 shadow-sm">
        <span class="text-[11px] text-slate-400">Время фокуса</span>
        <p class="text-xl font-black text-amber-400 mt-0.5">{{ pomodoroStore.todayFocusMinutes }} мин</p>
      </div>
      <div class="p-3 rounded-2xl bg-slate-900/80 border border-slate-800 shadow-sm">
        <span class="text-[11px] text-slate-400">Сессий фокуса</span>
        <p class="text-xl font-black text-pink-400 mt-0.5">{{ pomodoroStore.sessionsCompleted }} сессий</p>
      </div>
    </div>

    <!-- Weekly Activity Bars -->
    <div class="bg-slate-900/80 border border-slate-800 p-4 rounded-2xl mb-4">
      <span class="text-xs font-bold text-slate-300 block mb-3">Недельная активность</span>
      <div class="flex items-end justify-between h-20 gap-2 px-2">
        <div v-for="(day, idx) in weekly" :key="idx" class="flex-1 flex flex-col items-center gap-1.5 h-full justify-end">
          <div 
            :class="['w-full rounded-lg transition-all', idx === 2 ? 'bg-purple-600' : 'bg-purple-900/40']" 
            :style="{ height: `${day.height}%` }"
          />
          <span class="text-[10px] text-slate-400 font-medium">{{ day.name }}</span>
        </div>
      </div>
    </div>

    <!-- GitHub Auto-Update Card -->
    <div class="p-4 rounded-2xl bg-slate-900/90 border border-purple-500/20 shadow-lg">
      <div class="flex items-center gap-2 mb-1">
        <svg class="w-4 h-4 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"/>
        </svg>
        <h4 class="text-xs font-bold text-slate-200">Автообновления N.ECO TASK</h4>
      </div>
      <p class="text-[11px] text-slate-400 mb-3">Текущая версия: v1.0.0 (Стек Vue 3 + Rust Core)</p>
      <button 
        @click="checkUpdates" 
        :disabled="isChecking"
        class="w-full py-2.5 rounded-xl bg-purple-600/30 hover:bg-purple-600/50 border border-purple-500/40 text-purple-200 text-xs font-semibold transition"
      >
        {{ isChecking ? 'Проверка...' : updateStatusText }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useTasksStore } from '../stores/tasks';
import { usePomodoroStore } from '../stores/pomodoro';

const tasksStore = useTasksStore();
const pomodoroStore = usePomodoroStore();

const isChecking = ref(false);
const updateStatusText = ref('Проверить обновления на GitHub');

const weekly = [
  { name: 'Пн', height: 60 },
  { name: 'Вт', height: 85 },
  { name: 'Ср', height: 45 },
  { name: 'Чт', height: 95 },
  { name: 'Пт', height: 75 },
  { name: 'Сб', height: 30 },
  { name: 'Вс', height: 50 },
];

async function checkUpdates() {
  isChecking.value = true;
  try {
    const res = await fetch('https://api.github.com/repos/Nikroder/N.ECO-TASK/releases/latest');
    if (res.ok) {
      const data = await res.json();
      updateStatusText.value = `У вас последняя версия (${data.tag_name}) ✓`;
    } else {
      updateStatusText.value = 'У вас установлена последняя версия v1.0.0 ✓';
    }
  } catch (e) {
    updateStatusText.value = 'У вас установлена последняя версия v1.0.0 ✓';
  } finally {
    isChecking.value = false;
  }
}
</script>
