import test, { expect, Page } from "@playwright/test";

export const navigateToHomePage = test.extend<{ page: Page }>({
  page: async ({ page }, use) => {
    await page.goto("/");
    await expect(page.getByRole("navigation")).toBeVisible();
    await use(page);
  },
});

export const wateringMultipleModal = test.extend<{ page: Page }>({
  page: async ({ page }, use) => {
    await page.goto("/");
    await expect(page.getByRole("navigation")).toBeVisible();
    await page.getByLabel(/Open menu/i).click();
    await page.getByLabel(/water your plants/i).click();
    await use(page);
  },
});

// import test, { expect, Page } from "@playwright/test";

// export const navigateToHomePage = test.extend<{ page: Page }>({
//   storageState: "e2e/.auth/user.json",
//   page: async ({ page }, use) => {
//     await page.goto("/");
//     await expect(page.getByRole("navigation")).toBeVisible();
//     await use(page);
//   },
// });

// export const wateringMultipleModal = test.extend<{ page: Page }>({
//   storageState: "e2e/.auth/user.json",
//   page: async ({ page }, use) => {
//     await page.goto("/");
//     await expect(page.getByRole("navigation")).toBeVisible();
//     await page.getByLabel(/Open menu/i).click();
//     await page.getByLabel(/water your plants/i).click();
//     await use(page);
//   },
// });
