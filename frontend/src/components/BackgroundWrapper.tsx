import type { ReactNode } from "react";
import bgimg from "@/assets/img/bg-img2.jpg";

export default function BackgroundWrapper({
  children,
}: {
  children: ReactNode;
}) {
  return (
    <div
      className="w-full min-h-screen m-0 p-0 z-[-2]"
      style={{
        backgroundImage: `linear-gradient(rgba(37, 48, 37, 0.9), rgba(37, 48, 37, 0.9)), url(${bgimg})`,
        backgroundColor: "#253025",
        backgroundRepeat: "repeat",
        backgroundSize: "300px 300px",
        backgroundPosition: "0 0",
      }}
    >
      {children}
    </div>
  );
}
