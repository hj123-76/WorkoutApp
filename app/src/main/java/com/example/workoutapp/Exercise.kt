package com.example.workoutapp
/**
 * Die Datenklasse Exercise repräsentiert eine einzelne Übung im Workout.
 *
 * Sie wird verwendet, um alle relevanten Informationen zu einer Übung strukturiert abzulegen und zu übergeben.
 * Instanzen dieser Klasse werden z.B. in der ExerciseData-Liste genutzt und an die UI weitergegeben.
 */
data class Exercise(
    val id : Int,
    val name: String,
    val description: String,
    val imageRes: Int
)