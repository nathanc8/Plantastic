import { test as setup } from "@playwright/test";

const authFile = "e2e/.auth/user.json";

setup("authenticate", async ({ page }) => {
  await page.goto("/login");

  await page.fill("input[name='username']", "user");
  await page.fill("input[name='password']", "User1234!");

  // 👇 Attends spécifiquement la réponse du LOGIN (pas my-digital-garden)
  const loginPromise = page.waitForResponse(
    (response) =>
      response.url().includes("/api/auth/login") && response.status() === 200,
  );

  await page.click("button[type='submit']");

  // Attends que le login API réussisse
  await loginPromise;

  // Attends la redirection
  await page.waitForURL("/");

  // Donne un peu de temps pour que le cookie soit bien établi
  await page.waitForTimeout(1000);

  // Sauvegarde l'état
  await page.context().storageState({ path: authFile });
});
