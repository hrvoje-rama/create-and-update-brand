Feature: API Service Testing

  Scenario Outline: Create and verify creation of new brand
    Given API endpoint is available at "https://api.practicesoftwaretesting.com/brands"
    When I create a new brand with name "<BrandName>" and slug "<Slug>"
    Then I can see the new brand name "<BrandName>" and slug "<Slug>" in the list and the total count should increase by 1

    @Standard
    Examples:
      | BrandName | Slug      |
      | demo1     | demo1             |

  Scenario Outline: Update existing brand
    Given API endpoint is available at "https://api.practicesoftwaretesting.com/brands"
    Then Change name "<BrandName>" and slug "<Slug>" to "<NewBrandName>" and "<NewSlugName>"

    @Standard
    Examples:
      | BrandName | Slug      | NewBrandName     | NewSlugName       |
      | demo1     | demo1     | demo2            | demo2             |
      | demo2     | demo2     | ForgeFlex Tools  | forgeflex-tools   |
      | demo2     | demo2     | demo2            | demo3             |
      | demo2     | demo3     | demo4            | demo4@            |















