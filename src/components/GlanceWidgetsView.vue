<template>
  <div class="flex-1 flex flex-col p-4 relative bg-cover bg-center overflow-y-auto no-scrollbar" style="background-image: linear-gradient(rgba(15, 23, 42, 0.75), rgba(15, 23, 42, 0.85)), url('https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80')">
    
    <!-- Android Desktop Clock Widget -->
    <div class="text-center my-4">
      <h3 class="text-5xl font-light tracking-tight text-white/90">{{ clockTime }}</h3>
      <p class="text-xs text-white/70 font-medium">Среда, 21 октября</p>
    </div>

    <!-- 4x3 GLANCE INTERACTIVE TODAY TASKS WIDGET -->
    <div class="bg-slate-900/90 backdrop-blur-xl border border-slate-700/80 rounded-3xl p-3.5 shadow-2xl mb-4">
      <div class="flex items-center justify-between pb-2 border-b border-slate-800 mb-2.5">
        <div>
          <span class="text-[11px] font-bold text-purple-400">N.ECO TASK • 21 окт</span>
          <p class="text-xs font-semibold text-white">Выполнено {{ tasksStore.completedCount }} из {{ tasksStore.tasks.length }}</p>
        </div>
        <button @click="$emit('open-add')" class="w-7 h-7 rounded-full bg-purple-600 text-white font-bold flex items-center justify-center text-sm shadow hover:bg-purple-500 transition">+</button>
      </div>

      <!-- Items inside widget -->
      <div class="flex flex-col gap-1.5">
        <div 
          v-for="task in tasksStore.tasks.slice(0, 4)" 
          :key="task.id"
          class="bg-slate-800/80 p-2 rounded-xl flex items-center gap-2 border border-slate-700/40"
        >
          <input 
            type="checkbox" 
            :checked="task.completed" 
            @change="tasksStore.toggleTask(task.id)" 
            class="w-3.5 h-3.5 rounded text-purple-600 bg-slate-900 border-slate-700 cursor-pointer"
          />
          <span :class="['text-xs truncate flex-1', task.completed ? 'line-through text-slate-500' : 'text-slate-200']">
            {{ task.title }}
          </span>
        </div>
      </div>
    </div>

    <!-- 4x1 GLANCE QUICK ACTION BAR WIDGET -->
    <div class="bg-slate-900/90 backdrop-blur-xl border border-slate-700/80 rounded-2xl p-2 shadow-xl flex items-center justify-between gap-1.5">
      <button @click="$emit('open-add')" class="flex-1 py-2 bg-purple-600 hover:bg-purple-500 text-white rounded-xl text-[11px] font-semibold text-center transition">
        ＋ Задача
      </button>
      <button @click="$emit('switch-tab', 'calendar')" class="flex-1 py-2 bg-slate-800 hover:bg-slate-700 text-slate-200 rounded-xl text-[11px] font-medium text-center transition">
        📅 План
      </button>
      <button @click="$emit('switch-tab', 'focus')" class="flex-1 py-2 bg-slate-800 hover:bg-slate-700 text-slate-200 rounded-xl text-[11px] font-medium text-center transition">
        ⏱ Фокус
      </button>
      <button @click="$emit('switch-tab', 'habits')" class="flex-1 py-2 bg-slate-800 hover:bg-slate-700 text-slate-200 rounded-xl text-[11px] font-medium text-center transition">
        🔥 Стрик
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useTasksStore } from '../stores/tasks';

defineEmits(['open-add', 'switch-tab']);

const tasksStore = useTasksStore();
const clockTime = ref('12:30');

let timer = null;
function updateClock() {
  const d = new Date();
  clockTime.value = `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
}

onMounted(() => {
  updateClock();
  timer = setInterval(updateClock, 1000);
});

onUnmounted(() => {
  if (timer) clearInterval(timer);
});
</script>
