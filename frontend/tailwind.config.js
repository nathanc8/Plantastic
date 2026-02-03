import { error } from "console";

/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        // navigation
        forest: "#2D3D2D",
        // backgrounds
        linen: "#FFFBEB",
        // buttons
        sage: {
          DEFAULT: "#4A5E4A",
          dark: "#232C23",
        },
        // destructives actions
        clay: {
          DEFAULT: "#8B4509",
          dark: "#7A3D08",
        },
        // texts
        text: {
          primary: "#111827",
          secondary: "#4B5563",
          placeholder: "#6B7280",
          error: "#B91C1C",
        },
        // focus states
        focus: "#3B82F6",
        // active
        active: "#FDE047",
      },
      fontFamily: {
        montserrat: ['"Montserrat Alternates"', "sans-serif"],
        bellota: ['"Bellota"', "cursive"],
      },
    },
  },
  plugins: [require("flowbite/plugin")],
};
