<template>
  <div class="flex-1 flex flex-col p-4 overflow-y-auto no-scrollbar pb-24">
    <div class="flex items-center justify-between mb-3">
      <h2 class="text-2xl font-black text-slate-100 tracking-tight">Календарь</h2>
      <span class="text-xs px-2.5 py-1 rounded-lg bg-purple-500/10 text-purple-400 border border-purple-500/20 font-semibold">Октябрь 2026</span>
    </div>

    <!-- Horizontal Days Strip -->
    <div class="flex items-center justify-between gap-1 bg-slate-900/60 p-1.5 rounded-2xl border border-slate-800 mb-4">
      <div 
        v-for="day in days" 
        :key="day.date"
        @click="selectedDay = day.date"
        :class="[
          'flex-1 text-center py-2 rounded-xl text-xs cursor-pointer transition-all',
          selectedDay === day.date 
            ? 'bg-purple-600 text-white font-bold shadow-md shadow-purple-600/30' 
            : 'text-slate-400 hover:bg-slate-800'
        ]"
      >
        <span class="block text-[10px]">{{ day.name }}</span>
        <span class="font-bold text-sm">{{ day.date }}</span>
      </div>
    </div>

    <h3 class="text-xs font-bold text-slate-400 uppercase tracking-wider mb-2">Тайм-блокинг на сегодня</h3>
    <div class="flex flex-col gap-2.5">
      <div 
        v-for="slot in timeSlots" 
        :key="slot.id"
        :class="['p-3 rounded-2xl border-l-4 border border-slate-800 transition', slot.colorClass]"
      >
        <div class="flex justify-between items-center text-xs font-semibold">
          <span>{{ slot.start }} - {{ slot.end }}</span>
          <span class="text-[10px] px-2 py-0.5 rounded bg-black/20">{{ slot.tag }}</span>
        </div>
        <p class="text-sm font-bold text-slate-200 mt-1">{{ slot.title }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const selectedDay = ref(21);

const days = [
  { name: 'Пн', date: 19 },
  { name: 'Вт', date: 20 },
  { name: 'Ср', date: 21 },
  { name: 'Чт', date: 22 },
  { name: 'Пт', date: 23 },
  { name: 'Сб', date: 24 }
];

const timeSlots = ref([
  { id: 1, title: 'Разработка архитектуры N.ECO TASK (Rust Core)', start: '09:00', end: '10:30', tag: 'Глубокая работа', colorClass: 'bg-indigo-500/10 border-indigo-500 text-indigo-300' },
  { id: 2, title: 'Синхронизация с командой проекта', start: '11:00', end: '12:00', tag: 'Созвон', colorClass: 'bg-emerald-500/10 border-emerald-500 text-emerald-300' },
  { id: 3, title: 'Релиз сборки v1.0.0 на GitHub', start: '15:30', end: '17:00', tag: 'Релиз', colorClass: 'bg-amber-500/10 border-amber-500 text-amber-300' }
]);
</script>
