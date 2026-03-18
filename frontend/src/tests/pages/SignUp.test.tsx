import { it, describe, expect } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import SignUp from "../../pages/SignUp";
import { AuthProvider } from "../../context/AuthContext";
import userEvent from "@testing-library/user-event";
import { server } from "../../mocks/server";
import { TEST_API_BASE_URL } from "../../mocks/config";
import { http, HttpResponse } from "msw";

// SETUP
const renderSignUp = () => {
  return render(
    <MemoryRouter>
      <AuthProvider>
        <SignUp />
      </AuthProvider>
    </MemoryRouter>,
  );
};

const fillSignUpForm = async (
  user: ReturnType<typeof userEvent.setup>,
  data: {
    email: string;
    username: string;
    password: string;
    confirmPassword: string;
  },
) => {
  await user.type(screen.getByPlaceholderText(/Enter your email/i), data.email);
  await user.type(
    screen.getByPlaceholderText(/Enter your username/i),
    data.username,
  );
  await user.type(
    screen.getByPlaceholderText(/Enter your password/i),
    data.password,
  );
  await user.type(
    screen.getByPlaceholderText(/Please confirm your password/i),
    data.confirmPassword,
  );
};

// CORRECT PAGE RENDERING TEST
describe("Sign up Page", () => {
  it("should render the sign up form", () => {
    renderSignUp();

    expect(
      screen.getByRole("heading", { name: /Sign up/i }),
    ).toBeInTheDocument();
    expect(
      screen.getByPlaceholderText(/Enter your email/i),
    ).toBeInTheDocument();
    expect(
      screen.getByPlaceholderText(/Enter your username/i),
    ).toBeInTheDocument();
    expect(
      screen.getByPlaceholderText(/Enter your password/i),
    ).toBeInTheDocument();
    expect(
      screen.getByPlaceholderText(/Please confirm your password/i),
    ).toBeInTheDocument();
  });
});

// SUCCESSFULL SIGN UP
it("should signup successfully", async () => {
  const user = userEvent.setup();

  server.use(
    http.post(`${TEST_API_BASE_URL}/api/auth/register`, async ({ request }) => {
      const body = (await request.json()) as {
        username: string;
        email: string;
        password: string;
      };
      return HttpResponse.text(`User created: ${body.username}`, {
        status: 200,
      });
    }),
  );

  renderSignUp();

  await fillSignUpForm(user, {
    email: "test@example.com",
    username: "testuser",
    password: "Password123!",
    confirmPassword: "Password123!",
  });

  const submitButton = screen.getByRole("button", {
    name: /Create an account/i,
  });
  await user.click(submitButton);
  await waitFor(() => {
    expect(screen.getByText(/Registration successful/i)).toBeInTheDocument();
    expect(screen.getByText(/testuser/i)).toBeInTheDocument();
  });
});

// PASSWORDS DO NOT MATCH
it("should not signup successfully if password do not match", async () => {
  const user = userEvent.setup();
  let apiCalled = false;

  server.use(
    http.post(`${TEST_API_BASE_URL}/api/auth/register`, () => {
      apiCalled = true;
      return HttpResponse.json({ success: true }, { status: 200 });
    }),
  );

  renderSignUp();

  await fillSignUpForm(user, {
    email: "test@example.com",
    username: "testuser",
    password: "Password123!",
    confirmPassword: "DifferentPassword123!",
  });

  const submitButton = screen.getByRole("button", {
    name: /Create an account/i,
  });
  await user.click(submitButton);
  await waitFor(() => {
    expect(screen.getByText(/Passwords do not match/i)).toBeInTheDocument();
  });
  expect(apiCalled).toBe(false);
});

// TOO SHORT PASSWORD
it("should show error for weak password", async () => {
  const user = userEvent.setup();
  let apiCalled = false;

  server.use(
    http.post(`${TEST_API_BASE_URL}/api/auth/register`, () => {
      apiCalled = true;
      return HttpResponse.json({ success: true }, { status: 200 });
    }),
  );

  renderSignUp();

  await fillSignUpForm(user, {
    email: "test@example.com",
    username: "testuser",
    password: "weak",
    confirmPassword: "weak",
  });
  const submitButton = screen.getByRole("button", {
    name: /Create an account/i,
  });
  await user.click(submitButton);
  await waitFor(() => {
    expect(screen.getByText(/too short/i)).toBeInTheDocument();
  });
  expect(apiCalled).toBe(false);
});

// INVALID EMAIL FORMAT
it("should show error for invalid email", async () => {
  const user = userEvent.setup();
  let apiCalled = false;

  server.use(
    http.post(`${TEST_API_BASE_URL}/api/auth/register`, () => {
      apiCalled = true;
      return HttpResponse.json({ success: true }, { status: 200 });
    }),
  );

  renderSignUp();

  await fillSignUpForm(user, {
    email: "not-an-email",
    username: "testuser",
    password: "Password123!",
    confirmPassword: "Password123!",
  });
  const submitButton = screen.getByRole("button", {
    name: /Create an account/i,
  });
  await user.click(submitButton);
  await waitFor(() => {
    expect(screen.getByText(/Invalid email format/i)).toBeInTheDocument();
  });
  expect(apiCalled).toBe(false);
});

// ALREADY USED EMAIL
it("should show error when email already exists", async () => {
  const user = userEvent.setup();

  server.use(
    http.post(`${TEST_API_BASE_URL}/api/auth/register`, () => {
      return HttpResponse.json("Email already exists", { status: 400 });
    }),
  );

  renderSignUp();

  await fillSignUpForm(user, {
    email: "existing-email@example.com",
    username: "testuser",
    password: "Password123!",
    confirmPassword: "Password123!",
  });
  const submitButton = screen.getByRole("button", {
    name: /Create an account/i,
  });
  await user.click(submitButton);
  await waitFor(() => {
    expect(screen.getByText(/Email already exists/i)).toBeInTheDocument();
  });
});
