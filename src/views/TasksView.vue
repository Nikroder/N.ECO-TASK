<template>
  <div class="flex-1 flex flex-col p-4 overflow-y-auto no-scrollbar pb-24">
    <!-- Top Bar -->
    <div class="flex items-center justify-between mb-3">
      <div>
        <h2 class="text-2xl font-black text-slate-100 tracking-tight">Задачи</h2>
        <p class="text-xs text-purple-400 font-medium">N.ECO TASK • Сегодня, 21 окт</p>
      </div>
      <button 
        @click="store.toggleKanban" 
        class="p-2 rounded-xl bg-slate-800/80 hover:bg-slate-700 text-purple-400 border border-slate-700/50 transition" 
        title="Переключить Kanban/Список"
      >
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17V7m0 10a2 2 0 01-2 2H5a2 2 0 01-2-2V7a2 2 0 012-2h2a2 2 0 012 2m0 10a2 2 0 002 2h2a2 2 0 002-2M9 7a2 2 0 012-2h2a2 2 0 012 2m0 10V7m0 10a2 2 0 002 2h2a2 2 0 002-2V7a2 2 0 00-2-2h-2a2 2 0 00-2 2"/>
        </svg>
      </button>
    </div>

    <!-- Search Input -->
    <div class="relative mb-3">
      <input 
        v-model="store.searchQuery"
        type="text" 
        placeholder="Поиск задач или #тегов..." 
        class="w-full bg-slate-900 border border-slate-800 rounded-xl px-9 py-2 text-xs text-slate-200 placeholder-slate-500 focus:outline-none focus:border-purple-500 transition shadow-inner"
      />
      <svg class="w-4 h-4 absolute left-3 top-2.5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
      </svg>
    </div>

    <!-- Filter Chips -->
    <div class="flex items-center gap-1.5 overflow-x-auto no-scrollbar mb-4 py-1">
      <button 
        v-for="f in filters" 
        :key="f.id"
        @click="store.setFilter(f.id)"
        :class="[
          'px-3 py-1 rounded-lg text-xs font-medium whitespace-nowrap transition-all shadow-sm',
          store.filter === f.id 
            ? 'bg-purple-600 text-white font-semibold' 
            : 'bg-slate-900 text-slate-400 hover:text-slate-200 border border-slate-800'
        ]"
      >
        {{ f.label }}
      </button>
    </div>

    <!-- List View -->
    <div v-if="!store.isKanban" class="flex flex-col gap-2.5">
      <div v-if="store.filteredTasks.length === 0" class="py-12 text-center text-slate-500 text-xs">
        🎉 Нет активных задач по выбранному фильтру
      </div>
      <TaskItem 
        v-for="task in store.filteredTasks" 
        :key="task.id" 
        :task="task" 
        @toggle="store.toggleTask" 
        @delete="store.deleteTask" 
      />
    </div>

    <!-- Kanban View -->
    <div v-else class="flex gap-3 overflow-x-auto no-scrollbar pb-2">
      <!-- To Do -->
      <div class="w-64 flex-shrink-0 bg-slate-900/60 p-3 rounded-2xl border border-slate-800 flex flex-col gap-2">
        <div class="flex justify-between items-center pb-2 border-b border-slate-800 text-xs font-bold text-slate-300">
          <span>К выполнению</span>
          <span class="bg-purple-500/20 text-purple-300 px-2 py-0.5 rounded-full text-[10px]">{{ todoTasks.length }}</span>
        </div>
        <TaskItem 
          v-for="task in todoTasks" 
          :key="task.id" 
          :task="task" 
          @toggle="store.toggleTask" 
          @delete="store.deleteTask" 
        />
      </div>

      <!-- Done -->
      <div class="w-64 flex-shrink-0 bg-slate-900/60 p-3 rounded-2xl border border-slate-800 flex flex-col gap-2">
        <div class="flex justify-between items-center pb-2 border-b border-slate-800 text-xs font-bold text-slate-300">
          <span>Завершено</span>
          <span class="bg-emerald-500/20 text-emerald-300 px-2 py-0.5 rounded-full text-[10px]">{{ doneTasks.length }}</span>
        </div>
        <TaskItem 
          v-for="task in doneTasks" 
          :key="task.id" 
          :task="task" 
          @toggle="store.toggleTask" 
          @delete="store.deleteTask" 
        />
      </div>
    </div>

    <!-- Floating Action Button -->
    <button 
      @click="showAddModal = true"
      class="absolute right-5 bottom-20 w-12 h-12 rounded-2xl bg-gradient-to-tr from-purple-600 to-indigo-600 hover:from-purple-500 hover:to-indigo-500 text-white shadow-xl shadow-purple-600/40 flex items-center justify-center transition active:scale-95 z-30"
      title="Добавить задачу"
    >
      <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M12 4v16m8-8H4"/>
      </svg>
    </button>

    <!-- Add Task Modal -->
    <AddTaskModal 
      :show="showAddModal" 
      @close="showAddModal = false" 
      @add="store.addTask" 
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useTasksStore } from '../stores/tasks';
import TaskItem from '../components/TaskItem.vue';
import AddTaskModal from '../components/AddTaskModal.vue';

const store = useTasksStore();
const showAddModal = ref(false);

const filters = [
  { id: 'all', label: 'Все' },
  { id: 'today', label: 'Сегодня' },
  { id: 'important', label: 'Важные' },
  { id: 'completed', label: 'Выполненные' }
];

const todoTasks = computed(() => store.tasks.filter(t => !t.completed));
const doneTasks = computed(() => store.tasks.filter(t => t.completed));
</script>
