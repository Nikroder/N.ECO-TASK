<template>
  <div class="flex-1 flex flex-col p-4 overflow-y-auto no-scrollbar pb-24">
    <!-- Header -->
    <div class="mb-3">
      <h2 class="text-2xl font-black text-slate-100 tracking-tight">Аналитика</h2>
      <p class="text-xs text-purple-400 font-semibold">N.ECO TASK • Метрики эффективности и баланс жизни</p>
    </div>

    <!-- 1. N.ECO REWIND STORIES BANNER -->
    <div 
      @click="openRewind = true"
      class="p-4 rounded-3xl bg-gradient-to-r from-purple-950 via-indigo-950 to-slate-900 border border-purple-500/40 mb-4 shadow-xl cursor-pointer hover:border-purple-400 transition relative overflow-hidden group"
    >
      <div class="flex items-center gap-3.5 relative z-10">
        <div class="w-12 h-12 rounded-2xl bg-gradient-to-tr from-purple-600 to-indigo-500 flex items-center justify-center text-white shadow-lg shadow-purple-600/30 group-hover:scale-105 transition">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
          </svg>
        </div>
        <div class="flex-1">
          <div class="flex items-center gap-1.5 mb-0.5">
            <span class="text-[10px] font-black text-cyan-400 tracking-wider">N.ECO REWIND</span>
            <span class="text-[9px] font-bold px-1.5 py-0.2 rounded bg-rose-500/20 text-rose-400 border border-rose-500/30">ИТОГИ</span>
          </div>
          <h4 class="text-sm font-bold text-slate-100">⚡ Неоновый Кибер-Стратег</h4>
          <p class="text-[11px] text-slate-400">Нажмите, чтобы открыть интерактивные сторис недели</p>
        </div>
        <svg class="w-5 h-5 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
        </svg>
      </div>
    </div>

    <!-- 2. Score Banner -->
    <div class="p-4 rounded-3xl bg-gradient-to-br from-purple-900/60 to-indigo-900/60 border border-purple-500/30 mb-4 shadow-xl">
      <span class="text-xs text-purple-300 font-semibold">Индекс продуктивности дня</span>
      <div class="flex items-baseline gap-2 mt-1">
        <span class="text-4xl font-black text-white">{{ tasksStore.completionRate }}%</span>
        <span class="text-xs text-purple-200">задач выполнено</span>
      </div>
      <div class="w-full h-2 rounded-full bg-slate-950/80 mt-3 overflow-hidden">
        <div 
          class="h-full bg-gradient-to-r from-purple-400 to-cyan-400 rounded-full transition-all duration-700" 
          :style="{ width: `${tasksStore.completionRate}%` }"
        />
      </div>
    </div>

    <!-- 3. 2x2 Grid -->
    <div class="grid grid-cols-2 gap-2.5 mb-4">
      <div class="p-3.5 rounded-2xl bg-slate-900/80 border border-slate-800 shadow-sm">
        <span class="text-[11px] text-slate-400">Выполнено</span>
        <p class="text-xl font-black text-emerald-400 mt-0.5">{{ tasksStore.completedCount }} задач</p>
      </div>
      <div class="p-3.5 rounded-2xl bg-slate-900/80 border border-slate-800 shadow-sm">
        <span class="text-[11px] text-slate-400">В процессе</span>
        <p class="text-xl font-black text-cyan-400 mt-0.5">{{ tasksStore.activeCount }} задачи</p>
      </div>
      <div class="p-3.5 rounded-2xl bg-slate-900/80 border border-slate-800 shadow-sm">
        <span class="text-[11px] text-slate-400">Время фокуса</span>
        <p class="text-xl font-black text-amber-400 mt-0.5">{{ pomodoroStore.todayFocusMinutes }} мин</p>
      </div>
      <div class="p-3.5 rounded-2xl bg-slate-900/80 border border-slate-800 shadow-sm">
        <span class="text-[11px] text-slate-400">Сессий фокуса</span>
        <p class="text-xl font-black text-pink-400 mt-0.5">{{ pomodoroStore.sessionsCompleted }} сессий</p>
      </div>
    </div>

    <!-- 4. КОЛЕСО ЖИЗНЕННОГО БАЛАНСА (LIFE BALANCE WHEEL / SVG RADAR) -->
    <div class="bg-slate-900/80 border border-slate-800 p-4 rounded-3xl mb-4 shadow-sm">
      <div class="flex items-center gap-2 mb-3">
        <div class="w-8 h-8 rounded-xl bg-purple-500/10 border border-purple-500/20 flex items-center justify-center text-purple-400">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 3.055A9.001 9.001 0 1020.945 13H11V3.055z"/>
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.488 9H15V3.512A9.025 9.025 0 0120.488 9z"/>
          </svg>
        </div>
        <div>
          <h3 class="text-sm font-bold text-slate-200">Колесо жизненного баланса</h3>
          <p class="text-[11px] text-slate-400">Гармония 6 ключевых сфер жизни</p>
        </div>
      </div>

      <!-- SVG Radar Chart -->
      <div class="flex justify-center items-center py-2">
        <svg viewBox="0 0 220 220" class="w-52 h-52">
          <!-- Concentric Hexagons -->
          <polygon points="110,25 183.5,67.5 183.5,152.5 110,195 36.5,152.5 36.5,67.5" fill="none" stroke="#334155" stroke-width="1"/>
          <polygon points="110,46.2 165.1,78.1 165.1,141.8 110,173.7 54.8,141.8 54.8,78.1" fill="none" stroke="#1e293b" stroke-width="1"/>
          <polygon points="110,67.5 146.7,88.7 146.7,131.2 110,152.5 73.2,131.2 73.2,88.7" fill="none" stroke="#1e293b" stroke-width="1"/>
          <polygon points="110,88.7 128.3,99.3 128.3,120.6 110,131.2 91.6,120.6 91.6,99.3" fill="none" stroke="#1e293b" stroke-width="1"/>

          <!-- Spokes -->
          <line x1="110" y1="110" x2="110" y2="25" stroke="#334155" stroke-width="1"/>
          <line x1="110" y1="110" x2="183.5" y2="67.5" stroke="#334155" stroke-width="1"/>
          <line x1="110" y1="110" x2="183.5" y2="152.5" stroke="#334155" stroke-width="1"/>
          <line x1="110" y1="110" x2="110" y2="195" stroke="#334155" stroke-width="1"/>
          <line x1="110" y1="110" x2="36.5" y2="152.5" stroke="#334155" stroke-width="1"/>
          <line x1="110" y1="110" x2="36.5" y2="67.5" stroke="#334155" stroke-width="1"/>

          <!-- Data Polygon (Filled) -->
          <polygon 
            points="110,38 172,74 162,143 110,165 48,138 52,78" 
            fill="rgba(139, 92, 246, 0.35)" 
            stroke="#a78bfa" 
            stroke-width="2.5"
          />

          <!-- Vertex Dots -->
          <circle cx="110" cy="38" r="4" fill="#6366f1"/>
          <circle cx="172" cy="74" r="4" fill="#10b981"/>
          <circle cx="162" cy="143" r="4" fill="#8b5cf6"/>
          <circle cx="110" cy="165" r="4" fill="#f59e0b"/>
          <circle cx="48" cy="138" r="4" fill="#ec4899"/>
          <circle cx="52" cy="78" r="4" fill="#06b6d4"/>
        </svg>
      </div>

      <!-- Spheres Breakdown -->
      <div class="grid grid-cols-2 gap-2 mt-2">
        <div v-for="item in spheres" :key="item.name" class="flex items-center justify-between p-2 rounded-xl bg-slate-950/60 border border-slate-800/60">
          <div class="flex items-center gap-1.5">
            <span class="text-sm">{{ item.emoji }}</span>
            <span class="text-xs text-slate-300 font-medium">{{ item.name }}</span>
          </div>
          <span :class="['text-xs font-bold', item.color]">{{ item.score }}%</span>
        </div>
      </div>
    </div>

    <!-- 5. ХРОНОТИП ПРОДУКТИВНОСТИ -->
    <div class="bg-slate-900/80 border border-slate-800 p-4 rounded-3xl mb-4 shadow-sm">
      <div class="flex items-center gap-2 mb-3">
        <div class="w-8 h-8 rounded-xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center text-amber-400">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 9h-1m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"/>
          </svg>
        </div>
        <div>
          <h3 class="text-sm font-bold text-slate-200">Хронотип и пик фокуса</h3>
          <p class="text-[11px] text-slate-400">Анализ биоритмов продуктивности</p>
        </div>
      </div>

      <div class="p-3 rounded-2xl bg-slate-950/60 border border-amber-500/30 flex items-center gap-3 mb-3">
        <span class="text-2xl">🌅</span>
        <div>
          <h4 class="text-xs font-bold text-slate-100">Твой хронотип: Утренний Снайпер</h4>
          <p class="text-[11px] text-slate-400">42% ваших задач выполняются с 06:00 до 12:00 дня.</p>
        </div>
      </div>

      <div class="grid grid-cols-4 gap-2">
        <div class="p-2 rounded-xl bg-slate-950/60 text-center">
          <p class="text-xs font-black text-amber-400">42%</p>
          <span class="text-[10px] text-slate-300 font-bold block mt-0.5">Утро</span>
          <span class="text-[9px] text-slate-500">06-12</span>
        </div>
        <div class="p-2 rounded-xl bg-slate-950/60 text-center">
          <p class="text-xs font-black text-emerald-400">35%</p>
          <span class="text-[10px] text-slate-300 font-bold block mt-0.5">День</span>
          <span class="text-[9px] text-slate-500">12-18</span>
        </div>
        <div class="p-2 rounded-xl bg-slate-950/60 text-center">
          <p class="text-xs font-black text-purple-400">18%</p>
          <span class="text-[10px] text-slate-300 font-bold block mt-0.5">Вечер</span>
          <span class="text-[9px] text-slate-500">18-23</span>
        </div>
        <div class="p-2 rounded-xl bg-slate-950/60 text-center">
          <p class="text-xs font-black text-cyan-400">5%</p>
          <span class="text-[10px] text-slate-300 font-bold block mt-0.5">Ночь</span>
          <span class="text-[9px] text-slate-500">23-06</span>
        </div>
      </div>
    </div>

    <!-- 6. Weekly Activity Bars -->
    <div class="bg-slate-900/80 border border-slate-800 p-4 rounded-3xl mb-4">
      <span class="text-xs font-bold text-slate-300 block mb-3">Недельная динамика</span>
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

    <!-- 7. GitHub Auto-Update Card -->
    <div class="p-4 rounded-3xl bg-slate-900/90 border border-purple-500/20 shadow-lg">
      <div class="flex items-center gap-2 mb-1">
        <svg class="w-4 h-4 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"/>
        </svg>
        <h4 class="text-xs font-bold text-slate-200">О приложении N.ECO TASK</h4>
      </div>
      <p class="text-[11px] text-slate-400 mb-3">Текущая версия: v1.0.4 (Spotify Wrapped & Life Balance Wheel)</p>
      <button 
        @click="checkUpdates" 
        :disabled="isChecking"
        class="w-full py-2.5 rounded-xl bg-purple-600 hover:bg-purple-500 text-white text-xs font-bold transition shadow-md shadow-purple-600/30"
      >
        {{ isChecking ? 'Проверка...' : updateStatusText }}
      </button>
    </div>

    <!-- N.ECO REWIND STORIES MODAL -->
    <div 
      v-if="openRewind" 
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/90 backdrop-blur-md p-4"
    >
      <div class="relative w-full max-w-sm h-[560px] rounded-[36px] bg-gradient-to-b from-purple-950 via-slate-950 to-slate-900 border border-purple-500/40 p-6 flex flex-col justify-between shadow-2xl">
        <!-- Progress Bars (5 segments) -->
        <div class="flex items-center gap-1.5 w-full">
          <div 
            v-for="i in 5" 
            :key="i" 
            :class="['h-1 rounded-full flex-1 transition-all', (i - 1) <= storyIndex ? 'bg-purple-400' : 'bg-white/20']"
          />
        </div>

        <!-- Top row: close -->
        <div class="flex items-center justify-between text-xs mt-2">
          <span class="font-black text-cyan-400 tracking-wider">N.ECO REWIND</span>
          <button @click="openRewind = false" class="p-1 rounded-full bg-white/10 hover:bg-white/20 text-white">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
          </button>
        </div>

        <!-- Slide content -->
        <div class="my-auto text-center px-4">
          <!-- Slide 0: Title -->
          <div v-if="storyIndex === 0">
            <div class="w-20 h-20 mx-auto rounded-full bg-gradient-to-tr from-purple-600 to-indigo-500 flex items-center justify-center text-4xl mb-4 shadow-xl shadow-purple-600/50">
              🏆
            </div>
            <span class="text-[11px] font-black tracking-widest text-amber-400 uppercase">Твой титул недели</span>
            <h3 class="text-2xl font-black text-white mt-1">⚡ Неоновый Кибер-Стратег</h3>
            <p class="text-xs text-slate-300 mt-3 leading-relaxed">
              Твоя продуктивность бьет рекорды! Ты эффективно совмещаешь фокус над сложными проектами и поддержку здоровых привычек.
            </p>
          </div>

          <!-- Slide 1: Focus -->
          <div v-if="storyIndex === 1">
            <div class="w-20 h-20 mx-auto rounded-full bg-gradient-to-tr from-cyan-500 to-blue-600 flex items-center justify-center text-4xl mb-4 shadow-xl shadow-cyan-500/50">
              ⏱️
            </div>
            <span class="text-[11px] font-black tracking-widest text-cyan-400 uppercase">Зона гиперфокуса</span>
            <h3 class="text-3xl font-black text-white mt-1">{{ pomodoroStore.todayFocusMinutes || 75 }} минут</h3>
            <p class="text-xs text-slate-300 mt-3 leading-relaxed">
              Чистой концентрации без уведомлений и прокрастинации. Это время, в которое рождаются великие результаты!
            </p>
          </div>

          <!-- Slide 2: Dominant Sphere -->
          <div v-if="storyIndex === 2">
            <div class="w-20 h-20 mx-auto rounded-full bg-gradient-to-tr from-indigo-500 to-purple-600 flex items-center justify-center text-4xl mb-4 shadow-xl">
              💼
            </div>
            <span class="text-[11px] font-black tracking-widest text-indigo-400 uppercase">Сфера лидерства</span>
            <h3 class="text-2xl font-black text-white mt-1">Карьера & Проекты</h3>
            <p class="text-xs text-slate-300 mt-3 leading-relaxed">
              Колесо жизненного баланса показывает 85% активности в этой области. Ты уверенно закрываешь ключевые вехи!
            </p>
          </div>

          <!-- Slide 3: Habits -->
          <div v-if="storyIndex === 3">
            <div class="w-20 h-20 mx-auto rounded-full bg-gradient-to-tr from-amber-500 to-rose-600 flex items-center justify-center text-4xl mb-4 shadow-xl">
              🔥
            </div>
            <span class="text-[11px] font-black tracking-widest text-amber-400 uppercase">Огонь дисциплины</span>
            <h3 class="text-2xl font-black text-white mt-1">Серия 5 дней подряд!</h3>
            <p class="text-xs text-slate-300 mt-3 leading-relaxed">
              Регулярность важнее разовой мотивации. Твоя привычка крепнет с каждым днем!
            </p>
          </div>

          <!-- Slide 4: Final Summary -->
          <div v-if="storyIndex === 4">
            <h3 class="text-xl font-black text-white mb-2">Отличная неделя, командир!</h3>
            <div class="p-4 rounded-2xl bg-slate-900/80 border border-purple-500/30 text-left space-y-2 mb-4 text-xs">
              <div class="flex justify-between">
                <span class="text-slate-400">Титул:</span>
                <span class="font-bold text-purple-300">⚡ Кибер-Стратег</span>
              </div>
              <div class="flex justify-between">
                <span class="text-slate-400">Выполнено задач:</span>
                <span class="font-bold text-emerald-400">{{ tasksStore.completedCount }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-slate-400">Фокус:</span>
                <span class="font-bold text-cyan-400">{{ pomodoroStore.todayFocusMinutes || 75 }} мин</span>
              </div>
            </div>
            <button 
              @click="openRewind = false"
              class="w-full py-3 rounded-xl bg-gradient-to-r from-purple-600 to-indigo-600 text-white font-bold text-xs shadow-lg shadow-purple-600/40"
            >
              Сохранить и продолжить
            </button>
          </div>
        </div>

        <!-- Bottom Navigation -->
        <div class="flex items-center justify-between mt-4">
          <button 
            @click="storyIndex > 0 ? storyIndex-- : null" 
            :disabled="storyIndex === 0"
            class="px-3.5 py-1.5 rounded-xl bg-white/10 hover:bg-white/20 disabled:opacity-30 text-white text-xs font-semibold"
          >
            Назад
          </button>
          <button 
            @click="storyIndex < 4 ? storyIndex++ : openRewind = false" 
            class="px-4 py-1.5 rounded-xl bg-purple-600 hover:bg-purple-500 text-white text-xs font-bold"
          >
            {{ storyIndex < 4 ? 'Далее →' : 'Готово' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useTasksStore } from '../stores/tasks';
import { usePomodoroStore } from '../stores/pomodoro';

const tasksStore = useTasksStore();
const pomodoroStore = usePomodoroStore();

const openRewind = ref(false);
const storyIndex = ref(0);
const isChecking = ref(false);
const updateStatusText = ref('Проверить обновления на GitHub');

const spheres = [
  { name: 'Карьера', emoji: '💼', score: 85, color: 'text-indigo-400' },
  { name: 'Здоровье', emoji: '🏋️', score: 70, color: 'text-emerald-400' },
  { name: 'Развитие', emoji: '📚', score: 75, color: 'text-purple-400' },
  { name: 'Финансы', emoji: '💰', score: 60, color: 'text-amber-400' },
  { name: 'Окружение', emoji: '🤝', score: 65, color: 'text-pink-400' },
  { name: 'Отдых', emoji: '🧘', score: 80, color: 'text-cyan-400' },
];

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
      updateStatusText.value = 'У вас установлена последняя версия ✓';
    }
  } catch (e) {
    updateStatusText.value = 'У вас установлена последняя версия ✓';
  } finally {
    isChecking.value = false;
  }
}
</script>
