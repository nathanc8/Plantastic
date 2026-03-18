import { useState } from "react";
import LegalModal from "./LegalModal";

export default function LegalFooter() {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <>
      <footer className="fixed bottom-0 left-0 right-0 z-20 flex justify-center py-2 bg-transparent pointer-events-none">
        <button
          onClick={() => setIsOpen(true)}
          className="pointer-events-auto text-text-secondary text-xs underline hover:text-white transition-colors"
        >
          Legal Notice & Cookies
        </button>
      </footer>
      <LegalModal isOpen={isOpen} onClose={() => setIsOpen(false)} />
    </>
  );
}
