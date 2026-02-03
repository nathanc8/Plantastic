import { useAuth } from "../context/AuthContext";
import { useState, useRef, useEffect } from "react";
import { PiUserCircleGearFill } from "react-icons/pi";
import { Backdrop } from "./Backdrop";
// import { useNavigate } from "react-router";

interface ProfileMenuProps {
  iconClassName: string;
  textClassName: string;
}

export const ProfileMenu = ({
  iconClassName,
  textClassName,
}: ProfileMenuProps) => {
  const { logout } = useAuth();
  const [isOpen, setIsOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);
  // const navigate = useNavigate();

  useEffect(() => {
    if (!isOpen) return;

    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [isOpen]);

  return (
    <>
      <Backdrop isOpen={isOpen} onClick={() => setIsOpen(false)} />
      <div ref={menuRef} className="relative z-20">
        <button
          onClick={() => setIsOpen(!isOpen)}
          className="flex flex-col items-center justify-center gap-1"
        >
          <PiUserCircleGearFill className={iconClassName} />
          <p className={textClassName}>Profile</p>
        </button>

        {isOpen && (
          <div
            className="absolute bottom-full mb-2 right-0 w-40 rounded-md shadow-lg bg-linen font-bellota ring-1 ring-black ring-opacity-5 z-30"
            role="menu"
          >
            <div className="py-1 flex flex-col items-center">
              <button
                onClick={() => setIsOpen(false)}
                className="block w-full text-center px-4 py-2 text-sm text-text-secondary hover:bg-gray-300"
              >
                Profile
              </button>
              <button
                onClick={() => setIsOpen(false)}
                className="block w-full text-center px-4 py-2 text-sm text-text-secondary hover:bg-gray-300"
              >
                Settings
              </button>
              <button
                onClick={logout}
                className="block  w-35 px-4 py-2 mt-4 bg-red-500 text-white rounded hover:bg-red-600"
              >
                Logout
              </button>
            </div>
          </div>
        )}
      </div>
    </>
  );
};
