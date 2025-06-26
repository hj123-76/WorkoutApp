package com.example.workoutapp

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {
    // Die aktuell verwendete MediaPlayer-Instanz. Wird verwendet, um einen Sound abzuspielen.
    private var player: MediaPlayer? = null

    /**
     * Spielt einen Sound ab, der durch die Ressourcen-ID [resId] festgelegt wird.
     * Wird bereits ein Sound abgespielt, wird dieser gestoppt und die Ressourcen freigegeben.
     * Der neue Sound wird dann abgespielt.
     *
     * @param context Kontext der Anwendung (z.B. Activity), um auf Ressourcen zugreifen zu können
     * @param resId Ressourcen-ID des abzuspielenden Sounds (z.B. R.raw.uebungston)
     */
    fun play(context: Context, resId: Int) {
        // Falls schon ein MediaPlayer existiert, diesen stoppen und freigeben
        player?.release()
        // Neuen MediaPlayer mit der gewünschten Ressourcen-ID erstellen
        player = MediaPlayer.create(context, resId)
        // Abspielen des Sounds starten
        player?.start()
    }
}