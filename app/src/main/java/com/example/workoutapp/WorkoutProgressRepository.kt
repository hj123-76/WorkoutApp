package com.example.workoutapp

import android.content.Context
import android.content.SharedPreferences

/**
 * Die WorkoutProgressRepository-Klasse verwaltet den Fortschritt des Workouts,
 * insbesondere welche Übungen bereits abgeschlossen wurden.
 * Die Daten werden persistent mit SharedPreferences gespeichert, sodass sie auch nach Beenden der App erhalten bleiben.
 */
class WorkoutProgressRepository(context: Context) {

    // Initialisiert ein SharedPreferences-Objekt.
    // Die Daten werden im Modus MODE_PRIVATE unter dem Namen "workout_progress" gespeichert.
    private val prefs: SharedPreferences = context.getSharedPreferences("workout_progress", Context.MODE_PRIVATE)

    /**
     * Gibt die IDs der bereits abgeschlossenen Übungen als Menge (Set<Int>) zurück.
     * Die IDs werden als Set von Strings in SharedPreferences gespeichert und beim Laden in Integer umgewandelt.
     * Falls noch keine Daten gespeichert wurden, wird ein leeres Set zurückgegeben.
     */
    fun getCompletedExercises(): Set<Int> {
        return prefs.getStringSet("completed_exercises", emptySet()) // Lese das Set von Strings aus SharedPreferences
            ?.mapNotNull { it.toIntOrNull() }   // Wandle jeden String in ein Int um, ignoriere ungültige Werte
            ?.toSet()                          // Wandle die Liste in ein Set um
            ?: emptySet()                      // Falls null, gib ein leeres Set zurück
    }

    /**
     * Fügt eine Übung (übergeben per exerciseId) zur Liste der abgeschlossenen Übungen hinzu.
     * Holt die aktuelle Liste, fügt die neue ID hinzu und speichert das aktualisierte Set wieder als Strings.
     */
    fun addCompletedExercise(exerciseId: Int) {
        val current = getCompletedExercises().toMutableSet()   // Lade das aktuelle Set als veränderbares Set
        current.add(exerciseId)                                // Füge die neue ID hinzu
        // Speichere das Set (umgewandelt in Strings) zurück in die SharedPreferences
        prefs.edit().putStringSet("completed_exercises", current.map { it.toString() }.toSet()).apply()
    }

    /**
     * Löscht den gespeicherten Fortschritt, indem das Feld "completed_exercises" aus den SharedPreferences entfernt wird.
     */
    fun clearCompletedExercises() {
        prefs.edit().remove("completed_exercises").apply()
    }
}