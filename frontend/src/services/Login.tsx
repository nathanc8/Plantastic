import { useRef, useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { zodResolver } from "@hookform/resolvers/zod";
import { loginSchema, type LoginFormData } from "../schemas/loginSchema";
//STYLES COMPONENTS
import AuthCard from "../components/AuthCard";
import SubmitButton from "../components/SubmitButton";
import InputField from "../components/InputField";
import FooterLink from "../components/FooterCard";
import Description from "../components/Description";
import BackgroundWrapper from "../components/BackgroundWrapper";
import { fetchAPI } from "../utils/api";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  // Set up states and routing for connection
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const nav = useNavigate();
  const isSubmitting = useRef(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

  const { refreshAuth } = useAuth();
  const onSubmit = async (data: LoginFormData) => {
    // prevents double submit
    if (loading || isSubmitting.current) return;

    isSubmitting.current = true;
    setLoading(true);
    setError("");

    try {
      const response = await fetchAPI("/auth/login", {
        method: "POST",
        headers: {
          // overrides the default set in api.ts
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams(data).toString(),
      });

      if (response.ok) {
        await new Promise((resolve) => setTimeout(resolve, 100));
        await refreshAuth();
        nav("/", { replace: true });
      } else {
        const errorData = await response.json();
        setError(errorData.message || "Invalid credentials");
      }
    } catch {
      setError("An error occurred while trying to log in, please try again.");
    } finally {
      setLoading(false);
      isSubmitting.current = false;
    }
  };

  return (
    <BackgroundWrapper>
      {/* Content */}
      <div
        id="login-content"
        className="h-screen w-screen z-10 flex flex-col lg:flex-row flex-1"
      >
        {/*  Logo & description  */}
        <div
          id="description-text"
          className="w-full lg:w-1/2 flex flex-col items-center justify-center p-6"
        >
          <Description
            descriptionTextJSX={
              <>
                🌿 <b>Plantastic</b> – The app that pampers your plants! 🌱
                <br />
                No more forgetting or overdoing it!
                <br />
                Receive <b>smart reminders</b>, tailored <b>advice</b>, and{" "}
                <b>fun facts</b>.
                <br />
                🍀 With <b>Plantastic</b>, grow your <b>indoor garden</b> 🍀
              </>
            }
          />
        </div>

        {/*  Form  */}
        <div
          id="login-card"
          className="w-full lg:w-1/2 flex flex-col items-center justify-center px-4 py-6"
        >
          <AuthCard title="Login">
            <form onSubmit={handleSubmit(onSubmit)} noValidate>
              <InputField
                label="Username or email"
                type="text"
                placeholder="Enter username or email"
                register={register("username")}
                error={errors.username}
              />

              <InputField
                label="Password"
                placeholder="Enter password"
                type="password"
                showPasswordToggle={true}
                register={register("password")}
                error={errors.password}
              />

              <SubmitButton loading={loading} text="Login" />

              <FooterLink
                text={
                  <>
                    Don't have an account?
                    <br />
                  </>
                }
                linkText="Register here"
                to="/signup"
              />
              {error && (
                <div
                  role="alert"
                  aria-live="assertive"
                  className="text-red-500 block mt-2"
                >
                  {error}
                </div>
              )}
            </form>
          </AuthCard>
        </div>
      </div>
    </BackgroundWrapper>
  );
}
