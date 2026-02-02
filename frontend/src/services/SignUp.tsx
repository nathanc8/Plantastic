import React, { useState } from "react";
import { useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Link } from "react-router-dom";
import { signUpSchema, type SignUpFormData } from "../schemas/signUpSchema";
//STYLES COMPONENTS
import AuthCard from "../components/AuthCard";
import SubmitButton from "../components/SubmitButton";
import InputField from "../components/InputField";
import FooterLink from "../components/FooterCard";
import Description from "../components/Description";
import BackgroundWrapper from "../components/BackgroundWrapper";
import { fetchAPI } from "../utils/api";

const SignUp: React.FC = () => {
  const [apiMessage, setApiMessage] = useState<React.ReactNode>(null);
  const [loading, setLoading] = useState(false);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<SignUpFormData>({
    resolver: zodResolver(signUpSchema), // replaces submitHandler
  });

  const onSubmit: SubmitHandler<SignUpFormData> = async (
    formData: SignUpFormData,
  ) => {
    try {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const { confirmPassword, ...registerData } = formData;
      const response = await fetchAPI("/auth/register", {
        method: "POST",
        body: JSON.stringify(registerData),
      });

      const data = await response.text();

      if (response.status === 401) {
        setApiMessage("Missing or invalid token");
        return;
      }
      if (!response.ok) {
        setApiMessage(`Error: ${data || "Registration failed"}`);
        return;
      }
      const username = data.split(": ")[1] || registerData.username;
      setApiMessage(
        <>
          Welcome <strong>{username}</strong>! Registration successful. Please{" "}
          <Link to="/login">login</Link> to enjoy Plantastic 🌿
        </>,
      );
      reset();
    } catch (error) {
      console.error(error);
      setApiMessage("An error occured.");
    } finally {
      setLoading(false);
    }
  };

  // SIGN UP FORM
  return (
    <BackgroundWrapper>
      {/* Content */}
      <div className="h-screen w-screen relative z-10 flex flex-col lg:flex-row flex-1">
        {/*  Logo & description  */}
        <div className="w-full lg:w-1/2 flex flex-col items-center justify-center p-6">
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

        {/* Form */}
        <div className="w-full lg:w-1/2 flex flex-col items-center justify-center px-4 py-6">
          <AuthCard title="Sign up">
            <form onSubmit={handleSubmit(onSubmit)} noValidate>
              <InputField
                label="Email"
                type="text"
                placeholder="Enter your email"
                register={register("email", {
                  required: "email is required",
                })}
                error={errors.email}
              />
              <InputField
                label="Username"
                type="text"
                placeholder="Enter your username"
                register={register("username", {
                  required: "username is required",
                })}
                error={errors.username}
              />
              <InputField
                label="Password"
                type="password"
                showPasswordToggle={true}
                placeholder="Enter your password"
                register={register("password", {
                  required: "password is required",
                })}
                error={errors.password}
                helperText="Password must contain at least: one uppercase, one lowercase, one digit and one special character [#?!@$ %^&*-]"
              />
              <InputField
                label="Confirm password"
                type="password"
                showPasswordToggle={true}
                placeholder="Please confirm your password"
                register={register("confirmPassword", {
                  required: "Please confirm your password",
                })}
                error={errors.confirmPassword}
              />
              <SubmitButton loading={loading} text="Create an account" />

              <FooterLink
                text={
                  <>
                    Already have an account?
                    <br />
                  </>
                }
                linkText="Log in here"
                to="/login"
              />
            </form>
            {apiMessage && <p className="text-green-500 mt-2">{apiMessage}</p>}
          </AuthCard>
        </div>
      </div>
    </BackgroundWrapper>
  );
};

export default SignUp;
