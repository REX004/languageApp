package ru.madfinal.launguageapp.domain.exercise.animals.usecase

import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.domain.exercise.animals.repository.AnimalRepository
import ru.madfinal.launguageapp.domain.models.Animal

class GetRandomAnimalUseCase(private val repository: AnimalRepository) {
    fun execute(): Single<Animal> {
        return repository.getRandomAnimal()
    }
}