import { test as setup } from "@playwright/test";

const authFile = "e2e/.auth/user.json";

setup("authenticate", async ({ page, request }) => {
  const registerResponse = await request.post(
    "http://localhost:8080/api/auth/register",
    {
      data: {
        username: "username",
        email: "user@yopmail.com",
        password: "User1234!",
      },
    },
  );

  await page.goto("/login");

  await page.fill("input[name='username']", "username");
  await page.fill("input[name='password']", "User1234!");

  const loginPromise = page.waitForResponse(
    (response) =>
      response.url().includes("/api/auth/login") && response.status() === 200,
  );

  await page.click("button[type='submit']");
  await loginPromise;
  await page.waitForURL("/");
  await page.waitForTimeout(1000);
  await page.context().storageState({ path: authFile });
});
