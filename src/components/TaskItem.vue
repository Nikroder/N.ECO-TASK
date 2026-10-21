<template>
  <div class="bg-slate-900/80 hover:bg-slate-800/80 border border-slate-800 p-3 rounded-2xl flex items-center justify-between gap-3 transition shadow-sm">
    <div class="flex items-center gap-3 flex-1 min-w-0">
      <input 
        type="checkbox" 
        :checked="task.completed" 
        @change="$emit('toggle', task.id)" 
        class="w-4 h-4 rounded text-purple-600 focus:ring-purple-500 bg-slate-950 border-slate-700 cursor-pointer"
      />
      <div class="flex-1 min-w-0">
        <p :class="['text-xs font-semibold truncate', task.completed ? 'line-through text-slate-500' : 'text-slate-100']">
          {{ task.title }}
        </p>
        <div class="flex items-center gap-2 mt-1 flex-wrap">
          <span class="text-[10px] text-purple-400 font-medium">{{ task.time }}</span>
          <span :class="['text-[9px] px-1.5 py-0.5 rounded border uppercase font-bold', priorityClass]">
            {{ task.priority }}
          </span>
          <span v-if="task.category" class="text-[10px] text-slate-500">
            #{{ task.category }}
          </span>
        </div>
      </div>
    </div>
    
    <button 
      @click="$emit('delete', task.id)" 
      class="text-slate-500 hover:text-red-400 p-1.5 rounded-lg transition"
      title="Удалить задачу"
    >
      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
      </svg>
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  task: {
    type: Object,
    required: true
  }
});

defineEmits(['toggle', 'delete']);

const priorityClass = computed(() => {
  switch (props.task.priority) {
    case 'urgent': return 'border-red-500/50 bg-red-500/10 text-red-400';
    case 'high': return 'border-amber-500/50 bg-amber-500/10 text-amber-400';
    case 'medium': return 'border-blue-500/50 bg-blue-500/10 text-blue-400';
    default: return 'border-slate-700 bg-slate-800 text-slate-400';
  }
});
</script>
