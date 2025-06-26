package com.example.workoutapp

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ProgressBar

/**
 * MainActivity verwaltet den gesamten Ablauf des Workouts:
 * - Starten, Stoppen und Pausieren des Workouts
 * - Fortschrittsanzeige
 * - Übergänge zwischen Übungen und Pausen
 * - Verwaltung des aktuellen Status
 */
class MainActivity : AppCompatActivity() {

    // UI-Elemente für die Bedienung und Anzeige
    private lateinit var startButton: Button                  // Button zum Starten/Fortsetzen des Workouts
    private lateinit var cancelExerciseButton: Button         // Button zum Pausieren/Fortsetzen einer Übung
    private lateinit var stopWorkoutButton: Button            // Button zum Stoppen des gesamten Workouts (zurück zur Startseite)
    private lateinit var exerciseImage: ImageView             // Bild zur aktuellen Übung
    private lateinit var exerciseText: TextView               // Name der aktuellen Übung
    private lateinit var exerciseDesc: TextView               // Beschreibung der aktuellen Übung
    private lateinit var countdownText: TextView              // Countdown-Anzeige für Übung/Pause
    private lateinit var progressBar: ProgressBar             // Fortschrittsbalken für das Workout
    private lateinit var progressText: TextView               // Fortschritt als Text

    // Variablen zur Steuerung des Workouts
    private var currentExerciseIndex = 0                      // Index der aktuellen Übung in der Liste
    private var isWorkoutRunning = false                      // Gibt an, ob ein Workout läuft
    private var isPause = false                               // Gibt an, ob gerade eine Pause läuft
    private var isExerciseAborted = false                     // Gibt an, ob eine Übung abgebrochen wurde
    private var isExercisePaused = false                      // Gibt an, ob eine Übung pausiert ist
    private var countDownTimer: CountDownTimer? = null        // Timer für Übungs-/Pausen-Countdown
    private var exerciseTimeLeftSeconds = 20                  // Restzeit der aktuellen Übung in Sekunden

    private lateinit var progressRepository: WorkoutProgressRepository  // Speichert den Fortschritt (z.B. absolvierte Übungen)

    /**
     * Wird beim Starten der Activity aufgerufen.
     * Initialisiert alle UI-Elemente und setzt die Button-Listener.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI-Elemente mit XML-IDs verbinden
        startButton = findViewById(R.id.startWorkoutButton)
        cancelExerciseButton = findViewById(R.id.cancelExerciseButton)
        stopWorkoutButton = findViewById(R.id.stopWorkoutButton)
        exerciseImage = findViewById(R.id.exerciseImage)
        exerciseText = findViewById(R.id.exerciseText)
        exerciseDesc = findViewById(R.id.exerciseDesc)
        countdownText = findViewById(R.id.countdownText)
        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)

        // Fortschrittsverwaltung initialisieren
        progressRepository = WorkoutProgressRepository(this)
        updateProgressUI() // Fortschrittsanzeige initialisieren

        // Start-Button: Startet das Workout oder setzt es fort
        startButton.setOnClickListener {
            val completed = progressRepository.getCompletedExercises()
            val allDone = completed.size == ExerciseData.exercises.size

            if (allDone) {
                // Wenn alle Übungen erledigt sind: Fortschritt zurücksetzen und Workout von vorne beginnen
                progressRepository.clearCompletedExercises()
                updateProgressUI()
                currentExerciseIndex = 0
            } else {
                // Ansonsten: zur nächsten noch nicht erledigten Übung springen
                val nextIndex = ExerciseData.exercises.indexOfFirst { it.id !in completed }
                currentExerciseIndex = if (nextIndex == -1) 0 else nextIndex
            }
            isWorkoutRunning = true
            showExercise(currentExerciseIndex)
        }

        // Button zum Pausieren/Fortsetzen einer Übung
        cancelExerciseButton.setOnClickListener {
            handlePauseOrResumeExercise()
        }

        // Workout stoppen und zur Startseite zurückkehren
        stopWorkoutButton.setOnClickListener {
            stopWorkoutAndReturnToStart()
        }
    }

    /**
     * Zeigt die Übung mit dem gegebenen Index an.
     * Optional kann eine Restzeit für die Übung übergeben werden (z.B. beim Fortsetzen).
     */
    private fun showExercise(index: Int, resumeTime: Int? = null) {
        isPause = false
        isExerciseAborted = false
        isExercisePaused = false
        countDownTimer?.cancel()
        if (index < ExerciseData.exercises.size) {
            // Hole die aktuelle Übung aus der Datenliste
            val exercise = ExerciseData.exercises[index]
            exerciseImage.setImageResource(exercise.imageRes)
            exerciseText.text = exercise.name
            exerciseDesc.text = exercise.description

            // UI-Elemente ein-/ausblenden
            exerciseImage.visibility = ImageView.VISIBLE
            exerciseText.visibility = TextView.VISIBLE
            exerciseDesc.visibility = TextView.VISIBLE
            startButton.visibility = Button.GONE
            cancelExerciseButton.visibility = Button.VISIBLE
            cancelExerciseButton.text = "Übung abbrechen"
            stopWorkoutButton.visibility = Button.GONE // Während der Übung nicht sichtbar
            countdownText.visibility = TextView.VISIBLE

            // Optional: akustisches Signal zu Beginn der Übung
            SoundPlayer.play(this, R.raw.uebungston)

            // Restzeit setzen (entweder übergeben oder Standardwert 20 Sekunden)
            exerciseTimeLeftSeconds = resumeTime ?: 20
            startExerciseCountdown(exerciseTimeLeftSeconds)
        } else {
            finishWorkout()
        }
    }

    /**
     * Startet den Countdown für die aktuelle Übung.
     * Bei Ablauf: Markiert Übung als erledigt, prüft ob Workout zu Ende ist und startet ggf. Pause.
     */
    private fun startExerciseCountdown(seconds: Int) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseTimeLeftSeconds = (millisUntilFinished / 1000).toInt()
                countdownText.text = "Übung: $exerciseTimeLeftSeconds"
            }
            override fun onFinish() {
                countdownText.text = ""
                if (!isExerciseAborted && !isExercisePaused) {
                    // Fortschritt speichern
                    progressRepository.addCompletedExercise(ExerciseData.exercises[currentExerciseIndex].id)
                    updateProgressUI()
                    // Prüfen, ob dies die letzte Übung war
                    if (currentExerciseIndex == ExerciseData.exercises.size - 1) {
                        // Letzte Übung: keine Pause, kein Pausenton, direkt zur Startseite
                        finishWorkout()
                    } else {
                        // Noch nicht die letzte Übung: Normale Pause mit Pausenton
                        SoundPlayer.play(this@MainActivity, R.raw.pausenton)
                        startPause()
                    }
                }
            }
        }.start()
    }

    /**
     * Reagiert auf den "Übung abbrechen"/"Übung fortsetzen"-Button.
     * Pausiert oder setzt die aktuelle Übung fort.
     */
    private fun handlePauseOrResumeExercise() {
        if (!isExercisePaused) {
            // Setzt die Übung in den Pausenmodus (Timer anhalten, Button-Text ändern)
            isExercisePaused = true
            countDownTimer?.cancel()
            countdownText.text = "Übung pausiert"
            cancelExerciseButton.text = "Übung fortsetzen"
            startButton.visibility = Button.GONE
        } else {
            // Fortsetzen der Übung an der Stelle, an der sie pausiert wurde
            isExercisePaused = false
            cancelExerciseButton.text = "Übung abbrechen"
            startExerciseCountdown(exerciseTimeLeftSeconds)
        }
    }

    /**
     * Startet die Pause zwischen zwei Übungen.
     * Während der Pause werden die Übungsanzeigen versteckt und der Stop-Button eingeblendet.
     * Nach Pausenende wird die nächste Übung angezeigt.
     */
    private fun startPause() {
        isPause = true
        exerciseImage.visibility = ImageView.GONE
        exerciseText.visibility = TextView.GONE
        exerciseDesc.visibility = TextView.GONE
        cancelExerciseButton.visibility = Button.GONE
        stopWorkoutButton.visibility = Button.VISIBLE // Während der Pause sichtbar

        countdownText.visibility = TextView.VISIBLE

        // Countdown für Pause (z.B. 10 Sekunden, kann beliebig angepasst werden)
        startCountdown(10) {
            // Akustisches Signal am Ende der Pause
            SoundPlayer.play(this, R.raw.pausenton)
            stopWorkoutButton.visibility = Button.GONE // Nach der Pause wieder ausblenden
            currentExerciseIndex++
            showExercise(currentExerciseIndex)
        }
    }

    /**
     * Startet einen allgemeinen Countdown, z.B. für Pausen.
     * Führt nach Ablauf onFinish() aus.
     */
    private fun startCountdown(seconds: Int, onFinish: () -> Unit) {
        countDownTimer?.cancel()
        countdownText.text = "Pause: $seconds"
        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                countdownText.text = "Pause: ${millisUntilFinished / 1000}"
            }
            override fun onFinish() {
                countdownText.text = ""
                onFinish()
            }
        }.start()
    }

    /**
     * Aktualisiert die Fortschrittsanzeige (Text und Balken) anhand erledigter Übungen.
     */
    private fun updateProgressUI() {
        val completed = progressRepository.getCompletedExercises().size
        val total = ExerciseData.exercises.size
        val progressPercent = if (total > 0) (completed * 100.0 / total).toInt() else 0
        progressBar.max = 100
        progressBar.progress = progressPercent
        progressText.text = "Fortschritt: $completed/$total"
    }

    /**
     * Wird aufgerufen, wenn das Workout abgeschlossen ist (nach letzter Übung).
     * Blendet alle Übungsanzeigen aus und zeigt den "Workout erneut starten"-Button.
     * Fortschritt bleibt erhalten, bis der Nutzer erneut startet.
     */
    private fun finishWorkout() {
        isWorkoutRunning = false
        countdownText.visibility = TextView.GONE
        exerciseImage.visibility = ImageView.GONE
        exerciseText.visibility = TextView.GONE
        exerciseDesc.visibility = TextView.GONE
        cancelExerciseButton.visibility = Button.GONE
        stopWorkoutButton.visibility = Button.GONE
        startButton.visibility = Button.VISIBLE
        startButton.text = "Workout erneut starten"
        // Fortschritt NICHT zurücksetzen! (wird erst beim nächsten Start gelöscht)
        updateProgressUI()
    }

    /**
     * Stoppt das Workout und kehrt zur Startseite zurück.
     * Fortschritt bleibt erhalten.
     */
    private fun stopWorkoutAndReturnToStart() {
        isWorkoutRunning = false
        countDownTimer?.cancel()
        countdownText.visibility = TextView.GONE
        exerciseImage.visibility = ImageView.GONE
        exerciseText.visibility = TextView.GONE
        exerciseDesc.visibility = TextView.GONE
        cancelExerciseButton.visibility = Button.GONE
        stopWorkoutButton.visibility = Button.GONE
        startButton.visibility = Button.VISIBLE
        startButton.text = "Workout starten"
        // Fortschritt bleibt erhalten!
        updateProgressUI()
    }

    /**
     * Sorgt dafür, dass beim Beenden der Activity der Timer gestoppt wird,
     * um Speicherlecks zu vermeiden.
     */
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}