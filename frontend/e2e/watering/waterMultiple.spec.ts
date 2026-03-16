import { test, expect, Page } from "@playwright/test";
import { navigateToHomePage, wateringMultipleModal } from "../fixtures";

test.describe("Water multiple plants", () => {
  navigateToHomePage(
    "should open the water multiple plants modal",
    async ({ page }) => {
      await page.getByLabel(/Open menu/i).click();
      await page.getByLabel(/water your plants/i).click();

      await expect(
        page.getByRole("heading", { name: /Water multiple plants/i }),
      ).toBeVisible();
    },
  );

  wateringMultipleModal(
    "should select all the plants when select all is clicked",
    async ({ page }) => {
      await page.getByRole("button", { name: /select all/i }).click();

      const checkboxes = page.getByRole("checkbox");
      const count = await checkboxes.count();

      for (let i = 0; i < count; i++) {
        await expect(checkboxes.nth(i)).toBeChecked();
      }
    },
  );

  wateringMultipleModal(
    "should select one plant and submit button should be enabled and updated",
    async ({ page }) => {
      const wateringButton = page.getByTestId("water-multiple-button");
      await expect(wateringButton).toBeDisabled();

      const checkboxes = page.getByRole("checkbox");
      await checkboxes.nth(0).click();

      await expect(wateringButton).toHaveText(/water 1 plant/i);
      await expect(wateringButton).toBeEnabled();
    },
  );

  test.describe("Watering function", () => {
    wateringMultipleModal(
      "should water the plants and get the success message",
      async ({ page }) => {
        const wateringButton = page.getByTestId("water-multiple-button");
        const checkboxes = page.getByRole("checkbox");
        await checkboxes.nth(0).click();
        await wateringButton.click();

        await expect(
          page.getByText(/your plants are no longer thirsty/i),
        ).toBeVisible({ timeout: 15000 });
      },
    );

    wateringMultipleModal(
      "should update last & next watering dates",
      async ({ page }) => {
        const wateringButton = page.getByTestId("water-multiple-button");
        const checkboxes = page.getByRole("checkbox");
        const dateInput = page.getByLabel(/watering date/i);

        await checkboxes.nth(0).click();
        const plantNickname =
          (await page.locator("span.font-medium").first().textContent()) ?? "";
        await dateInput.fill("2026-02-01");
        await wateringButton.click();

        const heading = page.getByRole("heading", { name: plantNickname });
        const card = page.getByTestId("plant-card").filter({ has: heading });
        const lastWatering = card.getByText(/Last watering:/i);
        const nextWatering = card.getByText(/Next watering:/i);

        await expect(lastWatering).toHaveText(/Last watering: 2026-02-01/i);
        await expect(nextWatering).toHaveText(/Next watering: 2026-02-21/i);
      },
    );
  });
});

test("add, water & delete a plant", async ({ page }) => {
  await page.goto("/");
  await expect(page.getByRole("navigation")).toBeVisible();

  await test.step("add a plant", async () => {
    await page.getByLabel(/Open menu/i).click();
    await page.getByLabel(/add a plant/i).click();

    const plantSearch = page.locator("#plant-search");
    const nicknameField = page.locator("#input-nickname");
    const acquisitionDateField = page.locator("#input-acquisitionDate");
    const addButton = page.getByRole("button", { name: /Send/i });

    await plantSearch.fill("sago");
    await page.getByRole("option", { name: /sago palm/i }).click();
    await nicknameField.fill("Jane Doe");
    await acquisitionDateField.fill("2026-01-01");
    await addButton.click();

    await expect(page.getByRole("heading", { name: "Jane Doe" })).toBeVisible();
  });

  await test.step("water the plant", async () => {
    await page.getByLabel(/Open menu/i).click();
    await page.getByLabel(/water your plants/i).click();

    const wateringButton = page.getByTestId("water-multiple-button");
    const dateInput = page.getByLabel(/watering date/i);
    const janeDoeCard = page
      .getByTestId("plant-card")
      .filter({ hasText: /jane doe/i });

    await page.getByRole("checkbox", { name: /Jane Doe/i }).check();
    await dateInput.fill("2026-02-01");
    await wateringButton.click();

    await expect(
      page.getByText(/your plants are no longer thirsty/i),
    ).toBeVisible();
    await expect(
      janeDoeCard.getByText(/last watering: 2026-02-01/i),
    ).toBeVisible();
    await expect(
      janeDoeCard.getByText(/next watering: 2026-02-11/i),
    ).toBeVisible();
  });

  await test.step("delete the plant", async () => {
    const janeDoeCard = page
      .getByTestId("plant-card")
      .filter({ hasText: /jane doe/i });

    await janeDoeCard.getByRole("button", { name: /consult/i }).click();
    await page.getByRole("button", { name: /delete this plant/i }).click();
    await page.getByRole("button", { name: /confirm/i }).click();

    await expect(page.getByText(/Your plant has been deleted/i)).toBeVisible();
    await expect(janeDoeCard).not.toBeVisible();
  });
});
