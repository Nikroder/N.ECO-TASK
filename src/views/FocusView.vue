<template>
  <div class="flex-1 flex flex-col items-center justify-between p-4 pb-24 text-center">
    <div>
      <h2 class="text-2xl font-black text-slate-100 tracking-tight">Глубокий фокус</h2>
      <p class="text-xs text-slate-400 mt-0.5">Техника Pomodoro и фоновые звуки</p>
    </div>

    <!-- Mode Selector -->
    <div class="flex items-center justify-center gap-1 bg-slate-900/90 p-1 rounded-xl border border-slate-800 text-xs">
      <button 
        @click="store.setMode('work')" 
        :class="['px-3 py-1 rounded-lg transition', store.mode === 'work' ? 'bg-purple-600 text-white font-medium shadow-sm' : 'text-slate-400 hover:text-white']"
      >
        Фокус (25м)
      </button>
      <button 
        @click="store.setMode('short')" 
        :class="['px-3 py-1 rounded-lg transition', store.mode === 'short' ? 'bg-emerald-600 text-white font-medium shadow-sm' : 'text-slate-400 hover:text-white']"
      >
        Отдых (5м)
      </button>
      <button 
        @click="store.setMode('long')" 
        :class="['px-3 py-1 rounded-lg transition', store.mode === 'long' ? 'bg-indigo-600 text-white font-medium shadow-sm' : 'text-slate-400 hover:text-white']"
      >
        Пауза (15м)
      </button>
    </div>

    <!-- Circular Timer -->
    <div class="relative w-56 h-56 flex items-center justify-center my-4">
      <svg class="w-full h-full transform -rotate-90" viewBox="0 0 100 100">
        <circle cx="50" cy="50" r="42" stroke="currentColor" stroke-width="6" class="text-slate-900" fill="transparent" />
        <circle 
          cx="50" cy="50" r="42" 
          stroke="currentColor" 
          stroke-width="6" 
          :class="[store.mode === 'work' ? 'text-purple-500' : 'text-emerald-500', 'transition-all duration-1000']"
          stroke-dasharray="264" 
          :stroke-dashoffset="dashOffset" 
          stroke-linecap="round" 
          fill="transparent" 
        />
      </svg>
      <div class="absolute flex flex-col items-center">
        <span class="text-4xl font-black tracking-tighter text-slate-100">{{ store.formattedTime }}</span>
        <span class="text-[11px] text-purple-400 font-medium tracking-wide uppercase mt-1">
          {{ store.isRunning ? 'Идет фокус...' : 'Пауза' }}
        </span>
      </div>
    </div>

    <!-- Timer Controls -->
    <div class="flex items-center gap-4">
      <button 
        @click="store.reset" 
        class="w-12 h-12 rounded-full bg-slate-900 hover:bg-slate-800 border border-slate-800 text-slate-300 flex items-center justify-center transition shadow"
        title="Сбросить таймер"
      >
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"/>
        </svg>
      </button>
      <button 
        @click="store.toggle" 
        class="w-16 h-16 rounded-full bg-purple-600 hover:bg-purple-500 text-white shadow-xl shadow-purple-600/40 flex items-center justify-center transition active:scale-95"
      >
        <svg v-if="!store.isRunning" class="w-7 h-7" fill="currentColor" viewBox="0 0 24 24"><path d="M8 5v14l11-7z"/></svg>
        <svg v-else class="w-7 h-7" fill="currentColor" viewBox="0 0 24 24"><path d="M6 19h4V5H6v14zm8-14v14h4V5h-4z"/></svg>
      </button>
    </div>

    <!-- Ambient Sound Toggle -->
    <div class="w-full bg-slate-900/60 p-2.5 rounded-2xl border border-slate-800 flex items-center justify-between text-xs">
      <span class="text-slate-400 flex items-center gap-1.5">
        🌧 Шум дождя
      </span>
      <button 
        @click="store.toggleAmbient" 
        :class="[
          'px-3 py-1 rounded-lg text-xs font-medium transition',
          store.ambientSound ? 'bg-purple-600 text-white animate-pulse' : 'bg-slate-800 text-purple-300'
        ]"
      >
        {{ store.ambientSound ? 'Выключить' : 'Включить звук' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { usePomodoroStore } from '../stores/pomodoro';

const store = usePomodoroStore();

const dashOffset = computed(() => {
  // 264 is the circumference of circle r=42
  return 264 - (264 * store.progress) / 100;
});
</script>
