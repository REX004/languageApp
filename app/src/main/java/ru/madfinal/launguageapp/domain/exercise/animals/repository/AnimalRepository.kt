package ru.madfinal.launguageapp.domain.exercise.animals.repository

import io.reactivex.rxjava3.core.Single
import ru.madfinal.launguageapp.domain.models.Animal

interface AnimalRepository {
    fun getRandomAnimal(): Single<Animal>
}