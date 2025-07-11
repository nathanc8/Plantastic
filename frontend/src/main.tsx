import { createRoot } from "react-dom/client";
import "./index.css";
import { App } from "./App.tsx";
import "./index.css";

// Handling the asynchronous operation to avoid a race condition
async function enableMocking() {
  if (process.env.NODE_ENV !== "development") {
    return;
  }
  const { worker } = await import("./mocks/browser");
  // `worker.start()` returns a Promise that resolves
  // once the Service Worker is up and ready to intercept requests.
  return worker.start();
}
enableMocking().then(() => {
  const container = document.getElementById("root");
  if (!container) throw new Error("Root container missing");

  const root = createRoot(container);
  root.render(<App />);
});
