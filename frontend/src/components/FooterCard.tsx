import type { ReactNode } from "react";
import { Link } from "react-router-dom";

type FooterLinkProps = {
  text: ReactNode;
  linkText: string;
  to: string;
};

export default function FooterLink({ text, linkText, to }: FooterLinkProps) {
  return (
    <div className="text-sm font-medium mt-2 text-text-placeholder font-montserrat text-center">
      <p>
        {text}{" "}
        <Link
          to={to}
          className="text-focus hover:underline font-montserrat text-base text-bold"
        >
          {linkText}
        </Link>
      </p>
    </div>
  );
}
