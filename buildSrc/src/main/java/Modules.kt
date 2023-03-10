// this one contains all the modules that exist inside the project

object Modules {
    const val app = ":app"

    const val core = ":core"
    const val coreUi = ":core-ui"

    const val onboardingDomain = ":onboarding:onboarding_domain"
    const val onboardingPresentation = ":onboarding:onboarding_presentation"

    const val trackerData = ":tracker:tracker_data"
    const val trackerDomain = ":tracker:tracker_domain"
    const val trackerPresentation = ":tracker:tracker_presentation"
}
// build source seems to be used to hold the dependencies strings and objects to be used by gradle