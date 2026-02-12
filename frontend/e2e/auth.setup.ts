import { test as setup } from "@playwright/test";

const authFile = "e2e/.auth/user.json";

setup("authenticate", async ({ page }) => {
  await page.goto("/login");

  await page.fill("input[name='username']", "user");
  await page.fill("input[name='password']", "User1234!");

  const loginPromise = page.waitForResponse(
    (response) =>
      response.url().includes("/api/auth/login") && response.status() === 200,
  );

  await page.click("button[type='submit']");

  await loginPromise;

  await page.waitForURL("/");

  await page.waitForTimeout(1000);

  const cookies = await page.context().cookies();
  console.log(
    "Session ID saved:",
    cookies.find((c) => c.name === "JSESSIONID")?.value,
  );

  await page.context().storageState({ path: authFile });
});
