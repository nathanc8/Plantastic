import { type ReactNode } from "react";

// React Props are like function arguments in JavaScript and attributes in HTML.
type AuthCardsProps = {
  title: string;
  children: ReactNode;
  className?: string;
};

export default function AuthCard({
  title,
  children,
  className,
}: AuthCardsProps) {
  return (
    <div
      className={`bg-amber-50/95 backdrop-blur-sm shadow-lg shadow-black/20 border border-[#4f674f] rounded-xl w-[90%] max-w-xs sm:max-w-sm p-2 sm:p-3 lg:p-6 mx-auto ${className || ""}`}
    >
      <h1 className="text-xl sm:text-2xl font-bold text-gray-900 font-montserrat mb-4 text-center">
        {title}
      </h1>
      {children}
    </div>
  );
}
