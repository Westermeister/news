# Environment Variables

The following environment variables need to be set before running the project.

These 3 are used for configuring a datasource for Spring Boot:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

The next 2 are API keys:

- `NYTIMES_API_KEY`
    - Used to get the top stories from the NYTimes API.
- `OPENAI_API_KEY`
    - Used to summarize the top stories from the NYTimes API via OpenAI's ChatGPT.
