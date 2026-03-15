import { defineConfig, configDefaults } from "vitest/config";

export default defineConfig({
  test: {
    globals: true,
    environment: "jsdom",
    css: true,
    setupFiles: "./src/tests/setup/setupTests.ts",
    testTimeout: 10000,
    coverage: {
      provider: "v8", // Use Vite's default coverage provider
      reporter: ["text", "json", "html"],
    },
    exclude: ["**/node_modules/**", "**/dist/**", "**/e2e/**"],
    alias: {
      "@/assets": "/src/assets",
    },
  },
});
