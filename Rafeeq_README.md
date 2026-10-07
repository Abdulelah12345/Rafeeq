# Rafeeq

Rafeeq is a Spring Boot healthcare support platform that uses user health data, medication schedules, nutrition plans, exercise plans, and AI-assisted analysis to provide personalized health guidance and planning features.

## My Contributions

I implemented **11 endpoints** focused on personalized nutrition, exercise planning, medication timing, and health-plan monitoring.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/v1/api/ai/nutrition/{userId}/alternative` | Generates an AI-based alternative for an item in the user's saved nutrition plan. |
| POST | `/v1/api/ai/exercise/{userId}/alternative` | Generates an AI-based alternative for an exercise in the user's saved exercise plan. |
| POST | `/v1/api/ai/exercise/{userId}` | Generates a personalized exercise plan using the user's health profile and vital signs. |
| GET | `/v1/api/exercise-plan/user/{userId}/review-status` | Checks whether new HIGH or CRITICAL vital signs require the exercise plan to be reviewed. Sends an email alert when review is needed. |
| GET | `/api/v1/medication-schedule/{userId}/medication-meal-check` | Uses AI to compare medication timing and meal relation with the user's nutrition plan. |
| POST | `/v1/api/ai/nutrition/{userId}` | Generates a personalized nutrition plan using the user's health profile and vital signs. |
| POST | `/v1/api/users/{userId}/nutrition-shopping-list` | Converts the user's nutrition plan into a personalized categorized grocery list. |
| GET | `/v1/api/users/{userId}/daily-timeline` | Combines medication times, nutrition-plan information, and exercise-plan information into one daily timeline. |
| GET | `/api/v1/medication-schedule/user/{userId}/next-dose` | Finds the nearest upcoming active medication dose using the current server/device time. |
| GET | `/v1/api/users/{userId}/health-plan-status` | Checks whether the user has the required health profile, vital signs, nutrition plan, and exercise plan. |
| POST | `/v1/api/users/{userId}/meal-suitability-check` | Uses AI and the user's health data to evaluate whether a meal is suitable for the current health plan. |

## Key Features I Worked On

- **AI Nutrition Plan Generation** — creates a personalized nutrition plan from the user's health profile and recorded vital signs.
- **AI Exercise Plan Generation** — generates an exercise plan based on health information and exercise-risk data.
- **Nutrition & Exercise Alternatives** — allows users to request alternatives without regenerating the entire saved plan.
- **Exercise Plan Review Monitoring** — detects new HIGH or CRITICAL vital readings recorded after the latest exercise-plan update and triggers an email notification.
- **Medication & Meal Timing Check** — compares medication meal instructions and times with the current nutrition plan.
- **Next Medication Dose** — calculates the closest upcoming medication time using `LocalTime.now()` and active medication schedules.
- **Personalized Grocery List** — extracts and categorizes shopping items from the user's nutrition plan.
- **Daily Timeline** — presents the user's medication, nutrition, and exercise information in a single daily view.
- **Health Plan Status** — verifies whether the main data required for a complete health plan is available.
- **Meal Suitability Check** — evaluates a meal against the user's health profile, vital signs, and current nutrition plan.

## Technologies Used in My Work

- Java 17
- Spring Boot
- Spring Web / REST APIs
- Spring Data JPA
- MySQL
- Jakarta Validation
- Lombok
- Anthropic AI API
- Spring Mail / `JavaMailSender`
- Maven

## Project Structure Used

```text
Controller
├── AIController
├── ExercisePlanController
├── MedicationScheduleController
└── UserController

Service
├── AIPlanService
├── ExercisePlanService
├── MedicationScheduleService
├── DailyTimelineService
└── HealthPlanStatusService

DTO
├── AlternativeRequestDTO
├── NutritionAlternativeResponse
├── ExerciseAlternativeResponse
├── MedicationMealCheckDTO
├── NutritionShoppingListDTO
├── MealSuitabilityRequestDTO
├── MealSuitabilityResponseDTO
├── DailyTimelineDTO
├── HealthPlanStatusDTO
├── PlanReviewStatusDTO
└── NextMedicationDoseDTO
```

## Notes

The AI-powered health features are intended to provide general supportive guidance and are not a replacement for professional medical advice, diagnosis, or treatment.
