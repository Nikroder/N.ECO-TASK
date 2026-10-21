<template>
  <div v-if="show" class="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
    <div class="bg-slate-900 border border-slate-800 rounded-3xl w-full max-w-sm p-5 shadow-2xl flex flex-col gap-3">
      <div class="flex justify-between items-center">
        <h3 class="font-bold text-base text-white">Новая задача</h3>
        <button @click="$emit('close')" class="text-slate-400 hover:text-white text-lg">&times;</button>
      </div>

      <div>
        <label class="text-[11px] text-slate-400 block mb-1">Умный ввод (NLP):</label>
        <input 
          v-model="rawInput" 
          ref="inputRef"
          type="text" 
          placeholder="Завтра в 15:30 созвон !срочно #работа" 
          class="w-full bg-slate-800/80 border border-slate-700 rounded-xl p-2.5 text-xs text-white placeholder-slate-500 focus:outline-none focus:border-purple-500 transition"
          @keyup.enter="submit"
        />
      </div>

      <!-- Live NLP Badges -->
      <div class="flex items-center flex-wrap gap-1.5 min-h-[24px]">
        <span v-if="nlp.date" class="text-[10px] bg-purple-500/20 text-purple-300 border border-purple-500/30 px-2 py-0.5 rounded-lg">
          📅 {{ nlp.date }}
        </span>
        <span v-if="nlp.time" class="text-[10px] bg-indigo-500/20 text-indigo-300 border border-indigo-500/30 px-2 py-0.5 rounded-lg">
          ⏰ {{ nlp.time }}
        </span>
        <span v-if="nlp.priority" class="text-[10px] bg-red-500/20 text-red-300 border border-red-500/30 px-2 py-0.5 rounded-lg">
          🔥 {{ nlp.priorityText }}
        </span>
        <span v-for="tag in nlp.tags" :key="tag" class="text-[10px] bg-slate-700 text-slate-300 px-2 py-0.5 rounded-lg">
          #{{ tag }}
        </span>
      </div>

      <div class="flex items-center justify-between pt-2 border-t border-slate-800 mt-2">
        <button @click="$emit('close')" class="px-4 py-2 rounded-xl text-xs text-slate-400 hover:text-white">Отмена</button>
        <button 
          @click="submit" 
          :disabled="!rawInput.trim()"
          class="px-5 py-2 rounded-xl text-xs font-bold bg-purple-600 hover:bg-purple-500 disabled:opacity-50 text-white shadow-lg shadow-purple-600/30 transition"
        >
          Создать
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue';

const props = defineProps({
  show: Boolean
});

const emit = defineEmits(['close', 'add']);

const rawInput = ref('');
const inputRef = ref(null);

watch(() => props.show, (val) => {
  if (val) {
    rawInput.value = '';
    nextTick(() => inputRef.value?.focus());
  }
});

const nlp = computed(() => {
  const text = rawInput.value;
  let date = null;
  let time = null;
  let priority = 'medium';
  let priorityText = null;
  const tags = [];

  if (text.match(/завтра/i)) date = 'Завтра';
  else if (text.match(/сегодня/i)) date = 'Сегодня';

  const timeMatch = text.match(/\b(\d{1,2}:\d{2})\b/);
  if (timeMatch) time = timeMatch[1];

  if (text.match(/!срочно|!urgent/i)) {
    priority = 'urgent';
    priorityText = 'Срочно';
  } else if (text.match(/!высокий|!важно/i)) {
    priority = 'high';
    priorityText = 'Высокий';
  }

  const tagMatches = text.matchAll(/#([a-zA-Zа-яА-Я0-9_]+)/g);
  for (const m of tagMatches) {
    tags.push(m[1]);
  }

  return { date, time, priority, priorityText, tags };
});

function submit() {
  const val = rawInput.value.trim();
  if (!val) return;

  const cleanTitle = val
    .replace(/!срочно|!высокий|!важно|#\w+/gi, '')
    .trim();

  emit('add', {
    title: cleanTitle || val,
    time: nlp.value.date ? `${nlp.value.date}${nlp.value.time ? ', ' + nlp.value.time : ''}` : 'Сегодня, 18:00',
    priority: nlp.value.priority,
    category: nlp.value.tags[0] || 'проект'
  });

  emit('close');
}
</script>
