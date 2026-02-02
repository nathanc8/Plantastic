import logo from "@/assets/img/plantastic1_logo.png";
import { useNavigate } from "react-router";
import { Link } from "react-router-dom";

export const Header = () => {
  const navigate = useNavigate();

  return (
    <Link
      to="/"
      aria-label="Go to digital garden"
      className="focus:outline-none rounded inline-block w-fit"
    >
      <img
        onClick={() => navigate("/")}
        alt="Plantastic logo"
        src={logo}
        className="w-24 h-24 ml-4
          sm:w-24 sm:h-30 
          md:w-36 md:ml-1
          lg:w-40 lg:ml-1
          2xl:h-44 2xl:ml-6 
          object-contain"
      />
    </Link>
  );
};
