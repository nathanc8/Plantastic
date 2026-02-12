import logo from "@/assets/img/plantastic1_logo.png";
import { useNavigate } from "react-router";
import { Link } from "react-router-dom";

export const Header = () => {
  const navigate = useNavigate();

  return (
    <button
      onClick={() => navigate("/")}
      className="cursor-pointer"
      aria-label="Go back to home page"
    >
      <img
        src={logo}
        alt="Plantastic logo"
        className="w-24 h-24 ml-4 
          sm:w-24 sm:h-30 
          md:w-36 md:ml-1
          lg:w-40 lg:ml-1
          2xl:h-44 2xl:ml-6 
          object-contain"
      />
    </button>
  );
};
