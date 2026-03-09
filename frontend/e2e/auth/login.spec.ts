import { test, expect } from "@playwright/test";

test.describe("Login Page", () => {
  test.beforeEach(async ({ page }) => {
    await page.goto("/login");
  });

  test("should display login form elements", async ({ page }) => {
    await expect(page.getByRole("heading", { name: /login/i })).toBeVisible();
    await expect(page.locator("#input-username")).toBeVisible();
    await expect(page.locator("#input-password")).toBeVisible();
    await expect(page.getByRole("button", { name: /login/i })).toBeVisible();
    await expect(page.getByText(/don't have an account?/i)).toBeVisible();
    await expect(
      page.getByRole("link", { name: /register here/i }),
    ).toBeVisible();
  });

  test("should show error with empty fields", async ({ page }) => {
    await page.click("button[type=submit]");

    await expect(
      page.getByText(/username must be at least 3 characters/i),
    ).toBeVisible();
    await expect(page.getByText(/password is required/i)).toBeVisible();
  });

  test("should show error with invalid credentials", async ({ page }) => {
    await page.locator("#input-password").fill("wrongusername");
    await page.locator("#input-username").fill("wrongpassword");
    await page.click("button[type=submit]");

    await expect(page.getByText(/bad credentials/i)).toBeVisible();
  });

  test("should login successfully", async ({ page }) => {
    await page.locator("#input-username").fill("username");
    await page.locator("#input-password").fill("User1234!");
    await page.click("button[type=submit]");

    await page.waitForURL("/");
    await expect(page.getByRole("navigation")).toBeVisible();
  });

  test("should display description with logo", async ({ page }) => {
    await expect(page.locator("#description-text")).toBeVisible();
  });
});
