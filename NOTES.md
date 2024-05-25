### Notes

Retry template is general for whole application (if not mentioned another) with these settings:
- max-attempts: 3
- initial-interval: 2s (exponential)
- multiplier: 5
- max-interval: 1m

Spring Batch processes 10 searches within 10 chunks (by idea 100 searches for one iteration) asynchronously

To fetch Djinni vacancies - 1s (to improve maybe need to get access to API, or make processing sync, and after first 
vacancy before `from` time, stop processing). Uses general retry.

To generate cover letter for one vacancy by OpenAI GPT3.5 - 1s (no idea what we can do here). Uses Spring AI retry
with settings from property plus default from reference site.

To send two messages by TG bot with vacancy and cover letter - 1s (no idea what we can do here). Doesn't use retry logic.