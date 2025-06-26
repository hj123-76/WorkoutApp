package com.example.workoutapp
/**
 * Die Liste dient als zentrale Datenbasis für die App und wird von MainActivity genutzt,
 * um die nächste Übung anzuzeigen oder den Fortschritt zu berechnen.
 */
object ExerciseData {
    // Die Liste aller Übungen des Workouts in der festgelegten Reihenfolge
    val exercises = listOf(
        Exercise(
            id = 1,
            name = "Liegestütze",
            description = "Bei Liegestützen begibst du dich in eine gerade Plank-Position mit gestreckten Armen, Händen schulterbreit unter den Schultern und gespanntem Körper. Dann beugst du die Arme und senkst den Oberkörper kontrolliert ab, bis die Brust fast den Boden berührt. Anschließend drückst du dich wieder nach oben in die Ausgangsposition, ohne die Körperspannung zu verlieren.\n",
            imageRes = R.drawable.pushup
        ),
        Exercise(
            id = 2,
            name = "Kniebeugen",
            description = "Bei Kniebeugen stellst du dich hüftbreit hin, die Fußspitzen leicht nach außen gedreht, der Rücken bleibt gerade und der Blick nach vorn. Dann beugst du die Knie und schiebst das Gesäß nach hinten und unten, als würdest du dich auf einen Stuhl setzen, bis die Oberschenkel mindestens parallel zum Boden sind. Anschließend drückst du dich kontrolliert über die Fersen zurück in den Stand.",
            imageRes = R.drawable.squat
        ),
        Exercise(
            id = 3,
            name = "Situps",
            description = "Bei Sit-ups liegst du in Rückenlage auf dem Boden, die Beine sind angewinkelt und die Füße stehen flach auf. Die Hände liegen an den Schläfen oder über der Brust. Dann spannst du die Bauchmuskeln an und hebst den Oberkörper kontrolliert bis in eine aufrechte Sitzposition. Anschließend senkst du ihn langsam wieder ab, ohne komplett abzulegen.\n",
            imageRes = R.drawable.situp
        ),
        Exercise(
            id = 4,
            name = "Ausfallschritte/Lunges",
            description = "Bei Ausfallschritten stehst du aufrecht und machst einen großen Schritt nach vorn. Dabei beugst du beide Knie, bis das hintere fast den Boden berührt und das vordere etwa im rechten Winkel steht. Dann drückst du dich kontrolliert zurück in die Ausgangsposition und wechselst das Bein.\n",
            imageRes = R.drawable.lunge
        ),
        Exercise(
            id = 5,
            name = "Dips",
            description = "Bei Dips stützt du dich mit den Händen auf einer stabilen Fläche ab, z. B. einer Bank, und streckst die Beine nach vorn. Dann beugst du langsam die Arme und senkst den Körper ab, bis die Ellenbogen etwa einen 90-Grad-Winkel bilden. Anschließend drückst du dich kontrolliert wieder nach oben in die Ausgangsposition.\n",
            imageRes = R.drawable.dip
        ),
        Exercise(
            id = 6,
            name = "Burpees",
            description = "Bei Burpees startest du im Stand, gehst in die Hocke und setzt die Hände vor dir auf den Boden. Dann springst du mit den Füßen nach hinten in die Liegestützposition, machst optional einen Liegestütz, springst zurück in die Hocke und explosiv nach oben mit einem Strecksprung.\n",
            imageRes = R.drawable.burpee
        ),
        Exercise(
            id = 7,
            name = "Beinheben",
            description = "Beim Beinheben liegst du flach auf dem Rücken, die Beine gestreckt und die Arme seitlich am Körper. Dann hebst du beide Beine kontrolliert nach oben, bis sie senkrecht stehen, und senkst sie langsam wieder ab, ohne den unteren Rücken vom Boden abzuheben oder die Beine ganz abzulegen.\n",
            imageRes = R.drawable.beinheben
        ),
        Exercise(
            id = 8,
            name = "Hampelmänner",
            description = "Bei Hampelmännern startest du im aufrechten Stand mit geschlossenen Beinen und Armen am Körper. Dann springst du leicht ab, spreizt gleichzeitig die Beine zur Seite und führst die Arme über dem Kopf zusammen. Beim nächsten Sprung kehrst du in die Ausgangsposition zurück und wiederholst die Bewegung rhythmisch.\n",
            imageRes = R.drawable.hampelmann
        ),
        Exercise(
            id = 9,
            name = "Planks",
            description = "Bei Planks stützt du dich mit den Unterarmen und den Zehenspitzen auf dem Boden ab, wobei die Ellbogen unter den Schultern stehen. Dein Körper bildet eine gerade Linie von Kopf bis Fuß, der Bauch ist angespannt, der Rücken gerade, und du hältst diese Position möglichst stabil ohne durchzuhängen oder den Po anzuheben.\n",
            imageRes = R.drawable.plank
        ),
    )
}