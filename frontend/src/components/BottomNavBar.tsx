import { PiPlantBold } from "react-icons/pi";
import { BiSolidBookBookmark } from "react-icons/bi";
import { HiOutlineLightBulb } from "react-icons/hi2";
import { useLocation, useNavigate } from "react-router";
import AddPlantForm from "./AddPlantForm";
import AddEncyclopediaPlantForm from "./AddEncyclopediaPlantForm";
import { useAuth } from "../context/AuthContext";
import FloatingAddButton from "./FloatingAddButton";
import { RiAddLargeLine } from "react-icons/ri";
import { LuDroplets } from "react-icons/lu";
import { WaterMultiplePlantsModal } from "./WaterMultiplePlantsModal";
import { ProfileMenu } from "./ProfileMenu";

interface BottomNavigationProps {
  onRefresh?: () => void;
}

export default function BottomNavigation({ onRefresh }: BottomNavigationProps) {
  const navigate = useNavigate();
  const location = useLocation();
  const { isAdmin } = useAuth();

  const isActive = (path: string) => location.pathname === path;

  const getButtonClass = (path: string) => {
    const baseClass =
      "group hover:-translate-y-1 transition-all duration-300 flex flex-col items-center justify-center gap-1";
    const activeClass = isActive(path) ? "text-yellow-200" : "";
    return `${baseClass} ${activeClass}`;
  };

  const getIconClass = (path: string) => {
    const baseClass =
      "w-8 h-8 md:w-10 md:h-10 lg:w-10 lg:h-10 xl:w-10 xl:h-10 2xl:w-14 2xl:h-14 transition-all group-hover:scale-110";
    const activeClass = isActive(path) ? "text-yellow-200 scale-110" : "";
    return `${baseClass} ${activeClass}`;
  };

  const textClass = "hidden sm:block sm:text-xs xl:text-sm 2xl:text-lg";

  return (
    <nav
      aria-label="Main navigation"
      id="navbar"
      className="fixed bottom-4 h-20 sm:h-24 2xl:h-28 left-4 right-4  xl:left-10 xl:right-10 2xl:right-12 2xl:left-12 grid grid-cols-2 gap-[4rem] text-white  mb-6 font-montserrat opacity-90 z-20"
    >
      <div
        id="right_navbar"
        className="relative z-20 bg-[#2D3D2D] grid grid-cols-2 place-items-center rounded-l-[2rem] pl-4"
      >
        <button
          id="digital-garden-button"
          onClick={() => navigate("/")}
          className={getButtonClass("/")}
          aria-label="digital garden"
        >
          <PiPlantBold className={getIconClass("/")} />
          <span className={textClass}>Digital garden</span>
        </button>
        <button
          onClick={() => navigate("/encyclopedia")}
          id="encyclopedia_button"
          className={getButtonClass("/encyclopedia")}
          aria-lable="encyclopedia"
        >
          <BiSolidBookBookmark className={getIconClass("/encyclopedia")} />
          <span className={textClass}>Encyclopedia</span>
        </button>
      </div>
      {isActive("/") && (
        <FloatingAddButton
          menuOptions={[
            {
              FormComponent: AddPlantForm,
              icon: RiAddLargeLine,
            },
            {
              FormComponent: WaterMultiplePlantsModal,
              icon: LuDroplets,
            },
          ]}
        />
      )}
      {/* ADD A PLANT TO ENCYCLOPEDIA BUTTON */}
      {isActive("/encyclopedia") && isAdmin && (
        <FloatingAddButton
          FormComponent={(props) => (
            <AddEncyclopediaPlantForm {...props} onRefresh={onRefresh} />
          )}
        ></FloatingAddButton>
      )}
      <div
        id="left_navbar"
        className="relative z-20 bg-[#2D3D2D] grid grid-cols-2 place-items-center rounded-r-[2rem] pr-4 "
      >
        <button
          id="advices_button"
          onClick={() => navigate("/advices")}
          className={getButtonClass("/advices")}
          aria-label="advices"
        >
          <HiOutlineLightBulb className={getIconClass("/advices")} />
          <span className={textClass}>Advices</span>
        </button>

        <ProfileMenu
          iconClassName={getIconClass("/profile")}
          textClassName={textClass}
        />
      </div>

      <div
        id="middle_navbar"
        className="
        absolute z-10 bottom-0 h-full left-1/2 -translate-x-1/2 w-1/2 bg-[#2D3D2D] pointer-events-none "
        aria-hidden="true"
      ></div>
    </nav>
  );
}
