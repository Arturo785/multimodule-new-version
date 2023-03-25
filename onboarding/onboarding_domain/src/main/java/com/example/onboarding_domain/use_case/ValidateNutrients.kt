package com.example.onboarding_domain.use_case

import com.example.core.util.UiText
import com.example.core.R


// only the onboarding section makes use of this
class ValidateNutrients {

    operator fun invoke(
        carbsRatioText: String,
        proteinRatioText: String,
        fatRatioText: String
    ): ResultNutrients {
        val carbsRatio = carbsRatioText.toIntOrNull()
        val proteinRatio = proteinRatioText.toIntOrNull()
        val fatRatio = fatRatioText.toIntOrNull()

        // not null fields
        if (carbsRatio == null || proteinRatio == null || fatRatio == null) {
            return ResultNutrients.Error(
                message = UiText.StringResource(R.string.error_invalid_values)
            )
        }
        // less or more than 100%
        if (carbsRatio + proteinRatio + fatRatio != 100) {
            return ResultNutrients.Error(
                message = UiText.StringResource(R.string.error_not_100_percent)
            )
        }

        return ResultNutrients.Success(
            carbsRatio / 100f,
            proteinRatio / 100f,
            fatRatio / 100f
        )
    }

    sealed class ResultNutrients {
        data class Success(
            val carbsRatio: Float,
            val proteinRatio: Float,
            val fatRatio: Float
        ) : ResultNutrients()

        data class Error(val message: UiText) : ResultNutrients()
    }
}