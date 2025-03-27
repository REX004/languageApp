package ru.madfinal.launguageapp.presentation.exercise.listen

import android.Manifest
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.madfinal.launguageapp.R
import ru.madfinal.launguageapp.databinding.FragmentListeningBinding
import ru.madfinal.launguageapp.domain.models.Word
import ru.madfinal.launguageapp.presentation.common.base.BaseFragment
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

class ListeningFragment :
    BaseFragment<FragmentListeningBinding>(FragmentListeningBinding::inflate) {

    private val viewModel: ListeningViewModel by viewModel()

    private var pulseAnimator: ValueAnimator? = null
    private var isRecording = AtomicBoolean(false)
    private var consecutiveSuccessCount = 0
    private var totalScore = 0

    private var audioRecord: AudioRecord? = null
    private var recordingThread: Thread? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    companion object {
        private const val RECORD_AUDIO_PERMISSION_CODE = 101
        private const val SAMPLE_RATE = 16000
        private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        private const val BUFFER_SIZE_FACTOR = 2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()

        // Загрузка первого слова
        viewModel.loadRandomWord()
    }

    private fun setupObservers() {
        viewModel.currentWord.observe(viewLifecycleOwner) { word ->
            updateWordUI(word)
        }

        viewModel.recognitionResult.observe(viewLifecycleOwner) { result ->
            handleRecognitionResult(result)
        }

        viewModel.isRecognizing.observe(viewLifecycleOwner) { isRecognizing ->
            if (isRecognizing) {
                binding.resultWord.text = getString(R.string.recognizing)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            resetUIState()
        }
    }

    private fun setupListeners() {
        // Кнопка назад
        binding.backBt.setOnClickListener {
            findNavController().navigateUp()
        }

        // Кнопка проверки произношения
        binding.checkBt.setOnClickListener {
            // Скрываем основную кнопку и показываем круглую кнопку микрофона
            binding.checkBt.visibility = View.GONE
            binding.microBt.visibility = View.VISIBLE

            // Начинаем запись при нажатии на круглую кнопку
            binding.microBt.setOnClickListener {
                if (isRecording.get()) {
                    stopRecording()
                } else {
                    checkPermissionAndStartRecording()
                }
            }

            // Автоматически запускаем запись
            checkPermissionAndStartRecording()
        }
    }

    private fun updateWordUI(word: Word) {
        binding.wordTxt.text = word.english
        binding.transcriptionTxt.text = word.transcription_english

        // Сбрасываем UI в начальное состояние
        resetUIState()
    }

    private fun resetUIState() {
        // Скрываем блок с результатом
        binding.resultContainer.visibility = View.GONE

        // Скрываем круглую кнопку микрофона
        binding.microBt.visibility = View.GONE

        // Показываем основную кнопку
        binding.checkBt.visibility = View.VISIBLE
        binding.checkBt.text = getString(R.string.check_my_speech)

        // Обновляем обработчик для кнопки
        binding.checkBt.setOnClickListener {
            binding.checkBt.visibility = View.GONE
            binding.microBt.visibility = View.VISIBLE

            binding.microBt.setOnClickListener {
                if (isRecording.get()) {
                    stopRecording()
                } else {
                    checkPermissionAndStartRecording()
                }
            }

            checkPermissionAndStartRecording()
        }
    }

    private fun checkPermissionAndStartRecording() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_CODE
            )
        } else {
            startRecording()
        }
    }

    private fun startRecording() {
        isRecording.set(true)

        val bufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT
        ) * BUFFER_SIZE_FACTOR

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            bufferSize
        )

        if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
            Toast.makeText(
                requireContext(),
                "Не удалось инициализировать AudioRecord",
                Toast.LENGTH_SHORT
            ).show()
            isRecording.set(false)
            return
        }

        audioRecord?.startRecording()

        // Создаем временный файл для записи аудио
        val audioFile = File(requireContext().cacheDir, "audio_record.pcm")

        // Запускаем запись в отдельном потоке
        recordingThread = Thread {
            val data = ByteArray(bufferSize)
            val fileOutputStream = FileOutputStream(audioFile)

            try {
                while (isRecording.get()) {
                    val read = audioRecord?.read(data, 0, bufferSize) ?: -1
                    if (read > 0) {
                        fileOutputStream.write(data, 0, read)
                    }
                }

                fileOutputStream.close()

                // После завершения записи отправляем файл на распознавание
                mainHandler.post {
                    // Показываем контейнер результата в UI потоке
                    binding.resultContainer.visibility = View.VISIBLE
                    binding.resultWord.text = getString(R.string.recognizing)

                    // Вызываем метод viewModel в UI потоке
                    viewModel.recognizeSpeech(audioFile)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                mainHandler.post {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка записи: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    resetUIState()
                }
            }
        }

        recordingThread?.start()

        // Запускаем анимацию пульсации микрофона
        startPulseAnimation()
    }

    private fun stopRecording() {
        isRecording.set(false)

        try {
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null

            recordingThread?.join()
            recordingThread = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Останавливаем анимацию
        pulseAnimator?.cancel()
    }

    private fun handleRecognitionResult(result: String) {
        // Отображаем распознанный текст
        binding.resultWord.text = result

        val currentWord = viewModel.currentWord.value?.english?.toLowerCase(Locale.ROOT) ?: ""
        val recognizedText = result.toLowerCase(Locale.ROOT).trim()

        if (recognizedText == currentWord) {
            // Правильное произношение
            handleCorrectPronunciation()
        } else {
            // Неправильное произношение
            handleIncorrectPronunciation()
        }
    }

    private fun handleCorrectPronunciation() {
        // Увеличиваем счетчик последовательных успехов
        consecutiveSuccessCount++

        // Начисляем баллы
        val bonus = if (consecutiveSuccessCount >= 2) {
            2 * consecutiveSuccessCount
        } else {
            0
        }

        val pointsEarned = 1 + bonus
        totalScore += pointsEarned

        // Скрываем круглую кнопку микрофона
        binding.microBt.visibility = View.GONE

        // Показываем основную кнопку с новым текстом
        binding.checkBt.visibility = View.VISIBLE
        binding.checkBt.text = "Yay! Go next (+$pointsEarned points)"

        // Обновляем счет в ViewModel
        viewModel.updateScore(totalScore)

        // Переходим к следующему слову при нажатии
        binding.checkBt.setOnClickListener {
            viewModel.loadRandomWord()
            resetUIState()
        }
    }

    private fun handleIncorrectPronunciation() {
        // Сбрасываем счетчик последовательных успехов
        consecutiveSuccessCount = 0

        // Скрываем круглую кнопку микрофона
        binding.microBt.visibility = View.GONE

        // Показываем основную кнопку с новым текстом
        binding.checkBt.visibility = View.VISIBLE
        binding.checkBt.text = getString(R.string.try_again)

        // Возвращаем обработчик для кнопки проверки
        binding.checkBt.setOnClickListener {
            binding.checkBt.visibility = View.GONE
            binding.microBt.visibility = View.VISIBLE

            binding.microBt.setOnClickListener {
                if (isRecording.get()) {
                    stopRecording()
                } else {
                    checkPermissionAndStartRecording()
                }
            }

            checkPermissionAndStartRecording()
        }
    }

    private fun startPulseAnimation() {
        pulseAnimator = ValueAnimator.ofFloat(0.8f, 1.2f).apply {
            duration = 800
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = LinearInterpolator()

            addUpdateListener { animator ->
                val scale = animator.animatedValue as Float
                val layoutParams = binding.microBt.layoutParams
                val originalWidth = 160 * resources.displayMetrics.density
                val originalHeight = 160 * resources.displayMetrics.density

                layoutParams.width = (originalWidth * scale).toInt()
                layoutParams.height = (originalHeight * scale).toInt()

                binding.microBt.layoutParams = layoutParams
            }

            start()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Разрешение на запись аудио необходимо для этого упражнения",
                    Toast.LENGTH_SHORT
                ).show()
                resetUIState()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopRecording()
        pulseAnimator?.cancel()
        mainHandler.removeCallbacksAndMessages(null)
    }
}