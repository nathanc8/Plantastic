import { useEffect, useRef } from "react";
import type { ModalProps } from "../types/ModalProps";
import { createPortal } from "react-dom";
import { FaWindowClose } from "react-icons/fa";

export default function Modal({
  isOpen,
  onClose,
  children,
  size = "md",
  focusOnFirstInput = false,
}: ModalProps) {
  const modalRef = useRef<HTMLDivElement>(null);
  const closeButtonRef = useRef<HTMLButtonElement>(null);

  // Automatic fonus on input field if form, close button if not
  useEffect(() => {
    if (isOpen && modalRef.current) {
      if (focusOnFirstInput) {
        const firstInput = modalRef.current.querySelector(
          'input:not([type="hidden"]), select, textarea',
        ) as HTMLElement;
        firstInput?.focus();
      } else {
        closeButtonRef.current?.focus();
      }
    }
  }, [isOpen, focusOnFirstInput]);

  // ESC key event
  useEffect(() => {
    const handleEsc = (event: KeyboardEvent) => {
      if (event.key === "Escape" && isOpen) {
        onClose();
      }
    };
    // Prevents body scroll when modal is open
    if (isOpen) {
      document.addEventListener("keydown", handleEsc);
      document.body.style.overflow = "hidden";
    }

    return () => {
      document.removeEventListener("keydown", handleEsc);
      document.body.style.overflow = "unset";
    };
  }, [isOpen, onClose]);

  // Prevents Focus trap
  useEffect(() => {
    const handleTab = (event: KeyboardEvent) => {
      if (event.key !== "Tab" || !modalRef.current) return;

      const focusableElements = modalRef.current.querySelectorAll(
        'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])',
      );
      const firstElement = focusableElements[0] as HTMLElement;
      const lastElement = focusableElements[
        focusableElements.length - 1
      ] as HTMLElement;

      if (event.shiftKey) {
        if (document.activeElement === firstElement) {
          lastElement?.focus();
          event.preventDefault();
        }
      } else {
        if (document.activeElement === lastElement) {
          firstElement?.focus();
          event.preventDefault();
        }
      }
    };

    if (isOpen) {
      document.addEventListener("keydown", handleTab);
    }
    return () => {
      document.removeEventListener("keydown", handleTab);
    };
  }, [isOpen]);

  if (!isOpen) return null;

  const sizeClasses = {
    sm: "max-w-md",
    md: "max-w-2xl",
    lg: "max-w-4xl",
    xl: "max-w-6xl",
  };

  return createPortal(
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50"
      onClick={onClose}
      role="presentation"
    >
      <div
        ref={modalRef}
        role="dialog"
        aria-modal="true"
        className={`bg-amber-50/95 rounded-lg p-4 sm:p-6 md:p-8 w-full ${sizeClasses[size]} max-h-[90vh] overflow-y-auto relative shadow-xl font-bellota mx-4 sm:mx-6`}
        onClick={(e) => e.stopPropagation()}
      >
        <button
          ref={closeButtonRef}
          aria-label="Close modal"
          className="absolute top-4 right-4 text-gray-500 hover:text-gray-700 focus:ring-2 focus:ring-blue-500 focus:outline-none rounded"
          onClick={onClose}
        >
          <FaWindowClose className="w-6 h-6" />
        </button>
        {children}
      </div>
    </div>,
    document.body,
  );
}
