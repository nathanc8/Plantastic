// // import { createRoot } from "react-dom/client";
// import "./index.css";
// // import { App } from "./App.tsx";
// // import { StrictMode } from "react";
// // import {
// //   BrowserRouter as Router,
// //   Routes,
// //   Route,
// //   Navigate,
// // } from "react-router-dom";
// // import { Login } from "./components/Login.tsx";
// // import { Signup } from "./components/SignUp.tsx";

// // Handling the asynchronous operation to avoid a race condition
// async function enableMocking() {
//   if (process.env.NODE_ENV !== "development") {
//     return;
//   }
//   const { worker } = await import("./mocks/browser.ts");
//   // `worker.start()` returns a Promise that resolves
//   // once the Service Worker is up and ready to intercept requests.
//   return worker.start();
// }
// enableMocking().then(() => {
//   const container = document.getElementById("root");
//   if (!container) throw new Error("Root container missing");

//   // const root = createRoot(container).render(
//   //   <StrictMode>
//   //     <Router>
//   //       <Routes>
//   //         <Route path="/" element={<App />} />
//   //         <Route path="/signup" element={<Signup />} />
//   //         <Route path="/login" element={<Login />} />
//   //       </Routes>
//   //     </Router>
//   //   </StrictMode>
//   // );
// });
// main.tsx

import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { worker } from './mocks/browser'; // Ton fichier MSW
import { BrowserRouter, Route, Routes } from 'react-router-dom';

async function prepare() {
  if (import.meta.env.DEV) {
    // Démarre MSW en mode dev
    await worker.start();
  }

  const root = document.getElementById('root')!;
  ReactDOM.createRoot(root).render(
    <React.StrictMode>
      <BrowserRouter>
          <App />
      </BrowserRouter>
    </React.StrictMode>,
  );
}

prepare();
