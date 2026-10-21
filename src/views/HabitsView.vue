<template>
  <div class="flex-1 flex flex-col p-4 overflow-y-auto no-scrollbar pb-24">
    <div class="flex items-center justify-between mb-3">
      <h2 class="text-2xl font-black text-slate-100 tracking-tight">Привычки</h2>
      <span class="text-xs text-amber-400 font-bold flex items-center gap-1">
        🔥 14 дней стрик
      </span>
    </div>

    <!-- Heatmap Card -->
    <div class="bg-slate-900/80 p-3.5 rounded-2xl border border-slate-800 mb-4">
      <div class="flex justify-between items-center mb-2">
        <span class="text-xs font-bold text-slate-300">Карта дисциплины (Heatmap)</span>
        <span class="text-[10px] text-slate-500">16 недель</span>
      </div>
      <div class="grid grid-flow-col grid-rows-5 gap-1 overflow-x-auto no-scrollbar py-1">
        <div 
          v-for="i in 80" 
          :key="i"
          :class="[
            'w-3 h-3 rounded-sm transition-colors',
            i % 3 !== 0 
              ? (i % 2 === 0 ? 'bg-emerald-500' : 'bg-emerald-600/60') 
              : 'bg-slate-800'
          ]"
        />
      </div>
    </div>

    <!-- Habits List -->
    <div class="flex flex-col gap-2.5">
      <div 
        v-for="h in store.habits" 
        :key="h.id"
        class="bg-slate-900/80 border border-slate-800 p-3 rounded-2xl flex items-center justify-between transition shadow-sm"
      >
        <div>
          <p class="text-xs font-bold text-slate-200">{{ h.title }}</p>
          <span class="text-[10px] text-amber-400 font-semibold">🔥 {{ h.streak }} дней подряд</span>
        </div>
        <button 
          @click="store.toggleHabit(h.id)" 
          :class="[
            'w-9 h-9 rounded-full flex items-center justify-center font-bold text-xs transition active:scale-95 shadow',
            h.completed ? 'bg-emerald-500 text-white' : 'bg-slate-800 text-slate-500 border border-slate-700'
          ]"
        >
          ✓
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useHabitsStore } from '../stores/habits';

const store = useHabitsStore();
</script>
