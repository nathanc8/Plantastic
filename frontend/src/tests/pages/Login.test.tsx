import { it, describe, expect } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import Login from "../../pages/Login";
import { AuthProvider } from "../../context/AuthContext";
import userEvent from "@testing-library/user-event";
import { server } from "../../mocks/server";
import { TEST_API_BASE_URL } from "../../mocks/config";
import { http, HttpResponse } from "msw";

// SETUP
const renderLogin = () => {
  render(
    <MemoryRouter>
      <AuthProvider>
        <Login />
      </AuthProvider>
    </MemoryRouter>,
  );
};

const renderLoginWithHomePage = () => {
  render(
    <MemoryRouter initialEntries={["/login"]}>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<div>Home Page</div>} />
        </Routes>
      </AuthProvider>
    </MemoryRouter>,
  );
};

const loginForm = async (
  user: ReturnType<typeof userEvent.setup>,
  data: {
    email?: string;
    username?: string;
    password: string;
  },
) => {
  const credential = data.email || data.username || "";
  await user.type(
    screen.getByPlaceholderText(/Enter username or email/i),
    credential,
  );
  await user.type(
    screen.getByPlaceholderText(/Enter password/i),
    data.password,
  );
};

// CORRECT PAGE RENDERING TEST
describe("Login Page", () => {
  it("should render the login form", () => {
    renderLogin();
    expect(screen.getByRole("heading", { name: /Login/i })).toBeInTheDocument();
    expect(
      screen.getByPlaceholderText(/Enter username or email/i),
    ).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/Enter password/i)).toBeInTheDocument();
  });
});

// LOGIN SUCESSFULL WITH EMAIL OR USERNAME
describe.each([
  [
    "email",
    "test@example.com",
    { id: 1, email: "test@example.com", role: "ROLE_USER" },
  ],
  [
    "username",
    "testuser",
    { id: 1, email: "test@test.com", role: "ROLE_USER" },
  ],
])("Login with %s", (inputValue, mockUser) => {
  it("should login successfully", async () => {
    const user = userEvent.setup();
    // 1. MOCK API LOGIN SUCCESS AND NAVIGATION
    server.use(
      http.post(`${TEST_API_BASE_URL}/api/auth/login`, () => {
        return HttpResponse.json({ success: true }, { status: 200 });
      }),
      http.get(`${TEST_API_BASE_URL}/api/me/my-digital-garden`, () => {
        return HttpResponse.json(
          {
            user: mockUser,
            digitalGarden: [],
          },
          { status: 200 },
        );
      }),
    );
    // 2. COMPONENT RENDERING WITH initialEntries TO TRACK NAVIGATION
    renderLoginWithHomePage();
    // 3. FILL THE FORM
    await loginForm(user, {
      username: inputValue,
      password: "Password123!",
    });
    // 4. CLICK SIMULATION
    const submitButton = screen.getByRole("button", { name: /Login/i });
    await user.click(submitButton);
    // 5. VALIDATION AND HOME PAGE LANDING
    await waitFor(() => {
      expect(screen.getByText(/Home Page/i)).toBeInTheDocument();
    });
  });
});

// EMPTY FIELDS
it("should show validation errors and not call API for empty fields", async () => {
  const user = userEvent.setup();

  let apiCalled = false;

  server.use(
    http.post(`${TEST_API_BASE_URL}/api/auth/login`, () => {
      apiCalled = true;
      return HttpResponse.json({ success: true }, { status: 200 });
    }),
  );
  renderLogin();
  const submitButton = screen.getByRole("button", { name: /Login/i });
  user.click(submitButton);

  await waitFor(() => {
    expect(screen.getByText(/at least 3 character/i)).toBeInTheDocument();
    expect(screen.getByText(/Password is required/i)).toBeInTheDocument();
  });
  expect(apiCalled).toBe(false);
});

// WRONG CREDENTIALS
it("should not login with wrong credentials", async () => {
  const user = userEvent.setup();
  server.use(
    http.post(`${TEST_API_BASE_URL}/api/auth/login`, () => {
      return HttpResponse.json(
        { message: "Invalid credentials" },
        { status: 401 },
      );
    }),
  );
  renderLoginWithHomePage();

  await loginForm(user, {
    username: "wrongemail@example.coom",
    password: "wrongpassword",
  });

  const submitButton = screen.getByRole("button", { name: /Login/i });
  await user.click(submitButton);
  await waitFor(() => {
    expect(screen.getByText(/Invalid credentials/i)).toBeInTheDocument();
  });
});
