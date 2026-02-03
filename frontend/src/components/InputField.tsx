import { useState } from "react";
import { type FieldError, type UseFormRegisterReturn } from "react-hook-form";
import { TbEye, TbEyeClosed } from "react-icons/tb";

type InputFieldProps = {
  label: string;
  type?: string;
  showPasswordToggle?: boolean;
  placeholder?: string;
  register: UseFormRegisterReturn;
  error?: FieldError;
  helperText?: string;
  max?: string;
};

export default function InputField({
  label,
  type,
  showPasswordToggle,
  placeholder,
  register,
  error,
  helperText,
  max,
}: InputFieldProps) {
  const [showPassword, setShowPassword] = useState(false);

  const inputType = showPasswordToggle
    ? showPassword
      ? "text"
      : "password"
    : type;

  return (
    <div className="mb-4">
      <label className="block text-sm font-bold text-text-secondary font-bellota mb-1">
        {label}
      </label>
      <div className="relative">
        <input
          type={inputType}
          placeholder={placeholder}
          {...register}
          className={`w-full p-2.5 text-sm rounded-lg focus:ring-focus focus:border-focus bg-gray-50 placeholder-text-placeholder
          ${showPasswordToggle ? "pr-10" : ""} ${
            error
              ? "border border-red-500 text-text-error"
              : "border border-gray-300 text-text-primary"
          }`}
          max={max}
        />
        {showPasswordToggle && (
          <button
            type="button"
            onClick={() => setShowPassword((prev) => !prev)}
            aria-label={showPassword ? "Hide password" : "Show password"}
            className="absolute right-2 top-1/2 -translate-y-1/2 text-text-placeholder hover:text-gray-700"
          >
            {showPassword ? (
              <TbEye className="w-5 h-5" />
            ) : (
              <TbEyeClosed className="w-5 h-5" />
            )}
          </button>
        )}
      </div>

      {error && <p className="text-text-error text-xs mt-1">{error.message}</p>}
      {helperText && !error && (
        <small
          id="helper-text"
          className="text-text-placeholder text-xs mt-1 block"
        >
          {helperText}
        </small>
      )}
    </div>
  );
}
