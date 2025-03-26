package ru.madfinal.launguageapp.domain.exercise.wordPractice.usecase


import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.domain.exercise.wordPractice.repository.WordPracticeRepository
import ru.madfinal.launguageapp.domain.models.Word
import kotlin.random.Random

class GetRandomWordUseCase(private val repository: WordPracticeRepository) {

    fun execute(): Single<Triple<Word, List<String>, Boolean>> {
        return repository.getWords()
            .map { words ->
                if (words.isEmpty()) {
                    throw IllegalStateException("No words available")
                }

                val word = words.random()
                val isEnglishQuestion = Random.nextBoolean()

                val options: List<String>

                if (isEnglishQuestion) {
                    options = generateOptions(words, word, true)
                } else {
                    options = generateOptions(words, word, false)
                }

                Triple(word, options, isEnglishQuestion)
            }
    }

    private fun generateOptions(
        allWords: List<Word>,
        correctWord: Word,
        isEnglishQuestion: Boolean
    ): List<String> {
        val correctAnswer = if (isEnglishQuestion) correctWord.russian else correctWord.english

        val wrongOptions = allWords
            .filter { it.id != correctWord.id }
            .map { if (isEnglishQuestion) it.russian else it.english }
            .shuffled()
            .take(3)

        return (listOf(correctAnswer) + wrongOptions).distinct().shuffled()
    }
}