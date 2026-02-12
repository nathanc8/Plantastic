import { test, expect } from "@playwright/test";

test.describe("Water multiple plants", () => {
  test.beforeEach(async ({ page }) => {
    await page.goto("/");
    const cookies = await page.context().cookies();
    console.log(
      "Session ID loaded:",
      cookies.find((c) => c.name === "JSESSIONID")?.value,
    );
    await expect(page.getByRole("navigation")).toBeVisible();
  });

  test("should open the water multiple plants modal", async ({ page }) => {
    await page.getByLabel(/Open menu/i).click();
    await page.getByLabel(/water your plants/i).click();

    await expect(
      page.getByRole("heading", { name: /Water multiple plants/i }),
    ).toBeVisible();
  });
});
