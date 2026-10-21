package com.omniplan.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Cyber Slate & Modern Indigo / Violet Palette (Matching Web Simulator)
val Slate950 = Color(0xFF0A0E1A) // Deep midnight slate background
val Slate900 = Color(0xFF0F172A) // Elevated screen background
val Slate850 = Color(0xFF151E32) // Sleek card background
val Slate800 = Color(0xFF1E293B) // Pill, chip, and active element background
val Slate700 = Color(0xFF334155) // Borders, outlines, dividers
val Slate600 = Color(0xFF475569) // Inactive icons, subtle hints
val Slate400 = Color(0xFF94A3B8) // Secondary typography
val Slate200 = Color(0xFFE2E8F0) // High contrast typography
val Slate100 = Color(0xFFF8FAFC) // Crisp white typography

// Neon & Brand Accents
val PurplePrimary = Color(0xFF8B5CF6) // Tailwind violet-500
val PurpleDark = Color(0xFF7C3AED)    // Tailwind violet-600
val PurpleLight = Color(0xFFA78BFA)   // Tailwind violet-400
val IndigoAccent = Color(0xFF6366F1)  // Tailwind indigo-500
val IndigoDark = Color(0xFF4F46E5)    // Tailwind indigo-600
val CyanAccent = Color(0xFF06B6D4)    // Tailwind cyan-500
val EmeraldAccent = Color(0xFF10B981) // Tailwind emerald-500
val AmberAccent = Color(0xFFF59E0B)   // Tailwind amber-500
val RoseAccent = Color(0xFFF43F5E)    // Tailwind rose-500

// Gradients
val PrimaryButtonGradient = Brush.horizontalGradient(listOf(PurpleDark, IndigoAccent))
val FabGradient = Brush.linearGradient(listOf(PurplePrimary, IndigoAccent))
val CardBorderGradient = Brush.linearGradient(listOf(Slate700, Slate800))
val AccentPillGradient = Brush.horizontalGradient(listOf(PurpleDark, PurplePrimary))

// Light Theme Fallbacks
val BackgroundLight = Color(0xFFF8FAFC)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariantLight = Color(0xFFF1F5F9)
val OutlineLight = Color(0xFFCBD5E1)

// Priority Accent Colors
val PriorityUrgent = Color(0xFFEF4444)
val PriorityHigh = Color(0xFFF97316)
val PriorityMedium = Color(0xFF3B82F6)
val PriorityLow = Color(0xFF10B981)
val PriorityNone = Color(0xFF64748B)

