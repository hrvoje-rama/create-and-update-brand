# API Automation Testing: Create and Update Brand

This repository contains automation tests for the `/brands` endpoints of the Practice Software Testing API (https://api.practicesoftwaretesting.com/api/documentation). These tests demonstrate the process of creating and updating brands while handling both positive and negative scenarios.

---

## **Project Overview**

The tests focus on:
1. **Create New Brand**: Validate successful creation of a new brand and ensure the brand is listed in the API response with the total count incremented.
2. **Update Existing Brand**: Test updating existing brand details, including validation of success cases and proper handling of errors for duplicate entries or invalid data.


| Original Name | Original Slug | New Name         | New Slug         | Expected Outcome                                                                                       |
|---------------|---------------|------------------|------------------|--------------------------------------------------------------------------------------------------------|
| demo1         | demo1         | demo2            | demo2            | Successfully updated the brand.                                                                       |
| demo2         | demo2         | ForgeFlex Tools  | forgeflex-tools  | Update failed due to duplicate entry: another brand already uses the slug `forgeflex-tools`.          |
| demo2         | demo2         | demo2            | demo3            | Successfully updated the brand `demo2` to name `demo2` and slug `demo3`.                              |
| demo2         | demo3         | demo4            | demo4@           | Update failed due to validation error: `{"slug":["The slug field must only contain letters, numbers, dashes, and underscores."]}` |


## Test Reports

- [HTML Report](reports/test-report.html)
- [PDF Report](reports/test-report.pdf)

