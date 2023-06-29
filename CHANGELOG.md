# 2023.06.28.1

- Replaced `System` logging with Logback logging.

# 2023.06.28

- Fixed issue where the news snippets were never being refreshed.
- Changed news refresh from twice a day (`13:00` and `23:00` UTC) to just once a day (`13:00` UTC).
- Added explicit query for news snippets by their slot number (was previously arbitrary order).
- Refactored all repositories to use `JpaRepository` instead of `CrudRepository`.
- Updated tests for `NewsUpdater`.
- Added missing docs for lock table columns `id` and `created`.
- Added a "cleaning" step inside the testing docs.

# 2023.06.27

- Added caching for static resources.
- Changed news refresh from `04:00` and `16:00` to `13:00` and `23:00` (UTC)
- Made the "this is a preview" message bold.
- Updated docs for testing.

# 2023.06.26

- Initial release.
