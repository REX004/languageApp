package ru.madfinal.launguageapp.presentation.exercise.animals.states

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.ar.core.ArCoreApk
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.databinding.FragmentAnimalsSuccessBinding
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment
import ru.madfinal.launguageapp.presentation.util.navigateWithAnimation
import androidx.core.view.isVisible

class AnimalsSuccessFragment :
    BaseFragment<FragmentAnimalsSuccessBinding>(FragmentAnimalsSuccessBinding::inflate) {

    private val args: AnimalsSuccessFragmentArgs by navArgs()
    private lateinit var arFragment: ArFragment
    private var modelRenderable: ModelRenderable? = null
    private var animalName: String = ""

    companion object {
        private const val TAG = "AnimalsSuccessFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupAR()
        setupListeners()
        if (!ArCoreApk.getInstance().checkAvailability(requireContext()).isSupported) {
            Toast.makeText(
                context,
                "ARCore не поддерживается на этом устройстве",
                Toast.LENGTH_LONG
            ).show()
            return
        }
    }

    override fun applyClick() {
        super.applyClick()
        binding.apply {
            backFromArButton.setOnClickListener {
                binding.arContainer.visibility = View.GONE
            }
            exitArButton.setOnClickListener {
                backPressed()
            }
        }
    }

    private fun setupUI() {
        animalName = args.animalName ?: "cat"
        binding.arInstructionText.text =
            "Нажмите на поверхность, чтобы разместить 3D модель животного"
    }

    private fun setupAR() {
        arFragment = childFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment
        loadAnimalModel(animalName)
        arFragment.setOnTapArPlaneListener { hitResult, plane, _ ->
            if (plane.type == Plane.Type.HORIZONTAL_UPWARD_FACING) {
                placeAnimalModel(hitResult, plane)
            }
        }
    }

    private fun loadAnimalModel(animalName: String) {
        try {
            val modelResourceUri = getModelUriForAnimal(animalName)

            Log.d(TAG, "Attempting to load model for animal: $animalName")
            Log.d(TAG, "Model Resource URI: $modelResourceUri")

            ModelRenderable.builder()
                .setSource(
                    context,
                    RenderableSource.builder()
                        .setSource(context, modelResourceUri, RenderableSource.SourceType.GLB)
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .setScale(0.5f)
                        .build()
                )
                .setRegistryId(modelResourceUri)
                .build()
                .thenAccept { renderable ->
                    modelRenderable = renderable
                    requireActivity().runOnUiThread {
                        binding.arReadyIndicator.visibility = View.VISIBLE
                        Log.d(TAG, "Model loaded successfully for $animalName")
                        Toast.makeText(
                            context,
                            "3D модель животного загружена",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .exceptionally { throwable ->
                    Log.e(TAG, "error", throwable)
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            context,
                            "Ошибка загрузки модели: ${throwable.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    null
                }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in loadAnimalModel", e)
            Toast.makeText(
                context,
                "Внутренняя ошибка: ${e.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getModelUriForAnimal(animal: String): Uri {
        val fileName = when (animal.lowercase()) {
            "fox" -> "fox_model.glb"
            "elephant" -> "elephant_model.glb"
            "dog" -> "dog_model.glb"
            "tiger" -> "tiger_model.glb"
            else -> "def.glb"
        }
        return Uri.parse("file:///android_asset/models/$fileName")
    }

    private fun placeAnimalModel(hitResult: HitResult, plane: Plane) {
        modelRenderable?.let { model ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            val transformableNode = TransformableNode(arFragment.transformationSystem)
            transformableNode.setParent(anchorNode)
            transformableNode.renderable = model

            transformableNode.select()

            addAnimalInfoNode(anchorNode, animalName)
        } ?: run {
            Toast.makeText(context, "3D модель еще не загружена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addAnimalInfoNode(parentNode: AnchorNode, animalName: String) {
    }

    private fun setupListeners() {
        binding.nextBt.setOnClickListener {
            findNavController().navigateWithAnimation(R.id.animalsFragment)
        }

        binding.backBt.setOnClickListener {
            backPressed()
        }

        binding.arToggleButton.setOnClickListener {
            toggleARVisibility()
        }
    }

    private fun toggleARVisibility() {
        with(binding) {
            if (arContainer.isVisible) {
                arContainer.visibility = View.GONE
                regularSuccessContainer.visibility = View.VISIBLE
                arToggleButton.text = "Показать в AR"
            } else {
                arContainer.visibility = View.VISIBLE
                regularSuccessContainer.visibility = View.GONE
                arToggleButton.text = "Скрыть AR"
            }
        }
    }
}