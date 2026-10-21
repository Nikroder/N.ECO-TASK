<template>
  <div class="min-h-screen bg-slate-950 text-slate-100 flex flex-col items-center justify-start p-3 md:p-6 font-sans">
    
    <!-- Top Web Controls Header -->
    <header class="w-full max-w-4xl mb-6 flex flex-col md:flex-row items-center justify-between gap-4 bg-slate-900/90 backdrop-blur-md p-3.5 rounded-2xl border border-slate-800 shadow-xl">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-xl bg-gradient-to-tr from-purple-600 to-indigo-500 flex items-center justify-center shadow-lg shadow-purple-500/20">
          <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
        </div>
        <div>
          <div class="flex items-center gap-2">
            <h1 class="text-lg font-black bg-gradient-to-r from-purple-400 to-indigo-300 bg-clip-text text-transparent">N.ECO TASK</h1>
            <span class="text-[10px] px-2 py-0.5 rounded-full bg-purple-500/10 text-purple-400 border border-purple-500/30 font-bold">Vue 3 + Rust</span>
          </div>
          <p class="text-xs text-slate-400">Tauri 2.0 Core • Jetpack Glance Widgets • Web Preview</p>
        </div>
      </div>

      <div class="flex items-center flex-wrap gap-2">
        <!-- Mode switcher: App vs Desktop Widgets -->
        <div class="bg-slate-950 p-1 rounded-xl border border-slate-800 flex items-center text-xs">
          <button 
            @click="viewMode = 'app'" 
            :class="['px-3 py-1.5 rounded-lg font-medium transition', viewMode === 'app' ? 'bg-purple-600 text-white shadow' : 'text-slate-400 hover:text-white']"
          >
            📱 Приложение
          </button>
          <button 
            @click="viewMode = 'widgets'" 
            :class="['px-3 py-1.5 rounded-lg font-medium transition', viewMode === 'widgets' ? 'bg-purple-600 text-white shadow' : 'text-slate-400 hover:text-white']"
          >
            🏠 Виджеты (Desktop)
          </button>
        </div>

        <a 
          href="https://github.com/Nikroder/N.ECO-TASK" 
          target="_blank" 
          class="flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-slate-800 hover:bg-slate-700 border border-slate-700 text-xs text-slate-300 transition"
        >
          <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24"><path fill-rule="evenodd" clip-rule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.53 1.032 1.53 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"/></svg>
          GitHub
        </a>

        <a 
          href="https://github.com/Nikroder/N.ECO-TASK/releases/latest" 
          target="_blank" 
          class="flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-purple-600 hover:bg-purple-500 text-white font-medium text-xs transition shadow-md shadow-purple-600/30"
        >
          ⬇ Скачать APK
        </a>
      </div>
    </header>

    <!-- Smartphone Mockup Frame -->
    <main class="relative w-[390px] h-[780px] rounded-[48px] bg-slate-950 border-[8px] border-slate-800 shadow-2xl overflow-hidden flex flex-col select-none">
      
      <!-- Top Status Bar (Clock & Punch Hole) -->
      <div class="relative w-full h-8 pt-2 px-6 flex items-center justify-between text-[11px] font-semibold text-slate-400 z-50">
        <span>{{ statusTime }}</span>
        <!-- Punch-hole Camera -->
        <div class="absolute left-1/2 -translate-x-1/2 top-2.5 w-3.5 h-3.5 rounded-full bg-black border border-slate-800"></div>
        <div class="flex items-center gap-1.5">
          <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 24 24"><path d="M12 3c-4.97 0-9 4.03-9 9 0 2.12.74 4.07 1.97 5.61L12 22l7.03-4.39C20.26 16.07 21 14.12 21 12c0-4.97-4.03-9-9-9z"/></svg>
          <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 24 24"><path d="M2 17h20v2H2zm1.15-4.05L4 11.92c3.15-2.09 7.02-3.17 11-3.17s7.85 1.08 11 3.17l.85 1.03c-3.4-2.25-7.57-3.41-11.85-3.41s-8.45 1.16-11.85 3.41z"/></svg>
          <span>98%</span>
        </div>
      </div>

      <!-- App Viewport -->
      <div v-if="viewMode === 'app'" class="flex-1 flex flex-col overflow-hidden relative">
        <component :is="activeViewComponent" />

        <!-- Bottom Navigation Bar -->
        <nav class="absolute bottom-0 left-0 right-0 h-16 bg-slate-950/95 backdrop-blur-lg border-t border-slate-800/80 flex items-center justify-around px-2 z-40">
          <button 
            v-for="tab in tabs" 
            :key="tab.id"
            @click="activeTab = tab.id"
            :class="[
              'flex flex-col items-center text-[10px] gap-1 transition',
              activeTab === tab.id ? 'text-purple-400 font-bold' : 'text-slate-400 font-medium hover:text-slate-200'
            ]"
          >
            <component :is="tab.icon" class="w-5 h-5" />
            {{ tab.label }}
          </button>
        </nav>
      </div>

      <!-- Android Desktop Widgets Viewport -->
      <GlanceWidgetsView 
        v-else 
        @open-add="openAddModal" 
        @switch-tab="switchToAppTab" 
      />

      <!-- Gesture Pill -->
      <div class="w-full h-4 flex items-center justify-center bg-slate-950 pb-1 z-50">
        <div class="w-32 h-1 bg-slate-600 rounded-full"></div>
      </div>
    </main>

    <!-- Global Add Task Modal -->
    <AddTaskModal 
      :show="showGlobalAddModal" 
      @close="showGlobalAddModal = false" 
      @add="onTaskAdded" 
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, h } from 'vue';
import { useTasksStore } from './stores/tasks';
import { useHabitsStore } from './stores/habits';

import TasksView from './views/TasksView.vue';
import CalendarView from './views/CalendarView.vue';
import HabitsView from './views/HabitsView.vue';
import FocusView from './views/FocusView.vue';
import AnalyticsView from './views/AnalyticsView.vue';
import GlanceWidgetsView from './components/GlanceWidgetsView.vue';
import AddTaskModal from './components/AddTaskModal.vue';

const tasksStore = useTasksStore();
const habitsStore = useHabitsStore();

const viewMode = ref('app'); // 'app' | 'widgets'
const activeTab = ref('tasks');
const statusTime = ref('12:30');
const showGlobalAddModal = ref(false);

// SVG Icons helper
const createIcon = (path) => ({
  render: () => h('svg', { class: 'w-5 h-5', fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
    h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: path })
  ])
});

const tabs = [
  { id: 'tasks', label: 'Задачи', icon: createIcon('M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z') },
  { id: 'calendar', label: 'Календарь', icon: createIcon('M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z') },
  { id: 'habits', label: 'Привычки', icon: createIcon('M17.657 18.657A8 8 0 016.343 7.343S7 9 9 10c0-2 .5-5 2.986-7C14 5 16.09 5.777 17.656 7.343A7.975 7.975 0 0120 13a7.975 7.975 0 01-2.343 5.657z') },
  { id: 'focus', label: 'Фокус', icon: createIcon('M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z') },
  { id: 'analytics', label: 'Аналитика', icon: createIcon('M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z') }
];

const activeViewComponent = computed(() => {
  switch (activeTab.value) {
    case 'calendar': return CalendarView;
    case 'habits': return HabitsView;
    case 'focus': return FocusView;
    case 'analytics': return AnalyticsView;
    default: return TasksView;
  }
});

function openAddModal() {
  showGlobalAddModal.value = true;
}

function switchToAppTab(tab) {
  viewMode.value = 'app';
  activeTab.value = tab;
}

function onTaskAdded(task) {
  tasksStore.addTask(task);
}

let timer = null;
function updateStatusTime() {
  const d = new Date();
  statusTime.value = `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
}

onMounted(() => {
  updateStatusTime();
  timer = setInterval(updateStatusTime, 1000);
  tasksStore.init();
  habitsStore.init();
});

onUnmounted(() => {
  if (timer) clearInterval(timer);
});
</script>
